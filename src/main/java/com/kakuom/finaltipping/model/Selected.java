package com.kakuom.finaltipping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
public class Selected implements Comparable<Selected> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer gameNumber;

    private String team;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "pick_id")
    private Pick pick;

    public Selected() {
    }

    public Selected(Integer gameNumber, String team) {
        this.gameNumber = gameNumber;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Pick getPick() {
        return pick;
    }

    public void setPick(Pick pick) {
        this.pick = pick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Selected selected = (Selected) o;

        if (!gameNumber.equals(selected.gameNumber)) return false;
        return team.equals(selected.team);
    }

    @Override
    public int hashCode() {
        int result = gameNumber.hashCode();
        result = 31 * result + team.hashCode();
        return result;
    }

    @Override
    public int compareTo(@NotNull Selected o) {
        return Integer.compare(this.getGameNumber(), o.getGameNumber());
    }
}
