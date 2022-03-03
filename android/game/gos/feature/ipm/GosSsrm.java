package com.samsung.android.game.gos.feature.ipm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.ipm.Ssrm;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.RinglogConstants;
import dalvik.system.PathClassLoader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class GosSsrm implements Ssrm {
    private static final String ACTION_SPA_INTENT = "com.enhance.gameservice.SpaIntent";
    private static final String EXTRA_ACTIVATE = "Activate";
    private static final String EXTRA_PAUSE_ACTIONS = "PauseActions";
    private static final String EXTRA_PAUSE_MULTI_WINDOW = "PauseMultiWindow";
    private static final String LOG_TAG = "Ssrm";
    private static final String PERMISSION_SSRM_NOTIFICATION = "com.samsung.android.permission.SSRM_NOTIFICATION_PERMISSION";
    private BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    /* access modifiers changed from: private */
    public final ArrayList<Ssrm.Listener> mListeners = new ArrayList<>();
    private final Resources mResources;

    private class SsrmBroadcastReceiver extends BroadcastReceiver {
        private boolean mPauseActions;
        private boolean mPauseActionsMain;
        private boolean mPauseActionsMultiWindow;

        private SsrmBroadcastReceiver() {
            this.mPauseActionsMain = false;
            this.mPauseActionsMultiWindow = false;
            this.mPauseActions = false;
        }

        public void onReceive(Context context, Intent intent) {
            Bundle extras;
            String action = intent.getAction();
            if (action != null && action.equals(GosSsrm.ACTION_SPA_INTENT) && (extras = intent.getExtras()) != null) {
                if (extras.containsKey(GosSsrm.EXTRA_ACTIVATE)) {
                    boolean z = extras.getBoolean(GosSsrm.EXTRA_ACTIVATE);
                    Iterator it = GosSsrm.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((Ssrm.Listener) it.next()).onActivateChanged(z);
                    }
                }
                if (extras.containsKey(GosSsrm.EXTRA_PAUSE_ACTIONS)) {
                    this.mPauseActionsMain = extras.getBoolean(GosSsrm.EXTRA_PAUSE_ACTIONS);
                    GosLog.i(GosSsrm.LOG_TAG, "GosSsrm, onReceive: PauseActions, " + this.mPauseActionsMain);
                    updatePauseActions();
                }
                if (extras.containsKey(GosSsrm.EXTRA_PAUSE_MULTI_WINDOW)) {
                    this.mPauseActionsMultiWindow = extras.getBoolean(GosSsrm.EXTRA_PAUSE_MULTI_WINDOW);
                    GosLog.i(GosSsrm.LOG_TAG, "GosSsrm, onReceive: PauseMultiWindow, " + this.mPauseActionsMultiWindow);
                    updatePauseActions();
                }
            }
        }

        private void updatePauseActions() {
            boolean z = this.mPauseActionsMain || this.mPauseActionsMultiWindow;
            if (this.mPauseActions != z) {
                this.mPauseActions = z;
                Iterator it = GosSsrm.this.mListeners.iterator();
                while (it.hasNext()) {
                    ((Ssrm.Listener) it.next()).onPauseActionsChanged(z);
                }
            }
        }
    }

    public GosSsrm(Context context, Resources resources) {
        this.mContext = context;
        this.mResources = resources;
    }

    public void activate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SPA_INTENT);
        SsrmBroadcastReceiver ssrmBroadcastReceiver = new SsrmBroadcastReceiver();
        this.mBroadcastReceiver = ssrmBroadcastReceiver;
        this.mContext.registerReceiver(ssrmBroadcastReceiver, intentFilter, PERMISSION_SSRM_NOTIFICATION, (Handler) null);
    }

    public void deactivate() {
        try {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            GosLog.e(LOG_TAG, e.getMessage());
        } catch (Exception unused) {
        }
        this.mBroadcastReceiver = null;
    }

    public void register(Ssrm.Listener listener) {
        this.mListeners.add(listener);
    }

    public void deregister(Ssrm.Listener listener) {
        this.mListeners.remove(listener);
    }

    public boolean isInitialized() {
        return SeSysProp.getProp("dev.ssrm.init").equals("1");
    }

    public String getVersion() {
        return SeSysProp.getProp("dev.ssrm.version");
    }

    public int getPst() {
        String prop = SeSysProp.getProp("dev.ssrm.pst");
        if (prop == null || prop.equals(BuildConfig.VERSION_NAME)) {
            return -1;
        }
        try {
            return (int) Long.parseLong(prop);
        } catch (NumberFormatException e) {
            GosLog.d(LOG_TAG, "PST value has invalid format: " + e);
            return -1;
        }
    }

    public InputStream getConfigFile() {
        try {
            return this.mResources.openRawResource(this.mResources.getIdentifier((String) new PathClassLoader("/system/framework/services.jar", ClassLoader.getSystemClassLoader()).loadClass("com.android.server.SsrmService").getField("SSRM_FILENAME").get((Object) null), RinglogConstants.DataUploadMode.RAW, "android"));
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return null;
        }
    }
}
