package com.example.harkkaty;

import java.util.ArrayList;

public class Movies {
    private ArrayList<MovieInfo> movieList;
    private ArrayList<String> showList;
    private String header;
    private static Movies instance = null;

    public Movies(){
        this.movieList = new ArrayList<MovieInfo>();
        this.showList = new ArrayList<String>();
    }

    public static Movies getInstance(){
        if(instance == null){
            instance = new Movies();
        }
        return instance;
    }

    public void addToList(String show){
        showList.add(show);
    }

    public ArrayList<String> getList(){
        return showList;
    }

    public void addToMovieList(MovieInfo movie){
        movieList.add(movie);
    }

    public ArrayList<MovieInfo> getMovieList(){
        return movieList;
    }

    public void clearList(){
        showList.clear();
    }

    public void setHeader(String s){
        this.header = s;
    }

    public String getHeader(){
        return header;
    }

    public MovieInfo findSelectedMovie(String title){

        for(MovieInfo movie: movieList){
            if(movie.getName().equals(title)){
                return movie;
            }
        }
        return null;
    }

}
