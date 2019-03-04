package com.example.pinkorange.vibrato;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.BassBoost;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class LiveWithSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final int LIVE_THRESHOLD = 130;
    private final int RECORD_THRESHOLD = 130;

    private boolean permissionsGranted;

    private BarVisualizer mVisualizer;

    private MediaPlayer mAudioPlayer;
    private Song recordedSong;
    private ArrayList<Integer> allSongId;
    private ArrayList<Song> allSongs;
    private int recordedSongId;
    private int curAudioSessionId;

    private boolean isLive;
    private Thread liveMusicThread;
    private LiveMusicAnalysis liveMusicAnalysis;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private boolean lyricsIsChecked;
    private Switch lyricsSwitch;
    private MenuItem lyricsItem;

    private SeekBar vibrate_seek, loudness, bass;
    private BassBoost mBassBoost;
    private LoudnessEnhancer mLoudnessEnhancer;

    private Button playButton;
    private Button skipButton;
    private Button prevButton;

    private TextView title;
    private TextView artist;

    private Thread seekBarThread;
    private SeekBar mSeekBar;
    private MusicSeekBar musicSeekBar;

    private double songDuration;
    private String convertedSongDuration;
    private TextView songCurTime, songEndTime;

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

        permissionsGranted = false;

        curAudioSessionId = -1;
        lyricsIsChecked = false;
        convertedSongDuration = "";

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        lyricsSwitch = (Switch) navigationView.getMenu().getItem(1).getActionView();
        lyricsItem = navigationView.getMenu().getItem(1);

        vibrate_seek = (SeekBar) navigationView.getMenu().getItem(3).getActionView();
        bass = (SeekBar) navigationView.getMenu().getItem(5).getActionView();
        loudness = (SeekBar) navigationView.getMenu().getItem(7).getActionView();

        mVisualizer = findViewById(R.id.visualizer);

        playButton = findViewById(R.id.play);
        skipButton = findViewById(R.id.skip);
        prevButton = findViewById(R.id.prev);

        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);

        mSeekBar = findViewById(R.id.music_prograss_bar);
        musicSeekBar = new MusicSeekBar();

        songCurTime = findViewById(R.id.song_start_time);
        songEndTime = findViewById(R.id.song_end_time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_music);
        initVariables();
        disableElements();
        requestPermissions();
    }

    private void disableElements() {
        playButton.setVisibility(View.GONE);
        skipButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        mSeekBar.setVisibility(View.GONE);
    }

    private void reenableElements() {
        playButton.setVisibility(View.VISIBLE);
        skipButton.setVisibility(View.VISIBLE);
        prevButton.setVisibility(View.VISIBLE);
        mSeekBar.setVisibility(View.VISIBLE);
    }

    private void continueInitialization() {
        setSupportActionBar(toolbar);
        setActionBarToggle();
        navigationView.setNavigationItemSelectedListener(this);

        setVibrationSeekBar();
        setBassSeekBar();
        setLoudnessSeekBar();
        initializeVisualizerAndFeedback();
        initializeSongSeekBar();
        setMusicControlButton();

        if (!isLive) {
            reenableElements();
            setSongDetails();
            bassBoost();
            loudnessEnhance();
        } else {
            setLiveDetails();
        }
    }

    private void setLiveDetails() {
        title.setText(R.string.live_music_title);
        lyricsItem.setVisible(false);
        mSeekBar.setVisibility(View.GONE);
    }

    private void initializeSongSeekBar() {
        songEndTime.setText(convertedSongDuration);

        seekBarThread = new Thread(musicSeekBar);
        seekBarThread.start();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mAudioPlayer != null && fromUser){
                    mAudioPlayer.seekTo((int) ((progress / 100.0) * songDuration * 1000));
                }
            }
        });
    }

    private void setMusicControlButton() {
        if (isLive) {
            playButton.setVisibility(View.GONE);
            skipButton.setVisibility(View.GONE);
            prevButton.setVisibility(View.GONE);
        } else {
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAudioPlayer.isPlaying()) {
                        mAudioPlayer.pause();
                        playButton.setBackgroundResource(R.drawable.round_play_arrow_24);
                    } else {
                        mAudioPlayer.start();
                        playButton.setBackgroundResource(R.drawable.round_pause_24);
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
                    playButton.setBackgroundResource(R.drawable.round_pause_24);
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
                    playButton.setBackgroundResource(R.drawable.round_pause_24);
                }
            });
        }
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

        final TextView lyrics = (TextView) findViewById(R.id.lyrics);

        if (lyricsIsChecked) {
            lyrics.setText(recordedSong.lyrics + "\n\n\n\n\n\n\n\n\n\n");
        }
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

        songDuration = mAudioPlayer.getDuration() / 1000.0;
        convertedSongDuration = convertSongDuration(songDuration);

        mAudioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playButton.setBackgroundResource(R.drawable.round_play_arrow_24);
            }
        });

        mAudioPlayer.start();

        int audioSessionId = mAudioPlayer.getAudioSessionId();
        this.curAudioSessionId = audioSessionId;
        if (audioSessionId != -1) {
            mVisualizer.setAudioSessionId(audioSessionId, this,
                    RECORD_THRESHOLD, 0.5);
        }
    }

    private String convertSongDuration(double timeToConvert) {
        String convertedString = "";

        int minutes = (int) (timeToConvert / 60);
        if (minutes < 10) {
            convertedString += "0" + minutes;
        } else {
            convertedString += minutes;
        }

        convertedString += ":";

        int seconds = (int) (timeToConvert % 60);

        if (seconds < 10) {
            convertedString += "0" + seconds;
        } else {
            convertedString += seconds;
        }

        return convertedString;
    }

    @Override
    public void onBackPressed() {
        lyricsIsChecked = false;
        if (!isLive) {
            mAudioPlayer.stop();
            musicSeekBar.stop();
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
        if (id == R.id.menu_title) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //drawer.closeDrawer(GravityCompat.START);
        return false;
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

    @SuppressLint("ClickableViewAccessibility")
    private void enableLeftSilde(SeekBar b){
        b.setOnTouchListener(new ListView.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int action = event.getAction();
                switch (action)
                {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle seekbar touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
    }
    private void setLoudnessSeekBar() {
        // TODO: Put these into the XML file somehow?
        loudness.setProgress(50);
        enableLeftSilde(loudness);
        loudness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLoudnessEnhancer.setEnabled(true);
                mLoudnessEnhancer.setTargetGain(5 * progress);
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
        bass.setProgress(50);
        enableLeftSilde(bass);
        bass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBassBoost.setEnabled(true);
                mBassBoost.setStrength((short) (10 * progress));
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
        vibrate_seek.setProgress(50);
        enableLeftSilde(vibrate_seek);
        vibrate_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVisualizer.setAudioSessionId(curAudioSessionId, LiveWithSettings.this,
                        RECORD_THRESHOLD, vibrate_seek.getProgress() / 100.0);
            }
        });
    }

    private void setSongDetails() {
        title.setText(recordedSong.title);
        artist.setText(recordedSong.artist);

        // Set song lyrics
        final TextView lyrics = findViewById(R.id.lyrics);
        lyricsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lyrics.setText(recordedSong.lyrics + "\n\n\n\n\n\n\n\n\n\n");
                    lyricsIsChecked = true;
                } else {
                    lyrics.setText("");
                    lyricsIsChecked = false;
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
        if (curAudioSessionId != -1) {
            mBassBoost = new BassBoost(0, curAudioSessionId);
            BassBoost.Settings bassBoostSettingTemp = mBassBoost.getProperties();
            BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());

            bassBoostSetting.strength = ((short) 1000 / 19);
            mBassBoost.setProperties(bassBoostSetting);
            mAudioPlayer.setAuxEffectSendLevel(1.0f);
        }
    }

    private void loudnessEnhance() {
        // Bass Booster object (priority -- app with highest priority controls
        // the equalizer, session id for audio sess -- attach to media player in same audio sess)
        if (curAudioSessionId != -1) {
            mLoudnessEnhancer = new LoudnessEnhancer(curAudioSessionId);
            // Gain is in mB (0mB = no amp)
            mLoudnessEnhancer.setTargetGain(0);
            mAudioPlayer.setAuxEffectSendLevel(1.0f);
        }
    }

    private class LiveMusicAnalysis implements Runnable {  
        private AudioRecord recorder;
        private AudioTrack audioPlayer;
        private Context musicContext;

        private int audioSource, channelConfig, samplingRate, channelConfigOut, audioFormat, bufferSize;
        private byte[] recordData;
        private boolean isRecording;

        public LiveMusicAnalysis(Context musicContext) {
            this.musicContext = musicContext;
            this.isRecording = false;
            this.audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
            this.channelConfig = AudioFormat.CHANNEL_IN_MONO;
            this.channelConfigOut = AudioFormat.CHANNEL_OUT_MONO;
            this.audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            this.bufferSize = 64;
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
                mVisualizer.setAudioSessionId(audioSessionId, this.musicContext,
                        LIVE_THRESHOLD, 0.5);
                curAudioSessionId = audioSessionId;
            }
        }

        private void initAudioStreams() {
            this.recorder = new AudioRecord(this.audioSource, this.samplingRate, this.channelConfig, this.audioFormat, this.bufferSize);
            if (this.recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                this.recorder.startRecording();
                this.isRecording = true;
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

            if(this.audioPlayer.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                this.audioPlayer.play();
                initVisualizer();
                bassBoost();
                loudnessEnhance();
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

        private void bassBoost() {
            if (curAudioSessionId!= -1) {
                mBassBoost = new BassBoost(0, curAudioSessionId);
                BassBoost.Settings bassBoostSettingTemp = mBassBoost.getProperties();
                BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());

                bassBoostSetting.strength = ((short) 1000 / 19);
                mBassBoost.setProperties(bassBoostSetting);
                audioPlayer.setAuxEffectSendLevel(1.0f);
            }
        }

        private void loudnessEnhance() {
            if (curAudioSessionId != -1) {
                mLoudnessEnhancer = new LoudnessEnhancer(curAudioSessionId);
                mLoudnessEnhancer.setTargetGain(0);
                audioPlayer.setAuxEffectSendLevel(1.0f);
            }
        }
    }

    private class MusicSeekBar implements Runnable {
        private boolean isRunning;

        public MusicSeekBar() {
            this.isRunning = true;
        }

        @Override
        public void run() {
            while (this.isRunning) {
                try {
                    if(mAudioPlayer != null){
                        double mCurrentPosition = mAudioPlayer.getCurrentPosition() / 1000.0;
                        String convertedTime = convertSongDuration(mCurrentPosition);
                        songCurTime.setText(convertedTime);
                        mSeekBar.setProgress((int)(mCurrentPosition / songDuration * 100));
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {}
            }
        }

        public void stop() {
           this.isRunning = false;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        continueInitialization();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
