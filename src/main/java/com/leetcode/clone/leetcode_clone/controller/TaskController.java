package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.task.TaskPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.task.TaskRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.task.TaskResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TaskMapper;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable @Positive Long id) {
        Task fetchedTask = taskService.getTaskOrThrow(id);
        return new ResponseEntity<>(taskMapper.toTaskResponseDTO(fetchedTask), HttpStatus.OK);
    }
    @Operation(summary = "Get all tasks")
    @GetMapping
    public ResponseEntity<TaskPageResponseDTO> getAllTasks(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<Task> page = taskService.getAllTasks(pageable);
        return new ResponseEntity<>(taskMapper.toTaskPageResponseDTO(page), HttpStatus.OK);
    }
    @Operation(summary = "Get all tasks created by a specific user")
    @GetMapping("/filter/createdByUserId")
    public ResponseEntity<TaskPageResponseDTO> getAllTasksByCreatedByUserId(
            @RequestParam @Positive Long createdByUserId,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<Task> page = taskService.getAllTasksByCreatedByUserId(createdByUserId, pageable);
        return new ResponseEntity<>(taskMapper.toTaskPageResponseDTO(page), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")   
    @Operation(summary = "Create a new task")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        Task created = taskService.createTask(taskRequestDTO);
        return new ResponseEntity<>(taskMapper.toTaskResponseDTO(created), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a task")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @Positive Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update task title")
    @PatchMapping("/{id}/title")
    public ResponseEntity<TaskResponseDTO> patchTaskTitle(
            @PathVariable @Positive Long id,
            @RequestParam @NotBlank @Size(min = 2, max = 200) String newTitle
    ) {
        Task patched = taskService.patchTaskTitle(id, newTitle);
        return new ResponseEntity<>(taskMapper.toTaskResponseDTO(patched), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a task")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> putTask(
            @PathVariable @Positive Long id,
            @Valid @RequestBody TaskRequestDTO taskRequestDTO
    ) {
        Task updated = taskService.putTask(id, taskRequestDTO);
        return new ResponseEntity<>(taskMapper.toTaskResponseDTO(updated), HttpStatus.OK);
    }
}
