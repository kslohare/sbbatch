package a.b.sbb.controller;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobLauncher jobLauncher;
    private final Map<String, Job> jobs;

    public JobController(
            JobLauncher jobLauncher,
            Map<String, Job> jobs) {

        this.jobLauncher = jobLauncher;
        this.jobs = jobs;
    }

    @GetMapping("/execute")
    public int execute(@RequestParam String jobName) {

        System.out.println("Executing job: " + jobName);

        // Find the job using its Spring Batch job name.
        Job job = jobs.values()
                .stream()
                .filter(registeredJob ->
                        registeredJob.getName().equals(jobName))
                .findFirst()
                .orElse(null);

        if (job == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Job not found: " + jobName
            );
        }

        try {
            JobParameters parameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, parameters);

            System.out.println(
                    "Job execution ID: " + execution.getId());

            System.out.println(
                    "Job execution status: " + execution.getStatus());

            return execution.getStatus().isUnsuccessful() ? 1 : 0;

        } catch (Exception e) {
            System.err.println("Failed to execute job: " + jobName);
            e.printStackTrace();

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to execute job: " + jobName,
                    e
            );
        }
    }
}