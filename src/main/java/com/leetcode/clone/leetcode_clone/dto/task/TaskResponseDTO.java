package com.leetcode.clone.leetcode_clone.dto.task;

import java.util.List;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        Long createdByUserId,
        List<Long> tagIds
) {}
