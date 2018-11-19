package com.example.pinkorange.vibrato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recorded_music_button = findViewById(R.id.recorded_music_button);
        recorded_music_button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, LiveWithSettings.class);
                 startActivity(intent);
             }
         });

        Button bass = findViewById(R.id.buttonBass);
        bass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), BassBooster.class);
                startActivity(i);
            }
        });
    }
}
