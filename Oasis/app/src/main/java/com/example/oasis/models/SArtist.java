package com.example.oasis.models;

import java.util.List;

public class SArtist {
    private String name;
    private String url;
    private List<TrackImage> image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TrackImage> getImage() {
        return image;
    }

    public void setImage(List<TrackImage> image) {
        this.image = image;
    }
}
