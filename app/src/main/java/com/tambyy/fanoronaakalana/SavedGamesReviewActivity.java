package com.tambyy.fanoronaakalana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.adapter.SavedGameBreadcrumbFolderAdapter;
import com.tambyy.fanoronaakalana.adapter.SavedGameReviewSpeedAdapter;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.graphics.anim.item.TimeoutAnimation;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.graphics.anim.Animation;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.LinearInterpolator;
import com.tambyy.fanoronaakalana.graphics.customview.SeekBarWithMarkers;
import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedGamesReviewActivity extends AppCompatActivity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private static final int ANIM_INTERVAL = 300;
    public static final String SAVED_GAME_REVIEW_TIME_CODE = "SAVED_GAME_REVIEW_TIME_CODE";
    public static final String SAVED_GAME_REVIEW_PAUSED_CODE = "SAVED_GAME_REVIEW_PAUSED_CODE";
    public static final String SAVED_GAME_REVIEW_SPEED_CODE = "SAVED_GAME_REVIEW_SPEED_CODE";
    public static final String SAVED_GAME_REVIEW_REALTIME_CODE = "SAVED_GAME_REVIEW_REALTIME_CODE";

    @BindView(R.id.review_akalana)
    AkalanaView akalanaView;

    @BindView(R.id.review_progress)
    SeekBarWithMarkers seekBarReviewProgress;

    @BindView(R.id.review_name)
    TextView textViewSavedGameName;

    @BindView(R.id.review_replay)
    ImageButton imageButtonReviewReplay;

    @BindView(R.id.review_pause)
    ImageButton imageButtonReviewPause;

    @BindView(R.id.review_play)
    ImageButton imageButtonReviewPlay;

    @BindView(R.id.review_speed)
    ImageButton imageButtonReviewSpeed;

    @BindView(R.id.review_real_time)
    ImageButton imageButtonReviewRealTime;

    @BindView(R.id.saved_game_review_overlay)
    FrameLayout linearLayoutSavedGameReviewOverlay;

    @BindView(R.id.saved_game_review_overlay_content)
    RelativeLayout linearLayoutSavedGameReviewOverlayContent;

    @BindView(R.id.saved_game_review_speeds)
    RecyclerView recyclerViewSavedGameReviewSpeeds;

    SavedGameReviewSpeedAdapter savedGameReviewSpeedAdapter;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    /**
     * Theme
     */
    private ThemeManager themeManager;

    /**
     * Database
     */
    private FanoronaDatabase database;

    /**
     *
     */
    private Game game;

    /**
     *
     */
    private boolean reviewPaused = false;

    /**
     *
     */
    private boolean realTime = false;

    /**
     *
     */
    private int progressTime = 0;

    /**
     *
     */
    private boolean controlsLayoutShown = false;

    /**
     *
     */
    private List<EngineAction> reviewActions;

    /**
     *
     */
    private Engine engine = new Engine(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games_review);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.database = FanoronaDatabase.getInstance(this);
        this.preferenceManager = PreferenceManager.getInstance(this);
        this.themeManager = ThemeManager.getInstance(this);

        intentFromSavedGamesActivity();

        configureProgressBar();
        configureAkalana();
        configureSpeeds();

        loadPreferences();
        // registerForContextMenu(imageButtonReviewSpeed);

        // start review
        replayReview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        engine.terminate();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(SAVED_GAME_REVIEW_TIME_CODE, progressTime);
        savedInstanceState.putFloat(SAVED_GAME_REVIEW_SPEED_CODE, akalanaView.getAnimationSpeed());
        savedInstanceState.putBoolean(SAVED_GAME_REVIEW_PAUSED_CODE, reviewPaused);
        savedInstanceState.putBoolean(SAVED_GAME_REVIEW_REALTIME_CODE, realTime);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        setRealTime(savedInstanceState.getBoolean(SAVED_GAME_REVIEW_REALTIME_CODE));
        setReviewPaused(savedInstanceState.getBoolean(SAVED_GAME_REVIEW_PAUSED_CODE));
        akalanaView.setAnimationSpeed(savedInstanceState.getFloat(SAVED_GAME_REVIEW_SPEED_CODE));
        gotoReview(savedInstanceState.getInt(SAVED_GAME_REVIEW_TIME_CODE));
    }

    @Override
    public void onBackPressed() {
        // if there're selected items
        // we unselect items
        if (speedsShown) {
            hideSpeeds();
        } else {
            // else we go back to the parent activity
            super.onBackPressed();
        }
    }

    public void setAnimationSpeed(float speed) {
        akalanaView.setAnimationSpeed(speed);
        preferenceManager.put(Constants.PREF_SAVED_GAMES_REVIEW_SPEED, speed);
        hideSpeeds();
    }

    /**
     *
     */
    private void intentFromSavedGamesActivity() {
        Bundle bundle = getIntent().getExtras();

        // retrieve saved game from database
        // retrieve saved game config
        if (bundle != null) {
            long gameId = bundle.getLong(SavedGamesActivity.EXTRA_GAME_ID);
            game = this.database.gameDao().getGame(gameId);

            if (game != null) {
                reviewActions = EngineActionsConverter.stringToEngineActions(game.getConfigs());
                seekBarReviewProgress.setMax(reviewActions.size() * 3);

                if (reviewActions.get(reviewActions.size() - 1).getTime() == 0) {
                    imageButtonReviewRealTime.setVisibility(View.GONE);
                }

                textViewSavedGameName.setText(game.getName());
            }
        }
    }

    /**
     *
     */
    private void configureProgressBar() {

        // set seekbar background color
        seekBarReviewProgress.getProgressDrawable().setColorFilter(Color.rgb(255, 194, 0), android.graphics.PorterDuff.Mode.SRC_IN);
        seekBarReviewProgress.getThumb().setColorFilter(Color.rgb(255, 170, 0), android.graphics.PorterDuff.Mode.SRC_IN);
        seekBarReviewProgress.setMarkersColor(Color.argb(200, 255, 230, 0));

        // on seekbar change
        // go to n time view
        seekBarReviewProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // animation is paused temporarily
                // until seekbar touch tracking is stopped
                if (fromUser) {
                    gotoReview((getReviewDelay() * progress) / seekBar.getMax());
                    showControlsLayout();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // re animate only
                // once touch tracking stopped
                if (!reviewPaused) {
                    akalanaView.startAnimation();
                }
            }
        });

    }

    /**
     * init Akalana configuration
     */
    private void configureAkalana() {
        akalanaView.setMovablePawnsShown(false);
        akalanaView.setMovablePositionsShown(false);
        akalanaView.setRemovablePawnsShown(false);
        akalanaView.setTraveledPositionsShown(false);
        akalanaView.setEngine(engine);
    }

    /**
     */
    private void configureSpeeds() {
        List<Float> speeds = new ArrayList<>();

        for (int i = 1; i <= 8; ++i) {
            speeds.add(i * 0.25f);
        }

        savedGameReviewSpeedAdapter = new SavedGameReviewSpeedAdapter(this, speeds);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSavedGameReviewSpeeds.setAdapter(savedGameReviewSpeedAdapter);
        recyclerViewSavedGameReviewSpeeds.setLayoutManager(layoutManager);
    }

    private boolean speedsShown = false;

    public void toggleSpeedsShown(View view) {
        if (speedsShown) {
            hideSpeeds();
        } else {
            showSpeeds();
        }
    }

    private void showSpeeds() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(recyclerViewSavedGameReviewSpeeds, "translationX", 0, -recyclerViewSavedGameReviewSpeeds.getWidth());
        anim.setDuration(300);
        anim.start();
        speedsShown = true;
    }

    private void hideSpeeds() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(recyclerViewSavedGameReviewSpeeds, "translationX", -recyclerViewSavedGameReviewSpeeds.getWidth(), 0);
        anim.setDuration(200);
        anim.start();
        speedsShown = false;
    }

    /**
     * Toggle play and pause button
     * @param paused
     */
    private void setReviewPaused(boolean paused) {
        reviewPaused = paused;

        if (paused) {
            android.view.animation.Animation hidePauseAnimation = AnimationUtils.loadAnimation(this, R.anim.hide_game_review_pause);
            imageButtonReviewPause.setAnimation(hidePauseAnimation);
            hidePauseAnimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @Override
                public void onAnimationStart(android.view.animation.Animation animation) {}
                @Override
                public void onAnimationRepeat(android.view.animation.Animation animation) {}

                @Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    imageButtonReviewPause.setVisibility(View.GONE);
                }
            });

            imageButtonReviewPlay.setVisibility(View.VISIBLE);
            android.view.animation.Animation showPlayAnimation = AnimationUtils.loadAnimation(this, R.anim.show_game_review_play);
            imageButtonReviewPlay.setAnimation(showPlayAnimation);
        } else {
            android.view.animation.Animation hidePlayAnimation = AnimationUtils.loadAnimation(this, R.anim.hide_game_review_play);
            imageButtonReviewPlay.setAnimation(hidePlayAnimation);
            hidePlayAnimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @Override
                public void onAnimationStart(android.view.animation.Animation animation) {}
                @Override
                public void onAnimationRepeat(android.view.animation.Animation animation) {}

                @Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    imageButtonReviewPlay.setVisibility(View.GONE);
                }
            });

            imageButtonReviewPause.setVisibility(View.VISIBLE);
            android.view.animation.Animation showPauseAnimation = AnimationUtils.loadAnimation(this, R.anim.show_game_review_pause);
            imageButtonReviewPause.setAnimation(showPauseAnimation);
        }
    }

    /**
     *
     * @return review delay
     */
    private int getReviewDelay() {
        return realTime ? (int) ((reviewActions.get(reviewActions.size() - 1).getTime() * 2) / 3) + 100
                        : ANIM_INTERVAL * reviewActions.size();
    }

    /**
     * Create an animation object
     * that will animate the progressBar
     * according to the time spent in the review
     *
     * @return progress animation
     */
    private Animation createProgressAnimation() {
        return new Animation(new LinearInterpolator()) {
            @Override
            public void advance(double y) {
                if (controlsLayoutShown || y == 1) {
                    progressTime = (int) (getReviewDelay() * y);
                    int progress = seekBarReviewProgress.getProgress();
                    int newProgress = (int) (y * seekBarReviewProgress.getMax());

                    if (progress != newProgress) {
                        runOnUiThread(() -> {
                            seekBarReviewProgress.setProgress(newProgress);
                        });
                    }
                }
            }
        };
    }

    /**
     *
     */
    private void reAnimate() {
        if (realTime) {
            boolean first = true;
            for (EngineAction action: reviewActions) {
                if (first) {
                    akalanaView.applyEngineAction(action);
                    first = !first;
                } else {
                    akalanaView.addAnimation(new TimeoutAnimation((int) ((action.getTime() * 2) / 3), animation -> {
                        akalanaView.applyEngineAction(action);
                    }));
                }
            }
        } else {
            akalanaView.animEngineActions(reviewActions, null, ANIM_INTERVAL);
        }

        Animation progressAnimation = createProgressAnimation();
        progressAnimation.setDelay(getReviewDelay());
        akalanaView.addAnimation(progressAnimation);
    }

    /**
     * Replay review
     */
    public void replayReview() {
        akalanaView.pauseAnimation(() -> {
            seekBarReviewProgress.setProgress(0);
            reAnimate();
            if (!reviewPaused) {
                akalanaView.startAnimation();
            }
        });
    }

    /**
     *
     */
    public void replayReview(View view) {
        if (controlsLayoutShown) {
            replayReview();
            hideControlsLayout();
        }
    }

    /**
     * On click on play button
     * Play review
     */
    public void playReview(View view) {
        if (controlsLayoutShown) {
            akalanaView.startAnimation();
            setReviewPaused(false);
            hideControlsLayout();
        }
    }

    /**
     * On click on pause button
     * Pause review
     */
    public void pauseReview(View view) {
        if (controlsLayoutShown) {
            akalanaView.pauseAnimation();
            setReviewPaused(true);
            showControlsLayout(false);
        }
    }

    /**
     * On click on progress bar
     * Go to given time
     */
    private void gotoReview(int time) {
        progressTime = time;
        akalanaView.pauseAnimation(() -> {
            reAnimate();
            akalanaView.goToTime(time);
        });
    }

    /**
     *
     * @param view
     */
    public void toggleRealTime(View view) {
        if (controlsLayoutShown) {
            setRealTime(!realTime);
            hideControlsLayout();
        }
    }

    private void setRealTime(boolean realTime) {
        this.realTime = realTime;

        int reviewDelay = getReviewDelay();
        int seekBarMax = reviewDelay / 80;
        seekBarReviewProgress.setMax(seekBarMax);

        if (realTime) {
            int[] arr = new int[10];
            int count = 0;
            for (EngineAction engineAction : reviewActions) {
                int i = (int) (engineAction.getTime() * seekBarMax * 2) / (reviewDelay * 3);
                if (arr.length == count) arr = Arrays.copyOf(arr, count * 2);
                arr[count++] = i;
            }
            arr = Arrays.copyOfRange(arr, 0, count);
            seekBarReviewProgress.setMarkersPosition(arr);
        } else {
            seekBarReviewProgress.setMarkersPosition(new int[]{});
        }

        replayReview();
    }

    public void toogleShowControlsLayout(View view) {
        if (controlsLayoutShown) {
            hideControlsLayout();
        } else {
            showControlsLayout();
        }
    }

    private Handler hideControlsLayoutHandler = null;
    private Runnable hideControlsLayoutRunnable = () -> hideControlsLayout();

    private void showControlsLayout() {
        showControlsLayout(true);
    }

    private void showControlsLayout(boolean hide) {
        controlsLayoutShown = true;
        linearLayoutSavedGameReviewOverlayContent.setVisibility(View.VISIBLE);
        linearLayoutSavedGameReviewOverlay.animate().alpha(1).setDuration(300);

        cancelHideControlsLayoutHandler();

        if (hide) {
            hideControlsLayoutHandler = new Handler();
            hideControlsLayoutHandler.postDelayed(hideControlsLayoutRunnable, 2000);
        }
    }

    private void hideControlsLayout() {
        controlsLayoutShown = false;
        linearLayoutSavedGameReviewOverlay.animate().setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!controlsLayoutShown)
                    linearLayoutSavedGameReviewOverlayContent.setVisibility(View.GONE);
            }
        }).alpha(0).setDuration(500);

        cancelHideControlsLayoutHandler();
    }

    private void cancelHideControlsLayoutHandler() {
        if (hideControlsLayoutHandler != null) {
            hideControlsLayoutHandler.removeCallbacks(hideControlsLayoutRunnable);
            hideControlsLayoutHandler = null;
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        akalanaView.setTheme(((AkalanaApplication) this.getApplication()).getAkalanaTheme());

        float speed = preferenceManager.get(Constants.PREF_SAVED_GAMES_REVIEW_SPEED, 1f);
        setAnimationSpeed(speed);
        savedGameReviewSpeedAdapter.setSelectedPosition((int) (speed / 0.25f) - 1);
    }
}