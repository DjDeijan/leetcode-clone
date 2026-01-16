
package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.SubmissionMapper;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionMapper submissionMapper;

    @Transactional
    public Submission createSubmission(SubmissionRequestDTO dto) {
        Submission submission = submissionMapper.toSubmission(dto);
        return submissionRepository.save(submission);
    }

    public Submission getSubmissionOrThrow(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Submission.class, id));
    }

    public Page<Submission> getAllSubmissions(Pageable pageable) {
        return submissionRepository.findAll(pageable);
    }
}