package com.leetcode.clone.leetcode_clone.dto.testResult;
) {}
        String err
        String stdout,
        String judge0Token,

        String status,
        @NotBlank(message = "Status is required")

        Long submissionId,
        @NotNull(message = "Submission ID is required")

        Long testCaseId,
        @NotNull(message = "Test case ID is required")
public record TestResultRequestDTO(

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

