package com.automation.secret.repository;

import com.automation.secret.model.TestUserEntity;
import java.time.Instant;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TestUserEntityRepository extends CrudRepository<TestUserEntity, Long> {
    @Query("""
        SELECT *
         FROM test_user
         WHERE is_locked = FALSE
         LIMIT 1
        """)
    TestUserEntity findNotLocked();

    @Modifying
    @Query("""
        UPDATE test_user
        SET is_locked = TRUE, locked_at = CURRENT_TIMESTAMP
        WHERE user_id = :userId
        """)
    int lock(@Param("userId") String userId);

    @Modifying
    @Query("""
        UPDATE test_user
        SET is_locked = FALSE, locked_at = NULL
        WHERE user_id = :userId
        """)
    int release(@Param("userId") String userId);

    @Modifying
    @Query("""
        UPDATE test_user
        SET is_locked = FALSE, locked_at = NULL
        WHERE is_locked = TRUE
        AND locked_at < :threshold
        """)
    int unlockStaleUsers(@Param("threshold") Instant threshold);
}
