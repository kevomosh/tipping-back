package com.kakuom.finaltipping.views;

import com.kakuom.finaltipping.dto.GameDTO;

import java.util.List;

public class CreateWeekView {
    private Integer weekNumber;

    private DateView dateView;

    private List<GameDTO> gamesToPlay;


    public CreateWeekView(Integer weekNumber, DateView dateView, List<GameDTO> gamesToPlay) {
        this.weekNumber = weekNumber;
        this.dateView = dateView;
        this.gamesToPlay = gamesToPlay;

    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public DateView getDateView() {
        return dateView;
    }

    public void setDateView(DateView dateView) {
        this.dateView = dateView;
    }

    public List<GameDTO> getGamesToPlay() {
        return gamesToPlay;
    }

    public void setGamesToPlay(List<GameDTO> gamesToPlay) {
        this.gamesToPlay = gamesToPlay;
    }
}
