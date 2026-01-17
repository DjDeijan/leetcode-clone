package com.leetcode.clone.leetcode_clone.dto.testCase;

public record TestCaseResponseDTO(
        Long id,
        Long taskId,
        String input,
        String expectedOutput,
        Integer timeLimitMs,
        Integer memoryLimitKb,
        Integer stackLimitKb
) {}

