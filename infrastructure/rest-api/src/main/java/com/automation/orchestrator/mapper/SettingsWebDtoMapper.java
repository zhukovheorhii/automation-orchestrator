package com.automation.orchestrator.mapper;

import com.automation.orchestrator.model.dto.SettingsDto;
import com.automation.orchestrator.restapi.generated.dto.SettingsWebDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingsWebDtoMapper {
    SettingsWebDto toSettingsWebDto(SettingsDto settingsDto);
}
