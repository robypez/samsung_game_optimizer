package com.samsung.android.game.gos.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.util.GosLog;

public class PreferenceHelper {
    private static final String LOG_TAG = PreferenceHelper.class.getSimpleName();
    public static final String PREF_AVAILABLE_REFRESH_RATE = "PREF_AVAILABLE_REFRESH_RATE";
    public static final String PREF_CLEAR_BG_PROCESS_DONE = "clear_bg_process_done";
    public static final String PREF_DEVICE_INFO_SRC_PROP = "DEVICE_INFO_SRC_PROP";
    public static final String PREF_DEVICE_NAME = "DEVICE_NAME";
    public static final String PREF_FOCUSED_IN_FEATURE_NAMES = "PREF_FOCUSED_IN_FEATURE_NAMES";
    public static final String PREF_HAS_CHECKED_DEVICE_REGISTRATION_TO_SERVER = "PREF_HAS_CHECKED_DEVICE_REGISTRATION_TO_SERVER";
    public static final String PREF_LAST_PAUSED_GAME_PKG_NAME = "LAST_PAUSED_GAME_PKG_NAME";
    public static final String PREF_LAST_PAUSED_GAME_TIME_STAMP = "LAST_PAUSED_GAME_TIME_STAMP";
    public static final String PREF_MODEL_NAME = "MODEL_NAME";
    private static final String PREF_NAME = "shared";
    public static final String PREF_SELECTIVE_TARGET_POLICY = "PREF_SELECTIVE_TARGET_POLICY";
    public static final String PREF_SIGNATURE = "PREF_SIGNATURE";
    private final Context mContext;

    public PreferenceHelper() {
        this(AppContext.get());
    }

    public PreferenceHelper(Context context) {
        this.mContext = context;
    }

    public void put(String str, String str2) throws IllegalStateException {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public void put(String str, long j) throws IllegalStateException {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putLong(str, j);
        edit.apply();
    }

    public void put(String str, float f) throws IllegalStateException {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putFloat(str, f);
        edit.apply();
    }

    public void put(String str, int i) throws IllegalStateException {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public void put(String str, boolean z) throws IllegalStateException {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public void remove(String str) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(PREF_NAME, 0).edit();
        edit.remove(str);
        edit.apply();
    }

    public String getValue(String str, String str2) {
        try {
            return this.mContext.getSharedPreferences(PREF_NAME, 0).getString(str, str2);
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, e.getMessage());
            return str2;
        }
    }

    public long getValue(String str, long j) {
        try {
            return this.mContext.getSharedPreferences(PREF_NAME, 0).getLong(str, j);
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, e.getMessage());
            return j;
        }
    }

    public float getValue(String str, float f) {
        try {
            return this.mContext.getSharedPreferences(PREF_NAME, 0).getFloat(str, f);
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, e.getMessage());
            return f;
        }
    }

    public int getValue(String str, int i) {
        try {
            return this.mContext.getSharedPreferences(PREF_NAME, 0).getInt(str, i);
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, e.getMessage());
            return i;
        }
    }

    public boolean getValue(String str, boolean z) {
        try {
            return this.mContext.getSharedPreferences(PREF_NAME, 0).getBoolean(str, z);
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, e.getMessage());
            return z;
        }
    }
}
