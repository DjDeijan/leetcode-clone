package com.leetcode.clone.leetcode_clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TaskTagMapper;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import com.leetcode.clone.leetcode_clone.service.CustomUserDetailsService;
import com.leetcode.clone.leetcode_clone.service.TaskTagService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskTagController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskTagService taskTagService;

    @MockitoBean
    private TaskTagMapper taskTagMapper;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    void assignTagToTask_returnsCreatedTaskTag() throws Exception {
        // Given
        TaskTagRequestDTO requestDTO = new TaskTagRequestDTO(1L, 2L);
        TaskTagResponseDTO responseDTO = new TaskTagResponseDTO(1L, 2L, "test", "test");
        TaskTag taskTag = new TaskTag(); // Mock object

        Mockito.when(taskTagService.assignTagToTask(any(TaskTagRequestDTO.class)))
                .thenReturn(taskTag);
        Mockito.when(taskTagMapper.toResponseDTO(taskTag))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/task-tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.tagId").value(2L));
    }

    @Test
    void removeTagFromTask_returnsNoContent() throws Exception {
        // Given
        Long taskId = 1L;
        Long tagId = 2L;

        Mockito.doNothing().when(taskTagService).removeTagFromTask(taskId, tagId);

        // When & Then
        mockMvc.perform(delete("/task-tags/{taskId}/{tagId}", taskId, tagId))
                .andExpect(status().isNoContent());
    }
}