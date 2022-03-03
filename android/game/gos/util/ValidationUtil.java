package com.samsung.android.game.gos.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String LOG_TAG = "ValidationUtil";

    public static float getValidDfs(float f) {
        if (f < 1.0f) {
            return 1.0f;
        }
        if (f > 120.0f) {
            return 120.0f;
        }
        return f;
    }

    public static float getValidDss(float f) {
        if (f < 1.0f) {
            return 1.0f;
        }
        if (f > 100.0f) {
            return 100.0f;
        }
        return f;
    }

    public static int getValidIpmMode(int i) {
        if (i < -1) {
            return -1;
        }
        if (i > 3) {
            return 3;
        }
        return i;
    }

    public static float[] getValidDssArray(float[] fArr) {
        if (fArr == null || fArr.length != 4) {
            return null;
        }
        for (int i = 0; i < fArr.length; i++) {
            fArr[i] = getValidDss(fArr[i]);
        }
        return fArr;
    }

    public static float[] getValidDfsArray(float[] fArr) {
        if (fArr == null || fArr.length != 4) {
            return null;
        }
        for (int i = 0; i < fArr.length; i++) {
            fArr[i] = getValidDfs(fArr[i]);
        }
        return fArr;
    }

    public static int getValidSiopMode(int i) {
        if (i < -1 && i != -1000) {
            GosLog.e(LOG_TAG, "unexpected siopMode. " + i + " uses " + -1);
            return -1;
        } else if (i <= 1) {
            return i;
        } else {
            GosLog.e(LOG_TAG, "unexpected siopMode. " + i + " uses " + 1);
            return 1;
        }
    }

    public static String getValidPkgName(String str) {
        if (str == null) {
            return str;
        }
        try {
            String replace = str.replace("/", "_..0.._").replace("&", "_..1.._").replace("=", "_..2.._").replace(",", "_..3.._");
            if (!(!Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*(\\.[a-zA-Z][a-zA-Z0-9_]*)+$").matcher(replace).matches())) {
                return replace;
            }
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return "_..invalid_package_name.._";
    }

    public static int getValidMode(int i) {
        if (i >= 0 && i <= 4) {
            return i;
        }
        GosLog.e(LOG_TAG, "unexpected Mode. " + i + " uses " + 1);
        return 1;
    }

    public static boolean floatEqual(float f, float f2) {
        return Math.abs(f - f2) < 1.0E-5f;
    }
}
