package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;


public class MovieInfoPage extends AppCompatActivity {
    private ImageView imageView;
    private TextView txtTitle;
    private TextView txtMovieInfo;
    private TextView txtDuration;
    private ImageView imageRating;
    private Movies movieList;
    private TextView txtRating;
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info_page);
        imageView = findViewById(R.id.imageTest);
        txtTitle = findViewById(R.id.movieName);
        txtMovieInfo = findViewById(R.id.movieInfo);
        txtDuration = findViewById(R.id.movieDuration);
        txtRating = findViewById(R.id.movieRating);
        imageRating = findViewById(R.id.imageRating);
        movieList = Movies.getInstance();
        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("movieName");
        MovieInfo movieObject = movieList.findSelectedMovie(movieTitle);
        String imdb = "";
        try {
            imdb = getImdbRating(movieObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(imdb.length() > 0){
            txtRating.setText("IMDb: "+imdb);
        } else {
            txtRating.setText("IMDb: not found");
        }
        // Loading images to the ImageViews
        Glide.with(getApplicationContext()).load(movieObject.getImageUrl()).placeholder(R.drawable.ic_baseline_image_24).into(imageView);
        Glide.with(getApplicationContext()).load(movieObject.getRatingImage()).placeholder(R.drawable.ic_baseline_image_24).into(imageRating);
        txtTitle.setText(movieObject.getName());
        txtDuration.setText(movieObject.getLenght() + " Minuuttia");
        txtMovieInfo.setText(movieObject.getSynopsis());

        String actors = "";
        if(movieObject.getActors().size() != 0){
            for(String s: movieObject.getActors()){
                actors += s + ", ";
            }
            txtMovieInfo.append("\n\n" + "Näyttelijät: "+ actors.substring(0, actors.length()-2));
        }


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.moviespage);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int index = item.getItemId();
                if(index == R.id.homepage){
                    startActivity(new Intent( MovieInfoPage.this, MainPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.newspage){
                    startActivity(new Intent(MovieInfoPage.this, NewsPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.profiilipage){
                    startActivity(new Intent(MovieInfoPage.this, MainProfilli.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.moviespage){
                    startActivity(new Intent(MovieInfoPage.this, MainMovies.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }

    public String getImdbRating(@NonNull MovieInfo movieObject) throws JSONException {
        String searchExpression = movieObject.getName() + " " + movieObject.getProductionYear();
        System.out.println(searchExpression);
        ImdbApi api = new ImdbApi();
        String rating = api.searchMovieAndGetRating(searchExpression);
        return rating;

    }
}