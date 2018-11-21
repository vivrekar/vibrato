package com.example.pinkorange.vibrato;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
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
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

public class LiveWithSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BarVisualizer mVisualizer;
    private HapticFeedback hapticFeedback;
    private MediaPlayer mAudioPlayer;
    private Vibrator vibrator;
    private Song recordedSong;
    private int recordedSongId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_music);

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestVisualizerPermissions();
        initializeVisualizerAndFeedback();
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
        mVisualizer = findViewById(R.id.bar);
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
            case R.raw.christmas:
                hapticFeedback = new HapticFeedback(90, 530);
                break;
            case R.raw.grilboyfriend:
                hapticFeedback = new HapticFeedback(90, 520);
                break;
            case R.raw.forever:
                hapticFeedback = new HapticFeedback(100, 565);
                break;
            case R.raw.alreadygone:
                hapticFeedback = new HapticFeedback(100, 575);
                break;
            case R.raw.doesntmatter:
                hapticFeedback = new HapticFeedback(100, 600);
                break;
            case R.raw.walkaway:
                hapticFeedback = new HapticFeedback(100, 600);
                break;
            case R.raw.enen:
                hapticFeedback = new HapticFeedback(100, 620);
                break;

        }
        Thread t = new Thread(hapticFeedback);
        t.start();
        mAudioPlayer.start();

        int audioSessionId = mAudioPlayer.getAudioSessionId();
        if (audioSessionId != -1)
            mVisualizer.setAudioSessionId(audioSessionId);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // TODO: Delete R.menu.live_with_settings file
        //getMenuInflater().inflate(R.menu.live_with_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.display_lyrics_switch) {
            // Handle the display lyrics action
        } /*else if (id == R.id.__________) {}*/

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
}
