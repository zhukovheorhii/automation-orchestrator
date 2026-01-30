package com.automation.orchestrator;

import com.automation.orchestrator.testconfig.PostgresContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
class ApplicationTests {

    @Test
    @SuppressWarnings("java:S2699")
    void contextLoads() {
        // check if app context is starting
    }
}
