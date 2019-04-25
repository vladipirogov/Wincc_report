package org.wincc.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wincc.report.model.MainReportDto;
import org.wincc.report.model.Report;
import org.wincc.report.service.ReportService;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(path = "/report/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Report> feed() {
        return reportService.findInInterval();
    }

    @PostMapping(path = "/report/main", produces="application/pdf;charset=UTF-8")
    public void report(HttpServletResponse response, @ModelAttribute(value="model") MainReportDto model) {
        response.setContentType("text/html");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("input_date_time", model.getDateInput());
        parameters.put("input_batch", model.getBatchInput());
        reportService.generateReport(response, parameters, model.getReport(), model.isPrint());
    }
}
