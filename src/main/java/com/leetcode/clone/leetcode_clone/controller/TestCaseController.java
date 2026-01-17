package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.testCase.TestCasePageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TestCaseMapper;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.service.TestCaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testcases")
@RequiredArgsConstructor
@Validated
public class TestCaseController {

    private final TestCaseService testCaseService;
    private final TestCaseMapper testCaseMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseResponseDTO> getTestCaseById(@PathVariable @Positive Long id) {
        TestCase fetched = testCaseService.getTestCaseOrThrow(id);
        return new ResponseEntity<>(testCaseMapper.toResponseDTO(fetched), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<TestCasePageResponseDTO> getAllTestCases(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<TestCase> page = testCaseService.getAllTestCases(pageable);
        return new ResponseEntity<>(testCaseMapper.toTestCasePageResponseDTO(page), HttpStatus.OK);
    }

    @GetMapping("/filter/taskId")
    public ResponseEntity<TestCasePageResponseDTO> getAllTestCasesByTaskId(
            @RequestParam @Positive Long taskId,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<TestCase> page = testCaseService.getAllTestCasesByTaskId(taskId, pageable);
        return new ResponseEntity<>(testCaseMapper.toTestCasePageResponseDTO(page), HttpStatus.OK);
    }

    @PostMapping("/tasks/{taskId}/test-cases")
    public ResponseEntity<TestCaseResponseDTO> create(
            @PathVariable Long taskId,
            @Valid @RequestBody TestCaseRequestDTO dto
    ) {
        TestCase testCase = testCaseService.createTestCase(taskId, dto);
        return ResponseEntity.ok(testCaseMapper.toResponseDTO(testCase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable @Positive Long id) {
        testCaseService.deleteTestCase(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCaseResponseDTO> putTestCase(
            @PathVariable @Positive Long id,
            @Valid @RequestBody TestCaseRequestDTO testcaseRequestDTO
    ) {
        TestCase updated = testCaseService.putTestCase(id, testcaseRequestDTO);
        return new ResponseEntity<>(testCaseMapper.toResponseDTO(updated), HttpStatus.OK);
    }

    @PatchMapping("/{id}/expectedOutput")
    public ResponseEntity<TestCaseResponseDTO> patchExpectedOutput(
            @PathVariable @Positive Long id,
            @RequestParam @NotBlank @Size(max = 100_000) String newExpectedOutput
    ) {
        TestCase patched = testCaseService.patchExpectedOutput(id, newExpectedOutput);
        return new ResponseEntity<>(testCaseMapper.toResponseDTO(patched), HttpStatus.OK);
    }
}

