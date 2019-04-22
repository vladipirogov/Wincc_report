package org.wincc.report.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wincc.report.model.Report;
import org.wincc.report.service.ReportService;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(path = "/report/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Report> feed() {
        return reportService.findInInterval();
    }

    @GetMapping(path = "report/main")
    public void report(HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportService.report());
        try(InputStream inputStream = this.getClass().getResourceAsStream("/static/report/report.jrxml")) {
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
            exporter.exportReport();
            JasperViewer.viewReport(jasperPrint, false);
            }
    }
}
