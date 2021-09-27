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

public class OptionAiPonderActivity extends AppCompatActivity {

    @BindView(R.id.option_ai_ponder_yes)
    CardView optionAiPonderYesCardView;

    @BindView(R.id.option_ai_ponder_no)
    CardView optionAiPonderNoCardView;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_ai_ponder);
        ButterKnife.bind(this);

        setupPopupSize();

        this.preferenceManager = PreferenceManager.getInstance(this);
        loadPreferences();
    }

    private void setupPopupSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.4), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    public void setAiPonderYes(View view) {
        setAiPonder(Constants.OPTION_AI_PONDER_YES);
    }

    public void setAiPonderNo(View view) {
        setAiPonder(Constants.OPTION_AI_PONDER_NO);
    }

    private void setAiPonder(int ponder) {
        this.preferenceManager.put(Constants.PREF_OPTION_AI_PONDER, ponder);

        Intent intent = new Intent();
        intent.putExtra(OptionActivity.AI_PONDER_VALUE_CODE, ponder);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showSelectedOption(int option) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            optionAiPonderYesCardView.setBackgroundTintList(Constants.OPTION_BGH);
            optionAiPonderNoCardView.setBackgroundTintList(Constants.OPTION_BGH);

            if (option == Constants.OPTION_AI_PONDER_YES) {
                optionAiPonderYesCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            } else if (option == Constants.OPTION_AI_PONDER_NO) {
                optionAiPonderNoCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            }
        } else {
            optionAiPonderYesCardView.setCardBackgroundColor(Constants.OPTION_BG);
            optionAiPonderNoCardView.setCardBackgroundColor(Constants.OPTION_BG);

            if (option == Constants.OPTION_AI_PONDER_YES) {
                optionAiPonderYesCardView.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            } else if (option == Constants.OPTION_AI_PONDER_NO) {
                optionAiPonderNoCardView.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            }
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        showSelectedOption(preferenceManager.get(Constants.PREF_OPTION_AI_PONDER, Constants.OPTION_AI_PONDER_YES));
    }

}