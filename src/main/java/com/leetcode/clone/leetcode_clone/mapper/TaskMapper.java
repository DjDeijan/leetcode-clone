package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.task.TaskPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.task.TaskRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.task.TaskResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Task;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(source = "createdByUser.id", target = "createdByUserId")
    @Mapping(
            target = "tagIds",
            expression = "java(task.getTaskTags().stream()" +
                    ".map(tt -> tt.getTag().getId())" +
                    ".toList())"
    )
    TaskResponseDTO toTaskResponseDTO(Task task);

    List<TaskResponseDTO> toTaskResponseDTOList(List<Task> tasks);

    @Mapping(target = "id", ignore = true)
    Task toTask(TaskRequestDTO taskRequestDTO);

    @Mapping(target = "id", ignore = true)
    void updateTaskFromRequestDTO(@MappingTarget Task task, TaskRequestDTO dto);

    default TaskPageResponseDTO toTaskPageResponseDTO(Page<Task> page) {
        return new TaskPageResponseDTO(
                page.getContent().stream().map(this::toTaskResponseDTO).toList(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
}
