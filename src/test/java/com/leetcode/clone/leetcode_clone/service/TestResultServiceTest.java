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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
                2L,
                1L,
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

    // ============= getAllTestResults Tests =============
    @Test
    void getAllTestResults_returnsPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<TestResult> testResults = List.of(
                TestResult.builder().id(1L).status("Accepted").build(),
                TestResult.builder().id(2L).status("Wrong Answer").build()
        );
        Page<TestResult> page = new PageImpl<>(testResults, pageable, 2);

        when(testResultRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<TestResult> result = testResultService.getAllTestResults(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        verify(testResultRepository, times(1)).findAll(pageable);
    }
    // =================================================


    // ============= getTestResultsByTestCase Tests =============
    @Test
    void getTestResultsByTestCase_returnsResults() {
        // Arrange
        List<TestResult> testResults = List.of(
                TestResult.builder().id(1L).status("Accepted").build(),
                TestResult.builder().id(2L).status("Accepted").build()
        );

        when(testResultRepository.findByTestCaseId(1L)).thenReturn(testResults);

        // Act
        List<TestResult> results = testResultService.getTestResultsByTestCase(1L);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(testResultRepository, times(1)).findByTestCaseId(1L);
    }

    @Test
    void getTestResultsByTestCase_returnsEmptyList_whenNoResults() {
        // Arrange
        when(testResultRepository.findByTestCaseId(999L)).thenReturn(List.of());

        // Act
        List<TestResult> results = testResultService.getTestResultsByTestCase(999L);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(testResultRepository, times(1)).findByTestCaseId(999L);
    }
    // =================================================


    // ============= getTestResultsByStatus Tests =============
    @Test
    void getTestResultsByStatus_returnsResults() {
        // Arrange
        List<TestResult> testResults = List.of(
                TestResult.builder().id(1L).status("Accepted").build(),
                TestResult.builder().id(2L).status("Accepted").build()
        );

        when(testResultRepository.findByStatus("Accepted")).thenReturn(testResults);

        // Act
        List<TestResult> results = testResultService.getTestResultsByStatus("Accepted");

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(testResultRepository, times(1)).findByStatus("Accepted");
    }

    @Test
    void getTestResultsByStatus_returnsEmptyList_whenNoResults() {
        // Arrange
        when(testResultRepository.findByStatus("Time Limit Exceeded")).thenReturn(List.of());

        // Act
        List<TestResult> results = testResultService.getTestResultsByStatus("Time Limit Exceeded");

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(testResultRepository, times(1)).findByStatus("Time Limit Exceeded");
    }
    // =================================================


    // ============= updateTestResult Tests =============
    @Test
    void updateTestResult_success() {
        // Arrange
        TestResultRequestDTO updateDTO = new TestResultRequestDTO(
                1L,
                1L,
                "Accepted",
                "token-updated",
                "Updated output",
                null
        );

        TestCase testCase = TestCase.builder().id(1L).build();
        Submission submission = Submission.builder().id(2L).build();

        TestResult existingTestResult = TestResult.builder()
                .id(1L)
                .testCase(testCase)
                .submission(submission)
                .status("Processing")
                .judge0Token("token-old")
                .build();

        TestResult updatedTestResult = TestResult.builder()
                .id(1L)
                .testCase(testCase)
                .submission(submission)
                .status("Accepted")
                .judge0Token("token-updated")
                .stdout("Updated output")
                .build();

        when(testResultRepository.findById(1L)).thenReturn(Optional.of(existingTestResult));
        when(submissionService.getSubmissionOrThrow(2L)).thenReturn(submission);
        when(testResultRepository.save(any(TestResult.class))).thenReturn(updatedTestResult);

        // Act
        TestResult result = testResultService.updateTestResult(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Accepted", result.getStatus());
        assertEquals("token-updated", result.getJudge0Token());
        verify(testResultMapper, times(1)).updateFromRequestDTO(existingTestResult, updateDTO);
        verify(testResultRepository, times(1)).save(existingTestResult);
    }

    @Test
    void updateTestResult_throwsException_whenNotFound() {
        // Arrange
        TestResultRequestDTO updateDTO = new TestResultRequestDTO(
                1L, 2L, "Accepted", "token", "output", null
        );

        when(testResultRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> testResultService.updateTestResult(999L, updateDTO));

        verify(testResultRepository, never()).save(any(TestResult.class));
    }

    @Test
    void updateTestResult_updatesSubmission_whenChanged() {
        // Arrange
        TestResultRequestDTO updateDTO = new TestResultRequestDTO(
                3L, // Use 3L here to trigger the change logic
                1L,
                "Accepted",
                "token",
                "output",
                null
        );

        TestCase testCase = TestCase.builder().id(1L).build();
        Submission oldSubmission = Submission.builder().id(1L).build();
        Submission newSubmission = Submission.builder().id(3L).build();

        TestResult existingTestResult = TestResult.builder()
                .id(1L)
                .testCase(testCase)
                .submission(oldSubmission)
                .status("Processing")
                .build();

        when(testResultRepository.findById(1L)).thenReturn(Optional.of(existingTestResult));
        when(submissionService.getSubmissionOrThrow(3L)).thenReturn(newSubmission); // Stub for 3L
        when(testResultRepository.save(any(TestResult.class))).thenReturn(existingTestResult);

        // Act
        testResultService.updateTestResult(1L, updateDTO);

        // Assert
        verify(submissionService, times(1)).getSubmissionOrThrow(3L);
        verify(testResultRepository, times(1)).save(existingTestResult);
    }
    // =================================================
}
