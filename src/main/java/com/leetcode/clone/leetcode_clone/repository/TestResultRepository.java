package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    List<TestResult> findBySubmissionId(Long submissionId);

    List<TestResult> findByTestCaseId(Long testCaseId);

    List<TestResult> findByStatus(String status);
}
