package com.example.wifitoggletest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =
                new Intent(Settings.ACTION_SOUND_SETTINGS);

        startActivity(intent);

        finish();
    }
}
