package com.leetcode.clone.leetcode_clone.dto.auth;

import com.leetcode.clone.leetcode_clone.dto.user.UserResponseDTO;

public record RegisterResponseDTO(
        String jwtToken,
        UserResponseDTO userData
) {}
