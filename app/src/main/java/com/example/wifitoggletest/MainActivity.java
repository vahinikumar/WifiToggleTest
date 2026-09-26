package com.example.wifitoggletest;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);

        button.setText("WIFI");
        button.setTextSize(40);
        button.setTextColor(Color.WHITE);
        button.setGravity(Gravity.CENTER);

        button.setOnClickListener(v -> {

            WifiAccessibilityService.openNotificationPanel();

        });

        setContentView(button);
    }
}
