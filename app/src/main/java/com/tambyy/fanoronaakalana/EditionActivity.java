package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.tambyy.fanoronaakalana.config.Theme;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

public class EditionActivity extends AppCompatActivity {

    @BindView(R.id.edition_akalana)
    AkalanaView akalanaView;

    @BindView(R.id.edition_type)
    RadioGroup radioGroupEditionType;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    /**
     * Theme
     */
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition);
        ButterKnife.bind(this);

        // set mode edition
        akalanaView.setModeEdition();
        akalanaView.setModeEditionAction(akalanaView.EDITION_ADD_BLACK_PAWN);

        // set edition mode type
        radioGroupEditionType.setOnCheckedChangeListener((group, checkedId) -> {
            // Add black pawn
            if (checkedId == R.id.edition_black_pawn) {
                akalanaView.setModeEditionAction(akalanaView.EDITION_ADD_BLACK_PAWN);
            // Add white pawn
            } else if (checkedId == R.id.edition_white_pawn) {
                akalanaView.setModeEditionAction(akalanaView.EDITION_ADD_WHITE_PAWN);
            // remove pawn
            } else {
                akalanaView.setModeEditionAction(akalanaView.EDITION_REMOVE_PAWN);
            }
        });

        this.preferenceManager = PreferenceManager.getInstance(this);
        this.themeManager = ThemeManager.getInstance(this);
        loadPreferences();

        intentFromOptionActivity();
    }

    /**
     *
     */
    private void intentFromOptionActivity() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(OptionActivity.EXTRA_EDITION_BLACK_CONFIG) && bundle.containsKey(OptionActivity.EXTRA_EDITION_WHITE_CONFIG)) {
                Engine.Point[] blackPositions = EngineActionsConverter.stringToPositions(bundle.getString(OptionActivity.EXTRA_EDITION_BLACK_CONFIG));
                Engine.Point[] whitePositions = EngineActionsConverter.stringToPositions(bundle.getString(OptionActivity.EXTRA_EDITION_WHITE_CONFIG));

                for (Engine.Point blackPosition: blackPositions) {
                    akalanaView.EDITION_ADD_BLACK_PAWN.applyAction(blackPosition);
                }

                for (Engine.Point whitePosition: whitePositions) {
                    akalanaView.EDITION_ADD_WHITE_PAWN.applyAction(whitePosition);
                }
            }
        }
    }

    /**
     * Clear board
     * @param v
     */
    public void clearBoard(View v) {
        akalanaView.removeAllPieces();
    }

    /**
     * Validate edition
     * and launch OptionActivity
     * @param v
     */
    public void launchOptionActivity(View v) {
        Intent intent = new Intent(this, OptionActivity.class);

        intent.putExtra(OptionActivity.EDITION_BLACK_VALUE_CODE, EngineActionsConverter.positionsToString(akalanaView.getBlackActivePieces().stream().map(pawn -> new Engine.Point(pawn.getFx(), pawn.getFy())).toArray(Engine.Point[]::new)));
        intent.putExtra(OptionActivity.EDITION_WHITE_VALUE_CODE, EngineActionsConverter.positionsToString(akalanaView.getWhiteActivePieces().stream().map(pawn -> new Engine.Point(pawn.getFx(), pawn.getFy())).toArray(Engine.Point[]::new)));

        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
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