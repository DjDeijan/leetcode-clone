package com.leetcode.clone.leetcode_clone.dto.task;

import java.util.List;

import com.leetcode.clone.leetcode_clone.model.User;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        Long createdByUserId,
        List<Long> tagIds
) {}
