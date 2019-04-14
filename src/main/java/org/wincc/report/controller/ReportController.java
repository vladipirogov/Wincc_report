package org.wincc.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wincc.report.model.Report;
import org.wincc.report.service.ReportService;
import reactor.core.publisher.Flux;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(path = "/report/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Report> feed() {
        return reportService.findInInterval();
    }
}
