package com.samsung.android.game.gos.test.util;

import android.util.Log;
import com.samsung.android.game.gos.util.PlatformUtil;

public class GosTestLog {
    private static final String LOG_TAG_PREFIX = "GOS:";

    public static void v(String str, String str2) {
        if (PlatformUtil.isDebugBinary()) {
            Log.v(LOG_TAG_PREFIX + str, str2);
        }
    }

    public static void d(String str, String str2) {
        Log.d(LOG_TAG_PREFIX + str, str2);
    }

    public static void i(String str, String str2) {
        Log.i(LOG_TAG_PREFIX + str, str2);
    }

    public static void w(String str, String str2) {
        Log.w(LOG_TAG_PREFIX + str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        Log.w(LOG_TAG_PREFIX + str, str2, th);
    }

    public static void w(String str, Throwable th) {
        Log.w(LOG_TAG_PREFIX + str, th);
    }

    public static void e(String str, String str2) {
        Log.e(LOG_TAG_PREFIX + str, str2);
    }

    public static void e(String str, String str2, Throwable th) {
        Log.e(LOG_TAG_PREFIX + str, str2, th);
    }
}
