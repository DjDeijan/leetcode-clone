package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.task.TaskPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.task.TaskRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.task.TaskResponseDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TaskMapper;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.service.CustomUserDetailsService;
import com.leetcode.clone.leetcode_clone.service.TaskService;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

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
    void getTaskById_returnsMappedDto() throws Exception {
        Long id = 1L;

        Task task = Task.builder()
                .id(id)
                .title("My title")
                .description("My description")
                .build();

        TaskResponseDTO responseDTO = new TaskResponseDTO(
                id,
                "My title",
                "My description",
                42L,
                List.of()
        );

        Mockito.when(taskService.getTaskOrThrow(id)).thenReturn(task);
        Mockito.when(taskMapper.toTaskResponseDTO(task)).thenReturn(responseDTO);

        mockMvc.perform(get("/tasks/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDTO.id().intValue()))
                .andExpect(jsonPath("$.title").value(responseDTO.title()))
                .andExpect(jsonPath("$.description").value(responseDTO.description()))
                .andExpect(jsonPath("$.createdByUserId").value(responseDTO.createdByUserId().intValue()));
    }

    @Test
    void getTaskById_whenNotFound_returns404() throws Exception {
        Long id = 999L;

        Mockito.when(taskService.getTaskOrThrow(id))
                .thenThrow(new ResourceNotFoundException(Task.class, id));

        mockMvc.perform(get("/tasks/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Task with id=999 was not found"))
                .andExpect(jsonPath("$.resource").value(Task.class.getSimpleName()));
    }

    @Test
    void getAllTasks_returnsMappedDtoPage() throws Exception {
        Task task1 = Task.builder().id(1L).title("T1").description("D1").build();
        Task task2 = Task.builder().id(2L).title("T2").description("D2").build();

        List<Task> tasks = List.of(task1, task2);

        TaskResponseDTO dto1 = new TaskResponseDTO(1L, "T1", "D1", 1L, List.of());
        TaskResponseDTO dto2 = new TaskResponseDTO(2L, "T2", "D2", 1L, List.of());

        Pageable pageable = PageRequest.of(0, 20);
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        TaskPageResponseDTO wrapper = new TaskPageResponseDTO(
                List.of(dto1, dto2),
                taskPage.getTotalPages(),
                taskPage.getTotalElements(),
                taskPage.getNumber(),
                taskPage.getSize()
        );

        Mockito.when(taskService.getAllTasks(any(Pageable.class))).thenReturn(taskPage);
        Mockito.when(taskMapper.toTaskPageResponseDTO(taskPage)).thenReturn(wrapper);

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tasks[0].id").value(dto1.id().intValue()))
                .andExpect(jsonPath("$.tasks[0].title").value(dto1.title()))
                .andExpect(jsonPath("$.tasks[1].id").value(dto2.id().intValue()))
                .andExpect(jsonPath("$.tasks[1].title").value(dto2.title()));
    }

    @Test
    void createTask_returns201_andMappedDto() throws Exception {
        TaskRequestDTO requestDTO = new TaskRequestDTO("New task", "Desc", List.of());

        Task created = Task.builder()
                .id(10L)
                .title(requestDTO.title())
                .description(requestDTO.description())
                .build();

        TaskResponseDTO responseDTO = new TaskResponseDTO(
                10L,
                requestDTO.title(),
                requestDTO.description(),
                7L,
                List.of()
        );

        Mockito.when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(created);
        Mockito.when(taskMapper.toTaskResponseDTO(created)).thenReturn(responseDTO);

        String json = """
                {
                  "title": "New task",
                  "description": "Desc",
                  "tagIds": []
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("New task"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.createdByUserId").value(7));
    }
}
