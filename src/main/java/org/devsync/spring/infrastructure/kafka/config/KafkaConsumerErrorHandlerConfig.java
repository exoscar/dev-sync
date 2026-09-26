package org.devsync.spring.infrastructure.kafka.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerErrorHandlerConfig {

    /*
     * Blocking Retry Mechanism
     *
     * */

    // below code commented because to using non blocking retry mechanism

//    @Bean
//    public DefaultErrorHandler kafkaErrorHandler(
//            DeadLetterPublishingRecoverer recoverer
//    ) {
//
//        FixedBackOff backOff = new FixedBackOff(
//                1000L, // 1 second between retries
//                2L     // 2 retries after the initial attempt
//        );
//
//        return new DefaultErrorHandler(recoverer,backOff);
//    }
//
//    @Bean
//    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
//            KafkaTemplate<String, Object> kafkaTemplate
//    ) {
//        return new DeadLetterPublishingRecoverer(
//                kafkaTemplate,
//                (record, exception) -> new TopicPartition(
//                        record.topic() + ".DLT", record.partition()
//                )
//        );
//    }
}