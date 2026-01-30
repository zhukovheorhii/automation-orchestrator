package com.automation.orchestrator.service;

import com.automation.orchestrator.model.dto.SettingsDto;
import com.automation.orchestrator.port.input.settings.SettingsService;
import com.automation.orchestrator.port.output.storage.SettingsStorage;

public class SettingsServiceImpl implements SettingsService {

    private final SettingsStorage settingsStorage;

    public SettingsServiceImpl(SettingsStorage settingsStorage) {
        this.settingsStorage = settingsStorage;
    }

    @Override
    public SettingsDto getSettings() {
        return settingsStorage.getSettings();
    }
}
