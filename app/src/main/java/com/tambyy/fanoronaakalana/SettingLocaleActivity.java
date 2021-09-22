package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tambyy.fanoronaakalana.utils.LocaleManager;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;

import java.util.Locale;

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
        cardViewSettingLocaleMg.setBackgroundTintList(Constants.OPTION_BC);
        cardViewSettingLocaleFr.setBackgroundTintList(Constants.OPTION_BC);
        cardViewSettingLocaleEn.setBackgroundTintList(Constants.OPTION_BC);

        if (locale.equals(Constants.SETTING_LOCALE_MG)) {
            cardViewSettingLocaleMg.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        } else if (locale.equals(Constants.SETTING_LOCALE_FR)) {
            cardViewSettingLocaleFr.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        } else if (locale.equals(Constants.SETTING_LOCALE_EN)) {
            cardViewSettingLocaleEn.setBackgroundTintList(Constants.SELECTED_OPTION_BC);
        }
    }

    /**
     *
     */
    private void loadPreferences() {
        showSelectedLocale(localeManager.getLocale());
    }
}