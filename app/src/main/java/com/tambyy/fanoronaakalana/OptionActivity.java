package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;

import java.util.Map;

public class OptionActivity extends AppCompatActivity {

    @BindView(R.id.game_option_mode)
    RadioGroup radioGroupOptionMode;

    @BindView(R.id.game_option_move_first)
    RadioGroup radioGroupOptionMoveFirst;

    @BindView(R.id.game_option_against)
    RadioGroup radioGroupOptionAgainst;

    @BindView(R.id.game_option_ai_ponder)
    RadioGroup radioGroupOptionAiPonder;

    @BindView(R.id.game_option_ai_level_choose)
    LinearLayout linearLayoutGameOptionAiLevelChoose;

    @BindView(R.id.game_option_ai_max_search_time_choose)
    LinearLayout linearLayoutGameOptionAiMaxSearchTimeChoose;

    @BindView(R.id.game_option_ai_level)
    TextView textViewGameOptionAiLevel;

    @BindView(R.id.game_option_ai_max_search_time)
    TextView textViewGameOptionAiMaxSearchTime;

    @BindView(R.id.game_option_play)
    Button buttonGameOptionPlay;

    @BindView(R.id.game_option_resume)
    Button buttonGameOptionResume;

    @BindView(R.id.game_option_re_edit)
    Button buttonGameOptionReEdit;

    /**
     *
     */
    private static final Map<Integer, Integer> GAME_MODE_OPTION_VALUES = Map.of(
            R.id.game_option_mode_riatra,     0,
            R.id.game_option_mode_vela_black, 1,
            R.id.game_option_mode_vela_white, 2
    );

    /**
     *
     */
    private static final Map<Integer, Integer> GAME_MOVE_FIRST_OPTION_VALUES = Map.of(
            R.id.game_option_move_first_black, 0,
            R.id.game_option_move_first_white, 1
    );

    /**
     *
     */
    private static final Map<Integer, Integer> GAME_AGAINST_OPTION_VALUES = Map.of(
            R.id.game_option_against_human,    0,
            R.id.game_option_against_ai_black, 1,
            R.id.game_option_against_ai_white, 2
    );

    /**
     *
     */
    private static final Map<Integer, Integer> GAME_AI_PONDER_OPTION_VALUES = Map.of(
            R.id.game_option_ai_ponder_yes, 0,
            R.id.game_option_ai_ponder_no,  1
    );

    private static final String PREF_OPTION_MODE = "OPTION_MODE";
    private static final String PREF_OPTION_MOVE_FIRST = "OPTION_MOVE_FIRST";
    private static final String PREF_OPTION_AGAISNT = "OPTION_AGAISNT";
    private static final String PREF_OPTION_AI_LEVEL = "OPTION_AI_LEVEL";
    private static final String PREF_OPTION_AI_MAX_SEARCH_TIME = "OPTION_AI_MAX_SEARCH_TIME";
    private static final String PREF_OPTION_AI_PONDER = "OPTION_AI_PONDER";

    public static final int AI_LEVEL_CODE = 0;
    public static final int AI_MAX_SEARCH_TIME_CODE = 1;
    public static final int GAME_RESUME_CODE = 2;
    public static final int EDITION_OK = 3;

    public static final String AI_LEVEL_VALUE_CODE = "AI_LEVEL_VALUE";
    public static final String AI_MAX_SEARCh_TIME_VALUE_CODE = "AI_MAX_SEARCH_TIME_VALUE";
    public static final String GAME_RESUME_HISTORY_VALUE_CODE = "GAME_RESUME_CODE_VALUE_CODE";
    public static final String GAME_RESUME_HISTORY_INDEX_VALUE_CODE = "GAME_RESUME_HISTORY_INDEX_VALUE_CODE";
    public static final String GAME_RESUME_HISTORY_BEGIN_TIME_VALUE_CODE = "GAME_RESUME_HISTORY_BEGIN_TIME_VALUE_CODE";

    public static final String EDITION_BLACK_VALUE_CODE = "EDITION_BLACK_VALUE_CODE";
    public static final String EDITION_WHITE_VALUE_CODE = "EDITION_WHITE_VALUE_CODE";

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
    private int optionAiMaxSearchTime = 5000;

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

        this.preferenceManager = PreferenceManager.getInstance(this);

        loadPreferences();

        // Set game mode
        radioGroupOptionMode.setOnCheckedChangeListener((group, checkedId) -> optionMode = GAME_MODE_OPTION_VALUES.get(checkedId));
        // Set move first
        radioGroupOptionMoveFirst.setOnCheckedChangeListener((group, checkedId) -> optionMoveFirst = GAME_MOVE_FIRST_OPTION_VALUES.get(checkedId));
        // Set against
        radioGroupOptionAgainst.setOnCheckedChangeListener((group, checkedId) -> optionAgaisnt = GAME_AGAINST_OPTION_VALUES.get(checkedId));
        // Set AI ponder
        radioGroupOptionAiPonder.setOnCheckedChangeListener((group, checkedId) -> optionAiPonder = GAME_AI_PONDER_OPTION_VALUES.get(checkedId));

        // check intent from EditionActivity
        intentFromEditionActivity();
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
                    radioGroupOptionMode.check(R.id.game_option_mode_vela_black);

                    // White must move first
                    if (whitePositions.length == 5) {
                        radioGroupOptionMoveFirst.check(R.id.game_option_move_first_white);
                    }
                // Vela White
                } else if (blackPositions.length < 20 && whitePositions.length == 22) {
                    radioGroupOptionMode.check(R.id.game_option_mode_vela_white);

                    // Black must move first
                    if (blackPositions.length == 5) {
                        radioGroupOptionMoveFirst.check(R.id.game_option_move_first_black);
                    }
                // Riatra
                } else {
                    radioGroupOptionMode.check(R.id.game_option_mode_riatra);
                }

                buttonGameOptionReEdit.setVisibility(View.VISIBLE);
            }
        }
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
    public void launchGameActivity(View v) {
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(EXTRA_GAME_MODE_CODE,               optionMode);
        intent.putExtra(EXTRA_GAME_MOVE_FIRST_CODE,         optionMoveFirst);
        intent.putExtra(EXTRA_GAME_AGAINST_CODE,            optionAgaisnt);
        intent.putExtra(EXTRA_GAME_AI_LEVEL_CODE,           optionAiLevel);
        intent.putExtra(EXTRA_GAME_AI_MAX_SEARCH_TIME_CODE, optionAiMaxSearchTime);
        intent.putExtra(EXTRA_GAME_AI_PONDER_CODE,          optionAiPonder);

        // config from EditionActivity if exists
        if (editionBlackConfig != null && editionWhiteConfig != null) {
            intent.putExtra(EXTRA_EDITION_BLACK_CONFIG, editionBlackConfig);
            intent.putExtra(EXTRA_EDITION_WHITE_CONFIG, editionWhiteConfig);
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
        intent.putExtra(EXTRA_GAME_AI_MAX_SEARCH_TIME_CODE, optionAiMaxSearchTime);
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
    private void onOptionAiLevelActivityResult(Bundle bundle) {
        optionAiLevel = bundle.getInt(AI_LEVEL_VALUE_CODE);
        textViewGameOptionAiLevel.setText(optionAiLevel + "");
    }

    /**
     *
     * @param bundle
     */
    private void onOptionAiMaxSearchTimeActivityResult(Bundle bundle) {
        float value = bundle.getFloat(AI_MAX_SEARCh_TIME_VALUE_CODE);
        optionAiMaxSearchTime = (int) (1000 * value);
        textViewGameOptionAiMaxSearchTime.setText(value + "s");
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

            if (requestCode == AI_LEVEL_CODE) {
                onOptionAiLevelActivityResult(bundle);
            } else if (requestCode == AI_MAX_SEARCH_TIME_CODE) {
                onOptionAiMaxSearchTimeActivityResult(bundle);
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

    /**
     *
     */
    private void loadPreferences() {
        optionMode            = preferenceManager.get(PREF_OPTION_MODE, 0);
        optionMoveFirst       = preferenceManager.get(PREF_OPTION_MOVE_FIRST, 0);
        optionAgaisnt         = preferenceManager.get(PREF_OPTION_AGAISNT, 0);
        optionAiLevel         = preferenceManager.get(PREF_OPTION_AI_LEVEL, 8);
        optionAiMaxSearchTime = preferenceManager.get(PREF_OPTION_AI_MAX_SEARCH_TIME, 5000);
        optionAiPonder        = preferenceManager.get(PREF_OPTION_AI_PONDER, 0);

        radioGroupOptionMode.check(getKey(GAME_MODE_OPTION_VALUES, optionMode));
        radioGroupOptionMoveFirst.check(getKey(GAME_MOVE_FIRST_OPTION_VALUES, optionMoveFirst));
        radioGroupOptionAgainst.check(getKey(GAME_AGAINST_OPTION_VALUES, optionAgaisnt));
        textViewGameOptionAiLevel.setText(optionAiLevel + "");
        textViewGameOptionAiMaxSearchTime.setText((((float) optionAiMaxSearchTime) / 1000) + "s");
        radioGroupOptionAiPonder.check(getKey(GAME_AI_PONDER_OPTION_VALUES, optionAiPonder));
    }

    /**
     *
     */
    private void savePreferences() {
        preferenceManager.put(PREF_OPTION_MODE, optionMode);
        preferenceManager.put(PREF_OPTION_MOVE_FIRST, optionMoveFirst);
        preferenceManager.put(PREF_OPTION_AGAISNT, optionAgaisnt);
        preferenceManager.put(PREF_OPTION_AI_LEVEL, optionAiLevel);
        preferenceManager.put(PREF_OPTION_AI_MAX_SEARCH_TIME, optionAiMaxSearchTime);
        preferenceManager.put(PREF_OPTION_AI_PONDER, optionAiPonder);
    }
}