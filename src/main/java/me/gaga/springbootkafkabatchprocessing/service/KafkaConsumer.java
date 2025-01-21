package me.gaga.springbootkafkabatchprocessing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MessageBuffer messageBuffer;

    @KafkaListener(topics = "message-topic", groupId = "batch-group")
    public void consume(String message) {
        messageBuffer.add(message);
    }
}
