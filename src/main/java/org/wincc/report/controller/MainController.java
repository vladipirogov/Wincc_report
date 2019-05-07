package org.wincc.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wincc.report.model.MainReportDto;
import org.wincc.report.model.Report;
import org.wincc.report.model.Setting;
import org.wincc.report.model.SettingDto;
import org.wincc.report.repository.ReportRepository;
import org.wincc.report.repository.SettingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@Slf4j
public class MainController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SettingRepository settingRepository;

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String index(Model model) {
        LocalDateTime dateStart = LocalDate.now().atStartOfDay();
        LocalDateTime dateEnd = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Report> reportList = reportRepository.getByPeriodReport(dateStart, dateEnd);
        model.addAttribute("reports", reportList);
        model.addAttribute("model", new MainReportDto());
        return "index";
    }

    @GetMapping(path = "/reports")
    public String reports(Model model) {
        model.addAttribute("model", new MainReportDto());
        return "reports";
    }

    @GetMapping(path = "/history")
    public String history() {
        return "history";
    }

    @GetMapping(path = "/setting")
    public String setting(Model model) {
        Setting setting = settingRepository.getOne(1);
        model.addAttribute("setting", setting);
        return "setting";
    }

    @PutMapping(path = "/setting")
    public void updateSetting(@RequestBody Setting input, Model model) {
        Setting setting = settingRepository.getOne(1);
        setting.setUpdateInterval(input.getUpdateInterval());
        model.addAttribute("setting", setting);
        settingRepository.save(setting);
    }

    @PostMapping(path = "/setting")
    @ResponseBody
    public SettingDto getSetting() {
        Setting setting = settingRepository.getOne(1);
        SettingDto dto = SettingDto.builder()
                .updateInterval(setting.getUpdateInterval())
                .build();
        return dto;
    }

    @PostMapping(path = "/period")
    public String reportByPeriod(@ModelAttribute(value="model") MainReportDto reportDto, Model model) {
        try {
            LocalDateTime dateStart = LocalDate.parse(reportDto.getDateInput(), formatter).atStartOfDay();
            LocalDateTime dateEnd = LocalDate.parse(reportDto.getEndDateInput(), formatter).atStartOfDay();
            List<Report> reportList = reportRepository.getByPeriodReport(dateStart, dateEnd);
            model.addAttribute("model", new MainReportDto());
            model.addAttribute("reports", reportList);
        }
        catch (DateTimeParseException ex) {
            log.error(ex.getMessage());
        }
        return "index";
    }

    @PostMapping(path = "/find_updates")
    @ResponseBody
    public List<Report> getUpdates(@RequestBody MainReportDto dto) {
        List<Report> result = null;
        try {
            LocalDateTime dateStart = getDateStart(dto.getDateInput());
            LocalDateTime dateEnd = getDateStart(dto.getEndDateInput());
            result = reportRepository.getByPeriodReport(dateStart, dateEnd);
        }
        catch (DateTimeParseException ex) {
            log.error(ex.getMessage());
        }
        return result;
    }

    private LocalDateTime getDateStart(String inputDateStart) throws DateTimeParseException {
        if (inputDateStart == null || inputDateStart.isEmpty())
            return LocalDate.now().atStartOfDay();
        return LocalDateTime.parse(inputDateStart, timeFormatter);
    }
}
