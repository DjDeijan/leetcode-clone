package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.dto.user.UserNameProjectionDTO;
import com.leetcode.clone.leetcode_clone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/sql/user_data.sql"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_returnsUser_whenExists() {
        List<UserNameProjectionDTO> users = userRepository.findByUsername("testuser");
        assertFalse(users.isEmpty());
        assertEquals("testuser", users.get(0).username());
    }

    @Test
    void existsByEmail_returnsTrue_whenExists() {
        boolean exists = userRepository.existsByEmail("test@example.com");
        assertTrue(exists);
    }
}
