package org.devsync.spring.infrastructure.kafka.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class KafkaMetrics {
    private final Counter notificationSuccess;
    private final Counter notificationFailure;
    private final Timer notificationProcessingTime;

    public KafkaMetrics(MeterRegistry registry) {
        notificationSuccess = Counter.builder(
                        "devsync.kafka.notification.success")
                .description("Successfully processed Kafka notifications")
                .register(registry);

        notificationFailure = Counter.builder(
                        "devsync.kafka.notification.failure")
                .description("Failed Kafka notification processing")
                .register(registry);

        notificationProcessingTime = Timer.builder(
                        "devsync.kafka.notification.processing")
                .description("Kafka notification processing time")
                .register(registry);
    }

    public void recordSuccess() {
        notificationSuccess.increment();
    }

    public void recordFailure() {
        notificationFailure.increment();
    }

    public Timer processingTimer() {
        return notificationProcessingTime;
    }
}
