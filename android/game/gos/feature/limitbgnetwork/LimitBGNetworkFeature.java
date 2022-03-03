package com.samsung.android.game.gos.feature.limitbgnetwork;

import android.os.Build;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.feature.RuntimeInterface;

public class LimitBGNetworkFeature implements RuntimeInterface {
    public static final String FEATURE_NAME = "limit_bg_network";

    public String getName() {
        return "limit_bg_network";
    }

    private LimitBGNetworkFeature() {
    }

    public static LimitBGNetworkFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        LimitBGNetworkCore.getInstance().onFocusIn(pkgData);
    }

    public void onFocusOut(PkgData pkgData) {
        LimitBGNetworkCore.getInstance().onFocusOut();
    }

    public void restoreDefault(PkgData pkgData) {
        LimitBGNetworkCore.getInstance().reset();
    }

    public boolean isAvailableForSystemHelper() {
        float gmsVersion = DbHelper.getInstance().getGlobalDao().getGmsVersion();
        if (Build.VERSION.SDK_INT == 29) {
            if (gmsVersion >= 100.02f) {
                return true;
            }
        } else if (Build.VERSION.SDK_INT <= 29 || gmsVersion < 110.003f) {
            return false;
        } else {
            return true;
        }
        return false;
    }

    public boolean resumeOk() {
        return LimitBGNetworkCore.getInstance().resumeOk();
    }

    public boolean pauseOk() {
        return LimitBGNetworkCore.getInstance().pauseOk();
    }

    public boolean resetOk() {
        return LimitBGNetworkCore.getInstance().resetOk();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final LimitBGNetworkFeature INSTANCE = new LimitBGNetworkFeature();

        private SingletonHolder() {
        }
    }
}
