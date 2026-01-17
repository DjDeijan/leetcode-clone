package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TaskTag;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void findByName_returnsTag() {
        Tag tag = Tag.builder().name("Greedy").build();
        entityManager.persist(tag);

        Optional<Tag> found = tagRepository.findByName("Greedy");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Greedy");
    }

    @Test
    void existsByName_returnsTrueWhenExists() {
        Tag tag = Tag.builder().name("DFS").build();
        entityManager.persist(tag);

        boolean exists = tagRepository.existsByName("DFS");

        assertThat(exists).isTrue();
    }

    @Test
    void findAllWithTasks_fetchesCorrectly() {
        User user = User.builder().username("admin").email("a@a.com").password("pass").build();
        entityManager.persist(user);

        Tag tag = Tag.builder().name("BFS").build();
        entityManager.persist(tag);

        Task task = Task.builder().title("Graph Problem").description("desc").createdByUser(user).build();
        entityManager.persist(task);

        TaskTag taskTag = TaskTag.builder().task(task).tag(tag).build();
        entityManager.persist(taskTag);

        entityManager.flush();
        entityManager.clear();

        List<Tag> tags = tagRepository.findAllWithTasks();

        assertThat(tags).hasSize(1);
        assertThat(tags.get(0).getTaskTags()).hasSize(1);
        assertThat(tags.get(0).getTaskTags().iterator().next().getTask().getTitle()).isEqualTo("Graph Problem");
    }
}