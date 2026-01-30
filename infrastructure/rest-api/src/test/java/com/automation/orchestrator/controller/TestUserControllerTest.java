package com.automation.orchestrator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.automation.orchestrator.advice.ControllerAdvise;
import com.automation.orchestrator.exception.NoTestUserAvailableException;
import com.automation.orchestrator.mapper.TestUserWebDtoMapper;
import com.automation.orchestrator.model.dto.TestUserDto;
import com.automation.orchestrator.port.input.testuser.TestUserService;
import com.automation.orchestrator.restapi.generated.dto.TestUserWebDto;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    value = {
        TestUserController.class, ControllerAdvise.class
    }
)
@AutoConfigureMockMvc(addFilters = false)
class TestUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestUserService testUserService;

    @MockitoBean
    private TestUserWebDtoMapper mapper;

    @Test
    void getAndLockUser_shouldReturnTestUser_whenUserAvailable() throws Exception {
        TestUserDto testUserDto = new TestUserDto(
            "testuser@example.com",
            "user123",
            "securepassword",
            true,
            Instant.now()
        );
        TestUserWebDto testUserWebDto = new TestUserWebDto()
            .userId(testUserDto.userId())
            .email(testUserDto.email());

        when(testUserService.getAndLockTestUser()).thenReturn(testUserDto);
        when(mapper.toTestUserWebDto(any(TestUserDto.class))).thenReturn(testUserWebDto);

        mockMvc.perform(get("/api/test-user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserDto.userId()))
                .andExpect(jsonPath("$.email").value(testUserDto.email()));

        verify(testUserService).getAndLockTestUser();
        verify(mapper).toTestUserWebDto(testUserDto);
    }

    @Test
    void getAndLockUser_shouldReturnError_whenNoUserAvailable() throws Exception {
        when(testUserService.getAndLockTestUser()).thenThrow(new NoTestUserAvailableException("No unlocked test user available"));

        mockMvc.perform(get("/api/test-user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(testUserService).getAndLockTestUser();
    }

    @Test
    void releaseUser_shouldReleaseUser_whenValidUserId() throws Exception {
        String userId = "user123";
        doNothing().when(testUserService).releaseTestUser(userId);

        mockMvc.perform(post("/api/test-user/release/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(testUserService).releaseTestUser(userId);
    }

    @Test
    void releaseUser_shouldReturnError_whenUserIdNotFound() throws Exception {
        String userId = "nonexistent";
        doThrow(new IllegalStateException("Failed to release test user with ID: " + userId))
            .when(testUserService).releaseTestUser(userId);

        mockMvc.perform(post("/api/test-user/release/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(testUserService).releaseTestUser(userId);
    }
}
