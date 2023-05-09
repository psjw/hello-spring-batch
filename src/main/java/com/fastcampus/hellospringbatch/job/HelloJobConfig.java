package com.fastcampus.hellospringbatch.job;

import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Configuration
public class HelloJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean("helloJob")
    public Job helloJob(Step helloStep){
        return jobBuilderFactory.get("helloJob")
                .incrementer(new RunIdIncrementer()) //횟수를 일정하게 증가 시키는것을 설정
                .start(helloStep)
                .build();
    }

    //Bean의 Scope정해줌 -> Job이 실행되는 동안에만 Step이 실행됨
    @JobScope
    @Bean("helloStep")
    public Step helloStep(Tasklet tasklet){
        return stepBuilderFactory.get("helloStep")
                .tasklet(tasklet)
                .build();
    }

    //Bean의 Scope정해줌 -> Step이 실행되는 동안에만 Object가 살아 있으면 됨
    @StepScope
    @Bean
    public Tasklet tasklet(){
        return (contribution, chunkContext) -> {
            System.out.println("Hello Spring Batch");
            return RepeatStatus.FINISHED;
        };
    }
}
