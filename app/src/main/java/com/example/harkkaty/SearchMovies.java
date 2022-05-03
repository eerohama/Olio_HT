package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SearchMovies extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Context context;
    private EditText editPvm, editStartTime, editFinishTime, editMoviename, editActorName;
    private String theaterName;
    private String firstNode;
    private Spinner spinner;
    private HashMap<String, Integer> ageValues;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        context = SearchMovies.this;
        spinner = (Spinner) findViewById(R.id.spinnerAgeLimit);
        editPvm = (EditText) findViewById(R.id.editPvm);
        editStartTime = (EditText) findViewById(R.id.editStart_time);
        editFinishTime = (EditText) findViewById(R.id.editFinishTime);
        editMoviename = (EditText) findViewById(R.id.editMoviename);
        editActorName = (EditText) findViewById(R.id.editActorName);
        ageValues = new HashMap<String, Integer>();
        Intent intent = getIntent();
        theaterName = intent.getStringExtra("theater");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Document doc = readXML("https://www.finnkino.fi/xml/TheatreAreas/");
        createList(doc);
        dropdownMenu();
        getAllMoviesInformation();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date today = new Date(System.currentTimeMillis());
        String date_today = dateFormat.format(today);
        editPvm.setHint(date_today);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.moviespage);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int index = item.getItemId();
                if(index == R.id.homepage){
                    startActivity(new Intent( SearchMovies.this, MainPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.newspage){
                    startActivity(new Intent( SearchMovies.this, NewsPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.profiilipage){
                    startActivity(new Intent(SearchMovies.this, MainProfilli.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.moviespage){
                    startActivity(new Intent(SearchMovies.this, MainMovies.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

    }

    public void dropdownMenu(){

        String[] spinnerList = {"Ikäraja", "S", "K7", "K12", "K16", "K18"};
        ageValues.put("S",0);
        ageValues.put("K7",7);
        ageValues.put("K12",12);
        ageValues.put("K16",16);
        ageValues.put("K18",18);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(adapter);

    }

    public Document readXML(String urlString) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            return doc;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            System.out.println("<<<<< XML tiedosto luettu >>>>>");
        }
        return null;
    }


    public void createList(Document doc){

        NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");

        for(int i = 0; i < nList.getLength(); i++){
            Node node = nList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element element = (Element) node;
                String t_name = element.getElementsByTagName("Name").item(0).getTextContent();

                if(i == 0){
                    firstNode = t_name;
                }
            }
        }
    }

    // Creates url and gathers information from finnkinos XML site
    public void makeUrl(View view) {
        TheaterList tList = TheaterList.getInstance();
        Movies mList = Movies.getInstance();
        mList.clearList();

        // Setting formats for handling dates
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm"); //dd.MM.yyyy
        Date date_start = null;
        Date date_end = null;
        Calendar cal1 = null;
        Calendar cal2 = null;
        Boolean findByName = false;
        Boolean actorSearch = false;
        Boolean actorFound;

        // If user has typed text to actor field, "actor search" gets enabled
        if(editActorName.getText().toString().isEmpty()){
            actorFound = true;
        } else {
            actorSearch = true;
            actorFound = false;
        }

        String pvm;
        if(editPvm.getText().toString().isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date today = new Date(System.currentTimeMillis());
            pvm = dateFormat.format(today);
        } else {
            pvm = editPvm.getText().toString();
        }

        String selectedActor = editActorName.getText().toString();
        String alku = editStartTime.getText().toString();
        String loppu = editFinishTime.getText().toString();
        String selectedAgeLimit = spinner.getSelectedItem().toString();
        String movieName = editMoviename.getText().toString();
        mList.setHeader(theaterName + "  " + pvm);
        int theater_id = tList.getID(theaterName);

        if(movieName.length() > 0){
            findByName = true;
            mList.setHeader(movieName);
        }

        if(selectedAgeLimit.equals("Ikäraja")){
            selectedAgeLimit = "K18";
        }

        // testCase:n arvo 0, kun alku ja loppu tekstikentät ovat molemmat tyhjiä, 1 kun vain alku sisältää ajan,
        // 2 kun vain loppu sisältää ajan ja 3 kun molemmat kentät sisältävät ajan.
        int testCase = 0;
        if(alku.length() > 0){
            cal1 = transformTime(alku);
            testCase = 1;
        }
        if(loppu.length() > 0){
            cal2 = transformTime(loppu);
            testCase = 2;
        }
        if(alku.length() > 0 && loppu.length() > 0){
            testCase = 3;
        }


        String[] mainAreas = {"Pääkaupunkiseutu","Jyväskylä: FANTASIA","Kuopio: SCALA","Lahti: KUVAPALATSI","Lappeenranta: STRAND","Oulu: PLAZA","Pori: PROMENADI","Tampere","Turku: KINOPALATSI"};
        String url;
        String temp = theaterName;

        for(int i = 0; i < mainAreas.length; i++){
            if(firstNode.equals(temp)){
                theaterName = mainAreas[i];
                theater_id = tList.getID(theaterName);
            }

            url = "https://www.finnkino.fi/xml/Schedule/?area="+theater_id+"&dt="+pvm;
            Document doc = readXML(url);

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            for(int j = 0; j < nList.getLength(); j++){
                Node node = nList.item(j);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String name = element.getElementsByTagName("Title").item(0).getTextContent();
                    String showStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    String showEnd = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();
                    String ageRestriction = element.getElementsByTagName("Rating").item(0).getTextContent();
                    int ageLimit;
                    System.out.println(ageRestriction +" "+name);

                    // Setting age limit value from data collected from XML file
                    if(ageRestriction.equals("S")){
                        ageLimit = 0;
                    } else if(ageRestriction.contains("Anniskelu")) {
                        ageLimit = 18;
                    } else {
                        ageLimit = Integer.parseInt(ageRestriction);
                    }

                    MovieInfo movieObject = mList.findSelectedMovie(name);

                    try {
                        date_start = format.parse(showStart);
                        date_end = format.parse(showEnd);
                    } catch (ParseException e){
                        Log.e("ParseException", "Virhe päivämäärän käsittelyssä.");
                    }
                    String startTime = newFormat.format(date_start);
                    String endTime = newFormat.format(date_end);

                    // Checking if the movie has the actor who is selected by the user
                    if(actorSearch) {
                        for (String s : movieObject.getActors()) {
                            if (s.equals(selectedActor)) {
                                actorFound = true;
                            }
                        }
                    }

                    // The main algorithm that combines all the search criterions and adds all the movies that meet the criterion and adds them to a "show list"
                    if(actorFound) {
                        switch (testCase) {
                            case 0:
                                if (ageValues.get(selectedAgeLimit) >= ageLimit) {
                                    if (findByName == true) {
                                        if (movieName.equals(name)) {
                                            mList.addToList(theaterName + "   " + startTime + "-" + endTime);
                                        }
                                    } else {
                                        mList.addToList(name+ "   " + startTime + "-" + endTime);
                                    }
                                    break;
                                }
                            case 1:
                                if (ageValues.get(selectedAgeLimit) >= ageLimit) {
                                    if (transformTime(startTime).after(cal1)) {
                                        if (findByName == true) {
                                            if (movieName.equals(name)) {
                                                mList.addToList(theaterName + "   " + startTime + "-" + endTime);
                                            }
                                        } else {
                                            mList.addToList(name +"   " + startTime + "-" + endTime);
                                        }
                                    }
                                }
                                break;
                            case 2:
                                if (ageValues.get(selectedAgeLimit) >= ageLimit) {
                                    if (transformTime(endTime).before(cal2)) {
                                        if (findByName == true) {
                                            if (movieName.equals(name)) {
                                                mList.addToList(theaterName + "   " + startTime + "-" + endTime);
                                            }
                                        } else {
                                            mList.addToList(name +"   " + startTime + "-" + endTime);
                                        }
                                    }
                                }
                                break;
                            case 3:
                                if (ageValues.get(selectedAgeLimit) >= ageLimit) {
                                    if (transformTime(startTime).after(cal1) && transformTime(endTime).before(cal2)) {
                                        if (findByName == true) {
                                            if (movieName.equals(name)) {
                                                mList.addToList(theaterName + "   " + startTime + "-" + endTime);
                                            }
                                        } else {
                                            mList.addToList(name +"   " + startTime + "-" + endTime);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                if(actorSearch){
                    actorFound = false;
                }
                }
            }
            if(firstNode.equals(temp) == false){
                break;
            }
        }
        // Open ListView activity to display the wanted movies
        openListView();
    }

    // Going throught all finnkino movies and adding information of them to a list
    public void getAllMoviesInformation(){
        Movies movies = Movies.getInstance();

        String url = "https://www.finnkino.fi/xml/Events/";
        Document doc = readXML(url);
        NodeList nList = doc.getDocumentElement().getElementsByTagName("Event");

        for(int i = 0; i < nList.getLength(); i++){
            Node node = nList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element element = (Element) node;
                String name = element.getElementsByTagName("Title").item(0).getTextContent();
                String duration = element.getElementsByTagName("LengthInMinutes").item(0).getTextContent();
                int lenght = Integer.parseInt(duration);
                String ageRestriction = element.getElementsByTagName("Rating").item(0).getTextContent();
                String genres = element.getElementsByTagName("Genres").item(0).getTextContent();
                String synopsis = element.getElementsByTagName("Synopsis").item(0).getTextContent();
                String ageImage = element.getElementsByTagName("RatingImageUrl").item(0).getTextContent();
                String year = element.getElementsByTagName("ProductionYear").item(0).getTextContent();

                // Creating new nodelist to catch correct image
                NodeList imageList = element.getElementsByTagName("Images");
                Node imageNode = imageList.item(0);
                Element imageElement = (Element) imageNode;
                String imageUrl = "";

                if(imageElement.getElementsByTagName("EventMediumImagePortrait").item(0) != null){
                    imageUrl = imageElement.getElementsByTagName("EventMediumImagePortrait").item(0).getTextContent();
                } else if (imageElement.getElementsByTagName("EventSmallImageLandscape").item(0) != null){
                    imageUrl = imageElement.getElementsByTagName("EventSmallImageLandscape").item(0).getTextContent();
                }


                MovieInfo info = new MovieInfo(name, lenght, ageRestriction, genres, synopsis, imageUrl, ageImage, year);


                // Creating new nodelist to catch all the actors
                NodeList castList = element.getElementsByTagName("Cast");
                Node node1 = castList.item(0);
                Element element2 = (Element) node1;
                castList = element2.getElementsByTagName("Actor");

                for(int k = 0; k < castList.getLength(); k++) {
                    node1 = castList.item(k);
                    if (node1 != null) {
                        element2 = (Element) node1;
                        String first = element2.getElementsByTagName("FirstName").item(0).getTextContent();
                        String last = element2.getElementsByTagName("LastName").item(0).getTextContent();
                        info.setActors(first + " " + last);
                    }
                }
                movies.addToMovieList(info);
            }
        }
    }


    // transforms dates to Calendar form to ease comparison between dates
    public Calendar transformTime(String time){
        // Source: https://stackoverflow.com/questions/10086053/comparing-hours-in-java

        String[] parts = time.split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        return cal;
    }


    public void openListView(){
        Intent intent = new Intent(SearchMovies.this, ShowView.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}