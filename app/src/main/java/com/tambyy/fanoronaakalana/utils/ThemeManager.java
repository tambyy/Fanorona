package com.tambyy.fanoronaakalana.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.tambyy.fanoronaakalana.config.Theme;
import com.tambyy.fanoronaakalana.dao.PreferenceDao;
import com.tambyy.fanoronaakalana.dao.ThemeDao;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.graphics.drawable.item.Pawn;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static interface ThemeLoadedListener {
        public void onThemeLoaded(Theme theme);
    }

    /**
     *
     */
    private class LoadThemeProcess extends AsyncTask<Long, Void, Theme> {

        private ThemeLoadedListener listener;

        public void setListener(ThemeLoadedListener listener) {
            this.listener = listener;
        }

        @Override
        protected Theme doInBackground(Long... arg0) {
            com.tambyy.fanoronaakalana.models.Theme theme = dao.getTheme(arg0[0]);

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
                                    case "akalana.jpg":
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
                                    case "movable_position.png":
                                        configTheme.setMovablePositionBitmap(bitmap);
                                        break;
                                    case "traveled_position.png":
                                        configTheme.setTraveledPositionBitmap(bitmap);
                                        break;
                                    case "removable_position.png":
                                        configTheme.setRemovablePositionBitmap(bitmap);
                                        break;
                                }
                            } catch (IOException e) {}
                        }

                        return configTheme;
                    }
                } catch (IOException e) {
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Theme theme) {
            this.listener.onThemeLoaded(theme);
        }

    };

    public void getTheme(long themeId, ThemeLoadedListener listener) {
        LoadThemeProcess loadThemeProcess = new LoadThemeProcess();
        loadThemeProcess.setListener(listener);
        loadThemeProcess.execute(themeId);
    }

    public void parseJson(String json, Theme theme, String folder) {
        try {
            JSONObject config = new JSONObject(json);

            // akalana
            JSONObject akalana = config.optJSONObject("akalana");
            if (akalana != null) {
                String akalanaBg = akalana.optString("bg");
                if (akalanaBg != null) {

                } else {
                    String akalanaBgColor = akalana.optString("bg-color");
                    String akalanaLineColor = akalana.optString("line-color");
                    int akalanaLineWidth = akalana.optInt("line-width", 1);
                }
            }

            // pieces
            JSONObject pieces = config.optJSONObject("pieces");

            JSONObject blackPieces = pieces.optJSONObject("black");
            if (blackPieces != null) {
                JSONObject defaultPieces = blackPieces.optJSONObject("default");
                if (defaultPieces != null) {
                    String image = defaultPieces.optString("image");
                    if (image != null) {
                        InputStream in = assetManager.open(folder + "/" + image);
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        theme.setBlackDefaultBitmap(bitmap);
                    } else {
                        String color = defaultPieces.optString("color");
                        String borderColor = defaultPieces.optString("border-color");
                        int borderWidth = defaultPieces.optInt("border-width");

                        if (color != null) {
                            theme.setBlackDefaultColor(Color.parseColor(color));
                        }
                        if (borderColor != null) {
                            theme.setBlackStrokeColor(Color.parseColor(color));
                        }

                        theme.setWhiteBorderWidth(borderWidth);
                    }
                }

                JSONObject movablePieces = blackPieces.optJSONObject("default");
                if (movablePieces != null) {
                    String image = movablePieces.optString("image");
                    if (image != null) {
                        InputStream in = assetManager.open(folder + "/" + image);
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        theme.setBlackMovableBitmap(bitmap);
                    } else {
                        String color = movablePieces.optString("color");
                        String borderColor = movablePieces.optString("border-color");
                        int borderWidth = movablePieces.optInt("border-width");
                    }
                }

                JSONObject selectedPieces = blackPieces.optJSONObject("default");
                if (selectedPieces != null) {
                    String image = selectedPieces.optString("image");
                    if (image != null) {
                        InputStream in = assetManager.open(folder + "/" + image);
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        theme.setBlackSelectedBitmap(bitmap);
                    } else {
                        String color = selectedPieces.optString("color");
                        String borderColor = selectedPieces.optString("border-color");
                        int borderWidth = selectedPieces.optInt("border-width");
                    }
                }

            }

            // marks
            JSONObject marks = config.optJSONObject("marks");

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.i("AKALANA", e.getMessage());
        }
    }
}
