package org.wincc.report.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SettingDto {
    private int updateInterval;
}
