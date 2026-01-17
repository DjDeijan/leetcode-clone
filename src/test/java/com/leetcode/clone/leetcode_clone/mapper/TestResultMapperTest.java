package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestResultMapperTest {

    private final TestResultMapper testResultMapper = Mappers.getMapper(TestResultMapper.class);

    // ============= toTestResultResponseDTO Tests =============
    @Test
    void toTestResultResponseDTO_mapsCorrectly() {
        // Arrange
        TestCase testCase = TestCase.builder()
                .id(1L)
                .build();

        Submission submission = Submission.builder()
                .id(2L)
                .build();

        TestResult testResult = TestResult.builder()
                .id(1L)
                .testCase(testCase)
                .submission(submission)
                .status("Accepted")
                .judge0Token("token-123")
                .stdout("Output: 5")
                .err(null)
                .build();

        // Act
        TestResultResponseDTO dto = testResultMapper.toResponseDTO(testResult);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals(1L, dto.testCaseId());
        assertEquals(2L, dto.submissionId());
        assertEquals("Accepted", dto.status());
        assertEquals("token-123", dto.judge0Token());
        assertEquals("Output: 5", dto.stdout());
        assertNull(dto.err());
    }
    // =================================================


    // ============= toTestResultResponseDTOList Tests =============
    @Test
    void toTestResultResponseDTOList_mapsCorrectly() {
        // Arrange
        TestCase testCase = TestCase.builder().id(1L).build();
        Submission submission = Submission.builder().id(2L).build();

        List<TestResult> testResults = List.of(
                TestResult.builder()
                        .id(1L)
                        .testCase(testCase)
                        .submission(submission)
                        .status("Accepted")
                        .build(),
                TestResult.builder()
                        .id(2L)
                        .testCase(testCase)
                        .submission(submission)
                        .status("Wrong Answer")
                        .build()
        );

        // Act
        List<TestResultResponseDTO> dtos = testResultMapper.toResponseDTOList(testResults);

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Accepted", dtos.get(0).status());
        assertEquals("Wrong Answer", dtos.get(1).status());
    }
    // =================================================


    // ============= toTestResultPageResponseDTO Tests =============
    @Test
    void toTestResultPageResponseDTO_mapsCorrectly() {
        // Arrange
        TestCase testCase = TestCase.builder().id(1L).build();
        Submission submission = Submission.builder().id(2L).build();

        List<TestResult> testResults = List.of(
                TestResult.builder()
                        .id(1L)
                        .testCase(testCase)
                        .submission(submission)
                        .status("Accepted")
                        .build()
        );

        Page<TestResult> page = new PageImpl<>(testResults, PageRequest.of(0, 10), 1);

        // Act
        TestResultPageResponseDTO dto = testResultMapper.toPageResponseDTO(page);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.testResults().size());
        assertEquals(1, dto.totalPages());
        assertEquals(1, dto.totalElements());
        assertEquals(0, dto.page());
        assertEquals(10, dto.size());
    }
    // =================================================


    // ============= toTestResult Tests =============
    @Test
    void toTestResult_mapsCorrectly() {
        // Arrange
        TestResultRequestDTO requestDTO = new TestResultRequestDTO(
                1L,
                2L,
                "Accepted",
                "token-123",
                "Output: 5",
                null
        );

        // Act
        TestResult testResult = testResultMapper.toTestResult(requestDTO);

        // Assert
        assertNotNull(testResult);
        assertNull(testResult.getId()); // Should be ignored
        assertNull(testResult.getTestCase()); // Should be ignored
        assertNull(testResult.getSubmission()); // Should be ignored
        assertEquals("Accepted", testResult.getStatus());
        assertEquals("token-123", testResult.getJudge0Token());
        assertEquals("Output: 5", testResult.getStdout());
        assertNull(testResult.getErr());
    }
    // =================================================

    // ============= updateFromRequestDTO Tests =============
    @Test
    void updateFromRequestDTO_updatesFieldsCorrectly() {
        // Arrange
        TestCase testCase = TestCase.builder().id(1L).build();
        Submission submission = Submission.builder().id(2L).build();

        TestResult existingTestResult = TestResult.builder()
                .id(1L)
                .testCase(testCase)
                .submission(submission)
                .status("Processing")
                .judge0Token("old-token")
                .stdout("Old output")
                .err(null)
                .build();

        TestResultRequestDTO updateDTO = new TestResultRequestDTO(
                1L,
                2L,
                "Accepted",
                "new-token",
                "New output",
                null
        );

        // Act
        testResultMapper.updateFromRequestDTO(existingTestResult, updateDTO);

        // Assert
        assertEquals(1L, existingTestResult.getId()); // Should remain unchanged
        assertEquals("Accepted", existingTestResult.getStatus()); // Should be updated
        assertEquals("new-token", existingTestResult.getJudge0Token()); // Should be updated
        assertEquals("New output", existingTestResult.getStdout()); // Should be updated
        assertNull(existingTestResult.getErr());
    }

    @Test
    void updateFromRequestDTO_updatesWithErrorMessage() {
        // Arrange
        TestCase testCase = TestCase.builder().id(1L).build();
        Submission submission = Submission.builder().id(2L).build();

        TestResult existingTestResult = TestResult.builder()
                .id(1L)
                .testCase(testCase)
                .submission(submission)
                .status("Accepted")
                .judge0Token("token")
                .stdout("Output: 5")
                .err(null)
                .build();

        TestResultRequestDTO updateDTO = new TestResultRequestDTO(
                1L,
                2L,
                "Runtime Error",
                "token-error",
                null,
                "NullPointerException at line 10"
        );

        // Act
        testResultMapper.updateFromRequestDTO(existingTestResult, updateDTO);

        // Assert
        assertEquals("Runtime Error", existingTestResult.getStatus());
        assertEquals("token-error", existingTestResult.getJudge0Token());
        assertNull(existingTestResult.getStdout());
        assertEquals("NullPointerException at line 10", existingTestResult.getErr());
    }
    // =================================================
}
