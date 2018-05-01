package com.isharipov.popularmoviesstagetwo.data.network;

import java.io.Serializable;
import java.util.List;

/**
 * 01.05.2018.
 */
public class ReviewResult implements Serializable {
    private String id;
    private String page;
    private List<Review> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
