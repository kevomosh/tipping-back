package com.kakuom.finaltipping.dto;

public class WeekResultDTO {
    private Integer gameNumber;

    private String team;

    public WeekResultDTO(Integer gameNumber, String team) {
        this.gameNumber = gameNumber;
        this.team = team;
    }

    public Integer getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(Integer gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
