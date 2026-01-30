package com.automation.orchestrator.storage.mapper;

import com.automation.orchestrator.model.SettingsEntity;
import com.automation.orchestrator.model.dto.SettingsDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SettingsMapper {
    public SettingsDto toSettingsDto(List<SettingsEntity> settingsEntityList) {
        Map<String, String> settings = settingsEntityList
            .stream()
            .collect(Collectors.toMap(SettingsEntity::key, SettingsEntity::value));
        return new SettingsDto(settings);
    }
}
