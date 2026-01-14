package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.dto.user.UserNameProjectionDTO;
import com.leetcode.clone.leetcode_clone.dto.auth.RegisterRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.user.UserPageResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.user.UserResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.user.UserNameResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.UserMapper;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.model.Role;
import com.leetcode.clone.leetcode_clone.repository.projection.UserNameProjection;
import com.leetcode.clone.leetcode_clone.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "Operations about user users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN') or #id == principal.userId")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable @Positive Long id) {
        User fetchedUser = userService.getUserOrThrow(id);
        return new ResponseEntity<>(userMapper.toUserResponseDTO(fetchedUser), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<UserPageResponseDTO> getAllUsers(
            @ParameterObject @PageableDefault(size = 2, sort = "id") Pageable pageable
    ) {
        Page<User> page = userService.getAllUsers(pageable);
        return new ResponseEntity<>(userMapper.toUserPageResponseDTO(page), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.userId")
    @PatchMapping("/{id}/firstName")
    public ResponseEntity<UserResponseDTO> patchUserFirstName(@PathVariable @Positive Long id,
                                                                      @RequestParam @NotBlank @Size(min = 2, max = 20) String newFirstName) {
        User patchedUser = userService.patchUserFirstName(id, newFirstName);
        return new ResponseEntity<>(userMapper.toUserResponseDTO(patchedUser), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.userId")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> putUser(@PathVariable @Positive Long id,
                                                           @Valid @RequestBody RegisterRequestDTO userRequestDTO) {
        User newUser = userService.putUser(id, userRequestDTO);
        return new ResponseEntity<>(userMapper.toUserResponseDTO(newUser), HttpStatus.OK);
    }

    @GetMapping("/filter/firstName")
    public ResponseEntity<List<UserNameResponseDTO>> getUsersByFirstName(@RequestParam @NotBlank String firstName) {
        List<UserNameProjectionDTO> userNameProjectionDTOs = userService.getUserNamesByUsername(firstName);
        return new ResponseEntity<>(userMapper.toUserNameResponseDTOListFromProjectionDTOList(userNameProjectionDTOs), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.userId")
    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> patchUserAvatar(@PathVariable @Positive Long id,
                                                                   @RequestParam @NotBlank MultipartFile file) {
        User patchedUser = userService.patchUserAvatar(id, file);
        return new ResponseEntity<>(userMapper.toUserResponseDTO(patchedUser), HttpStatus.OK);
    }

    @Operation(summary = "Delete user's profile picture (avatar) (Requires: ADMIN/SAME USER)")
    @ApiResponse(responseCode = "200", description = "Avatar deleted")
    @ApiResponse(responseCode = "403", description = "User Unauthorized")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.userId")
    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Void> deleteUserAvatar(@PathVariable @Positive Long id) {
        userService.deleteUserAvatar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Change user's role (Requires: ADMIN)",
            description = "Returns the user with updated role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Role Updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "403",
                    description = "User Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> patchUserRole(
            @Parameter(description = "User id", example = "2", required = true)
            @PathVariable @Positive Long id,

            @Parameter(description = "Role to be assigned", example = "ADMIN", required = true)
            @RequestParam @NotBlank Role newRole) {
        User updatedUser = userService.patchUserRole(id, newRole);
        return new ResponseEntity<>(userMapper.toUserResponseDTO(updatedUser), HttpStatus.OK);
    }

}
