package org.wincc.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wincc.report.model.MainReportDto;
import org.wincc.report.service.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;


    @PostMapping(path = "/report/main", produces="application/pdf;charset=UTF-8")
    public void report(HttpServletResponse response, @ModelAttribute(value="model") MainReportDto model) {
        response.setContentType("text/html");
        Map<String, Object> parameters = reportService.repParameters(model);
        reportService.generateReport(response, parameters, model.getReport(), model.isPrint());
    }
}
