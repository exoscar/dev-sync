package org.devsync.spring.workspace.event;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.cache.WorkspaceMembershipCache;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WorkspaceMembershipCacheListener {
    private final WorkspaceMembershipCache workspaceMembershipCache;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMembershipChange(WorkspaceMembershipChangedEvent event) {
        workspaceMembershipCache.evict(event.workspaceId(), event.userId());
    }
}
