package com.automation.secret.mapper;

import com.automation.secret.model.dto.TestUserDto;
import com.automation.secret.restapi.generated.dto.TestUserWebDto;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TestUserWebDtoMapperTest {

    private final TestUserWebDtoMapper mapper = Mappers.getMapper(TestUserWebDtoMapper.class);

    @Test
    void shouldMapTestUserDtoToTestUserWebDto() {
        TestUserDto testUserDto = new TestUserDto(
            "testuser@example.com",
            "user123",
            "securepassword",
            true,
            Instant.now()
        );

        TestUserWebDto result = mapper.toTestUserWebDto(testUserDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(testUserDto.email());
        assertThat(result.getUserId()).isEqualTo(testUserDto.userId());
    }
}
