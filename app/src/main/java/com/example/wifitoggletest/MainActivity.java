package com.example.wifitoggletest;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioManager audioManager =
                (AudioManager) getSystemService(AUDIO_SERVICE);

        int currentVolume =
                audioManager.getStreamVolume(
                        AudioManager.STREAM_RING
                );

        int maxVolume =
                audioManager.getStreamMaxVolume(
                        AudioManager.STREAM_RING
                );

        if (currentVolume < maxVolume) {

            audioManager.setStreamVolume(
                    AudioManager.STREAM_RING,
                    maxVolume,
                    0
            );
        }

        finish();
    }
}
