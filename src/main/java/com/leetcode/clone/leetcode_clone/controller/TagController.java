package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.tag.TagRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.tag.TagResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.TagMapper;
import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@Validated
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Controller", description = "Operations about problem tags")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new tag")
    public ResponseEntity<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO dto) {
        Tag tag = tagService.createTag(dto);
        return new ResponseEntity<>(tagMapper.toTagResponseDTO(tag), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "View all tags")
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tagMapper.toTagResponseDTOList(tags));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get tag by ID")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable @Positive Long id) {
        Tag tag = tagService.getTagOrThrow(id);
        return ResponseEntity.ok(tagMapper.toTagResponseDTO(tag));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a tag")
    public ResponseEntity<TagResponseDTO> updateTag(@PathVariable @Positive Long id, @Valid @RequestBody TagRequestDTO dto) {
        Tag updatedTag = tagService.updateTag(id, dto);
        return ResponseEntity.ok(tagMapper.toTagResponseDTO(updatedTag));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<Void> deleteTag(@PathVariable @Positive Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}