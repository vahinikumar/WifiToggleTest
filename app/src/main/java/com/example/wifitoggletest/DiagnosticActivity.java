package com.example.wifitoggletest;

import android.app.Activity;
import android.os.Bundle;
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

        ScrollView scrollView =
                new ScrollView(this);

        scrollView.addView(text);

        setContentView(scrollView);
    }
}