package org.wincc.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wincc.report.model.Setting;

public interface SettingRepository extends JpaRepository<Setting, Integer> {
}
