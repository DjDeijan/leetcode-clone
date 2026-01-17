package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByCreatedByUserId_returnsCorrectTasks() {
        User user = userRepository.save(User.builder()
                .username("u")
                .email("test@gmail.com")
                .password("testPassword")
                .build());

        Task t1 = Task.builder().title("A").createdByUser(user).description("test").build();
        Task t2 = Task.builder().title("B").createdByUser(user).description("test2").build();

        taskRepository.saveAll(List.of(t1, t2));

        List<Task> result = taskRepository.findAllByCreatedByUserId(user.getId());

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(t -> t.getCreatedByUser().getId().equals(user.getId()));
    }
}

