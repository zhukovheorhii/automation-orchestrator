package com.automation.secret.storage.mapper;

import com.automation.secret.model.TestUserEntity;
import com.automation.secret.model.dto.TestUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestUserMapper {
    TestUserDto toTestUserDto(TestUserEntity entity);
}
