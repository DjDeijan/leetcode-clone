package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TestResultMapper;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import com.leetcode.clone.leetcode_clone.service.TestResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/test-results")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Test Result Controller", description = "Operations for managing test results")
public class TestResultController {

    private final TestResultService testResultService;
    private final TestResultMapper testResultMapper;

    @Operation(summary = "Create a new test result")
    @PostMapping
    public ResponseEntity<TestResultResponseDTO> createTestResult(
            @Valid @RequestBody TestResultRequestDTO testResultRequestDTO) {
        TestResult testResult = testResultService.createTestResult(testResultRequestDTO);
        return new ResponseEntity<>(testResultMapper.toResponseDTO(testResult), HttpStatus.CREATED);
    }

    @Operation(summary = "Get test result by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TestResultResponseDTO> getTestResultById(@PathVariable @Positive Long id) {
        TestResult testResult = testResultService.getTestResultOrThrow(id);
        return ResponseEntity.ok(testResultMapper.toResponseDTO(testResult));
    }

    @Operation(summary = "Get all test results with pagination")
    @GetMapping
    public ResponseEntity<TestResultPageResponseDTO> getAllTestResults(
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<TestResult> page = testResultService.getAllTestResults(pageable);
        return ResponseEntity.ok(testResultMapper.toPageResponseDTO(page));
    }

    @Operation(summary = "Get test results by submission ID")
    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<List<TestResultResponseDTO>> getTestResultsBySubmission(
            @PathVariable @Positive Long submissionId) {
        List<TestResult> testResults = testResultService.getTestResultsBySubmission(submissionId);
        return ResponseEntity.ok(testResultMapper.toResponseDTOList(testResults));
    }

    @Operation(summary = "Get test results by test case ID")
    @GetMapping("/test-case/{testCaseId}")
    public ResponseEntity<List<TestResultResponseDTO>> getTestResultsByTestCase(
            @PathVariable @Positive Long testCaseId) {
        List<TestResult> testResults = testResultService.getTestResultsByTestCase(testCaseId);
        return ResponseEntity.ok(testResultMapper.toResponseDTOList(testResults));
    }

    @Operation(summary = "Get test results by status")
    @GetMapping("/status")
    public ResponseEntity<List<TestResultResponseDTO>> getTestResultsByStatus(
            @RequestParam @NotBlank String status) {
        List<TestResult> testResults = testResultService.getTestResultsByStatus(status);
        return ResponseEntity.ok(testResultMapper.toResponseDTOList(testResults));
    }

    @Operation(summary = "Update test result")
    @PutMapping("/{id}")
    public ResponseEntity<TestResultResponseDTO> updateTestResult(
            @PathVariable @Positive Long id,
            @Valid @RequestBody TestResultRequestDTO testResultRequestDTO) {
        TestResult updatedTestResult = testResultService.updateTestResult(id, testResultRequestDTO);
        return ResponseEntity.ok(testResultMapper.toResponseDTO(updatedTestResult));
    }

    @Operation(summary = "Delete test result")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestResult(@PathVariable @Positive Long id) {
        testResultService.deleteTestResult(id);
        return ResponseEntity.noContent().build();
    }
}
