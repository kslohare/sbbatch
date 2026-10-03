

## Add dependency to pom.xml

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.9.1</version>
</dependency>
```

## Add Swagger configuration
```properties
# Swagger / OpenAPI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Start your application
```bash
./mvnw spring-boot:run
```

### Then open:
http://localhost:8585/swagger-ui.html
http://localhost:8585/v3/api-docs