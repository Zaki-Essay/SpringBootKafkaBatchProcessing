package me.gaga.springbootkafkabatchprocessing.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.gaga.springbootkafkabatchprocessing.model.Message;
import me.gaga.springbootkafkabatchprocessing.repository.MessageRepository;
import me.gaga.springbootkafkabatchprocessing.service.MessageBuffer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MessageBuffer messageBuffer;
    private final MessageRepository messageRepository;

    @Bean
    public Job processMessagesJob() {
        return new JobBuilder("processMessagesJob", jobRepository)
                .start(processMessagesStep())
                .build();
    }

    @Bean
    public Step processMessagesStep() {
        return new StepBuilder("processMessagesStep", jobRepository)
                .<List<String>, List<Message>>chunk(1, transactionManager)
                .reader(() -> {
                    List<String> messages = messageBuffer.getAndClear();
                    return messages.isEmpty() ? null : messages;
                })
                .processor(messageProcessor())
                .writer(messageWriter())
                .build();
    }

    @Bean
    public ItemProcessor<List<String>, List<Message>> messageProcessor() {
        return messages -> {
            log.info("Processing {} messages", messages.size());
            return messages.stream()
                    .map(content -> {
                        Message message = new Message();
                        message.setContent(content);
                        message.setTimestamp(LocalDateTime.now().toString());
                        return message;
                    })
                    .toList();
        };
    }

    @Bean
    public ItemWriter<List<Message>> messageWriter() {
        return items -> {
            for (List<Message> messages : items) {
                log.info("Saving {} messages to database", messages.size());
                messageRepository.saveAll(messages);
            }
        };
    }
}
