package org.wincc.report.model;

import lombok.Data;

@Data
public class MainReportDto {

    private String batchInput;

    private String dateInput;

    private String report;

    private boolean print;
}
