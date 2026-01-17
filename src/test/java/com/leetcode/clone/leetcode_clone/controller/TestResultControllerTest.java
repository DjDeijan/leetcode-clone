package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TestResultMapper;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import com.leetcode.clone.leetcode_clone.service.TestResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestResultControllerTest {

    private TestResultService testResultService;
    private TestResultMapper testResultMapper;
    private TestResultController testResultController;

    @BeforeEach
    void setUp() {
        testResultService = mock(TestResultService.class);
        testResultMapper = mock(TestResultMapper.class);
        testResultController = new TestResultController(testResultService, testResultMapper);
    }

    // ============= createTestResult Tests =============
    @Test
    void createTestResult_success() {
        // Arrange
        TestResultRequestDTO requestDTO = new TestResultRequestDTO(
                1L,
                2L,
                "Accepted",
                "token-123",
                "Output: 5",
                null
        );

        TestResult testResult = TestResult.builder()
                .id(1L)
                .status("Accepted")
                .build();

        TestResultResponseDTO responseDTO = new TestResultResponseDTO(
                1L,
                1L,
                2L,
                "Accepted",
                "token-123",
                "Output: 5",
                null
        );

        when(testResultService.createTestResult(requestDTO)).thenReturn(testResult);
        when(testResultMapper.toResponseDTO(testResult)).thenReturn(responseDTO);

        // Act
        ResponseEntity<TestResultResponseDTO> response = testResultController.createTestResult(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());

        verify(testResultService, times(1)).createTestResult(requestDTO);
    }
    // =================================================


    // ============= getTestResultById Tests =============
    @Test
    void getTestResultById_success() {
        // Arrange
        TestResult testResult = TestResult.builder()
                .id(1L)
                .status("Accepted")
                .build();

        TestResultResponseDTO responseDTO = new TestResultResponseDTO(
                1L,
                1L,
                2L,
                "Accepted",
                "token-123",
                "Output: 5",
                null
        );

        when(testResultService.getTestResultOrThrow(1L)).thenReturn(testResult);
        when(testResultMapper.toResponseDTO(testResult)).thenReturn(responseDTO);

        // Act
        ResponseEntity<TestResultResponseDTO> response = testResultController.getTestResultById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());

        verify(testResultService, times(1)).getTestResultOrThrow(1L);
    }
    // =================================================


    // ============= getTestResultsBySubmission Tests =============
    @Test
    void getTestResultsBySubmission_success() {
        // Arrange
        List<TestResult> testResults = List.of(
                TestResult.builder().id(1L).status("Accepted").build(),
                TestResult.builder().id(2L).status("Wrong Answer").build()
        );

        List<TestResultResponseDTO> responseDTOs = List.of(
                new TestResultResponseDTO(1L, 1L, 1L, "Accepted", "token-1", "Output: 5", null),
                new TestResultResponseDTO(2L, 2L, 1L, "Wrong Answer", "token-2", "Output: 3", "Expected 5")
        );

        when(testResultService.getTestResultsBySubmission(1L)).thenReturn(testResults);
        when(testResultMapper.toResponseDTOList(testResults)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<TestResultResponseDTO>> response =
                testResultController.getTestResultsBySubmission(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        verify(testResultService, times(1)).getTestResultsBySubmission(1L);
    }
    // =================================================


    // ============= deleteTestResult Tests =============
    @Test
    void deleteTestResult_success() {
        // Arrange
        doNothing().when(testResultService).deleteTestResult(1L);

        // Act
        ResponseEntity<Void> response = testResultController.deleteTestResult(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(testResultService, times(1)).deleteTestResult(1L);
    }
    // =================================================
}
