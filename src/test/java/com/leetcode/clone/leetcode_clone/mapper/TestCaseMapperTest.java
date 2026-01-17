package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class TestCaseMapperTest {
    private final TestCaseMapper mapper = Mappers.getMapper(TestCaseMapper.class);

    @Test
    void toResponseDTO_mapsTaskIdCorrectly() {
        Task task = Task.builder().id(5L).build();

        TestCase testCase = TestCase.builder()
                .id(10L)
                .task(task)
                .input("in")
                .expectedOutput("out")
                .timeLimitMs(1000)
                .memoryLimitKb(256000)
                .stackLimitKb(64000)
                .build();

        TestCaseResponseDTO dto = mapper.toResponseDTO(testCase);

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.taskId()).isEqualTo(5L);
        assertThat(dto.input()).isEqualTo("in");
        assertThat(dto.expectedOutput()).isEqualTo("out");
    }

    @Test
    void toEntity_doesNotSetTask() {
        TestCaseRequestDTO dto = new TestCaseRequestDTO(
                "input",
                "output",
                1000,
                256000,
                64000
        );

        TestCase testCase = mapper.toEntity(dto);

        assertThat(testCase.getId()).isNull();
        assertThat(testCase.getTask()).isNull(); // IMPORTANT
        assertThat(testCase.getInput()).isEqualTo("input");
    }
}
