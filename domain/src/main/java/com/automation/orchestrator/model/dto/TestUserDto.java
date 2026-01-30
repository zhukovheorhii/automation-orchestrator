package com.automation.orchestrator.model.dto;

import java.time.Instant;

public record TestUserDto(
        String email,
        String userId,
        String password,
        boolean isLocked,
        Instant lockedAt
) {
}
