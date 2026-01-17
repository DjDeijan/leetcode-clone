package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskTagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskTagRepository taskTagRepository;

    @Test
    void findById_returnsTaskTag() {
        User user = User.builder().username("tester").email("t@t.com").password("pass").build();
        entityManager.persist(user);

        Task task = Task.builder().title("Array Task").description("desc").createdByUser(user).build();
        entityManager.persist(task);

        Tag tag = Tag.builder().name("Array").build();
        entityManager.persist(tag);

        TaskTag taskTag = TaskTag.builder()
                .task(task)
                .tag(tag)
                .build();
        entityManager.persist(taskTag);

        entityManager.flush();
        entityManager.clear();

        TaskTagId id = new TaskTagId(task.getId(), tag.getId());
        Optional<TaskTag> found = taskTagRepository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getTask().getTitle()).isEqualTo("Array Task");
        assertThat(found.get().getTag().getName()).isEqualTo("Array");
    }
}