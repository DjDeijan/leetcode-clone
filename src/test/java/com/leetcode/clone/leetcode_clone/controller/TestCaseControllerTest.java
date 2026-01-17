package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCasePageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.testCase.TestCaseResponseDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TestCaseMapper;
import com.leetcode.clone.leetcode_clone.model.Task;
import com.leetcode.clone.leetcode_clone.model.TestCase;
import com.leetcode.clone.leetcode_clone.service.CustomUserDetailsService;
import com.leetcode.clone.leetcode_clone.service.TestCaseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestCaseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestCaseService testcaseService;

    @MockitoBean
    private TestCaseMapper testcaseMapper;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // ============= getTestcaseById Tests =============
    @Test
    void getTestcaseById_returnsMappedDto() throws Exception {
        Long id = 1L;

        Task task = Task.builder().id(10L).build();

        TestCase testcase = TestCase.builder()
                .id(id)
                //.taskId(10L)
                .task(task)
                .input("1\n")
                .expectedOutput("1\n")
                .timeLimitMs(1000)
                .memoryLimitKb(256000)
                .stackLimitKb(64000)
                .build();

        TestCaseResponseDTO responseDTO = new TestCaseResponseDTO(
                id,
                10L,
                "1\n",
                "1\n",
                1000,
                256000,
                64000
        );

        Mockito.when(testcaseService.getTestCaseOrThrow(id)).thenReturn(testcase);
        Mockito.when(testcaseMapper.toResponseDTO(testcase)).thenReturn(responseDTO);

        mockMvc.perform(get("/testcases/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDTO.id().intValue()))
                .andExpect(jsonPath("$.taskId").value(responseDTO.taskId().intValue()))
                .andExpect(jsonPath("$.input").value(responseDTO.input()))
                .andExpect(jsonPath("$.expectedOutput").value(responseDTO.expectedOutput()));
    }

    @Test
    void getTestcaseById_whenNotFound_returns404() throws Exception {
        Long id = 999L;

        Mockito.when(testcaseService.getTestCaseOrThrow(id))
                .thenThrow(new ResourceNotFoundException(TestCase.class, id));

        mockMvc.perform(get("/testcases/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.resource").value(TestCase.class.getSimpleName()));
    }

    // ============= getAllTestcases Tests =============
    @Test
    void getAllTestcases_returnsMappedDtoPage() throws Exception {
        Task task = Task.builder().id(1L).build();

        TestCase tc1 = TestCase.builder().id(1L).task(task).input("A").expectedOutput("B").build();
        TestCase tc2 = TestCase.builder().id(2L).task(task).input("C").expectedOutput("D").build();
        List<TestCase> testcases = List.of(tc1, tc2);

        TestCaseResponseDTO dto1 = new TestCaseResponseDTO(1L, 1L, "A", "B", null, null, null);
        TestCaseResponseDTO dto2 = new TestCaseResponseDTO(2L, 1L, "C", "D", null, null, null);

        Pageable pageable = PageRequest.of(0, 20);
        Page<TestCase> page = new PageImpl<>(testcases, pageable, testcases.size());

        TestCasePageResponseDTO wrapper = new TestCasePageResponseDTO(
                List.of(dto1, dto2),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );

        Mockito.when(testcaseService.getAllTestCases(any(Pageable.class))).thenReturn(page);
        Mockito.when(testcaseMapper.toTestCasePageResponseDTO(page)).thenReturn(wrapper);

        mockMvc.perform(get("/testcases")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.testcases[0].id").value(dto1.id().intValue()))
                .andExpect(jsonPath("$.testcases[0].taskId").value(dto1.taskId().intValue()))
                .andExpect(jsonPath("$.testcases[1].id").value(dto2.id().intValue()))
                .andExpect(jsonPath("$.testcases[1].taskId").value(dto2.taskId().intValue()));
    }

    // ============= createTestcase Tests =============
    @Test
    void createTestcase_returns201_andMappedDto() throws Exception {

        Task task = Task.builder().id(7L).build();

        TestCaseRequestDTO requestDTO = new TestCaseRequestDTO(
                "1 2\n",
                "3\n",
                1000,
                256000,
                64000
        );

        TestCase created = TestCase.builder()
                .id(10L)
                //.taskId(requestDTO.taskId()) => because the model doesn't use primitives anymore (Task task)
                .task(task)
                .input(requestDTO.input())
                .expectedOutput(requestDTO.expectedOutput())
                .timeLimitMs(requestDTO.timeLimitMs())
                .memoryLimitKb(requestDTO.memoryLimitKb())
                .stackLimitKb(requestDTO.stackLimitKb())
                .build();

        Long taskId = 1L;
        TestCaseResponseDTO responseDTO = new TestCaseResponseDTO(
                10L,
                taskId,
                requestDTO.input(),
                requestDTO.expectedOutput(),
                requestDTO.timeLimitMs(),
                requestDTO.memoryLimitKb(),
                requestDTO.stackLimitKb()
        );

        //Mockito.when(testcaseService.createTestCase(any(TestCaseRequestDTO.class))).thenReturn(created);
        Mockito.when(testcaseService.createTestCase(
                //anyLong(),
                eq(7L),
                any(TestCaseRequestDTO.class)
        )).thenReturn(created);
        Mockito.when(testcaseMapper.toResponseDTO(created)).thenReturn(responseDTO);

        String json = """
                {
                  \"taskId\": 7,
                  \"input\": \"1 2\\n\",
                  \"expectedOutput\": \"3\\n\",
                  \"timeLimitMs\": 1000,
                  \"memoryLimitKb\": 256000,
                  \"stackLimitKb\": 64000
                }
                """;

        mockMvc.perform(post("/testcases/tasks/{taskId}/test-cases", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.taskId").value(7))
                .andExpect(jsonPath("$.input").value("1 2\n"))
                .andExpect(jsonPath("$.expectedOutput").value("3\n"))
                .andExpect(jsonPath("$.timeLimitMs").value(1000))
                .andExpect(jsonPath("$.memoryLimitKb").value(256000))
                .andExpect(jsonPath("$.stackLimitKb").value(64000));
    }
}

