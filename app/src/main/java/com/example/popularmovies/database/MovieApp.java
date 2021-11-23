package com.example.popularmovies.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@androidx.room.Entity(tableName = "stored")
public class MovieApp {
    @PrimaryKey
    @NonNull

    @ColumnInfo(name = "images")
    private String image;
    @ColumnInfo(name = "texts")
    private String descriptions;
    private String backdrop;
    private String title;
    private String release;
    private String vote;
    private String id;
    private String rating;
    private String vids;
    @ColumnInfo(name = "favs")
    private String favs;
    @ColumnInfo(name = "poster_favs")
    private String posters;

    public MovieApp(String image, String descriptions, String backdrop, String title, String release,
                    String vote, String id, String rating, String vids) {
        this.image = image;
        this.descriptions = descriptions;
        this.backdrop = backdrop;
        this.title = title;
        this.release = release;
        this.vote = vote;
        this.id = id;
        this.rating = rating;
        this.vids = vids;
    }

    @NonNull
    public String getImage() {
        return image;
    }

    public void setImage(@NonNull String image) {
        this.image = image;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVids() {
        return vids;
    }

    public void setVids(String vids) {
        this.vids = vids;
    }

    public String getFavs() {
        return favs;
    }

    public void setFavs(String favs) { this.favs = favs; }

    public String getPosters() {
        return posters;
    }

    public void setPosters(String posters) {
        this.posters = posters;
    }
}



