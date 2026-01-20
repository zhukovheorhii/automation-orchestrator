package com.automation.secret.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.automation.secret.advice.ControllerAdvise;
import com.automation.secret.mapper.SettingsWebDtoMapper;
import com.automation.secret.model.dto.SettingsDto;
import com.automation.secret.port.input.settings.SettingsService;
import com.automation.secret.restapi.generated.dto.SettingsWebDto;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    value = {
        SettingsController.class, ControllerAdvise.class
    }
)
@AutoConfigureMockMvc(addFilters = false)
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SettingsService settingsService;

    @MockitoBean
    private SettingsWebDtoMapper mapper;

    @Test
    void getSettings_shouldReturnSettings_whenCalled() throws Exception {
        SettingsDto settingsDto = new SettingsDto(
            Map.of("key1", "value1", "key2", "value2")
        );
        SettingsWebDto settingsWebDto = new SettingsWebDto()
            .settings(settingsDto.settings());

        when(settingsService.getSettings()).thenReturn(settingsDto);
        when(mapper.toSettingsWebDto(any(SettingsDto.class))).thenReturn(settingsWebDto);

        mockMvc.perform(get("/api/settings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settings.key1").value("value1"))
                .andExpect(jsonPath("$.settings.key2").value("value2"));

        verify(settingsService).getSettings();
        verify(mapper).toSettingsWebDto(settingsDto);
    }
}
