package com.nortonlam.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by norton
 *
 * Created date: 10/8/15.
 */
public class TmdbConfiguration {
    Images images;
    @SerializedName("change_keys")
    List<String> changeKeys;

    public Images getImages() {
        return images;
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

        public List<String> getPosterSizes() {
            return posterSizes;
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