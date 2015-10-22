package com.nortonlam.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by norton
 *
 * Created date: 10/7/15.
 */
public class Movie implements Parcelable {
    public final static String PARAM_KEY = "movie";

    // Date format for serialization
    private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private long id;
    private String title;
    @SerializedName("original_title")
    private String originalTitle;
    private String overview;
    private double popularity;
    private boolean video;
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("genre_ids")
    private List<Integer> genreIdList;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("vote_average")
    private String voteAverage;
    @SerializedName("vote_count")
    private String voteCount;

    public Movie(long id, String title) { //}, String originalTitle, String overview, double popularity, boolean video, boolean adult, String backdropPath, List<Integer> genreIdList, String originalLanguage, Date releaseDate, String posterPath, String voteAverage, String voteCount) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.video = video;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIdList = genreIdList;
        this.originalLanguage = originalLanguage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public boolean isVideo() {
        return video;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Integer> getGenreIdList() {
        return genreIdList;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseDateString() {
        if (null != releaseDate) {
            return DATE_FORMATTER.format(releaseDate);
        }
        else {
            return "";
        }
    }

    public Date parseReleaseDate(String releaseDate) throws ParseException {
        if (!TextUtils.isEmpty(releaseDate)) {
            return DATE_FORMATTER.parse(releaseDate);
        }
        else {
            return null;
        }
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        boolean[] booleanArray = new boolean[2];
        booleanArray[0] = video;
        booleanArray[1] = adult;

        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeBooleanArray(booleanArray);
        dest.writeString(backdropPath);
        //dest.writeList(genreIdList);
        dest.writeString(originalLanguage);
        dest.writeString(DATE_FORMATTER.format(releaseDate));
        dest.writeString(posterPath);
        dest.writeString(voteAverage);
        dest.writeString(voteCount);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        boolean[] booleanArray = new boolean[2];

        try {
            id = in.readLong();
            title = in.readString();
            originalTitle = in.readString();
            overview = in.readString();
            popularity = in.readDouble();
            in.readBooleanArray(booleanArray);
            video = booleanArray[0];
            adult = booleanArray[1];
            backdropPath = in.readString();
            //genreIdList = in.readList();
            originalLanguage = in.readString();
            releaseDate = DATE_FORMATTER.parse(in.readString());
            posterPath = in.readString();
            voteAverage = in.readString();
            voteCount = in.readString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

/*{
        "adult":false,
            "backdrop_path":"/t5KONotASgVKq4N19RyhIthWOPG.jpg",
            "genre_ids":[
        28,
                12,
                878,
                53
        ],
        "id":135397,
            "original_language":"en",
            "original_title":"Jurassic World",
            "overview":"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.",
            "release_date":"2015-06-12",
            "poster_path":"/hTKME3PUzdS3ezqK5BZcytXLCUl.jpg",
            "popularity":52.593315,
            "title":"Jurassic World",
            "video":false,
            "vote_average":7.0,
            "vote_count":2494
    }*/
