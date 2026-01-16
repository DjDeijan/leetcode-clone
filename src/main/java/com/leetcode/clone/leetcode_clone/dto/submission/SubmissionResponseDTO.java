package com.leetcode.clone.leetcode_clone.dto.submission;

public record SubmissionResponseDTO(
        Long id,
        Long taskId,
        String sourceCode,
        String languageId,
        String status,
        String grade,
        String errors,
        Long userId
) {}