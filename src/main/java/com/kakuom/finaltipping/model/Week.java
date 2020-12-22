package com.kakuom.finaltipping.model;

import com.kakuom.finaltipping.dto.WeekInfoDTO;
import com.kakuom.finaltipping.enums.Comp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SqlResultSetMapping(
        name = "WeekInfoDtoMapping",
        classes = @ConstructorResult(
                targetClass = WeekInfoDTO.class,
                columns = {
                        @ColumnResult(name = "dead_line", type = OffsetDateTime.class),
                        @ColumnResult(name = "number", type = Integer.class),
                        @ColumnResult(name = "nxt", type = Boolean.class),
                        @ColumnResult(name = "margin", type = Integer.class),
                        @ColumnResult(name = "fs")
                }
        )
)
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    private Boolean scoreUpdated;

    @Column(nullable = false)
    private OffsetDateTime deadLine;

    private Integer margin;

    private String firstScorer;

    @Column(length = 3)
    private Comp comp;

    @OneToMany(
            mappedBy = "week",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Game> games = new ArrayList<>();

    @OneToMany(
            mappedBy = "week",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Result> resultList = new ArrayList<>();

    public Week() {
    }

    public Week(Integer number, OffsetDateTime deadLine, Comp comp) {
        this.number = number;
        this.deadLine = deadLine;
        this.comp = comp;
        this.scoreUpdated = false;
    }

    public void addGame(Game game) {
        games.add(game);
        game.setWeek(this);
    }

    public void removeGame(Game game) {
        games.remove(game);
        game.setWeek(null);
    }

    public void addResult(Result result) {
        resultList.add(result);
        result.setWeek(this);
    }

    public void removeResult(Result result) {
        resultList.remove(result);
        result.setWeek(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getScoreUpdated() {
        return scoreUpdated;
    }

    public void setScoreUpdated(Boolean scoreUpdated) {
        this.scoreUpdated = scoreUpdated;
    }

    public OffsetDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(OffsetDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public String getFirstScorer() {
        return firstScorer;
    }

    public void setFirstScorer(String firstScorer) {
        this.firstScorer = firstScorer;
    }

    public Comp getComp() {
        return comp;
    }

    public void setComp(Comp comp) {
        this.comp = comp;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Week week = (Week) o;

        if (!id.equals(week.id)) return false;
        if (!number.equals(week.number)) return false;
        return comp == week.comp;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + comp.hashCode();
        return result;
    }
}
