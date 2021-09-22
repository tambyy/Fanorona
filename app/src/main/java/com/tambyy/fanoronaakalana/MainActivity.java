package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.tambyy.fanoronaakalana.utils.LocaleManager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.app_title)
    LinearLayout linearLayoutAppTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LocaleManager.getInstance(this);

        animateSplashScreen();
        launchHomeActivity();
    }

    /**
     * We add here all animations
     * we should do before
     * we start the HomeActivity
     */
    private void animateSplashScreen() {
        Animation appTitleAnimation = AnimationUtils.loadAnimation(this, R.anim.app_title_appears_animation);
        linearLayoutAppTitle.setAnimation(appTitleAnimation);
    }

    /**
     * Launch home activity
     */
    private void launchHomeActivity() {
        // We wait for 1800ms before starting
        // the HomeActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, HomeActivity.class);
            // ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, appTitleTextview, "app_title_transition");
            startActivity(intent);
            finish();
        }, 1800);
    }
}