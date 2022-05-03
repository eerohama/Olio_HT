package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.harkkaty.databinding.ActivityMainloginBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NewsPage extends AppCompatActivity {
    private ArrayList<NewsContent> newsList;
    private ImageView imageView;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        imageView = (ImageView) findViewById(R.id.newsImage);
        listView = (ListView) findViewById(R.id.newsListView);
        newsList = new ArrayList<NewsContent>();
        getNewsContent();

        ListAdapter listAdapter = new ListAdapter(NewsPage.this, newsList);
        listView.setAdapter(listAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(NewsPage.this, ShowNewsContent.class);
                NewsContent selectedItem = newsList.get(position);
                intent.putExtra("object", selectedItem);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.newspage);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO ala navibaarin komennot--- uusiin activiteetteihin
                int index = item.getItemId();
                if(index == R.id.homepage){
                    startActivity(new Intent( NewsPage.this, MainPage.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.newspage){
                    return true;
                }
                if(index == R.id.profiilipage){
                    startActivity(new Intent(NewsPage.this, MainProfilli.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(index == R.id.moviespage){
                    startActivity(new Intent(NewsPage.this, MainMovies.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


    }


    public void getNewsContent() {

        String urlString = "https://www.finnkino.fi/xml/News/";
        Document doc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            System.out.println("<<<<< XML tiedosto luettu >>>>>");
        }

        NodeList nList = doc.getDocumentElement().getElementsByTagName("NewsArticle");
        for (int j = 0; j < nList.getLength(); j++) {
            Node node = nList.item(j);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String imageUrl = element.getElementsByTagName("ImageURL").item(0).getTextContent();
                String articleUrl = element.getElementsByTagName("ArticleURL").item(0).getTextContent();
                NewsContent news = new NewsContent(title, imageUrl, articleUrl);
                newsList.add(news);

            }
        }
    }
}
