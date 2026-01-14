package com.leetcode.clone.leetcode_clone.dto.user;

import java.util.List;

public record UserPageResponseDTO(
        List<UserResponseDTO> users,
        int totalPages,
        long totalElements,
        int page,
        int size
) {}
