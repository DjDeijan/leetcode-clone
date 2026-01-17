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

import com.leetcode.clone.leetcode_clone.dto.Submissions.Judge0SubmissionDTO;
import com.leetcode.clone.leetcode_clone.dto.Submissions.UserSubmissionDTO;
import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
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
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public String test(){
        return judge0Ip;
    }

    public String JudgeSubmission(SubmissionRequestDTO userSubmission){
        
         List<TestCase> testCases = testCaseRepository.findAllByTaskId(userSubmission.taskId());
         if (testCases == null || testCases.isEmpty()) {
        System.out.println("Aborting: No test cases found for task ID: " + userSubmission.taskId());
        return "No test cases found for this task.";
         }
         List<Map<String, Object>> submissions = new ArrayList<>();
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

        }catch (Exception e) {
            System.err.println("Failed to connect to Judge0: " + e.getMessage());
        }
         
    } catch (Exception e){
        System.err.println("Failed to serialize payload to JSON: " + e.getMessage());
    }

        
    
        return "Judging submission: " + finalPayload.toString();
    }
    public String GetBatch(){
        return "Getting batch";
    }
    public String GetSingleSubmission(){
        return "Getting single submission";
    }

    private String JudgeBatch(List<Judge0SubmissionDTO> submissions){

        //buildvame submission kum judge za vseki testcase za taska i gi prashtame za ocenka



        return "Judging batch internal";
    }
    private String encode(String value) {
    if (value == null) return "";
    return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}