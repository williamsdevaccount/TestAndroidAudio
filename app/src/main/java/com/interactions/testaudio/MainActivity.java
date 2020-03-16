package com.interactions.testaudio;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String recordAudioStart = "Record Audio";
    private String recordAudioStop = "Stop Recording";
    private String playAudioStart = "Play Audio";
    private String playAudioStop = "Stop Audio";
    private static final int DEFAULT_CHANNEL = 1;
    private boolean audioRecording = false;
    private boolean audioPlaying = false;
    public static final String LOG_TAG = "AUDIO FUNCTIONS";

    @BindView(R.id.btn_record_audio)
    Button btnRecordAudio;

    @BindView(R.id.btn_play_audio)
    Button btnPlayAudio;

    @BindView(R.id.et_audio_channel)
    EditText etAudioChannel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionsDialog dialog =  new PermissionsDialog(this);
        dialog.checkPermissions(this);
        TestAudioMethods methods = new TestAudioMethods(this);
        methods.testAudioManager();
    }

    @OnClick(R.id.btn_record_audio)
    public void recordAudio() {
        audioRecording = !audioRecording;
        onRecord(audioRecording);
        updateRecordAudioButton(audioRecording);
    }

    @OnClick(R.id.btn_play_audio)
    public void playAudio() {
        audioPlaying = !audioPlaying;
        onPlay(audioPlaying);
        updatePlayAudioButton(audioPlaying);
    }

    private void updateRecordAudioButton(boolean recording) {
        String updateText = recordAudioStart;
        if (recording) {
            updateText = recordAudioStop;
        }
        btnRecordAudio.setText(updateText);
    }

    private void updatePlayAudioButton(boolean playing) {
        String updateText = playAudioStart;
        if (playing) {
            updateText = playAudioStop;
        }
        btnPlayAudio.setText(updateText);
    }
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
    private int getAudioChannel() {
        int channel = DEFAULT_CHANNEL;
        String aChannel = etAudioChannel.getText().toString();
        try{
            channel = Integer.parseInt(aChannel);
        }
        catch (Exception e) {
            Log.e("GET_AUDIO_CHANNEL",
                    "Error parsing audio channel from EditText with text : " + aChannel + ", setting channel to " + DEFAULT_CHANNEL);
        }
        return channel;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setOnCompletionListener(this);
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioChannels(getAudioChannel());
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playAudio();
    }
}
