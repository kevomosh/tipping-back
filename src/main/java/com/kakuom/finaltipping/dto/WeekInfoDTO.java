package com.kakuom.finaltipping.dto;

import java.time.OffsetDateTime;

public class WeekInfoDTO {
    private OffsetDateTime deadLine;
    private Integer number;
    private Boolean fwp;
    private Integer margin;
    private String firstScorer;


    public WeekInfoDTO(OffsetDateTime deadLine, Integer number,
                       Boolean fwp, Integer margin, String firstScorer) {
        this.deadLine = deadLine;
        this.number = number;
        this.fwp = fwp;
        this.margin = margin;
        this.firstScorer = firstScorer;
    }

    public OffsetDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(OffsetDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getFwp() {
        return fwp;
    }

    public void setFwp(Boolean fwp) {
        this.fwp = fwp;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public String getFirstScorer() {
        return firstScorer;
    }

    public void setFirstScorer(String firstScorer) {
        this.firstScorer = firstScorer;
    }

}
