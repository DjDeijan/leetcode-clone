package com.leetcode.clone.leetcode_clone.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequestDTO(
        @NotBlank @Size(min = 2, max = 50) String name
) {}