ALTER TABLE processed_kafka_events
DROP
CONSTRAINT processed_kafka_events_pkey;

ALTER TABLE processed_kafka_events
    ADD COLUMN consumer_type VARCHAR(50);

UPDATE processed_kafka_events
SET consumer_type = 'NOTIFICATION'
WHERE consumer_type IS NULL;

ALTER TABLE processed_kafka_events
    ALTER COLUMN consumer_type SET NOT NULL;

ALTER TABLE processed_kafka_events
    ADD CONSTRAINT pk_processed_kafka_events
        PRIMARY KEY (event_id, consumer_type);