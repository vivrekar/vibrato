package com.example.pinkorange.vibrato;

import java.io.Serializable;

public class Song implements Serializable {
    int id;
    public String title;
    public String artist = "Unknown Artist";
    public String lyrics = "No Lyrics Found.";

    Song(int id, String title) {
        this.id = id;
        this.title = title;
    }

    Song(int id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    Song(int id, String title, String artist, String lyrics) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
    }

}
