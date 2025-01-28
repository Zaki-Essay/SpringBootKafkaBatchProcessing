package me.gaga.springbootkafkabatchprocessing.service;

import lombok.RequiredArgsConstructor;
import me.gaga.springbootkafkabatchprocessing.dto.MessageRequestDto;
import me.gaga.springbootkafkabatchprocessing.dto.mapper.impl.MessageMapperImpl;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageMapperImpl messageMapperImpl;

    public void sendMessage(MessageRequestDto message) {
        kafkaTemplate.send("message-topic", message.getContent());
    }
}
