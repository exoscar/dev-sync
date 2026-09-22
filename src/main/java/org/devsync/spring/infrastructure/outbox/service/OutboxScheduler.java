package org.devsync.spring.infrastructure.outbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxPublisher outboxPublisher;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {
        outboxPublisher.publishPendingEvents();
    }
}