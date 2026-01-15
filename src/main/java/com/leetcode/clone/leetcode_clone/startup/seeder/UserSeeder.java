package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.model.Role;
import com.leetcode.clone.leetcode_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("dev")
@Order(1)
@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user1 = User.builder()
                .email("v.vasilev@abv.bg")
                .password(passwordEncoder.encode("myPass1234"))
                .role(Role.USER)
                .username("Valentin")
                .build();

        User user2 = User.builder()
                .email("ivpetrov@gmail.com")
                .password(passwordEncoder.encode("changeMe4321"))
                .role(Role.ADMIN)
                .username("Ivan")
                .build();

        User user3 = User.builder()
                .email("johnny4@abv.bg")
                .password(passwordEncoder.encode("secretPass1"))
                .role(Role.USER)
                .username("Johnny")
                .build();
        if (!userRepository.existsByEmail(user1.getEmail())) {
        userRepository.save(user1);
        }
        if (!userRepository.existsByEmail(user2.getEmail())) {
            userRepository.save(user2);
        }
        if (!userRepository.existsByEmail(user3.getEmail())) {
            userRepository.save(user3);
        }
        log.info("--- 3 Users were seeded! ---");
    }

}
