package org.devsync.spring.issue.projection;

import org.devsync.spring.issue.entity.IssuePriority;

public interface IssuePriorityCountProjection {
    IssuePriority getPriority();

    long getCount();
}
