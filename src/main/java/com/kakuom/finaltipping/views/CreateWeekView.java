package com.kakuom.finaltipping.views;

import com.kakuom.finaltipping.dto.GameDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateWeekView {
    @NotNull
    @Min(value = 1)
    @Max(value = 50)
    private Integer weekNumber;

    @NotNull
    private DateView dateView;

    @NotEmpty
    private List<GameDTO> gamesToPlay;

    public CreateWeekView() {
    }

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
