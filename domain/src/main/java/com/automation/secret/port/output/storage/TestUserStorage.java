package com.automation.secret.port.output.storage;

import com.automation.secret.model.dto.TestUserDto;
import java.time.Instant;

public interface TestUserStorage {
    TestUserDto findNotLocked();

    void lock(String userId);

    void release(String userId);

    void unlockStaleUsers(Instant threshold);
}
