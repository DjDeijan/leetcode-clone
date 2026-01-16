package com.leetcode.clone.leetcode_clone.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TaskRequestDTO(
        @NotBlank @Size(min = 2, max = 200) String title,
        @Size(max = 10_000) String description,
        //@Positive Long createdByUserId,
        List<@Positive Long> tagIds

) {}
