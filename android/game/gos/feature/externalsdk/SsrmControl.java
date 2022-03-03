package com.samsung.android.game.gos.feature.externalsdk;

import android.app.Application;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.externalsdk.value.Const;
import com.samsung.android.game.gos.selibrary.SeDvfsInterfaceImpl;
import com.samsung.android.game.gos.util.GosLog;
import java.util.Arrays;

class SsrmControl implements IControlStrategy {
    private static boolean DEBUG_INFO = false;
    private static final String LOG_TAG = "SsrmControl";
    private boolean mBusBoostAvailable = false;
    private int[] mBusFreq;
    private SeDvfsInterfaceImpl mBusHelper;
    private int mBusIndex;
    private boolean mCpuBoostAvailable = false;
    private int[] mCpuFreq;
    private SeDvfsInterfaceImpl mCpuHelper;
    private int mCpuIndex;
    private boolean mGpuBoostAvailable = false;
    private SeDvfsInterfaceImpl mGpuHelper;
    private int mGpuIndex;

    public void releaseCpuLevel() {
    }

    public void releaseGpuLevel() {
    }

    public void setMaxFps(int i) {
    }

    private void SLOGI(String str) {
        if (DEBUG_INFO) {
            GosLog.d(LOG_TAG, str);
        }
    }

    public SsrmControl() {
        Application application = AppContext.get();
        this.mCpuHelper = SeDvfsInterfaceImpl.createInstance(application, SeDvfsInterfaceImpl.TYPE_CPU_MIN);
        this.mBusHelper = SeDvfsInterfaceImpl.createInstance(application, SeDvfsInterfaceImpl.TYPE_BUS_MIN);
        this.mGpuHelper = SeDvfsInterfaceImpl.createInstance(application, SeDvfsInterfaceImpl.TYPE_GPU_MIN);
        this.mCpuFreq = getSupportedCpuFreq();
        this.mBusFreq = getSupportedBusFreq();
    }

    public long getControlFlags() {
        SLOGI("getControlFlags");
        return Const.ControlFlags.PERFORMANCE_LEVEL.value();
    }

    public boolean isAvailable() {
        SLOGI("isAvailableFeatureFlag.");
        return true;
    }

    public boolean setBoost() {
        SLOGI("setBoost.");
        return true;
    }

    public void releaseBoost() {
        SLOGI("releaseBoost");
    }

    /* renamed from: com.samsung.android.game.gos.feature.externalsdk.SsrmControl$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType[] r0 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType = r0
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.NONE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.LOW     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.MID     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.HIGH     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x003e }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.ULTRA     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.CRITICAL     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.externalsdk.SsrmControl.AnonymousClass1.<clinit>():void");
        }
    }

    public boolean setPerformanceLevel(int i) {
        SLOGI("setPerformanceLevel. level: " + i);
        int i2 = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType[Const.ApplyType.values()[i].ordinal()];
        if (i2 == 1 || i2 == 2) {
            releasePerformanceLevel();
        } else if (i2 == 4 || i2 == 5 || i2 == 6) {
            int[] iArr = this.mCpuFreq;
            if (iArr != null && iArr.length > 0) {
                setCpuLevel(0);
            }
            int[] iArr2 = this.mBusFreq;
            if (iArr2 != null && iArr2.length > 0) {
                changeBusSettings(0);
            }
        }
        return true;
    }

    public void releasePerformanceLevel() {
        SLOGI("releasePerformanceLevel");
        SeDvfsInterfaceImpl seDvfsInterfaceImpl = this.mBusHelper;
        if (seDvfsInterfaceImpl != null && this.mBusBoostAvailable) {
            seDvfsInterfaceImpl.release();
            this.mBusBoostAvailable = false;
        }
        SeDvfsInterfaceImpl seDvfsInterfaceImpl2 = this.mCpuHelper;
        if (seDvfsInterfaceImpl2 != null && this.mCpuBoostAvailable) {
            seDvfsInterfaceImpl2.release();
            this.mCpuBoostAvailable = false;
        }
    }

    public boolean setCpuLevel(int i) {
        SLOGI("setCpuLevel. level: " + i);
        changeCpuSettings(i);
        return true;
    }

    public boolean setGpuLevel(int i) {
        SLOGI("setGpuLevel. level: " + i);
        changeGpuSettings(i);
        return true;
    }

    public void releaseAll() {
        SLOGI("releaseAll.");
        releasePerformanceLevel();
    }

    private boolean setCpuIndex(int i) {
        int i2;
        this.mCpuIndex = i;
        SeDvfsInterfaceImpl seDvfsInterfaceImpl = this.mCpuHelper;
        if (seDvfsInterfaceImpl != null) {
            int[] supportedFrequency = seDvfsInterfaceImpl.getSupportedFrequency();
            SLOGI("cpuFreq: " + Arrays.toString(supportedFrequency));
            if (supportedFrequency == null || supportedFrequency.length <= (i2 = this.mCpuIndex)) {
                this.mCpuBoostAvailable = false;
            } else {
                this.mCpuHelper.setDvfsValue((long) supportedFrequency[i2]);
                this.mCpuBoostAvailable = true;
            }
        }
        return this.mCpuBoostAvailable;
    }

    private boolean setBusIndex(int i) {
        int i2;
        this.mBusIndex = i;
        SeDvfsInterfaceImpl seDvfsInterfaceImpl = this.mBusHelper;
        if (seDvfsInterfaceImpl != null) {
            int[] supportedFrequency = seDvfsInterfaceImpl.getSupportedFrequency();
            SLOGI("busFreq: " + Arrays.toString(supportedFrequency));
            if (supportedFrequency == null || supportedFrequency.length <= (i2 = this.mBusIndex)) {
                this.mBusBoostAvailable = false;
            } else {
                this.mBusHelper.setDvfsValue((long) supportedFrequency[i2]);
                this.mBusBoostAvailable = true;
            }
        }
        return this.mBusBoostAvailable;
    }

    private boolean setGpuIndex(int i) {
        int i2;
        this.mGpuIndex = i;
        SeDvfsInterfaceImpl seDvfsInterfaceImpl = this.mGpuHelper;
        if (seDvfsInterfaceImpl != null) {
            int[] supportedFrequency = seDvfsInterfaceImpl.getSupportedFrequency();
            SLOGI("gpuFreq: " + Arrays.toString(supportedFrequency));
            if (supportedFrequency == null || supportedFrequency.length <= (i2 = this.mGpuIndex)) {
                this.mGpuBoostAvailable = false;
            } else {
                this.mGpuHelper.setDvfsValue((long) supportedFrequency[i2]);
                this.mGpuBoostAvailable = true;
            }
        }
        return this.mGpuBoostAvailable;
    }

    /* access modifiers changed from: package-private */
    public boolean changeCpuSettings(int i) {
        setCpuIndex(i);
        if (!this.mCpuBoostAvailable) {
            return false;
        }
        this.mCpuHelper.acquire(15000);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean changeBusSettings(int i) {
        setBusIndex(i);
        if (!this.mBusBoostAvailable) {
            return false;
        }
        this.mBusHelper.acquire(15000);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean changeGpuSettings(int i) {
        this.mGpuIndex = i;
        setGpuIndex(i);
        if (!this.mGpuBoostAvailable) {
            return false;
        }
        this.mGpuHelper.acquire(15000);
        return true;
    }

    public int[] getSupportedCpuFreq() {
        return this.mCpuHelper.getSupportedFrequency();
    }

    public int[] getSupportedBusFreq() {
        return this.mBusHelper.getSupportedFrequency();
    }
}
