package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TestCase;
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
@Order(3)
@Component
@RequiredArgsConstructor
public class TestCaseSeeder implements CommandLineRunner {

    private final TestCaseRepository testcaseRepository;

    @Override
    public void run(String... args) {
        if (testcaseRepository.count() > 0) {
            log.info("--- Testcases table already contains data; skipping testcase seeding. ---");
            return;
        }

        Long id = 1L;
        Task task = Task.builder().id(10L).build();

        // Assumes Tasks are seeded and IDs start at 1.
        TestCase tc1 = TestCase.builder()
                //.taskId(1L)
                .task(task)
                .input("2 7 11 15\n9\n")
                .expectedOutput("0 1\n")
                .timeLimitMs(1000)
                .memoryLimitKb(256000)
                .stackLimitKb(64000)
                .build();

        TestCase tc2 = TestCase.builder()
                //.taskId(1L)
                .task(task)
                .input("3 2 4\n6\n")
                .expectedOutput("1 2\n")
                .timeLimitMs(1000)
                .memoryLimitKb(256000)
                .stackLimitKb(64000)
                .build();

        id = 2L;
        TestCase tc3 = TestCase.builder()
                //.taskId(2L)
                .task(task)
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
