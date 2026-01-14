package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.auth.RegisterRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.UserMapper;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private UserMapper userMapper;
    private CloudinaryService cloudinaryService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        cloudinaryService = mock(CloudinaryService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, userMapper, cloudinaryService, passwordEncoder);
    }

    // ============= getUserOrThrow Tests =============
    @Test
    void getUserOrThrow_returnsUser_whenFound() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("John")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserOrThrow(1L);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserOrThrow_throwsException_whenNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserOrThrow(99L));

        verify(userRepository, times(1)).findById(99L);
    }
    // =================================================


    // ============= registerUser Tests =============
    @Test
    void registerUser() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO(
                "doe18@abv.bg",
                "_secretPass_",
                "John"
        );

        User userMapped = User.builder()
                .username("John")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username("John")
                .build();

        // Stub Mapper
        when(userMapper.toUser(request)).thenReturn(userMapped);

        // Stub Repo Save
        when(userRepository.save(userMapped)).thenReturn(savedUser);

        // Act
        User result = userService.registerUser(request);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());

        verify(userRepository, times(1)).save(userMapped);
    }
}
