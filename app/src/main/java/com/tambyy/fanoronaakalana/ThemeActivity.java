package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.tambyy.fanoronaakalana.adapter.EditGameImageAdapter;
import com.tambyy.fanoronaakalana.adapter.ThemeAdapter;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.models.Theme;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

import java.util.List;

public class ThemeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);

        this.database = FanoronaDatabase.getInstance(this);
        this.preferenceManager = PreferenceManager.getInstance(this);

        initThemesList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        engine.terminate();
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
     * @param view
     */
    public void validateEdition(View view) {
        Intent intent = new Intent();

        int selectedTheme = themeAdapter.getSelectedTheme();
        Log.d("AKALANA", selectedTheme + "");
        if (selectedTheme >= 0) {
            Log.d("AKALANA", themes.get(selectedTheme).getId() + "");
            this.preferenceManager.put(ThemeManager.PREF_THEME, themes.get(selectedTheme).getId());
        }
        // intent.putExtra(SavedGamesActivity.GAME_EDIT_IMAGE_INDEX_CODE, editGameImageAdapter.getActions().indexOf(action));
        setResult(RESULT_OK, intent);

        finish();
    }
}