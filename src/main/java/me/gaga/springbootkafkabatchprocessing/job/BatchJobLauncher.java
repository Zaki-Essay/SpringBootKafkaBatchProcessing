package me.gaga.springbootkafkabatchprocessing.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.gaga.springbootkafkabatchprocessing.service.MessageBuffer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJobLauncher extends QuartzJobBean {
    private final JobLauncher jobLauncher;
    private final Job processMessagesJob;
    private final MessageBuffer messageBuffer;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            // Only run the job if there are messages to process
            if (messageBuffer.size() > 0) {
                log.info("Starting batch job with {} messages", messageBuffer.size());
                JobParameters params = new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

                jobLauncher.run(processMessagesJob, params);
            } else {
                log.debug("No messages to process, skipping job execution");
            }
        } catch (Exception e) {
            log.error("Error executing batch job", e);
            throw new JobExecutionException(e);
        }
    }
}