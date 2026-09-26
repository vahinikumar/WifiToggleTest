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

        if (audioManager != null) {

            if (audioManager.getRingerMode()
                    == AudioManager.RINGER_MODE_NORMAL) {

                audioManager.setRingerMode(
                        AudioManager.RINGER_MODE_VIBRATE
                );

            } else {

                audioManager.setRingerMode(
                        AudioManager.RINGER_MODE_NORMAL
                );
            }
        }

        finish();
    }
}
