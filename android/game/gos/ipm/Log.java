package com.samsung.android.game.gos.ipm;

public class Log {
    private static final String LOG_TAG_PREFIX = "IPM:";

    public static void v(String str, String str2) {
        android.util.Log.v(LOG_TAG_PREFIX + str, str2);
    }

    public static void d(String str, String str2) {
        android.util.Log.d(LOG_TAG_PREFIX + str, str2);
    }

    public static void i(String str, String str2) {
        android.util.Log.i(LOG_TAG_PREFIX + str, str2);
    }

    public static void w(String str, String str2) {
        android.util.Log.w(LOG_TAG_PREFIX + str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        android.util.Log.w(LOG_TAG_PREFIX + str, str2, th);
    }

    public static void w(String str, Throwable th) {
        android.util.Log.w(LOG_TAG_PREFIX + str, th);
    }

    public static void e(String str, String str2) {
        android.util.Log.e(LOG_TAG_PREFIX + str, str2);
    }

    public static void e(String str, String str2, Throwable th) {
        android.util.Log.e(LOG_TAG_PREFIX + str, str2, th);
    }
}
