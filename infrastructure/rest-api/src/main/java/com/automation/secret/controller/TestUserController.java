package com.automation.secret.controller;

import com.automation.secret.mapper.TestUserWebDtoMapper;
import com.automation.secret.model.dto.TestUserDto;
import com.automation.secret.port.input.testuser.TestUserService;
import com.automation.secret.restapi.generated.api.TestUserApi;
import com.automation.secret.restapi.generated.dto.TestUserWebDto;
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
