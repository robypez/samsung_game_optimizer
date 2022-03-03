package com.samsung.android.game.gos.feature.externalsdk;

import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.value.Constants;

public class ExternalSdkFeature implements RuntimeInterface {
    private static final String LOG_TAG = "ExternalSdkFeature";
    private static ExternalSdkFeature mInstance = new ExternalSdkFeature();

    public String getName() {
        return Constants.V4FeatureFlag.EXTERNAL_SDK;
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    private ExternalSdkFeature() {
    }

    public static ExternalSdkFeature getInstance() {
        return mInstance;
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        ExternalSdkCore instanceUnsafe = ExternalSdkCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            instanceUnsafe.onFocusIn(pkgData);
        }
    }

    public void onFocusOut(PkgData pkgData) {
        ExternalSdkCore instanceUnsafe = ExternalSdkCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            instanceUnsafe.onFocusOut(pkgData);
        }
    }

    public void restoreDefault(PkgData pkgData) {
        ExternalSdkCore instanceUnsafe = ExternalSdkCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            instanceUnsafe.restoreDefault();
        }
    }
}
