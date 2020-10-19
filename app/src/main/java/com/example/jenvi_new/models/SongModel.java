package com.example.jenvi_new.models;

import android.graphics.Bitmap;

public class SongModel {
    private String id;
    private String album;
    private String title;
    private String duration;
    private String artist;
    private String path;
    private Bitmap Albumart;
    private String FormattedDuration;


    //region Getters
    public String getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getAlbumart() { return Albumart; }

    public String getFormattedDuration() {
        return FormattedDuration;
    }

    //endregion


    //region Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setAlbum(String album) { this.album = album; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        this.FormattedDuration = formattedTime(Integer.parseInt(this.duration));
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setPath(String path) {
        this.path = path;
    }
    //endregion

    private String formattedTime(int currentPos) {
        String total1;
        String total2;
        currentPos /= 1000;
        String seconds = String.valueOf(currentPos % 60);
        String minutes = String.valueOf(currentPos / 60);
        total1 = minutes + ":" + "0" + seconds;
        total2 = minutes + ":" + seconds;
        if (seconds.length() == 1)
            return total1;
        else
            return total2;
    }

    // TODO: Remove after albumart
    public void setAlbumart(Bitmap albumart) {
        Albumart = albumart;
    }
}

