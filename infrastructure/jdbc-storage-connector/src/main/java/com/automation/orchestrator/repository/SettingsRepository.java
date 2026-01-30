package com.automation.orchestrator.repository;

import com.automation.orchestrator.model.SettingsEntity;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface SettingsRepository extends CrudRepository<SettingsEntity, Long> {
    @Query("SELECT * FROM settings")
    List<SettingsEntity> findAllSettings();
}
