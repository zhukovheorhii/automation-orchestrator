package com.automation.orchestrator.port.output.storage;

import com.automation.orchestrator.model.dto.TestUserDto;
import java.time.Instant;

public interface TestUserStorage {
    TestUserDto findNotLocked();

    void lock(String userId);

    void release(String userId);

    void unlockStaleUsers(Instant threshold);
}
