package com.samsung.android.game.gos.ipm;

public final class AppVariable {
    private static boolean sIsUnitTest = false;

    private AppVariable() {
    }

    public static boolean isUnitTest() {
        return sIsUnitTest;
    }

    public static void setUnitTest(boolean z) {
        sIsUnitTest = z;
    }
}
