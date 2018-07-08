package com.example.srikkanth.news.Common;

import java.io.Serializable;

/**
 * Created by srikkanth on 31/5/18.
 */

public class HelperClass implements Serializable {
    private String source;
    private String authour;
    private String Headline;
    private String description;
    private String urlImage;
    private String publishedDate;
    private String url;

    public HelperClass(String source, String authour, String headline, String description, String urlImage, String publishedDate, String url) {
        this.source = source;
        this.authour = authour;
        Headline = headline;
        this.description = description;
        this.urlImage = urlImage;
        this.publishedDate = publishedDate;
        this.url = url;
    }
    public HelperClass(){

    }

    public String getSource() {
        return source;
    }

    public String getAuthour() {
        return authour;
    }

    public String getHeadline() {
        return Headline;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getUrl() {
        return url;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setAuthour(String authour) {
        this.authour = authour;
    }

    public void setHeadline(String headline) {
        Headline = headline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
