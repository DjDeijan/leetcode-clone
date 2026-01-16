package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.repository.SubmissionRepository;
import com.leetcode.clone.leetcode_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionSeeder implements CommandLineRunner {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (submissionRepository.count() == 0) {
            User user = userRepository.findAll().stream().findFirst().orElse(null);
            if (user != null) {
                submissionRepository.save(Submission.builder()
                        .task(Task.builder().id(1L).build()) // Assuming Task 1 exists
                        .user(user)
                        .sourceCode("print('Hello World')")
                        .languageId("python")
                        .status("ACCEPTED")
                        .grade("100")
                        .build());
            }
        }
    }
}