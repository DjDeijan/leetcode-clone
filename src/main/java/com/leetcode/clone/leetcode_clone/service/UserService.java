package com.leetcode.clone.leetcode_clone.service;

import com.leetcode.clone.leetcode_clone.config.security.CustomUserPrincipal;
import com.leetcode.clone.leetcode_clone.dto.user.UserNameProjectionDTO;
import com.leetcode.clone.leetcode_clone.dto.auth.RegisterRequestDTO;
import com.leetcode.clone.leetcode_clone.exception.DuplicateResourceException;
import com.leetcode.clone.leetcode_clone.exception.ResourceNotFoundException;
import com.leetcode.clone.leetcode_clone.mapper.UserMapper;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.model.Role;
import com.leetcode.clone.leetcode_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterRequestDTO registerRequestDTO) {

        log.debug("Checking if email={} already exists", registerRequestDTO.email());
        // Prevent duplicate accounts for same email (before DB throws for unique column constraint)
        if (userRepository.existsByEmail(registerRequestDTO.email()))
            throw new DuplicateResourceException(User.class, "email", registerRequestDTO.email());

        User user = userMapper.toUser(registerRequestDTO);

        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));

        user.setRole(Role.USER);

        log.info("New user registered with email:{}", registerRequestDTO.email());
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) userRepository.deleteById(id);
        else throw new ResourceNotFoundException(User.class, id);

    }

    @Transactional
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("No authenticated user found in security context");
        }

        CustomUserPrincipal principal = (CustomUserPrincipal) auth.getPrincipal();
        return getUserOrThrow(principal.getUserId());
    }

    @Transactional
    public User patchUserFirstName(Long id, String newUsername) {
        User fetchedUser = getUserOrThrow(id);
        fetchedUser.setUsername(newUsername);
        return userRepository.save(fetchedUser);
    }

    @Transactional
    public User putUser(Long id, RegisterRequestDTO userRequestDTO) {
        User fetchedUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.class, id));

        userMapper.updateUserFromRequestDTO(fetchedUser, userRequestDTO);

        return userRepository.save(fetchedUser);
    }

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }

    public List<UserNameProjectionDTO> getUserNamesByUsername(String firstName) {
        return userRepository.findByUsername(firstName);
    }

    @Transactional
    public User patchUserAvatar(Long userId, MultipartFile avatarImage) {
        User fetchedUser = getUserOrThrow(userId);
        String imagePublicId = cloudinaryService.uploadImage(avatarImage);
        fetchedUser.setProfileImgPublicId(imagePublicId);
        return userRepository.save(fetchedUser);
    }

    @Transactional
    public void deleteUserAvatar(Long userId) {
        User fetchedUser = getUserOrThrow(userId);
        cloudinaryService.deleteImage(fetchedUser.getProfileImgPublicId());
        fetchedUser.setProfileImgPublicId(null);
        userRepository.save(fetchedUser);
    }



    /**
     * Updates User's Role.
     *
     * @param userId id of the user account's PP to change
     * @param newRole    the new role to be assigned
     * @return The user with edited role
     * @throws IllegalStateException if the user is trying to update their own role
     //* @see #patchUserAvatar(Long, MultipartFile)
     * @since Exercise 11
     */
//     Showcase @deprecated Javadoc Annotation:
//    * @deprecated Use {@link #deleteUserAvatar(Long)} instead.
    @Transactional
    public User patchUserRole(Long userId, Role newRole) {
        User fetchedUser = getUserOrThrow(userId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal principal = (CustomUserPrincipal) auth.getPrincipal();
        Long currentUserId = principal.getUserId();

        if (fetchedUser.getId().equals(currentUserId)) {
            throw new IllegalStateException("You cannot change your own role");
        }

        fetchedUser.setRole(newRole);
        return userRepository.save(fetchedUser);
    }

}
