package com.example.pinkorange.vibrato;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.BassBoost;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class LiveWithSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BarVisualizer mVisualizer;

    private MediaPlayer mAudioPlayer;
    private Song recordedSong;
    private ArrayList<Integer> allSongId;
    private ArrayList<Song> allSongs;
    private int recordedSongId;

    private boolean isLive;
    private Thread liveMusicThread;
    private LiveMusicAnalysis liveMusicAnalysis;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Switch song_notif_switch;

    private SeekBar vibrate_seek, loudness, bass;
    private BassBoost mBassBoost;
    private LoudnessEnhancer mLoudnessEnhancer;

    private Button playButton;
    private Button skipButton;
    private Button prevButton;

    private void initVariables() {
        Intent intent = getIntent();
        isLive = intent.getBooleanExtra("live", false);

        if (!isLive) {
            recordedSong = (Song) intent.getSerializableExtra("song");
            allSongs = (ArrayList<Song>) intent.getSerializableExtra("allSongs");
            allSongId = (ArrayList<Integer>) intent.getSerializableExtra("songId");
            recordedSongId = recordedSong.id;
            if (recordedSongId == -1) {
                Log.e("App", "Failed to pass the current song through activities");
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        song_notif_switch = (Switch) navigationView.getMenu().getItem(2).getActionView();
        vibrate_seek = (SeekBar) navigationView.getMenu().getItem(4).getActionView();
        bass = (SeekBar) navigationView.getMenu().getItem(6).getActionView();
        loudness = (SeekBar) navigationView.getMenu().getItem(8).getActionView();
        mVisualizer = findViewById(R.id.visualizer);
        playButton = findViewById(R.id.play);
        skipButton = findViewById(R.id.skip);
        prevButton = findViewById(R.id.prev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_music);
        requestPermissions();
        initVariables();

        setSupportActionBar(toolbar);
        setActionBarToggle();
        navigationView.setNavigationItemSelectedListener(this);

        setSongSwitchNotifications();
        setMusicControlButton();
        setVibrationSeekBar();
        setBassSeekBar();
        setLoudnessSeekBar();
        initializeVisualizerAndFeedback();

        if (!isLive) {
            setSongDetails();
            bassBoost();
            loudnessEnhance();
        }
    }

    private void setMusicControlButton() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAudioPlayer.isPlaying()) {
                    mAudioPlayer.pause();
                } else {
                    mAudioPlayer.start();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curSongIndex = allSongId.indexOf(recordedSongId);
                int newSongIndex = curSongIndex + 1;
                if (curSongIndex >= allSongId.size() - 1) {
                    newSongIndex = 0;
                }
                setPrevNextButton(newSongIndex);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curSongIndex = allSongId.indexOf(recordedSongId);
                int newSongIndex = curSongIndex - 1;
                if (curSongIndex <= 0) {
                    newSongIndex = allSongId.size() - 1;
                }
                setPrevNextButton(newSongIndex);
            }
        });


    }

    private void setPrevNextButton(int newSongIndex) {
        recordedSongId = allSongId.get(newSongIndex);
        recordedSong = allSongs.get(newSongIndex);
        if (!isLive) {
            mAudioPlayer.stop();
        } else {
            liveMusicAnalysis.stop();
        }
        initializeVisualizerAndFeedback();
        setSongDetails();
    }

    private void initializeVisualizerAndFeedback() {
        if (isLive) {
            liveVisualizerAndFeedback();
        } else {
            recordedVisualizerAndFeedback();
        }
    }

    private void liveVisualizerAndFeedback() {
        liveMusicAnalysis = new LiveMusicAnalysis(this);
        liveMusicThread = new Thread(liveMusicAnalysis);
        liveMusicThread.start();
    }

    private void recordedVisualizerAndFeedback() {
        mAudioPlayer = MediaPlayer.create(this, recordedSongId);
        mAudioPlayer.start();

        int audioSessionId = mAudioPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            mVisualizer.setAudioSessionId(audioSessionId, this);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isLive) {
            mAudioPlayer.stop();
        } else {
            liveMusicAnalysis.stop();
        }

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
        if (mAudioPlayer != null)
            mAudioPlayer.release();
        if (mVisualizer != null)
            mVisualizer.release();
        if (liveMusicAnalysis != null && isLive)
            liveMusicAnalysis.release();
    }

    private void setLoudnessSeekBar() {
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

    private void setBassSeekBar() {
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

    private void setVibrationSeekBar() {
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
    }

    private void setSongSwitchNotifications() {
        song_notif_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        //TODO Implement song notifications
                }
            }
        });
    }

    private void setSongDetails() {
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
    }

    private void setActionBarToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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

    private class LiveMusicAnalysis implements Runnable {  
        private AudioRecord recorder;
        private AudioTrack audioPlayer;
        private Context musicContext;

        private int audioSource, samplingRate, channelConfig, channelConfigOut, audioFormat, bufferSize;
        private byte[] recordData = new byte[bufferSize];
        private boolean isRecording;

        public LiveMusicAnalysis(Context musicContext) {
            this.musicContext = musicContext;
            this.isRecording = false;
            this.audioSource = MediaRecorder.AudioSource.MIC;
            this.samplingRate = 44100;
            this.channelConfig = AudioFormat.CHANNEL_IN_MONO;
            this.channelConfigOut = AudioFormat.CHANNEL_OUT_MONO;
            this.audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            this.bufferSize = 2048;
            this.recordData = new byte[bufferSize];
        }
        
        @Override
        public void run() {
            initAudioStreams();
            listenAndPlay();
        }

        public void stop() {
            this.isRecording = false;
            this.audioPlayer.stop();
            this.recorder.stop();
        }

        public void release() {
            if (this.audioPlayer != null)
                this.audioPlayer.release();
            if (this.recorder != null)
                this.recorder.release();
        }

        private void initVisualizer() {
            int audioSessionId = audioPlayer.getAudioSessionId();
            if (audioSessionId != -1) {
                mVisualizer.setAudioSessionId(audioSessionId, this.musicContext);
            }
        }

        private void initAudioStreams() {
            this.recorder = new AudioRecord(audioSource, samplingRate, channelConfig, audioFormat, bufferSize);
            if (this.recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                this.recorder.startRecording();
            }

            this.audioPlayer = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(this.audioFormat)
                        .setSampleRate(this.samplingRate)
                        .setChannelMask(this.channelConfigOut)
                        .build())
                .setBufferSizeInBytes(this.bufferSize)
                .build();

            this.audioPlayer.setVolume(0);
            if(this.audioPlayer.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                this.audioPlayer.play();
                initVisualizer();
            }
        }

        private void listenAndPlay() {
            int readBytes, writtenBytes = 0;
            do {
                readBytes = recorder.read(recordData, 0, bufferSize);

                if(AudioRecord.ERROR_INVALID_OPERATION != readBytes){
                    writtenBytes += audioPlayer.write(recordData, 0, readBytes);
                }
            } while (isRecording);
        }
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
