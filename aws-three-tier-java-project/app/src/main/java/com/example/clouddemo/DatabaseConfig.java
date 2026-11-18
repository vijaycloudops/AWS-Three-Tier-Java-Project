package com.example.clouddemo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() throws Exception {
        String secretArn = System.getenv("DB_SECRET_ARN");
        String regionName = System.getenv().getOrDefault("AWS_REGION", "eu-west-1");
        if (secretArn == null || secretArn.isBlank()) {
            throw new IllegalStateException("DB_SECRET_ARN environment variable is required");
        }

        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.of(regionName)).build()) {
            String secretText = client.getSecretValue(GetSecretValueRequest.builder()
                    .secretId(secretArn).build()).secretString();
            JsonNode secret = new ObjectMapper().readTree(secretText);

            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl("jdbc:postgresql://" + secret.path("host").asText() + ":"
                    + secret.path("port").asInt(5432) + "/"
                    + secret.path("dbname").asText()
                    + "?sslmode=require&connectTimeout=5");
            ds.setUsername(secret.path("username").asText());
            ds.setPassword(secret.path("password").asText());
            return ds;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
