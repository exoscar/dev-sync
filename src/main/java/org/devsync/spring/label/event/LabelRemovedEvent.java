package org.devsync.spring.label.event;

import java.util.UUID;

public record LabelRemovedEvent(UUID issueId,
                                UUID labelId,
                                UUID actorId,
                                UUID workspaceId,
                                UUID projectId,
                                String labelName,
                                String issueTitle) {
}
