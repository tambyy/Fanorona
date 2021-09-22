package com.tambyy.fanoronaakalana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tambyy.fanoronaakalana.config.Theme;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.engine.EngineActionMoveSelectedPiece;
import com.tambyy.fanoronaakalana.engine.EngineActionSelectPiece;
import com.tambyy.fanoronaakalana.engine.EngineActionStopMove;
import com.tambyy.fanoronaakalana.graphics.anim.item.ProgressBarAnimation;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.utils.EngineActionsHistoryManager;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.game_akalana)
    AkalanaView akalanaView;

    @BindView(R.id.game_history_prev)
    ImageButton imageButtonGameHistoryPrev;

    @BindView(R.id.game_history_next)
    ImageButton imageButtonGameHistoryNext;

    @BindView(R.id.game_stop_moves_sequence)
    ImageButton imageButtonGameStopMovesSequence;

    @BindView(R.id.game_save)
    ImageButton imageButtonGameSave;

    @BindView(R.id.game_ai_level_text)
    TextView textViewAiLevel;

    @BindView(R.id.game_ai_thinking_progress)
    ProgressBar progressBarAiThinking;

    public static final String EXTRA_GAME_RESUME               = "EXTRA_GAME_RESUME";
    public static final String EXTRA_GAME_RESUME_HISTORY_INDEX = "EXTRA_GAME_RESUME_HISTORY_INDEX";

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    /**
     * Theme
     */
    private ThemeManager themeManager;

    /**
     * Fanoroa Engine
     */
    private final Engine engine = new Engine(1);

    /**
     * Is this a game against an AI?
     */
    private boolean vsAi = false;

    /**
     * if it is a game against an AI,
     * is the AI black or white?
     */
    private boolean aiBlack = false;

    /**
     * AI pondering
     */
    private int aiMaxReflexionTime = 0;

    /**
     * AI pondering
     */
    private boolean ponder = false;

    /**
     * We need to have the begin time
     * to determine at which time
     * each action on the akalana
     * has been made
     */
    private long beginTime;

    /**
     * To manage move do and undo
     * also game resume when we go back to OptionActivity
     * > See: onBackPressed
     */
    private EngineActionsHistoryManager history = new EngineActionsHistoryManager();


    // Sounds


    private MediaPlayer putPieceSound;
    private MediaPlayer movePieceSound;
    private MediaPlayer stopMoveSound;
    private MediaPlayer selectPieceSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        this.preferenceManager = PreferenceManager.getInstance(this);
        this.themeManager = ThemeManager.getInstance(this);

        // For this activity
        // we need to keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setAiThinking(false);
        loadSoundEffects();
        configureAkalana();
        loadPreferences();
        intentFromOptionActivity(savedInstanceState);

        beginTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // stop ponder and evaluate process
        if (evaluateProcess != null || ponderProcess != null) {
            engine.stopSearch(0);

            try {
                if (evaluateProcess != null) {
                    evaluateProcess.get();
                } else {
                    ponderProcess.get();
                }
            } catch (ExecutionException | InterruptedException ignored) {}
        }

        engine.terminate();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();

        // save last game history
        intent.putExtra(
                OptionActivity.GAME_RESUME_HISTORY_VALUE_CODE,
                EngineActionsConverter.engineActionsToString(new ArrayList<>(history.getHistory()), true)
        );
        intent.putExtra(OptionActivity.GAME_RESUME_HISTORY_INDEX_VALUE_CODE, history.getHistoryIndex());

        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // save last game history
        savedInstanceState.putString(
                OptionActivity.GAME_RESUME_HISTORY_VALUE_CODE,
                EngineActionsConverter.engineActionsToString(new ArrayList<>(history.getHistory()), true)
        );
        savedInstanceState.putInt(OptionActivity.GAME_RESUME_HISTORY_INDEX_VALUE_CODE, history.getHistoryIndex());

        // ...
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // restore last game history
        history.restoreHistory(
            EngineActionsConverter.stringToEngineActions(savedInstanceState.getString(OptionActivity.GAME_RESUME_HISTORY_VALUE_CODE)),
            savedInstanceState.getInt(OptionActivity.GAME_RESUME_HISTORY_INDEX_VALUE_CODE)
        );
        setEngineConfigFromHistory();

        if (history.getHistory().size() > 0) {
            beginTime = System.currentTimeMillis() - history.getHistory().get(history.getHistory().size() - 1).getTime();
        }
        // ...
    }

    /**
     *
     */
    private void loadSoundEffects() {
        movePieceSound = MediaPlayer.create(this, R.raw.move_piece);
        putPieceSound = MediaPlayer.create(this, R.raw.put_piece);
        stopMoveSound = MediaPlayer.create(this, R.raw.stop_move);
        selectPieceSound = MediaPlayer.create(this, R.raw.select_piece);
    }

    /**
     * Configure Akanala view
     */
    private void configureAkalana() {
        akalanaView.setEngine(engine);
        akalanaView.addEngineActionListener(engineActionListenerPushHistory);
        akalanaView.addEngineActionListener(engineActionListenerSoundEffect);
        akalanaView.addMovesSequenceOverListener(movesSequenceOverListener);
    }

    /**
     *
     */
    private void startAnimateAiThinking() {
        /*ProgressBarAnimation anim = new ProgressBarAnimation(progressBarAiThinking, 0, progressBarAiThinking.getMax());
        anim.setDuration(aiMaxReflexionTime);
        progressBarAiThinking.startAnimation(anim);*/
    }

    /**
     *
     */
    private void stopAnimateAiThinking() {
        /*progressBarAiThinking.clearAnimation();
        progressBarAiThinking.setProgress(progressBarAiThinking.getMax());*/
    }

    /**
     * Retrieve intent from OptionActivity
     * We'll get:
     * > Game config
     * > AI config (if match against AI)
     * > Game resume (if exists)
     * > Edition config (if exists)
     */
    private void intentFromOptionActivity(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            // A. CLASSIC CONFIG
            checkOptionActivityGameConfig(bundle);

            // B. IA
            checkOptionActivityAIConfig(bundle);

            if (savedInstanceState == null) {
                // C. CHECK IF THERE'RE A CONFIG FROM EDITION
                checkOptionActivityEditionConfig(bundle);

                // D. CHECK IF WE MUST RESUME PREVIOUS GAME
                if (!checkOptionActivityGameResumeConfig(bundle)) {
                    akalanaView.initConfigFromEngine();
                }
            }
        }
    }

    /**
     *
     * @param bundle
     */
    private void checkOptionActivityGameConfig(Bundle bundle) {
        // 1. Check game mode
        // 0: Mode Riatra
        // 1: Mode vela (black)
        // 2: Mode vela (white)
        engine.setMode(bundle.getInt(OptionActivity.EXTRA_GAME_MODE_CODE));

        // 2. Check move first
        // 0: black opponent
        // 1: white opponent
        engine.setFirstPlayerBlack(bundle.getInt(OptionActivity.EXTRA_GAME_MOVE_FIRST_CODE) == 0);

        // 3. Check against
        // 0: human
        // 1: black AI
        // 2: white AI
        int against = bundle.getInt(OptionActivity.EXTRA_GAME_AGAINST_CODE);
        if (against == 0) {
            vsAi = false;
        } else {
            vsAi = true;
            aiBlack = against == 1;
        }
    }

    /**
     * Retrieve IA config
     * @param bundle
     */
    private void checkOptionActivityAIConfig(Bundle bundle) {
        // IA.1. Check IA level
        // between 1 and 12
        int searchDepth = bundle.getInt(OptionActivity.EXTRA_GAME_AI_LEVEL_CODE);
        engine.setSearchDepth(0, searchDepth);
        textViewAiLevel.setText(searchDepth + "");

        // IA.2. Check max time evaluation
        // between 1 and 20
        aiMaxReflexionTime = bundle.getInt(OptionActivity.EXTRA_GAME_AI_MAX_SEARCH_TIME_CODE);
        engine.setSearchMaxTime(0, aiMaxReflexionTime);

        // IA.3. Check IA ponder
        // 0: yes
        // 1: no
        ponder = bundle.getInt(OptionActivity.EXTRA_GAME_AI_PONDER_CODE) == 0;
    }

    /**
     * check if this is a game config
     * from edition
     * @param bundle
     */
    private boolean checkOptionActivityEditionConfig(Bundle bundle) {
        if (bundle.containsKey(OptionActivity.EXTRA_EDITION_BLACK_CONFIG) && bundle.containsKey(OptionActivity.EXTRA_EDITION_WHITE_CONFIG)) {
            history.clearHistory();

            // retrieve black and white pawns positions
            engine.setPositions(
                    EngineActionsConverter.stringToPositions(bundle.getString(OptionActivity.EXTRA_EDITION_BLACK_CONFIG)),
                    EngineActionsConverter.stringToPositions(bundle.getString(OptionActivity.EXTRA_EDITION_WHITE_CONFIG))
            );

            return true;
        }

        return false;
    }

    /**
     * check if we have a game to resume
     * @param bundle
     */
    private boolean checkOptionActivityGameResumeConfig(Bundle bundle) {
        if (bundle.containsKey(OptionActivity.EXTRA_GAME_RESUME)) {
            // last game moves history
            List<EngineAction> actions = EngineActionsConverter.stringToEngineActions(bundle.getString(OptionActivity.EXTRA_GAME_RESUME));
            // last game history index
            int historyIndex = bundle.containsKey(OptionActivity.EXTRA_GAME_RESUME_HISTORY_INDEX) ?
                    bundle.getInt(OptionActivity.EXTRA_GAME_RESUME_HISTORY_INDEX) : actions.size();
            // restore history
            history.restoreHistory(actions, historyIndex);

            setEngineConfigFromHistory();

            beginTime = System.currentTimeMillis() - actions.get(actions.size() - 1).getTime();

            return true;
        }

        return false;
    }

    /**
     *
     * @param aiThinking
     */
    private void setAiThinking(boolean aiThinking) {
        imageButtonGameHistoryPrev.setEnabled(!aiThinking);
        imageButtonGameHistoryNext.setEnabled(!aiThinking);
        imageButtonGameStopMovesSequence.setEnabled(!aiThinking);
        imageButtonGameSave.setEnabled(!aiThinking);
    }

    /**
     *
     */
    private class EvaluateProcess extends AsyncTask<Void, Void, Engine.PieceMoveSession> {

        @Override
        protected void onPreExecute() {
            akalanaView.setTouchable(false);
            setAiThinking(true);
            startAnimateAiThinking();

            // before making search
            // stop AI pondering
            if (ponderProcess != null) {
                try {
                    engine.stopSearch(0);
                    ponderProcess.get();
                } catch (Exception ignored) {}
            }
        }

        @Override
        protected Engine.PieceMoveSession doInBackground(Void... arg0) {

            // increase thread priority
            // for better performance
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND + android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);

            // make search
            return engine.search(0);
        }

        @Override
        protected void onPostExecute(Engine.PieceMoveSession result) {

            // Just for Log :D :D :D

            CharSequence s = engine.getLastSearchDepth(0) + " - " +
                    engine.getLastNodesCount(0) + " nd - " +
                    engine.getLastSearchTime(0) + " ms - " +
                    (engine.getLastNodesCount(0) / Math.max(1, engine.getLastSearchTime(0))) + " nd/ms";

            Log.d("AKALANA", s.toString());

            // Anim AI search result moves

            akalanaView.animEngineActions(movesSequenceToEngineActions(!engine.currentBlack(), result), () -> {
                akalanaView.setTouchable(true);
                setAiThinking(false);
            });
            evaluateProcess = null;
            stopAnimateAiThinking();
        }

    };

    /**
     *
     */
    private void setEngineConfigFromHistory() {
        history.applyToEngine(engine);

        // draw current state
        akalanaView.updateConfigFromEngine();
    }

    /**
     * Evaluate process for AI
     */
    private EvaluateProcess evaluateProcess = null;


    /**
     * When human is thinking or moving piece
     * we let the AI to pondering
     * It will allow to the AI to think faster during his next move
     *
     * After human move, the ponder process will be stopped
     * to toggle to the evaluate process
     */
    private class PonderProcess extends AsyncTask<Void, Void, Void> {

        /**
         * There're no result to get
         * the AI just ponder
         *
         * @param arg0
         * @return
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            engine.ponder(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ponderProcess = null;
        }
    };

    /**
     * Ponder process for AI
     */
    private PonderProcess ponderProcess = null;


    /**
     * After each engine action
     * we register this action in a actions history
     * to alloq player to undo his move
     */
    private final AkalanaView.EngineActionListener engineActionListenerPushHistory = action -> {
        // register action in history
        action.setTime(System.currentTimeMillis() - beginTime);
        history.addHistory(action);
    };


    /**
     *
     */
    private final AkalanaView.EngineActionListener engineActionListenerSoundEffect = action -> {
        if (action instanceof EngineActionStopMove) {
            stopMoveSound.start();
        } else if (action instanceof EngineActionSelectPiece) {
            selectPieceSound.start();
        } else if (action instanceof EngineActionMoveSelectedPiece) {
            movePieceSound.start();
            new Handler().postDelayed(() -> {
                putPieceSound.start();
            }, 130);
        }
    };

    /**
     * this will be call after each engine action
     * (init, piece selection, move, move sequence stop)
     */
    private final AkalanaView.MovesSequenceOverListener movesSequenceOverListener = () -> {
        // check if game is over

        if (engine.gameOver()) {

            // @todo make dialog box
            // to show who wins
            // and give proposition
            // if human would replay game
            // or save game
            // or toggle to vela mode

            if (!engine.winner()) {
                Toast.makeText(this, "Vous pouvez faire mieux ;)!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Félicitations, vous avez gagné!", Toast.LENGTH_SHORT).show();
            }

        } else {

            // only for game against AI

            if (vsAi) {

                // when it's the AI's turn
                // make search

                if (engine.currentBlack() == aiBlack) {
                    evaluateProcess = new EvaluateProcess();
                    evaluateProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                // when it's the human's turn
                // make ponder

                } else if (ponder && ponderProcess == null) {
                    ponderProcess = new PonderProcess();
                    ponderProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        }
    };

    /**
     * Convert a moves sequences object
     * to engine actions list
     * @param movesSequence
     * @return
     */
    private List<EngineAction> movesSequenceToEngineActions(boolean black, Engine.PieceMoveSession movesSequence) {
        List<EngineAction> actions = new ArrayList<>();

        actions.add(new EngineActionSelectPiece(black, movesSequence.originPosition));
        for (Engine.Move move: movesSequence.moves) {
            actions.add(new EngineActionMoveSelectedPiece(black, move));
        }
        actions.add(new EngineActionStopMove(black));

        return actions;
    }

    /**
     * Stop moves sequence
     */
    public void stopMovesSequence(View view) {
        akalanaView.stopMovesSequence();
    }

    /**
     * Undo
     * Move to previous action
     */
    public void prevHistory(View view) {
        if (history.prevHistory(vsAi, aiBlack))
            setEngineConfigFromHistory();
    }

    /**
     * Do
     * move to next action
     */
    public void nextHistory(View view) {
        if (history.nextHistory(vsAi, aiBlack))
            setEngineConfigFromHistory();
    }

    /**
     *
     * @param view
     */
    public void replay(View view) {
        history.backwardHistory();
        setEngineConfigFromHistory();
        beginTime = System.currentTimeMillis();
    }

    /**
     *
     */
    public void launchSavedGamesActivity(View view) {
        Intent intent = new Intent(this, SavedGamesActivity.class);

        intent.putExtra(EXTRA_GAME_RESUME, EngineActionsConverter.engineActionsToString(new ArrayList<>(history.getHistory()), true));
        intent.putExtra(EXTRA_GAME_RESUME_HISTORY_INDEX, history.getHistoryIndex());

        startActivity(intent);
    }

    /**
     *
     */
    private void loadPreferences() {

        // Theme

        themeManager.getTheme(preferenceManager.get(ThemeManager.PREF_THEME, 0l), theme -> {
            if (theme != null) {
                akalanaView.setTheme(theme);
            }
        });

        // Sound volume
        float volume = Math.min((float) preferenceManager.get(Constants.PREF_SETTING_SOUND_VOLUME, 10) / 10f, 1f);

        movePieceSound.setVolume(volume * 0.05f, volume * 0.05f);
        putPieceSound.setVolume(volume, volume);
        stopMoveSound.setVolume(volume, volume);
        selectPieceSound.setVolume(volume, volume * 0.2f);
    }

    /**
     *
     */
    private void savePreferences() {
    }
}