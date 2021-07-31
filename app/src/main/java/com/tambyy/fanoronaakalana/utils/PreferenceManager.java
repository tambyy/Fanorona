package com.tambyy.fanoronaakalana.utils;

import android.content.Context;

import com.tambyy.fanoronaakalana.dao.PreferenceDao;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.models.Preference;

public class PreferenceManager {
    private PreferenceDao dao;
    private static PreferenceManager instance = null;

    /**
     * singleton
     * @param context
     * @return
     */
    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }

        return instance;
    }

    private PreferenceManager(Context context) {
        dao = FanoronaDatabase.getInstance(context).preferenceDao();
    }

    public void put(String key, String value) {
        dao.insertPreference(new Preference(key, value));
    }

    public void put(String key, int value) {
        dao.insertPreference(new Preference(key, Integer.toString(value)));
    }

    public void put(String key, long value) {
        dao.insertPreference(new Preference(key, Long.toString(value)));
    }

    public void put(String key, float value) {
        dao.insertPreference(new Preference(key, Float.toString(value)));
    }

    public void put(String key, double value) {
        dao.insertPreference(new Preference(key, Double.toString(value)));
    }

    public String get(String key, String defaultValue) {
        String value = dao.getPreference(key);
        return value == null ? defaultValue : value;
    }

    public int get(String key, int defaultValue) {
        String value = dao.getPreference(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    public long get(String key, long defaultValue) {
        String value = dao.getPreference(key);
        return value == null ? defaultValue : Long.parseLong(value);
    }

    public float get(String key, float defaultValue) {
        String value = dao.getPreference(key);
        return value == null ? defaultValue : Float.parseFloat(value);
    }

    public double get(String key, double defaultValue) {
        String value = dao.getPreference(key);
        return value == null ? defaultValue : Double.parseDouble(value);
    }

    public boolean remove(String key) {
        return dao.deletePreference(key) >= 0;
    }
}
