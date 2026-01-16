package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.mapper.SubmissionMapper;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubmissionServiceTest {

    private SubmissionRepository submissionRepository;
    private SubmissionMapper submissionMapper;
    private SubmissionService submissionService;

    @BeforeEach
    void setUp() {
        submissionRepository = mock(SubmissionRepository.class);
        submissionMapper = mock(SubmissionMapper.class);
        submissionService = new SubmissionService(submissionRepository, submissionMapper);
    }

    @Test
    void getSubmissionOrThrow_returnsSubmission_whenFound() {
        Submission submission = Submission.builder().id(1L).build();
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        Submission result = submissionService.getSubmissionOrThrow(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void createSubmission_savesAndReturns() {
        SubmissionRequestDTO request = new SubmissionRequestDTO(1L, "code", "java", 1L);
        Submission submission = new Submission();

        when(submissionMapper.toSubmission(request)).thenReturn(submission);
        when(submissionRepository.save(submission)).thenReturn(submission);

        Submission result = submissionService.createSubmission(request);

        assertNotNull(result);
        verify(submissionRepository, times(1)).save(submission);
    }
}