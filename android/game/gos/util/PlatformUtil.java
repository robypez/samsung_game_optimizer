package com.samsung.android.game.gos.util;

import android.content.Context;
import android.os.Build;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.game.gos.selibrary.SeFloatingFeatureManager;
import com.samsung.android.game.gos.value.Constants;

public class PlatformUtil {
    private static final String LOG_TAG = "PlatformUtil";
    private static boolean multiResolutionSupported = false;

    static {
        String deviceResolution = SeFloatingFeatureManager.getInstance().getDeviceResolution();
        if (deviceResolution != null) {
            multiResolutionSupported = Constants.MULTI_RESOLUTION.endsWith(deviceResolution);
        }
    }

    public static final boolean isSemDevice(Context context) {
        return context.getPackageManager().hasSystemFeature("com.samsung.feature.samsung_experience_mobile");
    }

    public static final boolean isSemDevice() {
        return ReflectUtil.getField(Build.VERSION.class, "SEM_INT") != null;
    }

    public static final int getSemPlatformVersion() {
        try {
            int i = Build.VERSION.class.getField("SEM_PLATFORM_INT").getInt(Build.VERSION.class);
            GosLog.i(LOG_TAG, "getSemPlatformVersion(). SEM_PLATFORM_INT: " + i);
            return i;
        } catch (IllegalAccessException | NoSuchFieldException unused) {
            GosLog.i(LOG_TAG, "getSemPlatformVersion(). SEM_PLATFORM_INT unavailable");
            return -1;
        }
    }

    public static final boolean isPlatformSupportHoverUI(Context context) {
        return context.getPackageManager().hasSystemFeature("com.sec.feature.hovering_ui");
    }

    public static boolean isDebugBinary() {
        return Build.TYPE != null && (Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug"));
    }

    public static final boolean isMultiResolutionSupported() {
        return multiResolutionSupported;
    }

    public static boolean isFoldableDevice() {
        return SemFloatingFeature.getInstance().getBoolean(Build.VERSION.SDK_INT >= 30 ? "SEC_FLOATING_FEATURE_FRAMEWORK_SUPPORT_FOLDABLE_TYPE_FOLD" : "SEC_FLOATING_FEATURE_FRAMEWORK_SUPPORT_WM_CONTROLS_DISPLAY_SWITCH");
    }
}
