package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.service.SubmissionService;
import com.leetcode.clone.leetcode_clone.mapper.SubmissionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
@Validated
@Tag(name = "Submission Controller", description = "Operations about submissions")
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
    public ResponseEntity<Page<SubmissionResponseDTO>> getAll(@ParameterObject @PageableDefault(size = 2, sort = "id") Pageable pageable) {
        Page<SubmissionResponseDTO> page = submissionService.getAllSubmissions(pageable)
                .map(submissionMapper::toResponseDTO);
        return ResponseEntity.ok(page);
    }
}