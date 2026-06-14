package org.devsync.spring.dashboard.mapper;

import org.devsync.spring.dashboard.dto.PrioritySummary;
import org.devsync.spring.dashboard.dto.StatusSummary;
import org.devsync.spring.issue.projection.IssuePriorityCountProjection;
import org.devsync.spring.issue.projection.IssueStatusCountProjection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardMapper {
    public StatusSummary toStatusSummary(List<IssueStatusCountProjection> statusCounts) {
        long todo = 0;
        long inProgress = 0;
        long done = 0;
        for (IssueStatusCountProjection projection : statusCounts) {
            switch (projection.getStatus()) {
                case TODO -> todo = projection.getCount();
                case IN_PROGRESS -> inProgress = projection.getCount();
                case DONE -> done = projection.getCount();
            }
        }
        return StatusSummary.builder().todo(todo).inProgress(inProgress).done(done).build();
    }

    public PrioritySummary toPrioritySummary(List<IssuePriorityCountProjection> priorityCounts) {
        long low = 0;
        long medium = 0;
        long high = 0;
        long critical = 0;
        for (IssuePriorityCountProjection projection : priorityCounts) {
            switch (projection.getPriority()) {
                case LOW -> low = projection.getCount();
                case MEDIUM -> medium = projection.getCount();
                case HIGH -> high = projection.getCount();
                case CRITICAL -> critical = projection.getCount();
            }
        }
        return PrioritySummary.builder().low(low).medium(medium).high(high).critical(critical).build();
    }
}
