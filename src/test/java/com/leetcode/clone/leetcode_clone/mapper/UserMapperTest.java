package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.user.UserResponseDTO;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper underTest = Mappers.getMapper(UserMapper.class);

    //======================
    private static Stream<Arguments> userProvider() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .id(1L)
                                .username("John")
                                .build(),
                        1L, "HR"
                ),
                Arguments.of(
                        User.builder()
                                .id(2L)
                                .username("Alice")
                                .build(),
                        null, null
                ),
                Arguments.of(
                        User.builder()
                                .id(3L)
                                .username("Bob")
                                .build(),
                        2L, "IT"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("userProvider")
    void toUserResponseDTOTest(User user, Long expectedDeptId, String expectedDeptName) {
        UserResponseDTO result = underTest.toUserResponseDTO(user);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(user.getId());
        assertThat(result.username()).isEqualTo(user.getUsername());
    }
    //======================

    //======================
    private static Stream<Arguments> userListProvider() {
        List<User> users = List.of(
                User.builder()
                        .id(1L)
                        .username("John")
                        .build(),
                User.builder()
                        .id(2L)
                        .username("Alice")
                        .build(),
                User.builder()
                        .id(3L)
                        .username("Bob")
                        .build()
        );

        return Stream.of(Arguments.of(users));
    }

    @ParameterizedTest
    @MethodSource("userListProvider")
    void toUserResponseDTOListTest(List<User> users) {
        // When
        List<UserResponseDTO> result = underTest.toUserResponseDTOList(users);

        // Then
        assertThat(result).hasSize(users.size());

        for (int i = 0; i < users.size(); i++) {
            User source = users.get(i);
            UserResponseDTO dto = result.get(i);

            // Basic fields
            assertThat(dto.id()).isEqualTo(source.getId());
            assertThat(dto.username()).isEqualTo(source.getUsername());
        }
    }
    //======================

}
