package com.kakuom.finaltipping.responses;

import com.kakuom.finaltipping.dto.ResultDTO;

import java.util.List;

public class ResultsForWeek {
    private Long total;
    private List<ResultDTO> results;

    public ResultsForWeek(Long total, List<ResultDTO> results) {
        this.total = total;
        this.results = results;
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
}
