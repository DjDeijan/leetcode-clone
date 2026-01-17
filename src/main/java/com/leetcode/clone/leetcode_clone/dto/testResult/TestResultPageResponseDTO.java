package com.leetcode.clone.leetcode_clone.dto.testResult;

import java.util.List;

public record TestResultPageResponseDTO(
        List<TestResultResponseDTO> testResults,
        int totalPages,
        long totalElements,
        int page,
        int size
) {}
