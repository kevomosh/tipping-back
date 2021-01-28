package com.kakuom.finaltipping.responses;

import java.util.List;

public class WeekNumberAllTeams {
    private Long weekNumber;
    private List<String> teamNames;

    public WeekNumberAllTeams(Long weekNumber, List<String> teamNames) {
        this.weekNumber = weekNumber;
        this.teamNames = teamNames;
    }

    public Long getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Long weekNumber) {
        this.weekNumber = weekNumber;
    }

    public List<String> getTeamNames() {
        return teamNames;
    }

    public void setTeamNames(List<String> teamNames) {
        this.teamNames = teamNames;
    }
}
