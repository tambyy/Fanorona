package com.tambyy.fanoronaakalana;

import android.app.Application;
import android.content.res.Resources;

import com.tambyy.fanoronaakalana.config.Theme;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

public class AkalanaApplication extends Application {

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    /**
     * Theme
     */
    private ThemeManager themeManager;

    private Theme akalanaTheme = new Theme();

    @Override
    public void onCreate() {
        super.onCreate();

        this.preferenceManager = PreferenceManager.getInstance(this);
        this.themeManager = ThemeManager.getInstance(this);
        this.loadPreferences();
    }

    public Theme getAkalanaTheme() {
        return akalanaTheme;
    }

    public void setAkalanaTheme(Long themeId) {
        themeManager.getTheme(themeId, theme -> {
            if (theme != null) {
                this.akalanaTheme = theme;
            }
        });
    }

    /**
     *
     */
    private void loadPreferences() {
        this.setAkalanaTheme(preferenceManager.get(ThemeManager.PREF_THEME, 1l));
    }

}