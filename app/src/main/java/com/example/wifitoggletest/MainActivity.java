package com.example.wifitoggletest;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean quietMode =
                getSharedPreferences(
                        "ringer_state",
                        MODE_PRIVATE
                ).getBoolean("quiet_mode", false);

        if (quietMode) {

            // Currently quiet.
            // Next tap should request normal ringing.
            WifiAccessibilityService.openNotificationPanel(
                    "ring"
            );

        } else {

            // Currently normal ringing.
            // Next tap should request quiet mode.
            WifiAccessibilityService.openNotificationPanel(
                    "quiet"
            );
        }

        // Remember the state we requested.
        getSharedPreferences(
                "ringer_state",
                MODE_PRIVATE
        )
                .edit()
                .putBoolean(
                        "quiet_mode",
                        !quietMode
                )
                .apply();

        finish();
    }
}
