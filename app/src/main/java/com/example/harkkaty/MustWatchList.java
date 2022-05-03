package com.example.harkkaty;

import android.graphics.Movie;

import java.util.ArrayList;

public class MustWatchList {
    public ArrayList<MovieDetailsForMovieListFrontPage> must_watch_Arraylist;
    public static MustWatchList mustWatchList = new MustWatchList();

    public MustWatchList(){
        must_watch_Arraylist = new ArrayList<MovieDetailsForMovieListFrontPage>();
    }

    public static MustWatchList getInstance(){return mustWatchList;}

    public void addToMustWatchList(MovieDetailsForMovieListFrontPage movieDetailsForMovieListFrontPage){
        must_watch_Arraylist.add(movieDetailsForMovieListFrontPage);
    }

    public ArrayList<MovieDetailsForMovieListFrontPage> getMust_watch_list() {
        return must_watch_Arraylist;
    }

    public void createMustWatchList(String title){
        MovieListFrontPage list = MovieListFrontPage.getInstance();

        for(MovieDetailsForMovieListFrontPage obj : list.getListOFMovies()){
            if(obj.getTitle().equals(title)){
                MovieDetailsForMovieListFrontPage moviedetails = new MovieDetailsForMovieListFrontPage(obj.getTitle(), obj.getYear(), obj.getImageURL(), obj.getGenres());
                must_watch_Arraylist.add(moviedetails);
            }
        }
    }


    public void clearList(){
        must_watch_Arraylist.clear();
    }

    public boolean checkMatch(String title){
        for(MovieDetailsForMovieListFrontPage object : must_watch_Arraylist){
            if(object.getTitle().equals(title)){
                return true;
            }
        }
        return false;
    }
}
