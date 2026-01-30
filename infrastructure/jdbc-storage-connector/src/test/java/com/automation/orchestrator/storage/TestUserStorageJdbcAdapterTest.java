package com.automation.secret.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.automation.secret.exception.NoTestUserAvailableException;
import com.automation.secret.model.TestUserEntity;
import com.automation.secret.model.dto.TestUserDto;
import com.automation.secret.repository.TestUserEntityRepository;
import com.automation.secret.storage.mapper.TestUserMapper;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestUserStorageJdbcAdapterTest {

    @Mock
    private TestUserEntityRepository repository;

    @Mock
    private TestUserMapper mapper;

    private TestUserStorageImpl storage;

    @BeforeEach
    void setUp() {
        storage = new TestUserStorageImpl(repository, mapper);
    }

    @Test
    void findNotLocked_shouldReturnTestUserDto_whenUserAvailable() {
        TestUserEntity entity = new TestUserEntity();
        TestUserDto expectedDto = new TestUserDto(
            "email@test.com",
            "1234567",
            "securepassword",
            true,
            Instant.now()
        );

        when(repository.findNotLocked()).thenReturn(entity);
        when(mapper.toTestUserDto(entity)).thenReturn(expectedDto);

        TestUserDto result = storage.findNotLocked();

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(repository).findNotLocked();
        verify(mapper).toTestUserDto(entity);
    }

    @Test
    void findNotLocked_shouldThrowNoTestUserAvailableException_whenNoUserAvailable() {
        when(repository.findNotLocked()).thenReturn(null);

        assertThrows(NoTestUserAvailableException.class, () -> storage.findNotLocked());
        verify(repository).findNotLocked();
        verifyNoInteractions(mapper);
    }

    @Test
    void lock_shouldSucceed_whenUserExists() {
        String userId = "user1";
        when(repository.lock(userId)).thenReturn(1);

        assertDoesNotThrow(() -> storage.lock(userId));
        verify(repository).lock(userId);
    }

    @Test
    void lock_shouldThrowIllegalStateException_whenLockFails() {
        String userId = "user1";
        when(repository.lock(userId)).thenReturn(0);

        IllegalStateException exception = assertThrows(
            IllegalStateException.class, () -> storage.lock(userId)
        );
        assertEquals("Failed to lock test user with ID: user1", exception.getMessage());
        verify(repository).lock(userId);
    }

    @Test
    void release_shouldSucceed_whenUserExists() {
        String userId = "user1";
        when(repository.release(userId)).thenReturn(1);

        assertDoesNotThrow(() -> storage.release(userId));
        verify(repository).release(userId);
    }

    @Test
    void release_shouldThrowIllegalStateException_whenReleaseFails() {
        String userId = "user1";
        when(repository.release(userId)).thenReturn(0);

        IllegalStateException exception = assertThrows(
            IllegalStateException.class, () -> storage.release(userId)
        );
        assertEquals("Failed to release test user with ID: user1", exception.getMessage());
        verify(repository).release(userId);
    }

    @Test
    void unlockStaleUsers_shouldUnlockUsers_whenStaleUsersExist() {
        Instant threshold = Instant.now().minusSeconds(3600);
        when(repository.unlockStaleUsers(threshold)).thenReturn(3);

        assertDoesNotThrow(() -> storage.unlockStaleUsers(threshold));
        verify(repository).unlockStaleUsers(threshold);
    }

    @Test
    void unlockStaleUsers_shouldHandleNoStaleUsers() {
        Instant threshold = Instant.now().minusSeconds(3600);
        when(repository.unlockStaleUsers(threshold)).thenReturn(0);

        assertDoesNotThrow(() -> storage.unlockStaleUsers(threshold));
        verify(repository).unlockStaleUsers(threshold);
    }
}
