package com.isharipov.popularmoviesstagetwo.data.network;

import java.io.Serializable;
import java.util.List;

/**
 * 01.05.2018.
 */
public class TrailerResult implements Serializable {
    private String id;
    private List<Trailer> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
