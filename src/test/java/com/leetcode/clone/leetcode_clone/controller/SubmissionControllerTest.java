package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.submission.SubmissionResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.SubmissionMapper;
import com.leetcode.clone.leetcode_clone.model.Submission;
import com.leetcode.clone.leetcode_clone.service.CustomUserDetailsService;
import com.leetcode.clone.leetcode_clone.service.Judge0Service;
import com.leetcode.clone.leetcode_clone.service.SubmissionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubmissionService submissionService;

    @MockitoBean
    private Judge0Service judge0Service;

    @MockitoBean
    private SubmissionMapper submissionMapper;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getSubmissionById_returnsDto() throws Exception {
        Long id = 1L;
        Submission sub = Submission.builder().id(id).build();
        SubmissionResponseDTO dto = new SubmissionResponseDTO(
                id, 1L, "print(1)", "51", "Accepted", "0", "", 1L
        );

        Mockito.when(submissionService.getSubmissionOrThrow(id)).thenReturn(sub);
        Mockito.when(submissionMapper.toResponseDTO(sub)).thenReturn(dto);

        mockMvc.perform(get("/submissions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sourceCode").value("print(1)"))
                .andExpect(jsonPath("$.status").value("Accepted"));
    }
}