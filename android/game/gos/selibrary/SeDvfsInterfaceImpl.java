package com.samsung.android.game.gos.selibrary;

import android.content.Context;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.os.SemDvfsManager;

public abstract class SeDvfsInterfaceImpl {
    protected static final int[] DEF_FREQUENCY = {40000, 30000, 25000, 20000};
    public static final int TYPE_BUS_MAX;
    public static final int TYPE_BUS_MIN;
    public static final int TYPE_CPU_CORE_NUM_MAX;
    public static final int TYPE_CPU_CORE_NUM_MIN;
    public static final int TYPE_CPU_MAX;
    public static final int TYPE_CPU_MIN;
    public static final int TYPE_CPU_POWER_COLLAPSE_DISABLE;
    public static final int TYPE_CPU_PRIME_ENABLE = 268444016;
    public static final int TYPE_GPU_MAX;
    public static final int TYPE_GPU_MIN;
    private static final int VER_THRESHOLD = 120500;
    private static boolean isVer125;
    protected SemDvfsManager mInstance = null;
    protected int mType = 0;

    public abstract int[] getSupportedFrequency();

    public abstract int[] getSupportedFrequencyForSsrm();

    public abstract void setDvfsValue(long j);

    static {
        isVer125 = false;
        try {
            if (PlatformUtil.getSemPlatformVersion() >= VER_THRESHOLD) {
                isVer125 = true;
                TYPE_CPU_MIN = 301993985;
                TYPE_CPU_MAX = 301993986;
                TYPE_CPU_CORE_NUM_MIN = 268443651;
                TYPE_CPU_CORE_NUM_MAX = 268443652;
                TYPE_GPU_MIN = 536875009;
                TYPE_GPU_MAX = 536875010;
                TYPE_BUS_MIN = 805310465;
                TYPE_BUS_MAX = 805310466;
                TYPE_CPU_POWER_COLLAPSE_DISABLE = 268447744;
            } else {
                TYPE_CPU_MIN = 12;
                TYPE_CPU_MAX = 13;
                TYPE_CPU_CORE_NUM_MIN = 14;
                TYPE_CPU_CORE_NUM_MAX = 15;
                TYPE_GPU_MIN = 16;
                TYPE_GPU_MAX = 17;
                TYPE_BUS_MIN = 19;
                TYPE_BUS_MAX = 20;
                TYPE_CPU_POWER_COLLAPSE_DISABLE = 23;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not init constants.", e);
        }
    }

    public static SeDvfsInterfaceImpl createInstance(Context context, int i) {
        if (isVer125) {
            return SeDvfsInterfaceSE125.createInstance(context, i);
        }
        return SeDvfsInterfaceDeprecated.createInstance(context, i);
    }

    public void acquire(int i) {
        if (this.mInstance != null && !AppVariable.isUnitTest()) {
            this.mInstance.acquire(i);
        }
    }

    public void release() {
        if (this.mInstance != null && !AppVariable.isUnitTest()) {
            this.mInstance.release();
        }
    }
}
