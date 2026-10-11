package a.b.sbb.batch.projectconfig;

public record ProjectConfig(
        String projectName,
        String projectVersion,
        String springBootVersion,
        String springBatchVersion,
        String javaVersion,
        String mavenVersion,
        String springdocVersion
) {
}