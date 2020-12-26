package com.kakuom.finaltipping.views;

import javax.validation.constraints.*;
import java.util.List;

public class PickView {

    @NotNull(message = "week number required")
    @Min(0)
    @Max(25)
    private Integer weekNumber;

    @NotEmpty
    private List<SelectedView> selectedViewList;

    @NotNull
    @Min(0)
    @Max(200)
    private Integer margin;

    @NotBlank
    private String firstScorer;

    public PickView( Integer weekNumber, List<SelectedView> selectedViewList,
                    Integer margin, String firstScorer) {
        this.weekNumber = weekNumber;
        this.selectedViewList = selectedViewList;
        this.margin = margin;
        this.firstScorer = firstScorer;
    }


    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }


    public List<SelectedView> getSelectedViewList() {
        return selectedViewList;
    }

    public void setSelectedViewList(List<SelectedView> selectedViewList) {
        this.selectedViewList = selectedViewList;
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
}
