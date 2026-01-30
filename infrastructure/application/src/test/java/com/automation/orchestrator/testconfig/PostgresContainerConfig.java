package com.automation.orchestrator.testconfig;

import static org.testcontainers.utility.DockerImageName.parse;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers(disabledWithoutDocker = true)
public class PostgresContainerConfig {

    protected static final Network NETWORK = Network.newNetwork();

    @ServiceConnection
    @SuppressWarnings("resource")
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
        parse("postgres:17.4"))
        .withNetwork(NETWORK)
        .withDatabaseName("orchestrator_db")
        .withUsername("postgres")
        .withPassword("postgres")
        .withExposedPorts(5432)
        .withStartupAttempts(5)
        .waitingFor(Wait.forListeningPort())
        .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    }

}
