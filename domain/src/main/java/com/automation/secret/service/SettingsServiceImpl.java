package com.automation.secret.service;

import com.automation.secret.model.dto.SettingsDto;
import com.automation.secret.port.input.settings.SettingsService;
import com.automation.secret.port.output.storage.SettingsStorage;

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
