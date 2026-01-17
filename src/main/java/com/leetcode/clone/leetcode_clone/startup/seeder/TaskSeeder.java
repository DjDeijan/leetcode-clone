package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TaskTagRepository;
import com.leetcode.clone.leetcode_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Profile("dev")
@Order(3)
@Component
@RequiredArgsConstructor
public class TaskSeeder implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TaskTagRepository taskTagRepository;

    //Note: This seeder assumes dev DB starts fresh and your seeded users become IDs 1, 2, 3
    //(typical with ddl-auto=create). If your IDs differ, adjust createdByUserId.

    @Override
    public void run(String... args) {
        if (taskRepository.count() > 0) {
            log.info("--- Tasks table already contains data; skipping task seeding. ---");
            return;
        }

        Task t1 = Task.builder()
                .title("Solve two-sum")
                .description("Warm-up problem: implement the classic two-sum using a hash map.")
                .createdByUser(userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Task Seeding failed!")))
                .build();

        TaskTag tt1 = TaskTag.builder()
                .task(t1)
                .tag(tagRepository.findById(2L).orElseThrow(() -> new RuntimeException("Task Seeding failed!")))
                .build();

        Task t2 = Task.builder()
                .title("Add pagination")
                .description("Implement pageable endpoints for tasks, similar to /users.")
                .createdByUser(userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Task Seeding failed!")))
                .build();

        TaskTag tt2 = TaskTag.builder()
                .task(t2)
                .tag(tagRepository.findById(3L).orElseThrow(() -> new RuntimeException("Task Seeding failed!")))
                .build();

        Task t3 = Task.builder()
                .title("Write controller tests")
                .description("Create WebMvcTest coverage for getById, getAll, create.")
                .createdByUser(userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Task Seeding failed!")))
                .build();

        TaskTag tt3 = TaskTag.builder()
                .task(t3)
                .tag(tagRepository.findById(1L).orElseThrow(() -> new RuntimeException("Task Seeding failed!")))
                .build();

        taskRepository.saveAll(List.of(t1, t2, t3));
        taskTagRepository.saveAll(List.of(tt1, tt2, tt3));
        log.info("--- 3 Tasks were seeded! ---");
    }
}