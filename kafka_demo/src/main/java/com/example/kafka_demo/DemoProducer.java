package com.example.kafka_demo;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class DemoProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG, "all");               // chờ broker xác nhận
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // không bị trùng khi retry
        props.put(ProducerConfig.LINGER_MS_CONFIG, "5");            // gom batch tối đa 5ms

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 0; i < 20; i++) {
                String key = "user" + (i % 5);
                ProducerRecord<String, String> record =
                        new ProducerRecord<>("demo", key, "event-" + i);

                producer.send(record, (meta, ex) -> {
                    if (ex != null) {
                        System.err.println("Gui loi: " + ex.getMessage());
                    } else {
                        System.out.printf("key=%s -> partition=%d offset=%d%n",
                                record.key(), meta.partition(), meta.offset());
                    }
                });
            }
            producer.flush(); // đảm bảo mọi message đã được gửi đi
        }
    }
}