package com.tambyy.fanoronaakalana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.tambyy.fanoronaakalana.config.Theme;
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

import java.util.List;
import java.util.Map;

public class SavedGameReviewActivity extends AppCompatActivity {

    private static final int ANIM_INTERVAL = 250;
    public static final String SAVED_GAME_REVIEW_TIME_CODE = "SAVED_GAME_REVIEW_TIME_CODE";
    public static final String SAVED_GAME_REVIEW_PAUSED_CODE = "SAVED_GAME_REVIEW_PAUSED_CODE";
    public static final String SAVED_GAME_REVIEW_SPEED_CODE = "SAVED_GAME_REVIEW_SPEED_CODE";

    @BindView(R.id.review_akalana)
    AkalanaView akalanaView;

    @BindView(R.id.review_progress)
    SeekBarWithMarkers seekBarReviewProgress;

    @BindView(R.id.review_pause)
    ImageButton imageButtonReviewPause;

    @BindView(R.id.review_play)
    ImageButton imageButtonReviewPlay;

    @BindView(R.id.review_speed)
    ImageButton imageButtonReviewSpeed;

    @BindView(R.id.review_real_time)
    ImageButton imageButtonReviewRealTime;

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
    private List<EngineAction> reviewActions;

    /**
     *
     */
    private Engine engine = new Engine(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_game_review);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.database = FanoronaDatabase.getInstance(this);
        this.preferenceManager = PreferenceManager.getInstance(this);
        this.themeManager = ThemeManager.getInstance(this);
        loadPreferences();

        intentFromSavedGamesActivity();

        configureProgressBar();
        configureAkalana();

        registerForContextMenu(imageButtonReviewSpeed);

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
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        reviewPaused = savedInstanceState.getBoolean(SAVED_GAME_REVIEW_PAUSED_CODE);
        akalanaView.setAnimationSpeed(savedInstanceState.getFloat(SAVED_GAME_REVIEW_SPEED_CODE));
        gotoReview(savedInstanceState.getInt(SAVED_GAME_REVIEW_TIME_CODE));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review_speed_menu, menu);
    }

    /**
     * We implement a context menu
     * on speed button
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.review_speed_x_025:
                akalanaView.setAnimationSpeed(0.25f);
                return true;
            case R.id.review_speed_x_050:
                akalanaView.setAnimationSpeed(0.5f);
                return true;
            case R.id.review_speed_x_075:
                akalanaView.setAnimationSpeed(0.75f);
                return true;
            case R.id.review_speed_x_100:
                akalanaView.setAnimationSpeed(1f);
                return true;
            case R.id.review_speed_x_125:
                akalanaView.setAnimationSpeed(1.25f);
                return true;
            case R.id.review_speed_x_150:
                akalanaView.setAnimationSpeed(1.5f);
                return true;
            case R.id.review_speed_x_175:
                akalanaView.setAnimationSpeed(1.75f);
                return true;
            case R.id.review_speed_x_200:
                akalanaView.setAnimationSpeed(2f);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

                if (reviewActions.get(reviewActions.size() - 1).getTime() > 0) {
                    imageButtonReviewRealTime.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     *
     */
    private void configureProgressBar() {

        // set seekbar background color
        seekBarReviewProgress.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        seekBarReviewProgress.getThumb().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
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
     * Toggle play and pause button
     * @param paused
     */
    private void setReviewPaused(boolean paused) {
        reviewPaused = paused;

        if (paused) {
            imageButtonReviewPause.setVisibility(View.GONE);
            imageButtonReviewPlay.setVisibility(View.VISIBLE);
        } else {
            imageButtonReviewPause.setVisibility(View.VISIBLE);
            imageButtonReviewPlay.setVisibility(View.GONE);
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
                progressTime = (int) (getReviewDelay() * y);
                int progress = seekBarReviewProgress.getProgress();
                int newProgress = (int) (y * seekBarReviewProgress.getMax());

                if (progress != newProgress) {
                    seekBarReviewProgress.setProgress(newProgress);
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
        replayReview();
    }

    /**
     * On click on play button
     * Play review
     */
    public void playReview(View view) {
        akalanaView.startAnimation();
        setReviewPaused(false);
    }

    /**
     * On click on pause button
     * Pause review
     */
    public void pauseReview(View view) {
        akalanaView.pauseAnimation();
        setReviewPaused(true);
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
     * On click on '<' button
     */
    public void prevReview(View view) {
        gotoReview(progressTime - ANIM_INTERVAL);
        setReviewPaused(true);
    }

    /**
     * On click on '>' button
     */
    public void nextReview(View view) {
        gotoReview(progressTime + ANIM_INTERVAL);
        setReviewPaused(true);
    }

    /**
     * On click on speed button
     */
    public void showReviewSpeedContextMenu(View view) {
        view.showContextMenu();
    }

    /**
     *
     * @param view
     */
    public void toggleRealTime(View view) {
        realTime = !realTime;

        int reviewDelay = getReviewDelay();
        int seekBarMax = reviewDelay / 80;
        seekBarReviewProgress.setMax(seekBarMax);

        if (realTime) {
            seekBarReviewProgress.setMarkersPosition(reviewActions.stream().mapToInt(engineAction -> {
                return (int) (engineAction.getTime() * seekBarMax * 2) / (reviewDelay * 3);
            }).toArray());
        } else {
            seekBarReviewProgress.setMarkersPosition(new int[]{});
        }

        replayReview();
    }

    /**
     *
     */
    private void loadPreferences() {
        Theme theme = themeManager.getTheme(preferenceManager.get(ThemeManager.PREF_THEME, 0l));
        if (theme != null) {
            akalanaView.setTheme(theme);
        }
    }
}