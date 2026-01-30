package com.automation.orchestrator.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.orchestrator.model.dto.SettingsDto;
import com.automation.orchestrator.restapi.generated.dto.SettingsWebDto;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SettingsWebDtoMapperTest {
    private final SettingsWebDtoMapper mapper = Mappers.getMapper(SettingsWebDtoMapper.class);

    @Test
    void shouldMapSettingsDtoToSettingsWebDto() {
        var settingsDto = new SettingsDto(
            Map.of("key1", "value1", "key2", "value2")
        );

        SettingsWebDto result = mapper.toSettingsWebDto(settingsDto);

        assertThat(result).isNotNull();
        assertThat(result.getSettings()).isEqualTo(settingsDto.settings());
    }
}
