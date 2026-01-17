package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/sql/user_data.sql"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_returnsUser_whenExists() {
        Optional<User> user = userRepository.findByUsername("testuser");
        assertTrue(user.isPresent());
    }

    @Test
    void existsByUsername_returnsTrue_whenExists() {
        boolean exists = userRepository.existsByUsername("testuser");
        assertTrue(exists);
    }
}
