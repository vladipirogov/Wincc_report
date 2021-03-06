package org.wincc.report.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Entity
@Table(name = "report")
@Data
@EqualsAndHashCode(of = "id")
public class Report {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    @Id
    private long id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "num")
    private int num;

    @Column(name = "code")
    private int code;

    @Column(name = "r_name")
    private String rName;

    @Column(name = "rub_0_5")
    private int rub05;

    @Column(name = "rub_5_10")
    private int rub510;

    @Column(name = "rub_10_20")
    private int rub1020;

    @Column(name = "rub_20_40")
    private int rub2040;

    @Column(name = "cellulose")
    private int cellulose;

    @Column(name = "mineral")
    private int mineral;

    @Column(name = "dust")
    private int dust;

    @Column(name = "bitum")
    private int bitum;

    @Column(name = "temper")
    private int temper;

    @Column(name = "mix_time")
    private int mixTime;

    @Column(name = "weight")
    private int weight;

    @Column(name = "all_weight")
    private long allWeight;

    @Column(name = "oper")
    private String oper;

    @Column(name = "trig_2")
    private int trig2;

    public String getDateTime() {
        return dateTime.format(formatter);
    }
}
