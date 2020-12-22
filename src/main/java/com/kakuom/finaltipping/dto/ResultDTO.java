package com.kakuom.finaltipping.dto;

public class ResultDTO {
    private String name;
    private Integer lastWeekScore;
    private Integer totalScore;


    public ResultDTO(String name, Integer lastWeekScore, Integer totalScore) {
        this.name = name;
        this.lastWeekScore = lastWeekScore;
        this.totalScore = totalScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getLastWeekScore() {
        return lastWeekScore;
    }

    public void setLastWeekScore(Integer lastWeekScore) {
        this.lastWeekScore = lastWeekScore;
    }
}
