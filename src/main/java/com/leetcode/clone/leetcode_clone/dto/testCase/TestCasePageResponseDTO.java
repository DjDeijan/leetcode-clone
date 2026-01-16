package com.leetcode.clone.leetcode_clone.dto.testCase;

import java.util.List;

public record TestCasePageResponseDTO(
        List<TestCaseResponseDTO> testcases,
        int totalPages,
        long totalElements,
        int pageNumber,
        int pageSize
) {}

