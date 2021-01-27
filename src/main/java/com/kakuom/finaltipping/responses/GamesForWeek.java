package com.kakuom.finaltipping.responses;

import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.dto.PlayerDTO;

import java.time.OffsetDateTime;
import java.util.List;

public class GamesForWeek {
    private OffsetDateTime deadLine;
    private Integer weekNumber;
    private Boolean fwp;
    private List<GameDTO> games;
    private List<PlayerDTO> players;

    public GamesForWeek(Integer weekNumber, List<GameDTO> games) {
        this.weekNumber = weekNumber;
        this.games = games;
    }

    public GamesForWeek(OffsetDateTime deadLine, Integer weekNumber, Boolean fwp,
                        List<GameDTO> games, List<PlayerDTO> players) {
        this.deadLine = deadLine;
        this.weekNumber = weekNumber;
        this.fwp = fwp;
        this.games = games;
        this.players = players;
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

    public Boolean getFwp() {
        return fwp;
    }

    public void setFwp(Boolean fwp) {
        this.fwp = fwp;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }
}
