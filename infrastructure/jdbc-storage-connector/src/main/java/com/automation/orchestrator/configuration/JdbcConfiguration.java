package com.automation.orchestrator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EnableJdbcRepositories(basePackages = "com.automation.orchestrator.repository")
@Configuration
public class JdbcConfiguration {
}
