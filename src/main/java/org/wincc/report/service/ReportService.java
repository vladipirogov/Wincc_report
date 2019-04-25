package org.wincc.report.service;

import org.wincc.report.model.Report;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ReportService {
    Flux<Report> findInInterval();

    public List<Map<String, Object>> report();

    void generateReport(HttpServletResponse response, Map<String, Object> parameters, String report, boolean print);
}
