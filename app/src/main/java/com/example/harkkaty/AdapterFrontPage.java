package com.example.harkkaty;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterFrontPage extends ArrayAdapter<MovieDetailsForMovieListFrontPage> {
    private Context context;
    private int resource;
    private ArrayList <MovieDetailsForMovieListFrontPage> list;
    private TextView MovieHeader, MovieYear, MovieGenre;
    private ImageView picture, star;


    public AdapterFrontPage(@NonNull Context context, int resource, @NonNull ArrayList<MovieDetailsForMovieListFrontPage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /* Getting the specific details from layout to put them on to movie title, picture, year and genre

         */

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        MovieHeader = convertView.findViewById(R.id.frontPageTextField);
        picture = convertView.findViewById(R.id.imageViewForMovie);
        MovieYear = convertView.findViewById(R.id.textViewForYear);
        MovieGenre = convertView.findViewById(R.id.textViewForGenre);


        Glide.with(context).load(getItem(position).getImageURL()).placeholder(R.drawable.ic_baseline_image_24).into(picture);
        MovieHeader.setText(getItem(position).getTitle());
        MovieYear.setText("Year: " + getItem(position).getYear());
        MovieGenre.setText(getItem(position).getGenres());
        return convertView;

    }
}

