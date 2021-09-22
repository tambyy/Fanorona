package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.GridView;

import com.tambyy.fanoronaakalana.adapter.ThemeAdapter;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.models.Theme;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

import java.util.List;

public class SettingThemeActivity extends AppCompatActivity {

    @BindView(R.id.themes_list)
    GridView themesList;

    /**
     * Database
     */
    private FanoronaDatabase database;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    /**
     *
     */
    private Engine engine = new Engine(0);

    /**
     *
     */
    ThemeAdapter themeAdapter;

    /**
     *
     */
    List<Theme> themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_theme);
        ButterKnife.bind(this);

        setPopupDisplay();

        this.database = FanoronaDatabase.getInstance(this);
        this.preferenceManager = PreferenceManager.getInstance(this);

        initThemesList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        engine.terminate();
    }

    private void setPopupDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.9));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    /**
     *
     */
    private void initThemesList() {
        themes = this.database.themeDao().getThemes();

        themeAdapter = new ThemeAdapter(this, themes, engine);
        themesList.setAdapter(themeAdapter);
        themesList.setOnItemClickListener((parent, view, position, id) -> {
            themeAdapter.setSelectedTheme(position);

            validateEdition();
        });

        // get selected theme index
        long selectedThemeId = this.preferenceManager.get(ThemeManager.PREF_THEME, -1l);
        if (selectedThemeId >= 0) {
            int selectedIndex = getSelectedThemeIndex(selectedThemeId);
            if (selectedIndex >= 0) {
                themeAdapter.setSelectedTheme(selectedIndex);
            }
        }
    }

    /**
     *
     * @param id
     * @return
     */
    private int getSelectedThemeIndex(long id) {
        int index = 0;

        for (Theme theme: themes) {
            if (theme.getId() == id) {
                return index;
            }

            ++index;
        }

        return -1;
    }

    /**
     *
     */
    public void validateEdition() {
        int selectedTheme = themeAdapter.getSelectedTheme();
        if (selectedTheme >= 0) {
            this.preferenceManager.put(ThemeManager.PREF_THEME, themes.get(selectedTheme).getId());
        }

        finish();
    }
}