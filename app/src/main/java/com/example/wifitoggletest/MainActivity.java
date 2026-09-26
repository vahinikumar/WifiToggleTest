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

        int mode = audioManager.getRingerMode();

        // If already in Normal mode, do nothing.
        if (mode == AudioManager.RINGER_MODE_NORMAL) {
            finish();
            return;
        }

        // Otherwise restore normal ringing.
        audioManager.setRingerMode(
                AudioManager.RINGER_MODE_NORMAL
        );

        audioManager.setVibrateSetting(
                AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF
        );

        finish();
    }
}
