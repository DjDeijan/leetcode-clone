package com.leetcode.clone.leetcode_clone.dto.Submissions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Judge0SubmissionDTO {

    @JsonProperty("source_code")
    private String sourceCode;

    @JsonProperty("language_id")
    private int languageId;

    @JsonProperty("cpu_time_limit")
    private Double cpuTimeLimit; // Use Double for nullable decimal

    @JsonProperty("memory_limit")
    private Integer memoryLimit; // Integer for nullable int

    @JsonProperty("stack_limit")
    private Integer stackLimit;

    @JsonProperty("stdin")
    private String stdIn;

    @JsonProperty("expected_output")
    private String expectedOutput;

}
