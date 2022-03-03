package com.samsung.android.game.gos.feature.ipm;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.feature.externalsdk.value.Const;
import com.samsung.android.game.gos.feature.gfi.GfiFeature;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostFeature;
import com.samsung.android.game.gos.ipm.AndroidIpmCallback;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.ipm.CommonUtil;
import com.samsung.android.game.gos.ipm.IntelMode;
import com.samsung.android.game.gos.ipm.Ipm;
import com.samsung.android.game.gos.ipm.LogLevel;
import com.samsung.android.game.gos.ipm.ParameterRequest;
import com.samsung.android.game.gos.ipm.Policy;
import com.samsung.android.game.gos.ipm.Profile;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class IpmCore {
    private static final String LOG_TAG = "IpmCore";
    private static IpmCore mInstance;
    private static Runnable mOnStartStopCallback;
    private final Context mContext;
    private final GosGlobalSettings mGosGlobalSettings;
    private final Ipm mIpm;
    /* access modifiers changed from: private */
    public final List<Listener> mListeners = new ArrayList();

    public interface Listener {
        void onStarted(IpmCore ipmCore);

        void onStopped(IpmCore ipmCore);

        void onSystemCreated(IpmCore ipmCore);
    }

    public void register(Listener listener) {
        this.mListeners.add(listener);
    }

    public void deregister(Listener listener) {
        this.mListeners.remove(listener);
    }

    public synchronized void resetSpaOn() {
        this.mIpm.resetSpaOn();
    }

    /* access modifiers changed from: package-private */
    public synchronized void resetParametersUsed() {
        this.mIpm.resetParametersUsed();
    }

    public synchronized JSONObject printParametersUsedToJsonFormat() {
        return this.mIpm.printParametersUsedToJsonFormat();
    }

    public static synchronized IpmCore getInstance(Context context) {
        IpmCore ipmCore;
        synchronized (IpmCore.class) {
            if (mInstance == null) {
                mInstance = new IpmCore(context);
            }
            ipmCore = mInstance;
        }
        return ipmCore;
    }

    public static synchronized IpmCore getInstanceUnsafe() {
        IpmCore ipmCore;
        synchronized (IpmCore.class) {
            ipmCore = mInstance;
        }
        return ipmCore;
    }

    static synchronized void resetInstance() {
        synchronized (IpmCore.class) {
            mInstance = null;
        }
    }

    private class AndroidIpmCallbackListener implements AndroidIpmCallback.Listener {
        private final Handler mHandler;

        private AndroidIpmCallbackListener() {
            this.mHandler = new Handler(Looper.getMainLooper());
        }

        /* synthetic */ AndroidIpmCallbackListener(IpmCore ipmCore, AnonymousClass1 r2) {
            this();
        }

        public void onStarted() {
            for (Listener onStarted : IpmCore.this.mListeners) {
                onStarted.onStarted(IpmCore.this);
            }
            this.mHandler.post($$Lambda$IpmCore$AndroidIpmCallbackListener$WJ76TcZMTEXq2Nv6MnV0qnw6dHM.INSTANCE);
        }

        public void onStopped() {
            for (Listener onStopped : IpmCore.this.mListeners) {
                onStopped.onStopped(IpmCore.this);
            }
            this.mHandler.post($$Lambda$IpmCore$AndroidIpmCallbackListener$iFtLFGl6WqXPlY0VIf1jAn5YK1c.INSTANCE);
        }

        public void onSystemCreated() {
            for (Listener onSystemCreated : IpmCore.this.mListeners) {
                onSystemCreated.onSystemCreated(IpmCore.this);
            }
        }
    }

    IpmCore(Context context) {
        this.mContext = context;
        GosGlobalSettings gosGlobalSettings = new GosGlobalSettings(DbHelper.getInstance().getGlobalDao(), GlobalDbHelper.getInstance());
        this.mGosGlobalSettings = gosGlobalSettings;
        this.mIpm = Ipm.create(context, gosGlobalSettings, new GosFrameInterpolator(GfiFeature.getInstance(context)), new GosResumeBooster(ResumeBoostFeature.getInstance()), new GosSysfs(SeGameManager.getInstance()), new GosActivityManager(SeActivityManager.getInstance()), new GosSsrm(context, Resources.getSystem()), new GosAndroidSystem(), new AndroidIpmCallbackListener(this, (AnonymousClass1) null));
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x006e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void start(com.samsung.android.game.gos.data.PkgData r6, boolean r7) {
        /*
            r5 = this;
            monitor-enter(r5)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r0 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.data.dao.GlobalDao r0 = r0.getGlobalDao()     // Catch:{ all -> 0x0084 }
            int r0 = r0.getSiopMode()     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.data.model.Package r1 = r6.getPkg()     // Catch:{ all -> 0x0084 }
            int r1 = r1.getCustomSiopMode()     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.data.dbhelper.DbHelper r2 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r2 = r2.getGlobalFeatureFlagDao()     // Catch:{ all -> 0x0084 }
            java.lang.String r3 = "siop_mode"
            com.samsung.android.game.gos.data.model.GlobalFeatureFlag r2 = r2.get(r3)     // Catch:{ all -> 0x0084 }
            java.util.Map r3 = r6.getFeatureFlagMap()     // Catch:{ all -> 0x0084 }
            java.lang.String r4 = "siop_mode"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.data.model.FeatureFlag r3 = (com.samsung.android.game.gos.data.model.FeatureFlag) r3     // Catch:{ all -> 0x0084 }
            boolean r4 = r2.usingUserValue     // Catch:{ all -> 0x0084 }
            if (r4 == 0) goto L_0x0038
            boolean r2 = r2.usingPkgValue     // Catch:{ all -> 0x0084 }
            if (r2 == 0) goto L_0x0040
            goto L_0x003f
        L_0x0038:
            boolean r2 = r3.isInheritedFlag()     // Catch:{ all -> 0x0084 }
            if (r2 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r0 = r1
        L_0x0040:
            r1 = 0
            r5.setEnabledDynamicSpa(r1)     // Catch:{ all -> 0x0084 }
            r1 = -1
            if (r0 == r1) goto L_0x006e
            r1 = 1
            if (r0 == r1) goto L_0x0062
            if (r0 != 0) goto L_0x0056
            r5.setEnabledDynamicSpa(r1)     // Catch:{ all -> 0x0084 }
            java.lang.String r0 = "IpmCore"
            java.lang.String r1 = "Dynamic SPA is enabled by SIOP 'Balanced' mode"
            com.samsung.android.game.gos.util.GosLog.i(r0, r1)     // Catch:{ all -> 0x0084 }
        L_0x0056:
            com.samsung.android.game.gos.ipm.Ipm r0 = r5.mIpm     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.ipm.Profile r1 = com.samsung.android.game.gos.ipm.Profile.HIGH     // Catch:{ all -> 0x0084 }
            int r1 = r1.toInt()     // Catch:{ all -> 0x0084 }
            r0.setOriginalIpmProfile(r1)     // Catch:{ all -> 0x0084 }
            goto L_0x0079
        L_0x0062:
            com.samsung.android.game.gos.ipm.Ipm r0 = r5.mIpm     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.ipm.Profile r1 = com.samsung.android.game.gos.ipm.Profile.ULTRA     // Catch:{ all -> 0x0084 }
            int r1 = r1.toInt()     // Catch:{ all -> 0x0084 }
            r0.setOriginalIpmProfile(r1)     // Catch:{ all -> 0x0084 }
            goto L_0x0079
        L_0x006e:
            com.samsung.android.game.gos.ipm.Ipm r0 = r5.mIpm     // Catch:{ all -> 0x0084 }
            com.samsung.android.game.gos.ipm.Profile r1 = com.samsung.android.game.gos.ipm.Profile.LOW     // Catch:{ all -> 0x0084 }
            int r1 = r1.toInt()     // Catch:{ all -> 0x0084 }
            r0.setOriginalIpmProfile(r1)     // Catch:{ all -> 0x0084 }
        L_0x0079:
            com.samsung.android.game.gos.ipm.Ipm r0 = r5.mIpm     // Catch:{ all -> 0x0084 }
            java.lang.String r6 = r6.getPackageName()     // Catch:{ all -> 0x0084 }
            r0.start(r6, r7)     // Catch:{ all -> 0x0084 }
            monitor-exit(r5)
            return
        L_0x0084:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ipm.IpmCore.start(com.samsung.android.game.gos.data.PkgData, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public synchronized void stop() {
        this.mIpm.stop();
    }

    public synchronized boolean isRunning() {
        return this.mIpm.isRunning();
    }

    /* renamed from: com.samsung.android.game.gos.feature.ipm.IpmCore$1  reason: invalid class name */
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
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.HIGH     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.ULTRA     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x003e }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.CRITICAL     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.samsung.android.game.gos.feature.externalsdk.value.Const$ApplyType r1 = com.samsung.android.game.gos.feature.externalsdk.value.Const.ApplyType.CUSTOM     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ipm.IpmCore.AnonymousClass1.<clinit>():void");
        }
    }

    public synchronized void updateGameSDK(int i) {
        int i2 = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$feature$externalsdk$value$Const$ApplyType[Const.ApplyType.values()[i].ordinal()];
        if (i2 == 2) {
            setProfile(Profile.LOW);
        } else if (i2 == 3) {
            setProfile(Profile.HIGH);
        } else if (i2 == 4) {
            setProfile(Profile.ULTRA);
        } else if (i2 == 5) {
            setProfile(Profile.CRITICAL);
        } else if (i2 != 6) {
            setProfile(this.mIpm.getOriginalProfile());
        } else {
            setProfile(Profile.CUSTOM);
        }
    }

    public synchronized void setTargetFps(float f) {
        this.mIpm.setTargetFps(f);
    }

    public synchronized void loadingBoostMode(boolean z) {
        this.mIpm.loadingBoostMode(z);
    }

    public synchronized int getCustomTfpsFlags() {
        return this.mIpm.getCustomTfpsFlags();
    }

    /* access modifiers changed from: package-private */
    public void destroySystem() {
        this.mIpm.destroySystem();
    }

    /* access modifiers changed from: package-private */
    public void addJson(String str, String str2) {
        this.mIpm.addJson(str, str2);
    }

    public void setProfile(Profile profile) {
        this.mIpm.setProfile(profile);
    }

    public void setLogLevel(LogLevel logLevel) {
        this.mIpm.setLogLevel(logLevel);
    }

    public LogLevel getDefaultLogLevel() {
        return this.mIpm.getDefaultLogLevel();
    }

    public void setSupertrain(boolean z) {
        this.mIpm.setSupertrain(z);
    }

    public synchronized void setOnlyCapture(boolean z) {
        this.mIpm.setOnlyCapture(z);
    }

    /* access modifiers changed from: package-private */
    public boolean getOnlyCapture() {
        return this.mIpm.getOnlyCapture();
    }

    public Profile getProfile() {
        return this.mIpm.getProfile();
    }

    public void setTargetPST(int i) {
        this.mIpm.setTargetPST(i);
    }

    public void setCustomTfpsFlags(int i) {
        this.mIpm.setCustomTfpsFlags(i);
    }

    public String getStatistics() {
        return this.mIpm.getStatistics();
    }

    /* access modifiers changed from: package-private */
    public int getVersion() {
        return this.mIpm.getVersion();
    }

    public void setMinFreqs(long j, long j2) {
        this.mIpm.setMinFreqs(j, j2);
    }

    public void setMaxFreqs(long j, long j2) {
        this.mIpm.setMaxFreqs(j, j2);
    }

    public void setIntelMode(IntelMode intelMode) {
        this.mIpm.setIntelMode(intelMode);
    }

    public void setFrameInterpolationEnabled(boolean z) {
        this.mIpm.setFrameInterpolationEnabled(z);
    }

    public boolean isFrameInterpolationEnabled() {
        return this.mIpm.isFrameInterpolationEnabled();
    }

    public void setFrameInterpolationTemperatureOffset(float f) {
        this.mIpm.setFrameInterpolationTemperatureOffset(f);
    }

    public float getFrameInterpolationTemperatureOffset() {
        return this.mIpm.getFrameInterpolationTemperatureOffset();
    }

    public void setFrameInterpolationFrameRateOffset(float f) {
        this.mIpm.setFrameInterpolationFrameRateOffset(f);
    }

    public float getFrameInterpolationFrameRateOffset() {
        return this.mIpm.getFrameInterpolationFrameRateOffset();
    }

    public void setFrameInterpolationDecayHalfLife(float f) {
        this.mIpm.setFrameInterpolationDecayHalfLife(f);
    }

    public float getFrameInterpolationDecayHalfLife() {
        return this.mIpm.getFrameInterpolationDecayHalfLife();
    }

    public void setCpuGap(int i) {
        this.mIpm.setCpuGap(i);
    }

    public void setGpuGap(int i) {
        this.mIpm.setGpuGap(i);
    }

    public void setGpuMinBoost(int i) {
        this.mIpm.setGpuMinBoost(i);
    }

    /* access modifiers changed from: package-private */
    public boolean canRun() {
        return this.mIpm.canRun();
    }

    public void setAllowMlOff(boolean z) {
        this.mIpm.setAllowMlOff(z);
    }

    public void setEnableBusFreq(boolean z) {
        this.mIpm.setEnableBusFreq(z);
    }

    public void setDynamicDecisions(boolean z) {
        this.mIpm.setDynamicDecisions(z);
    }

    public boolean getDynamicDecisions() {
        return this.mIpm.getDynamicDecisions();
    }

    public void setLowLatencySceneSDK(int i, String str, int i2, boolean z) {
        this.mIpm.setLowLatencySceneSDK(i, str, i2, z);
    }

    public String getToTGPA() {
        return this.mIpm.getToTGPA();
    }

    public void setEnableToTGPA(boolean z) {
        this.mIpm.setEnableToTGPA(z);
    }

    /* access modifiers changed from: package-private */
    public String readSessionsJSON() {
        return this.mIpm.readSessionsJSON();
    }

    public String readSessionsJSON(int[] iArr) {
        return this.mIpm.readSessionsJSON(iArr);
    }

    public String readDataJSON(List<ParameterRequest> list, int i, long j, long j2) {
        return this.mIpm.readDataJSON(list, i, j, j2);
    }

    public void setEnableAnyMode(boolean z) {
        this.mIpm.setEnableAnyMode(z);
    }

    public boolean getEnableAnyMode() {
        return this.mIpm.getEnableAnyMode();
    }

    public void setEnableLRPST(boolean z) {
        this.mIpm.setEnableLRPST(z);
    }

    public boolean getEnableLRPST() {
        return this.mIpm.getEnableLRPST();
    }

    public void setEnableAllowMlOff(boolean z) {
        this.mIpm.setEnableAllowMlOff(z);
    }

    public boolean getEnableAllowMlOff() {
        return this.mIpm.getEnableAllowMlOff();
    }

    public void setEnableGpuMinFreqControl(boolean z) {
        this.mIpm.setEnableGpuMinFreqControl(z);
    }

    public boolean getEnableGpuMinFreqControl() {
        return this.mIpm.getEnableGpuMinFreqControl();
    }

    public void setEnableGTLM(boolean z) {
        this.mIpm.setEnableGTLM(z);
    }

    public boolean getEnableGTLM() {
        return this.mIpm.getEnableGTLM();
    }

    public void setEnableCpuMinFreqControl(boolean z) {
        this.mIpm.setEnableCpuMinFreqControl(z);
    }

    public boolean getEnableCpuMinFreqControl() {
        return this.mIpm.getEnableCpuMinFreqControl();
    }

    public void setEnableBusMinFreqControl(boolean z) {
        this.mIpm.setEnableBusMinFreqControl(z);
    }

    public boolean getEnableBusMinFreqControl() {
        return this.mIpm.getEnableBusMinFreqControl();
    }

    public void setLRPST(int i) {
        this.mIpm.setLRPST(i);
    }

    public int getLRPST() {
        return this.mIpm.getLRPST();
    }

    public String processCommands(String str) {
        try {
            return CommonUtil.processSpaCommands(this.mGosGlobalSettings, this.mIpm, new JSONObject(str)).toString();
        } catch (JSONException e) {
            GosLog.e(LOG_TAG, String.format(Locale.US, "Failed to parse JSON: %s", new Object[]{e.toString()}));
            return new JSONObject().toString();
        }
    }

    public String getEncodedRinglog() {
        return CommonUtil.getIpmEncodedRinglog(this.mContext.getCacheDir());
    }

    public String getReadableRinglog() {
        try {
            return CommonUtil.getIpmReadableRinglog(new JSONObject(readSessionsJSON()));
        } catch (JSONException e) {
            GosLog.e(LOG_TAG, String.format(Locale.US, "Failed to parse session JSON: %s", new Object[]{e.toString()}));
            return BuildConfig.VERSION_NAME;
        }
    }

    /* access modifiers changed from: package-private */
    public Policy applyJsonPolicy(Policy policy) {
        return policy.apply(this.mGosGlobalSettings, this.mIpm);
    }

    /* access modifiers changed from: package-private */
    public void restoreJsonPolicy(Policy policy) {
        policy.restore(this.mGosGlobalSettings, this.mIpm);
    }

    public static void setOnStartStopCallback(Runnable runnable) {
        mOnStartStopCallback = runnable;
    }

    /* access modifiers changed from: private */
    public static void callOnStartStopEvent() {
        Runnable runnable = mOnStartStopCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void setDynamicRefreshRate(float f) {
        this.mIpm.setDynamicRefreshRate(f);
    }

    public void setEnabledDynamicSpa(boolean z) {
        this.mIpm.setDynamicPowerMode(z);
    }
}
