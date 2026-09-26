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

        int ringerMode =
                audioManager.getRingerMode();

        int vibrationSetting =
                audioManager.getVibrateSetting(
                        AudioManager.VIBRATE_TYPE_RINGER
                );

        boolean isQuiet =
                ringerMode == AudioManager.RINGER_MODE_SILENT
                && vibrationSetting == AudioManager.VIBRATE_SETTING_ON;

        if (isQuiet) {

            audioManager.setRingerMode(
                    AudioManager.RINGER_MODE_NORMAL
            );

            audioManager.setVibrateSetting(
                    AudioManager.VIBRATE_TYPE_RINGER,
                    AudioManager.VIBRATE_SETTING_OFF
            );
        }

        finish();
    }
}
