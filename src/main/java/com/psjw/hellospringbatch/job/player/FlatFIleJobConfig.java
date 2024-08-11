package com.psjw.hellospringbatch.job.player;

import com.psjw.hellospringbatch.core.service.PlayerSalaryService;
import com.psjw.hellospringbatch.dto.PlayerDto;
import com.psjw.hellospringbatch.dto.PlayerSalaryDto;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.jsr.ItemProcessListenerAdapter;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

//@Configuration
@AllArgsConstructor
public class FlatFIleJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob(Step flatFileStep) {
        return jobBuilderFactory.get("flatFileJob")
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep)
                .build();
    }

//    @JobScope
//    @Bean
//    public Step flatFileStep(FlatFileItemReader<PlayerDto> playerFileItemReader) {
//        return stepBuilderFactory.get("flatFileStep")
//                .<PlayerDto, PlayerDto>chunk(5)
//                .reader(playerFileItemReader)
//                .writer(new ItemWriter<PlayerDto>() {
//                    @Override
//                    public void write(List<? extends PlayerDto> items) throws Exception {
//                        items.forEach(System.out::println);
//                    }
//                })
//                .build();
//    }


//ItemProcessor 추가
//    @JobScope
//    @Bean
//    public Step flatFileStep(FlatFileItemReader<PlayerDto> playerFileItemReader,
//                             ItemProcessor<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessor) {
//        return stepBuilderFactory.get("flatFileStep")
//                .<PlayerDto, PlayerSalaryDto>chunk(5)
//                .reader(playerFileItemReader)
//                .processor(playerSalaryItemProcessor)
//                .writer(new ItemWriter<PlayerSalaryDto>() {
//                    @Override
//                    public void write(List<? extends PlayerSalaryDto> items) throws Exception {
//                        items.forEach(System.out::println);
//                    }
//                })
//                .build();
//    }


//ItemProcessorAdapter 추가
//    @JobScope
//    @Bean
//    public Step flatFileStep(FlatFileItemReader<PlayerDto> playerFileItemReader,
//                             ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessorAdapter) {
//        return stepBuilderFactory.get("flatFileStep")
//                .<PlayerDto, PlayerSalaryDto>chunk(5)
//                .reader(playerFileItemReader)
//                .processor(playerSalaryItemProcessorAdapter)
//                .writer(new ItemWriter<PlayerSalaryDto>() {
//                    @Override
//                    public void write(List<? extends PlayerSalaryDto> items) throws Exception {
//                        items.forEach(System.out::println);
//                    }
//                })
//                .build();
//    }

    @JobScope
    @Bean
    public Step flatFileStep(FlatFileItemReader<PlayerDto> playerFileItemReader,
                             ItemProcessor<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessor,
                             FlatFileItemWriter<PlayerSalaryDto> playerFileItemWriter) {
        return stepBuilderFactory.get("flatFileStep")
                .<PlayerDto, PlayerSalaryDto>chunk(5)
                .reader(playerFileItemReader)
                .processor(playerSalaryItemProcessor)
                .writer(playerFileItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerSalaryDto> playerFileItemWriter() throws IOException {

        BeanWrapperFieldExtractor<PlayerSalaryDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"ID","firstName","lastName","salary"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerSalaryDto> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        lineAggregator.setFieldExtractor(fieldExtractor);

        // 기존의 파일을 덮어 쓴다.
        new File("player-salary-list.txt").createNewFile();
        FileSystemResource fileSystemResource = new FileSystemResource("player-salary-list.txt");

        return new FlatFileItemWriterBuilder<PlayerSalaryDto>()
                .name("playerFileItemWriter")
                .resource(fileSystemResource)
                .lineAggregator(lineAggregator)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessorAdapter(PlayerSalaryService playerSalaryService){
        ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> adapter = new ItemProcessorAdapter<>();
        adapter.setTargetObject(playerSalaryService);
        adapter.setTargetMethod("calcSalary");
        return adapter;
    }


    @StepScope
    @Bean
    public ItemProcessor<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessor(PlayerSalaryService playerSalaryService) {
        return new ItemProcessor<PlayerDto, PlayerSalaryDto>() {
            @Override
            public PlayerSalaryDto process(PlayerDto item) throws Exception {
                return playerSalaryService.calcSalary(item);
            }
        };
    }

    @StepScope
    @Bean
    public FlatFileItemReader<PlayerDto> playerFileItemReader() {
        return new FlatFileItemReaderBuilder<PlayerDto>()
                .name("playerFileItemReader")
                .lineTokenizer(new DelimitedLineTokenizer()) //기본구분값
                .linesToSkip(1) //위의 1줄 스킾
                .fieldSetMapper(new PlayerFieldSetMapper()) //객체 맵핑
                .resource(new FileSystemResource("player-list.txt")) //읽을 파일
                .build();
    }

}
