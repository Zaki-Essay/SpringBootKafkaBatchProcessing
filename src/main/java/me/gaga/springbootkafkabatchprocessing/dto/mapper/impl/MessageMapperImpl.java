package me.gaga.springbootkafkabatchprocessing.dto.mapper.impl;

import me.gaga.springbootkafkabatchprocessing.dto.MessageRequestDto;
import me.gaga.springbootkafkabatchprocessing.dto.mapper.MessageMapper;
import me.gaga.springbootkafkabatchprocessing.model.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapperImpl implements MessageMapper {
    @Override
    public Message toEntity(MessageRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Message message = new Message();
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now().toString());
        return message;
    }

    @Override
    public void updateEntityFromDto(MessageRequestDto dto, Message message) {
        if (dto == null || message == null) {
            return;
        }

        if (dto.getContent() != null) {
            message.setContent(dto.getContent());
        }
    }
}