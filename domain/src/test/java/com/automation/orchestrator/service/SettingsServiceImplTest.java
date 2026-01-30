package com.automation.orchestrator.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.automation.orchestrator.model.dto.SettingsDto;
import com.automation.orchestrator.port.output.storage.SettingsStorage;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingsServiceImplTest {

    @Mock
    private SettingsStorage settingsStorage;

    @InjectMocks
    private SettingsServiceImpl settingsService;


    @Test
    void getSettings_shouldReturnSettings() {
        SettingsDto settingsDto = new SettingsDto(Map.of(
            "testKey1", "testValue1",
            "testKey2", "testValue2"
        ));

        when(settingsStorage.getSettings()).thenReturn(settingsDto);

        SettingsDto result = settingsService.getSettings();

        assert(result).equals(settingsDto);

        verify(settingsStorage).getSettings();
    }
}
