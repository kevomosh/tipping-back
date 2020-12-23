package com.kakuom.finaltipping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakuom.finaltipping.enums.Comp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pick {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer weekNumber;

    @Column(length = 3)
    private Comp comp;

    private Integer score;

    @Column(length = 50)
    private String name;

    private Integer margin;

    @Column(length = 40)
    private String firstScorer;

    private Integer extraPoint;

    @OneToMany(
            mappedBy = "pick",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Selected> teamsSelected = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    private User user;

    public Pick() {
    }


    public Pick(Integer weekNumber, Comp comp, String name,
                Integer margin, String firstScorer) {
        this.weekNumber = weekNumber;
        this.comp = comp;
        this.name = name;
        this.margin = margin;
        this.firstScorer = firstScorer;
        this.score = 0;
        this.extraPoint = 0;
    }

    public void addSelected(Selected selected) {
        teamsSelected.add(selected);
        selected.setPick(this);
    }

    public void removeSelected(Selected selected) {
        teamsSelected.remove(selected);
        selected.setPick(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Comp getComp() {
        return comp;
    }

    public void setComp(Comp comp) {
        this.comp = comp;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getExtraPoint() {
        return extraPoint;
    }

    public void setExtraPoint(Integer extraPoint) {
        this.extraPoint = extraPoint;
    }


    public List<Selected> getTeamsSelected() {
        return teamsSelected;
    }

    public void setTeamsSelected(List<Selected> teamsSelected) {
        this.teamsSelected = teamsSelected;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pick pick = (Pick) o;

        if (!weekNumber.equals(pick.weekNumber)) return false;
        return comp == pick.comp;
    }

    @Override
    public int hashCode() {
        int result = weekNumber.hashCode();
        result = 31 * result + comp.hashCode();
        return result;
    }


}
