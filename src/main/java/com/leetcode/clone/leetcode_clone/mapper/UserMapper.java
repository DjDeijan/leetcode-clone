package com.leetcode.clone.leetcode_clone.mapper;

import com.leetcode.clone.leetcode_clone.dto.auth.RegisterRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.auth.RegisterResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.user.*;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.repository.projection.UserNameProjection;
import com.leetcode.clone.leetcode_clone.service.CloudinaryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Autowired
    private  CloudinaryService cloudinaryService;

    @Mapping(target = "profileImage",
            source = "profileImgPublicId",
            qualifiedByName = "profileImgUrl")
    public abstract UserResponseDTO toUserResponseDTO(User user);

    public abstract List<UserResponseDTO> toUserResponseDTOList(List<User> users);

    @Named("profileImgUrl")
    protected String generateProfileImgUrl(String profileImgPublicId) {
        if (profileImgPublicId == null || profileImgPublicId.isBlank()) return null;
        return cloudinaryService.generateUrl(profileImgPublicId, 200, 200); // Can adjust image size
    }

    public UserPageResponseDTO toUserPageResponseDTO(Page<User> page) {
        return new UserPageResponseDTO(
                page.getContent().stream().map(this::toUserResponseDTO).toList(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Mapping(target = "jwtToken", source = "jwtToken")
    @Mapping(target = "userData", source = "user")
    public abstract RegisterResponseDTO toRegisterResponseDTO(User user, String jwtToken);

    public abstract UserSummaryResponseDTO toUserSummaryResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    public abstract User toUser(RegisterRequestDTO registerRequestDTO);

    public abstract List<UserNameResponseDTO> toUserNameResponseDTOList(List<UserNameProjection> userNameProjections);

    public abstract List<UserNameResponseDTO> toUserNameResponseDTOListFromProjectionDTOList(List<UserNameProjectionDTO> userNameProjectionDTOs);

    public abstract void updateUserFromRequestDTO(@MappingTarget User user, RegisterRequestDTO dto);

    // Showcasing alternative method to trim endpoint inputs
    /*
    @AfterMapping
    protected void trimUserFields(@MappingTarget User entity, UserRequestDTO dto) {
        entity.setFirstName(dto.firstName().trim());
        entity.setLastName(dto.lastName().trim());
        if (entity.getPhoneNumber() != null) entity.setPhoneNumber(dto.phoneNumber().trim());
    }
    */

}
