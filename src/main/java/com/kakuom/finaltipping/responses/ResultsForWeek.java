package com.kakuom.finaltipping.responses;

import com.kakuom.finaltipping.dto.ResultDTO;

import java.util.List;

public class ResultsForWeek {
    private Long total;
    private Integer pageNumber;
    private Integer size;
    private Long latestWeekNumber;
    private List<ResultDTO> results;

    public ResultsForWeek(Long total,Integer pageNumber, Integer size,  List<ResultDTO> results, Long latestWeekNumber) {
        this.total = total;
        this.pageNumber = pageNumber;
        this.size = size;
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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
