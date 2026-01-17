package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.tag.TagResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TagMapper;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.service.CustomUserDetailsService;
import com.leetcode.clone.leetcode_clone.service.TagService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TagService tagService;

    @MockitoBean
    private TagMapper tagMapper;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getAllTags_returnsList() throws Exception {
        Tag tag = Tag.builder().id(1L).name("Math").build();
        TagResponseDTO dto = new TagResponseDTO(1L, "Math", null);

        Mockito.when(tagService.getAllTags()).thenReturn(List.of(tag));
        Mockito.when(tagMapper.toTagResponseDTOList(Mockito.anyList())).thenReturn(List.of(dto));

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Math"));
    }
}