package com.samsung.android.game.gos.feature.ipm;

import android.os.Binder;
import android.os.Process;
import com.samsung.android.game.gos.ipm.ActivityManager;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.util.GosLog;

public class GosActivityManager implements ActivityManager {
    private static final String LOG_TAG = "ActivityManager";
    private final Binder mForegroundToken = new Binder();
    private final SeActivityManager mSeActivityManager;

    public GosActivityManager(SeActivityManager seActivityManager) {
        this.mSeActivityManager = seActivityManager;
    }

    public void setIsForeground(boolean z) {
        try {
            this.mSeActivityManager.setProcessImportant(this.mForegroundToken, Process.myPid(), z);
        } catch (RuntimeException e) {
            GosLog.e(LOG_TAG, "Failed to set process foreground status: " + e);
        }
    }
}
