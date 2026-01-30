package com.automation.orchestrator.controller;

import com.automation.orchestrator.mapper.SettingsWebDtoMapper;
import com.automation.orchestrator.model.dto.SettingsDto;
import com.automation.orchestrator.port.input.settings.SettingsService;
import com.automation.orchestrator.restapi.generated.api.SettingsApi;
import com.automation.orchestrator.restapi.generated.dto.SettingsWebDto;
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
