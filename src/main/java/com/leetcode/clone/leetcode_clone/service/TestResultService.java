package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TestResultMapper;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import com.leetcode.clone.leetcode_clone.repository.SubmissionRepository;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
import com.leetcode.clone.leetcode_clone.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TestResultService {

    private final TestResultRepository testResultRepository;
    private final TestResultMapper testResultMapper;
    private final SubmissionService submissionService;
    private final TaskService taskService;
    private final TestCaseRepository testCaseRepository;

    @Transactional
    public TestResult createTestResult(TestResultRequestDTO dto) {
        log.debug("Creating test result for submission={}, testCase={}", dto.submissionId(), dto.testCaseId());

        TestResult testResult = testResultMapper.toTestResult(dto);

        // Fetch and set the submission
        Submission submission = submissionService.getSubmissionOrThrow(dto.submissionId());
        testResult.setSubmission(submission);

        // Fetch and set the test case
        TestCase testCase = testCaseRepository.findById(dto.testCaseId())
                .orElseThrow(() -> new ResourceNotFoundException(TestCase.class, dto.testCaseId()));
        testResult.setTestCase(testCase);

        log.info("Test result created with status: {}", dto.status());
        return testResultRepository.save(testResult);
    }

    public TestResult getTestResultOrThrow(Long id) {
        return testResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TestResult.class, id));
    }

    public Page<TestResult> getAllTestResults(Pageable pageable) {
        return testResultRepository.findAll(pageable);
    }

    public List<TestResult> getTestResultsBySubmission(Long submissionId) {
        return testResultRepository.findBySubmissionId(submissionId);
    }

    public List<TestResult> getTestResultsByTestCase(Long testCaseId) {
        return testResultRepository.findByTestCaseId(testCaseId);
    }

    public List<TestResult> getTestResultsByStatus(String status) {
        return testResultRepository.findByStatus(status);
    }

    @Transactional
    public TestResult updateTestResult(Long id, TestResultRequestDTO dto) {
        TestResult existingTestResult = getTestResultOrThrow(id);

        testResultMapper.updateFromRequestDTO(existingTestResult, dto);

        // Update submission if changed
        if (!existingTestResult.getSubmission().getId().equals(dto.submissionId())) {
            Submission submission = submissionService.getSubmissionOrThrow(dto.submissionId());
            existingTestResult.setSubmission(submission);
        }

        // Update test case if changed
        if (!existingTestResult.getTestCase().getId().equals(dto.testCaseId())) {
            TestCase testCase = testCaseRepository.findById(dto.testCaseId())
                    .orElseThrow(() -> new ResourceNotFoundException(TestCase.class, dto.testCaseId()));
            existingTestResult.setTestCase(testCase);
        }

        return testResultRepository.save(existingTestResult);
    }

    @Transactional
    public void deleteTestResult(Long id) {
        if (testResultRepository.existsById(id)) {
            testResultRepository.deleteById(id);
            log.info("Test result with id={} deleted", id);
        } else {
            throw new ResourceNotFoundException(TestResult.class, id);
        }
    }
}
