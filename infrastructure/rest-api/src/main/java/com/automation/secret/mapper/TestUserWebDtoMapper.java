package com.automation.secret.mapper;

import com.automation.secret.model.dto.TestUserDto;
import com.automation.secret.restapi.generated.dto.TestUserWebDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestUserWebDtoMapper {
    TestUserWebDto toTestUserWebDto(TestUserDto testUserDto);
}
