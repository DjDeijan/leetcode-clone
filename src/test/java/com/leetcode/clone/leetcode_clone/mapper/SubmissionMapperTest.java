package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SubmissionMapperTest {

    private final SubmissionMapper mapper = Mappers.getMapper(SubmissionMapper.class);

    @Test
    void toSubmission_mapsRequestDtoCorrectly() {
        // Given
        SubmissionRequestDTO dto = new SubmissionRequestDTO(
                10L, // taskId
                "print('hello')",
                "71", // languageId
                5L   // userId
        );

        // When
        Submission submission = mapper.toSubmission(dto);

        // Then
        assertThat(submission.getId()).isNull();
        assertThat(submission.getStatus()).isEqualTo("PENDING");
        assertThat(submission.getSourceCode()).isEqualTo("print('hello')");
        assertThat(submission.getLanguageId()).isEqualTo("71");

        assertThat(submission.getTask()).isNotNull();
        assertThat(submission.getTask().getId()).isEqualTo(10L);

        assertThat(submission.getUser()).isNotNull();
        assertThat(submission.getUser().getId()).isEqualTo(5L);
    }

    @Test
    void toResponseDTO_mapsEntityCorrectly() {
        // Given
        Task task = Task.builder().id(20L).build();
        User user = User.builder().id(30L).build();
        LocalDateTime now = LocalDateTime.now();

        Submission submission = Submission.builder()
                .id(1L)
                .task(task)
                .user(user)
                .sourceCode("def solve(): pass")
                .languageId("71")
                .status("Accepted")
                .grade("A")
                .build();

        // When
        SubmissionResponseDTO dto = mapper.toResponseDTO(submission);

        // Then
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.taskId()).isEqualTo(20L);
        assertThat(dto.userId()).isEqualTo(30L);
        assertThat(dto.sourceCode()).isEqualTo("def solve(): pass");
        assertThat(dto.languageId()).isEqualTo("71");
        assertThat(dto.status()).isEqualTo("Accepted");;
    }
}