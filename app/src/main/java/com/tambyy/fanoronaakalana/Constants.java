package com.tambyy.fanoronaakalana;

import android.content.res.ColorStateList;
import android.graphics.Color;

public class Constants {

    public static final int OPTION_GAME_MODE_RIATRA = 0;
    public static final int OPTION_GAME_MODE_VELA_BLACK = 1;
    public static final int OPTION_GAME_MODE_VELA_WHITE = 2;

    public static final int OPTION_FIRST_MOVE_BLACK = 0;
    public static final int OPTION_FIRST_MOVE_WHITE = 1;

    public static final int OPTION_PLAY_AGAINST_HUMAN = 0;
    public static final int OPTION_PLAY_AGAINST_AI_BLACK = 1;
    public static final int OPTION_PLAY_AGAINST_AI_WHITE = 2;

    public static final int OPTION_AI_PONDER_YES = 0;
    public static final int OPTION_AI_PONDER_NO = 1;


    public static final String PREF_OPTION_GAME_MODE = "OPTION_GAME_MODE";
    public static final String PREF_OPTION_FIRST_MOVE = "OPTION_MOVE_FIRST";
    public static final String PREF_OPTION_PLAY_AGAISNT = "PREF_OPTION_PLAY_AGAISNT";
    public static final String PREF_OPTION_AI_LEVEL = "OPTION_AI_LEVEL";
    public static final String PREF_OPTION_AI_MAX_SEARCH_TIME = "OPTION_AI_MAX_SEARCH_TIME";
    public static final String PREF_OPTION_AI_PONDER = "OPTION_AI_PONDER";

    public static final String PREF_SETTING_SOUND_VOLUME = "PREF_SETTING_SOUND_VOLUME";
    public static final String PREF_SETTING_LOCALE = "PREF_SETTING_LOCALE";


    public static final ColorStateList OPTION_BC = ColorStateList.valueOf(Color.TRANSPARENT);
    public static final ColorStateList SELECTED_OPTION_BC = ColorStateList.valueOf(Color.argb(70, 0, 0, 0));


    public static final String SETTING_LOCALE_MG = "mg";
    public static final String SETTING_LOCALE_FR = "fr";
    public static final String SETTING_LOCALE_EN = "en";

}
