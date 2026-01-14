package com.leetcode.clone.leetcode_clone.controller;

import com.leetcode.clone.leetcode_clone.config.security.CustomUserPrincipal;
import com.leetcode.clone.leetcode_clone.config.security.JwtProvider;
import com.leetcode.clone.leetcode_clone.dto.auth.LoginRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.auth.LoginResponseDTO;
import com.leetcode.clone.leetcode_clone.dto.auth.RegisterRequestDTO;
import com.leetcode.clone.leetcode_clone.dto.auth.RegisterResponseDTO;
import com.leetcode.clone.leetcode_clone.mapper.UserMapper;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                )
        );

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        String token = jwtProvider.generateToken(
                principal.getEmail(),
                principal.getAuthorities().iterator().next().getAuthority()
        );

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO) {

        User registeredUser = userService.registerUser(registerRequestDTO);

        String token = jwtProvider.generateToken(
                registeredUser.getEmail(),
                "ROLE_" + registeredUser.getRole().name()
        );

        RegisterResponseDTO registerResponseDTO =
                userMapper.toRegisterResponseDTO(registeredUser, token);

        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }

}
