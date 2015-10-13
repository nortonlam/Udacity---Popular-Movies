package com.nortonlam.popularmovies.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.nortonlam.popularmovies.R;
import com.nortonlam.popularmovies.net.TheMovieDb;
import com.nortonlam.popularmovies.net.TheMovieDbApi;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by norton on 10/8/15.
 */
public class TmdbConfiguration {
    private static TmdbConfiguration _config;

    Images images;
    @SerializedName("change_keys")
    List<String> changeKeys;

    public static void init(Context context) {
    }

    public static TmdbConfiguration getInstance() {
        if (null == _config) {
            throw new IllegalStateException("Configurtation not initialized.  Please call init() first.");
        }

        return _config;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
    }

    public class Images {
        public static final int PREFERRED_POSTER_SIZE = 4;

        @SerializedName("base_url")
        String baseUrl;
        @SerializedName("secure_base_url")
        String secureBaseUrl;
        @SerializedName("backdrop_sizes")
        List<String> backdropSizes;
        @SerializedName("logo_sizes")
        List<String> logoSizes;
        @SerializedName("poster_sizes")
        List<String> posterSizes;
        @SerializedName("profile_sizes")
        List<String> profileSizes;
        @SerializedName("still_sizes")
        List<String> stillSizes;

        public String getFullBaseUrl() {
            return baseUrl + getPosterSizes().get(PREFERRED_POSTER_SIZE);
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getSecureBaseUrl() {
            return secureBaseUrl;
        }

        public void setSecureBaseUrl(String secureBaseUrl) {
            this.secureBaseUrl = secureBaseUrl;
        }

        public List<String> getBackdropSizes() {
            return backdropSizes;
        }

        public void setBackdropSizes(List<String> backdropSizes) {
            this.backdropSizes = backdropSizes;
        }

        public List<String> getLogoSizes() {
            return logoSizes;
        }

        public void setLogoSizes(List<String> logoSizes) {
            this.logoSizes = logoSizes;
        }

        public List<String> getPosterSizes() {
            return posterSizes;
        }

        public void setPosterSizes(List<String> posterSizes) {
            this.posterSizes = posterSizes;
        }

        public List<String> getProfileSizes() {
            return profileSizes;
        }

        public void setProfileSizes(List<String> profileSizes) {
            this.profileSizes = profileSizes;
        }

        public List<String> getStillSizes() {
            return stillSizes;
        }

        public void setStillSizes(List<String> stillSizes) {
            this.stillSizes = stillSizes;
        }
    }
}

/*
{
   "images":{
      "base_url":"http://image.tmdb.org/t/p/",
      "secure_base_url":"https://image.tmdb.org/t/p/",
      "backdrop_sizes":[
         "w300",
         "w780",
         "w1280",
         "original"
      ],
      "logo_sizes":[
         "w45",
         "w92",
         "w154",
         "w185",
         "w300",
         "w500",
         "original"
      ],
      "poster_sizes":[
         "w92",
         "w154",
         "w185",
         "w342",
         "w500",
         "w780",
         "original"
      ],
      "profile_sizes":[
         "w45",
         "w185",
         "h632",
         "original"
      ],
      "still_sizes":[
         "w92",
         "w185",
         "w300",
         "original"
      ]
   },
   "change_keys":[
      "adult",
      "air_date",
      "also_known_as",
      "alternative_titles",
      "biography",
      "birthday",
      "budget",
      "cast",
      "certifications",
      "character_names",
      "created_by",
      "crew",
      "deathday",
      "episode",
      "episode_number",
      "episode_run_time",
      "freebase_id",
      "freebase_mid",
      "general",
      "genres",
      "guest_stars",
      "homepage",
      "images",
      "imdb_id",
      "languages",
      "name",
      "network",
      "origin_country",
      "original_name",
      "original_title",
      "overview",
      "parts",
      "place_of_birth",
      "plot_keywords",
      "production_code",
      "production_companies",
      "production_countries",
      "releases",
      "revenue",
      "runtime",
      "season",
      "season_number",
      "season_regular",
      "spoken_languages",
      "status",
      "tagline",
      "title",
      "translations",
      "tvdb_id",
      "tvrage_id",
      "type",
      "video",
      "videos"
   ]
}
 */