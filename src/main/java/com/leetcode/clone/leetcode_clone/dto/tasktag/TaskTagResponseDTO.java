package com.leetcode.clone.leetcode_clone.dto.tasktag;

public record TaskTagResponseDTO(
        Long taskId,
        Long tagId,
        String tagName,
        String taskTitle
) {}