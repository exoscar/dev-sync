package org.devsync.spring.search.dto;

import lombok.Builder;
import org.devsync.spring.issue.entity.IssueStatus;

import java.util.UUID;

@Builder
public record IssueSearchResult(
        UUID id,
        String title,
        IssueStatus status,
        UUID projectId,
        String projectName
) {}
