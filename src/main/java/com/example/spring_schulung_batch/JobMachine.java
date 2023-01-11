package com.example.spring_schulung_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobMachine {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    SoutBuilder soutBuilder;

    @Bean
    public Job firstJob() {
        Tasklet tasklet = (contribution, chunkContext) -> {
            System.out.println("Hallo Welt");
            return RepeatStatus.FINISHED;
        };

        TaskletStep firstStep = new StepBuilder("firstStep", jobRepository)
                .tasklet(tasklet, transactionManager).build();

        Job firstJob = new JobBuilder("firstJob", jobRepository)
                .start(firstStep)
                .build();

        return firstJob;
    }

    @Bean
    @Primary
    Job chainedStepsWithStatus() {
        TaskletStep step1 = soutBuilder.setMessage("Message 1").setStepName("Step 1").getTaskletStep();
        TaskletStep step2 = soutBuilder.setMessage("Message 2").setStepName("Step 2").getTaskletStep();
        TaskletStep step3 = soutBuilder.setMessage("Message 3").setStepName("Step 3").getTaskletStep();

        return new JobBuilder("chainedJob", jobRepository)
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }
}
