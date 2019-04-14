package org.wincc.report.service;

import org.wincc.report.model.Report;
import reactor.core.publisher.Flux;

public interface ReportService {
    Flux<Report> findInInterval();
}
