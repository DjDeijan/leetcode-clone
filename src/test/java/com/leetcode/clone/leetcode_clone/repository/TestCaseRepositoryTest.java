package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class TestCaseRepositoryTest {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByTaskId_returnsOnlyMatchingTestCases() {
        User user = userRepository.save(User.builder()
                .email("test@example.com")
                .password("password")
                .username("testuser")
                .build());

        Task task1 = taskRepository.save(Task.builder()
                .title("Ta")
                .description("Da")
                .createdByUser(user)
                .build());
        Task task2 = taskRepository.save(Task.builder()
                .title("Ta2")
                .description("Da")
                .createdByUser(user)
                .build());

        TestCase tc1 = TestCase.builder()
                .task(task1)
                .input("A")
                .expectedOutput("B")
                .build();

        TestCase tc2 = TestCase.builder()
                .task(task1)
                .input("C")
                .expectedOutput("D")
                .build();

        TestCase tc3 = TestCase.builder()
                .task(task2)
                .input("X")
                .expectedOutput("Y")
                .build();

        testCaseRepository.saveAll(List.of(tc1, tc2, tc3));

        Page<TestCase> result =
                testCaseRepository.findAllByTaskId(task1.getId(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .allMatch(tc -> tc.getTask().getId().equals(task1.getId()));
    }
}

