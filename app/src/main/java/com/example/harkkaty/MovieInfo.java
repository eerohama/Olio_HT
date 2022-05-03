package com.example.harkkaty;

import java.util.ArrayList;

public class MovieInfo {

    private String name;
    private int lenght;
    private ArrayList<String> actors;
    private String ageRestriction;
    private String genre;
    private String synopsis;
    private String imageUrl;
    private String ratingImage;
    private String productionYear;


    public MovieInfo(String name, int lenght, String age, String genre, String synopsis, String imageUrl, String rating, String year){
        this.name = name;
        this.lenght = lenght;
        this.ageRestriction = age;
        this.genre = genre;
        this.synopsis = synopsis;
        this.imageUrl = imageUrl;
        this.ratingImage = rating;
        this.productionYear = year;
        actors = new ArrayList<>();
    }

    public void setName(String s){
        this.name = s;
    }
    public String getName(){
        return this.name;
    }
    public void setLength(int i){
        this.lenght = i;
    }
    public int getLenght(){
        return this.lenght;
    }
    public void setActors(String s){
        this.actors.add(s);
    }
    public ArrayList<String> getActors(){
        return this.actors;
    }
    public void setAgeRestriction(String s){
        this.ageRestriction = s;
    }
    public String getAgeRestriction(){
        return this.ageRestriction;
    }
    public void setGenre(String s){
        this.genre = s;
    }
    public String getGenre(){
        return this.genre;
    }
    public void setSynopsis(String s){
        this.synopsis = s;
    }
    public String getSynopsis(){
        return this.synopsis;
    }
    public void setImageUrl(String s){
        this.imageUrl = s;
    }
    public String getImageUrl(){
        return this.imageUrl;
    }
    public void setRatingImage(String s){
        this.ratingImage = s;
    }
    public String getRatingImage(){
        return this.ratingImage;
    }

    public String getProductionYear() {
        return productionYear;
    }
}
