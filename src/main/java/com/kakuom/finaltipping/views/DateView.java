package com.kakuom.finaltipping.views;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DateView {

    @Min(value = 1)
    @Max(value = 12)
    @NotNull
    private Integer month;

    @NotNull
    @Min(value = 1)
    @Max(value = 31)
    private Integer day;

    @NotNull
    @Min(value = 14)
    @Max(value = 21)
    private Integer hour;

    @NotNull
    @Min(value = 0)
    @Max(value = 59)
    private Integer minute;

    @NotBlank
    private String offset;

    public DateView() {
    }

    public DateView( Integer month, Integer day,
                    Integer hour, Integer minute, String offset) {
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.offset = offset;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

}
