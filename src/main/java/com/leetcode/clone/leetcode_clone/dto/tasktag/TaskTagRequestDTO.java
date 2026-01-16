package com.leetcode.clone.leetcode_clone.dto.tasktag;

import jakarta.validation.constraints.NotNull;

public record TaskTagRequestDTO(
        @NotNull Long taskId,
        @NotNull Long tagId
) {}