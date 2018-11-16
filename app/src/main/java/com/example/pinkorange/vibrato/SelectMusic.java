package com.example.pinkorange.vibrato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectMusic extends AppCompatActivity {
    private ArrayList<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeSongList();
        setContentView(R.layout.activity_select_music);

        ListView listView = findViewById(R.id.list_view);
        MyAdaptor myAdaptor = new MyAdaptor();
        listView.setAdapter(myAdaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), LiveWithSettings.class);
                Song cur_song = songList.get(i);
                intent.putExtra("id", cur_song.songId);
                startActivity(intent);
            }
        });
    }

    private void initializeSongList() {
        songList = new ArrayList<Song>();
        songList.add(new Song("Croatian Rhapsody", R.raw.croatian));
        songList.add(new Song("High Score", R.raw.highscore));
        songList.add(new Song("Unity", R.raw.unity));
    }


    private class MyAdaptor extends BaseAdapter{

        @Override
        public int getCount() {
            return songList.size();
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
            view = getLayoutInflater().inflate(R.layout.detailed_view, null);
            Song curSong = songList.get(i);
            TextView name = view.findViewById(R.id.song_name);
            name.setText(curSong.name);
            return view;
        }
    }
}
