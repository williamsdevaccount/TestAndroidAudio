package com.interactions.testaudio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MicrophoneInfo;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestAudioMethods {
    AudioManager  manager;
    Context context;
    private static final String TAG = "AUDIO DEVICE TEST";
    public TestAudioMethods(Context context) {
        this.context = context;

        manager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
    }

    public void testAudioManager() {
        Log.e(TAG,"STARTING TEST AUDIO MANAGER");
        getAudioDevices();

    }

    private void getAudioDevices() {
        try {
            AudioDeviceInfo[] microphones = manager.getDevices(AudioManager.GET_DEVICES_ALL);
            Log.e(TAG,"AUDIO DEVICE SIZE ARRAY SIZE : " + microphones.length);
            for (AudioDeviceInfo microphone : microphones) {
                Log.w(TAG,"DEVICE CHANNELS: " + Arrays.toString(microphone.getChannelCounts()));
                Log.w(TAG,"DEVICE PRODUCT NAME : " + microphone.getProductName());
                Log.w(TAG,"DEVICE TYPE NAME : " + microphone.getType());
            }
        }
        catch(Exception e) {
            Log.e(TAG,"ERROR GETTING AUDIO DEVICES");
        }
    }
}

