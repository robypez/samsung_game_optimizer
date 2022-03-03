package com.samsung.android.game.gos.data;

import com.samsung.android.game.gos.util.GosLog;

public class TestDataManager {
    private static final String LOG_TAG = TestDataManager.class.getSimpleName();
    private static boolean sIsDevMode = false;
    private static boolean sIsTestMode = false;

    public static boolean isTestMode() {
        return sIsTestMode;
    }

    public static void setTestMode(boolean z) {
        sIsTestMode = z;
        String str = LOG_TAG;
        GosLog.d(str, "isTestMode : " + z);
    }

    public static boolean isDevMode() {
        return sIsDevMode;
    }
}
