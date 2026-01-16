package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT DISTINCT t FROM Tag t LEFT JOIN FETCH t.taskTags tt LEFT JOIN FETCH tt.task")
    List<Tag> findAllWithTasks();

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.taskTags tt LEFT JOIN FETCH tt.task WHERE t.id = :id")
    Optional<Tag> findByIdWithTasks(@Param("id") Long id);
}