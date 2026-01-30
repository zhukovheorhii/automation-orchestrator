package com.automation.orchestrator.config;

import com.automation.orchestrator.port.input.settings.SettingsService;
import com.automation.orchestrator.port.input.testuser.TestUserService;
import com.automation.orchestrator.port.output.storage.SettingsStorage;
import com.automation.orchestrator.port.output.storage.TestUserStorage;
import com.automation.orchestrator.service.SettingsServiceImpl;
import com.automation.orchestrator.service.TestUserServiceImpl;
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
