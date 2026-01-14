package com.leetcode.clone.leetcode_clone.repository;

import com.leetcode.clone.leetcode_clone.dto.user.UserNameProjectionDTO;
import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.repository.projection.UserNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT new com.leetcode.clone.leetcode_clone.dto.user.UserNameProjectionDTO(e.username) FROM User e WHERE e.username = :username")
    List<UserNameProjectionDTO> findByUsername(@Param("username") String username);
    // -------------------

}
