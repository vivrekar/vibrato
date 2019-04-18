package com.example.pinkorange.vibrato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    protected Intent recordedIntent, liveIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recorded_music_button = findViewById(R.id.recorded_music_button);
        recordedIntent = new Intent(MainActivity.this, SelectMusic.class);
        recorded_music_button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 startActivity(recordedIntent);
             }
         });

        Button live_music_button = findViewById(R.id.live_music_button);
        liveIntent = new Intent(MainActivity.this, LiveWithSettings.class);
        live_music_button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 Log.e("App", "ASIOHXIOH");
                 liveIntent.putExtra("live", true);
                 startActivity(liveIntent);
             }
         });

        //for test

       setView();
    }

    public void setView(){
        TextView view = findViewById(R.id.test_track);
        FetchLyrics a = new FetchLyrics("Sugar", "maroon5", this);
        a.findTrackId();
        view.setText(a.trackId + "");
    }
}
