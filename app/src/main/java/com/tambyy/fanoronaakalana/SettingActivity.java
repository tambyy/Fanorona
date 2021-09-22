package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    /**
     *
     */
    public void launchThemeActivity(View v) {
        Intent intent = new Intent(this, SettingThemeActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    public void launchSoundActivity(View v) {
        Intent intent = new Intent(this, SettingSoundActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    public void launchLocaleActivity(View v) {
        Intent intent = new Intent(this, SettingLocaleActivity.class);
        startActivity(intent);
    }

}