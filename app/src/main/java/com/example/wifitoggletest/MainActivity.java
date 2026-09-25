package com.example.wifitoggletest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        result = new TextView(this);
        result.setTextSize(20);
        result.setText("Wi-Fi test");

        Button button = new Button(this);
        button.setText("OPEN WI-FI CONTROL");

        button.setOnClickListener(v -> {

            try {
                Intent intent =
                        new Intent(Settings.Panel.ACTION_WIFI);

                startActivity(intent);

                result.setText(
                        "Wi-Fi control opened.\n\n" +
                        "Check if you can turn Wi-Fi ON/OFF there."
                );

            } catch (Exception e) {

                Intent intent =
                        new Intent(Settings.ACTION_WIFI_SETTINGS);

                startActivity(intent);

                result.setText(
                        "Opened Wi-Fi settings."
                );
            }
        });

        layout.addView(result);
        layout.addView(button);

        setContentView(layout);
    }
}
