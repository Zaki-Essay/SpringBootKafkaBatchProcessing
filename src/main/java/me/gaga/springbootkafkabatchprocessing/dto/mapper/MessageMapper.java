package me.gaga.springbootkafkabatchprocessing.dto.mapper;

import me.gaga.springbootkafkabatchprocessing.dto.MessageRequestDto;
import me.gaga.springbootkafkabatchprocessing.model.Message;

public interface MessageMapper {
    Message toEntity(MessageRequestDto dto);
    void updateEntityFromDto(MessageRequestDto dto, Message message);
}
