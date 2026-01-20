package com.automation.secret.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.automation.secret.model.dto.TestUserDto;
import com.automation.secret.port.output.storage.TestUserStorage;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestUserServiceImplTest {

    @Mock
    private TestUserStorage testUserStorage;

    @InjectMocks
    private TestUserServiceImpl testUserService;

    private TestUserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUserDto = new TestUserDto(
            "testuser@example.com",
            "user123",
            "securepassword",
            true,
            Instant.now()
        );
    }

    @Test
    void getAndLockTestUser_shouldFindAndLockUser() {
        when(testUserStorage.findNotLocked()).thenReturn(testUserDto);

        TestUserDto result = testUserService.getAndLockTestUser();

        assertNotNull(result);
        assertEquals(testUserDto.userId(), result.userId());
        assertEquals(testUserDto.email(), result.email());
        assertEquals(testUserDto.password(), result.password());
        assertEquals(testUserDto.isLocked(), result.isLocked());
        assertEquals(testUserDto.lockedAt(), result.lockedAt());

        verify(testUserStorage).findNotLocked();
        verify(testUserStorage).lock(testUserDto.userId());
    }

    @Test
    void releaseTestUser_shouldReleaseUser() {
        String userId = "user123";

        testUserService.releaseTestUser(userId);

        verify(testUserStorage).release(userId);
    }
}
