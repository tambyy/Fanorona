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

public class OptionFirstMoveActivity extends AppCompatActivity {

    @BindView(R.id.option_first_move_black)
    CardView optionFirstMoveBlackCardView;

    @BindView(R.id.option_first_move_white)
    CardView optionFirstMoveWhiteCardView;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_first_move);
        ButterKnife.bind(this);

        setupPopupSize();

        this.preferenceManager = PreferenceManager.getInstance(this);
        loadPreferences();
    }

    private void setupPopupSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.35), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    public void setFirstMoveBlack(View view) {
        setFirstMove(Constants.OPTION_FIRST_MOVE_BLACK);
    }

    public void setFirstMoveWhite(View view) {
        setFirstMove(Constants.OPTION_FIRST_MOVE_WHITE);
    }

    private void setFirstMove(int moveFirst) {
        this.preferenceManager.put(Constants.PREF_OPTION_FIRST_MOVE, moveFirst);
        this.showSelectedOption(moveFirst);

        Intent intent = new Intent();
        intent.putExtra(OptionActivity.FIRST_MOVE_VALUE_CODE, moveFirst);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showSelectedOption(int option) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            optionFirstMoveBlackCardView.setBackgroundTintList(Constants.OPTION_BGH);
            optionFirstMoveWhiteCardView.setBackgroundTintList(Constants.OPTION_BGH);

            if (option == Constants.OPTION_FIRST_MOVE_BLACK) {
                optionFirstMoveBlackCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            } else if (option == Constants.OPTION_FIRST_MOVE_WHITE) {
                optionFirstMoveWhiteCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            }
        } else {
            optionFirstMoveBlackCardView.setCardBackgroundColor(Constants.OPTION_BG);
            optionFirstMoveWhiteCardView.setCardBackgroundColor(Constants.OPTION_BG);

            if (option == Constants.OPTION_FIRST_MOVE_BLACK) {
                optionFirstMoveBlackCardView.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            } else if (option == Constants.OPTION_FIRST_MOVE_WHITE) {
                optionFirstMoveWhiteCardView.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            }
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        showSelectedOption(preferenceManager.get(Constants.PREF_OPTION_FIRST_MOVE, Constants.OPTION_FIRST_MOVE_BLACK));
    }

}