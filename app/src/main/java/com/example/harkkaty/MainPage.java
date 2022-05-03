package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kotlin.collections.unsigned.UArraysKt;

public class MainPage<onStart> extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth auth;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationBarView;
    private ListView favoriteMoviesList;
    Movies movies = Movies.getInstance();
    private TheaterList theaterlist = TheaterList.getInstance();
    private String firstNode;
    ImageView star, movie;
    MovieListFrontPage movie_list;
    private FirebaseFirestore db;
    private FirebaseUser user;
    MustWatchList mustWatchList = MustWatchList.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        movie_list = MovieListFrontPage.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        navigationBarView = findViewById(R.id.navigationbarmenu);
        drawerLayout = findViewById(R.id.drawerlayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        favoriteMoviesList = findViewById(R.id.MarkFavoriteMovies);
        star = findViewById(R.id.imageViewStar);
        movie = findViewById(R.id.imageViewFrontPage);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView textView = findViewById(R.id.frontPageTextField);
        ImageView imageView = findViewById(R.id.imageViewFrontPage);
        Document document = readerXML("https://www.finnkino.fi/xml/Schedule/");
        ArrayList <MovieDetailsForMovieListFrontPage> list = createList(document);
        AdapterFrontPage adapter = new AdapterFrontPage(this, R.layout.testo,movie_list.getListOFMovies());
        favoriteMoviesList.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Map<String, ArrayList<String>> users_favouriteMovies = new HashMap<>();
        ArrayList<String> titles = new ArrayList<String>();
        // MustWatch alustus firestoresta
        String userID = auth.getCurrentUser().getUid();
        if(mustWatchList.getMust_watch_list().size() > 0) {
            //mustWatchList.clearList();
            DocumentReference docRef = db.collection("FavouriteMovies").document(userID);
            //mustWatchList.clearList();
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();

                    String str = String.valueOf(task.getResult().getData().values());
                    String str2 = str.substring(2,str.length()-2);
                    String[] array = str2.split(",");
                    String movieTitle;
                    for( String s : array){
                        movieTitle = s.trim();
                        //mustWatchList.createMustWatchList(movieTitle);
                    }
                }
            });



            /*
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    Map<String, Object> firemap = documentSnapshot.getData();
                    Object obj = firemap.get("movie");
                    ArrayList<MovieDetailsForMovieListFrontPage> list = (ArrayList<MovieDetailsForMovieListFrontPage>) obj;

                    for(int i = 0; i < list.size(); i++){
                        System.out.println(list.get(i).getTitle() + "     FIRE");
                    }

                    //for(MovieDetailsForMovieListFrontPage obj : movie.getMust_watch_list()){
                    //    System.out.println(obj.getTitle() + "  FIREST");
                    //}
                }
            });

             */
        }

        //TODO firebase tietokantaan lempileffat
        /*
        String userID = auth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("users").document(userID);
        Map<String, Object> user_hasMap = new HashMap<>();
        user_hasMap.put("favorite movies", movie_list.getListOFMovies().get(0).getTitle());
        */

        //Map<String, ArrayList<MovieDetailsForMovieListFrontPage>> users_favouriteMovies = new HashMap<>();

        /*
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "Success: New movies added to database ");
            }
        });
         */

        favoriteMoviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title, year, imageurl, genres;
                title = movie_list.getListOFMovies().get(i).getTitle();
                year = movie_list.getListOFMovies().get(i).getYear();
                imageurl = movie_list.getListOFMovies().get(i).getImageURL();
                genres = movie_list.getListOFMovies().get(i).getGenres();
                MovieDetailsForMovieListFrontPage moviedetails = new MovieDetailsForMovieListFrontPage(title, year, imageurl, genres);

                if (mustWatchList.checkMatch(title) == false){
                    mustWatchList.addToMustWatchList(moviedetails);
                    titles.add(title);

                    users_favouriteMovies.put("movie", titles);
                    db.collection("FavouriteMovies").document(userID).set(users_favouriteMovies);


                    //star.setColorFilter(0xffffff00);
                } else {
                    Toast.makeText(MainPage.this, "Movie is already in the list!", Toast.LENGTH_SHORT).show();
                }


                System.out.println(title);
                //Klikkauksella saa leffan titlen
            }
        });





        navigationBarView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = item.getItemId();
                if(index == R.id.LogOutItemFromNavTopBar){
                    Toast.makeText(MainPage.this, "Log out working", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    startActivity(new Intent( MainPage.this, MainLogin.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.Settings){
                    startActivity(new Intent(MainPage.this, SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return true;
            }
        });

        // Set home selected
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO ala navibaarin komennot--- uusiin activiteetteihin
                int index = item.getItemId();
                if(index == R.id.homepage){
                    return true;
                }
                if(index == R.id.moviespage){
                    startActivity(new Intent(MainPage.this, MainMovies.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.profiilipage){
                    startActivity(new Intent(MainPage.this, MainProfilli.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.newspage){
                    startActivity(new Intent(MainPage.this, NewsPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        //itemSelectedLister


    }

    public Document readerXML(String urlString) {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            return doc;
            //System.out.println("Root element: "+ doc.getDocumentElement().getNodeName());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList <MovieDetailsForMovieListFrontPage> createList(Document document){

        NodeList nList = document.getDocumentElement().getElementsByTagName("Show");
        ArrayList list = new ArrayList();
        ArrayList eventsList = new ArrayList();
        String eventsID;

        for(int i = 0; i < nList.getLength(); i++){

            Node node = nList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element element = (Element) node;
                eventsID = element.getElementsByTagName("EventID").item(0).getTextContent();
                if(eventsList.contains(eventsID) == true){
                    continue;
                }
                eventsList.add(eventsID);
                String t_name = element.getElementsByTagName("Title").item(0).getTextContent();
                String t_year = element.getElementsByTagName("ProductionYear").item(0).getTextContent();
                String imageURL = element.getElementsByTagName("EventSmallImagePortrait").item(0).getTextContent();
                String genre = element.getElementsByTagName("Genres").item(0).getTextContent();
                MovieDetailsForMovieListFrontPage movie = new MovieDetailsForMovieListFrontPage(t_name, t_year, imageURL, genre);
                movie_list.addMoviesToList(movie);
                //System.out.println(movie.getTitle());

                list.add(movie);
            }
        }
        return list;
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainPage.this, MainLogin.class));
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