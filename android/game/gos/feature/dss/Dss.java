package com.samsung.android.game.gos.feature.dss;

import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import java.util.Arrays;
import java.util.Locale;

public class Dss {
    private static final String LOG_TAG = "Dss";

    private Dss() {
    }

    public static Dss getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final Dss INSTANCE = new Dss();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: package-private */
    public SemPackageConfiguration getUpdatedConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration) {
        boolean isUsingUserValue = FeatureHelper.isUsingUserValue(Constants.V4FeatureFlag.RESOLUTION);
        boolean isUsingPkgValue = FeatureHelper.isUsingPkgValue(Constants.V4FeatureFlag.RESOLUTION);
        float dssValueForCurrentMode = getDssValueForCurrentMode(getActualResolutionMode(pkgData, isUsingUserValue, isUsingPkgValue), pkgData, isUsingUserValue, isUsingPkgValue);
        semPackageConfiguration.setDynamicSurfaceScaling(dssValueForCurrentMode / 100.0f);
        DbHelper.getInstance().getPackageDao().setAppliedDss(new Package.PkgNameAndAppliedDss(pkgData.getPackageName(), dssValueForCurrentMode));
        return semPackageConfiguration;
    }

    /* access modifiers changed from: package-private */
    public SemPackageConfiguration getDefaultConfig(SemPackageConfiguration semPackageConfiguration) {
        if (semPackageConfiguration != null) {
            semPackageConfiguration.setDynamicSurfaceScaling(1.0f);
        }
        return semPackageConfiguration;
    }

    public static int getActualResolutionMode(PkgData pkgData, boolean z, boolean z2) {
        if (!z) {
            return 1;
        }
        if (!z2) {
            return DbHelper.getInstance().getGlobalDao().getResolutionMode();
        }
        return pkgData.getPkg().getCustomResolutionMode();
    }

    public float getDssValueForCurrentMode(int i, PkgData pkgData, boolean z, boolean z2) {
        if (pkgData == null) {
            GosLog.w(LOG_TAG, "getDssValueForCurrentMode(). pkgData is null");
            return 100.0f;
        }
        if (PlatformUtil.isMultiResolutionSupported()) {
            GosLog.i(LOG_TAG, "getDssValueForCurrentMode(). is multi resolution");
        }
        if (!TssCore.isAvailable()) {
            return getDssByMode(i, pkgData, z, z2);
        }
        GosLog.i(LOG_TAG, "getDssValueForCurrentMode(). global target short side is available.");
        return getDssFromTssByMode("getDssValueForCurrentMode(). ", i, pkgData, z, z2);
    }

    /* access modifiers changed from: package-private */
    public float getDssFromTssByMode(String str, int i, PkgData pkgData, boolean z, boolean z2) {
        float f;
        float f2;
        int[] csvToInts = TypeConverter.csvToInts(DbHelper.getInstance().getGlobalDao().getEachModeTargetShortSide());
        if (csvToInts == null || csvToInts.length <= 1) {
            f = 100.0f;
        } else {
            int i2 = csvToInts[1];
            GosLog.d(LOG_TAG, str + "displayShortSide : " + DssFeature.getInstance().getDisplayShortSide() + ", globalDefaultTargetShortSide : " + i2 + ", globalEachModeTargetShortSide : " + Arrays.toString(csvToInts));
            f = TssCore.getDssByShortSide(i2);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("default target short side is ");
            sb.append(i2);
            sb.append(", default dss from globalDefaultTargetShortSide : ");
            sb.append(f);
            GosLog.d(LOG_TAG, sb.toString());
        }
        if (i == 4) {
            if (z) {
                if (!z2) {
                    f2 = DbHelper.getInstance().getGlobalDao().getCustomDss();
                } else {
                    f2 = pkgData.getPkg().getCustomDss();
                }
                f = f2;
            }
            GosLog.i(LOG_TAG, str + "dss for custom mode : " + f);
            return f;
        }
        int tssValueByMode = TssCore.getTssValueByMode(i, pkgData);
        if (TssCore.isValidShortSide(tssValueByMode)) {
            float dssByShortSide = TssCore.getDssByShortSide(tssValueByMode);
            GosLog.i(LOG_TAG, str + "mode level : " + i + ", target short side is " + tssValueByMode + ", dss for other modes : " + dssByShortSide);
            return dssByShortSide;
        }
        GosLog.e(LOG_TAG, String.format(str + " there is no value for mode %d, pkgName: %s", new Object[]{Integer.valueOf(i), pkgData.getPackageName()}));
        return f;
    }

    public static float getDssByMode(int i, PkgData pkgData, boolean z, boolean z2) {
        float defaultDss = DssFeature.getDefaultDss(pkgData);
        if (i != 4) {
            float[] mergedEachModeDss = DssFeature.getMergedEachModeDss(pkgData);
            if (mergedEachModeDss.length > i) {
                return mergedEachModeDss[i];
            }
            GosLog.e(LOG_TAG, String.format(Locale.US, "getDssByMode(): there is no value for mode %d, pkgName: %s", new Object[]{Integer.valueOf(i), pkgData.getPackageName()}));
            return defaultDss;
        } else if (!z) {
            return defaultDss;
        } else {
            if (!z2) {
                return DbHelper.getInstance().getGlobalDao().getCustomDss();
            }
            return pkgData.getPkg().getCustomDss();
        }
    }
}
