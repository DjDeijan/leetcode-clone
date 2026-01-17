package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SubmissionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    void findByUserId_returnsSubmissions() {
        User user = User.builder().username("coder").email("c@c.com").password("pass").build();
        entityManager.persist(user);

        Task task = Task.builder().title("Sum").description("desc").createdByUser(user).build();
        entityManager.persist(task);

        Submission sub = Submission.builder()
                .user(user)
                .task(task)
                .sourceCode("print(1)")
                .languageId("51")
                .status("Accepted")
                .build();
        entityManager.persist(sub);

        Page<Submission> result = submissionRepository.findByUserId(user.getId(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getSourceCode()).isEqualTo("print(1)");
    }
}