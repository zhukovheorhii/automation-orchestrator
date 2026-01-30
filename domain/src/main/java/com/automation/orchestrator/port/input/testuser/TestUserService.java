package com.automation.orchestrator.port.input.testuser;


import com.automation.orchestrator.model.dto.TestUserDto;

public interface TestUserService {
    TestUserDto getAndLockTestUser();

    void releaseTestUser(String userId);
}
