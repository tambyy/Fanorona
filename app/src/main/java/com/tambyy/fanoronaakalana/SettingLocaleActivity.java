package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tambyy.fanoronaakalana.utils.LocaleManager;

public class SettingLocaleActivity extends AppCompatActivity {

    @BindView(R.id.setting_locale_mg)
    CardView cardViewSettingLocaleMg;

    @BindView(R.id.setting_locale_fr)
    CardView cardViewSettingLocaleFr;

    @BindView(R.id.setting_locale_en)
    CardView cardViewSettingLocaleEn;

    /**
     * Preference
     */
    private LocaleManager localeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_locale);
        ButterKnife.bind(this);

        setupPopupSize();

        this.localeManager = LocaleManager.getInstance(this);
        loadPreferences();
    }

    private void setupPopupSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.6), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    public void setLocaleMg(View view) {
        setLocale(Constants.SETTING_LOCALE_MG);
    }

    public void setLocaleFr(View view) {
        setLocale(Constants.SETTING_LOCALE_FR);
    }

    public void setLocaleEn(View view) {
        setLocale(Constants.SETTING_LOCALE_EN);
    }

    private void setLocale(String locale) {
        this.localeManager.setLocale(this, locale);
        this.showSelectedLocale(locale);

        finish();
    }

    private void showSelectedLocale(String locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardViewSettingLocaleMg.setBackgroundTintList(Constants.OPTION_BGH);
            cardViewSettingLocaleFr.setBackgroundTintList(Constants.OPTION_BGH);
            cardViewSettingLocaleEn.setBackgroundTintList(Constants.OPTION_BGH);

            if (locale.equals(Constants.SETTING_LOCALE_MG)) {
                cardViewSettingLocaleMg.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            } else if (locale.equals(Constants.SETTING_LOCALE_FR)) {
                cardViewSettingLocaleFr.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            } else if (locale.equals(Constants.SETTING_LOCALE_EN)) {
                cardViewSettingLocaleEn.setBackgroundTintList(Constants.SELECTED_OPTION_BGH);
            }
        } else {
            cardViewSettingLocaleMg.setCardBackgroundColor(Constants.OPTION_BG);
            cardViewSettingLocaleFr.setCardBackgroundColor(Constants.OPTION_BG);
            cardViewSettingLocaleEn.setCardBackgroundColor(Constants.OPTION_BG);

            if (locale.equals(Constants.SETTING_LOCALE_MG)) {
                cardViewSettingLocaleMg.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            } else if (locale.equals(Constants.SETTING_LOCALE_FR)) {
                cardViewSettingLocaleFr.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            } else if (locale.equals(Constants.SETTING_LOCALE_EN)) {
                cardViewSettingLocaleEn.setCardBackgroundColor(Constants.SELECTED_OPTION_BG);
            }
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        showSelectedLocale(localeManager.getLocale());
    }
}