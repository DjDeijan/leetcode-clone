package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagRequestDTO;
import com.leetcode.clone.leetcode_clone.model.*;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TaskTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskTagServiceTest {

    private TaskTagRepository taskTagRepository;
    private TaskRepository taskRepository;
    private TagRepository tagRepository;
    private TaskTagService taskTagService;

    @BeforeEach
    void setUp() {
        taskTagRepository = mock(TaskTagRepository.class);
        taskRepository = mock(TaskRepository.class);
        tagRepository = mock(TagRepository.class);
        taskTagService = new TaskTagService(taskTagRepository, taskRepository, tagRepository);
    }

    @Test
    void assignTagToTask_Success() {
        // Arrange
        TaskTagRequestDTO request = new TaskTagRequestDTO(1L, 2L);
        Task task = Task.builder().id(1L).title("Test Task").build();
        Tag tag = Tag.builder().id(2L).name("Java").build();
        TaskTag savedTaskTag = TaskTag.builder()
                .id(new TaskTagId(1L, 2L))
                .task(task)
                .tag(tag)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));
        when(taskTagRepository.save(any(TaskTag.class))).thenReturn(savedTaskTag);

        // Act
        TaskTag result = taskTagService.assignTagToTask(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId().getTaskId());
        assertEquals(2L, result.getId().getTagId());
        verify(taskTagRepository, times(1)).save(any(TaskTag.class));
    }
}