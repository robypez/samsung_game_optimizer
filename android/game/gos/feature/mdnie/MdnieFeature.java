package com.samsung.android.game.gos.feature.mdnie;

import android.app.Application;
import android.content.Intent;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.selibrary.SeDisplaySolutionManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;

public class MdnieFeature implements RuntimeInterface {
    static final String ACTION_GAME_MODE_STATE_IN = "ACTION_GAME_MODE_STATE_IN";
    static final String ACTION_GAME_MODE_STATE_OUT = "ACTION_GAME_MODE_STATE_OUT";
    private static final String LOG_TAG = MdnieFeature.class.getSimpleName();

    public String getName() {
        return Constants.V4FeatureFlag.MDNIE;
    }

    public static MdnieFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private MdnieFeature() {
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        Intent intent = new Intent();
        intent.setAction(ACTION_GAME_MODE_STATE_IN);
        Application application = AppContext.get();
        if (application != null) {
            application.sendBroadcast(intent, "android.permission.HARDWARE_TEST");
        }
        GosLog.i(LOG_TAG, "onResume(), sendBroadcast. ACTION_GAME_MODE_STATE_IN");
    }

    public void onFocusOut(PkgData pkgData) {
        restoreDefault(pkgData);
    }

    public void restoreDefault(PkgData pkgData) {
        Intent intent = new Intent();
        intent.setAction(ACTION_GAME_MODE_STATE_OUT);
        Application application = AppContext.get();
        if (application != null) {
            application.sendBroadcast(intent, "android.permission.HARDWARE_TEST");
        }
        GosLog.i(LOG_TAG, "restoreDefault(), sendBroadcast. ACTION_GAME_MODE_STATE_OUT");
    }

    public boolean isAvailableForSystemHelper() {
        return SeDisplaySolutionManager.getInstance().isMdnieScenarioControlServiceEnabled();
    }

    private static class SingletonHolder {
        public static MdnieFeature INSTANCE = new MdnieFeature();

        private SingletonHolder() {
        }
    }
}
