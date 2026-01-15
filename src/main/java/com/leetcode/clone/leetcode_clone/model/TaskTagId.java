package com.leetcode.clone.leetcode_clone.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskTagId implements Serializable {

    private Long taskId;
    private Long tagId;
}
