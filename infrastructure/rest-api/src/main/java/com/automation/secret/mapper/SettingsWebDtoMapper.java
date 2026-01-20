package com.automation.secret.mapper;

import com.automation.secret.model.dto.SettingsDto;
import com.automation.secret.restapi.generated.dto.SettingsWebDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingsWebDtoMapper {
    SettingsWebDto toSettingsWebDto(SettingsDto settingsDto);
}
