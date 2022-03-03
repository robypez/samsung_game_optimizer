package com.samsung.android.game.gos.feature.mdswitchwifiblock;

import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;

public class MDSwitchWifiBlockFeature implements RuntimeInterface {
    private static final String LOG_TAG = "MDSwitchWifiBlockFeature";

    public String getName() {
        return Constants.V4FeatureFlag.MD_SWITCH_WIFI;
    }

    private MDSwitchWifiBlockFeature() {
    }

    public static MDSwitchWifiBlockFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static MDSwitchWifiBlockFeature INSTANCE = new MDSwitchWifiBlockFeature();

        private SingletonHolder() {
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        MDSwitchWifiBlockCore.getInstance().onFocusIn(pkgData);
    }

    public void onFocusOut(PkgData pkgData) {
        MDSwitchWifiBlockCore.getInstance().onFocusOut(pkgData);
    }

    public void restoreDefault(PkgData pkgData) {
        GosLog.i(LOG_TAG, "restoreDefault");
        if (pkgData != null) {
            MDSwitchWifiBlockCore.getInstance().reset();
        }
    }

    public boolean isAvailableForSystemHelper() {
        return DbHelper.getInstance().getGlobalDao().getGmsVersion() >= 110.015f;
    }
}
