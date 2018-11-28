package com.example.pinkorange.vibrato;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    protected Intent recordedIntent, liveIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        Button recorded_music_button = findViewById(R.id.recorded_music_button);
        recordedIntent = new Intent(MainActivity.this, SelectMusic.class);
        recorded_music_button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 startActivity(recordedIntent);
             }
         });

        Button live_music_button = findViewById(R.id.live_music_button);
        liveIntent = new Intent(MainActivity.this, LiveMusic.class);
        live_music_button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 startActivity(liveIntent);
             }
         });

        /*Button bass = findViewById(R.id.buttonBass);
        bass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, BassBooster.class);
                startActivity(i);
            }
        });*/
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_DENIED)
            Log.d("App", "No MODIFY_AUDIO_SETTINGS" );
        else
            Log.d("App", "Yes MODIFY_AUDIO_SETTINGS" );
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
            Log.d("App", "No RECORD_AUDIO" );
        else
            Log.d("App", "Yes RECORD_AUDIO" );

        Log.d("App","Requesting permissions" );
        ActivityCompat.requestPermissions( this, new String[]
                {
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.RECORD_AUDIO
                },1 );
        Log.d("App","Requested perms");
    }

}
