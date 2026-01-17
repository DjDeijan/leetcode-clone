package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.task.TaskRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TaskMapper;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
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
                1L,
                List.of(10L)
        );

        Task mapped = Task.builder().title("Title").build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(tagRepository.findAllById(List.of(10L))).thenReturn(List.of(tag));
        Mockito.when(taskMapper.toTask(dto)).thenReturn(mapped);
        Mockito.when(taskRepository.save(mapped)).thenReturn(mapped);

        Task result = taskService.createTask(dto);

        assertThat(result.getCreatedByUser()).isEqualTo(user);
        assertThat(result.getTaskTags()).hasSize(1);
    }

    @Test
    void createTask_whenUserMissing_throwsException() {
        TaskRequestDTO dto = new TaskRequestDTO("T", "D", 99L, List.of());

        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

