package com.leetcode.clone.leetcode_clone.dto.testCase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TestCaseRequestDTO(
        @NotBlank String input,
        @NotBlank String expectedOutput,
        @Positive Integer timeLimitMs,
        @Positive Integer memoryLimitKb,
        @Positive Integer stackLimitKb
) {}

