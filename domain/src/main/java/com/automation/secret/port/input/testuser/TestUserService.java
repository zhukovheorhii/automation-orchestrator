package com.automation.secret.port.input.testuser;


import com.automation.secret.model.dto.TestUserDto;

public interface TestUserService {
    TestUserDto getAndLockTestUser();

    void releaseTestUser(String userId);
}
