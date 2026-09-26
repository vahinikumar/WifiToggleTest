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

        audioManager.setStreamVolume(
                AudioManager.STREAM_RING,
                audioManager.getStreamMaxVolume(
                        AudioManager.STREAM_RING),
                AudioManager.FLAG_SHOW_UI
        );

        finish();
    }
}
