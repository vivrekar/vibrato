package com.example.pinkorange.vibrato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectMusic extends AppCompatActivity {
    private SongStore ss = new SongStore();
    private ArrayList<Song> songs = ss.getSongs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);

        ListView listView = findViewById(R.id.list_view);
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), LiveWithSettings.class);
                Song cur_song = songs.get(i);
                intent.putExtra("id", cur_song.songId);
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.song_item_view, null);
            Song curSong = songs.get(i);
            TextView title = view.findViewById(R.id.song_title);
            title.setText(curSong.title);
            TextView artist = view.findViewById(R.id.song_artist);
            artist.setText(curSong.artist);
            return view;
        }
    }
}
