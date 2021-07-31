package com.tambyy.fanoronaakalana.dao;

import com.tambyy.fanoronaakalana.models.Preference;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PreferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPreference(Preference preference);

    @Query("DELETE FROM Preference WHERE `key` = :key")
    int deletePreference(String key);

    @Query("SELECT value FROM Preference WHERE `key` = :key")
    String getPreference(String key);
}
