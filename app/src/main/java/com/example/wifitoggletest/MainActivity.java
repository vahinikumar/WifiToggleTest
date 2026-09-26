package com.example.wifitoggletest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        result = new TextView(this);
        result.setTextSize(18);
        result.setPadding(30, 30, 30, 30);
        result.setText("Opening Control Center...");

        setContentView(result);

        boolean opened =
                WifiAccessibilityService.openNotificationPanel();

        if (opened) {

            result.setText(
                    "Control Center opened.\n\n" +
                    "Wait 2 seconds, then check the screen."
            );

            new Handler().postDelayed(() -> {

                String nodes =
                        WifiAccessibilityService.inspectWindows();

                result.setText(nodes);

            }, 2000);

        } else {

            result.setText(
                    "Accessibility Service is not connected."
            );
        }
    }
}
