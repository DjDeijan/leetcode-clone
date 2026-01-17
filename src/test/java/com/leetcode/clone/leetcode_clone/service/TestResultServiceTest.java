package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TestResultMapper;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
import com.leetcode.clone.leetcode_clone.repository.TestResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestResultServiceTest {

    private TestResultRepository testResultRepository;
    private TestResultService testResultService;
    private TestResultMapper testResultMapper;
    private SubmissionService submissionService;
    private TestCaseRepository testCaseRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        testResultRepository = mock(TestResultRepository.class);
        testResultMapper = mock(TestResultMapper.class);
        submissionService = mock(SubmissionService.class);
        testCaseRepository = mock(TestCaseRepository.class);
        taskService = mock(TaskService.class);

        testResultService = new TestResultService(
                testResultRepository,
                testCaseRepository,
                testResultMapper,
                submissionService,
                taskService
        );
    }

    // ============= getTestResultOrThrow Tests =============
    @Test
    void getTestResultOrThrow_returnsTestResult_whenFound() {
        // Arrange
        TestResult testResult = TestResult.builder()
                .id(1L)
                .status("Accepted")
                .build();

        when(testResultRepository.findById(1L)).thenReturn(Optional.of(testResult));

        // Act
        TestResult result = testResultService.getTestResultOrThrow(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testResult.getId(), result.getId());
        assertEquals(testResult.getStatus(), result.getStatus());

        verify(testResultRepository, times(1)).findById(1L);
    }

    @Test
    void getTestResultOrThrow_throwsException_whenNotFound() {
        // Arrange
        when(testResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> testResultService.getTestResultOrThrow(99L));

        verify(testResultRepository, times(1)).findById(99L);
    }
    // =================================================


    // ============= createTestResult Tests =============
    @Test
    void createTestResult_success() {
        // Arrange
        TestResultRequestDTO request = new TestResultRequestDTO(
                1L,
                2L,
                "Accepted",
                "token-123",
                "Output: 5",
                null
        );

        TestResult testResultMapped = TestResult.builder()
                .status("Accepted")
                .judge0Token("token-123")
                .stdout("Output: 5")
                .build();

        Submission submission = Submission.builder()
                .id(2L)
                .build();

        TestCase testCase = TestCase.builder()
                .id(1L)
                .build();

        TestResult savedTestResult = TestResult.builder()
                .id(1L)
                .submission(submission)
                .testCase(testCase)
                .status("Accepted")
                .judge0Token("token-123")
                .stdout("Output: 5")
                .build();

        // Stub Mapper
        when(testResultMapper.toTestResult(request)).thenReturn(testResultMapped);

        // Stub Submission Service
        when(submissionService.getSubmissionOrThrow(2L)).thenReturn(submission);

        // Stub Test Case Repository
        when(testCaseRepository.findById(1L)).thenReturn(Optional.of(testCase));

        // Stub Repository Save
        when(testResultRepository.save(any(TestResult.class))).thenReturn(savedTestResult);

        // Act
        TestResult result = testResultService.createTestResult(request);

        // Assert
        assertNotNull(result);
        assertEquals(savedTestResult.getId(), result.getId());
        assertEquals("Accepted", result.getStatus());

        verify(testResultRepository, times(1)).save(any(TestResult.class));
        verify(submissionService, times(1)).getSubmissionOrThrow(2L);
        verify(testCaseRepository, times(1)).findById(1L);
    }
    // =================================================


    // ============= getTestResultsBySubmission Tests =============
    @Test
    void getTestResultsBySubmission_returnsResults() {
        // Arrange
        List<TestResult> testResults = List.of(
                TestResult.builder().id(1L).status("Accepted").build(),
                TestResult.builder().id(2L).status("Wrong Answer").build()
        );

        when(testResultRepository.findBySubmissionId(1L)).thenReturn(testResults);

        // Act
        List<TestResult> results = testResultService.getTestResultsBySubmission(1L);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(testResultRepository, times(1)).findBySubmissionId(1L);
    }
    // =================================================


    // ============= deleteTestResult Tests =============
    @Test
    void deleteTestResult_success() {
        // Arrange
        when(testResultRepository.existsById(1L)).thenReturn(true);

        // Act
        testResultService.deleteTestResult(1L);

        // Assert
        verify(testResultRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTestResult_throwsException_whenNotFound() {
        // Arrange
        when(testResultRepository.existsById(99L)).thenReturn(false);

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> testResultService.deleteTestResult(99L));

        verify(testResultRepository, never()).deleteById(99L);
    }
    // =================================================
}
