package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "errors", ignore = true)
    @Mapping(target = "task.id", source = "taskId")
    @Mapping(target = "user.id", source = "userId")
    Submission toSubmission(SubmissionRequestDTO dto);

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "userId", source = "user.id")
    SubmissionResponseDTO toResponseDTO(Submission submission);
}