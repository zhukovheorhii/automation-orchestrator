package com.automation.orchestrator.scheduler;

import com.automation.orchestrator.port.output.storage.TestUserStorage;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestUserCleanupScheduler {

    private final TestUserStorage testUserStorage;
    private final Duration ttl;

    public TestUserCleanupScheduler(
            TestUserStorage testUserStorage,
            @Value("${scheduler.test-user-cleanup.ttl}") Duration ttl
    ) {
        this.testUserStorage = testUserStorage;
        this.ttl = ttl;
    }

    @Scheduled(fixedRateString = "${scheduler.test-user-cleanup.ttl}")
    public void cleanupStaleLockedUsers() {
        Instant threshold = Instant.now().minus(ttl);
        testUserStorage.unlockStaleUsers(threshold);
    }
}
