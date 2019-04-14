package org.wincc.report.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wincc.report.model.Report;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Test
    public void should_find_all() {
        List<Report> reportList = reportRepository.findAll();
        log.info(reportList.toString());
    }
}