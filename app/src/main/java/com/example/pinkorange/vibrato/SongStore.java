package com.example.pinkorange.vibrato;

import java.util.ArrayList;

class SongStore {
    private ArrayList<Song> songs;

    SongStore() {
        this.songs = new ArrayList<>();
        this.songs.add(new Song(
                R.raw.croatian,
                "Croatian Rhapsody",
                "Maksim Mrvica"
        ));
        this.songs.add(new Song(
                R.raw.highscore,
                "High Score",
                "Panda Eyes"
        ));
        this.songs.add(new Song(
                R.raw.unity,
                "Unity",
                "The Fat Rat"
        ));
        this.songs.add(new Song(
                R.raw.christmas,
                "All I Want For Christmas Is You",
                "Mariah Carey",
                "I don\'t want a lot for Christmas\nThere is just one thing I need\nI don\'t care about the presents\nUnderneath the Christmas tree\nI just want you for my own\nMore than you could ever know\nMake my wish come true\nAll I want for Christmas\nIs you\n\nI don't want a lot for Christmas\nThere is just one thing I need\nAnd I don't care about the presents underneath the Christmas tree.\n\nI don\'t need to hang my stocking\nThere upon the fireplace\nSanta Claus won\'t make me happy\nWith a toy on Christmas Day\n\nI just want you for my own\nMore than you could ever know\nMake my wish come true\nAll I want for Christmas\nIs you\nYou, baby\n\nOh, I won\'t ask for much this Christmas\nI won\'t even wish for snow\nAnd I\'m just gonna keep on waiting\nUnderneath the mistletoe\n\nI won\'t make a list and send it\nTo the North Pole for Saint Nick\nI won\'t even stay awake to\nHear those magic reindeer click\n\n\'Cause I just want you here tonight\nHolding on to me so tight\nWhat more can I do?\nCause baby all I want for Christmas is you\nYou\n\nOh-ho, all…\n"
        ));
        this.songs.add(new Song(
                R.raw.grilboyfriend,
                "Girlfriend/Boyfriend",
                "Blackstreet"
        ));
        this.songs.add(new Song(
                R.raw.forever,
                "Forever",
                "Chris Brown"
        ));
        this.songs.add(new Song(
                R.raw.alreadygone,
                "Already Gone",
                "Kelly Clarkson"
        ));
        this.songs.add(new Song(
                R.raw.doesntmatter,
                "Doesn't Really Matter",
                "Janet Jackson"
        ));
        this.songs.add(new Song(
                R.raw.walkaway,
                "Walk Away",
                "Kelly Clarkson"
        ));
        this.songs.add(new Song(
                R.raw.enen,
                "Eh, Eh",
                "Lady GaGa"
        ));
    }

    ArrayList<Song> getSongs() {
        return this.songs;
    }

}
