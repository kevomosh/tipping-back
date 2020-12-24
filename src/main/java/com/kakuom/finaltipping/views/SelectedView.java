package com.kakuom.finaltipping.views;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SelectedView {
    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer gameNumber;

    @NotBlank
    private String team;

    public SelectedView() {
    }

    public SelectedView(Integer gameNumber, String team) {
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
