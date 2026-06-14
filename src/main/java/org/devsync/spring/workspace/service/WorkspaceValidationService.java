package org.devsync.spring.workspace.service;

import org.devsync.spring.common.util.Utils;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WorkspaceValidationService {
    public UUID parseWorkspaceId(String id) {
        return Utils.parseUuid(id, "Invalid Workspace Id");
    }
}
