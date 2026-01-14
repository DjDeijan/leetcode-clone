package com.leetcode.clone.leetcode_clone.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = {"/sql/user_data.sql"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
}
