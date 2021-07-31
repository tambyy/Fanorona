package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     *
     */
    public void launchOptionActivity(View v) {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    public void launchEditionActivity(View v) {
        Intent intent = new Intent(this, EditionActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    public void launchSavedGamesActivity(View v) {
        Intent intent = new Intent(this, SavedGamesActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    public void launchThemeActivity(View v) {
        Intent intent = new Intent(this, ThemeActivity.class);
        startActivity(intent);
    }

}