package com.example.pinkorange.vibrato;

import java.io.Serializable;

public class Song implements Serializable {
    int id;
    public String title, artist, lyrics;

    Song(int id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.lyrics = "No Lyrics Found.";
    }

    Song(int id, String title, String artist, String lyrics) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
    }

}
