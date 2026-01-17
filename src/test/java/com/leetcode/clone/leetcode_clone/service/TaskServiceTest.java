package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.task.TaskRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TaskMapper;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserService userService;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_setsUserAndTags() {
        User user = User.builder().id(1L).build();
        Tag tag = Tag.builder().id(10L).build();

        TaskRequestDTO dto = new TaskRequestDTO(
                "Title",
                "Desc",
                null,
                1L,
                List.of(10L)
        );

        Task mapped = Task.builder().title("Title").build();

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(tagRepository.findById(10L)).thenReturn(Optional.of(tag));
        Mockito.when(taskMapper.toTask(dto)).thenReturn(mapped);
        Mockito.when(taskRepository.save(mapped)).thenReturn(mapped);

        Task result = taskService.createTask(dto);

        assertThat(result.getCreatedByUser()).isEqualTo(user);
        assertThat(result.getTaskTags()).hasSize(1);
    }

    @Test
    void createTask_whenUserMissing_throwsException() {
        TaskRequestDTO dto = new TaskRequestDTO("T", "D", null, 99L, List.of());

        Mockito.when(userService.getCurrentUser()).thenThrow(new IllegalStateException("No authenticated user"));

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(IllegalStateException.class);
    }
}