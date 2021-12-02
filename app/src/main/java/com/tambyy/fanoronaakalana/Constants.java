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

    public static final String PREF_LAST_GAME_CONFIG = "PREF_LAST_GAME_CONFIG";
    public static final String PREF_LAST_HISTORY_INDEX = "PREF_LAST_HISTORY_INDEX";

    public static final String PREF_SETTING_SOUND_VOLUME = "PREF_SETTING_SOUND_VOLUME";
    public static final String PREF_SETTING_LOCALE = "PREF_SETTING_LOCALE";

    public static final String PREF_SAVED_GAMES_REVIEW_SPEED = "PREF_SAVED_GAMES_REVIEW_SPEED";

    public static final ColorStateList OPTION_BGH = ColorStateList.valueOf(Color.TRANSPARENT);
    public static final ColorStateList SELECTED_OPTION_BGH = ColorStateList.valueOf(Color.argb(70, 0, 0, 0));

    public static final int OPTION_BG = Color.TRANSPARENT;
    public static final int SELECTED_OPTION_BG = Color.argb(70, 0, 0, 0);


    public static final String SETTING_LOCALE_MG = "mg";
    public static final String SETTING_LOCALE_FR = "fr";
    public static final String SETTING_LOCALE_EN = "en";

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

}
