package me.gaga.springbootkafkabatchprocessing.controller;

import lombok.RequiredArgsConstructor;
import me.gaga.springbootkafkabatchprocessing.service.KafkaProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final KafkaProducer kafkaProducer;

    @PostMapping
    public void sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
    }
}
