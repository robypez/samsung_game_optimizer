package com.samsung.android.game.gos.feature.externalsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.externalsdk.value.Const;
import com.samsung.android.game.gos.util.GosLog;

class SsrmReport implements IReportStrategy {
    private static final String ACTION_CHECK_SIOP_LEVEL_M = "android.intent.action.CHECK_SIOP_LEVEL";
    private static final String ACTION_CHECK_SIOP_LEVEL_N = "com.samsung.intent.action.CHECK_SIOP_LEVEL";
    private static final String EXTRA_SIOP_LEVEL = "siop_level_broadcast";
    private static final int GAME_PAUSE_RESULT = -1001;
    private static final int GAME_RESUME_RESULT = -1002;
    private static final String LOG_TAG = "SsrmReport";
    private static final int SIOP_LEVEL_UNKNOWN = -1000;
    private boolean mIsRegistered = false;
    /* access modifiers changed from: private */
    public IExternalSdkListener mListener = null;
    /* access modifiers changed from: private */
    public int mPrevTempLevel = -1000;
    private final BroadcastReceiver mSsrmReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int intExtra;
            String action = intent.getAction();
            if (action == null) {
                GosLog.e(SsrmReport.LOG_TAG, "action is null");
                return;
            }
            char c = 65535;
            int hashCode = action.hashCode();
            if (hashCode != 95359098) {
                if (hashCode == 676060760 && action.equals(SsrmReport.ACTION_CHECK_SIOP_LEVEL_M)) {
                    c = 0;
                }
            } else if (action.equals(SsrmReport.ACTION_CHECK_SIOP_LEVEL_N)) {
                c = 1;
            }
            if ((c == 0 || c == 1) && (intExtra = intent.getIntExtra(SsrmReport.EXTRA_SIOP_LEVEL, -1000)) != -1000 && SsrmReport.this.mListener != null) {
                SsrmReport.this.mListener.onSiopLevelChanged(SsrmReport.this.mPrevTempLevel, intExtra);
                int unused = SsrmReport.this.mPrevTempLevel = intExtra;
            }
        }
    };

    public boolean isAvailable() {
        return true;
    }

    SsrmReport() {
    }

    public long setListener(long j, IExternalSdkListener iExternalSdkListener) {
        if (!Const.ReportFlags.SIOP_LEVEL.isPresent(j)) {
            return j;
        }
        long removeFrom = Const.ReportFlags.SIOP_LEVEL.removeFrom(j);
        this.mListener = iExternalSdkListener;
        return removeFrom;
    }

    public boolean startWatching() {
        if (!this.mIsRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_CHECK_SIOP_LEVEL_M);
            intentFilter.addAction(ACTION_CHECK_SIOP_LEVEL_N);
            AppContext.get().registerReceiver(this.mSsrmReceiver, intentFilter);
        }
        this.mIsRegistered = true;
        IExternalSdkListener iExternalSdkListener = this.mListener;
        if (iExternalSdkListener != null) {
            iExternalSdkListener.onResult(GAME_RESUME_RESULT, 0);
        }
        return true;
    }

    public boolean stopWatching() {
        if (this.mIsRegistered) {
            try {
                AppContext.get().unregisterReceiver(this.mSsrmReceiver);
            } catch (IllegalArgumentException e) {
                GosLog.d(LOG_TAG, e.getMessage());
            }
        }
        this.mIsRegistered = false;
        IExternalSdkListener iExternalSdkListener = this.mListener;
        if (iExternalSdkListener == null) {
            return true;
        }
        iExternalSdkListener.onResult(GAME_PAUSE_RESULT, 0);
        return true;
    }
}
