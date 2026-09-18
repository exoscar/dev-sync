package org.devsync.spring.dashboard.event;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.cache.MemberStatisticsCache;
import org.devsync.spring.cache.ProjectDashboardCache;
import org.devsync.spring.cache.WorkspaceDashboardCache;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DashboardCacheInvalidationListener {

    private final WorkspaceDashboardCache workspaceDashboardCache;
    private final ProjectDashboardCache projectDashboardCache;
    private final MemberStatisticsCache memberStatisticsCache;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(DashboardCacheInvalidationEvent event) {

        workspaceDashboardCache.evict(event.workspaceId());

        if (event.projectId() != null) {
            projectDashboardCache.evict(event.projectId());
        }

        memberStatisticsCache.evict(event.workspaceId());
    }
}
