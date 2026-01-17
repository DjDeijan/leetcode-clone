package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.task.TaskResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TaskMapper;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.service.CustomUserDetailsService;
import com.leetcode.clone.leetcode_clone.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getTaskById_returnsDto() throws Exception {
        Task task = Task.builder().id(1L).build();

        TaskResponseDTO dto = new TaskResponseDTO(
                1L,
                "Title",
                "Desc",
                2L,
                List.of(10L)
        );

        Mockito.when(taskService.getTaskOrThrow(1L)).thenReturn(task);
        Mockito.when(taskMapper.toTaskResponseDTO(task)).thenReturn(dto);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.createdByUserId").value(2));
    }

    @Test
    void createTask_returns201() throws Exception {
        User user = User.builder().id(1L).build();
        Task task = Task.builder()
                .id(1L)
                .title("Ta")
                .description("Da")
                .createdByUser(user)
                .build();

        Mockito.when(taskService.createTask(any())).thenReturn(task);
        Mockito.when(taskMapper.toTaskResponseDTO(task))
                .thenReturn(new TaskResponseDTO(1L, "Ta", "Da", 1L, List.of()));

        String json = """
                {
                  "title": "Ta",
                  "description": "Da",
                  "createdByUserId": 1,
                  "tagIds": []
                }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}