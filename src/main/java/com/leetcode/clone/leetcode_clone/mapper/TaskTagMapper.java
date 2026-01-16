package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagResponseDTO;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskTagMapper {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "tag.id", target = "tagId")
    @Mapping(source = "tag.name", target = "tagName")
    @Mapping(source = "task.title", target = "taskTitle")
    TaskTagResponseDTO toResponseDTO(TaskTag taskTag);
}