package org.wincc.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wincc.report.model.Report;
import org.wincc.report.repository.ReportRepository;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Flux<Report> findInInterval() {
        return Flux.interval(Duration.ofSeconds(1))
                .onBackpressureDrop()
                .map(this::generateReports)
                .flatMapIterable(x -> x);
    }

    private List<Report> generateReports(long interval) {
        return reportRepository.findAll();
    }
}
