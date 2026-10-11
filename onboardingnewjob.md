# Step 1 — Create a Package for the New Job 

  

Example: create a reconciliation job. Create this directory: src/main/java/a/b/sbb/batch/reconciliation/

Add the following files: reconciliation/ ├── ReconciliationJobConfig.java └── ReconciliationTasklet.java

Additional classes can be introduced as required. For example:

-   ReconciliationService.java
-   ReconciliationResult.java
-   ReconciliationRepository.java Keep each class focused on one responsibility.

# Step 2 — Create the Tasklet 

  

Create: ReconciliationTasklet.java package a.b.sbb.batch.reconciliation;

import org.springframework.batch.core.step.StepContribution; import org.springframework.batch.core.scope.context.ChunkContext; import org.springframework.batch.core.step.tasklet.Tasklet; import org.springframework.batch.infrastructure.repeat.RepeatStatus; import org.springframework.stereotype.Component;

@Component public class ReconciliationTasklet implements Tasklet {

```
@Override
public RepeatStatus execute(
        StepContribution contribution,
        ChunkContext chunkContext) {

    System.out.println("Starting reconciliation job");

    // Implement reconciliation business logic here.

    System.out.println("Reconciliation job completed");

    return RepeatStatus.FINISHED;
}
```

}

Important This example uses the package structure previously established for Spring Batch 6.0.5 in this project. A tasklet executes a unit of work. Examples:

-   Read a file.
-   Validate input data.
-   Call a service.
-   Execute a database operation.
-   Generate a report.
-   Transfer a file. RepeatStatus.FINISHED tells Spring Batch that the tasklet has finished. Analogy: A tasklet is like one worker responsible for completing a particular task.

# Create the Job Configuration Create

  

: ReconciliationJobConfig.java package a.b.sbb.batch.reconciliation;

import org.springframework.batch.core.job.Job; import org.springframework.batch.core.job.builder.JobBuilder; import org.springframework.batch.core.repository.JobRepository; import org.springframework.batch.core.step.Step; import org.springframework.batch.core.step.builder.StepBuilder; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.transaction.PlatformTransactionManager;

@Configuration public class ReconciliationJobConfig {

```
@Bean
public Step reconciliationStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        ReconciliationTasklet reconciliationTasklet) {

    return new StepBuilder(
            "reconciliationStep",
            jobRepository
    )
            .tasklet(
                    reconciliationTasklet,
                    transactionManager
            )
            .build();
}

@Bean
public Job reconciliationJob(
        JobRepository jobRepository,
        Step reconciliationStep) {

    return new JobBuilder(
            "reconciliationJob",
            jobRepository
    )
            .start(reconciliationStep)
            .build();
}
```

}

Understanding the configuration @Configuration Marks the class as a Spring configuration class. @Bean Registers the returned object in the Spring application context. JobRepository Stores Spring Batch execution metadata. Step Represents one stage of a job. Tasklet Implements the business logic for the step. PlatformTransactionManager Provides transaction management for the step. JobBuilder Constructs the Spring Batch job. Analogy: Job = A complete journey. Step = One stage of the journey. Tasklet = The work performed at that stage. JobRepository = The history book recording executions.

# Step 4 — Verify Spring Component Scanning The main application class should be in the root package

  

: a.b.sbb Example: package a.b.sbb;

import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication public class SbbApplication {

```
public static void main(String[] args) {
    SpringApplication.run(
            SbbApplication.class,
            args
    );
}
```

}

Spring Boot normally scans subpackages of the main application package. Therefore, this package is automatically discoverable: a.b.sbb.batch.reconciliation

Do not place job configuration classes outside the component-scanned package unless you configure scanning explicitly.

# Step 5 — Verify the Job Is Registered Start the application and inspect the registered jobs. 

  

Expected output: Bean name: reconciliationJob, Job name: reconciliationJob

The bean name and job name are related but distinct concepts. Example: @Bean public Job reconciliationJob(...) { return new JobBuilder( "reconciliationJob", jobRepository ) .start(reconciliationStep) .build(); }

Here:

-   Bean name: reconciliationJob
-   Spring Batch job name: reconciliationJob If the job does not appear, check:

1.  Is @Configuration present?
2.  Is the @Bean method present?
3.  Is the configuration inside the component-scanned package?
4.  Did the application start successfully?

Step 6 — Execute the Job Through the REST Controller The existing controller exposes: GET /api/jobs/execute?jobName=

Example URL: [http://localhost:8585/api/jobs/execute?jobName=reconciliationJob](http://localhost:8585/api/jobs/execute?jobName=reconciliationJob)

The controller should:

1.  Receive the job name.
2.  Find the registered job by job.getName().
3.  Create unique job parameters.
4.  Launch the job using the supported Spring Batch 6 API.
5.  Log the execution ID and status.
6.  Return an appropriate response. Important API note Spring Batch 6 deprecates JobLauncher. Use the supported JobOperator API in the existing application, following the project's configured operator bean. Do not reintroduce JobLauncher when adding new jobs. If your operator is configured for asynchronous execution, the initial execution status might be STARTING or STARTED. Do not interpret that initial status as proof of final success.

# Step 7 — Use Unique Job Parameters Spring Batch identifies job instances using the job name and identifying parameters.

  

 Example: JobParameters parameters = new JobParametersBuilder() .addLong("timestamp", System.currentTimeMillis()) .toJobParameters();

A new timestamp allows a new job instance to be created for each invocation. However, this also means every invocation is a different job instance. For business jobs, choose parameters carefully. Example: businessDate = 2026-10-11

If the job should process a particular business date only once, use a business date as an identifying parameter rather than always generating a new timestamp. Analogy: A job name identifies the type of work, while job parameters identify a particular assignment.

# Step 8 — Compile the Application From the project root

  

: cd ~/code/sbbatch

mvn clean compile

# Step 9 — Test the Job Start the application using your normal development workflow. 

  

Execute the job: curl -i  
"[http://localhost:8585/api/jobs/execute?jobName=reconciliationJob](http://localhost:8585/api/jobs/execute?jobName=reconciliationJob)"

Verify:

-   The endpoint finds the job.
-   The job is launched.
-   The tasklet executes.
-   The job execution ID is logged.
-   The final execution status is checked.
-   Errors are logged with a logger.
-   The Spring Batch metadata is persisted in PostgreSQL.