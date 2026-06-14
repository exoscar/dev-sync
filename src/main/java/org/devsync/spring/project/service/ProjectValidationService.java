package org.devsync.spring.project.service;

import org.devsync.spring.common.util.Utils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectValidationService {
    public UUID parseProjectId(String id) {
        return Utils.parseUuid(id, "Invalid Project Id");
    }
}
