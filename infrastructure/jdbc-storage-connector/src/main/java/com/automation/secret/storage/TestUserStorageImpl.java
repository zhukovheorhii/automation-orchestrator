package com.automation.secret.storage;

import com.automation.secret.exception.NoTestUserAvailableException;
import com.automation.secret.model.TestUserEntity;
import com.automation.secret.model.dto.TestUserDto;
import com.automation.secret.port.output.storage.TestUserStorage;
import com.automation.secret.repository.TestUserEntityRepository;
import com.automation.secret.storage.mapper.TestUserMapper;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
class TestUserStorageImpl implements TestUserStorage {

    private static final Logger log = LoggerFactory.getLogger(TestUserStorageImpl.class);

    private final TestUserEntityRepository repository;
    private final TestUserMapper mapper;

    public TestUserStorageImpl(TestUserEntityRepository repository, TestUserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TestUserDto findNotLocked() {
        TestUserEntity testUserEntity = repository.findNotLocked();

        if (testUserEntity == null) {
            throw new NoTestUserAvailableException("No unlocked test user available");
        }

        return mapper.toTestUserDto(testUserEntity);
    }

    @Override
    public void lock(String userId) {
        int updatedRows = repository.lock(userId);
        if (updatedRows == 0) {
            throw new IllegalStateException("Failed to lock test user with ID: " + userId);
        }
        log.info("Locked test user with ID: {}", userId);
    }

    @Override
    public void release(String userId) {
        int updatedRows = repository.release(userId);
        if (updatedRows == 0) {
            throw new IllegalStateException("Failed to release test user with ID: " + userId);
        }
        log.info("Released test user with ID: {}", userId);
    }

    @Override
    public void unlockStaleUsers(Instant threshold) {
        int unlockedCount = repository.unlockStaleUsers(threshold);
        log.info("Unlocked {} stale test users", unlockedCount);
    }
}
