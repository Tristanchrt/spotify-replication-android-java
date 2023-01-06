package com.example.oasis.models;

import java.util.Locale;

public class AudioFile {
    private String title;
    private String filePath;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private int duration;
    private String imagePath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDuration() {
        return duration;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDurationText() {
        int second = duration % 60;
        int durationMinute = (duration - second) / 60;
        int minute = durationMinute % 60;
        int hour = (durationMinute - minute) / 60;
        if (hour > 0)
            return String.format(Locale.getDefault(),
                    "%02d:%02d:%02d", hour, minute, second);
        return String.format(Locale.getDefault(),
                "%02d:%02d", minute, second);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
