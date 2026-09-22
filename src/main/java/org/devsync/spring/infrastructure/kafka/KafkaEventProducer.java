package org.devsync.spring.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, Object event) {

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, exception) -> {

                    if (exception != null) {
                        log.error(
                                "Failed to publish Kafka event",
                                exception
                        );
                        return;
                    }

                    log.info(
                            "Kafka event published: topic={}, partition={}, offset={}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset()
                    );
                });
    }
}
