package com.example.harkkaty;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MovieListFrontPage {

    public static MovieListFrontPage movielist = new MovieListFrontPage();
    public ArrayList <MovieDetailsForMovieListFrontPage> ListOFMovies;

    public MovieListFrontPage(){
        this.ListOFMovies = new ArrayList<MovieDetailsForMovieListFrontPage>();
    }




    public static MovieListFrontPage getInstance(){
        return movielist;
    }

    public ArrayList<MovieDetailsForMovieListFrontPage> getListOFMovies() {
        return ListOFMovies;
    }

    public void addMoviesToList(MovieDetailsForMovieListFrontPage movie){
        ListOFMovies.add(movie);
    }

    //public ListO


}
