package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import com.tambyy.fanoronaakalana.utils.PreferenceManager;

public class SettingSoundActivity extends AppCompatActivity {

    @BindView(R.id.setting_sound_volume)
    SeekBar seekBarSoundVolume;

    @BindView(R.id.setting_sound_save)
    Button buttonSaveSoundSetting;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sound);
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

    public void saveSoundSetting(View view) {
        savePreferences();
        finish();
    }

    /**
     *
     */
    private void loadPreferences() {
        seekBarSoundVolume.setProgress(preferenceManager.get(Constants.PREF_SETTING_SOUND_VOLUME, seekBarSoundVolume.getMax()));
    }

    /**
     *
     */
    private void savePreferences() {
        preferenceManager.put(Constants.PREF_SETTING_SOUND_VOLUME, seekBarSoundVolume.getProgress());
    }
}