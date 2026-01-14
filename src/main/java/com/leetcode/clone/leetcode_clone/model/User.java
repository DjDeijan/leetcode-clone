package com.leetcode.clone.leetcode_clone.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    // Cloudinary Image public_id
    @Column(name = "profile_img_public_id", unique = true)
    private String profileImgPublicId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @PrePersist
    public void prePersist() {
        if (role == null) {
            role = Role.USER; // Default role before saving in DB
        }
    }

}
