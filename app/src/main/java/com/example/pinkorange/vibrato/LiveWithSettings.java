package com.example.pinkorange.vibrato;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class LiveWithSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BarVisualizer mVisualizer;

    private MediaPlayer mAudioPlayer;
    private Song recordedSong;
    private int recordedSongId;

    private SeekBar vibrate_seek;
    private SeekBar loudness;
    private SeekBar bass;

    private BassBoost mBassBoost;
    private LoudnessEnhancer mLoudnessEnhancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_music);

        Intent intent = getIntent();
        recordedSong = (Song) intent.getSerializableExtra("song");
        recordedSongId = recordedSong.id;

        if (recordedSongId == -1) {
            Log.e("App", "Failed to pass the current song through activities");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set song title and artist
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(recordedSong.title);
        TextView artist = (TextView) findViewById(R.id.artist);
        artist.setText(recordedSong.artist);

        // Set song lyrics
        final TextView lyrics = (TextView) findViewById(R.id.lyrics);
        Switch lyricsSwitch = (Switch) navigationView.getMenu().getItem(1).getActionView();
        lyricsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lyrics.setText(recordedSong.lyrics + "\n\n\n\n\n\n\n\n\n\n");
                } else {
                    lyrics.setText("");
                }
            }
        });

        Switch song_notif_switch = (Switch) navigationView.getMenu().getItem(2).getActionView();
        song_notif_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        //TODO Implement song notifications
                } 
            }
        });

        vibrate_seek = (SeekBar) navigationView.getMenu().getItem(4).getActionView();
        vibrate_seek.setPadding(120, 0, 0, 0);

        vibrate_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //TODO add what to do when we change vibration intensity (just take the progress,
                // which is a value from 0-100 and multiply that into your curr vibration intensity?
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        bass = (SeekBar) navigationView.getMenu().getItem(6).getActionView();
        bass.setPadding(120, 0, 0, 0);

        bass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        loudness = (SeekBar) navigationView.getMenu().getItem(8).getActionView();
        loudness.setPadding(120, 0, 0, 0);

        loudness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });
        
        requestVisualizerPermissions();
        initializeVisualizerAndFeedback();
        bassBoost();
        loudnessEnhance();
    }

    private void bassBoost() {
        // Bass Booster object (priority -- app with highest priority controls
        // the equalizer, session id for audio sess -- attach to media player in same audio sess)
        int audioSessionId = mAudioPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            Log.e("Get player audio sess", "Audio session ====================== -1");
            mBassBoost = new BassBoost(0, audioSessionId);
            BassBoost.Settings bassBoostSettingTemp = mBassBoost.getProperties();
            BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());

            bassBoostSetting.strength = ((short) 1000 / 19);
            mBassBoost.setProperties(bassBoostSetting);
            mAudioPlayer.setAuxEffectSendLevel(1.0f);
        } else {
            Log.e("BassBooster", "audio session id  == -1");
        }
    }

    private void loudnessEnhance() {
        // Bass Booster object (priority -- app with highest priority controls
        // the equalizer, session id for audio sess -- attach to media player in same audio sess)
        int audioSessionId = mAudioPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            Log.e("Get player audio sess", "Audio session ====================== -1");
            mLoudnessEnhancer = new LoudnessEnhancer(audioSessionId);
            // Gain is in mB (0mB = no amp)
            mLoudnessEnhancer.setTargetGain(0);
            mAudioPlayer.setAuxEffectSendLevel(1.0f);
        } else {
            Log.e("Loudness Enhancer", "audio session id  == -1");
        }
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

    private void initializeVisualizerAndFeedback() {
        mAudioPlayer = MediaPlayer.create(this, recordedSongId);
        mAudioPlayer.start();

        int audioSessionId = mAudioPlayer.getAudioSessionId();
        mVisualizer = findViewById(R.id.visualizer);
        if (audioSessionId != -1) {
            mVisualizer.setAudioSessionId(audioSessionId, this);
        }
    }

    @Override
    public void onBackPressed() {
        mAudioPlayer.stop();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_app_bar_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.display_lyrics_switch) {
            // Handle the display lyrics action
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVisualizer != null) {
            mVisualizer.release();
        }
    }
}
