package club.peiyan.goaltrack.utils;

import android.content.Context;
import android.content.SharedPreferences;

import club.peiyan.goaltrack.GoalApplication;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class AppSp {
    private static final String SP_NAME = "GoalTrack";
    private static final SharedPreferences mSP = GoalApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    private static final SharedPreferences.Editor mEditor = mSP.edit();

    private AppSp() {
    }

    public static void putString(String key, String value) {
        mEditor.putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return mSP.getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        mEditor.putInt(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        return mSP.getInt(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mSP.getBoolean(key, defaultValue);
    }
}
