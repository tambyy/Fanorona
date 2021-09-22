package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tambyy.fanoronaakalana.utils.PreferenceManager;

public class OptionPlayAgainstActivity extends AppCompatActivity {

    @BindView(R.id.option_play_against_human)
    CardView optionPlayAgainstHumanCardView;

    @BindView(R.id.option_play_against_ai_black)
    CardView optionPlayAgainstAiBlackCardView;

    @BindView(R.id.option_play_against_ai_white)
    CardView optionPlayAgainstAiWhiteCardView;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_play_against);
        ButterKnife.bind(this);

        setPopupDisplay();

        this.preferenceManager = PreferenceManager.getInstance(this);
        loadPreferences();
    }

    private void setPopupDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.5), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    public void setPlayAgainstHuman(View view) {
        setPlayAgainst(Constants.OPTION_PLAY_AGAINST_HUMAN);
    }

    public void setPlayAgainstAiBlack(View view) {
        setPlayAgainst(Constants.OPTION_PLAY_AGAINST_AI_BLACK);
    }

    public void setPlayAgainstAiWhite(View view) {
        setPlayAgainst(Constants.OPTION_PLAY_AGAINST_AI_WHITE);
    }

    private void setPlayAgainst(int against) {
        this.preferenceManager.put(Constants.PREF_OPTION_PLAY_AGAISNT, against);
        this.showSelectedOption(against);

        Intent intent = new Intent();
        intent.putExtra(OptionActivity.PLAY_AGAINST_VALUE_CODE, against);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showSelectedOption(int option) {
        optionPlayAgainstHumanCardView.setBackgroundTintList(Constants.OPTION_BC);
        optionPlayAgainstAiBlackCardView.setBackgroundTintList(Constants.OPTION_BC);
        optionPlayAgainstAiWhiteCardView.setBackgroundTintList(Constants.OPTION_BC);

        if (option == Constants.OPTION_PLAY_AGAINST_HUMAN) {
            optionPlayAgainstHumanCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        } else if (option == Constants.OPTION_PLAY_AGAINST_AI_BLACK) {
            optionPlayAgainstAiBlackCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        } else if (option == Constants.OPTION_PLAY_AGAINST_AI_WHITE) {
            optionPlayAgainstAiWhiteCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        showSelectedOption(preferenceManager.get(Constants.PREF_OPTION_PLAY_AGAISNT, Constants.OPTION_PLAY_AGAINST_HUMAN));
    }

}