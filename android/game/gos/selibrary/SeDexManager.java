package com.samsung.android.game.gos.selibrary;

import com.samsung.android.desktopmode.SemDesktopModeManager;
import com.samsung.android.desktopmode.SemDesktopModeState;
import com.samsung.android.game.gos.context.AppContext;

public class SeDexManager {
    private SemDesktopModeManager.DesktopModeListener mDesktopModeListener;
    private SemDesktopModeManager mDesktopModeManager;
    /* access modifiers changed from: private */
    public boolean mDexEnabled;

    private SeDexManager() {
        this.mDexEnabled = false;
        SemDesktopModeManager semDesktopModeManager = (SemDesktopModeManager) AppContext.get().getSystemService(SemDesktopModeManager.class);
        this.mDesktopModeManager = semDesktopModeManager;
        if (semDesktopModeManager != null) {
            AnonymousClass1 r0 = new SemDesktopModeManager.DesktopModeListener() {
                public void onDesktopModeStateChanged(SemDesktopModeState semDesktopModeState) {
                    boolean unused = SeDexManager.this.mDexEnabled = semDesktopModeState.enabled == 4;
                }
            };
            this.mDesktopModeListener = r0;
            this.mDesktopModeManager.registerListener(r0);
        }
    }

    public static SeDexManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeDexManager INSTANCE = new SeDexManager();

        private SingletonHolder() {
        }
    }

    public boolean isDexEnabled() {
        return this.mDexEnabled;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        SemDesktopModeManager semDesktopModeManager = this.mDesktopModeManager;
        if (semDesktopModeManager != null) {
            semDesktopModeManager.unregisterListener(this.mDesktopModeListener);
        }
        super.finalize();
    }
}
