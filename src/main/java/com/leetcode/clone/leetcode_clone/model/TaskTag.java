package com.leetcode.clone.leetcode_clone.model;

import com.leetcode.clone.leetcode_clone.model.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_tag")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
public class TaskTag {

    @EmbeddedId
    private TaskTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
