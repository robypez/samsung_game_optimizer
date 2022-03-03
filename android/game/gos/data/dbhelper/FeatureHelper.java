package com.samsung.android.game.gos.data.dbhelper;

public class FeatureHelper {
    public static boolean isAvailable(String str) {
        return DbHelper.getInstance().getGlobalFeatureFlagDao().isAvailable(str);
    }

    public static boolean isEnabledFlagByUser(String str) {
        return DbHelper.getInstance().getGlobalFeatureFlagDao().isEnabledFlagByUser(str);
    }

    public static boolean isUsingUserValue(String str) {
        return DbHelper.getInstance().getGlobalFeatureFlagDao().isUsingUserValue(str);
    }

    public static boolean isUsingPkgValue(String str) {
        return DbHelper.getInstance().getGlobalFeatureFlagDao().isUsingPkgValue(str);
    }
}
