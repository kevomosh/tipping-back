package com.kakuom.finaltipping.dto;

public class GameDTO {
    private Integer gameNumber;
    private String homeTeam;
    private String awayTeam;

    public GameDTO(Integer gameNumber, String homeTeam, String awayTeam) {
        this.gameNumber = gameNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Integer getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(Integer gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }
}
