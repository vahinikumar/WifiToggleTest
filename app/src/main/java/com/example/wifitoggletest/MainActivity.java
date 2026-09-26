package com.example.wifitoggletest;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
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

        showState(
                audioManager,
                "BEFORE OPENING CONTROL CENTER"
        );

        WifiAccessibilityService.openNotificationPanel();

        new Handler().postDelayed(() -> {

            showState(
                    audioManager,
                    "AFTER 3 SECONDS"
            );

        }, 3000);
    }

    private void showState(
            AudioManager audioManager,
            String title) {

        int mode =
                audioManager.getRingerMode();

        String modeName;

        if (mode == AudioManager.RINGER_MODE_NORMAL) {
            modeName = "NORMAL";
        } else if (mode == AudioManager.RINGER_MODE_VIBRATE) {
            modeName = "VIBRATE";
        } else if (mode == AudioManager.RINGER_MODE_SILENT) {
            modeName = "SILENT";
        } else {
            modeName = "UNKNOWN";
        }

        boolean vibration =
                audioManager.getVibrateSetting(
                        AudioManager.VIBRATE_TYPE_RINGER
                ) == AudioManager.VIBRATE_SETTING_ON;

        result.append(
                "\n\n" +
                "===== " + title + " =====\n" +
                "Ringer Mode: " + modeName + "\n" +
                "Ringer Mode Number: " + mode + "\n" +
                "Vibration Setting: " +
                (vibration ? "ON" : "OFF") +
                "\n"
        );
    }
}
