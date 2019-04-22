package org.wincc.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wincc.report.model.Report;
import org.wincc.report.repository.ReportRepository;
import org.wincc.report.repository.SettingRepository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SettingRepository settingRepository;

    @Override
    public Flux<Report> findInInterval() {
        int interval = settingRepository.getOne(1).getUpdateInterval();
        return Flux.interval(Duration.ofSeconds(interval))
                .onBackpressureDrop()
                .map(this::generateReports)
                .flatMapIterable(x -> x);
    }

    @Override
    public List<Map<String, Object>> report() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Report report : reportRepository.findAll()) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("date_time", report.getDateTime());
            result.add(item);
        }
        return result;
    }

    private List<Report> generateReports(long interval) {
        return reportRepository.findAll();
    }
}
