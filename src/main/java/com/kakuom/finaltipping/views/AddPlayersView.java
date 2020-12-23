package com.kakuom.finaltipping.views;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class AddPlayersView {
    @NotBlank
    private String teamName;

    @NotEmpty
    private Set<PlayerView> players;

    public AddPlayersView(String teamName, Set<PlayerView> players) {
        this.teamName = teamName;
        this.players = players;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<PlayerView> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerView> players) {
        this.players = players;
    }
}
