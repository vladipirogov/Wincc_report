package org.wincc.report.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "energo")
@Data
@EqualsAndHashCode(of = {"id"})
public class Energo {

    @Id
    private long id;

    @Column(name = "date_time")
    private Timestamp dateTime;

    @Column(name = "counter_dt")
    private long counterDt;

    @Column(name = "counter_1")
    private int counter1;

    @Column(name = "counter_2")
    private int counter2;

    @Column(name = "counter_3")
    private int counter3;

    @Column(name = "oper")
    private String oper;

    @Column(name = "trig_1")
    private int trig1;
}
