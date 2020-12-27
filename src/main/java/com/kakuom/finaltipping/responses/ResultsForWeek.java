package com.kakuom.finaltipping.responses;

import com.kakuom.finaltipping.dto.ResultDTO;

import java.util.List;

public class ResultsForWeek {
    private Long total;
    private Long latestWeekNumber;
    private List<ResultDTO> results;

    public ResultsForWeek(Long total, List<ResultDTO> results, Long latestWeekNumber) {
        this.total = total;
        this.results = results;
        this.latestWeekNumber = latestWeekNumber;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<ResultDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultDTO> results) {
        this.results = results;
    }

    public Long getLatestWeekNumber() {
        return latestWeekNumber;
    }

    public void setLatestWeekNumber(Long latestWeekNumber) {
        this.latestWeekNumber = latestWeekNumber;
    }
}
