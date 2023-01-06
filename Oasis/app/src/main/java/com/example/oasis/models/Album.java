package com.example.oasis.models;

import java.util.List;

public class Album {
    private String artist;
    private String title;
    private String url;
    private List<TrackImage> image;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
