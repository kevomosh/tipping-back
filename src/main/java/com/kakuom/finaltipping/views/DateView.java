package com.kakuom.finaltipping.views;

public class DateView {
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private String offset;

    public DateView(Integer month, Integer day,
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
