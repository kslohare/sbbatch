package a.b.sbb.batch.projectconfig;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProjectConfigJobConfig {

    @Bean
    public Step projectConfigStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ProjectConfigTasklet projectConfigTasklet) {

        return new StepBuilder("PROJECTCONFIG_STEP", jobRepository)
                .tasklet(projectConfigTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job projectConfigJob(
            JobRepository jobRepository,
            Step projectConfigStep) {

        return new JobBuilder("PROJECTCONFIG", jobRepository)
                .start(projectConfigStep)
                .build();
    }
}