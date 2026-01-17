package com.leetcode.clone.leetcode_clone.dto.testResult;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record TestResultRequestDTO(
        @NotNull(message = "Submission ID is required")
        Long submissionId,

        @NotNull(message = "Test case ID is required")
        Long testCaseId,

        @NotBlank(message = "Status is required")
        String status,

        String judge0Token,

        String stdout,

        String err) {}
