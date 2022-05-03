package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ShowView extends AppCompatActivity {
    private ListView lv;
    private TextView tw;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showview);
        lv = (ListView) findViewById(R.id.listView);
        tw = (TextView) findViewById(R.id.textView);
        Movies mList = Movies.getInstance();
        tw.setText(mList.getHeader());
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList.getList());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String clickedItem = mList.getList().get(position);
                String[] tempStrings = clickedItem.split(" ", 10);
                String parsedString = "";
                for(int i = 0; i < tempStrings.length-1; i++){
                    parsedString += tempStrings[i] + " ";
                }
                String finalStr = parsedString.trim();
                loadMovieInfo(finalStr);
                // Opens movie info activity of the selected movie
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.moviespage);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int index = item.getItemId();
                if(index == R.id.homepage){
                    startActivity(new Intent( ShowView.this, MainPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.newspage){
                    startActivity(new Intent(ShowView.this, NewsPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.profiilipage){
                    startActivity(new Intent(ShowView.this, MainProfilli.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.moviespage){
                    startActivity(new Intent(ShowView.this, MainMovies.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }

    public void loadMovieInfo(String item){
        Intent intent = new Intent(this, MovieInfoPage.class);
        intent.putExtra("movieName", item);
        startActivity(intent);
    }

}