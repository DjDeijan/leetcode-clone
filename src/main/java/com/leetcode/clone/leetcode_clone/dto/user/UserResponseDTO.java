package com.leetcode.clone.leetcode_clone.dto.user;

import com.leetcode.clone.leetcode_clone.model.Role;

public record UserResponseDTO(
        Long id,
        String email,
        Role role,
        String username,
        String profileImage
) {}
