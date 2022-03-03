package com.samsung.android.game.gos.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.value.RinglogConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class PackageUtil {
    private static final String FEATURE_DAYDREAM_VR = "android.software.vr.mode";
    private static final String LOG_TAG = "PackageUtil";
    private static final String METADATA_VR_APPLICATION_MODE = "com.samsung.android.vr.application.mode";
    private static final String METADATA_VR_MODE_DUAL = "dual";
    private static final String METADATA_VR_MODE_VR_ONLY = "vr_only";
    private static final String THEME_DESIGNER_PACKAGE = "com.samsung.themedesigner.";
    private static final String WEB_APK_PACKAGE = "org.chromium.webapk.";

    public static boolean isPackageInstalled(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return true;
        }
        try {
            packageManager.getPackageInfo(str, 128);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean isPackageInstalledAsUser(Context context, String str, int i) {
        PackageManager packageManager = context.getPackageManager();
        try {
            Method method = packageManager.getClass().getMethod("getPackageInfoAsUser", new Class[]{String.class, Integer.TYPE, Integer.TYPE});
            if (method == null) {
                return false;
            }
            if (((PackageInfo) method.invoke(packageManager, new Object[]{str, 0, Integer.valueOf(i)})) != null) {
                return true;
            }
            return false;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            GosLog.w(LOG_TAG, "isPackageInstalledAsUser(). Exception: " + e.toString());
            return false;
        }
    }

    public static boolean isPackageEnabled(Context context, String str) {
        PackageManager packageManager;
        GosLog.d(LOG_TAG, "isPackageEnabled(), begin, " + str);
        boolean z = false;
        if (!(context == null || (packageManager = context.getPackageManager()) == null)) {
            try {
                z = packageManager.getApplicationInfo(str, 128).enabled;
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        GosLog.i(LOG_TAG, "isPackageEnabled(), " + str + ", enabled : " + z);
        return z;
    }

    public static boolean hasLaunchIntent(Context context, String str) {
        PackageManager packageManager;
        GosLog.d(LOG_TAG, "hasLaunchIntent(), begin, " + str);
        boolean z = false;
        if (!(context == null || (packageManager = context.getPackageManager()) == null || packageManager.getLaunchIntentForPackage(str) == null)) {
            z = true;
        }
        GosLog.i(LOG_TAG, "hasLaunchIntent(), " + str + ", result : " + z);
        return z;
    }

    public static boolean hasLaunchIntentAsUser(Context context, String str, int i) {
        PackageManager packageManager = context.getPackageManager();
        boolean z = false;
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setPackage(str);
            Method method = packageManager.getClass().getMethod("queryIntentActivitiesAsUser", new Class[]{Intent.class, Integer.TYPE, Integer.TYPE});
            if (method != null) {
                List list = (List) method.invoke(packageManager, new Object[]{intent, 0, Integer.valueOf(i)});
                if (list != null && list.size() > 0) {
                    z = true;
                }
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            GosLog.w(LOG_TAG, "hasLaunchIntentAsUser(). Exception: " + e.getMessage());
        }
        GosLog.d(LOG_TAG, "hasLaunchIntentAsUser(), " + str + ", userId: " + i + ", result: " + z);
        return z;
    }

    public static boolean launchPackage(Context context, String str) {
        PackageManager packageManager;
        Intent launchIntentForPackage;
        GosLog.d(LOG_TAG, "launchPackage(), begin, " + str);
        if (context == null || (packageManager = context.getPackageManager()) == null || (launchIntentForPackage = packageManager.getLaunchIntentForPackage(str)) == null) {
            GosLog.i(LOG_TAG, "launchPackage(), " + str + ", result : false");
            return false;
        }
        context.startActivity(launchIntentForPackage);
        GosLog.i(LOG_TAG, "launchPackage(), " + str + ", result : true");
        return true;
    }

    public static long getPackageVersionCode(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            try {
                return packageManager.getPackageInfo(str, 128).getLongVersionCode();
            } catch (PackageManager.NameNotFoundException e) {
                GosLog.w(LOG_TAG, "getPackageVersionCode(). NameNotFoundException: " + e.getMessage());
            }
        }
        return -1;
    }

    public static boolean isNonGamePackage(String str) {
        return isWepApk(str) || isThemeDesignerApk(str);
    }

    public static boolean isWepApk(String str) {
        return str != null && str.startsWith(WEB_APK_PACKAGE);
    }

    public static boolean isThemeDesignerApk(String str) {
        return str != null && str.startsWith(THEME_DESIGNER_PACKAGE);
    }

    public static boolean isVrApp(Context context, String str) {
        PackageManager packageManager;
        if (context == null || str == null || (packageManager = context.getPackageManager()) == null) {
            return false;
        }
        try {
            Bundle bundle = packageManager.getApplicationInfo(str, 128).metaData;
            if (bundle != null) {
                String string = bundle.getString(METADATA_VR_APPLICATION_MODE, RinglogConstants.SYSTEM_STATUS_RAW_TEMP);
                if (string.equalsIgnoreCase(METADATA_VR_MODE_VR_ONLY) || string.equalsIgnoreCase(METADATA_VR_MODE_DUAL)) {
                    GosLog.i(LOG_TAG, "isVrApp(). " + str + " is VR app. mode: " + string);
                    return true;
                }
            }
            FeatureInfo[] featureInfoArr = packageManager.getPackageInfo(str, 16384).reqFeatures;
            if (featureInfoArr != null && featureInfoArr.length > 0) {
                int length = featureInfoArr.length;
                int i = 0;
                while (i < length) {
                    FeatureInfo featureInfo = featureInfoArr[i];
                    if (featureInfo == null || featureInfo.name == null || !featureInfo.name.equalsIgnoreCase(FEATURE_DAYDREAM_VR)) {
                        i++;
                    } else {
                        GosLog.i(LOG_TAG, "isVrApp(). " + str + " is Daydream app. flags: " + featureInfo.flags);
                        if (featureInfo.flags == 1) {
                            return true;
                        }
                        return false;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            GosLog.w(LOG_TAG, "isVrApp(). PackageManager.NameNotFoundException: " + e.getMessage());
        }
        return false;
    }

    public static String getCallerPkgName() {
        PackageManager packageManager = AppContext.get().getPackageManager();
        if (packageManager == null) {
            return null;
        }
        int callingUid = Binder.getCallingUid();
        String nameForUid = packageManager.getNameForUid(callingUid);
        if (nameForUid != null && nameForUid.contains(":")) {
            nameForUid = SeActivityManager.getInstance().getAppNameFromPid(AppContext.get(), Binder.getCallingPid());
            GosLog.i(LOG_TAG, "getCallerPkgName(), _pkgName: " + nameForUid);
        }
        GosLog.i(LOG_TAG, "getCallerPkgName(), callerUid: " + callingUid + ", callerPkgName: " + nameForUid);
        return nameForUid;
    }

    public static int getPkgUid(String str) {
        int i;
        try {
            i = AppContext.get().getPackageManager().getApplicationInfo(str, 128).uid;
        } catch (PackageManager.NameNotFoundException e) {
            GosLog.w(LOG_TAG, "getUid. PackageManager.NameNotFoundException: " + e.getMessage());
            i = -1;
        }
        GosLog.i(LOG_TAG, "pkgName = " + str + ", uid = " + i);
        return i;
    }

    public static void setUidForPkgMap(Map<String, Integer> map) {
        PackageManager packageManager = AppContext.get().getPackageManager();
        for (String next : map.keySet()) {
            try {
                map.put(next, Integer.valueOf(packageManager.getApplicationInfo(next, 128).uid));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
    }

    public static boolean isTopPackage(Context context, String str) {
        String[] topProcessPkg = SeActivityManager.getInstance().getTopProcessPkg(context);
        if (topProcessPkg == null) {
            return false;
        }
        for (String str2 : topProcessPkg) {
            if (str.equals(str2)) {
                return true;
            }
            GosLog.i(LOG_TAG, "isTopPackage(), pkg = " + str2);
        }
        return false;
    }
}
