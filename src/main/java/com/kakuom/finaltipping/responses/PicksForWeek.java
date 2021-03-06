package com.kakuom.finaltipping.responses;

import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.dto.WeekResultDTO;
import com.kakuom.finaltipping.model.Pick;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PicksForWeek {
    private Long total;
    private Integer pageNumber;
    private Boolean fwp;
    private String firstScorer;
    private Integer margin;
    private List<Pick> picks;
    private List<WeekResultDTO> winners;
    private List<GameDTO> gamesForWeek;
    private OffsetDateTime deadLine;
    Integer weekNumber;

    public PicksForWeek(List<Pick> picks, List<GameDTO> gamesForWeek,
                        Boolean fwp, OffsetDateTime deadLine, Integer weekNumber) {
        this.picks = picks;
        this.gamesForWeek = gamesForWeek;
        this.pageNumber = 0;
        this.winners = new ArrayList<>();
        this.total = (long)1;
        this.firstScorer = "";
        this.margin = 0;
        this.fwp = fwp;
        this.deadLine = deadLine;
        this.weekNumber = weekNumber;
    }

    public PicksForWeek(Long total, Integer pageNumber ,Boolean fwp,
                        String firstScorer, Integer margin,
                        List<Pick> picks, List<WeekResultDTO> winners,
                        List<GameDTO> gamesForWeek, OffsetDateTime deadLine,
                        Integer weekNumber) {
        this.total = total;
        this.pageNumber = pageNumber;
        this.fwp = fwp;
        this.firstScorer = firstScorer;
        this.margin = margin;
        this.picks = picks;
        this.winners = winners;
        this.gamesForWeek = gamesForWeek;
        this.deadLine = deadLine;
        this.weekNumber = weekNumber;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean getFwp() {
        return fwp;
    }

    public void setFwp(Boolean fwp) {
        this.fwp = fwp;
    }

    public String getFirstScorer() {
        return firstScorer;
    }

    public void setFirstScorer(String firstScorer) {
        this.firstScorer = firstScorer;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public List<Pick> getPicks() {
        return picks;
    }

    public void setPicks(List<Pick> picks) {
        this.picks = picks;
    }

    public List<WeekResultDTO> getWinners() {
        return winners;
    }

    public void setWinners(List<WeekResultDTO> winners) {
        this.winners = winners;
    }

    public List<GameDTO> getGamesForWeek() {
        return gamesForWeek;
    }

    public void setGamesForWeek(List<GameDTO> gamesForWeek) {
        this.gamesForWeek = gamesForWeek;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public OffsetDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(OffsetDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }
}
