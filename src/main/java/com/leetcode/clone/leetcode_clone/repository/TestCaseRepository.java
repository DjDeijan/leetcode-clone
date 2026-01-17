package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.TestCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    Page<TestCase> findAllByTaskId(Long taskId, Pageable pageable);

    List<TestCase> findAllByTaskId(Long taskId);

    List<TestCase> findByTaskId(Long taskId);
}


