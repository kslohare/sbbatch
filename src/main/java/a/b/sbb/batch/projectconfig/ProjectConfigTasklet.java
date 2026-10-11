package a.b.sbb.batch.projectconfig;

import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;

import com.sun.management.OperatingSystemMXBean;

@Component
public class ProjectConfigTasklet implements Tasklet {

    private final Environment environment;

    public ProjectConfigTasklet(Environment environment) {
        this.environment = environment;
    }

    @Override
    public RepeatStatus execute(
            StepContribution contribution,
            ChunkContext chunkContext) {

        ProjectConfig config = new ProjectConfig(
                environment.getProperty("project.name", "sbb"),
                environment.getProperty(
                        "project.version", "0.0.1-SNAPSHOT"),
                SpringBootVersion.getVersion(),
                org.springframework.batch.core.SpringBatchVersion
                        .getVersion(),
                System.getProperty("java.version"),
                environment.getProperty(
                        "project.maven.version", "Not available"),
                environment.getProperty(
                        "project.springdoc.version", "3.1.1")
        );

        System.out.println();
        System.out.println("==============================================");
        System.out.println("        PROJECT CONFIGURATION");
        System.out.println("==============================================");
        System.out.println("Project Name      : " + config.projectName());
        System.out.println("Project Version   : " + config.projectVersion());
        System.out.println("Spring Boot       : " + config.springBootVersion());
        System.out.println("Spring Batch      : " + config.springBatchVersion());
        System.out.println("Java              : " + config.javaVersion());
        System.out.println("Maven             : " + config.mavenVersion());
        System.out.println("Springdoc OpenAPI : " + config.springdocVersion());
        System.out.println("==============================================");

        try {
            printResourceInformation();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read system resource information", e);
        }

        return RepeatStatus.FINISHED;
    }

    private void printResourceInformation() throws IOException {

        Runtime runtime = Runtime.getRuntime();

        // JVM memory
        long maxHeap = runtime.maxMemory();
        long committedHeap = runtime.totalMemory();
        long usedHeap = committedHeap - runtime.freeMemory();

        // CPU and physical memory visible to the JVM
        java.lang.management.OperatingSystemMXBean managementBean =
                ManagementFactory.getOperatingSystemMXBean();

        System.out.println();
        System.out.println("==============================================");
        System.out.println("          SYSTEM RESOURCE INFORMATION");
        System.out.println("==============================================");

        System.out.println("Available CPU Cores : "
                + runtime.availableProcessors());

        if (managementBean instanceof OperatingSystemMXBean osBean) {
            System.out.println("Total System RAM    : "
                    + toGB(osBean.getTotalMemorySize()));

            System.out.println("Available System RAM: "
                    + toGB(osBean.getFreeMemorySize()));

            double cpuLoad = osBean.getCpuLoad();

            System.out.println("System CPU Usage    : "
                    + (cpuLoad >= 0
                    ? String.format("%.2f%%", cpuLoad * 100)
                    : "Not available"));
        }

        System.out.println();
        System.out.println("---------- JVM MEMORY ----------");

        System.out.println("Maximum Heap Memory : " + toGB(maxHeap));
        System.out.println("Committed Heap      : " + toGB(committedHeap));
        System.out.println("Used Heap           : " + toGB(usedHeap));

        System.out.println();
        System.out.println("---------- DISK SPACE ----------");

        Path applicationPath = Path.of(".").toAbsolutePath();
        var fileStore = Files.getFileStore(applicationPath);

        System.out.println("Application Path    : " + applicationPath);
        System.out.println("Total Disk Space    : " + toGB(fileStore.getTotalSpace()));
        System.out.println("Usable Disk Space   : " + toGB(fileStore.getUsableSpace()));

        System.out.println("==============================================");
    }

    private String toGB(long bytes) {
        double gb = bytes / (1024.0 * 1024 * 1024);
        return String.format("%.2f GB", gb);
    }
}