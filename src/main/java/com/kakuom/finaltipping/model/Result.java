package com.kakuom.finaltipping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer gameNumber;

    @Column(length = 20)
    private String team;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "week_id")
    private Week week;

    public Result() {
    }

    public Result(Integer gameNumber, String team) {
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

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (!id.equals(result.id)) return false;
        return gameNumber.equals(result.gameNumber);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + gameNumber.hashCode();
        return result;
    }
}
