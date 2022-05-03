package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class MainProfilli extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth auth;
    private  FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationBarView;
    private TextView email,userName,city;
    private FirebaseFirestore db;
    private ListView mustwatchlistView;
    private MovieListFrontPage movieListFrontPage;
    MustWatchList mustWatchList = MustWatchList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profilli);

        movieListFrontPage = MovieListFrontPage.getInstance();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userID = auth.getCurrentUser().getUid();
        user = auth.getCurrentUser();

        navigationBarView = findViewById(R.id.navigationbarmenu);
        drawerLayout = findViewById(R.id.drawerlayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainProfilli.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomNavigationView.setSelectedItemId(R.id.profiilipage);
        email = findViewById(R.id.sPosti);
        mustwatchlistView = findViewById(R.id.profileListView);

        email = findViewById(R.id.sPosti);
        userName = findViewById(R.id.nameDetails);
        city = findViewById(R.id.cityDetails);


        //Gets email, name, city details from database
        //email.setText(auth.getCurrentUser().getEmail().toString());

        //System.out.println(auth.getCurrentUser().getEmail().toString());

        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //TODO vaiheessa, ota olio lista firebasesta


                AdapterFrontPage adapterFrontPage = new AdapterFrontPage(MainProfilli.this,R.layout.testo, mustWatchList.getMust_watch_list());
                mustwatchlistView.setAdapter(adapterFrontPage);


                userName.setText(documentSnapshot.getString("name"));

                email.setText(user.getEmail());
                city.setText(documentSnapshot.getString("city"));

            }
        });
        navigationBarView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = item.getItemId();
                if(index == R.id.LogOutItemFromNavTopBar){
                    Toast.makeText(MainProfilli.this, "Log out working", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    startActivity(new Intent( MainProfilli.this, MainLogin.class));
                    return true;
                }
                if(index == R.id.Settings){
                    startActivity(new Intent(MainProfilli.this, SettingsActivity.class));
                    return true;
                }
                return true;
            }
        });

        // Set home selected
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int index = item.getItemId();
                if(index == R.id.homepage){
                    startActivity(new Intent( MainProfilli.this, MainPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.moviespage){
                    startActivity(new Intent(MainProfilli.this, MainMovies.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.profiilipage){
                    return true;
                }
                if(index == R.id.newspage){
                    startActivity(new Intent(MainProfilli.this, NewsPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


        mustwatchlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(MainProfilli.this, "Clicked favorite list", Toast.LENGTH_SHORT).show();
            }
        });


        //itemSelectedLister
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainProfilli.this, MainLogin.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}