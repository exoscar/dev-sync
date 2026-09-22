package org.devsync.spring.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.retry.annotation.CircuitBreaker;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic testEventsTopic(){
        return TopicBuilder
                .name("devsync.test-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic issueEventTopic(){
        return TopicBuilder
                .name("devsync.issue-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
