package org.wincc.report.service;

import org.wincc.report.model.MainReportDto;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ReportService {

    Map<String, Object> repParameters(MainReportDto model);

    void generateReport(HttpServletResponse response, Map<String, Object> parameters, String report, boolean print);
}
