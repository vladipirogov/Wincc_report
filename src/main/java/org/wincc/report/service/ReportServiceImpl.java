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
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EntityManager entityManager;

    private DataSource getDataSource() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        return info.getDataSource();
    }

    @Override
    public Map<String, Object> repParameters(MainReportDto model) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("input_date_time", model.getDateInput());
        parameters.put("input_end_date_time", model.isEndDate() ? model.getEndDateInput() : model.getDateInput());
        parameters.put("input_batch", "");
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
