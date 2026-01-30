package com.automation.orchestrator.model.dto;

import java.util.Map;

public record SettingsDto(
    Map<String, String> settings
) {
}