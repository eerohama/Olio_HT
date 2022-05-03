package com.example.harkkaty;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImdbApi {

    private String apiKey;
    private String movieID;
    private String imdbRating;
    private String searchUrl;
    private String ratingUrl;

    public ImdbApi(){
        this.apiKey = "k_bl59we7o";
    }


    public String searchMovieAndGetRating(String searchExpression) throws JSONException {

        // Help taken from: https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java

        JSONReader jsonReader = new JSONReader();

        // Searching movie's imdb ID first in order to get the imdb rating
        searchUrl = "https://imdb-api.com/en/API/SearchMovie/"+apiKey+"/"+searchExpression;
        System.out.println(searchUrl);
        String json = jsonReader.getJSON(searchUrl);
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            JSONArray results_arr = obj.getJSONArray("results");
            this.movieID = results_arr.getJSONObject(0).getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ratingUrl = "https://imdb-api.com/en/API/Ratings/"+apiKey+"/"+movieID;
        json = jsonReader.getJSON(ratingUrl);

        try {
            obj = new JSONObject(json);
            this.imdbRating = obj.getString("imDb");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(imdbRating);
        return imdbRating;
    }
}
