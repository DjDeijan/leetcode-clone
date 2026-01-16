package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Profile("dev")
@Order(2)
@Component
@RequiredArgsConstructor
public class TaskSeeder implements CommandLineRunner {

    private final TaskRepository taskRepository;

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
                .createdByUserId(1L)
                .build();

        Task t2 = Task.builder()
                .title("Add pagination")
                .description("Implement pageable endpoints for tasks, similar to /users.")
                .createdByUserId(2L)
                .build();

        Task t3 = Task.builder()
                .title("Write controller tests")
                .description("Create WebMvcTest coverage for getById, getAll, create.")
                .createdByUserId(1L)
                .build();

        taskRepository.saveAll(List.of(t1, t2, t3));
        log.info("--- 3 Tasks were seeded! ---");
    }
}
