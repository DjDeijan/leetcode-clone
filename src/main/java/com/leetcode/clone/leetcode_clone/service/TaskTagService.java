package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.tasktag.TaskTagRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.model.*;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import com.leetcode.clone.leetcode_clone.repository.TaskTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskTagService {

    private final TaskTagRepository taskTagRepository;
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;

    @Transactional
    public TaskTag assignTagToTask(TaskTagRequestDTO dto) {
        Task task = taskRepository.findById(dto.taskId())
                .orElseThrow(() -> new ResourceNotFoundException(Task.class, dto.taskId()));
        Tag tag = tagRepository.findById(dto.tagId())
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, dto.tagId()));

        TaskTag taskTag = TaskTag.builder()
                .id(new TaskTagId(task.getId(), tag.getId()))
                .task(task)
                .tag(tag)
                .build();

        return taskTagRepository.save(taskTag);
    }

    public List<TaskTag> getTagsByTaskId(Long taskId) {
        return taskTagRepository.findAllById_TaskId(taskId);
    }

    @Transactional
    public void removeTagFromTask(Long taskId, Long tagId) {
        TaskTagId id = new TaskTagId(taskId, tagId);
        if (!taskTagRepository.existsById(id)) {
            throw new ResourceNotFoundException(TaskTag.class, taskId + "-" + tagId);
        }
        taskTagRepository.deleteById(id);
    }
}