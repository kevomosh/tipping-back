package com.kakuom.finaltipping.views;

import com.kakuom.finaltipping.dto.WeekResultDTO;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class ResultView{
    private Integer margin;

    private String firstScorer;

    @NotEmpty
    private Set<WeekResultDTO> resultViewSet;

    public ResultView() {
    }

    public ResultView(Integer margin, String firstScorer, Set<WeekResultDTO> resultViewSet) {
        this.margin = margin;
        this.firstScorer = firstScorer;
        this.resultViewSet = resultViewSet;
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

    public Set<WeekResultDTO> getResultViewSet() {
        return resultViewSet;
    }

    public void setResultViewSet(Set<WeekResultDTO> resultViewSet) {
        this.resultViewSet = resultViewSet;
    }
}
