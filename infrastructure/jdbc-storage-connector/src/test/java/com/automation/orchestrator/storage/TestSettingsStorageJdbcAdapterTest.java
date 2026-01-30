package com.automation.secret.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.automation.secret.model.SettingsEntity;
import com.automation.secret.model.dto.SettingsDto;
import com.automation.secret.repository.SettingsRepository;
import com.automation.secret.storage.mapper.SettingsMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestSettingsStorageJdbcAdapterTest {

    @Mock
    private SettingsRepository settingsRepository;

    @Mock
    private SettingsMapper mapper;

    private SettingsStorageImpl storage;

    @BeforeEach
    void setUp() {
        storage = new SettingsStorageImpl(settingsRepository, mapper);
    }

    @Test
    void getSettings_shouldReturnSettingsDto_whenSettingsExist() {
        List<SettingsEntity> entities = List.of(
            new SettingsEntity(1L, "testKey1", "testValue1"),
            new SettingsEntity(2L, "testKey2", "testValue2")
        );
        SettingsDto expectedDto = new SettingsDto(
            Map.of(
                "testKey1", "testValue1",
                "testKey2", "testValue2"
            )
        );

        when(settingsRepository.findAllSettings()).thenReturn(entities);
        when(mapper.toSettingsDto(entities)).thenReturn(expectedDto);

        SettingsDto result = storage.getSettings();

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(settingsRepository).findAllSettings();
        verify(mapper).toSettingsDto(entities);
    }

    @Test
    void getSettings_shouldReturnSettingsDto_whenNoSettingsExist() {
        List<SettingsEntity> emptyList = Collections.emptyList();
        SettingsDto expectedDto = new SettingsDto(Map.of());

        when(settingsRepository.findAllSettings()).thenReturn(emptyList);
        when(mapper.toSettingsDto(emptyList)).thenReturn(expectedDto);

        SettingsDto result = storage.getSettings();

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(settingsRepository).findAllSettings();
        verify(mapper).toSettingsDto(emptyList);
    }
}
