package com.samsung.android.game.gos.feature.gfi.value;

import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.vrr.VrrFeature;
import com.samsung.android.game.gos.util.GosLog;

public class GfiMaxGameFps {
    private static final String LOG_TAG = "GfiMaxGameFps";

    public static int getMaxGameFps(Package packageR) {
        int i;
        if (VrrFeature.getInstance() != null) {
            i = VrrFeature.getInstance().publicGetSystemVrrMax();
        } else {
            GosLog.w(LOG_TAG, "getMaxGameFps: VrrFeature is null, deviceVrrMax defaulting to 60");
            i = 60;
        }
        int vrrMaxValue = packageR.getVrrMaxValue();
        GosLog.d(LOG_TAG, "deviceVrrMax: " + i + ", pkgVrrMax: " + vrrMaxValue);
        return (vrrMaxValue > i || vrrMaxValue <= 0) ? i : vrrMaxValue;
    }
}
