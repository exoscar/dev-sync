package org.devsync.spring.infrastructure.kafka.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerErrorHandlerConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            DeadLetterPublishingRecoverer recoverer
    ) {

        FixedBackOff backOff = new FixedBackOff(
                1000L, // 1 second between retries
                2L     // 2 retries after the initial attempt
        );

        return new DefaultErrorHandler(recoverer,backOff);
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        return new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, exception) -> new TopicPartition(
                        record.topic() + ".DLT", record.partition()
                )
        );
    }
}