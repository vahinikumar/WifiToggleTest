package com.example.wifitoggletest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.ViewGroup;

public class MainActivity extends Activity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        result = new TextView(this);
        result.setTextSize(16);
        result.setText("Wi-Fi diagnostic");

        Button openButton = new Button(this);
        openButton.setText("OPEN WI-FI CONTROL");

        Button showButton = new Button(this);
        showButton.setText("SHOW ALL CONTROLS");

        openButton.setOnClickListener(v -> {

            try {
                Intent intent =
                        new Intent(Settings.Panel.ACTION_WIFI);

                startActivity(intent);

            } catch (Exception e) {

                Intent intent =
                        new Intent(Settings.ACTION_WIFI_SETTINGS);

                startActivity(intent);
            }
        });

        showButton.setOnClickListener(v -> {

            String controls =
                    WifiAccessibilityService.getAllControls();

            if (controls.isEmpty()) {

                result.setText(
                        "No controls detected yet.\n\n" +
                        "First tap OPEN WI-FI CONTROL."
                );

            } else {

                result.setText(controls);
            }
        });

        layout.addView(
                result,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        layout.addView(openButton);
        layout.addView(showButton);

        setContentView(layout);
    }
}
