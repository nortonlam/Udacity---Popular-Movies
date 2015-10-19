package com.nortonlam.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by norton
 *
 * Created date: 10/7/15.
 */
public class MovieResults {
    private int page;
    private List<Movie> results;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public List<Movie> getResults() {
        return results;
    }
}
