package com.psjw.hellospringbatch.job;

import com.psjw.hellospringbatch.core.domain.PlainText;
import com.psjw.hellospringbatch.core.domain.ResultText;
import com.psjw.hellospringbatch.core.repository.PlainTextRepository;
import com.psjw.hellospringbatch.core.repository.ResultTextRepository;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import javax.xml.transform.Result;
import java.util.Collections;
import java.util.List;

//@Configuration
@Slf4j
@RequiredArgsConstructor
public class PlainTextJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PlainTextRepository plainTextRepository;
    private final ResultTextRepository resultTextRepository;


    @Bean("plainTextJob")
    public Job plainTextJob(Step helloStep) {
        return jobBuilderFactory.get("plainTextJob")
                .incrementer(new RunIdIncrementer()) //Job 실행시 횟수를 일정하게 증가
                .start(helloStep)
                .build();
    }

    @JobScope //Job이 실행되는 동안에만 빈이 실행
    @Bean("plainTextStep")
    public Step plainTextStep(ItemReader plainTextReader,
                              ItemProcessor plainTextProcessor,
                              ItemWriter plainTextWriter) {
        return stepBuilderFactory.get("plainTextStep")
                .<PlainText, PlainText>chunk(5)
                .reader(plainTextReader)
                .processor(plainTextProcessor)
                .writer(plainTextWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<PlainText> plainTextReader() {
        return new RepositoryItemReaderBuilder<PlainText>()
                .name("plainTextReader")
                .repository(plainTextRepository)
                .methodName("findBy") // repository에 선안한 메시지
                .pageSize(5)
                .arguments(List.of()) //조건 파리미터가 있으면 리스트로 넘겨줌
                .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<PlainText, String> plainTextProcessor() {
        return item -> "processed " + item.getText();
    }

    @StepScope
    @Bean
    public ItemWriter<String> plainTextWriter(){
//        return items -> {
//            items.forEach(log::info);
//            log.info("==== chunk is finished");
//        };
        return items -> {
            items.forEach(item -> {
                resultTextRepository.save(new ResultText(null, item));
            });
            log.info("==== chunk is finished");
        };
    }
}
