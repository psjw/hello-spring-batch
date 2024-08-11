package com.psjw.hellospringbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@Slf4j
@RequiredArgsConstructor
public class HelloJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean("helloJob")
    public Job helloJob(Step helloStep){
        return jobBuilderFactory.get("helloJob")
                .incrementer(new RunIdIncrementer()) //Job 실행시 횟수를 일정하게 증가
                .start(helloStep)
                .build();
    }

    @JobScope //Job이 실행되는 동안에만 빈이 실행
    @Bean("helloStep")
    public Step helloStep(Tasklet tasklet){
        return stepBuilderFactory.get("helloStep")
                .tasklet(tasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet tasklet(){
        return (stepContribution, chunkContext) -> {
            log.info("Hello Spring Batch");
            return RepeatStatus.FINISHED;
        };
    }
}
