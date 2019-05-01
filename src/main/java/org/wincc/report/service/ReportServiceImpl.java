package org.wincc.report.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.wincc.report.model.MainReportDto;
import org.wincc.report.model.Report;
import org.wincc.report.repository.ReportRepository;
import org.wincc.report.repository.SettingRepository;
import reactor.core.publisher.Flux;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private EntityManager entityManager;

    private DataSource getDataSource() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        return info.getDataSource();
    }

    @Override
    public Flux<Report> findInInterval() {
        int interval = settingRepository.getOne(1).getUpdateInterval();
        Flux<Report> flux = Flux.interval(Duration.ofSeconds(interval))
                .onBackpressureDrop()
                .map(x -> this.generateReports(interval))
                .flatMapIterable(x -> x);
        return flux;
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
        LocalDateTime dateStart = LocalDateTime.now().minusSeconds(interval).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateEnd = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return reportRepository.getByPeriodReport(dateStart, dateEnd);
    }

    @Override
    public Map<String, Object> repParameters(MainReportDto model) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("input_date_time", model.getDateInput());
        parameters.put("input_end_date_time", model.isEndDate() ? model.getEndDateInput() : model.getDateInput());
        parameters.put("input_batch", model.getBatchInput());
        return parameters;
    }

    @Override
    public void generateReport(HttpServletResponse response,
                               Map<String, Object> parameters,
                               String report,
                               boolean print) {
        try(InputStream inputStream = this.getClass().getResourceAsStream("/static/report/main_report.jrxml");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ServletOutputStream servletOutputStream = response.getOutputStream()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getDataSource().getConnection());
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            response.setContentLength(baos.size());
            baos.writeTo(servletOutputStream);
            if (print == true)
                JasperViewer.viewReport(jasperPrint, false);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SQLException e) {
            log.error(e.getMessage());
        } catch (JRException e) {
            log.error(e.getMessage());
        }
    }
}
