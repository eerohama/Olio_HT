package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainMovies extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TheaterList theaterlist = TheaterList.getInstance();
    private String firstNode;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth auth;
    private NavigationView navigationBarView;


    AutoCompleteTextView autoCompleteTextView, autoComplete;
    ArrayAdapter<String> adapterItems1, adapterTimes2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_movies);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        auth = FirebaseAuth.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Document document = readerXML("https://www.finnkino.fi/xml/TheatreAreas/");
        drawerLayout= findViewById(R.id.drawerlayout);
        autoCompleteTextView = findViewById(R.id.MoviesDropDownMenu);
        navigationBarView = findViewById(R.id.navigationbarmenu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createList(document);
        createDropDownMenu();

        bottomNavigationView.setSelectedItemId(R.id.moviespage);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            /* bottomnavigation functionality

             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO ala navibaarin komennot--- uusiin activiteetteihin
                int index = item.getItemId();
                if(index == R.id.homepage){
                    startActivity(new Intent( MainMovies.this, MainPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.moviespage){
                    return true;
                }
                if(index == R.id.profiilipage){
                    startActivity(new Intent(MainMovies.this, MainProfilli.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.newspage){
                    startActivity(new Intent(MainMovies.this, NewsPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(MainMovies.this, SearchMovies.class);
                intent.putExtra("theater", item);
                startActivity(intent);

            }
        });

        navigationBarView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            /* navigation fuctionality

             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = item.getItemId();
                if(index == R.id.LogOutItemFromNavTopBar){
                    Toast.makeText(MainMovies.this, "Log out working", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    startActivity(new Intent( MainMovies.this, MainLogin.class));
                    return true;
                }
                if(index == R.id.Settings){
                    startActivity(new Intent(MainMovies.this, SettingsActivity.class));
                    return true;
                }
                return true;
            }
        });



    }


    public Document readerXML(String urlString) {
        /* XML file reader functionality

         */

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

    public void createDropDownMenu(){
        /* creating the dropdown menu for movie
        theater areas got from XML file
         */
        ArrayList<String> droplist = new ArrayList<String>();
        for(Theater t : theaterlist.getList()){
            droplist.add(t.getName().toString());
        }
        adapterItems1 = new ArrayAdapter<String>(this, R.layout.movies_list,droplist);
        autoCompleteTextView.setAdapter(adapterItems1);


    }
    public void createList(Document document){
        /* creating list from xml file nodes and details

         */
        NodeList nList = document.getDocumentElement().getElementsByTagName("TheatreArea");

        for(int i = 0; i < nList.getLength(); i++){
            Node node = nList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element element = (Element) node;
                String s_id = element.getElementsByTagName("ID").item(0).getTextContent();
                int t_id = Integer.parseInt(s_id);

                String t_name = element.getElementsByTagName("Name").item(0).getTextContent();

                Theater t = new Theater(t_name, t_id);
                theaterlist.addToList(t);
                if(i == 0){
                    firstNode = t_name;
                }
            }
        }
    }
    @Override
    protected void onStart(){

        /* Checking if the user id logged in
         */
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainMovies.this, MainLogin.class));
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