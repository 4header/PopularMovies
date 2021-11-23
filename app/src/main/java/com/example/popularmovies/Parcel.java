package com.example.popularmovies;

import android.os.Parcelable;

public class Parcel implements Parcelable {

    private String img, desc, bg, title, release, vote, id;

    public Parcel(String img, String desc, String bg, String title, String release, String vote, String id) {
        this.img = img;
        this.desc = desc;
        this.bg = bg;
        this.title = title;
        this.release = release;
        this.vote = vote;
        this.id = id;
    }

    protected Parcel(android.os.Parcel in) {
        String[] datastore = new String[7];
        in.readStringArray(datastore);
        this.img = datastore[0];
        this.desc = datastore[1];
        this.bg = datastore[2];
        this.title = datastore[3];
        this.release = datastore[4];
        this.vote = datastore[5];
        this.id = datastore[6];
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
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

    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
        @Override
        public Parcel createFromParcel(android.os.Parcel in) {
            return new Parcel(in);
        }

        @Override
        public Parcel[] newArray(int size) {
            return new Parcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.img,
                this.desc,
                this.bg,
                this.title,
                this.release,
                this.vote,
                this.id
                });
    }
}
