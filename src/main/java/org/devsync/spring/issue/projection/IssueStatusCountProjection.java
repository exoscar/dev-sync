package org.devsync.spring.issue.projection;

import org.devsync.spring.issue.entity.IssueStatus;

public interface IssueStatusCountProjection {

    IssueStatus getStatus();

    long getCount();
}