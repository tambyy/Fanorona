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

        setPopupDisplay();

        this.preferenceManager = PreferenceManager.getInstance(this);
        loadPreferences();
    }

    private void setPopupDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.3), WindowManager.LayoutParams.WRAP_CONTENT);

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
        optionFirstMoveBlackCardView.setBackgroundTintList(Constants.OPTION_BC);
        optionFirstMoveWhiteCardView.setBackgroundTintList(Constants.OPTION_BC);

        if (option == Constants.OPTION_FIRST_MOVE_BLACK) {
            optionFirstMoveBlackCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        } else if (option == Constants.OPTION_FIRST_MOVE_WHITE) {
            optionFirstMoveWhiteCardView.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        showSelectedOption(preferenceManager.get(Constants.PREF_OPTION_FIRST_MOVE, Constants.OPTION_FIRST_MOVE_BLACK));
    }

}