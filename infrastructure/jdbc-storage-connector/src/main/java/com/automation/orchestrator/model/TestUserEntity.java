package com.automation.orchestrator.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_user")
public class TestUserEntity {
    @Id
    private Long id;

    @Column("user_id")
    private String userId;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("is_locked")
    private boolean isLocked;

    @Column("locked_at")
    private Instant lockedAt;
}
