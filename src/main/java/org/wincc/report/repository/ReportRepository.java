package org.wincc.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wincc.report.model.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}
