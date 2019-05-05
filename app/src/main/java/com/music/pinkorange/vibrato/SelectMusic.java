package com.music.pinkorange.vibrato;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectMusic extends AppCompatActivity {
    private ArrayList<Uri> songId;
    private MediaMetadataRetriever mmr;
    Thread thread;
    ArrayList<Audio> audioList;
    Activity activity;
    SharedPreferences prefs;
    AlertDialog dialog;
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
        prefs = getSharedPreferences("com.example.pinkorange.vibrato", MODE_PRIVATE);
        songId = new ArrayList<>();
        mmr = new MediaMetadataRetriever();
        activity = this;

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) && !prefs.getBoolean("firstrun", true)){
            showDialogTipUserGoToAppSetting();
        } else if(prefs.getBoolean("firstrun", true)){
            requestPermission(activity);
        } else{
            continueInitialize();
        }
    }

    private void continueInitialize(){
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

    //load audio from local storage
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
                try{
                    Uri mediaPath = Uri.parse(data);
                    mmr.setDataSource(SelectMusic.this, mediaPath);
                }catch(Exception e){
                    continue;
                }
                // Save to audioList
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        cursor.close();
    }

    private void requestPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        boolean failed = false;
        for(Integer i : grantResults){
            if (i !=  PackageManager.PERMISSION_GRANTED){
                failed = true;
            }
        }
        if (failed){
            showDialogTipUserGoToAppSetting();
        } else {
            continueInitialize();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //show the user tip to enable the permission
    private void showDialogTipUserGoToAppSetting(){
         dialog = new AlertDialog.Builder(this)
                .setTitle("Unable To Access External Storage !")
                .setMessage("Please Enable It In Setting...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getPackageName(),null);
                        intent.setData(uri);

                        startActivityForResult(intent,123);
                        finish();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();

        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLACK);

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.BLACK);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {

            prefs.edit().putBoolean("firstrun", false).commit();
        }
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

            Audio curSong = audioList.get(i);
            TextView title = view.findViewById(R.id.song_title);
            ImageView album = view.findViewById(R.id.album_cover);
            title.setText(curSong.getTitle());

            TextView artist = view.findViewById(R.id.song_artist);
            if (curSong.artist.equals("<unknown>")){
                curSong.artist = "Unknown Artist";
            }

            artist.setText(curSong.getArtist());

            TextView songDuration = view.findViewById(R.id.music_time);
            Uri mediaPath = Uri.parse(curSong.getData());
            mmr.setDataSource(SelectMusic.this, mediaPath);

            //set the album image
            byte[] art = mmr.getEmbeddedPicture();

            if( art != null ){
                album.setImageBitmap( BitmapFactory.decodeByteArray(art, 0, art.length));
            }
            else{
                album.setImageResource(R.drawable.no_image);
            }

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
