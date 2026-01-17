package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TaskTagMapper;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import com.leetcode.clone.leetcode_clone.service.TaskTagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-tags")
@RequiredArgsConstructor
@Validated
@Tag(name = "TaskTag Controller", description = "Operations for linking Tasks and Tags")
public class TaskTagController {

    private final TaskTagService taskTagService;
    private final TaskTagMapper taskTagMapper;

    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign a tag to a task")
    @PostMapping
    public ResponseEntity<TaskTagResponseDTO> assignTag(@Valid @RequestBody TaskTagRequestDTO dto) {
        TaskTag taskTag = taskTagService.assignTagToTask(dto);
        return new ResponseEntity<>(taskTagMapper.toResponseDTO(taskTag), HttpStatus.CREATED);
    }
    @Operation(summary = "Get all tags for a specific task")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskTagResponseDTO>> getTagsForTask(@PathVariable Long taskId) {
        List<TaskTagResponseDTO> tags = taskTagService.getTagsByTaskId(taskId).stream()
                .map(taskTagMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(tags);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove a tag from a task")
    @DeleteMapping("/{taskId}/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable Long taskId, @PathVariable Long tagId) {
        taskTagService.removeTagFromTask(taskId, tagId);
        return ResponseEntity.noContent().build();
    }
}