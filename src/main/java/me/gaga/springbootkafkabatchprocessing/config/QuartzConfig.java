package me.gaga.springbootkafkabatchprocessing.config;


import me.gaga.springbootkafkabatchprocessing.job.BatchJobLauncher;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail batchJobDetail() {
        return JobBuilder.newJob(BatchJobLauncher.class)
                .withIdentity("batchJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger batchJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(30)  // Trigger every 30 seconds
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(batchJobDetail())
                .withIdentity("batchJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}

