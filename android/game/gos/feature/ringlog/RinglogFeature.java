package com.samsung.android.game.gos.feature.ringlog;

import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;

public class RinglogFeature implements RuntimeInterface {
    private static final String LOG_TAG = RinglogFeature.class.getSimpleName();

    public String getName() {
        return Constants.V4FeatureFlag.RINGLOG;
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    public static RinglogFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        static RinglogFeature INSTANCE = new RinglogFeature();

        private SingletonHolder() {
        }
    }

    private RinglogFeature() {
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onResume(), isCreate: ");
        sb.append(z);
        sb.append(", pkgData: ");
        sb.append(pkgData != null ? pkgData.getPackageName() : null);
        GosLog.d(str, sb.toString());
    }

    public void onFocusOut(PkgData pkgData) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPause(), pkgData: ");
        sb.append(pkgData != null ? pkgData.getPackageName() : null);
        GosLog.d(str, sb.toString());
        boolean saveRinglogSessionToDb = RinglogUtil.saveRinglogSessionToDb();
        String str2 = LOG_TAG;
        GosLog.d(str2, "onPause, savingSuccessfully: " + saveRinglogSessionToDb);
    }

    public void restoreDefault(PkgData pkgData) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("restoreDefault(), pkgData: ");
        sb.append(pkgData != null ? pkgData.getPackageName() : null);
        GosLog.d(str, sb.toString());
    }
}
