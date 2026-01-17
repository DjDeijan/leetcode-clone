package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import com.leetcode.clone.leetcode_clone.repository.SubmissionRepository;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
import com.leetcode.clone.leetcode_clone.repository.TestResultRepository;
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
public class TestResultSeeder implements CommandLineRunner {

    private final TestResultRepository testResultRepository;
    private final SubmissionRepository submissionRepository;
    private final TestCaseRepository testCaseRepository;

    @Override
    public void run(String... args) throws Exception {
        if (testResultRepository.count() > 0) {
            log.info("--- Test results already exist, skipping seeding ---");
            return;
        }

        List<Submission> submissions = submissionRepository.findAll();
        List<TestCase> testCases = testCaseRepository.findAll();

        if (submissions.isEmpty() || testCases.isEmpty()) {
            log.warn("--- Cannot seed test results: No submissions or test cases found ---");
            return;
        }

        // Create sample test results
        TestResult result1 = TestResult.builder()
                .submission(submissions.get(0))
                .testCase(testCases.get(0))
                .status("Accepted")
                .judge0Token("token-12345")
                .stdout("Output: 5")
                .err(null)
                .build();

        TestResult result2 = TestResult.builder()
                .submission(submissions.get(0))
                .testCase(testCases.size() > 1 ? testCases.get(1) : testCases.get(0))
                .status("Wrong Answer")
                .judge0Token("token-12346")
                .stdout("Output: 3")
                .err("Expected 5 but got 3")
                .build();

        TestResult result3 = TestResult.builder()
                .submission(submissions.size() > 1 ? submissions.get(1) : submissions.get(0))
                .testCase(testCases.get(0))
                .status("Time Limit Exceeded")
                .judge0Token("token-12347")
                .stdout("")
                .err("Execution time exceeded limit")
                .build();

        testResultRepository.saveAll(List.of(result1, result2, result3));
        log.info("--- 3 Test Results were seeded! ---");
    }
}
