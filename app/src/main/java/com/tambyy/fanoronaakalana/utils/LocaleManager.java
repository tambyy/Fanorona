package com.tambyy.fanoronaakalana.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.tambyy.fanoronaakalana.Constants;
import com.tambyy.fanoronaakalana.dao.PreferenceDao;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;

import java.util.Locale;

public class LocaleManager {
    private PreferenceManager preferenceManager;
    private static LocaleManager instance = null;

    /**
     * singleton
     * @param context
     * @return
     */
    public static LocaleManager getInstance(Context context) {
        instance = new LocaleManager(context);

        return instance;
    }

    private LocaleManager(Context context) {
        preferenceManager = PreferenceManager.getInstance(context);
        setContextLocale(context, getLocale());
    }

    private Context setContextLocale(Context context, String language) {
        // updating the language for devices above android nougat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        // for devices having lower version of android os
        return updateResourcesLegacy(context, language);
    }

    public String getLocale() {
        return preferenceManager.get(Constants.PREF_SETTING_LOCALE, Constants.SETTING_LOCALE_FR);
    }

    public void setLocale(Context context, String locale) {
        preferenceManager.put(Constants.PREF_SETTING_LOCALE, locale);
        this.setContextLocale(context, locale);
    }

    // the method is used update the language of application by creating
    // object of inbuilt Locale class and passing language argument to it
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String locale) {

        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        configuration.setLocale(new Locale(locale));
        res.updateConfiguration(configuration, res.getDisplayMetrics());

        return context.createConfigurationContext(configuration);
    }


    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}
