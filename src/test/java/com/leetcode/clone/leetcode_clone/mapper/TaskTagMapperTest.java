package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTagMapperTest {

    private final TaskTagMapper mapper = Mappers.getMapper(TaskTagMapper.class);

    @Test
    void toResponseDTO_mapsIdsCorrectly() {
        Task task = Task.builder().id(100L).build();
        Tag tag = Tag.builder().id(200L).build();
        TaskTag taskTag = TaskTag.builder().task(task).tag(tag).build();

        TaskTagResponseDTO dto = mapper.toResponseDTO(taskTag);

        assertThat(dto.taskId()).isEqualTo(100L);
        assertThat(dto.tagId()).isEqualTo(200L);
    }
}