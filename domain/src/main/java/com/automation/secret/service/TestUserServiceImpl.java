package com.automation.secret.service;

import com.automation.secret.model.dto.TestUserDto;
import com.automation.secret.port.input.testuser.TestUserService;
import com.automation.secret.port.output.storage.TestUserStorage;

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
