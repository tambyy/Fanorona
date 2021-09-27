package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tambyy.fanoronaakalana.utils.PreferenceManager;

public class OptionGameModeActivity extends AppCompatActivity {

    @BindView(R.id.option_game_mode_riatra)
    CardView cardViewoptionGameModeRiatra;

    @BindView(R.id.option_game_mode_vela_black)
    CardView cardViewoptionGameModeVelaBlack;

    @BindView(R.id.option_game_mode_vela_white)
    CardView cardViewoptionGameModeVelaWhite;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_game_mode);
        ButterKnife.bind(this);

        setupPopupSize();

        this.preferenceManager = PreferenceManager.getInstance(this);
        loadPreferences();
    }

    private void setupPopupSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.7), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    public void setGameModeRiatra(View view) {
        setMode(Constants.OPTION_GAME_MODE_RIATRA);
    }

    public void setGameModeVelaBlack(View view) {
        setMode(Constants.OPTION_GAME_MODE_VELA_BLACK);
    }

    public void setGameModeVelaWhite(View view) {
        setMode(Constants.OPTION_GAME_MODE_VELA_WHITE);
    }

    private void setMode(int mode) {
        this.preferenceManager.put(Constants.PREF_OPTION_GAME_MODE, mode);
        this.showSelectedOption(mode);

        Intent intent = new Intent();
        intent.putExtra(OptionActivity.GAME_MODE_VALUE_CODE, mode);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showSelectedOption(int option) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardViewoptionGameModeRiatra.setBackgroundTintList(Constants.OPTION_BGH);
            cardViewoptionGameModeVelaBlack.setBackgroundTintList(Constants.OPTION_BGH);
            cardViewoptionGameModeVelaWhite.setBackgroundTintList(Constants.OPTION_BGH);

            if (option == Constants.OPTION_GAME_MODE_RIATRA) {
                cardViewoptionGameModeRiatra.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            } else if (option == Constants.OPTION_GAME_MODE_VELA_BLACK) {
                cardViewoptionGameModeVelaBlack.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            } else if (option == Constants.OPTION_GAME_MODE_VELA_WHITE) {
                cardViewoptionGameModeVelaWhite.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            }
        } else {
            cardViewoptionGameModeRiatra.setCardBackgroundColor(Constants.OPTION_BG);
            cardViewoptionGameModeVelaBlack.setCardBackgroundColor(Constants.OPTION_BG);
            cardViewoptionGameModeVelaWhite.setCardBackgroundColor(Constants.OPTION_BG);

            if (option == Constants.OPTION_GAME_MODE_RIATRA) {
                cardViewoptionGameModeRiatra.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            } else if (option == Constants.OPTION_GAME_MODE_VELA_BLACK) {
                cardViewoptionGameModeVelaBlack.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            } else if (option == Constants.OPTION_GAME_MODE_VELA_WHITE) {
                cardViewoptionGameModeVelaWhite.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            }
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        showSelectedOption(preferenceManager.get(Constants.PREF_OPTION_GAME_MODE, Constants.OPTION_GAME_MODE_RIATRA));
    }

}