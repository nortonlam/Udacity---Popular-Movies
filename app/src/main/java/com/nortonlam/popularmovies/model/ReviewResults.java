package com.nortonlam.popularmovies.model;

import java.util.List;

/**
 * Created by norton
 * <p/>
 * Created date: 10/19/15.
 */
public class ReviewResults {
    private long id;
    private int page;
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }
}
