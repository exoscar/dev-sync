package org.devsync.spring.issue.projection;

import java.util.UUID;

public interface MemberIssueStatsProjection {
    UUID getUserId();
    Long getCount();
}
