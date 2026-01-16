package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}