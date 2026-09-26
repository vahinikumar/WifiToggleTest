package com.example.wifitoggletest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class DiagnosticActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String output =
                getIntent().getStringExtra("diagnostic");

        TextView text = new TextView(this);
        text.setText(output);
        text.setTextSize(16);
        text.setPadding(20, 20, 20, 20);

        Button button = new Button(this);
        button.setText("Show All");

        button.setOnClickListener(v ->
                text.setText(output));

        ScrollView scrollView = new ScrollView(this);

        android.widget.LinearLayout layout =
                new android.widget.LinearLayout(this);

        layout.setOrientation(
                android.widget.LinearLayout.VERTICAL);

        layout.addView(button);
        layout.addView(text);

        scrollView.addView(layout);

        setContentView(scrollView);
    }
}