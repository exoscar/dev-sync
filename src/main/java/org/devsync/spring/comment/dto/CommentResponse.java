package org.devsync.spring.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private UUID id;

    private String content;

    private UUID authorId;
    private String authorEmail;

    private UUID issueId;

    private Instant createdAt;

}
