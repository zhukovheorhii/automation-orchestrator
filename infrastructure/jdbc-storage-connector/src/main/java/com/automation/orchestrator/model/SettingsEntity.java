package com.automation.orchestrator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "settings")
public record SettingsEntity(
    @Id Long id,
    String key,
    String value
) {
}
