package com.example.wifitoggletest;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        result = new TextView(this);
        result.setTextSize(18);
        result.setPadding(30, 30, 30, 30);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(result);

        setContentView(scrollView);

        AudioManager audioManager =
                (AudioManager) getSystemService(AUDIO_SERVICE);

        result.setText(
                "BEFORE\n" +
                "Ringer Mode: " +
                getModeName(audioManager.getRingerMode()) +
                "\nVibration: " +
                getVibrationState(audioManager) +
                "\n\nTrying to turn vibration ON..."
        );

        try {

            audioManager.setVibrateSetting(
                    AudioManager.VIBRATE_TYPE_RINGER,
                    AudioManager.VIBRATE_SETTING_ON
            );

        } catch (Exception e) {

            result.append(
                    "\n\nERROR:\n" +
                    e.getClass().getSimpleName() +
                    "\n" +
                    e.getMessage()
            );

            return;
        }

        result.append(
                "\n\nAFTER\n" +
                "Ringer Mode: " +
                getModeName(audioManager.getRingerMode()) +
                "\nVibration: " +
                getVibrationState(audioManager)
        );
    }

    private String getModeName(int mode) {

        if (mode == AudioManager.RINGER_MODE_NORMAL) {
            return "NORMAL";
        }

        if (mode == AudioManager.RINGER_MODE_VIBRATE) {
            return "VIBRATE";
        }

        if (mode == AudioManager.RINGER_MODE_SILENT) {
            return "SILENT";
        }

        return "UNKNOWN";
    }

    private String getVibrationState(
            AudioManager audioManager) {

        int setting =
                audioManager.getVibrateSetting(
                        AudioManager.VIBRATE_TYPE_RINGER
                );

        if (setting == AudioManager.VIBRATE_SETTING_ON) {
            return "ON";
        }

        if (setting == AudioManager.VIBRATE_SETTING_OFF) {
            return "OFF";
        }

        if (setting == AudioManager.VIBRATE_SETTING_ONLY_SILENT) {
            return "ONLY_SILENT";
        }

        return "UNKNOWN";
    }
}
