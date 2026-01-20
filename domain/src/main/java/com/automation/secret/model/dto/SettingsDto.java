package com.automation.secret.model.dto;

import java.util.Map;

public record SettingsDto(
    Map<String, String> settings
) {
}