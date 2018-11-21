package com.example.pinkorange.vibrato;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

public class LiveWithSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BarVisualizer mVisualizer;

    private MediaPlayer mAudioPlayer;
    private BassBoost mBassBoost;
    private LoudnessEnhancer mLoudnessEnhancer;

    private SeekBar vibrate;
    private SeekBar loudness;
    private SeekBar bass;

    private Toolbar toolbar;
    private ImageView settingsIcon;

    private boolean isFragmentLoaded;
    private android.support.v4.app.Fragment menuFragment;
    private ImageView settingsButton;

    private HapticFeedback hapticFeedback;
    private Vibrator vibrator;
    private Song recordedSong;
    private int recordedSongId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        recordedSong = (Song) intent.getSerializableExtra("song");
        recordedSongId = recordedSong.id;

        // Set song title and artist
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(recordedSong.title);
        TextView artist = (TextView) findViewById(R.id.artist);
        artist.setText(recordedSong.artist);
        // Set song lyrics
        TextView lyrics = (TextView) findViewById(R.id.lyrics);
        boolean lyricsOn = true; //TODO: Update this variable field to read lyrics toggle switch value
        if (lyricsOn) {
            lyrics.setText(recordedSong.lyrics);
        } else {
            lyrics.setText("");
        }

        if (recordedSongId == -1) {
            Log.e("App", "Failed to pass the current song through activities");
        }

        initAddlayout(R.layout.activity_live_with_settings);


        setContentView(R.layout.activity_live_with_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        settingsButton = (ImageView) findViewById(R.id.menu_icon);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFragmentLoaded) {
                    loadFragment();
                }
                else {
                    if (menuFragment != null) {
                        if (menuFragment.isAdded()) {
                            hideFragment();
                        }
                    }
                }
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // MediaPlayer object (audio file in raw/test.mp3)
        mAudioPlayer = MediaPlayer.create(this, recordedSongId);

        switch (recordedSongId) {
            case R.raw.croatian:
                hapticFeedback = new HapticFeedback(80,618);
                break;
            case R.raw.highscore:
                hapticFeedback = new HapticFeedback(100, 538);
                break;
            case R.raw.unity:
                hapticFeedback = new HapticFeedback(100, 555);
                break;
        }
        Thread t = new Thread(hapticFeedback);
        t.start();

        mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mAudioPlayer.start();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        requestVisualizerPermissions();
        initializeVisualizerAndFeedback();
        initializeAudioVisualizer();



        // When music stream ends
        mAudioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("-----------------------", "6");
                mp.release();
                Log.e("-----------------------", "7");
                //when music stream ends playing
            }
        });

    }

    protected void initAddlayout(int layout) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null);
        //((FrameLayout) findViewById(R.id.bar)).addView(view);

    }



    public void hideFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_out, R.anim.slide_back);
        fragmentTransaction.remove(menuFragment);
        fragmentTransaction.commit();
        settingsButton.setImageResource(R.drawable.settings);
        isFragmentLoaded = false;

    }
    public void loadFragment(){
        FragmentManager fm = getSupportFragmentManager();
        menuFragment = fm.findFragmentById(R.id.container);
        settingsButton.setImageResource(R.drawable.ic_up_arrow);
        if(menuFragment == null){
            menuFragment = new MenuFrag();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_back, R.anim.slide_out);
            fragmentTransaction.add(R.id.container,menuFragment);
            fragmentTransaction.commit();
        }

        isFragmentLoaded = true;
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.e("--------------", "-----------------------------------");
        getMenuInflater().inflate(R.menu.activity_live_with_settings_drawer, menu);
        //sideMenu = menu;
        //  store the menu to var when creating options menu
        vibrate = (SeekBar) menu.findItem(R.id.vibration);
        loudness = (SeekBar) menu.findItem(R.id.loudness);
        bass = (SeekBar) menu.findItem(R.id.bass);




        bassBoost();
        loudnessEnchance();

        return true;
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

    private void initializeAudioVisualizer() {
        mVisualizer = findViewById(R.id.bar);

        mAudioPlayer = MediaPlayer.create(this, R.raw.test);
        mAudioPlayer.start();

        int audioSessionId = mAudioPlayer.getAudioSessionId();
        if (audioSessionId != -1)
            mVisualizer.setAudioSessionId(audioSessionId);
    }

    private void lyrics() {
        //ZUOJUN
        // Seek Bar
        //SeekBar seekBar = (SeekBar) sideMenu.findItem(R.id.vibration);


        // Change progress when slider position is changed
        vibrate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        // Create the bars

        // Seek Bar
        //SeekBar seekBar = (SeekBar) sideMenu.findItem(R.id.bass);

        // Change progress when slider position is changed
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
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loudnessEnchance() {
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

        // Create the bars

        // Seek Bar
        //SeekBar seekBar = (SeekBar) sideMenu.findItem(R.id.loudness);


        // Change progress when slider position is changed
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
    }

    @Override
    public void onBackPressed() {
        mAudioPlayer.stop();
        hapticFeedback.stop();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class HapticFeedback implements Runnable {
            private volatile boolean exit = false;
            private int vibrationTime, delay;
            public HapticFeedback(int vibrationTime, int delay) {
                this.vibrationTime = vibrationTime;
                this.delay = delay;
            }
            @Override
            public void run() {
                beginHapticFeedback();
            }
            public void stop() {
                exit = true;
            }
            private void beginHapticFeedback() {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                try {
                    while (!exit & mAudioPlayer.isPlaying()) {
                        vibrator.vibrate(VibrationEffect.createOneShot(vibrationTime, 255));
                        Thread.sleep(delay);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVisualizer != null)
            mVisualizer.release();
    }

}
