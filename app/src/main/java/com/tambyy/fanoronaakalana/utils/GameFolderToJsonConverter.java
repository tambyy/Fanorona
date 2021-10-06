package com.tambyy.fanoronaakalana.utils;

import android.content.Context;

import com.tambyy.fanoronaakalana.dao.FolderDao;
import com.tambyy.fanoronaakalana.dao.GameDao;
import com.tambyy.fanoronaakalana.dao.PreferenceDao;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.models.Folder;
import com.tambyy.fanoronaakalana.models.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameFolderToJsonConverter {

    private GameDao gameDao;
    private FolderDao folderDao;
    private static GameFolderToJsonConverter instance = null;

    /**
     * singleton
     * @param context
     * @return
     */
    public static GameFolderToJsonConverter getInstance(Context context) {
        if (instance == null) {
            instance = new GameFolderToJsonConverter(context);
        }

        return instance;
    }

    private GameFolderToJsonConverter(Context context) {
        gameDao = FanoronaDatabase.getInstance(context).gameDao();
        folderDao = FanoronaDatabase.getInstance(context).folderDao();
    }

    public JSONArray convert(List<Folder> folders, List<Game> games) {
        JSONArray child = new JSONArray();

        for (Folder folder1: folders) {
            child.put(convert(folder1));
        }

        for (Game game: games) {
            child.put(convert(game));
        }

        return child;
    }

    public JSONObject convert(Game game) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type", "game");
            jsonObject.put("name", game.getName());
            jsonObject.put("config", game.getConfigs());
            jsonObject.put("image_index", game.getImage_index());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject convert(Folder folder) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type", "folder");
            jsonObject.put("name", folder.getName());
            jsonObject.put("child", convert(folderDao.getChildFolders(folder.getId()), gameDao.getChildGames(folder.getId())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
