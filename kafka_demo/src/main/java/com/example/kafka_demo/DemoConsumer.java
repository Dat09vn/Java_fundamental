package com.example.kafka_demo;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class DemoConsumer {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "java-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // tự commit thủ công

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        Thread mainThread = Thread.currentThread();

        // Ctrl+C: đánh thức poll() để thoát êm, rời group đúng cách
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.wakeup();
            try { mainThread.join(); } catch (InterruptedException ignored) {}
        }));

        try {
            consumer.subscribe(List.of("demo"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> r : records) {
                    System.out.printf("partition=%d offset=%d key=%s value=%s%n",
                            r.partition(), r.offset(), r.key(), r.value());
                    // ... xử lý nghiệp vụ ở đây ...
                }
                if (!records.isEmpty()) {
                    consumer.commitSync(); // xử lý xong rồi mới commit
                }
            }
        } catch (WakeupException e) {
            // thoát bình thường
        } finally {
            consumer.close(); // rời group, kích hoạt rebalance ngay
        }
    }
}