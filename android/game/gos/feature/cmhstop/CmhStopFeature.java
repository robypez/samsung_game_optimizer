package com.samsung.android.game.gos.feature.cmhstop;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;

public class CmhStopFeature implements RuntimeInterface {
    private static final String CMH_ACTION_INTENT = "com.samsung.cmh.action.CMH_EXECUTE";
    private static final String CMH_PACKAGE = "com.samsung.cmh";
    private static final String CMH_SERVICE = "com.samsung.cmh.service.CMHService";
    private static final String LOG_TAG = CmhStopFeature.class.getSimpleName();
    private static final int TYPE_PAUSE = 1;
    private static final int TYPE_RESUME = 0;

    public String getName() {
        return Constants.V4FeatureFlag.GALLERY_CMH_STOP;
    }

    public static CmhStopFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private CmhStopFeature() {
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        sendIntentToCmh(0);
    }

    public void onFocusOut(PkgData pkgData) {
        sendIntentToCmh(1);
    }

    public void restoreDefault(PkgData pkgData) {
        sendIntentToCmh(1);
    }

    public boolean isAvailableForSystemHelper() {
        PackageManager packageManager = AppContext.get().getPackageManager();
        if (packageManager != null) {
            try {
                packageManager.getPackageInfo(CMH_PACKAGE, 128);
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                GosLog.i(LOG_TAG, "CMH package is not exist");
            }
        }
        return false;
    }

    private void sendIntentToCmh(int i) {
        Intent intent = new Intent(CMH_ACTION_INTENT);
        intent.setClassName(CMH_PACKAGE, CMH_SERVICE);
        intent.putExtra("type", i);
        String str = LOG_TAG;
        GosLog.i(str, "before send intent to CMH. intent:" + intent.toString());
        try {
            ComponentName startService = AppContext.get().startService(intent);
            String str2 = LOG_TAG;
            GosLog.i(str2, "after send intent to CMH. componentName:" + startService);
        } catch (IllegalStateException | SecurityException e) {
            GosLog.e(LOG_TAG, "sendIntentToCmh()-failed to send intent to CMH, ", e);
        }
    }

    private static class SingletonHolder {
        public static CmhStopFeature INSTANCE = new CmhStopFeature();

        private SingletonHolder() {
        }
    }
}
