package com.automation.secret.controller;

import com.automation.secret.mapper.SettingsWebDtoMapper;
import com.automation.secret.model.dto.SettingsDto;
import com.automation.secret.port.input.settings.SettingsService;
import com.automation.secret.restapi.generated.api.SettingsApi;
import com.automation.secret.restapi.generated.dto.SettingsWebDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController implements SettingsApi {

    private final SettingsService settingsService;

    private final SettingsWebDtoMapper mapper;

    public SettingsController(SettingsService settingsService, SettingsWebDtoMapper mapper) {
        this.settingsService = settingsService;
        this.mapper = mapper;
    }

    @Override
    public SettingsWebDto getSettings() {
        SettingsDto settingsDto = settingsService.getSettings();
        return mapper.toSettingsWebDto(settingsDto);
    }
}
