package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ShowNewsContent extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private WebView web;
    private NewsContent selectedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_content);
        Intent intent = getIntent();
        selectedObject = (NewsContent) intent.getSerializableExtra("object");

        web = (WebView) findViewById(R.id.webView);
        web.setWebViewClient(new WebViewClient());
        web.getSettings().setJavaScriptEnabled(true);
        String url = selectedObject.getArticleUrl();
        web.loadUrl(url);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.newspage);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int index = item.getItemId();
                if(index == R.id.homepage){
                    startActivity(new Intent( ShowNewsContent.this, MainPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.newspage){
                    startActivity(new Intent(ShowNewsContent.this, NewsPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.profiilipage){
                    startActivity(new Intent(ShowNewsContent.this, MainProfilli.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.moviespage){
                    startActivity(new Intent(ShowNewsContent.this, MainMovies.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

    }
}