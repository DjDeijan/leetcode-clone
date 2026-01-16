package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.tag.TagRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TagMapper;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceTest {

    private TagRepository tagRepository;
    private TagMapper tagMapper;
    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        tagMapper = mock(TagMapper.class);
        tagService = new TagService(tagRepository, tagMapper);
    }

    @Test
    void createTag_savesAndReturnsTag() {
        TagRequestDTO dto = new TagRequestDTO("Graph");
        Tag mappedTag = Tag.builder().name("Graph").build();
        Tag savedTag = Tag.builder().id(1L).name("Graph").build();

        when(tagRepository.existsByName("Graph")).thenReturn(false);
        when(tagMapper.toTag(dto)).thenReturn(mappedTag);
        when(tagRepository.save(mappedTag)).thenReturn(savedTag);

        Tag result = tagService.createTag(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(tagRepository).save(mappedTag);
    }

    @Test
    void getTagOrThrow_throwsException_whenNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.getTagOrThrow(1L));
    }
}