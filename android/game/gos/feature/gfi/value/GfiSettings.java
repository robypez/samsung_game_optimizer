package com.samsung.android.game.gos.feature.gfi.value;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.android.game.gos.util.PlatformUtil;

public class GfiSettings {
    private static final boolean DEFAULT_RECORD_SESSION = false;
    private static final int DEFAULT_WATCHDOG_EXPIRE_DURATION = 0;
    private static final String GFI_SHARED_PREFERENCES = "com.samsung.android.game.gos.feature.gfi_preferences";
    private static final String RECORD_SESSION_STRING = "gfi_record_session";
    private static final String WATCHDOG_EXPIRE_DURATION_STRING = "gfi_watchdog_expire_duration";
    static GfiSettings mInstance;
    private Context mContext = null;
    private SharedPreferences mPreferences = null;
    private boolean mRecordSession;
    private int mWatchdogExpireDuration;

    public static synchronized GfiSettings getInstance(Context context) {
        GfiSettings gfiSettings;
        synchronized (GfiSettings.class) {
            if (mInstance == null) {
                mInstance = new GfiSettings(context);
            }
            gfiSettings = mInstance;
        }
        return gfiSettings;
    }

    private GfiSettings(Context context) {
        this.mContext = context;
        this.mRecordSession = false;
        this.mWatchdogExpireDuration = 0;
        if (PlatformUtil.isDebugBinary()) {
            SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(GFI_SHARED_PREFERENCES, 0);
            this.mPreferences = sharedPreferences;
            this.mRecordSession = sharedPreferences.getBoolean(RECORD_SESSION_STRING, false);
            this.mWatchdogExpireDuration = this.mPreferences.getInt(WATCHDOG_EXPIRE_DURATION_STRING, 0);
        }
    }

    public boolean isSessionRecordingEnabled() {
        return this.mRecordSession;
    }

    public void setSessionRecordingEnabled(boolean z) {
        this.mRecordSession = z;
        SharedPreferences sharedPreferences = this.mPreferences;
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(RECORD_SESSION_STRING, z);
            edit.commit();
        }
    }

    public int getWatchdogExpireDuration() {
        return this.mWatchdogExpireDuration;
    }

    public void setWatchdogExpireDuration(int i) {
        this.mWatchdogExpireDuration = i;
        SharedPreferences sharedPreferences = this.mPreferences;
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(WATCHDOG_EXPIRE_DURATION_STRING, i);
            edit.commit();
        }
    }
}
