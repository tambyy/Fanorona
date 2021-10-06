package com.tambyy.fanoronaakalana.dao;

import com.tambyy.fanoronaakalana.models.Game;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GameDao {
    @Query("SELECT * FROM Game WHERE id = :id")
    Game getGame(Long id);

    @Insert
    long insertGame(Game game);

    @Update
    int updateGame(Game game);

    @Query("DELETE FROM Game WHERE id = :gameId")
    int deleteGame(long gameId);

    @Query("SELECT * FROM Game WHERE folder_id = :folderId ORDER BY name COLLATE NOCASE")
    List<Game> getChildGames(Long folderId);

    @Query("SELECT * FROM Game WHERE folder_id IS NULL ORDER BY name COLLATE NOCASE")
    List<Game> getRootGames();

}
