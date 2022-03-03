package com.samsung.android.game.gos.feature.externalsdk;

import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.externalsdk.value.Const;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.selibrary.SeDvfsInterfaceImpl;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.NetworkConstants;

public class SpaControl implements IControlStrategy {
    private static boolean DEBUG_INFO = false;
    private static final String LOG_TAG = "SpaControl";
    private boolean mBusBoostAvailable = false;
    private int[] mBusFreq;
    private SeDvfsInterfaceImpl mBusHelper;
    private int mMaxFps = 60;

    private void SLOGI(String str) {
        if (DEBUG_INFO) {
            GosLog.d(LOG_TAG, str);
        }
    }

    public SpaControl() {
        SeDvfsInterfaceImpl createInstance = SeDvfsInterfaceImpl.createInstance(AppContext.get(), SeDvfsInterfaceImpl.TYPE_BUS_MIN);
        this.mBusHelper = createInstance;
        this.mBusFreq = createInstance.getSupportedFrequency();
    }

    public boolean isAvailable() {
        SLOGI("isAvailableFeatureFlag.");
        IpmCore instanceUnsafe = IpmCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            return instanceUnsafe.isRunning();
        }
        return false;
    }

    public long getControlFlags() {
        SLOGI("getControlFlags");
        return Const.ControlFlags.BOOST.value() | Const.ControlFlags.PERFORMANCE_LEVEL.value();
    }

    public boolean setBoost() {
        SLOGI("setBoost.");
        IpmCore instanceUnsafe = IpmCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            instanceUnsafe.setTargetFps((float) this.mMaxFps);
        }
        int[] iArr = this.mBusFreq;
        if (iArr != null && iArr.length > 0) {
            SLOGI("boosting with busFreq for " + this.mBusFreq[0]);
            this.mBusHelper.setDvfsValue((long) this.mBusFreq[0]);
            this.mBusHelper.acquire(NetworkConstants.CONNECTION_TIMEOUT);
            this.mBusBoostAvailable = true;
            if (instanceUnsafe != null) {
                instanceUnsafe.loadingBoostMode(true);
            }
        }
        return true;
    }

    public void releaseBoost() {
        SLOGI("releaseBoost");
        IpmCore instanceUnsafe = IpmCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            instanceUnsafe.setTargetFps(-1.0f);
            instanceUnsafe.loadingBoostMode(false);
        }
        if (this.mBusBoostAvailable) {
            this.mBusHelper.release();
            this.mBusBoostAvailable = false;
        }
    }

    public boolean setPerformanceLevel(int i) {
        SLOGI("setPerformanceLevel.  level: " + i);
        IpmCore instanceUnsafe = IpmCore.getInstanceUnsafe();
        if (instanceUnsafe == null) {
            return false;
        }
        instanceUnsafe.updateGameSDK(i);
        return true;
    }

    public void releasePerformanceLevel() {
        SLOGI("releasePerformanceLevel");
        IpmCore instanceUnsafe = IpmCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            instanceUnsafe.updateGameSDK(0);
        }
    }

    public boolean setCpuLevel(int i) {
        SLOGI("setCpuLevel. level: " + i);
        return false;
    }

    public void releaseCpuLevel() {
        SLOGI("releaseCpuLevel");
    }

    public boolean setGpuLevel(int i) {
        SLOGI("setGpuLevel. level: " + i);
        return false;
    }

    public void releaseGpuLevel() {
        SLOGI("releaseGpuLevel");
    }

    public void releaseAll() {
        SLOGI("releaseAll.");
        releaseBoost();
        releasePerformanceLevel();
    }

    public void setMaxFps(int i) {
        this.mMaxFps = i;
    }

    public int getMaxFps() {
        return this.mMaxFps;
    }
}
