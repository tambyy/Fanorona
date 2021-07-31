package com.tambyy.fanoronaakalana.dao;

import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.models.Theme;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ThemeDao {
    @Query("SELECT * FROM Theme WHERE id = :id")
    Theme getTheme(Long id);

    @Insert
    long insertTheme(Theme theme);

    @Update
    int updateTheme(Theme theme);

    @Query("DELETE FROM Theme WHERE id = :themeId")
    int deleteTheme(long themeId);

    @Query("SELECT * FROM Theme")
    List<Theme> getThemes();

}
