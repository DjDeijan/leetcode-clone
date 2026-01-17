package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TaskTagMapper;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import com.leetcode.clone.leetcode_clone.service.TaskTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<TaskTagResponseDTO> assignTag(@Valid @RequestBody TaskTagRequestDTO dto) {
        TaskTag taskTag = taskTagService.assignTagToTask(dto);
        return new ResponseEntity<>(taskTagMapper.toResponseDTO(taskTag), HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskTagResponseDTO>> getTagsForTask(@PathVariable Long taskId) {
        List<TaskTagResponseDTO> tags = taskTagService.getTagsByTaskId(taskId).stream()
                .map(taskTagMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(tags);
    }

    @DeleteMapping("/{taskId}/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable Long taskId, @PathVariable Long tagId) {
        taskTagService.removeTagFromTask(taskId, tagId);
        return ResponseEntity.noContent().build();
    }
}