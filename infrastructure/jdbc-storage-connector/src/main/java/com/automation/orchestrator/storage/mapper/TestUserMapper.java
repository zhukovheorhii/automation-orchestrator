package com.automation.orchestrator.storage.mapper;

import com.automation.orchestrator.model.TestUserEntity;
import com.automation.orchestrator.model.dto.TestUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestUserMapper {
    TestUserDto toTestUserDto(TestUserEntity entity);
}
