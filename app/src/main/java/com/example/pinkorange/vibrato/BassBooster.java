package com.example.pinkorange.vibrato;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.io.IOException;


public class BassBooster extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private BassBoost mBassBoost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_effects);

        // Let device's volume controls control our audio stream
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        requestVisualizerPermissions();

        // MediaPlayer object (audio file in raw/test.mp3)
        mMediaPlayer = MediaPlayer.create(this, R.raw.test);

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();

            }
        });

        // Bass Booster object (priority -- app with highest priority controls
        // the equalizer, session id for audio sess -- attach to media player in same audio sess)
        int audioSessionId = mMediaPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            mBassBoost = new BassBoost(0, audioSessionId);
            mBassBoost.setEnabled(true);
            mBassBoost.setStrength((short) 1000);
            //mMediaPlayer.attachAuxEffect(mBassBoost.getId());
            mMediaPlayer.setAuxEffectSendLevel(1.0f);
            mMediaPlayer.start();
        } else {
            Log.e("BassBooster", "audio session id  == -1");
        }






        // Create the bars
        setupBoosterFxAndUi();




        // When music stream ends
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("-----------------------", "6");
                mp.release();
                Log.e("-----------------------", "7");
                //when music stream ends playing
            }
        });


    }

    private void requestVisualizerPermissions() {
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


    private void setupBoosterFxAndUi() {
        // Seek Bar
        SeekBar seekBar = findViewById(R.id.seekBar);


        // Change progress when slider position is changed
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBassBoost.setStrength((short) (10 * progress));
                Log.e("----", Short.toString(mBassBoost.getRoundedStrength()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

}
