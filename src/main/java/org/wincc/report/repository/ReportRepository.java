package org.wincc.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wincc.report.model.Report;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query(value = "select r from Report r where r.dateTime between :date_start and :date_end and r.code <> 0")
    List<Report> getByPeriodReport(@Param("date_start")LocalDateTime dateStart,
                                   @Param("date_end")LocalDateTime dateEnd);
}
