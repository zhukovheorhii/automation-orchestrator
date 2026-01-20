package com.automation.secret.config;

import com.automation.secret.port.input.settings.SettingsService;
import com.automation.secret.port.input.testuser.TestUserService;
import com.automation.secret.port.output.storage.SettingsStorage;
import com.automation.secret.port.output.storage.TestUserStorage;
import com.automation.secret.service.SettingsServiceImpl;
import com.automation.secret.service.TestUserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class BeanConfiguration {

    @Bean
    @Transactional
    public TestUserService testUserService(TestUserStorage testUserStorage) {
        return new TestUserServiceImpl(testUserStorage);
    }

    @Bean
    public SettingsService settingsService(SettingsStorage settingsStorage) {
        return new SettingsServiceImpl(settingsStorage);
    }
}
