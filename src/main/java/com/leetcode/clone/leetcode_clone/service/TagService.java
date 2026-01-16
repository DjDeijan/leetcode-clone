package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.dto.tag.TagRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.DuplicateResourceException;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.TagMapper;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional
    public Tag createTag(TagRequestDTO dto) {
        if (tagRepository.existsByName(dto.name())) {
            throw new DuplicateResourceException(Tag.class, "name", dto.name());
        }
        Tag tag = tagMapper.toTag(dto);
        return tagRepository.save(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagOrThrow(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, id));
    }

    @Transactional
    public Tag updateTag(Long id, TagRequestDTO dto) {
        Tag tag = getTagOrThrow(id);

        if (!tag.getName().equals(dto.name()) && tagRepository.existsByName(dto.name())) {
            throw new DuplicateResourceException(Tag.class, "name", dto.name());
        }

        tagMapper.updateTagFromRequestDTO(tag, dto);
        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException(Tag.class, id);
        }
        tagRepository.deleteById(id);
    }
}