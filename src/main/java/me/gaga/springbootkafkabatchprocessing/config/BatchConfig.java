package me.gaga.springbootkafkabatchprocessing.config;

import lombok.RequiredArgsConstructor;
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
                .<String, Message>chunk(10, transactionManager)
                .reader(messageReader())
                .processor(messageProcessor())
                .writer(messageWriter())
                .build();
    }

    @Bean
    public ItemReader<String> messageReader() {
        return new ListItemReader<>(messageBuffer.getAndClear());
    }

    @Bean
    public ItemProcessor<String, Message> messageProcessor() {
        return item -> {
            Message message = new Message();
            message.setContent(item);
            message.setTimestamp(LocalDateTime.now().toString());
            return message;
        };
    }

    @Bean
    public ItemWriter<Message> messageWriter() {
        return messageRepository::saveAll;
    }
}
