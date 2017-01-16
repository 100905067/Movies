package com.example.amrit.movies.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amrita on 1/11/2017.
 */

//use of Parcelable over Serializable : parcelable is faster
public class Movie implements Parcelable {

    private int id;
    private String title;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private double popularity;
    private float vote_average;
    private String video_path = null;

    public Movie(int id, String title, String overview, String poster_path,
                 String backdrop_path, double popularity, float vote_average) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_average = vote_average;
    }


    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = in.readDouble();
        this.vote_average = in.readFloat();
        this.video_path = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeDouble(popularity);
        parcel.writeFloat(vote_average);
        parcel.writeString(video_path);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getVideo_path() {
        return video_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }
}
