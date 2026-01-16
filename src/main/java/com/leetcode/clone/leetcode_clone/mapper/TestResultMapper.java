package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testResult.TestResultResponseDTO;
import com.leetcode.clone.leetcode_clone.model.TestResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestResultMapper {

    @Mapping(target = "testCaseId", source = "testCase.id")
    @Mapping(target = "submissionId", source = "submission.id")
    TestResultResponseDTO toResponseDTO(TestResult testResult);

    List<TestResultResponseDTO> toResponseDTOList(List<TestResult> testResults);

    default TestResultPageResponseDTO toPageResponseDTO(Page<TestResult> page) {
        return new TestResultPageResponseDTO(
                page.getContent().stream().map(this::toResponseDTO).toList(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "testCase", ignore = true)
    @Mapping(target = "submission", ignore = true)
    TestResult toTestResult(TestResultRequestDTO testResultRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "testCase", ignore = true)
    @Mapping(target = "submission", ignore = true)
    void updateFromRequestDTO(@MappingTarget TestResult testResult, TestResultRequestDTO dto);
}
