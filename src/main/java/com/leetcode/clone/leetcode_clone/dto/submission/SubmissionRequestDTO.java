package com.leetcode.clone.leetcode_clone.dto.submission;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmissionRequestDTO(
        @NotNull @JsonProperty("task_id") Long taskId,
        @NotBlank @JsonProperty("source_code") String sourceCode,
        @NotBlank @JsonProperty("language_id") String languageId,
        @NotNull Long userId
) {}