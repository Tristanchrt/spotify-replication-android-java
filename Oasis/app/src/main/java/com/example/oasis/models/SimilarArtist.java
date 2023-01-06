package com.example.oasis.models;

import java.util.List;

public class SimilarArtist
{
    private String name;
    private String url;
    private List<TrackImage> image;
    private String streamable;
    private String ontour;
    private ArtistStats stats;
    private Similar similar;
    private TopTag tags;
    private Bio bio;

    public Bio getBio() {
        return bio;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }

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

    public String getStreamable() {
        return streamable;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

    public String getOntour() {
        return ontour;
    }

    public void setOntour(String ontour) {
        this.ontour = ontour;
    }

    public ArtistStats getStats() {
        return stats;
    }

    public void setStats(ArtistStats stats) {
        this.stats = stats;
    }

    public Similar getSimilar() {
        return similar;
    }

    public void setSimilar(Similar similar) {
        this.similar = similar;
    }

    public TopTag getTags() {
        return tags;
    }

    public void setTags(TopTag tags) {
        this.tags = tags;
    }
}
