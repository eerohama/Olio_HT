package com.example.harkkaty;

import java.io.Serializable;

public class NewsContent implements Serializable {
    private String title;
    private String imageUrl;
    private String articleUrl;

    public NewsContent(String title, String image, String article){
        this.title = title;
        this.imageUrl = image;
        this.articleUrl = article;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
