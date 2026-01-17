package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.task.TaskRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.task.TaskResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

    private final TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void toTaskResponseDTO_mapsUserAndTagsCorrectly() {
        User user = User.builder().id(3L).build();
        Tag tag1 = Tag.builder().id(10L).build();
        Tag tag2 = Tag.builder().id(20L).build();

        Task task = Task.builder()
                .id(1L)
                .title("Title")
                .description("Desc")
                .createdByUser(user)
                .taskTags(Set.of(
                        TaskTag.builder().tag(tag1).build(),
                        TaskTag.builder().tag(tag2).build()
                ))
                .build();

        TaskResponseDTO dto = mapper.toTaskResponseDTO(task);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.createdByUserId()).isEqualTo(3L);
        assertThat(dto.tagIds()).containsExactlyInAnyOrder(10L, 20L);
    }

    @Test
    void toTask_doesNotSetIdOrRelations() {
        TaskRequestDTO dto = new TaskRequestDTO(
                "Title",
                "Desc",
                null,
                3L,
                List.of(1L, 2L)
        );

        Task task = mapper.toTask(dto);

        assertThat(task.getId()).isNull();
        assertThat(task.getCreatedByUser()).isNull();
        assertThat(task.getTaskTags()).isEmpty();
    }
}

