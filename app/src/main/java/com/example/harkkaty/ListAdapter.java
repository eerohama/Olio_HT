package com.example.harkkaty;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<NewsContent> {

    public ListAdapter(Context context, ArrayList<NewsContent> newsList){
        super(context, R.layout.news_list_item, newsList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewsContent content = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,parent,false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.newsImage);
        TextView textView = (TextView) convertView.findViewById(R.id.newsTitle);

        Glide.with(getContext()).load(content.getImageUrl()).placeholder(R.drawable.ic_baseline_image_24).into(imageView);
        textView.setText(content.getTitle());
        return convertView;
    }
}
