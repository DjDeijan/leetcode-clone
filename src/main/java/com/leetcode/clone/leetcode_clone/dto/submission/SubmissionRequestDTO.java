package com.leetcode.clone.leetcode_clone.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmissionRequestDTO(
        @NotNull Long taskId,
        @NotBlank String sourceCode,
        @NotBlank String languageId,
        @NotNull Long userId
) {}