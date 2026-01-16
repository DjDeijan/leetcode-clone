package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.task.TaskRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TaskMapper;
import com.leetcode.clone.leetcode_clone.mapper.UserMapper;
import com.leetcode.clone.leetcode_clone.model.*;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Transactional
    public Task createTask(TaskRequestDTO taskRequestDTO) {
        Task task = taskMapper.toTask(taskRequestDTO);

        User currentUser = userService.getCurrentUser();
        task.setCreatedByUser(currentUser);

        log.info("New task created title={} createdByUserId={}", task.getTitle(), task.getCreatedByUser());
        return taskRepository.save(task);
    }

    // Needs Tag in order to function
//    @Transactional
//    public void addTagToTask(Long taskId, Long tagId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId));
//
//        Tag tag = tagRepository.findById(tagId)
//                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, tagId));
//
//        TaskTag taskTag = TaskTag.builder()
//                .id(new TaskTagId(taskId, tagId))
//                .task(task)
//                .tag(tag)
//                .build();
//
//        task.getTaskTags().add(taskTag);
//    }

//    @Transactional
//    public void removeTagFromTask(Long taskId, Long tagId) {
//        Task task = getTaskOrThrow(taskId);
//
//        task.getTaskTags().removeIf(
//                tt -> tt.getId().getTagId().equals(tagId)
//        );
//    }


    public Task getTaskOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Task.class, id));
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Page<Task> getAllTasksByCreatedByUserId(Long createdByUserId, Pageable pageable) {
        return taskRepository.findAllByCreatedByUserId(createdByUserId, pageable);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (taskRepository.existsById(id)) taskRepository.deleteById(id);
        else throw new ResourceNotFoundException(Task.class, id);
    }

    @Transactional
    public Task patchTaskTitle(Long id, String newTitle) {
        Task task = getTaskOrThrow(id);
        task.setTitle(newTitle);
        return taskRepository.save(task);
    }

    @Transactional
    public Task putTask(Long id, TaskRequestDTO taskRequestDTO) {
        Task existingTask = getTaskOrThrow(id);
        taskMapper.updateTaskFromRequestDTO(existingTask, taskRequestDTO);
        return taskRepository.save(existingTask);
    }
}
