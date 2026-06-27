package org.devsync.spring.comment.event;

import java.util.UUID;

public record CommentCreatedEvent(UUID issueId,
                                  UUID commentId,
                                  UUID actorId,
                                  UUID workspaceId,
                                  UUID projectId,
                                  String firstName,
                                  String issueTitle) {
}
