package com.leetcode.clone.leetcode_clone.dto.Submissions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSubmissionDTO {

    @JsonProperty("source_code")
    private String sourceCode;

    @JsonProperty("language_id")
    private int languageId;

    @JsonProperty("task_id")
    private int TaskId;
}
