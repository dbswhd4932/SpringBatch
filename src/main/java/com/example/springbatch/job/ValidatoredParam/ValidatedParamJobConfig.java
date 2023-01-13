package com.example.springbatch.job.ValidatoredParam;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc : 파일 이름 파라미터 전달 , 검증
 * run : --spring.batch.job.names=validatedParamJob -fileName=test.csv
 */

@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job ValidatedParamJob(Step validatedParamStep) {
        return jobBuilderFactory.get("validatedParamJob")
                .incrementer(new RunIdIncrementer())
                .start(validatedParamStep)
                .build();
    }

    @JobScope
    @Bean
    public Step validatedParamStep(Tasklet validatedParamTasklet) {
        return stepBuilderFactory.get("validatedParamStep")
                // 간단한 경우에는 tasklet 을 사용
                .tasklet(validatedParamTasklet)
                .build();
    }

    @StepScope
    @Bean                               // 스프링 프레임워크의 Value 사용
    public Tasklet validatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                System.out.println("fileName = " + fileName);
                System.out.println("validated Param Tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }

}
