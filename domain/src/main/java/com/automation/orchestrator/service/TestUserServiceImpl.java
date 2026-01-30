package com.automation.orchestrator.service;

import com.automation.orchestrator.model.dto.TestUserDto;
import com.automation.orchestrator.port.input.testuser.TestUserService;
import com.automation.orchestrator.port.output.storage.TestUserStorage;

public class TestUserServiceImpl implements TestUserService {

    private final TestUserStorage testUserStorage;

    public TestUserServiceImpl(TestUserStorage testUserStorage) {
        this.testUserStorage = testUserStorage;
    }

    @Override
    public TestUserDto getAndLockTestUser() {
        TestUserDto testUserDto = testUserStorage.findNotLocked();
        testUserStorage.lock(testUserDto.userId());
        return testUserDto;
    }

    @Override
    public void releaseTestUser(String userId) {
        testUserStorage.release(userId);
    }
}
