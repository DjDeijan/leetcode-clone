package com.leetcode.clone.leetcode_clone.dto.testResult;

public record TestResultResponseDTO(
        Long id,
        Long testCaseId,
        Long submissionId,
        String status,
        String judge0Token,
        String stdout,
        String err
) {}
