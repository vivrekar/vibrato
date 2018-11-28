package com.example.pinkorange.vibrato;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LiveMusic extends AppCompatActivity {
    private AudioTrack audioPlayer;
    private AudioRecord recorder;
    private LiveMusicAnalysis liveMusicAnalysis;
    private BarVisualizer mVisualizer;

    private int audioSource, samplingRate, channelConfig, channelConfigOut, audioFormat, bufferSize;
    private byte[] recordData = new byte[bufferSize];

    private void initVariables() {
        audioSource = MediaRecorder.AudioSource.MIC;
        samplingRate = 44100;
        channelConfig = AudioFormat.CHANNEL_IN_MONO;
        channelConfigOut = AudioFormat.CHANNEL_OUT_MONO;
        audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        bufferSize = 2048;
        recordData = new byte[bufferSize];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_music);

        initVariables();
        initAudioStream();
    }

    private void initAudioStream() {
        recorder = new AudioRecord(audioSource, samplingRate, channelConfig, audioFormat, bufferSize);
        if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
            recorder.startRecording();
        }

        audioPlayer = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(audioFormat)
                        .setSampleRate(samplingRate)
                        .setChannelMask(channelConfigOut)
                        .build())
                .setBufferSizeInBytes(bufferSize)
                .build();

        audioPlayer.setVolume(0);
        if(audioPlayer.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
            audioPlayer.play();
            initVisualizer();
        }

        liveMusicAnalysis = new LiveMusicAnalysis();
        Thread t = new Thread(liveMusicAnalysis);
        t.start();
    }

    private void initVisualizer() {
        mVisualizer = findViewById(R.id.liveVisualizer);
        int audioSessionId = audioPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            mVisualizer.setAudioSessionId(audioSessionId, this);
        }
    }

    private class LiveMusicAnalysis implements Runnable {
        private boolean isRecording;

        public LiveMusicAnalysis() {
            isRecording = true;
        }

        @Override
        public void run() {
            listenAndPlay();
        }

        public void stop() {
            isRecording = false;
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

    @Override
    public void onBackPressed() {
        audioPlayer.stop();
        liveMusicAnalysis.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mVisualizer != null) {
            mVisualizer.release();
        } if (audioPlayer != null) {
            audioPlayer.release();
        } if (recorder != null) {
            recorder.release();
        }
        super.onDestroy();
    }
}
