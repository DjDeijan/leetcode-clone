package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.tag.TagRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.tag.TagResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    TagResponseDTO toTagResponseDTO(Tag tag);

    List<TagResponseDTO> toTagResponseDTOList(List<Tag> tags);

    @Mapping(target = "id", ignore = true)
    Tag toTag(TagRequestDTO tagRequestDTO);

    void updateTagFromRequestDTO(@MappingTarget Tag tag, TagRequestDTO dto);
}