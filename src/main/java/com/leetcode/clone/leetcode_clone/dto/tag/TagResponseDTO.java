package com.leetcode.clone.leetcode_clone.dto.tag;

import java.util.List;

public record TagResponseDTO(
        Long id,
        String name,
        List<TaskSummaryDTO> tasks
) {}