package com.automation.orchestrator.storage;

import com.automation.orchestrator.model.SettingsEntity;
import com.automation.orchestrator.model.dto.SettingsDto;
import com.automation.orchestrator.port.output.storage.SettingsStorage;
import com.automation.orchestrator.repository.SettingsRepository;
import com.automation.orchestrator.storage.mapper.SettingsMapper;
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
