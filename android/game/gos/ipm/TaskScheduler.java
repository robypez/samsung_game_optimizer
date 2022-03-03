package com.samsung.android.game.gos.ipm;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
    private final ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    public void shutdown() {
        this.mScheduledThreadPoolExecutor.shutdown();
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        return this.mScheduledThreadPoolExecutor.scheduleWithFixedDelay(runnable, j, j2, timeUnit);
    }
}
