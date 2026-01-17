package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TestCaseMapper;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.repository.TaskRepository;
import com.leetcode.clone.leetcode_clone.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TaskRepository taskRepository;
    private final TestCaseMapper testCaseMapper;

    @Transactional
    public TestCase createTestCase(Long taskId, TestCaseRequestDTO dto) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId));

        TestCase testCase = testCaseMapper.toEntity(dto);
        testCase.setTask(task);

        log.info(
                "New testcase created taskId={} timeLimitMs={} memoryLimitKb={} stackLimitKb={}",
                //testCase.getTask().getId(),
                task.getId(),
                testCase.getTimeLimitMs(),
                testCase.getMemoryLimitKb(),
                testCase.getStackLimitKb()
        );

        return testCaseRepository.save(testCase);
    }

    public TestCase getTestCaseOrThrow(Long id) {
        return testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TestCase.class, id));
    }

    public Page<TestCase> getAllTestCases(Pageable pageable) {
        return testCaseRepository.findAll(pageable);
    }

    public Page<TestCase> getAllTestCasesByTaskId(Long taskId, Pageable pageable) {
        return testCaseRepository.findAllByTaskId(taskId, pageable);
    }

    @Transactional
    public void deleteTestCase(Long id) {
        if (testCaseRepository.existsById(id)) testCaseRepository.deleteById(id);
        else throw new ResourceNotFoundException(TestCase.class, id);
    }

    @Transactional
    public TestCase putTestCase(Long id, TestCaseRequestDTO testcaseRequestDTO) {
        TestCase existing = getTestCaseOrThrow(id);
        testCaseMapper.updateTestCaseFromRequestDTO(existing, testcaseRequestDTO);
        return testCaseRepository.save(existing);
    }

    @Transactional
    public TestCase patchExpectedOutput(Long id, String newExpectedOutput) {
        TestCase existing = getTestCaseOrThrow(id);
        existing.setExpectedOutput(newExpectedOutput);
        return testCaseRepository.save(existing);
    }
}
