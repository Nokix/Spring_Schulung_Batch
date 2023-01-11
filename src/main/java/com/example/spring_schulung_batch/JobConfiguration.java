package com.example.spring_schulung_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class JobConfiguration {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Bean
    public CommandLineRunner startJob() {
        return args -> {
            long parameter = System.currentTimeMillis();
//            long parameter = 15L;
            JobParameters parameters = new JobParametersBuilder()
                    .addLong("startAt", parameter)
                    .toJobParameters();
            jobLauncher.run(job, parameters);
        };
    }
}
