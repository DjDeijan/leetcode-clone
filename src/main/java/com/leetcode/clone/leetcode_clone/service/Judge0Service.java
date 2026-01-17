package com.leetcode.clone.leetcode_clone.service;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.leetcode.clone.leetcode_clone.dto.Judge0.Judge0SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import com.leetcode.clone.leetcode_clone.repository.SubmissionRepository;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
import com.leetcode.clone.leetcode_clone.repository.TestResultRepository;
import com.leetcode.clone.leetcode_clone.service.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class Judge0Service {
   @Value("${judge0.ip}")
    private String judge0Ip;
    private final TaskRepository taskRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestResultRepository testResultRepository;
    private final SubmissionRepository submissionRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public String test(){
        return judge0Ip;
    }

    public String JudgeSubmission(Judge0SubmissionRequestDTO userSubmission){
        
         List<TestCase> testCases = testCaseRepository.findAllByTaskId(userSubmission.taskId());
         if (testCases == null || testCases.isEmpty()) {
        System.out.println("Aborting: No test cases found for task ID: " + userSubmission.taskId());
        return "No test cases found for this task.";
         }
         List<Map<String, Object>> submissions = new ArrayList<>();
         List<TestResult> results = new ArrayList<>();
         for (TestCase tc : testCases) {
            Map<String, Object> submission = new HashMap<>();
            submission.put("source_code", encode(userSubmission.sourceCode()));
            submission.put("language_id", Integer.parseInt(userSubmission.languageId()));
            submission.put("stdin", encode(tc.getInput()));
            submission.put("expected_output", encode(tc.getExpectedOutput()));
            submission.put("memory_limit",tc.getMemoryLimitKb());
            submission.put("time_limit", tc.getTimeLimitMs()/1000.0);
            submission.put("stack_limit", tc.getStackLimitKb());
            submissions.add(submission); 
            
            TestResult temp = TestResult.builder()
                .testCase(tc)
                .submission(submissionRepository.findById(userSubmission.submissionId()).orElse(null))
                .status("Pending")
                .judge0Token(("not generated yet"))
                .stdout(null)
                .err(null)
                .build();
            results.add(temp);
         }
         Map<String, Object> finalPayload = Map.of("submissions", submissions);  //json for the batch

         String url = judge0Ip + "/submissions/batch?base64_encoded=true";
         
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    try{
        String jsonRequest = objectMapper.writeValueAsString(finalPayload);
        System.out.println("EXACT JSON BEING SENT: " + jsonRequest);
        try{
        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
        ResponseEntity<List> response = restTemplate.postForEntity(url, request, List.class);
        List<Map<String, String>> tokens = response.getBody();
        
        System.out.println("Received tokens from Judge0: " + tokens);

        if (tokens != null && tokens.size() == results.size()) {
        for (int i = 0; i < results.size(); i++) {
            String realToken = tokens.get(i).get("token");
            results.get(i).setJudge0Token(realToken);
        }
        }

        }catch (Exception e) {
            System.err.println("Failed to connect to Judge0: " + e.getMessage());
        }
         
    } catch (Exception e){
        System.err.println("Failed to serialize payload to JSON: " + e.getMessage());
    }

        List<TestResult> completed = new ArrayList<>();
        while (results.size()>0) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
          
            e.printStackTrace();
        }
        List<String> tokensToCheck = new ArrayList<>();
                for(TestResult tr : results){
                    tokensToCheck.add(tr.getJudge0Token());
                }
            String getbatchurl = judge0Ip + "/submissions/batch?tokens=" + String.join(",", tokensToCheck) + "&base64_encoded=true&fields=*";
            try{
                ResponseEntity<Map> response = restTemplate.getForEntity(getbatchurl, Map.class);
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> batchResults = (List<Map<String, Object>>) body.get("submissions");
                if (batchResults != null) {
                for (Map<String, Object> res : batchResults) {
                    String token = (String) res.get("token");
                    String status = (String) ((Map<String, Object>) res.get("status")).get("description");
                    for (TestResult tr : results) {
                        if (tr.getJudge0Token().equals(token)) {
                            if (!status.equals("In Queue") && !status.equals("Processing")) {
                                tr.setStatus(status);
                                tr.setStdout(safeBase64Decode(res.get("stdout")));
                                tr.setErr(safeBase64Decode(res.get("stderr")));
                                completed.add(tr);
                            }
                            break;
                        }
                    }
                }
                }
                results.removeAll(completed);
            } catch (Exception e) {
                System.err.println("Error while fetching batch results: " + e.getMessage());
            }
        }
        int correct = 0;
        String errors = "";
        for (TestResult tr : completed) {
            testResultRepository.save(tr);
            if(tr.getStatus().equals("Accepted")){
                correct++;
            } else {
                errors += "Test case ID " + tr.getTestCase().getId() + " failed with status: " + tr.getStatus() + "\n";
            }
        }
        Submission sub = submissionRepository.findById(userSubmission.submissionId()).orElse(null);
        if(sub != null){
            sub.setGrade((correct * 100) / testCases.size() +"%");
            sub.setStatus("Judged.");
            sub.setErrors(errors);
            submissionRepository.save(sub);
        }
        var grade = (correct * 100) / testCases.size();

        return "Judged submission";
    }
   
    private String encode(String value) {
    if (value == null) return "";
    return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String safeBase64Decode(Object value) {
    if (value == null || !(value instanceof String)) {
        return "";
    }
    try {
        byte[] decodedBytes = Base64.getDecoder().decode((String) value);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
        return ""; 
    }
}
}