package com.fastcampus.hellospringbatch.job;

import com.fastcampus.hellospringbatch.core.domain.PlainText;
import com.fastcampus.hellospringbatch.core.repository.PlainTextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class PlainTextJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PlainTextRepository plainTextRepository;

    @Bean("plainTextJob")
    public Job helloJob(Step plainTextStep) {
        return jobBuilderFactory.get("plainTextJob")
                .incrementer(new RunIdIncrementer()) //횟수를 일정하게 증가 시키는것을 설정
                .start(plainTextStep)
                .build();
    }

    //Bean의 Scope정해줌 -> Job이 실행되는 동안에만 Step이 실행됨
    @JobScope
    @Bean("plainTextStep")
    public Step plainTextStep(ItemReader plainTextItemReader,
                              ItemProcessor plainTextItemProcessor,
                              ItemWriter plainTextItemWriter) {
        return stepBuilderFactory.get("plainTexItemReader")
                .<PlainText, String>chunk(5) //<읽어올 타입, 프로세싱할 타입>
                .reader(plainTextItemReader)
                .processor(plainTextItemProcessor)
                .writer(plainTextItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<PlainText> plainTextItemReader() {
        return new RepositoryItemReaderBuilder<PlainText>()
                .name("plainTextReader")
                .repository(plainTextRepository)
                .methodName("findBy")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.DESC)) //데이터 순서 : id의 역순
                .build();
    }

    //Bean의 Scope정해줌 -> Step이 실행되는 동안에만 Object가 살아 있으면 됨
    @StepScope
    @Bean
    public ItemProcessor<PlainText, String> plainTextItemProcessor() {
        return item -> "processed " + item.getText();

    }

    @StepScope
    @Bean
    public ItemWriter<String> plainTextItemWriter() {
        return items -> {
            items.forEach(System.out::println);
            System.out.println("=== chunk is finished");
        };
    }
}
