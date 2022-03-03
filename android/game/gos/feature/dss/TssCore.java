package com.samsung.android.game.gos.feature.dss;

import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.Constants;
import java.util.Arrays;

public class TssCore {
    private static final String LOG_TAG = TssCore.class.getSimpleName();

    public static boolean isValidShortSide(int i) {
        return i > 0;
    }

    private TssCore() {
    }

    static float getDssByShortSide(int i) {
        int displayShortSide = DssFeature.getInstance().getDisplayShortSide();
        if (!isValidShortSide(displayShortSide) || !isValidShortSide(i)) {
            String str = LOG_TAG;
            GosLog.w(str, "getDssByShortSide(). input error, displayShortSide : " + displayShortSide + ", targetShortSide : " + i);
            return 100.0f;
        }
        float f = (((float) i) * 100.0f) / ((float) displayShortSide);
        float validDss = ValidationUtil.getValidDss(f);
        String str2 = LOG_TAG;
        GosLog.i(str2, "getDssByShortSide(). displayShortSide : " + displayShortSide + ", targetShortSide : " + i + ", tempDss : " + f + ", resultDss : " + validDss);
        return validDss;
    }

    public static boolean isAvailable() {
        int displayShortSide = DssFeature.getInstance().getDisplayShortSide();
        int[] csvToInts = TypeConverter.csvToInts(DbHelper.getInstance().getGlobalDao().getEachModeTargetShortSide());
        String str = LOG_TAG;
        GosLog.d(str, "displayShortSide :  " + displayShortSide + ", globalEachModeTargetShortSide : " + Arrays.toString(csvToInts));
        return _isAvailable(displayShortSide, csvToInts);
    }

    static boolean _isAvailable(int i, int[] iArr) {
        if (!FeatureHelper.isAvailable(Constants.V4FeatureFlag.RESOLUTION) || !isValidShortSide(i) || !isValidEachModeShortSide(iArr)) {
            return false;
        }
        GosLog.d(LOG_TAG, "global target short side is available.");
        return true;
    }

    private static int[] getMergedEachModeTss(PkgData pkgData) {
        int[] csvToInts = TypeConverter.csvToInts(DbHelper.getInstance().getGlobalDao().getEachModeTargetShortSide());
        if (!isValidEachModeShortSide(csvToInts)) {
            return null;
        }
        int[] eachModeTargetShortSideArray = pkgData.getPkg().getEachModeTargetShortSideArray();
        if (eachModeTargetShortSideArray != null) {
            int i = 0;
            while (i < eachModeTargetShortSideArray.length && i < csvToInts.length) {
                if (isValidShortSide(eachModeTargetShortSideArray[i])) {
                    csvToInts[i] = eachModeTargetShortSideArray[i];
                }
                if (i > 0) {
                    int i2 = i - 1;
                    if (csvToInts[i2] < csvToInts[i]) {
                        csvToInts[i] = csvToInts[i2];
                    }
                }
                i++;
            }
        }
        GosLog.i(LOG_TAG, "getMergedEachModeTss()-merged: " + Arrays.toString(csvToInts) + ", " + pkgData.getPackageName());
        return csvToInts;
    }

    public static int getDefaultTss(PkgData pkgData) {
        int[] mergedEachModeTss = getMergedEachModeTss(pkgData);
        if (mergedEachModeTss == null || mergedEachModeTss.length <= 1) {
            return -1;
        }
        return mergedEachModeTss[1];
    }

    public static int getTssValueByMode(int i, PkgData pkgData) {
        int[] mergedEachModeTss = getMergedEachModeTss(pkgData);
        if (!isValidEachModeShortSide(mergedEachModeTss) || mergedEachModeTss.length <= i) {
            return -1;
        }
        return mergedEachModeTss[i];
    }

    private static boolean isValidEachModeShortSide(int[] iArr) {
        if (iArr == null || iArr.length != 4) {
            return false;
        }
        for (int isValidShortSide : iArr) {
            if (!isValidShortSide(isValidShortSide)) {
                return false;
            }
        }
        return true;
    }
}
