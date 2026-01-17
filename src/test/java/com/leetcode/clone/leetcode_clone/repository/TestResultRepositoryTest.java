package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TestResultRepositoryTest {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestEntityManager entityManager;

    private TestResult testResult1;
    private TestResult testResult2;
    private Submission submission;
    private TestCase testCase1;
    private TestCase testCase2;

    @BeforeEach
    void setUp() {
        // Create User
        User user = User.builder()
                .username("testuser")
                .email("test@test.com")
                .password("password")
                .role(Role.USER)
                .build();
        entityManager.persist(user);

        // Create Task
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .build();
        entityManager.persist(task);

        // Create Submission
        submission = Submission.builder()
                .task(task)
                .user(user)
                .sourceCode("public class Solution {}")
                .languageId("java")
                .status("Processing")
                .build();
        entityManager.persist(submission);

        // Create TestCases
        testCase1 = TestCase.builder()
                .task(task)
                .input("1 2")
                .expectedOutput("3")
                .timeLimitMs(1000)
                .memoryLimitKb(256000)
                .stackLimitKb(128000)
                .build();
        entityManager.persist(testCase1);

        testCase2 = TestCase.builder()
                .task(task)
                .input("2 3")
                .expectedOutput("5")
                .timeLimitMs(1000)
                .memoryLimitKb(256000)
                .stackLimitKb(128000)
                .build();
        entityManager.persist(testCase2);

        // Create TestResults
        testResult1 = TestResult.builder()
                .testCase(testCase1)
                .submission(submission)
                .status("Accepted")
                .judge0Token("token-123")
                .stdout("3")
                .build();
        entityManager.persist(testResult1);

        testResult2 = TestResult.builder()
                .testCase(testCase2)
                .submission(submission)
                .status("Wrong Answer")
                .judge0Token("token-456")
                .stdout("4")
                .err("Expected 5 but got 4")
                .build();
        entityManager.persist(testResult2);

        entityManager.flush();
    }

    @Test
    void findById_returnsTestResult_whenExists() {
        Optional<TestResult> found = testResultRepository.findById(testResult1.getId());

        assertTrue(found.isPresent());
        assertEquals(testResult1.getId(), found.get().getId());
        assertEquals("Accepted", found.get().getStatus());
        assertEquals("token-123", found.get().getJudge0Token());
    }

    @Test
    void findById_returnsEmpty_whenNotExists() {
        Optional<TestResult> found = testResultRepository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    void findBySubmissionId_returnsAllTestResultsForSubmission() {
        List<TestResult> results = testResultRepository.findBySubmissionId(submission.getId());

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void findBySubmissionId_returnsEmptyList_whenNoResults() {
        List<TestResult> results = testResultRepository.findBySubmissionId(999L);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findByTestCaseId_returnsAllTestResultsForTestCase() {
        List<TestResult> results = testResultRepository.findByTestCaseId(testCase1.getId());

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testResult1.getId(), results.get(0).getId());
        assertEquals("Accepted", results.get(0).getStatus());
    }

    @Test
    void findByTestCaseId_returnsEmptyList_whenNoResults() {
        List<TestResult> results = testResultRepository.findByTestCaseId(999L);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findByStatus_returnsAllTestResultsWithStatus() {
        List<TestResult> acceptedResults = testResultRepository.findByStatus("Accepted");

        assertNotNull(acceptedResults);
        assertEquals(1, acceptedResults.size());
        assertEquals("Accepted", acceptedResults.get(0).getStatus());
    }

    @Test
    void findByStatus_returnsMultipleResults_whenMultipleMatch() {
        // Create another accepted test result
        TestResult testResult3 = TestResult.builder()
                .testCase(testCase1)
                .submission(submission)
                .status("Accepted")
                .judge0Token("token-789")
                .stdout("3")
                .build();
        entityManager.persist(testResult3);
        entityManager.flush();

        List<TestResult> acceptedResults = testResultRepository.findByStatus("Accepted");

        assertNotNull(acceptedResults);
        assertEquals(2, acceptedResults.size());
    }

    @Test
    void findByStatus_returnsEmptyList_whenNoMatch() {
        List<TestResult> results = testResultRepository.findByStatus("Time Limit Exceeded");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void save_persistsTestResult() {
        TestResult newTestResult = TestResult.builder()
                .testCase(testCase2)
                .submission(submission)
                .status("Runtime Error")
                .judge0Token("token-999")
                .err("NullPointerException")
                .build();

        TestResult saved = testResultRepository.save(newTestResult);

        assertNotNull(saved.getId());
        assertEquals("Runtime Error", saved.getStatus());

        Optional<TestResult> found = testResultRepository.findById(saved.getId());
        assertTrue(found.isPresent());
    }

    @Test
    void update_modifiesExistingTestResult() {
        testResult1.setStatus("Accepted (Optimized)");
        testResult1.setStdout("3 (optimized)");

        TestResult updated = testResultRepository.save(testResult1);

        assertEquals("Accepted (Optimized)", updated.getStatus());
        assertEquals("3 (optimized)", updated.getStdout());
    }

    @Test
    void delete_removesTestResult() {
        Long id = testResult1.getId();
        testResultRepository.deleteById(id);

        Optional<TestResult> found = testResultRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_returnsAllTestResults() {
        List<TestResult> allResults = testResultRepository.findAll();

        assertNotNull(allResults);
        assertTrue(allResults.size() >= 2);
    }
}
