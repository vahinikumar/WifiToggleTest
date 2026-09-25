package com.example.wifitoggletest;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private WifiManager wifiManager;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wifiManager =
                (WifiManager) getApplicationContext()
                        .getSystemService(WIFI_SERVICE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        result = new TextView(this);
        result.setTextSize(20);

        Button button = new Button(this);
        button.setText("TRY TO TOGGLE WI-FI");

        updateStatus();

        button.setOnClickListener(v -> {

            boolean currentState =
                    wifiManager.isWifiEnabled();

            boolean requestedState =
                    !currentState;

            boolean success =
                    wifiManager.setWifiEnabled(requestedState);

            result.setText(
                    "Wi-Fi before: " +
                    currentState +
                    "\n\nRequested: " +
                    requestedState +
                    "\n\nsetWifiEnabled() returned: " +
                    success
            );
        });

        layout.addView(result);
        layout.addView(button);

        setContentView(layout);
    }

    private void updateStatus() {

        boolean currentState =
                wifiManager.isWifiEnabled();

        result.setText(
                "Current Wi-Fi: " +
                (currentState ? "ON" : "OFF")
        );
    }
}
