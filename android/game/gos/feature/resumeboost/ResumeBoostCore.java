package com.samsung.android.game.gos.feature.resumeboost;

import android.app.Application;
import android.content.Intent;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeDvfsInterfaceImpl;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class ResumeBoostCore {
    private static final int BIG_TURBO_TIMEOUT_DEFAULT = 30;
    private static final String INTENT_GAME_CREATE = "com.samsung.android.game.intent.action.GAME_CREATE";
    private static final int LAUNCH_BOOST_BUS_INDEX_DEFAULT = 0;
    private static final int LAUNCH_BOOST_CPU_INDEX_DEFAULT = 0;
    public static final int LAUNCH_BOOST_TIMEOUT_DEFAULT = 10;
    private static final String LOG_TAG = "ResumeBoostCore";
    private boolean isGmsOk;
    private boolean mBigTurboAvailable;
    private int mBigTurboDurationSec;
    private boolean mBigTurboEnable;
    private boolean mBigTurboProperty;
    private boolean mBigTurboWorking;
    private boolean mBusBoostAvailable;
    private ArrayList<Integer> mBusFreqList;
    private SeDvfsInterfaceImpl mBusHelper;
    private int mBusIndex;
    private boolean mCpuBoostAvailable;
    private ArrayList<Integer> mCpuFreqList;
    private SeDvfsInterfaceImpl mCpuHelper;
    private int mCpuIndex;
    private int mDurationSec;
    private boolean mMinLockBoosterWorking;

    public static final class Policy {
        static final String BIG_TURBO_DURATION = "big_turbo_duration";
        static final String BIG_TURBO_POLICY = "big_turbo_policy";
        public static final String BUS_FREQ = "bus_freq";
        public static final String CPU_FREQ = "cpu_freq";
        public static final String DURATION = "duration";
    }

    /* synthetic */ ResumeBoostCore(AnonymousClass1 r1) {
        this();
    }

    private ResumeBoostCore() {
        this.mCpuBoostAvailable = false;
        this.mBusBoostAvailable = false;
        this.mMinLockBoosterWorking = false;
        this.mCpuFreqList = null;
        this.mBusFreqList = null;
        this.mBigTurboAvailable = false;
        this.mBigTurboEnable = false;
        this.mBigTurboWorking = false;
        this.mBigTurboProperty = false;
        this.mBigTurboDurationSec = 30;
        this.isGmsOk = false;
        Application application = AppContext.get();
        this.mDurationSec = 10;
        this.mCpuIndex = 0;
        this.mBusIndex = 0;
        updateGmsOk(DbHelper.getInstance().getGlobalDao().getGmsVersion());
        this.mCpuHelper = SeDvfsInterfaceImpl.createInstance(application, SeDvfsInterfaceImpl.TYPE_CPU_MIN);
        this.mBusHelper = SeDvfsInterfaceImpl.createInstance(application, SeDvfsInterfaceImpl.TYPE_BUS_MIN);
        SeDvfsInterfaceImpl seDvfsInterfaceImpl = this.mCpuHelper;
        if (seDvfsInterfaceImpl != null) {
            int[] supportedFrequency = seDvfsInterfaceImpl.getSupportedFrequency();
            if (supportedFrequency != null) {
                this.mCpuFreqList = TypeConverter.arrayToIntegerList(supportedFrequency);
            } else {
                GosLog.w(LOG_TAG, "cpuFreqArray is null");
            }
        }
        SeDvfsInterfaceImpl seDvfsInterfaceImpl2 = this.mBusHelper;
        if (seDvfsInterfaceImpl2 != null) {
            int[] supportedFrequency2 = seDvfsInterfaceImpl2.getSupportedFrequency();
            if (supportedFrequency2 != null) {
                this.mBusFreqList = TypeConverter.arrayToIntegerList(supportedFrequency2);
            } else {
                GosLog.w(LOG_TAG, "busFreqArray is null");
            }
        }
        checkBigTurboProperty();
        updatePolicy((Package) null, true);
    }

    public static ResumeBoostCore getInstance() {
        return SingletonHolder.INSTANCE;
    }

    static class SingletonHolder {
        static ResumeBoostCore INSTANCE = new ResumeBoostCore((AnonymousClass1) null);

        SingletonHolder() {
        }
    }

    public void onFocusIn(Package packageR, boolean z) {
        String str;
        GosLog.i(LOG_TAG, "onFocusIn()-isCreate: " + z);
        if (!this.mBigTurboProperty) {
            checkBigTurboProperty();
        }
        if (!z) {
            String resumeBoostPolicy = DbHelper.getInstance().getGlobalDao().getResumeBoostPolicy();
            boolean z2 = false;
            boolean z3 = resumeBoostPolicy != null && resumeBoostPolicy.length() > 3;
            if (!(packageR == null || (str = packageR.resumeBoostPolicy) == null || str.length() <= 3)) {
                z2 = true;
            }
            if (!z3 && !z2) {
                return;
            }
        }
        updatePolicy(packageR, z);
        if (!(this.mCpuFreqList == null && this.mBusFreqList == null)) {
            boolean z4 = this.isGmsOk;
            String str2 = BuildConfig.VERSION_NAME;
            if (z4) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(this.mCpuBoostAvailable ? "cpu," : str2);
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                if (this.mBusBoostAvailable) {
                    str2 = "bus,";
                }
                sb3.append(str2);
                SeGameManager.getInstance().boostUp(sb3.toString(), this.mDurationSec);
                this.mMinLockBoosterWorking = true;
            } else {
                boostUp(this.mDurationSec);
                SeGameManager.getInstance().writeSystemFile("/sys/kernel/gmc/maxlock_delay_sec", str2 + this.mDurationSec);
                this.mMinLockBoosterWorking = true;
                try {
                    SeDvfsInterfaceImpl.createInstance(AppContext.get(), SeDvfsInterfaceImpl.TYPE_CPU_POWER_COLLAPSE_DISABLE).acquire(this.mDurationSec * 1000);
                    GosLog.i(LOG_TAG, "cstateDisable.acquire()");
                } catch (Exception e) {
                    GosLog.w(LOG_TAG, (Throwable) e);
                }
            }
        }
        GosLog.i(LOG_TAG, "onFocusIn(), mBigTurboAvailable = " + this.mBigTurboAvailable + ", mBigTurboProperty = " + this.mBigTurboProperty);
        if (z && this.mBigTurboAvailable && this.mBigTurboEnable) {
            Intent intent = new Intent();
            intent.setAction(INTENT_GAME_CREATE);
            intent.putExtra("duration_sec", this.mBigTurboDurationSec);
            AppContext.get().sendBroadcast(intent, "android.permission.HARDWARE_TEST");
            GosLog.i(LOG_TAG, "onFocusIn(), send GAME_CREATE intent, duration_sec: " + this.mBigTurboDurationSec);
            this.mBigTurboWorking = true;
        }
    }

    public void onFocusOut() {
        if (this.mMinLockBoosterWorking) {
            if (this.isGmsOk) {
                SeGameManager.getInstance().releaseBoost();
                this.mMinLockBoosterWorking = false;
            } else {
                releaseBoost();
                this.mMinLockBoosterWorking = false;
                try {
                    SeDvfsInterfaceImpl.createInstance(AppContext.get(), SeDvfsInterfaceImpl.TYPE_CPU_POWER_COLLAPSE_DISABLE).release();
                    GosLog.i(LOG_TAG, "cstateDisable.release()");
                } catch (Exception e) {
                    GosLog.w(LOG_TAG, (Throwable) e);
                }
            }
        }
        this.mBigTurboWorking = false;
    }

    /* access modifiers changed from: package-private */
    public boolean isMinLockBoosterWorking() {
        return this.mMinLockBoosterWorking;
    }

    /* access modifiers changed from: package-private */
    public boolean isBigTurboWorking() {
        return this.mBigTurboWorking;
    }

    /* access modifiers changed from: package-private */
    public int getCurrentBigTurboDurationSec() {
        return this.mBigTurboDurationSec;
    }

    public boolean ismCpuBoostAvailable() {
        return this.mCpuBoostAvailable;
    }

    public boolean ismBusBoostAvailable() {
        return this.mBusBoostAvailable;
    }

    public boolean ismBigTurboEnable() {
        return this.mBigTurboEnable;
    }

    private void checkBigTurboProperty() {
        String propBigturboEnable = SeSysProp.getPropBigturboEnable();
        GosLog.i(LOG_TAG, "Property of BigTurbo = " + propBigturboEnable);
        if (!propBigturboEnable.isEmpty()) {
            this.mBigTurboProperty = true;
            this.mBigTurboAvailable = propBigturboEnable.equals("true");
        }
    }

    private void boostUp(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("boostUp(). timeout: ");
        int i2 = i * 1000;
        sb.append(i2);
        GosLog.i(LOG_TAG, sb.toString());
        SeDvfsInterfaceImpl seDvfsInterfaceImpl = this.mCpuHelper;
        if (seDvfsInterfaceImpl != null && this.mCpuBoostAvailable) {
            seDvfsInterfaceImpl.acquire(i2);
        }
        SeDvfsInterfaceImpl seDvfsInterfaceImpl2 = this.mBusHelper;
        if (seDvfsInterfaceImpl2 != null && this.mBusBoostAvailable) {
            seDvfsInterfaceImpl2.acquire(i2);
        }
    }

    private void releaseBoost() {
        GosLog.i(LOG_TAG, "releaseBoost()");
        SeDvfsInterfaceImpl seDvfsInterfaceImpl = this.mCpuHelper;
        if (!(seDvfsInterfaceImpl == null || this.mCpuFreqList == null)) {
            seDvfsInterfaceImpl.release();
        }
        SeDvfsInterfaceImpl seDvfsInterfaceImpl2 = this.mBusHelper;
        if (seDvfsInterfaceImpl2 != null && this.mBusFreqList != null) {
            seDvfsInterfaceImpl2.release();
        }
    }

    /* access modifiers changed from: package-private */
    public int getCurrentDurationSec() {
        return this.mDurationSec;
    }

    /* access modifiers changed from: package-private */
    public int getCurrentCpuIndex() {
        return this.mCpuIndex;
    }

    /* access modifiers changed from: package-private */
    public int getCurrentBusIndex() {
        return this.mBusIndex;
    }

    /* access modifiers changed from: package-private */
    public void changeSettings(int i, int i2, int i3, int i4) {
        int i5;
        ArrayList<Integer> arrayList = this.mCpuFreqList;
        int i6 = 0;
        if (arrayList == null || i3 >= arrayList.size()) {
            GosLog.e(LOG_TAG, "changeSettings(). Failed to get cpuFreq");
            i5 = 0;
        } else {
            i5 = this.mCpuFreqList.get(i3).intValue();
        }
        ArrayList<Integer> arrayList2 = this.mBusFreqList;
        if (arrayList2 == null || i4 > arrayList2.size()) {
            GosLog.e(LOG_TAG, "changeSettings(). Failed to get busFreq");
        } else {
            i6 = this.mBusFreqList.get(i4).intValue();
        }
        if (i5 != 0 && i6 != 0) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("duration", i2);
                jSONObject.put(Policy.CPU_FREQ, i5);
                jSONObject.put(Policy.BUS_FREQ, i6);
                StringBuilder sb = new StringBuilder();
                sb.append("changeSettings(), type: ");
                sb.append(i == 0 ? "resume" : "launch");
                sb.append(", durationSec: ");
                sb.append(i2);
                sb.append(", cpuIndex: ");
                sb.append(i3);
                sb.append(", busIndex: ");
                sb.append(i4);
                GosLog.d(LOG_TAG, sb.toString());
                if (i == 0) {
                    DbHelper.getInstance().getGlobalDao().setResumeBoostPolicy(new Global.IdAndResumeBoostPolicy(jSONObject.toString()));
                } else {
                    DbHelper.getInstance().getGlobalDao().setLaunchBoostPolicy(new Global.IdAndLaunchBoostPolicy(jSONObject.toString()));
                }
                updatePolicy((Package) null, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void changePackageSettings(Constants.BoostType boostType, Package packageR, int i, int i2, int i3) {
        int i4;
        ArrayList<Integer> arrayList = this.mCpuFreqList;
        int i5 = 0;
        if (arrayList == null || i2 >= arrayList.size()) {
            GosLog.e(LOG_TAG, "changeSettings(). Failed to get cpuFreq");
            i4 = 0;
        } else {
            i4 = this.mCpuFreqList.get(i2).intValue();
        }
        ArrayList<Integer> arrayList2 = this.mBusFreqList;
        if (arrayList2 == null || i3 > arrayList2.size()) {
            GosLog.e(LOG_TAG, "changeSettings(). Failed to get busFreq");
        } else {
            i5 = this.mBusFreqList.get(i3).intValue();
        }
        if (packageR != null && i4 != 0 && i5 != 0) {
            int i6 = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType[boostType.ordinal()];
            if (i6 == 1) {
                packageR.resumeBoostPolicy = updatePolicy(packageR.resumeBoostPolicy, i, i4, i5).toString();
            } else if (i6 == 2) {
                packageR.launchBoostPolicy = updatePolicy(packageR.launchBoostPolicy, i, i4, i5).toString();
            }
        }
    }

    /* renamed from: com.samsung.android.game.gos.feature.resumeboost.ResumeBoostCore$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.samsung.android.game.gos.value.Constants$BoostType[] r0 = com.samsung.android.game.gos.value.Constants.BoostType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType = r0
                com.samsung.android.game.gos.value.Constants$BoostType r1 = com.samsung.android.game.gos.value.Constants.BoostType.Resume     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.value.Constants$BoostType r1 = com.samsung.android.game.gos.value.Constants.BoostType.Launch     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.resumeboost.ResumeBoostCore.AnonymousClass1.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public JSONObject updatePolicy(String str, int i, int i2, int i3) {
        JSONObject jSONObject = null;
        if (str != null) {
            try {
                jSONObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            jSONObject = new JSONObject();
        }
        jSONObject.put("duration", i);
        jSONObject.put(Policy.CPU_FREQ, i2);
        jSONObject.put(Policy.BUS_FREQ, i3);
        return jSONObject;
    }

    private JSONObject getJsonPolicy(Package packageR, boolean z) {
        String str;
        String str2;
        if (z) {
            str = DbHelper.getInstance().getGlobalDao().getLaunchBoostPolicy();
        } else {
            str = DbHelper.getInstance().getGlobalDao().getResumeBoostPolicy();
        }
        if (str == null || str.equals(BuildConfig.VERSION_NAME)) {
            str = null;
        } else {
            GosLog.i(LOG_TAG, "updatePolicy(). globalPolicy : " + str);
        }
        if (packageR != null) {
            if (z) {
                str2 = packageR.launchBoostPolicy;
            } else {
                str2 = packageR.resumeBoostPolicy;
            }
            if (str2 != null && !str2.equals(BuildConfig.VERSION_NAME)) {
                GosLog.i(LOG_TAG, "updatePolicy(). pkgPolicy of " + packageR.pkgName + " : " + str2);
                str = str2;
            }
        }
        if (str != null) {
            try {
                return new JSONObject(str);
            } catch (JSONException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void updatePolicy(Package packageR, boolean z) {
        int i;
        int i2;
        int indexOf;
        int indexOf2;
        JSONObject jsonPolicy = getJsonPolicy(packageR, z);
        int i3 = 30;
        int i4 = 10;
        String str = null;
        if (jsonPolicy != null) {
            i2 = jsonPolicy.has(Policy.CPU_FREQ) ? jsonPolicy.optInt(Policy.CPU_FREQ) : 0;
            i = jsonPolicy.has(Policy.BUS_FREQ) ? jsonPolicy.optInt(Policy.BUS_FREQ) : 0;
            if (jsonPolicy.has("duration")) {
                i4 = jsonPolicy.optInt("duration", 10);
            }
            if (jsonPolicy.has("big_turbo_policy")) {
                str = jsonPolicy.optString("big_turbo_policy");
            }
            if (jsonPolicy.has("big_turbo_duration")) {
                i3 = jsonPolicy.optInt("big_turbo_duration", 30);
            }
        } else {
            i2 = 0;
            i = 0;
        }
        this.mCpuIndex = 0;
        ArrayList<Integer> arrayList = this.mCpuFreqList;
        if (arrayList != null) {
            if (!(i2 == 0 || (indexOf2 = arrayList.indexOf(Integer.valueOf(i2))) == -1)) {
                this.mCpuIndex = indexOf2;
            }
            int size = this.mCpuFreqList.size();
            int i5 = this.mCpuIndex;
            if (size > i5) {
                if (this.isGmsOk) {
                    SeGameManager.getInstance().boostSetDvfsValue("cpu", this.mCpuFreqList.get(this.mCpuIndex).intValue());
                } else {
                    this.mCpuHelper.setDvfsValue((long) this.mCpuFreqList.get(i5).intValue());
                }
                this.mCpuBoostAvailable = true;
                GosLog.i(LOG_TAG, "updatePolicy(). mCpuIndex: " + this.mCpuIndex + ", cpuFreq:" + this.mCpuFreqList.get(this.mCpuIndex));
            } else {
                this.mCpuBoostAvailable = false;
            }
        } else {
            this.mCpuBoostAvailable = false;
        }
        this.mBusIndex = 0;
        ArrayList<Integer> arrayList2 = this.mBusFreqList;
        if (arrayList2 != null) {
            if (!(i == 0 || (indexOf = arrayList2.indexOf(Integer.valueOf(i))) == -1)) {
                this.mBusIndex = indexOf;
            }
            int size2 = this.mBusFreqList.size();
            int i6 = this.mBusIndex;
            if (size2 > i6) {
                if (this.isGmsOk) {
                    SeGameManager.getInstance().boostSetDvfsValue("bus", this.mBusFreqList.get(this.mBusIndex).intValue());
                } else {
                    this.mBusHelper.setDvfsValue((long) this.mBusFreqList.get(i6).intValue());
                }
                this.mBusBoostAvailable = true;
                GosLog.i(LOG_TAG, "updatePolicy(). mBusIndex: " + this.mBusIndex + ", busFreq:" + this.mBusFreqList.get(this.mBusIndex));
            } else {
                this.mBusBoostAvailable = false;
            }
        } else {
            this.mBusBoostAvailable = false;
        }
        this.mDurationSec = i4;
        GosLog.i(LOG_TAG, "updatePolicy(). mDurationSec: " + this.mDurationSec);
        this.mBigTurboEnable = true;
        if (str != null && str.equals("0")) {
            GosLog.i(LOG_TAG, "updatePolicy(). disable BigTurbo by server policy");
            this.mBigTurboEnable = false;
        }
        this.mBigTurboDurationSec = i3;
    }

    public int getCpuIndexFromServer(int i) {
        int indexOf;
        ArrayList<Integer> arrayList = this.mCpuFreqList;
        if (arrayList == null || i == 0 || (indexOf = arrayList.indexOf(Integer.valueOf(i))) == -1) {
            return 0;
        }
        return indexOf;
    }

    public int getBusIndexFromServer(int i) {
        int indexOf;
        ArrayList<Integer> arrayList = this.mBusFreqList;
        if (arrayList == null || i == 0 || (indexOf = arrayList.indexOf(Integer.valueOf(i))) == -1) {
            return 0;
        }
        return indexOf;
    }

    public void updateGmsOk(float f) {
        this.isGmsOk = f >= 110.004f;
    }

    public int getDuration(Package packageR, boolean z) {
        JSONObject jsonPolicy = getJsonPolicy(packageR, z);
        if (jsonPolicy == null || !jsonPolicy.has("duration")) {
            return 10;
        }
        return jsonPolicy.optInt("duration", 10);
    }

    public void setBigTurboProperty(boolean z) {
        this.mBigTurboProperty = z;
    }
}
