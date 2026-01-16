package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.service.SubmissionService;
import com.leetcode.clone.leetcode_clone.mapper.SubmissionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;

    @PostMapping
    public ResponseEntity<SubmissionResponseDTO> create(@Valid @RequestBody SubmissionRequestDTO dto) {
        Submission submission = submissionService.createSubmission(dto);
        return new ResponseEntity<>(submissionMapper.toResponseDTO(submission), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponseDTO> getById(@PathVariable Long id) {
        Submission submission = submissionService.getSubmissionOrThrow(id);
        return ResponseEntity.ok(submissionMapper.toResponseDTO(submission));
    }

    @GetMapping
    public ResponseEntity<Page<SubmissionResponseDTO>> getAll(Pageable pageable) {
        Page<SubmissionResponseDTO> page = submissionService.getAllSubmissions(pageable)
                .map(submissionMapper::toResponseDTO);
        return ResponseEntity.ok(page);
    }
}