package com.samsung.android.game.gos.feature.touchboost;

import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.feature.dss.Dss;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.feature.dss.TssCore;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import java.util.Locale;

class TouchBoost {
    private static final String LOG_TAG = "TouchBoost";

    TouchBoost() {
    }

    /* access modifiers changed from: package-private */
    public String getTouchBoostCommand(PkgData pkgData) {
        boolean isUsingUserValue = FeatureHelper.isUsingUserValue(Constants.V4FeatureFlag.RESOLUTION);
        boolean isUsingPkgValue = FeatureHelper.isUsingPkgValue(Constants.V4FeatureFlag.RESOLUTION);
        int actualResolutionMode = Dss.getActualResolutionMode(pkgData, isUsingUserValue, isUsingPkgValue);
        int tss = TssCore.isAvailable() ? getTss(pkgData, actualResolutionMode) : -1;
        if (tss != -1) {
            return getParamValueByTss(tss);
        }
        return getParamValueByDss((int) Dss.getDssByMode(actualResolutionMode, pkgData, isUsingUserValue, isUsingPkgValue));
    }

    /* access modifiers changed from: package-private */
    public int getTss(PkgData pkgData, int i) {
        if (i != 4) {
            int tssValueByMode = TssCore.getTssValueByMode(i, pkgData);
            if (TssCore.isValidShortSide(tssValueByMode)) {
                return tssValueByMode;
            }
            GosLog.e(LOG_TAG, String.format(Locale.US, "getTss(), there is no value for mode %d, pkgName: %s", new Object[]{Integer.valueOf(i), pkgData.getPackageName()}));
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public String getParamValueByTss(int i) {
        GosLog.i(LOG_TAG, "getParamValueByTss " + i);
        int displayShortSide = DssFeature.getInstance().getDisplayShortSide();
        if (1260 >= i || i > 1440) {
            if (720 >= i || i > 1260) {
                if (i <= 0 || i > 720) {
                    return "off_game_touch_booster";
                }
            } else if (displayShortSide > 720) {
                return "mid_game_touch_booster";
            }
        } else if (displayShortSide > 720) {
            return displayShortSide <= 1080 ? "mid_game_touch_booster" : "high_game_touch_booster";
        }
        return "low_game_touch_booster";
    }

    /* access modifiers changed from: package-private */
    public String getParamValueByDss(int i) {
        GosLog.i(LOG_TAG, "getParamValueByDss " + i);
        if (85 < i && i <= 100) {
            return "high_game_touch_booster";
        }
        if (50 < i && i <= 85) {
            return "mid_game_touch_booster";
        }
        if (i > 0 && i <= 50) {
            return "low_game_touch_booster";
        }
        GosLog.e(LOG_TAG, "getParamValueByDss. unexpected dss: " + i);
        return "off_game_touch_booster";
    }
}
