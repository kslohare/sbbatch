package a.b.sbb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @GetMapping("/execute")
    public int execute(@RequestParam String jobName) {

        System.out.println("Executing job: " + jobName);

        // TODO: Add actual job execution logic

        int exitCode = 0;

        return exitCode;
    }
}