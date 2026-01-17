package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.user.UserPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.user.UserResponseDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.UserMapper;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.model.Role;
import com.leetcode.clone.leetcode_clone.service.CustomUserDetailsService;
import com.leetcode.clone.leetcode_clone.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // ============= getUserById Tests =============
    @Test
    void getUserById_returnsMappedDto() throws Exception {
        // Given
        Long id = 1L;

        User user = User.builder()
                .username("John")
                .build();

        UserResponseDTO responseDTO = new UserResponseDTO(
                id,
                "doe18@abv.bg",
                Role.USER,
                "John",
                null
        );

        Mockito.when(userService.getUserOrThrow(id))
                .thenReturn(user);

        Mockito.when(userMapper.toUserResponseDTO(user))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDTO.id().intValue()))
                .andExpect(jsonPath("$.username").value(responseDTO.username()));
    }

    @Test
    void getUserById_whenNotFound_returns404() throws Exception {
        Long id = 1L;

        // Service throws custom ErrorResponse
        Mockito.when(userService.getUserOrThrow(id))
                .thenThrow(new ResourceNotFoundException(User.class, id));

        mockMvc.perform(get("/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                // ProblemDetail from ResourceNotFoundException
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.resource").value(User.class.getSimpleName()));
    }
    // =================================================


    // ============= getAllUsers Tests =============
    @Test
    void getAllUsers_returnsMappedDtoPage() throws Exception {
        User user1 = User.builder()
                .username("John")
                .build();

        User user2 = User.builder()
                .username("Toma")
                .build();

        List<User> users = List.of(user1, user2);

        UserResponseDTO responseDTO1 = new UserResponseDTO(
                1L, "doe18@abv.bg", Role.USER,
                "John",
                null
        );

        UserResponseDTO responseDTO2 = new UserResponseDTO(
                2L, "tommy@gmail.com", Role.USER,
                "Toma",
                null
        );

        Pageable pageable = PageRequest.of(0, 20);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        UserPageResponseDTO wrapper = new UserPageResponseDTO(
                List.of(responseDTO1, responseDTO2),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.getNumber(),
                userPage.getSize()
        );

        Mockito.when(userService.getAllUsers(any(Pageable.class)))
                .thenReturn(userPage);

        Mockito.when(userMapper.toUserPageResponseDTO(userPage))
                .thenReturn(wrapper);

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.users[0].id").value(responseDTO1.id().intValue()))
                .andExpect(jsonPath("$.users[0].username").value(responseDTO1.username()))
                .andExpect(jsonPath("$.users[1].id").value(responseDTO2.id().intValue()))
                .andExpect(jsonPath("$.users[1].username").value(responseDTO2.username()));
    }
    // =================================================

}
