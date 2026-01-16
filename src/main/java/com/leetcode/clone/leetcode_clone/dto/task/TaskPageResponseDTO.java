package com.leetcode.clone.leetcode_clone.dto.task;

import java.util.List;

public record TaskPageResponseDTO(
        List<TaskResponseDTO> tasks,
        int totalPages,
        long totalElements,
        int pageNumber,
        int pageSize
) {}
