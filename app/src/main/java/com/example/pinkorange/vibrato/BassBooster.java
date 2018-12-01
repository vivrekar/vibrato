package com.example.pinkorange.vibrato;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

public class BassBooster extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private BassBoost mBassBoost;
    private LoudnessEnhancer mLoudnessEnhancer;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_effects);

        // Let device's volume controls control our audio stream
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // MediaPlayer object (audio file in raw/test.mp3)
        mMediaPlayer = MediaPlayer.create(this, R.raw.croatian);

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();

            }
        });

        bassBoost();
        loudnessEnchance();

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

    private void bassBoost() {
        // Bass Booster object (priority -- app with highest priority controls
        // the equalizer, session id for audio sess -- attach to media player in same audio sess)
        int audioSessionId = mMediaPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            Log.e("Get player audio sess", "Audio session ====================== -1");
            mBassBoost = new BassBoost(0, audioSessionId);

            BassBoost.Settings bassBoostSettingTemp = mBassBoost.getProperties();
            BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());

            bassBoostSetting.strength = ((short) 1000 / 19);
            mBassBoost.setProperties(bassBoostSetting);
            //mBassBoost.setStrength((short) 1000);
            //mMediaPlayer.attachAuxEffect(mBassBoost.getId());
            mMediaPlayer.setAuxEffectSendLevel(1.0f);

        } else {
            Log.e("BassBooster", "audio session id  == -1");
        }

        // Create the bars

        // Seek Bar
        SeekBar seekBar = findViewById(R.id.seekBar);


        // Change progress when slider position is changed
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBassBoost.setEnabled(true);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loudnessEnchance() {
        // Bass Booster object (priority -- app with highest priority controls
        // the equalizer, session id for audio sess -- attach to media player in same audio sess)
        int audioSessionId = mMediaPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            Log.e("Get player audio sess", "Audio session ====================== -1");
            mLoudnessEnhancer = new LoudnessEnhancer(audioSessionId);

            // Gain is in mB (0mB = no amp)
            mLoudnessEnhancer.setTargetGain(0);

            mMediaPlayer.setAuxEffectSendLevel(1.0f);
        } else {
            Log.e("Loudness Enhancer", "audio session id  == -1");
        }

        // Create the bars

        // Seek Bar
        /*SeekBar seekBar = findViewById(R.id.loudness);


        // Change progress when slider position is changed
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLoudnessEnhancer.setEnabled(true);
                mLoudnessEnhancer.setTargetGain(5 * progress);
                Log.e("----", Float.toString(mLoudnessEnhancer.getTargetGain()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }
}
