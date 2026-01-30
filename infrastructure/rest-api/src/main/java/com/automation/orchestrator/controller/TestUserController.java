package com.automation.orchestrator.controller;

import com.automation.orchestrator.mapper.TestUserWebDtoMapper;
import com.automation.orchestrator.model.dto.TestUserDto;
import com.automation.orchestrator.port.input.testuser.TestUserService;
import com.automation.orchestrator.restapi.generated.api.TestUserApi;
import com.automation.orchestrator.restapi.generated.dto.TestUserWebDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestUserController implements TestUserApi {

    private final TestUserService testUserService;
    private final TestUserWebDtoMapper mapper;

    public TestUserController(TestUserService testUserService, TestUserWebDtoMapper mapper) {
        this.testUserService = testUserService;
        this.mapper = mapper;
    }

    @Override
    public TestUserWebDto getAndLockUser() {
        TestUserDto testUserWebDto = testUserService.getAndLockTestUser();
        return mapper.toTestUserWebDto(testUserWebDto);
    }

    @Override
    public void releaseUser(String userId) {
        testUserService.releaseTestUser(userId);
    }
}
