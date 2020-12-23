package com.kakuom.finaltipping.views;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class PickView {
    @NotBlank(message = "Id required")
    private Long userId;

    @NotBlank(message = "week number required")
    @Min(0)
    @Max(25)
    private Integer weekNumber;

    @NotBlank()
    private List<SelectedView> selectedViewList;

    private Integer margin;

    private String firstScorer;

    public PickView(Long userId, Integer weekNumber, List<SelectedView> selectedViewList,
                    Integer margin, String firstScorer) {
        this.userId = userId;
        this.weekNumber = weekNumber;
        this.selectedViewList = selectedViewList;
        this.margin = margin;
        this.firstScorer = firstScorer;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
