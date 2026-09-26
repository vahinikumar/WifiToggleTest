package com.example.wifitoggletest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        result.setTextSize(18);
        result.setText("Accessibility Control Center test");

        Button button = new Button(this);
        button.setText("OPEN CONTROL CENTER");

        button.setOnClickListener(v -> {

            boolean opened =
                    WifiAccessibilityService
                            .openNotificationPanel();

            if (opened) {

                result.setText(
                        "System panel opened."
                );

            } else {

                result.setText(
                        "Accessibility Service is not connected."
                );
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

        layout.addView(button);

        setContentView(layout);
    }
}
