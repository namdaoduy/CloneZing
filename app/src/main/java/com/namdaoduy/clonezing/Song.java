package com.namdaoduy.clonezing;

public class Song {
    private String title;
    private int file;
    private String artist;

    public Song(String title, String artist, int file) {
        this.title = title;
        this.file = file;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public int getFile() {
        return file;
    }

    public String getArtist() {
        return artist;
    }
}
