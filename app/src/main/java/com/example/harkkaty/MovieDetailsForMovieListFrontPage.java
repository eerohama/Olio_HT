package com.example.harkkaty;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieDetailsForMovieListFrontPage {
    private String Title;
    private String Year;
    private String ID;
    private int imageStarColor;


    private String ImageURL;
    private String genres;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public MovieDetailsForMovieListFrontPage(String title, String year, String imageurl, String genres){
        this.Title = title;
        this.Year = year;
        this.ImageURL = imageurl;
        this.genres = genres;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
    public void setTitle(String Title){
        this.Title = Title;

    }
    public String getTitle(){
        return this.Title;
    }

    public void setYear(String year){
        this.Year = year;
    }

    public String getYear(){
        return this.Year;
    }

    public void setImageStarColor(int color){this.imageStarColor = color;}

    public int getImageStarColor(){return imageStarColor;}

}
