package com.example.pinkorange.vibrato;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectMusic extends AppCompatActivity {
    private SongStore ss = new SongStore();
    private ArrayList<Uri> songId;
    private MediaMetadataRetriever mmr;
    Thread thread;
    ArrayList<Audio> audioList;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);
        thread = new Thread(){
            @Override
            public void run() {
                try {
                    SelectMusic.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        songId = new ArrayList<>();
        mmr = new MediaMetadataRetriever();
        activity = this;

        requestPermission(activity);
        loadAudio();
        if(audioList == null){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "There is no local music...",
                    Toast.LENGTH_SHORT);
            toast.show();
            thread.start();
            return;
        }

        for(Audio a : audioList){
            songId.add(Uri.parse(a.data));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.list_view);
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), LiveWithSettings.class);
                Audio cur_song = audioList.get(i);
                // TODO: bundle the song item and send the whole thing to the next intent
                // TODO: so that the next screen can use song to set title, artist, lyrics
                intent.putExtra("song", cur_song);
                intent.putExtra("live", false);
                intent.putExtra("songId", songId);
                intent.putExtra("allSongs", audioList);
                startActivity(intent);
            }
        });
    }
    private void requestPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to audioList
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        cursor.close();
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return audioList.size();
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
            // TODO: Set background shape of song card
            //LinearLayout songCard = findViewById(R.id.song_card);
            //songCard.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle));
            // Add text details to song card
            Audio curSong = audioList.get(i);
            TextView title = view.findViewById(R.id.song_title);
            title.setText(curSong.getTitle());

            TextView artist = view.findViewById(R.id.song_artist);
            if (curSong.artist.equals("<unknown>")){
                curSong.artist = "Unknown Artist";
            }

            artist.setText(curSong.getArtist());

            TextView songDuration = view.findViewById(R.id.music_time);
            Uri mediaPath = Uri.parse(curSong.getData());
            mmr.setDataSource(SelectMusic.this, mediaPath);
            String song_duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int intSongDuration = Integer.parseInt(song_duration) / 1000;
            String minSec = (intSongDuration / 60) + ":";
            int seconds = intSongDuration % 60;

            if (seconds < 10) {
                minSec += "0" + seconds;
            } else {
                minSec += seconds;
            }

            songDuration.setText(minSec);

            return view;
        }
    }
}
