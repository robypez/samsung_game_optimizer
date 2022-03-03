package com.samsung.android.game.gos.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorProvider {
    private static final int DB_THREAD_COUNT = 1;
    private ExecutorService mDbExecutor;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final ExecutorProvider INSTANCE = new ExecutorProvider();

        private SingletonHolder() {
        }
    }

    public static ExecutorProvider getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ExecutorService getDbExecutor() {
        if (this.mDbExecutor == null) {
            this.mDbExecutor = Executors.newFixedThreadPool(1);
        }
        return this.mDbExecutor;
    }
}
