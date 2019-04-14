package org.wincc.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wincc.report.model.Energo;

public interface EnergoRepository extends JpaRepository<Energo, Integer> {
}
