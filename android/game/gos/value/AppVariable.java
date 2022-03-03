package com.samsung.android.game.gos.value;

import android.os.Build;

public class AppVariable {
    private static String sOriginalDeviceName = Build.DEVICE;
    private static String sOriginalModelName = Build.MODEL;
    private static boolean sUnitTest = false;

    private AppVariable() {
    }

    public static boolean isUnitTest() {
        return sUnitTest;
    }

    public static void setUnitTest(boolean z) {
        sUnitTest = z;
    }

    public static String getOriginalDeviceName() {
        return sOriginalDeviceName;
    }

    public static String getOriginalModelName() {
        return sOriginalModelName;
    }

    public static void initDeviceInfo(String str, String str2) {
        sOriginalDeviceName = str;
        sOriginalModelName = str2;
    }
}
