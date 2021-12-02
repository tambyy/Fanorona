package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

public class OptionActivity extends AppCompatActivity {

    @BindView(R.id.option_game_mode_text)
    TextView textViewGameMode;

    @BindView(R.id.option_first_move_text)
    TextView textViewFirstMove;

    @BindView(R.id.option_play_against_text)
    TextView textViewPlayAgainst;

    @BindView(R.id.option_ai_level_text)
    TextView textViewAILevel;

    @BindView(R.id.option_ai_max_relexion_time_text)
    TextView textViewAIMaxReflexionTime;

    @BindView(R.id.option_ai_ponder_text)
    TextView textViewAIPonder;


    @BindView(R.id.game_option_play)
    Button buttonGameOptionPlay;

    @BindView(R.id.game_option_resume)
    Button buttonGameOptionResume;

    @BindView(R.id.game_option_re_edit)
    Button buttonGameOptionReEdit;

    @BindView(R.id.option_game_mode)
    CardView cardViewOptionGameMode;

    @BindView(R.id.option_first_move)
    CardView cardViewOptionMoveFirst;

    @BindView(R.id.option_ai_level)
    CardView cardViewOptionAiLevel;

    @BindView(R.id.option_ai_max_reflexion_time)
    CardView cardViewOptionAiMaxReflexionTime;

    @BindView(R.id.option_ai_ponder)
    CardView cardViewOptionAiPonder;

    @BindView(R.id.option_ai)
    LinearLayout linearLayoutOptionAi;

    private Map<Integer, String> game_mode_option_values;
    private Map<Integer, String> first_move_option_values;
    private Map<Integer, String> play_against_option_values;
    private Map<Integer, String> ai_ponder_values;

    private static final String PREF_OPTION_MODE = "OPTION_MODE";
    private static final String PREF_OPTION_MOVE_FIRST = "OPTION_MOVE_FIRST";
    private static final String PREF_OPTION_AGAISNT = "OPTION_AGAISNT";
    private static final String PREF_OPTION_AI_LEVEL = "OPTION_AI_LEVEL";
    private static final String PREF_OPTION_AI_MAX_SEARCH_TIME = "OPTION_AI_MAX_SEARCH_TIME";
    private static final String PREF_OPTION_AI_PONDER = "OPTION_AI_PONDER";

    public static final int GAME_MODE_CODE = 0;
    public static final int FIRST_MOVE_CODE = 1;
    public static final int PLAY_AGAINST_CODE = 2;
    public static final int AI_LEVEL_CODE = 3;
    public static final int AI_MAX_SEARCH_TIME_CODE = 4;
    public static final int AI_PONDER_CODE = 5;
    public static final int GAME_RESUME_CODE = 6;
    public static final int EDITION_OK = 7;

    public static final String GAME_MODE_VALUE_CODE = "GAME_MODE_VALUE_CODE";
    public static final String FIRST_MOVE_VALUE_CODE = "FIRST_MOVE_VALUE_CODE";
    public static final String PLAY_AGAINST_VALUE_CODE = "PLAY_AGAINST_VALUE_CODE";
    public static final String AI_LEVEL_VALUE_CODE = "AI_LEVEL_VALUE";
    public static final String AI_MAX_SEARCh_TIME_VALUE_CODE = "AI_MAX_SEARCH_TIME_VALUE";
    public static final String AI_PONDER_VALUE_CODE = "AI_PONDER_VALUE_CODE";
    public static final String GAME_RESUME_HISTORY_VALUE_CODE = "GAME_RESUME_CODE_VALUE_CODE";
    public static final String GAME_RESUME_HISTORY_INDEX_VALUE_CODE = "GAME_RESUME_HISTORY_INDEX_VALUE_CODE";
    public static final String GAME_RESUME_HISTORY_BEGIN_TIME_VALUE_CODE = "GAME_RESUME_HISTORY_BEGIN_TIME_VALUE_CODE";

    public static final String EDITION_BLACK_VALUE_CODE = "EDITION_BLACK_VALUE_CODE";
    public static final String EDITION_WHITE_VALUE_CODE = "EDITION_WHITE_VALUE_CODE";
    public static final String SAVED_GAMES_CONFIG_CODE = "SAVED_GAMES_CONFIG_CODE";

    /*
     * extras exchanged between OptionActivity and GameActivity
     * to initialize the state of the game in GameActivity
     */

    public static final String EXTRA_GAME_MODE_CODE               = "EXTRA_GAME_MODE_CODE";
    public static final String EXTRA_GAME_MOVE_FIRST_CODE         = "EXTRA_GAME_MOVE_FIRST_CODE";
    public static final String EXTRA_GAME_AGAINST_CODE            = "EXTRA_GAME_AGAINST_CODE";
    public static final String EXTRA_GAME_AI_LEVEL_CODE           = "EXTRA_GAME_AI_LEVEL_CODE";
    public static final String EXTRA_GAME_AI_MAX_SEARCH_TIME_CODE = "EXTRA_GAME_AI_MAX_SEARCH_TIME_CODE";
    public static final String EXTRA_GAME_AI_PONDER_CODE          = "EXTRA_GAME_AI_PONDER_CODE";

    /*
     * extras exchanged between OptionActivity and GameActivity
     * in order to save the previous state (history) of the game in GameActivity
     * and resume it when it is relaunched by clicking on Play
     */
    public static final String EXTRA_GAME_RESUME                  = "EXTRA_GAME_RESUME";
    public static final String EXTRA_GAME_RESUME_HISTORY_INDEX    = "EXTRA_GAME_RESUME_HISTORY_INDEX";

    /*
     * extras exchanged between OptionActivity and GameActivity
     * from EditionActivity
     * in order to init game state from config from EditionActivity
     */
    public static final String EXTRA_EDITION_BLACK_CONFIG = "EXTRA_EDITION_BLACK_CONFIG";
    public static final String EXTRA_EDITION_WHITE_CONFIG = "EXTRA_EDITION_WHITE_CONFIG";
    public static final String EXTRA_SAVED_GAMES_CONFIG = "EXTRA_SAVED_GAMES_CONFIG";

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    /**
     * Option Mode
     *
     * 0: Mode Riatra
     * 1: Mode vela black
     * 2: Mode vela white
     */
    private int optionMode = 0;

    /**
     * Option Move first
     *
     * 0: black
     * 1: white
     */
    private int optionMoveFirst = 0;

    /**
     * Option against
     *
     * 0: Human
     * 1: black AI
     * 2: White AI
     */
    private int optionAgaisnt = 0;

    /**
     * Option against
     *
     * 0: Human
     * 1: black AI
     * 2: White AI
     */
    private int optionAiLevel = 8;

    /**
     * Option against
     *
     * 0: Human
     * 1: black AI
     * 2: White AI
     */
    private int optionAiMaxReflexionTime = 5000;

    /**
     * Option AI Ponder
     *
     * 0: Yes
     * 1: No
     */
    private int optionAiPonder = 0;

    /**
     *
     */
    private String editionBlackConfig = null;

    /**
     *
     */
    private String editionWhiteConfig = null;

    /**
     *
     */
    private String savedGameConfig = null;

    /**
     *
     */
    private String gameResume = null;

    /**
     *
     */
    private int gameResumeHistoryIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        ButterKnife.bind(this);

        loadOptionsValues();

        this.preferenceManager = PreferenceManager.getInstance(this);
        loadPreferences();

        // check intent from EditionActivity
        intentFromEditionActivity();

        // check intent from SavedGamesActivity
        intentFromSavedGamesActivity();

        // check if we must continue last game
        checkLastGame();
    }

    /**
     *
     */
    private void intentFromEditionActivity() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(OptionActivity.EDITION_BLACK_VALUE_CODE) && bundle.containsKey(OptionActivity.EDITION_WHITE_VALUE_CODE)) {
                editionBlackConfig = bundle.getString(OptionActivity.EDITION_BLACK_VALUE_CODE);
                editionWhiteConfig = bundle.getString(OptionActivity.EDITION_WHITE_VALUE_CODE);

                // Check if we should automatically switch to vela mode
                Engine.Point[] blackPositions = EngineActionsConverter.stringToPositions(editionBlackConfig);
                Engine.Point[] whitePositions = EngineActionsConverter.stringToPositions(editionWhiteConfig);

                // Vela black
                if (blackPositions.length == 22 && whitePositions.length < 20) {
                    setOptionGameMode(Constants.OPTION_GAME_MODE_VELA_BLACK);

                    // White must move first
                    if (whitePositions.length == 5) {
                        setOptionFirstMove(Constants.OPTION_FIRST_MOVE_WHITE);
                    }
                    // Vela White
                } else if (blackPositions.length < 20 && whitePositions.length == 22) {
                    setOptionGameMode(Constants.OPTION_GAME_MODE_VELA_WHITE);

                    // Black must move first
                    if (blackPositions.length == 5) {
                        setOptionFirstMove(Constants.OPTION_FIRST_MOVE_BLACK);
                    }
                    // Riatra
                } else {
                    setOptionGameMode(Constants.OPTION_GAME_MODE_RIATRA);
                }

                buttonGameOptionReEdit.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * if we want to continue
     * a game from save
     */
    private void intentFromSavedGamesActivity() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(OptionActivity.SAVED_GAMES_CONFIG_CODE)) {
                savedGameConfig = bundle.getString(OptionActivity.SAVED_GAMES_CONFIG_CODE);

                // we cannot edit game mode and who move first
                // because it's already set in the game to continue config
                // so we hide these 2 buttons
                cardViewOptionGameMode.setVisibility(View.GONE);
                cardViewOptionMoveFirst.setVisibility(View.GONE);
            }
        }
    }

    /**
     * We show the resume button
     * if there are a last game played before we do a new game
     * @return
     */
    private boolean checkLastGame() {

        // PREF_LAST_GAME_CONFIG:
        // key for the last game config
        String gameConfig = preferenceManager.get(Constants.PREF_LAST_GAME_CONFIG, null);

        // if key is set in preference manager
        if (gameConfig != null) {
            gameResume = gameConfig;
            gameResumeHistoryIndex = preferenceManager.get(Constants.PREF_LAST_HISTORY_INDEX, 0);

            // show resume button
            buttonGameOptionResume.setVisibility(View.VISIBLE);

            return true;
        }

        return false;
    }

    /**
     *
     */
    public void launchOptionGameModeActivity(View v) {
        Intent intent = new Intent(this, OptionGameModeActivity.class);
        startActivityForResult(intent, GAME_MODE_CODE);
    }

    /**
     *
     */
    public void launchOptionFirstMoveActivity(View v) {
        Intent intent = new Intent(this, OptionFirstMoveActivity.class);
        startActivityForResult(intent, FIRST_MOVE_CODE);
    }

    /**
     *
     */
    public void launchOptionPlayAgainstActivity(View v) {
        Intent intent = new Intent(this, OptionPlayAgainstActivity.class);
        startActivityForResult(intent, PLAY_AGAINST_CODE);
    }

    /**
     *
     */
    public void launchOptionAiLevelActivity(View v) {
        Intent intent = new Intent(this, OptionAiLevelActivity.class);
        startActivityForResult(intent, AI_LEVEL_CODE);
    }

    /**
     *
     */
    public void launchOptionAiMaxSearchTimeActivity(View v) {
        Intent intent = new Intent(this, OptionAiMaxSearchTimeActivity.class);
        startActivityForResult(intent, AI_MAX_SEARCH_TIME_CODE);
    }

    /**
     *
     */
    public void launchOptionAiPonderActivity(View v) {
        Intent intent = new Intent(this, OptionAiPonderActivity.class);
        startActivityForResult(intent, AI_PONDER_CODE);
    }

    /**
     *
     */
    public void launchGameActivity(View v) {
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(EXTRA_GAME_MODE_CODE,               optionMode);
        intent.putExtra(EXTRA_GAME_MOVE_FIRST_CODE,         optionMoveFirst);
        intent.putExtra(EXTRA_GAME_AGAINST_CODE,            optionAgaisnt);
        intent.putExtra(EXTRA_GAME_AI_LEVEL_CODE,           optionAiLevel);
        intent.putExtra(EXTRA_GAME_AI_MAX_SEARCH_TIME_CODE, optionAiMaxReflexionTime);
        intent.putExtra(EXTRA_GAME_AI_PONDER_CODE,          optionAiPonder);

        // config from EditionActivity if exists
        if (editionBlackConfig != null && editionWhiteConfig != null) {
            intent.putExtra(EXTRA_EDITION_BLACK_CONFIG, editionBlackConfig);
            intent.putExtra(EXTRA_EDITION_WHITE_CONFIG, editionWhiteConfig);
        } else if (savedGameConfig != null) {
            intent.putExtra(EXTRA_SAVED_GAMES_CONFIG, savedGameConfig);
        }

        savePreferences();

        startActivityForResult(intent, GAME_RESUME_CODE);
    }

    /**
     *
     */
    public void resumeGameActivity(View v) {
        Intent intent = new Intent(this, GameActivity.class);

        if (gameResume != null) {
            intent.putExtra(EXTRA_GAME_RESUME,               gameResume);
            intent.putExtra(EXTRA_GAME_RESUME_HISTORY_INDEX, gameResumeHistoryIndex);
        }

        intent.putExtra(EXTRA_GAME_AGAINST_CODE,            optionAgaisnt);
        intent.putExtra(EXTRA_GAME_AI_LEVEL_CODE,           optionAiLevel);
        intent.putExtra(EXTRA_GAME_AI_MAX_SEARCH_TIME_CODE, optionAiMaxReflexionTime);
        intent.putExtra(EXTRA_GAME_AI_PONDER_CODE,          optionAiPonder);

        startActivityForResult(intent, GAME_RESUME_CODE);
    }

    /**
     *
     */
    public void resumeEditionActivity(View v) {
        Intent intent = new Intent(this, EditionActivity.class);

        intent.putExtra(EXTRA_EDITION_BLACK_CONFIG, editionBlackConfig);
        intent.putExtra(EXTRA_EDITION_WHITE_CONFIG, editionWhiteConfig);

        startActivity(intent);
        finish();
    }

    /**
     *
     * @param bundle
     */
    private void onOptionGameModeActivityResult(Bundle bundle) {
        setOptionGameMode(bundle.getInt(GAME_MODE_VALUE_CODE));
    }

    /**
     *
     * @param bundle
     */
    private void onOptionFirstMoveActivityResult(Bundle bundle) {
        setOptionFirstMove(bundle.getInt(FIRST_MOVE_VALUE_CODE));
    }

    /**
     *
     * @param bundle
     */
    private void onOptionPlayAgainstActivityResult(Bundle bundle) {
        setOptionPlayAgainst(bundle.getInt(PLAY_AGAINST_VALUE_CODE));
    }

    /**
     *
     * @param bundle
     */
    private void onOptionAiLevelActivityResult(Bundle bundle) {
        setOptionAILevel(bundle.getInt(AI_LEVEL_VALUE_CODE));
        // textViewGameOptionAiLevel.setText(optionAiLevel + "");
    }

    /**
     *
     * @param bundle
     */
    private void onOptionAiMaxSearchTimeActivityResult(Bundle bundle) {
        float value = bundle.getFloat(AI_MAX_SEARCh_TIME_VALUE_CODE);
        setOptionAIMaxReflexionTime((int) (1000 * value));
    }

    /**
     *
     * @param bundle
     */
    private void onOptionAiPonderActivityResult(Bundle bundle) {
        setOptionAIPonder(bundle.getInt(AI_PONDER_VALUE_CODE));
    }

    /**
     *
     * @param bundle
     */
    private void onGameActivityResult(Bundle bundle) {
        gameResume = bundle.getString(GAME_RESUME_HISTORY_VALUE_CODE);
        gameResumeHistoryIndex = bundle.getInt(GAME_RESUME_HISTORY_INDEX_VALUE_CODE);

        buttonGameOptionResume.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (requestCode == GAME_MODE_CODE) {
                onOptionGameModeActivityResult(bundle);
            } else if (requestCode == FIRST_MOVE_CODE) {
                onOptionFirstMoveActivityResult(bundle);
            } else if (requestCode == PLAY_AGAINST_CODE) {
                onOptionPlayAgainstActivityResult(bundle);
            } else if (requestCode == AI_LEVEL_CODE) {
                onOptionAiLevelActivityResult(bundle);
            } else if (requestCode == AI_MAX_SEARCH_TIME_CODE) {
                onOptionAiMaxSearchTimeActivityResult(bundle);
            } else if (requestCode == AI_PONDER_CODE) {
                onOptionAiPonderActivityResult(bundle);
            } else if (requestCode == GAME_RESUME_CODE) {
                onGameActivityResult(bundle);
            }
        }
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void setOptionGameMode(int mode) {
        optionMode = mode;
        textViewGameMode.setText(game_mode_option_values.get(optionMode));
    }

    private void setOptionFirstMove(int firstMove) {
        optionMoveFirst = firstMove;
        textViewFirstMove.setText(first_move_option_values.get(optionMoveFirst));
    }

    private void setOptionPlayAgainst(int playAgainst) {
        optionAgaisnt = playAgainst;
        textViewPlayAgainst.setText(play_against_option_values.get(optionAgaisnt));

        boolean vsAi = playAgainst == Constants.OPTION_PLAY_AGAINST_AI_WHITE || playAgainst == Constants.OPTION_PLAY_AGAINST_AI_BLACK;
        cardViewOptionAiLevel.setClickable(vsAi);
        cardViewOptionAiMaxReflexionTime.setClickable(vsAi);
        cardViewOptionAiPonder.setClickable(vsAi);
        linearLayoutOptionAi.setAlpha(vsAi ? 1f : 0.5f);
    }

    private void setOptionAILevel(int aiLevel) {
        optionAiLevel = aiLevel;
        textViewAILevel.setText(optionAiLevel + "");
    }

    private void setOptionAIMaxReflexionTime(int aiMaxReflexionTime) {
        optionAiMaxReflexionTime = aiMaxReflexionTime;
        textViewAIMaxReflexionTime.setText((optionAiMaxReflexionTime / 1000) + "s");
    }

    private void setOptionAIPonder(int aiPonder) {
        optionAiPonder = aiPonder;
        textViewAIPonder.setText(ai_ponder_values.get(optionAiPonder));
    }

    /**
     *
     */
    private void loadPreferences() {
        setOptionGameMode(preferenceManager.get(Constants.PREF_OPTION_GAME_MODE, Constants.OPTION_GAME_MODE_RIATRA));
        setOptionFirstMove(preferenceManager.get(Constants.PREF_OPTION_FIRST_MOVE, Constants.OPTION_FIRST_MOVE_BLACK));
        setOptionPlayAgainst(preferenceManager.get(Constants.PREF_OPTION_PLAY_AGAISNT, Constants.OPTION_PLAY_AGAINST_HUMAN));
        setOptionAILevel(preferenceManager.get(Constants.PREF_OPTION_AI_LEVEL, 8));
        setOptionAIMaxReflexionTime(preferenceManager.get(Constants.PREF_OPTION_AI_MAX_SEARCH_TIME, 5000));
        setOptionAIPonder(preferenceManager.get(Constants.PREF_OPTION_AI_PONDER, Constants.OPTION_AI_PONDER_YES));
    }

    /**
     *
     */
    private void savePreferences() {
        preferenceManager.put(PREF_OPTION_MODE, optionMode);
        preferenceManager.put(PREF_OPTION_MOVE_FIRST, optionMoveFirst);
        preferenceManager.put(PREF_OPTION_AGAISNT, optionAgaisnt);
        preferenceManager.put(PREF_OPTION_AI_LEVEL, optionAiLevel);
        preferenceManager.put(PREF_OPTION_AI_MAX_SEARCH_TIME, optionAiMaxReflexionTime);
        preferenceManager.put(PREF_OPTION_AI_PONDER, optionAiPonder);
    }

    private void loadOptionsValues() {
        Resources res = getResources();

        game_mode_option_values = new HashMap<>();
        game_mode_option_values.put(Constants.OPTION_GAME_MODE_RIATRA, res.getString(R.string.game_option_mode_riatra));
        game_mode_option_values.put(Constants.OPTION_GAME_MODE_VELA_BLACK, res.getString(R.string.game_option_mode_vela_black));
        game_mode_option_values.put(Constants.OPTION_GAME_MODE_VELA_WHITE, res.getString(R.string.game_option_mode_vela_white));

        first_move_option_values = new HashMap<>();
        first_move_option_values.put(Constants.OPTION_FIRST_MOVE_BLACK, res.getString(R.string.game_option_move_first_black));
        first_move_option_values.put(Constants.OPTION_FIRST_MOVE_WHITE, res.getString(R.string.game_option_move_first_white));

        play_against_option_values = new HashMap<>();
        play_against_option_values.put(Constants.OPTION_PLAY_AGAINST_HUMAN, res.getString(R.string.game_option_against_human));
        play_against_option_values.put(Constants.OPTION_PLAY_AGAINST_AI_BLACK, res.getString(R.string.game_option_against_ai_black));
        play_against_option_values.put(Constants.OPTION_PLAY_AGAINST_AI_WHITE, res.getString(R.string.game_option_against_ai_white));

        ai_ponder_values = new HashMap<>();
        ai_ponder_values.put(Constants.OPTION_AI_PONDER_YES, res.getString(R.string.game_option_ai_ponder_yes));
        ai_ponder_values.put(Constants.OPTION_AI_PONDER_NO, res.getString(R.string.game_option_ai_ponder_no));

    }
}