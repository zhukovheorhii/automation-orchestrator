package com.automation.secret.storage;

import com.automation.secret.model.SettingsEntity;
import com.automation.secret.model.dto.SettingsDto;
import com.automation.secret.port.output.storage.SettingsStorage;
import com.automation.secret.repository.SettingsRepository;
import com.automation.secret.storage.mapper.SettingsMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SettingsStorageImpl implements SettingsStorage {
    private final SettingsRepository settingsRepository;
    private final SettingsMapper mapper;


    public SettingsStorageImpl(SettingsRepository settingsRepository, SettingsMapper mapper) {
        this.settingsRepository = settingsRepository;
        this.mapper = mapper;
    }

    @Override
    public SettingsDto getSettings() {
        List<SettingsEntity> settingsEntityList = settingsRepository.findAllSettings();
        return mapper.toSettingsDto(settingsEntityList);
    }
}
