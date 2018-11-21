package com.example.pinkorange.vibrato;

public class Song {
    int songId;
    public String title;
    public String artist = "Unknown Artist";
    public String lyrics = "No Lyrics Found.";

    Song(int songId, String title) {
        this.songId = songId;
        this.title = title;
    }

    Song(int songId, String title, String artist) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
    }

    Song(int songId, String title, String artist, String lyrics) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
    }

}
