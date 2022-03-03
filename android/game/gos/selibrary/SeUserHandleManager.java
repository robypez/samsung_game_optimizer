package com.samsung.android.game.gos.selibrary;

import android.os.UserHandle;
import com.samsung.android.game.gos.value.AppVariable;

public class SeUserHandleManager {
    private int mCallingUserId;
    private int mUserId;

    private SeUserHandleManager() {
        this.mUserId = 0;
        this.mCallingUserId = 0;
        if (!AppVariable.isUnitTest()) {
            try {
                this.mCallingUserId = UserHandle.semGetCallingUserId();
                this.mUserId = UserHandle.semGetMyUserId();
            } catch (NoSuchMethodError unused) {
            }
        }
    }

    public static SeUserHandleManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeUserHandleManager INSTANCE = new SeUserHandleManager();

        private SingletonHolder() {
        }
    }

    public int getMyUserId() throws RuntimeException {
        return this.mUserId;
    }

    public int getCallingUserId() throws RuntimeException {
        return this.mCallingUserId;
    }
}
