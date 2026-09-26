package org.devsync.spring.infrastructure.kafka.consumer;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.infrastructure.kafka.config.KafkaMetrics;
import org.devsync.spring.infrastructure.kafka.event.KafkaNotificationEvent;
import org.devsync.spring.notification.handler.KafkaNotificationHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaNotificationConsumer {

    private final KafkaNotificationHandler notificationHandler;
    private final MeterRegistry meterRegistry;
    private final KafkaMetrics kafkaMetrics;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2.0
            )
    )
    @KafkaListener(
            groupId = "devsync-notification-service",
            topics = "devsync.issue-events",
            concurrency = "3"
    )
    public void consume(KafkaNotificationEvent event) {

        Timer.Sample sample =
                Timer.start(meterRegistry);

        try {
            notificationHandler.handle(event);
            kafkaMetrics.recordSuccess();

        } catch (Exception e) {
            kafkaMetrics.recordFailure();
            throw e;

        } finally {
            sample.stop(
                    kafkaMetrics.processingTimer()
            );
        }
    }
}