package com.nortonlam.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norton
 * <p/>
 * Created date: 10/19/15.
 */
public class Video {
    public enum  Type {
        Trailer { @Override public String toString() { return "Trailer"; } }
    }

    private String id;
    @SerializedName("iso_639_1")
    private String language;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public String getName() {
        return name;
    }

    public boolean isTrailer() {
        return Type.Trailer.toString().equals(type);
    }
}
