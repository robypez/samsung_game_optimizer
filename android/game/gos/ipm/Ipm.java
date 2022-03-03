package com.samsung.android.game.gos.ipm;

import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import com.samsung.android.game.gos.ipm.AndroidBattery;
import com.samsung.android.game.gos.ipm.AndroidDisplay;
import com.samsung.android.game.gos.ipm.AndroidIpmCallback;
import com.samsung.android.game.gos.ipm.Ssrm;
import com.samsung.android.game.gos.ipm.system.SemGameManager;
import com.samsung.android.game.gos.ipm.system.ServiceManager;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class Ipm {
    private static final String LOG_TAG = "Ipm";
    private static final boolean USE_SSRM = true;
    private final ActivityManager mActivityManager;
    private final AndroidBattery mAndroidBattery;
    private final AndroidDeviceSettings mAndroidDeviceSettings;
    private final AndroidDisplay mAndroidDisplay;
    private final AndroidPackage mAndroidPackage;
    private final AndroidSystem mAndroidSystem;
    /* access modifiers changed from: private */
    public final Display mDisplay;
    private boolean mDynamicDecisions = false;
    private boolean mEnableAllowMlOff = false;
    private boolean mEnableAnyMode = false;
    private boolean mEnableBusMinFreqControl = false;
    private boolean mEnableCpuMinFreqControl = false;
    private boolean mEnableGTLM = false;
    private boolean mEnableGpuMinFreqControl = false;
    private boolean mEnableLRPST = false;
    private boolean mForceMode = false;
    private float mFrameInterpolationDecayHalfLife = 1.0f;
    private boolean mFrameInterpolationEnabled = false;
    private float mFrameInterpolationFrameRateOffset = 0.0f;
    private float mFrameInterpolationTemperatureOffset = 0.0f;
    private final GlobalSettings mGlobalSettings;
    private String mIntelConfig = null;
    private IntelMode mIntelMode = null;
    /* access modifiers changed from: private */
    public final IpmJava mIpm;
    private boolean mIsStarted = false;
    private boolean mIsStartedCapture = false;
    private int mLRPST = 0;
    private final int mNativeLoaded;
    private Profile mOriginalProfile = Profile.HIGH;
    private ParametersUsed mParametersUsed = new ParametersUsed();
    private boolean mShouldRun = false;
    private final Ssrm mSsrm;
    private final SteadySystemTimer mSteadySystemTimer;

    private static class ParametersUsed {
        Long cpuBottomFreq;
        Float[] customProfileValues;
        Integer customTfpsFlags;
        Map<Long, String> events;
        String intelConfig;
        IntelMode intelMode;
        int maxFpsGuess;
        Long maxFreqCpu;
        Long maxFreqGpu;
        Long minFreqCpu;
        Long minFreqGpu;
        Profile profile;
        Integer siopMode;
        Integer softkill_value;
        Long startTime;
        int target_lrpst;
        int target_pst;
        Integer tempHardLimit;
        int version;
        boolean wasIpmOn;

        private ParametersUsed() {
            this.wasIpmOn = false;
            this.version = 0;
            this.target_pst = -1;
            this.target_lrpst = -1;
            this.profile = null;
            this.siopMode = null;
            this.customProfileValues = null;
            this.softkill_value = null;
            this.tempHardLimit = null;
            this.customTfpsFlags = null;
            this.intelMode = null;
            this.intelConfig = null;
            this.cpuBottomFreq = null;
            this.minFreqCpu = null;
            this.minFreqGpu = null;
            this.maxFreqCpu = null;
            this.maxFreqGpu = null;
            this.startTime = null;
            this.maxFpsGuess = -1;
            this.events = new LinkedHashMap();
        }
    }

    public synchronized void resetSpaOn() {
        this.mParametersUsed.wasIpmOn = false;
    }

    public synchronized void resetParametersUsed() {
        this.mParametersUsed = new ParametersUsed();
    }

    private void recordEvent(String str) {
        this.mParametersUsed.events.put(Long.valueOf(this.mSteadySystemTimer.now(TimeUnit.MILLISECONDS) - this.mParametersUsed.startTime.longValue()), str);
    }

    public synchronized JSONObject printParametersUsedToJsonFormat() {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        ParametersUsed parametersUsed = this.mParametersUsed;
        if (parametersUsed == null) {
            return jSONObject;
        }
        try {
            jSONObject.put("spa_on", parametersUsed.wasIpmOn);
            if (parametersUsed.wasIpmOn) {
                jSONObject.put(GosInterface.KeyName.VERSION, parametersUsed.version);
                jSONObject.put("target_pst", parametersUsed.target_pst);
                jSONObject.put("target_lrpst", parametersUsed.target_lrpst);
                if (parametersUsed.profile != null) {
                    jSONObject.put(GosInterface.KeyName.PROFILE, parametersUsed.profile.toInt());
                }
                if (parametersUsed.customProfileValues != null) {
                    jSONObject.put("custom_profile_values", "[" + parametersUsed.customProfileValues[0] + "," + parametersUsed.customProfileValues[1] + "," + parametersUsed.customProfileValues[2] + "," + parametersUsed.customProfileValues[3] + "]");
                }
                if (parametersUsed.siopMode != null) {
                    jSONObject.put("siop_mode", parametersUsed.siopMode);
                }
                if (parametersUsed.softkill_value != null) {
                    jSONObject.put("softkill_pst", parametersUsed.softkill_value);
                }
                if (parametersUsed.cpuBottomFreq != null) {
                    jSONObject.put("cpu_bottom_freq", parametersUsed.cpuBottomFreq);
                }
                if (parametersUsed.minFreqCpu != null) {
                    jSONObject.put("minfreq_cpu", parametersUsed.minFreqCpu);
                }
                if (parametersUsed.minFreqGpu != null) {
                    jSONObject.put("minfreq_gpu", parametersUsed.minFreqGpu);
                }
                if (parametersUsed.maxFreqCpu != null) {
                    jSONObject.put("maxfreq_cpu", parametersUsed.maxFreqCpu);
                }
                if (parametersUsed.maxFreqGpu != null) {
                    jSONObject.put("maxfreq_gpu", parametersUsed.maxFreqGpu);
                }
                if (parametersUsed.startTime != null) {
                    jSONObject.put("session_starttime", parametersUsed.startTime);
                }
                if (parametersUsed.tempHardLimit != null) {
                    jSONObject.put("temp_hard_limit", parametersUsed.tempHardLimit);
                }
                if (parametersUsed.maxFpsGuess != -1) {
                    jSONObject.put("max_fps_guess", parametersUsed.maxFpsGuess);
                }
                if (parametersUsed.customTfpsFlags != null) {
                    jSONObject.put("custom_tfps_flags", parametersUsed.customTfpsFlags);
                }
                if (parametersUsed.intelMode != null) {
                    jSONObject.put(GosInterface.KeyName.INTEL_MODE, parametersUsed.intelMode.toInt());
                    if (parametersUsed.intelConfig != null) {
                        jSONObject.put("intel_config", parametersUsed.intelConfig);
                    }
                }
                jSONObject.put("events_list", CommonUtil.getEventsLists(parametersUsed.events));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public static Ipm create(Context context, GlobalSettings globalSettings, FrameInterpolator frameInterpolator, ResumeBooster resumeBooster, Sysfs sysfs, ActivityManager activityManager, Ssrm ssrm, AndroidSystem androidSystem, AndroidIpmCallback.Listener listener) {
        Context context2 = context;
        AndroidDisplay androidDisplay = new AndroidDisplay(context2, Looper.getMainLooper());
        AndroidPackage androidPackage = new AndroidPackage(context.getPackageManager());
        AndroidDeviceSettings androidDeviceSettings = new AndroidDeviceSettings(Resources.getSystem(), ssrm);
        Display display = new Display();
        AndroidDeviceSettings androidDeviceSettings2 = androidDeviceSettings;
        AndroidIpmCallback androidIpmCallback = r0;
        AndroidIpmCallback androidIpmCallback2 = new AndroidIpmCallback(context, androidPackage, androidDeviceSettings2, sysfs, resumeBooster, listener, BuildConfig.VERSION_NAME);
        return new Ipm(new IpmJava(androidIpmCallback, display, new AndroidSurfaceFlinger(ServiceManager.getService("SurfaceFlinger"), androidPackage, androidDisplay, frameInterpolator), new AndroidGameManager(new SemGameManager())), display, androidDeviceSettings2, globalSettings, ssrm, new SteadySystemTimer(), androidSystem, new AndroidBattery(context2, sysfs), androidDisplay, androidPackage, activityManager);
    }

    public Ipm(IpmJava ipmJava, Display display, AndroidDeviceSettings androidDeviceSettings, GlobalSettings globalSettings, Ssrm ssrm, SteadySystemTimer steadySystemTimer, AndroidSystem androidSystem, AndroidBattery androidBattery, AndroidDisplay androidDisplay, AndroidPackage androidPackage, ActivityManager activityManager) {
        this.mIpm = ipmJava;
        this.mDisplay = display;
        this.mAndroidDeviceSettings = androidDeviceSettings;
        this.mGlobalSettings = globalSettings;
        this.mSteadySystemTimer = steadySystemTimer;
        this.mSsrm = ssrm;
        this.mAndroidSystem = androidSystem;
        this.mAndroidBattery = androidBattery;
        this.mAndroidDisplay = androidDisplay;
        this.mAndroidPackage = androidPackage;
        this.mActivityManager = activityManager;
        androidDisplay.register(new AndroidDisplay.Listener() {
            public void onRefreshRateChanged(float f, float f2) {
                Ipm.this.mDisplay.onRefreshRateChanged(f, f2);
            }
        });
        this.mAndroidBattery.register(new AndroidBattery.Listener() {
            public void onPowerConnected() {
                Ipm.this.powerChanged(true);
            }

            public void onPowerDisconnected() {
                Ipm.this.powerChanged(false);
            }
        });
        this.mSsrm.register(new Ssrm.Listener() {
            public void onActivateChanged(boolean z) {
                Ipm.this.toggleOnOff(z);
            }

            public void onPauseActionsChanged(boolean z) {
                Ipm.this.mIpm.pauseActions(z);
            }
        });
        this.mNativeLoaded = this.mIpm.getVersion();
    }

    public synchronized void start(String str, boolean z) {
        if (this.mAndroidDeviceSettings.getIpmScenario() || z) {
            if (this.mSsrm.isInitialized() && !canRun()) {
                this.mGlobalSettings.setIpmEnabled(false);
                Log.w(LOG_TAG, "Disabling SPA availableFeatureFlag due to error");
            }
            this.mSsrm.activate();
            this.mAndroidBattery.activate();
            this.mAndroidDisplay.activate();
            this.mDisplay.onRefreshRateChanged(this.mAndroidDisplay.getRefreshRate(), Float.POSITIVE_INFINITY);
            this.mShouldRun = true;
            this.mAndroidPackage.setName(str);
            setSupertrain(this.mGlobalSettings.isSupertrainEnabled());
            setLogLevel(getDefaultLogLevel());
            setOnlyCapture(z);
            setTargetPST(adjustTargetTemp(this.mGlobalSettings.getTargetTemperature()));
            setTargetLRPST(this.mLRPST);
            setProfile(this.mOriginalProfile);
            setCPUBottomFreq(this.mGlobalSettings.getCpuBottomFrequency());
            setUseSsrm(true);
            this.mParametersUsed.wasIpmOn = true;
            this.mParametersUsed.intelMode = this.mIntelMode;
            this.mParametersUsed.intelConfig = this.mIntelConfig;
            this.mParametersUsed.version = getVersion();
            this.mParametersUsed.siopMode = Integer.valueOf(this.mGlobalSettings.getSiopMode());
            this.mParametersUsed.startTime = Long.valueOf(this.mSteadySystemTimer.now(TimeUnit.MILLISECONDS));
            this.mParametersUsed.events = new LinkedHashMap();
            this.mParametersUsed.maxFpsGuess = getMaxFpsGuess();
            Log.d(LOG_TAG, "MaxFPS guess is " + this.mParametersUsed.maxFpsGuess);
            this.mIsStartedCapture = z;
            startNative();
            return;
        }
        Log.e(LOG_TAG, "Ssrm did not set up IPM scenario in the xml rules, can't run");
    }

    public synchronized void stop() {
        this.mAndroidDisplay.deactivate();
        this.mAndroidBattery.deactivate();
        this.mSsrm.deactivate();
        if (this.mIsStarted) {
            stopNative();
        }
        if (this.mShouldRun) {
            recordEvent("off");
        }
        this.mShouldRun = false;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean startSpaIfPossible(String str) {
        if (!this.mShouldRun || this.mIsStarted) {
            return false;
        }
        startNative();
        Log.i(LOG_TAG, "SPA start - reason " + str);
        return true;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean stopSpaIfPossible(String str) {
        if (!this.mIsStarted) {
            return false;
        }
        stopNative();
        Log.i(LOG_TAG, "SPA stop - reason " + str);
        return true;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0015, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void toggleOnOff(boolean r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.mIsStartedCapture     // Catch:{ all -> 0x0016 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r1)
            return
        L_0x0007:
            if (r2 == 0) goto L_0x000f
            java.lang.String r2 = "SSRM Handler"
            r1.startSpaIfPossible(r2)     // Catch:{ all -> 0x0016 }
            goto L_0x0014
        L_0x000f:
            java.lang.String r2 = "SSRM Handler"
            r1.stopSpaIfPossible(r2)     // Catch:{ all -> 0x0016 }
        L_0x0014:
            monitor-exit(r1)
            return
        L_0x0016:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.Ipm.toggleOnOff(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void tempLimiterChanged(boolean r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.mIsStartedCapture     // Catch:{ all -> 0x0026 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r1)
            return
        L_0x0007:
            if (r2 == 0) goto L_0x0017
            java.lang.String r2 = "tempLimiterChanged: Temperature Recover"
            boolean r2 = r1.startSpaIfPossible(r2)     // Catch:{ all -> 0x0026 }
            if (r2 == 0) goto L_0x0024
            java.lang.String r2 = "on_temp"
            r1.recordEvent(r2)     // Catch:{ all -> 0x0026 }
            goto L_0x0024
        L_0x0017:
            java.lang.String r2 = "tempLimiterChanged: High Temperature"
            boolean r2 = r1.stopSpaIfPossible(r2)     // Catch:{ all -> 0x0026 }
            if (r2 == 0) goto L_0x0024
            java.lang.String r2 = "off_temp"
            r1.recordEvent(r2)     // Catch:{ all -> 0x0026 }
        L_0x0024:
            monitor-exit(r1)
            return
        L_0x0026:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.Ipm.tempLimiterChanged(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0015, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void powerChanged(boolean r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.mIsStartedCapture     // Catch:{ all -> 0x0016 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r1)
            return
        L_0x0007:
            if (r2 == 0) goto L_0x000f
            java.lang.String r2 = "on_power"
            r1.recordEvent(r2)     // Catch:{ all -> 0x0016 }
            goto L_0x0014
        L_0x000f:
            java.lang.String r2 = "off_power"
            r1.recordEvent(r2)     // Catch:{ all -> 0x0016 }
        L_0x0014:
            monitor-exit(r1)
            return
        L_0x0016:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.Ipm.powerChanged(boolean):void");
    }

    public LogLevel getDefaultLogLevel() {
        boolean isDebugBinary = this.mAndroidSystem.isDebugBinary();
        boolean isVerbose = this.mGlobalSettings.isVerbose();
        if (isDebugBinary && isVerbose) {
            return LogLevel.TRACE;
        }
        if (isDebugBinary || isVerbose) {
            return LogLevel.DEBUG;
        }
        return LogLevel.INFO;
    }

    public void setInputTempType(int i) {
        Log.d(LOG_TAG, "setIpmInputTempType: " + i);
    }

    public synchronized boolean isRunning() {
        return this.mIsStarted;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setTargetFps(float r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.mShouldRun     // Catch:{ all -> 0x001e }
            if (r0 != 0) goto L_0x0007
            monitor-exit(r2)
            return
        L_0x0007:
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r1 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r1 != 0) goto L_0x0013
            com.samsung.android.game.gos.ipm.Profile r3 = r2.mOriginalProfile     // Catch:{ all -> 0x001e }
            r2.setProfile(r3)     // Catch:{ all -> 0x001e }
            goto L_0x001c
        L_0x0013:
            r1 = 0
            r2.setCustomProfile(r0, r1, r3, r3)     // Catch:{ all -> 0x001e }
            com.samsung.android.game.gos.ipm.Profile r3 = com.samsung.android.game.gos.ipm.Profile.CUSTOM     // Catch:{ all -> 0x001e }
            r2.setProfile(r3)     // Catch:{ all -> 0x001e }
        L_0x001c:
            monitor-exit(r2)
            return
        L_0x001e:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.Ipm.setTargetFps(float):void");
    }

    public synchronized void loadingBoostMode(boolean z) {
        int i;
        if (this.mShouldRun) {
            Log.d(LOG_TAG, "LoadingBoost - Adjusting CPU GAP");
            if (z) {
                i = 1;
            } else {
                i = this.mEnableCpuMinFreqControl ? -2 : -1;
            }
            setCpuGap(i);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00dc, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int adjustTargetTemp(int r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            com.samsung.android.game.gos.ipm.Ipm$ParametersUsed r0 = r5.mParametersUsed     // Catch:{ all -> 0x00dd }
            java.lang.Integer r0 = r0.tempHardLimit     // Catch:{ all -> 0x00dd }
            if (r0 != 0) goto L_0x0009
            monitor-exit(r5)
            return r6
        L_0x0009:
            int r0 = r5.getTargetTemperatureLimit()     // Catch:{ all -> 0x00dd }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x00dd }
            java.lang.String r1 = "Ipm"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00dd }
            r2.<init>()     // Catch:{ all -> 0x00dd }
            java.lang.String r3 = "Adjust temperature based on USB (tempHardLimit) is enabled  with value: "
            r2.append(r3)     // Catch:{ all -> 0x00dd }
            r2.append(r0)     // Catch:{ all -> 0x00dd }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00dd }
            com.samsung.android.game.gos.ipm.Log.v(r1, r2)     // Catch:{ all -> 0x00dd }
            com.samsung.android.game.gos.ipm.AndroidBattery r1 = r5.mAndroidBattery     // Catch:{ all -> 0x00dd }
            int r1 = r1.getUsbTemperature()     // Catch:{ all -> 0x00dd }
            r2 = 100
            if (r1 >= r2) goto L_0x003a
            java.lang.String r0 = "Ipm"
            java.lang.String r1 = "Ignoring tempHardLimit; usbTemp not available targetPST will remain unchanged"
            com.samsung.android.game.gos.ipm.Log.d(r0, r1)     // Catch:{ all -> 0x00dd }
            monitor-exit(r5)
            return r6
        L_0x003a:
            int r2 = r6 + -80
            if (r1 > r2) goto L_0x005e
            java.lang.String r0 = "Ipm"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00dd }
            r2.<init>()     // Catch:{ all -> 0x00dd }
            java.lang.String r3 = "Ignoring tempHardLimit: PST will be :"
            r2.append(r3)     // Catch:{ all -> 0x00dd }
            r2.append(r6)     // Catch:{ all -> 0x00dd }
            java.lang.String r3 = " :: Current Temperature: "
            r2.append(r3)     // Catch:{ all -> 0x00dd }
            r2.append(r1)     // Catch:{ all -> 0x00dd }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x00dd }
            com.samsung.android.game.gos.ipm.Log.d(r0, r1)     // Catch:{ all -> 0x00dd }
            monitor-exit(r5)
            return r6
        L_0x005e:
            int r2 = r0.intValue()     // Catch:{ all -> 0x00dd }
            r3 = -1
            if (r2 == r3) goto L_0x00db
            if (r0 != 0) goto L_0x0068
            goto L_0x00db
        L_0x0068:
            if (r1 < r6) goto L_0x0096
            java.lang.String r2 = "Ipm"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00dd }
            r3.<init>()     // Catch:{ all -> 0x00dd }
            java.lang.String r4 = "Using tempHardLimit: Adjusting Configured TargetTemp from "
            r3.append(r4)     // Catch:{ all -> 0x00dd }
            r3.append(r6)     // Catch:{ all -> 0x00dd }
            java.lang.String r6 = " to "
            r3.append(r6)     // Catch:{ all -> 0x00dd }
            r3.append(r0)     // Catch:{ all -> 0x00dd }
            java.lang.String r6 = " :: tempNow: "
            r3.append(r6)     // Catch:{ all -> 0x00dd }
            r3.append(r1)     // Catch:{ all -> 0x00dd }
            java.lang.String r6 = r3.toString()     // Catch:{ all -> 0x00dd }
            com.samsung.android.game.gos.ipm.Log.d(r2, r6)     // Catch:{ all -> 0x00dd }
            int r6 = r0.intValue()     // Catch:{ all -> 0x00dd }
            monitor-exit(r5)
            return r6
        L_0x0096:
            r2 = 1092616192(0x41200000, float:10.0)
            int r3 = r0.intValue()     // Catch:{ all -> 0x00dd }
            int r3 = r3 - r6
            float r3 = (float) r3     // Catch:{ all -> 0x00dd }
            int r4 = r6 - r1
            float r4 = (float) r4     // Catch:{ all -> 0x00dd }
            float r3 = r3 / r4
            float r3 = r3 * r2
            int r2 = (int) r3     // Catch:{ all -> 0x00dd }
            int r2 = r2 + r6
            int r3 = r0.intValue()     // Catch:{ all -> 0x00dd }
            if (r2 <= r3) goto L_0x00af
            int r2 = r0.intValue()     // Catch:{ all -> 0x00dd }
        L_0x00af:
            if (r2 >= r6) goto L_0x00b3
            monitor-exit(r5)
            return r6
        L_0x00b3:
            java.lang.String r0 = "Ipm"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00dd }
            r3.<init>()     // Catch:{ all -> 0x00dd }
            java.lang.String r4 = "Using tempHardLimit: Adjusting Configured TargetTemp from "
            r3.append(r4)     // Catch:{ all -> 0x00dd }
            r3.append(r6)     // Catch:{ all -> 0x00dd }
            java.lang.String r6 = " to "
            r3.append(r6)     // Catch:{ all -> 0x00dd }
            r3.append(r2)     // Catch:{ all -> 0x00dd }
            java.lang.String r6 = " :: tempNow: "
            r3.append(r6)     // Catch:{ all -> 0x00dd }
            r3.append(r1)     // Catch:{ all -> 0x00dd }
            java.lang.String r6 = r3.toString()     // Catch:{ all -> 0x00dd }
            com.samsung.android.game.gos.ipm.Log.d(r0, r6)     // Catch:{ all -> 0x00dd }
            monitor-exit(r5)
            return r2
        L_0x00db:
            monitor-exit(r5)
            return r6
        L_0x00dd:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.Ipm.adjustTargetTemp(int):int");
    }

    /* access modifiers changed from: package-private */
    public int getUsbTemp() {
        return this.mAndroidBattery.getUsbTemperature();
    }

    /* access modifiers changed from: package-private */
    public synchronized int getTargetTemperatureLimit() {
        Log.d(LOG_TAG, "getTargetTemperatureLimit");
        if (this.mParametersUsed.tempHardLimit == null) {
            return -1;
        }
        return this.mParametersUsed.tempHardLimit.intValue();
    }

    /* access modifiers changed from: package-private */
    public synchronized void changeTempHardLimit(int i) {
        Log.d(LOG_TAG, "changeTempHardLimit: " + i);
        this.mParametersUsed.tempHardLimit = Integer.valueOf(i);
    }

    public synchronized int getCustomTfpsFlags() {
        Log.d(LOG_TAG, "getCustomTfpsFlags");
        if (this.mParametersUsed.customTfpsFlags == null) {
            return 0;
        }
        return this.mParametersUsed.customTfpsFlags.intValue();
    }

    /* access modifiers changed from: package-private */
    public synchronized IntelMode getIntelMode() {
        Log.d(LOG_TAG, "getIntelMode");
        if (this.mIntelMode == null) {
            return IntelMode.QTABLE_X;
        }
        return this.mIntelMode;
    }

    /* access modifiers changed from: package-private */
    public synchronized String getIntelConfig() {
        Log.d(LOG_TAG, "getIntelConfig");
        if (this.mIntelConfig == null) {
            return new String();
        }
        return this.mIntelConfig;
    }

    /* access modifiers changed from: package-private */
    public synchronized void startNative() {
        Log.v(LOG_TAG, "startNative");
        if (this.mIsStarted) {
            Log.e(LOG_TAG, "Already started, stopping first. Double start issue");
            stopNativeThread();
        } else {
            this.mActivityManager.setIsForeground(true);
        }
        startNativeThread();
        this.mIsStarted = true;
    }

    /* access modifiers changed from: package-private */
    public synchronized void stopNative() {
        Log.v(LOG_TAG, "stopNative");
        if (this.mIsStarted) {
            stopNativeThread();
        } else {
            Log.e(LOG_TAG, "Already stopped. Double pause issue");
        }
        this.mIsStarted = false;
        this.mActivityManager.setIsForeground(false);
    }

    /* access modifiers changed from: package-private */
    public void startNativeThread() {
        new Thread(new Runnable() {
            public final void run() {
                Ipm.this.lambda$startNativeThread$0$Ipm();
            }
        }).start();
    }

    public /* synthetic */ void lambda$startNativeThread$0$Ipm() {
        Log.d(LOG_TAG, "Started Thread");
        this.mIpm.start(getIntelMode(), getIntelConfig());
    }

    /* access modifiers changed from: package-private */
    public void stopNativeThread() {
        this.mIpm.stop();
    }

    public void destroySystem() {
        this.mIpm.releaseMem();
    }

    public void addJson(String str, String str2) {
        Log.v(LOG_TAG, "addJson");
        this.mIpm.addJson(str, str2);
    }

    public void setProfile(Profile profile) {
        Log.d(LOG_TAG, "setProfile to " + profile);
        this.mParametersUsed.profile = profile;
        this.mIpm.setProfile(profile);
    }

    public Profile getProfile() {
        return this.mIpm.getProfile();
    }

    public Profile getOriginalProfile() {
        return this.mOriginalProfile;
    }

    /* access modifiers changed from: package-private */
    public void setCustomProfile(float f, float f2, float f3, float f4) {
        Log.d(LOG_TAG, "setCustomProfile to (" + f + "," + f2 + "," + f3 + "," + f4 + ")");
        ParametersUsed parametersUsed = this.mParametersUsed;
        parametersUsed.customProfileValues = new Float[]{Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4)};
        ParametersUsed parametersUsed2 = this.mParametersUsed;
        parametersUsed2.softkill_value = Integer.valueOf(Math.round(((float) parametersUsed2.target_pst) + (10.0f * f)));
        this.mIpm.setCustomProfile(f, f2, f3, f4);
    }

    public void setLogLevel(LogLevel logLevel) {
        Log.d(LOG_TAG, "setLogLevel " + logLevel);
        this.mIpm.setLogLevel(logLevel);
    }

    public void setSupertrain(boolean z) {
        Log.d(LOG_TAG, "setSupertrain " + z);
        this.mIpm.setSupertrain(z);
    }

    public boolean getSupertrain() {
        return this.mIpm.getSupertrain();
    }

    public synchronized void setOnlyCapture(boolean z) {
        Log.d(LOG_TAG, "setOnlyCapture " + z);
        this.mIpm.setOnlyCapture(z);
    }

    public boolean getOnlyCapture() {
        return this.mIpm.getOnlyCapture();
    }

    public void setTargetPST(int i) {
        Log.d(LOG_TAG, "setTargetPST " + i);
        this.mParametersUsed.target_pst = i;
        this.mIpm.setTargetPST(i);
    }

    public int getTargetPST() {
        return this.mIpm.getTargetPST();
    }

    /* access modifiers changed from: package-private */
    public void setTargetLRPST(int i) {
        Log.d(LOG_TAG, "setTargetLRPST " + i);
        this.mIpm.setTargetLRPST(i);
        this.mLRPST = i;
        this.mParametersUsed.target_lrpst = i;
    }

    public void setCustomTfpsFlags(int i) {
        Log.d(LOG_TAG, "setCustomTfpsFlags " + i);
        this.mIpm.setCustomTfpsFlags(i);
        this.mParametersUsed.customTfpsFlags = Integer.valueOf(i);
    }

    public void setFixedTargetFps(float f) {
        this.mIpm.setFixedTargetFps(f);
    }

    public void resetFixedTargetFps() {
        this.mIpm.resetFixedTargetFps();
    }

    public String getStatistics() {
        Log.d(LOG_TAG, "getStatistics");
        return this.mIpm.getStatistics();
    }

    public int getVersion() {
        Log.d(LOG_TAG, "getVersion");
        return this.mNativeLoaded;
    }

    /* access modifiers changed from: package-private */
    public void setCPUBottomFreq(long j) {
        Log.d(LOG_TAG, "setCPUBottomFreq");
        this.mParametersUsed.cpuBottomFreq = Long.valueOf(j);
        this.mIpm.setCPUBottomFreq(j);
    }

    public void setMinFreqs(long j, long j2) {
        Log.d(LOG_TAG, "setMinFreqs");
        this.mParametersUsed.minFreqCpu = Long.valueOf(j2);
        this.mParametersUsed.minFreqGpu = Long.valueOf(j);
        this.mIpm.setMinFreqs(j, j2);
    }

    public void setMaxFreqs(long j, long j2) {
        Log.d(LOG_TAG, "setMaxFreqs");
        this.mParametersUsed.maxFreqCpu = Long.valueOf(j2);
        this.mParametersUsed.maxFreqGpu = Long.valueOf(j);
        this.mIpm.setMaxFreqs(j, j2);
    }

    public void setIntelMode(IntelMode intelMode) {
        Log.v(LOG_TAG, "setIntelMode");
        setIntelMode(intelMode, new String());
    }

    public void setIntelMode(IntelMode intelMode, String str) {
        Log.v(LOG_TAG, "setIntelMode with cfg");
        this.mIntelMode = intelMode;
        this.mIntelConfig = str;
        this.mParametersUsed.intelMode = intelMode;
        this.mParametersUsed.intelConfig = this.mIntelConfig;
    }

    private void updateFrameInterpolation() {
        Log.d(LOG_TAG, "updateFrameInterpolation");
        if (isFrameInterpolationEnabled()) {
            this.mIpm.enableFrameInterpolation(getFrameInterpolationTemperatureOffset(), getFrameInterpolationFrameRateOffset(), getFrameInterpolationDecayHalfLife());
        } else {
            this.mIpm.disableFrameInterpolation();
        }
    }

    public void setFrameInterpolationEnabled(boolean z) {
        Log.d(LOG_TAG, "setFrameInterpolationEnabled");
        this.mFrameInterpolationEnabled = z;
        updateFrameInterpolation();
    }

    public boolean isFrameInterpolationEnabled() {
        return this.mFrameInterpolationEnabled;
    }

    public void setFrameInterpolationTemperatureOffset(float f) {
        Log.d(LOG_TAG, "setFrameInterpolationTemperatureOffset");
        this.mFrameInterpolationTemperatureOffset = f;
        updateFrameInterpolation();
    }

    public float getFrameInterpolationTemperatureOffset() {
        return this.mFrameInterpolationTemperatureOffset;
    }

    public void setFrameInterpolationFrameRateOffset(float f) {
        Log.d(LOG_TAG, "setFrameInterpolationFrameRateOffset");
        this.mFrameInterpolationFrameRateOffset = f;
        updateFrameInterpolation();
    }

    public float getFrameInterpolationFrameRateOffset() {
        return this.mFrameInterpolationFrameRateOffset;
    }

    public void setFrameInterpolationDecayHalfLife(float f) {
        Log.d(LOG_TAG, "setFrameInterpolationDecayHalfLife");
        this.mFrameInterpolationDecayHalfLife = f;
        updateFrameInterpolation();
    }

    public float getFrameInterpolationDecayHalfLife() {
        return this.mFrameInterpolationDecayHalfLife;
    }

    public void setCpuGap(int i) {
        Log.d(LOG_TAG, "setCpuGap");
        this.mIpm.setCpuGap(i);
    }

    public void setGpuGap(int i) {
        Log.d(LOG_TAG, "setGpuGap");
        this.mIpm.setGpuGap(i);
    }

    public void setGpuMinBoost(int i) {
        Log.d(LOG_TAG, "setGpuMinBoost");
        this.mIpm.setGpuMinBoost(i);
    }

    /* access modifiers changed from: package-private */
    public String getID() {
        Log.d(LOG_TAG, "getID");
        return this.mIpm.getID();
    }

    /* access modifiers changed from: package-private */
    public void setUseSsrm(boolean z) {
        Log.v(LOG_TAG, "setUseSsrm");
        this.mIpm.setUseSsrm(z);
    }

    /* access modifiers changed from: package-private */
    public int getMaxFpsGuess() {
        Log.d(LOG_TAG, "getMaxFpsGuess");
        return this.mIpm.getMaxFpsGuess();
    }

    /* access modifiers changed from: package-private */
    public void handOverControlToGameSDK(String str, boolean z) {
        Log.d(LOG_TAG, String.format("Should SPA HandOver Control to GameSDK: (packageName=%s, enable=%b)", new Object[]{str, Boolean.valueOf(z)}));
        this.mIpm.handOverControlToGameSDK(str, z);
    }

    public boolean isClusterControlAvailable() {
        Log.v(LOG_TAG, "isClusterControlAvailable");
        return this.mIpm.isClusterControlAvailable();
    }

    public float getCurrentTargetTemperature() {
        Log.v(LOG_TAG, "getCurrentTargetTemperature");
        return this.mIpm.getCurrentTargetTemp();
    }

    public float getCurrentTemperature() {
        Log.v(LOG_TAG, "getCurrentTemperature");
        return this.mIpm.getCurrentTemp();
    }

    public boolean canRun() {
        Log.d(LOG_TAG, "canRun");
        return this.mIpm.canRun();
    }

    public void setThreadControl(int i) {
        Log.d(LOG_TAG, "setThreadControl: " + i);
        this.mIpm.setThreadControl(i);
    }

    public void setThermalControl(boolean z) {
        Log.d(LOG_TAG, "setThermalControl: " + z);
        this.mIpm.setThermalControl(z);
    }

    public void setAllowMlOff(boolean z) {
        Log.d(LOG_TAG, "setAllowMlOff: " + z);
        this.mIpm.setAllowMlOff(z);
    }

    public void setEnableBusFreq(boolean z) {
        Log.d(LOG_TAG, "setEnableBusFreq: " + z);
        this.mIpm.setEnableBusFreq(z);
    }

    public void setDynamicDecisions(boolean z) {
        Log.d(LOG_TAG, "setHighStabilityMode: " + z);
        this.mIpm.setHighStabilityMode(z);
        this.mDynamicDecisions = z;
    }

    public boolean getDynamicDecisions() {
        return this.mDynamicDecisions;
    }

    public void setLowLatencySceneSDK(int i, String str, int i2, boolean z) {
        Log.d(LOG_TAG, "setLowLatencySceneSDK fd " + i + " description " + str + " size " + i2);
        this.mIpm.setLowLatencySceneSDK(i, str, i2, z);
    }

    public String getToTGPA() {
        Log.d(LOG_TAG, "getToTGPA");
        return this.mIpm.getToTGPA();
    }

    public void setEnableToTGPA(boolean z) {
        Log.d(LOG_TAG, "setEnableToTGPA " + z);
        this.mIpm.setEnableToTGPA(z);
    }

    public String readSessionsJSON() {
        Log.d(LOG_TAG, "readSessionsJSON");
        return this.mIpm.readSessionsJSON();
    }

    public String readSessionsJSON(int[] iArr) {
        Log.d(LOG_TAG, "readSessionsJSON");
        if (iArr == null) {
            return this.mIpm.readSessionsJSON();
        }
        VectorInt vectorInt = new VectorInt();
        for (int add : iArr) {
            vectorInt.add(add);
        }
        return this.mIpm.readSessionsJSON(vectorInt);
    }

    public String readDataJSON(List<ParameterRequest> list, int i, long j, long j2) {
        Log.d(LOG_TAG, "readDataJSON");
        if (list == null || list.isEmpty()) {
            return "{}";
        }
        VectorParameterRequest vectorParameterRequest = new VectorParameterRequest();
        for (ParameterRequest add : list) {
            vectorParameterRequest.add(add);
        }
        return this.mIpm.readDataJSON(vectorParameterRequest, i, j, j2);
    }

    public void setEnableAnyMode(boolean z) {
        Log.d(LOG_TAG, "setEnableAnyMode: " + z);
        this.mEnableAnyMode = z;
    }

    public boolean getEnableAnyMode() {
        return this.mEnableAnyMode;
    }

    public void setForceMode(boolean z) {
        Log.d(LOG_TAG, "setForceMode: " + z);
        this.mForceMode = z;
    }

    public boolean getForceMode() {
        return this.mForceMode;
    }

    public void setEnableLRPST(boolean z) {
        Log.d(LOG_TAG, "setEnableLRPST: " + z);
        this.mEnableLRPST = z;
    }

    public boolean getEnableLRPST() {
        return this.mEnableLRPST;
    }

    public void setEnableAllowMlOff(boolean z) {
        Log.d(LOG_TAG, "setEnableAllowMlOff: " + z);
        this.mEnableAllowMlOff = z;
    }

    public boolean getEnableAllowMlOff() {
        return this.mEnableAllowMlOff;
    }

    public void setEnableGpuMinFreqControl(boolean z) {
        Log.d(LOG_TAG, "setEnableGpuMinFreqControl: " + z);
        this.mEnableGpuMinFreqControl = z;
        this.mIpm.setGpuGap(z ? -2 : -1);
    }

    public boolean getEnableGpuMinFreqControl() {
        return this.mEnableGpuMinFreqControl;
    }

    public void setEnableGTLM(boolean z) {
        Log.d(LOG_TAG, "setEnableGTLM: " + z);
        this.mEnableGTLM = z;
    }

    public boolean getEnableGTLM() {
        return this.mEnableGTLM;
    }

    public void setEnableCpuMinFreqControl(boolean z) {
        Log.d(LOG_TAG, "setEnableCpuMinFreqControl: " + z);
        this.mEnableCpuMinFreqControl = z;
        this.mIpm.setCpuGap(z ? -2 : -1);
    }

    public boolean getEnableCpuMinFreqControl() {
        return this.mEnableCpuMinFreqControl;
    }

    public void setEnableBusMinFreqControl(boolean z) {
        Log.d(LOG_TAG, "setEnableBusMinFreqControl: " + z);
        this.mEnableBusMinFreqControl = z;
        this.mIpm.setBusGap(z ? -2 : -1);
    }

    public boolean getEnableBusMinFreqControl() {
        return this.mEnableBusMinFreqControl;
    }

    public void setLRPST(int i) {
        Log.d(LOG_TAG, "setLRPST: " + i);
        this.mLRPST = i;
    }

    public int getLRPST() {
        return this.mLRPST;
    }

    public boolean isCpuEfficiencyTablePopulated() {
        return this.mAndroidDeviceSettings.getClusterSize() > 0;
    }

    public void setOriginalIpmProfile(int i) {
        this.mOriginalProfile = Profile.fromInt(i);
    }

    public void setDynamicRefreshRate(float f) {
        Log.d(LOG_TAG, "setDynamicRefreshRate: " + f);
        this.mAndroidDisplay.setDynamicRefreshRate(f);
    }

    public void setDynamicPowerMode(boolean z) {
        this.mIpm.setDynamicPowerMode(z);
    }
}
