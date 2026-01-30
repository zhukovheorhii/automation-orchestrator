package com.automation.orchestrator.mapper;

import com.automation.orchestrator.model.dto.TestUserDto;
import com.automation.orchestrator.restapi.generated.dto.TestUserWebDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestUserWebDtoMapper {
    TestUserWebDto toTestUserWebDto(TestUserDto testUserDto);
}
