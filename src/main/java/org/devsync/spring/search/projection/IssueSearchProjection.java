package org.devsync.spring.search.projection;

import org.devsync.spring.issue.entity.IssueStatus;

import java.util.UUID;

public interface IssueSearchProjection {
    UUID getId();
    String getTitle();
    IssueStatus getStatus();
    UUID getProjectId();
    String getProjectName();
    Float getRank();
}
