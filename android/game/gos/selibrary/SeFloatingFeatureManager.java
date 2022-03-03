package com.samsung.android.game.gos.selibrary;

import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;

public class SeFloatingFeatureManager {
    private static final String LOG_TAG = SeFloatingFeatureManager.class.getSimpleName();
    private String mDeviceResolution;

    private SeFloatingFeatureManager() {
        this.mDeviceResolution = null;
        if (AppVariable.isUnitTest()) {
            this.mDeviceResolution = Constants.MULTI_RESOLUTION;
            return;
        }
        try {
            this.mDeviceResolution = SemFloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_COMMON_CONFIG_DYN_RESOLUTION_CONTROL");
        } catch (IllegalStateException | NullPointerException e) {
            GosLog.w(LOG_TAG, e);
        }
    }

    public static SeFloatingFeatureManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeFloatingFeatureManager INSTANCE = new SeFloatingFeatureManager();

        private SingletonHolder() {
        }
    }

    public String getDeviceResolution() {
        return this.mDeviceResolution;
    }
}
