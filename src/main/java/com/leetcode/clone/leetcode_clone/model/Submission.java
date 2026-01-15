package com.leetcode.clone.leetcode_clone.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "submission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "source_code", nullable = false)
    private String sourceCode;

    @Column(name = "language_id", nullable = false)
    private String languageId;

    private String status;
    private String grade;

    @Column(columnDefinition = "TEXT")
    private String errors;
}
