package org.wincc.report.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "setting")
@Data
@EqualsAndHashCode(of = {"id"})
public class Setting {

    @Id
    private int id;

    @Column(name = "update_interval")
    private int updateInterval;

    @Column(name = "report_path")
    private String reportPath;
}
