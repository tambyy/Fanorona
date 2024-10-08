package com.tambyy.fanoronaakalana.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.tambyy.fanoronaakalana.config.Theme;
import com.tambyy.fanoronaakalana.dao.PreferenceDao;
import com.tambyy.fanoronaakalana.dao.ThemeDao;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ThemeManager {

    private ThemeDao dao;
    private AssetManager assetManager;
    private static ThemeManager instance = null;
    public static final String PREF_THEME = "PREF_THEME";

    /**
     * singleton
     * @param context
     * @return
     */
    public static ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context);
            instance.assetManager = context.getAssets();
        }

        return instance;
    }

    private ThemeManager(Context context) {
        dao = FanoronaDatabase.getInstance(context).themeDao();
    }

    public Theme getTheme(long themeId) {
        com.tambyy.fanoronaakalana.models.Theme theme = dao.getTheme(themeId);

        if (theme != null) {
            Theme configTheme = new Theme();
            configTheme.setAkalanaBgColor(Color.TRANSPARENT);

            try {
                if (Arrays.asList(assetManager.list("themes")).contains(theme.getFolder_name())) {
                    final String folder = "themes/" + theme.getFolder_name();
                    for (String imageName : assetManager.list(folder)) {

                        try {
                            InputStream in = assetManager.open(folder + "/" + imageName);
                            Bitmap bitmap = BitmapFactory.decodeStream(in);

                            switch (imageName) {
                                case "background.png":
                                    configTheme.setBackgroundBitmap(bitmap);
                                    break;
                                case "akalana.png":
                                    configTheme.setAkalanaBitmap(bitmap);
                                    break;
                                case "black_default.png":
                                    configTheme.setBlackDefaultBitmap(bitmap);
                                    break;
                                case "black_movable.png":
                                    configTheme.setBlackMovableBitmap(bitmap);
                                    break;
                                case "black_selected.png":
                                    configTheme.setBlackSelectedBitmap(bitmap);
                                    break;
                                case "white_default.png":
                                    configTheme.setWhiteDefaultBitmap(bitmap);
                                    break;
                                case "white_movable.png":
                                    configTheme.setWhiteMovableBitmap(bitmap);
                                    break;
                                case "white_selected.png":
                                    configTheme.setWhiteSelectedBitmap(bitmap);
                                    break;
                            }
                        } catch (IOException e) {
                        }
                    }

                    return configTheme;
                }
            } catch (IOException e) {
            }
        }

        return null;
    }

}
