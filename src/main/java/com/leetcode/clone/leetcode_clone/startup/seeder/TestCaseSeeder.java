package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Profile("dev")
@Order(4)
@Component
@RequiredArgsConstructor
public class TestCaseSeeder implements CommandLineRunner {

    private final TestCaseRepository testcaseRepository;
    private final TaskRepository taskRepository;

    @Override
    public void run(String... args) {
        if (testcaseRepository.count() > 0) {
            log.info("--- Testcases table already contains data; skipping testcase seeding. ---");
            return;
        }

        TestCase tc1 = TestCase.builder()
                .task(taskRepository.findById(1L).orElseThrow(() -> new RuntimeException("Test case Seeding failed!")))
                .input("Bla bla")
                .expectedOutput("Bla bla")
                .timeLimitMs(2000)
                .memoryLimitKb(128000)
                .stackLimitKb(64000)
                .build();

        TestCase tc2 = TestCase.builder()
                .task(taskRepository.findById(1L).orElseThrow(() -> new RuntimeException("Test case Seeding failed!")))
                .input("3 2")
                .expectedOutput("3 2")
                .timeLimitMs(2000)
                .memoryLimitKb(128000)
                .stackLimitKb(64000)
                .build();

        TestCase tc3 = TestCase.builder()
                .task(taskRepository.findById(2L).orElseThrow(() -> new RuntimeException("Test case Seeding failed!")))
                .input("5\n")
                .expectedOutput("120\n")
                .timeLimitMs(1000)
                .memoryLimitKb(256000)
                .stackLimitKb(64000)
                .build();

        testcaseRepository.saveAll(List.of(tc1, tc2, tc3));
        log.info("--- 3 Testcases were seeded! ---");
    }
}
