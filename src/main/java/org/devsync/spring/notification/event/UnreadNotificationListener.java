package org.devsync.spring.notification.event;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.cache.UnreadNotificationCache;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UnreadNotificationListener {

    private final UnreadNotificationCache unreadNotificationCache;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotificationChange(UnreadNotificationEvent event) {
        unreadNotificationCache.evict(event.userId());
    }
}
