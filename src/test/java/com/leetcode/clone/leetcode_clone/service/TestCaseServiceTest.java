package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TestCaseMapper;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TestCaseServiceTest {

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TestCaseMapper testCaseMapper;

    @InjectMocks
    private TestCaseService testCaseService;

    @Test
    void createTestCase_setsTaskAndSaves() {
        Long taskId = 7L;
        Task task = Task.builder().id(taskId).build();

        TestCaseRequestDTO dto = new TestCaseRequestDTO(
                "1 2\n",
                "3\n",
                1000,
                256000,
                64000
        );

        TestCase mapped = TestCase.builder()
                .input(dto.input())
                .expectedOutput(dto.expectedOutput())
                .build();

        TestCase saved = TestCase.builder()
                .id(1L)
                .task(task)
                .input(dto.input())
                .expectedOutput(dto.expectedOutput())
                .build();

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(testCaseMapper.toEntity(dto)).thenReturn(mapped);
        Mockito.when(testCaseRepository.save(mapped)).thenReturn(saved);

        TestCase result = testCaseService.createTestCase(taskId, dto);

        assertThat(result.getTask()).isEqualTo(task);
        Mockito.verify(testCaseRepository).save(mapped);
    }

    @Test
    void createTestCase_whenTaskMissing_throwsException() {
        Long taskId = 99L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                testCaseService.createTestCase(taskId, Mockito.mock(TestCaseRequestDTO.class))
        ).isInstanceOf(ResourceNotFoundException.class);
    }
}

