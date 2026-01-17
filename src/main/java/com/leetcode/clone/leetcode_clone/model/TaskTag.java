package com.leetcode.clone.leetcode_clone.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskTag {

    @EmbeddedId
    @Builder.Default
    private TaskTagId id = new TaskTagId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;
}