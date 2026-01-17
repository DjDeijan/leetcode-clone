package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.testCase.TestCasePageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseResponseDTO;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestCaseMapper {

    @Mapping(
            target = "taskId",
            expression = "java(testCase.getTask().getId())"
    )
    TestCaseResponseDTO toResponseDTO(TestCase testCase);

    List<TestCaseResponseDTO> toResponseDTOList(List<TestCase> testCases);

//    You only need ONE DTO → entity method, and it should always ignore task
//    because the service is responsible for loading and setting it. Code right below is not needed:
//    @Mapping(target = "id", ignore = true)
//    TestCase toTestCase(TestCaseRequestDTO testCaseRequestDTO);

    @Mapping(target = "id", ignore = true)
    void updateTestCaseFromRequestDTO(@MappingTarget TestCase testCase, TestCaseRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true) // set in service
    TestCase toEntity(TestCaseRequestDTO dto);

    default TestCasePageResponseDTO toTestCasePageResponseDTO(Page<TestCase> page) {
        return new TestCasePageResponseDTO(
                page.getContent().stream().map(this::toResponseDTO).toList(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
}

