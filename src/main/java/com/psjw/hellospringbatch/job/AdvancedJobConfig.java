package com.psjw.hellospringbatch.job;

import com.psjw.hellospringbatch.job.validator.LocalDateParameterValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@AllArgsConstructor
@Slf4j
public class AdvancedJobConfig {
    //--spring.batch.job.names=advancedJob -targetDate=2021-01-01
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job advancedJob(JobExecutionListener jobExecutionListener, Step advancedStep) {
        return jobBuilderFactory.get("advancedJob")
                .incrementer(new RunIdIncrementer())
                .validator(new LocalDateParameterValidator("targetDate")) //앞선 작업을 실행할 수 있으므로 미리 거른다.
                .listener(jobExecutionListener)
                .start(advancedStep)
                .build();
    }


    //Job이 실패났을 경우에 처리
    @JobScope
    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("[JobExecutionListener#beforeJob] jobExecution is " + jobExecution.getStatus());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    log.info("[JobExecutionListener#afterJob] jobExecution is FAILED!!! RECOVER ASAP");
                    //NotificationService.notify(""); 실패시 정보 넘김
                } else {
                    log.info("[JobExecutionListener#afterJob] jobExecution is " + jobExecution.getStatus());
                }
            }
        };
    }

    @JobScope
    @Bean
    public Step advancedStep(Tasklet advancedTasklet) {
        return stepBuilderFactory.get("advancedStep")
                .tasklet(advancedTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet advancedTasklet(@Value("#{jobParameters['targetDate']}") String targetDate) {
        return (stepContribution, chunkContext) -> {
            log.info("[AdvancedJobConfig] jobParameter - targetDate = {}", targetDate);
            LocalDate executionDate = LocalDate.parse(targetDate);
            // executionDate -> 로직 수행
            log.info("[AdvancedJobConfig] executed advancedTasklet");
//            throw new RuntimeException("ERROR!!!");
            return RepeatStatus.FINISHED;
        };
    }
}
