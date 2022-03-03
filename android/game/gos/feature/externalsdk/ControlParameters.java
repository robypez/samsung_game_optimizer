package com.samsung.android.game.gos.feature.externalsdk;

import android.os.Handler;
import android.os.Looper;

public class ControlParameters {
    private boolean boost = false;
    private Runnable boostRunnable;
    private Integer cpuLevel = null;
    private Runnable cpuLevelRunnable;
    private Integer gpuLevel = null;
    private Runnable gpuLevelRunnable;
    private ControlParametersListener mCallback = null;
    private Integer performanceLevel = null;
    private Runnable performanceLevelRunnable;
    private Handler runnableHandler;

    ControlParameters(ControlParametersListener controlParametersListener) {
        this.mCallback = controlParametersListener;
        this.runnableHandler = new Handler(Looper.getMainLooper());
        this.boostRunnable = new Runnable() {
            public void run() {
                ControlParameters.this.setBoost(false, -1);
            }
        };
        this.performanceLevelRunnable = new Runnable() {
            public void run() {
                ControlParameters.this.setPerformanceLevel((Integer) null, -1);
            }
        };
        this.cpuLevelRunnable = new Runnable() {
            public void run() {
                ControlParameters.this.setCpuLevel((Integer) null, -1);
            }
        };
        this.gpuLevelRunnable = new Runnable() {
            public void run() {
                ControlParameters.this.setGpuLevel((Integer) null, -1);
            }
        };
    }

    public synchronized void paramsChanged() {
        this.mCallback.controlParamsChanged(this.boost, this.performanceLevel, this.cpuLevel, this.gpuLevel);
    }

    public synchronized void setBoost(boolean z, int i) {
        if (i > 0) {
            this.runnableHandler.removeCallbacks(this.boostRunnable);
            this.runnableHandler.postDelayed(this.boostRunnable, ((long) i) * 1000);
        }
        if (z != this.boost) {
            this.boost = z;
            paramsChanged();
        }
    }

    public synchronized void setPerformanceLevel(Integer num, int i) {
        if (i > 0) {
            this.runnableHandler.removeCallbacks(this.performanceLevelRunnable);
            this.runnableHandler.postDelayed(this.performanceLevelRunnable, ((long) i) * 1000);
        }
        if (this.performanceLevel == null || !this.performanceLevel.equals(num)) {
            this.performanceLevel = num;
            paramsChanged();
        }
    }

    public synchronized void setCpuLevel(Integer num, int i) {
        if (i > 0) {
            this.runnableHandler.removeCallbacks(this.cpuLevelRunnable);
            this.runnableHandler.postDelayed(this.cpuLevelRunnable, ((long) i) * 1000);
        }
        if (this.cpuLevel == null || !this.cpuLevel.equals(num)) {
            this.cpuLevel = num;
            paramsChanged();
        }
    }

    public synchronized void setGpuLevel(Integer num, int i) {
        if (i > 0) {
            this.runnableHandler.removeCallbacks(this.gpuLevelRunnable);
            this.runnableHandler.postDelayed(this.gpuLevelRunnable, ((long) i) * 1000);
        }
        if (this.gpuLevel == null || !this.gpuLevel.equals(num)) {
            this.gpuLevel = num;
            paramsChanged();
        }
    }
}
