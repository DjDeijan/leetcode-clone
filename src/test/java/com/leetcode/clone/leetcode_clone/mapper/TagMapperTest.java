package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.tag.TagRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.tag.TagResponseDTO;
import com.leetcode.clone.leetcode_clone.model.Tag;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class TagMapperTest {

    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    @Test
    void toResponseDTO_mapsFieldsCorrectly() {
        Tag tag = Tag.builder()
                .id(1L)
                .name("Dynamic Programming")
                .build();

        TagResponseDTO dto = mapper.toTagResponseDTO(tag);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Dynamic Programming");
    }

    @Test
    void toEntity_doesNotSetId() {
        TagRequestDTO dto = new TagRequestDTO("Graph");

        Tag tag = mapper.toTag(dto);

        assertThat(tag.getId()).isNull();
        assertThat(tag.getName()).isEqualTo("Graph");
    }
}