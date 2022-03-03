package com.samsung.android.game.gos.feature.autocontrol;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.ISecureSettingChangeListener;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.SecureSettingHelper;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.dfs.FpsController;
import com.samsung.android.game.gos.feature.ringlog.RinglogInterface;
import com.samsung.android.game.gos.feature.ringlog.RinglogUtil;
import com.samsung.android.game.gos.feature.ringlog.SystemStatusUtil;
import com.samsung.android.game.gos.feature.volumecontrol.VolumeControlFeature;
import com.samsung.android.game.gos.ipm.Aggregation;
import com.samsung.android.game.gos.ipm.ParameterRequest;
import com.samsung.android.game.gos.selibrary.SePowerManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.BadHardcodedConstant;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.RinglogConstants;
import com.samsung.android.game.gos.value.SecureSettingConstants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AutoControlFeature implements RuntimeInterface, ComponentCallbacks2, ISecureSettingChangeListener {
    private static final int DFS_MAX_SUPPORTED = 60;
    private static final int FLAG_TEMPERATURE_CONTROL_AUDIO = 2;
    private static final int FLAG_TEMPERATURE_CONTROL_BRIGHTNESS = 1;
    private static final String INTENT_ACTION_GAMETOOLS_OPTIMIZATION = "com.samsung.android.game.gos.action.OPTIMIZATION";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = AutoControlFeature.class.getSimpleName();
    static final int MIN_SESSION_LENGTH = 10000;
    private static final String MSG_BUNDLE_FORCE_UPDATE = "FORCE_UPDATE";
    private static final int MSG_STOP = 0;
    private static final int MSG_TEMPERATURE_CHECK = 1;
    private static final int PST_CHECK_INTERVAL_MS_DEFAULT = 30000;
    private static final int PST_CHECK_INTERVAL_MS_HIGH_TEMP = 60000;
    private final String FPS_TAG;
    private Map<Level, LevelControl> mControlLevels;
    /* access modifiers changed from: private */
    public volatile ControlSettings mControlSettings;
    private ControlStatus mControlStatus;
    private long mGameFocusInTime;
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public volatile boolean mIsGameFocusIn;
    /* access modifiers changed from: private */
    public Level mLevel;
    private String mPkgName;

    public String getName() {
        return Constants.V4FeatureFlag.AUTO_CONTROL;
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onSettingChangeExternal() {
    }

    public static String getGppStateJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(GosInterface.KeyName.AUTO_CONTROL_STATUS, getAutoControlStatus());
        } catch (JSONException e) {
            String str = LOG_TAG;
            GosLog.e(str, "getGppStateJSON " + e);
        }
        return jSONObject.toString();
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        this.mGameFocusInTime = System.currentTimeMillis();
        this.mIsGameFocusIn = true;
        this.mControlStatus = new ControlStatus();
        this.mPkgName = pkgData.getPackageName();
        updateControlLevels();
        this.mControlSettings.update();
        String str = LOG_TAG;
        GosLog.i(str, "onFocusIn , " + this.mControlSettings);
        if (this.mControlSettings.shouldControl()) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler.sendEmptyMessage(1);
        }
        registerMemoryTrimEvents();
    }

    public void onFocusOut(PkgData pkgData) {
        GosLog.d(LOG_TAG, "onFocusOut");
        resetAutoControls(pkgData);
    }

    public void restoreDefault(PkgData pkgData) {
        if (pkgData != null && this.mIsGameFocusIn) {
            GosLog.d(LOG_TAG, "restoreDefault");
            resetAutoControls(pkgData);
        }
    }

    private void resetAutoControls(PkgData pkgData) {
        this.mIsGameFocusIn = false;
        this.mPkgName = pkgData.getPackageName();
        unregisterMemoryTrimEvents();
        this.mHandler.removeCallbacksAndMessages((Object) null);
        resetControls();
    }

    public void onTrimMemory(int i) {
        String str = LOG_TAG;
        GosLog.i(str, "onTrimMemory " + i);
        checkMemoryState();
    }

    public void onLowMemory() {
        GosLog.d(LOG_TAG, "onLowMemory ");
    }

    public void onSecureSettingChanged(String str, Object obj) {
        GosLog.d(LOG_TAG, String.format("onSecureSettingChanged(), key=%s, value=%s", new Object[]{str, obj}));
        if (this.mIsGameFocusIn) {
            onSettingsChange(str, obj);
        }
    }

    public enum Level {
        L0(1.0f, false, 1.0f),
        L1(0.9f, true, 1.0f),
        L2(0.7f, true, 0.833f),
        L3(0.5f, true, 0.75f);
        
        float fpsPercent;
        float maxBrightnessPercent;
        boolean volumeLimitEnable;

        private Level(float f, boolean z, float f2) {
            this.maxBrightnessPercent = f;
            this.fpsPercent = f2;
            this.volumeLimitEnable = z;
        }

        public static Level valueOf(int i) throws InvalidParameterException {
            Level[] values = values();
            if (i >= 0 && i < values.length) {
                return values[i];
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Parameter ordinal should be [0-");
            sb.append(values.length - 1);
            sb.append("]");
            throw new InvalidParameterException(sb.toString());
        }

        static Level getLevelFromPst(float f, float f2, ControlSettings controlSettings) {
            if (controlSettings.getTestLevel() != null) {
                String access$400 = AutoControlFeature.LOG_TAG;
                GosLog.d(access$400, "getLevelFromPst TestLevel mode, level=" + controlSettings.getTestLevel().name());
                return controlSettings.getTestLevel();
            }
            if (controlSettings.getTestTemperatures() != null) {
                try {
                    float[] access$500 = controlSettings.getTestTemperatures();
                    for (int i = 0; i < access$500.length; i++) {
                        if (f < access$500[i]) {
                            Level valueOf = valueOf(i);
                            if (valueOf != null) {
                                String access$4002 = AutoControlFeature.LOG_TAG;
                                GosLog.d(access$4002, "getLevelFromPst TestLevelTemperatures mode, level=" + valueOf.name());
                            }
                            return valueOf;
                        }
                    }
                    String access$4003 = AutoControlFeature.LOG_TAG;
                    GosLog.d(access$4003, "getLevelFromPst TestLevel mode, level=" + valueOf(values().length - 1).name());
                    return valueOf(values().length - 1);
                } catch (InvalidParameterException e) {
                    String access$4004 = AutoControlFeature.LOG_TAG;
                    GosLog.e(access$4004, "getLevelFromPst " + e.getMessage(), e);
                }
            }
            float f3 = f - f2;
            if (f3 < 0.0f) {
                return L0;
            }
            if (f3 < 2.0f) {
                return L1;
            }
            if (f3 < 4.0f) {
                return L2;
            }
            return L3;
        }
    }

    private AutoControlFeature() {
        this.mLevel = Level.L0;
        this.FPS_TAG = getClass().getSimpleName();
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                Bundle data = message.getData();
                boolean z = data != null && data.getBoolean(AutoControlFeature.MSG_BUNDLE_FORCE_UPDATE);
                float[] access$700 = AutoControlFeature.this.getPstTpst();
                AutoControlFeature.this.mControlSettings.update();
                String access$400 = AutoControlFeature.LOG_TAG;
                GosLog.i(access$400, "handleMessage pst=" + access$700[0] + ", tpst=" + access$700[1] + ", msg=" + message.what + ", forceUpdate=" + z + ", mLevel=" + AutoControlFeature.this.mLevel + ", " + AutoControlFeature.this.mControlSettings);
                if (((access$700[0] == -1.0f || access$700[1] == -1.0f) && !AutoControlFeature.this.mControlSettings.isTestMode()) || !AutoControlFeature.this.mControlSettings.shouldControl()) {
                    AutoControlFeature.this.resetControls();
                } else {
                    AutoControlFeature.this.controlPerformance(access$700[0], access$700[1], z);
                }
                if (message.what != 0 && AutoControlFeature.this.mIsGameFocusIn) {
                    sendEmptyMessageDelayed(1, AutoControlFeature.this.mControlSettings.pstCheckInterval);
                }
            }
        };
        this.mControlLevels = new HashMap();
        updateControlLevels();
        this.mControlSettings = new ControlSettings();
        this.mControlStatus = new ControlStatus();
        SecureSettingHelper.getInstance().registerListener(SecureSettingConstants.KEY_AUTO_CONTROL, this);
        SecureSettingHelper.getInstance().registerListener(SecureSettingConstants.KEY_ALLOW_MORE_HEAT, this);
    }

    private void updateControlLevels() {
        int i;
        GosLog.i(LOG_TAG, "updateControlLevels maxBrightness=255, maxFps=60");
        boolean isRGB = isRGB();
        for (Level level : Level.values()) {
            int round = Math.round(level.fpsPercent * 60.0f);
            if (isRGB && round >= 50 && round <= 59) {
                GosLog.d(LOG_TAG, "Temporary set Fps " + round + " -> 48");
                round = 48;
            }
            if (ValidationUtil.floatEqual(level.maxBrightnessPercent, 1.0f)) {
                i = -1;
            } else {
                i = Math.round(level.maxBrightnessPercent * 255.0f);
            }
            this.mControlLevels.put(level, new LevelControl(i, level.volumeLimitEnable, round));
        }
    }

    public static AutoControlFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public void controlPerformance(float f, float f2, boolean z) {
        Level levelFromPst = Level.getLevelFromPst(f, f2 + this.mControlSettings.getAllowedMoreHeat(), this.mControlSettings);
        if (levelFromPst != this.mLevel || z) {
            LevelControl levelControl = this.mControlLevels.get(levelFromPst);
            LevelControl levelControl2 = this.mControlLevels.get(Level.L0);
            String str = LOG_TAG;
            GosLog.i(str, "controlPerformance lvl=" + levelFromPst + ", " + levelControl + ", force=" + z);
            if (levelControl == null || levelControl2 == null) {
                String str2 = LOG_TAG;
                GosLog.w(str2, "controlPerformance levelControl=" + levelControl + ", resetLevelControl=" + levelControl2 + ", return");
                return;
            }
            if (this.mControlSettings.shouldControlBrightness()) {
                SePowerManager.getInstance().setMaxBrightnessLimit(levelControl.maxBrightness);
                this.mControlStatus.brightnessControlled = true;
            } else if (this.mControlStatus.brightnessControlled) {
                SePowerManager.getInstance().setMaxBrightnessLimit(levelControl2.maxBrightness);
                this.mControlStatus.brightnessControlled = false;
            }
            if (this.mControlSettings.shouldControlVolume()) {
                VolumeControlFeature.enableVolumeLimit(this.mPkgName, levelControl.volumeLimitEnable, AppContext.get().getPackageManager());
                this.mControlStatus.volumeControlled = true;
            } else if (this.mControlStatus.volumeControlled) {
                VolumeControlFeature.enableVolumeLimit(this.mPkgName, levelControl2.volumeLimitEnable, AppContext.get().getPackageManager());
                this.mControlStatus.volumeControlled = false;
            }
            FpsController instance = FpsController.getInstance();
            if (this.mControlSettings.shouldControlFps()) {
                instance.requestFpsFixedValue(levelControl.fps, this.FPS_TAG);
                this.mControlStatus.fpsControlled = true;
            } else if (this.mControlStatus.fpsControlled) {
                instance.requestFpsFixedValue(levelControl2.fps, this.FPS_TAG);
                this.mControlStatus.fpsControlled = false;
            }
            if (levelFromPst == Level.L0) {
                long unused = this.mControlSettings.pstCheckInterval = 30000;
            } else {
                long unused2 = this.mControlSettings.pstCheckInterval = 60000;
            }
            this.mLevel = levelFromPst;
            sendEdgeLightingIntent(levelFromPst);
        }
    }

    private boolean isRGB() {
        String originalDeviceName = AppVariable.getOriginalDeviceName();
        boolean contains = Arrays.asList(BadHardcodedConstant.RGB_DEVICE_LIST.split(",")).contains(originalDeviceName);
        String str = LOG_TAG;
        GosLog.v(str, "isRGB()-deviceName: " + originalDeviceName + ", result: " + contains);
        return contains;
    }

    /* access modifiers changed from: package-private */
    public void resetControls() {
        String str = LOG_TAG;
        GosLog.i(str, "resetControls old level=" + this.mLevel);
        this.mControlStatus.memoryBroadcastEventSent = false;
        if (this.mLevel != Level.L0) {
            Level level = Level.L0;
            this.mLevel = level;
            LevelControl levelControl = this.mControlLevels.get(level);
            if (levelControl == null) {
                GosLog.w(LOG_TAG, "resetControls lvlControl=null, return");
                return;
            }
            if (this.mControlStatus.brightnessControlled) {
                SePowerManager.getInstance().setMaxBrightnessLimit(levelControl.maxBrightness);
            }
            if (this.mControlStatus.volumeControlled) {
                VolumeControlFeature.enableVolumeLimit(this.mPkgName, levelControl.volumeLimitEnable, AppContext.get().getPackageManager());
            }
            if (this.mControlStatus.fpsControlled) {
                FpsController.getInstance().requestFpsFixedValue(levelControl.fps, this.FPS_TAG);
            }
            if (this.mControlStatus.brightnessControlled || this.mControlStatus.volumeControlled || this.mControlStatus.fpsControlled) {
                sendEdgeLightingIntent(this.mLevel);
            }
            long unused = this.mControlSettings.pstCheckInterval = 30000;
            this.mControlStatus = new ControlStatus();
        }
    }

    /* access modifiers changed from: private */
    public float[] getPstTpst() {
        float[] fArr = {-1.0f, -1.0f};
        try {
            RinglogConstants.SessionWrapper latestSessionInfo = RinglogUtil.getLatestSessionInfo("com.samsung.android.game.gos");
            if (latestSessionInfo != null) {
                if (latestSessionInfo.info != null) {
                    boolean isUsingLrpst = latestSessionInfo.info.isUsingLrpst();
                    boolean isOngoingSession = isOngoingSession(latestSessionInfo, this.mGameFocusInTime);
                    long j = latestSessionInfo.info.data_end_ms - latestSessionInfo.info.data_start_ms;
                    if (isOngoingSession) {
                        if (j >= RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS) {
                            ArrayList arrayList = new ArrayList();
                            if (isUsingLrpst) {
                                arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.TEMP_LRPST.getName(), Aggregation.MEAN, RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS));
                            } else {
                                arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.TEMP_PST.getName(), Aggregation.MEAN, RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS));
                            }
                            _getPstTpstFromSessionInfo(fArr, arrayList, isUsingLrpst, latestSessionInfo);
                            return fArr;
                        }
                    }
                    String str = LOG_TAG;
                    GosLog.d(str, "getPstTpst return, isRecentSession=" + isOngoingSession + ", sessionLen=" + j);
                }
            }
            return fArr;
        } catch (JSONException e) {
            String str2 = LOG_TAG;
            GosLog.e(str2, "getCurrentPst " + e);
        }
    }

    /* access modifiers changed from: package-private */
    public void _getPstTpstFromSessionInfo(float[] fArr, List<ParameterRequest> list, boolean z, RinglogConstants.SessionWrapper sessionWrapper) throws JSONException {
        JSONArray jSONArray;
        JSONObject jSONObject = new JSONObject(RinglogInterface.getInstance().readDataSimpleRequestJSON("com.samsung.android.game.gos", list, sessionWrapper.id, -10000, 0));
        if (z) {
            jSONArray = jSONObject.optJSONArray(RinglogConstants.PerfParams.TEMP_LRPST.getName());
        } else {
            jSONArray = jSONObject.optJSONArray(RinglogConstants.PerfParams.TEMP_PST.getName());
        }
        if (jSONArray != null && jSONArray.length() > 0) {
            fArr[0] = (float) jSONArray.getDouble(jSONArray.length() - 1);
        }
        if (z) {
            fArr[1] = ((float) sessionWrapper.info.targetLRPST) / 10.0f;
        } else {
            fArr[1] = ((float) sessionWrapper.info.targetPst) / 10.0f;
        }
    }

    private boolean isOngoingSession(RinglogConstants.SessionWrapper sessionWrapper, long j) {
        return (sessionWrapper == null || sessionWrapper.info == null || j >= sessionWrapper.info.data_start_ms) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public void onSettingsChange(String str, Object obj) {
        this.mControlSettings.update(str, obj);
        String str2 = LOG_TAG;
        GosLog.i(str2, "onSettingsChange(), mControlSettings=" + this.mControlSettings);
        this.mHandler.removeCallbacksAndMessages((Object) null);
        if (this.mControlSettings.shouldControl()) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(MSG_BUNDLE_FORCE_UPDATE, true);
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.setData(bundle);
            this.mHandler.sendMessage(obtain);
            return;
        }
        this.mHandler.sendEmptyMessage(0);
    }

    private void sendEdgeLightingIntent(Level level) {
        Intent intent = new Intent(INTENT_ACTION_GAMETOOLS_OPTIMIZATION);
        intent.setPackage(Constants.PACKAGE_NAME_GAME_TOOLS);
        intent.putExtra("by", "gpp");
        intent.putExtra("trigger", "temperature");
        intent.putExtra("level", level.ordinal());
        AppContext.get().sendBroadcast(intent);
    }

    private void registerMemoryTrimEvents() {
        AppContext.get().registerComponentCallbacks(this);
        checkMemoryState();
    }

    private void unregisterMemoryTrimEvents() {
        AppContext.get().unregisterComponentCallbacks(this);
    }

    private void checkMemoryState() {
        if (this.mControlSettings.shouldControl() && !this.mControlStatus.memoryBroadcastEventSent) {
            ActivityManager.RunningAppProcessInfo myProcessInfo = SystemStatusUtil.getMyProcessInfo();
            int memoryGrade = getMemoryGrade();
            if (myProcessInfo.lastTrimLevel >= SystemStatusUtil.LOW_MEMORY_TRIM_LEVEL && memoryGrade == 0) {
                sendMemoryTriggerIntent(memoryGrade);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void sendMemoryTriggerIntent(int i) {
        GosLog.d(LOG_TAG, "checkMemoryState sendBroadcast to GameBooster for memory trigger case");
        Intent intent = new Intent(INTENT_ACTION_GAMETOOLS_OPTIMIZATION);
        intent.setPackage(Constants.PACKAGE_NAME_GAME_TOOLS);
        intent.putExtra("by", "gpp");
        intent.putExtra("trigger", Constants.RingLog.Parameter.MEMORY);
        intent.putExtra(GosInterface.KeyName.GRADE, i);
        AppContext.get().sendBroadcast(intent);
        this.mControlStatus.memoryBroadcastEventSent = true;
    }

    private int getMemoryGrade() {
        try {
            return SystemStatusUtil.getMemoryJSON(AppContext.get(), false).optInt(GosInterface.KeyName.GRADE, -1);
        } catch (JSONException e) {
            String str = LOG_TAG;
            GosLog.e(str, "checkMemoryState " + e);
            return -1;
        }
    }

    public static boolean applyTestLevel(int i) {
        synchronized (AutoControlFeature.class) {
            if (i >= 0) {
                if (i < Level.values().length) {
                    resetTestModes(false);
                    getInstance().mControlSettings.setTestLevel(Level.valueOf(i));
                    String str = LOG_TAG;
                    GosLog.d(str, "applyTestLevel success " + getInstance().mControlSettings.getTestLevel().name());
                    sendMsgTemperatureCheck();
                    return true;
                }
            }
            GosLog.d(LOG_TAG, "applyTestLevel failed");
            return false;
        }
    }

    public static boolean applyTestTemperatures(String str) {
        synchronized (AutoControlFeature.class) {
            if (str != null) {
                if (!str.isEmpty()) {
                    String[] split = str.split(",");
                    if (split.length != Level.values().length - 1) {
                        GosLog.d(LOG_TAG, "applyTestTemperatures, plz provide " + (Level.values().length - 1) + " key-value pairs");
                        return false;
                    }
                    int length = split.length;
                    float[] fArr = new float[length];
                    int length2 = split.length;
                    int i = 0;
                    while (true) {
                        if (i >= length2) {
                            break;
                        }
                        String str2 = split[i];
                        if (str2 != null && !str2.isEmpty() && str2.contains("<")) {
                            String[] split2 = str2.split("<");
                            if (split2.length != 2) {
                                GosLog.e(LOG_TAG, "applyTestTemperatures failed to parse " + str2);
                                break;
                            }
                            try {
                                fArr[Level.valueOf(split2[0].trim()).ordinal()] = Float.parseFloat(split2[1].trim());
                            } catch (IllegalArgumentException | NullPointerException e) {
                                GosLog.e(LOG_TAG, "applyTestTemperatures " + e);
                            }
                        }
                        i++;
                    }
                    float f = 0.0f;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (fArr[i2] <= f) {
                            GosLog.d(LOG_TAG, "applyTestTemperatures level " + i2 + "(=" + fArr[i2] + ") should be > previous level temperature=" + f);
                            return false;
                        }
                        f = fArr[i2];
                    }
                    resetTestModes(false);
                    getInstance().mControlSettings.setTestTemperatures(fArr);
                    GosLog.d(LOG_TAG, "applyTestTemperatures success " + Arrays.toString(getInstance().mControlSettings.getTestTemperatures()));
                    sendMsgTemperatureCheck();
                    return true;
                }
            }
            GosLog.d(LOG_TAG, "applyTestTemperatures invalid string");
            return false;
        }
    }

    public static void resetTestModes(boolean z) {
        synchronized (AutoControlFeature.class) {
            getInstance().mControlSettings.setTestLevel((Level) null);
            getInstance().mControlSettings.setTestTemperatures((float[]) null);
            if (z) {
                GosLog.d(LOG_TAG, "resetTestModes ");
                sendMsgTemperatureCheck();
            }
        }
    }

    private static void sendMsgTemperatureCheck() {
        AutoControlFeature instance = getInstance();
        if (instance.mIsGameFocusIn && instance.mControlSettings.shouldControl()) {
            instance.mHandler.removeCallbacksAndMessages((Object) null);
            instance.mHandler.sendEmptyMessage(1);
        }
    }

    private static class LevelControl {
        int fps;
        int maxBrightness;
        boolean volumeLimitEnable;

        LevelControl(int i, boolean z, int i2) {
            this.maxBrightness = i;
            this.volumeLimitEnable = z;
            this.fps = i2;
        }

        public String toString() {
            return "LevelControl{maxBrightness=" + this.maxBrightness + ", volumeLimitEnable=" + this.volumeLimitEnable + ", fps=" + this.fps + '}';
        }
    }

    private static class ControlStatus {
        boolean brightnessControlled;
        boolean fpsControlled;
        boolean memoryBroadcastEventSent;
        boolean volumeControlled;

        private ControlStatus() {
        }
    }

    static class ControlSettings {
        private float allowedMoreHeatCelsius;
        private boolean brightness;
        private boolean fps;
        /* access modifiers changed from: private */
        public long pstCheckInterval = 30000;
        private Level testLevel;
        private float[] testTemperatures;
        private boolean volume;

        ControlSettings() {
            update();
            this.testLevel = null;
            this.testTemperatures = null;
        }

        /* access modifiers changed from: private */
        public void update() {
            update((String) null, (Object) null);
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x0045  */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x005a  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x005c  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0067  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0069  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x00ab A[SYNTHETIC, Splitter:B:25:0x00ab] */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x00c4  */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x00d4  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void update(java.lang.String r6, java.lang.Object r7) {
            /*
                r5 = this;
                java.lang.String r0 = com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature.LOG_TAG
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "update(): secureSettingKey="
                r1.append(r2)
                r1.append(r6)
                java.lang.String r2 = ", secureSettingValue="
                r1.append(r2)
                r1.append(r7)
                java.lang.String r1 = r1.toString()
                com.samsung.android.game.gos.util.GosLog.d(r0, r1)
                java.lang.String r0 = "game_auto_temperature_control"
                boolean r1 = java.util.Objects.equals(r6, r0)
                r2 = 0
                if (r1 == 0) goto L_0x0041
                r1 = r7
                java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Exception -> 0x0035 }
                int r1 = r1.intValue()     // Catch:{ Exception -> 0x0035 }
                java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Exception -> 0x0035 }
                goto L_0x0042
            L_0x0035:
                r1 = move-exception
                java.lang.String r3 = com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature.LOG_TAG
                java.lang.String r1 = r1.getLocalizedMessage()
                com.samsung.android.game.gos.util.GosLog.e(r3, r1)
            L_0x0041:
                r1 = r2
            L_0x0042:
                r3 = 0
                if (r1 != 0) goto L_0x0052
                com.samsung.android.game.gos.data.PreferenceHelper r1 = new com.samsung.android.game.gos.data.PreferenceHelper
                r1.<init>()
                int r0 = r1.getValue((java.lang.String) r0, (int) r3)
                java.lang.Integer r1 = java.lang.Integer.valueOf(r0)
            L_0x0052:
                int r0 = r1.intValue()
                r4 = 1
                r0 = r0 & r4
                if (r0 != r4) goto L_0x005c
                r0 = r4
                goto L_0x005d
            L_0x005c:
                r0 = r3
            L_0x005d:
                r5.brightness = r0
                int r0 = r1.intValue()
                r1 = 2
                r0 = r0 & r1
                if (r0 != r1) goto L_0x0069
                r0 = r4
                goto L_0x006a
            L_0x0069:
                r0 = r3
            L_0x006a:
                r5.volume = r0
                boolean r1 = r5.brightness
                if (r1 != 0) goto L_0x0072
                if (r0 == 0) goto L_0x0073
            L_0x0072:
                r3 = r4
            L_0x0073:
                r5.fps = r3
                java.lang.String r0 = com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature.LOG_TAG
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r3 = "update(): brightness="
                r1.append(r3)
                boolean r3 = r5.brightness
                r1.append(r3)
                java.lang.String r3 = ", volume="
                r1.append(r3)
                boolean r3 = r5.volume
                r1.append(r3)
                java.lang.String r3 = ", fps="
                r1.append(r3)
                boolean r3 = r5.fps
                r1.append(r3)
                java.lang.String r1 = r1.toString()
                com.samsung.android.game.gos.util.GosLog.i(r0, r1)
                java.lang.String r0 = "allow_more_heat_value"
                boolean r6 = java.util.Objects.equals(r6, r0)
                if (r6 == 0) goto L_0x00c2
                java.lang.Float r7 = (java.lang.Float) r7     // Catch:{ Exception -> 0x00b6 }
                float r6 = r7.floatValue()     // Catch:{ Exception -> 0x00b6 }
                java.lang.Float r2 = java.lang.Float.valueOf(r6)     // Catch:{ Exception -> 0x00b6 }
                goto L_0x00c2
            L_0x00b6:
                r6 = move-exception
                java.lang.String r7 = com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature.LOG_TAG
                java.lang.String r6 = r6.getLocalizedMessage()
                com.samsung.android.game.gos.util.GosLog.e(r7, r6)
            L_0x00c2:
                if (r2 != 0) goto L_0x00d4
                com.samsung.android.game.gos.data.PreferenceHelper r6 = new com.samsung.android.game.gos.data.PreferenceHelper
                r6.<init>()
                r7 = 0
                float r6 = r6.getValue((java.lang.String) r0, (float) r7)
                r7 = 1092616192(0x41200000, float:10.0)
                float r6 = r6 / r7
                r5.allowedMoreHeatCelsius = r6
                goto L_0x00da
            L_0x00d4:
                float r6 = r2.floatValue()
                r5.allowedMoreHeatCelsius = r6
            L_0x00da:
                java.lang.String r6 = com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature.LOG_TAG
                java.lang.StringBuilder r7 = new java.lang.StringBuilder
                r7.<init>()
                java.lang.String r0 = "update(): allowedMoreHeatCelsius="
                r7.append(r0)
                float r0 = r5.allowedMoreHeatCelsius
                r7.append(r0)
                java.lang.String r7 = r7.toString()
                com.samsung.android.game.gos.util.GosLog.i(r6, r7)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature.ControlSettings.update(java.lang.String, java.lang.Object):void");
        }

        /* access modifiers changed from: private */
        public boolean shouldControlBrightness() {
            return this.brightness;
        }

        /* access modifiers changed from: private */
        public boolean shouldControlVolume() {
            return this.volume;
        }

        /* access modifiers changed from: private */
        public boolean shouldControlFps() {
            return this.fps;
        }

        /* access modifiers changed from: private */
        public boolean shouldControl() {
            return this.brightness || this.volume || this.fps;
        }

        /* access modifiers changed from: private */
        public float getAllowedMoreHeat() {
            return this.allowedMoreHeatCelsius;
        }

        /* access modifiers changed from: private */
        public boolean isTestMode() {
            return (this.testLevel == null && this.testTemperatures == null) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public void setTestLevel(Level level) {
            this.testLevel = level;
        }

        /* access modifiers changed from: private */
        public Level getTestLevel() {
            return this.testLevel;
        }

        /* access modifiers changed from: package-private */
        public void setTestTemperatures(float[] fArr) {
            this.testTemperatures = fArr;
        }

        /* access modifiers changed from: private */
        public float[] getTestTemperatures() {
            return this.testTemperatures;
        }

        public String toString() {
            return "ControlSettings{brightness=" + this.brightness + ", volume=" + this.volume + ", fps=" + this.fps + ", moreHeat=" + this.allowedMoreHeatCelsius + ", pstCheckInt=" + this.pstCheckInterval + ", testLevel=" + this.testLevel + ", testTemp=" + Arrays.toString(this.testTemperatures) + '}';
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final AutoControlFeature INSTANCE = new AutoControlFeature();

        private SingletonHolder() {
        }
    }

    public Level getLevel() {
        return this.mLevel;
    }

    public static JSONObject getAutoControlStatus() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(RinglogConstants.KEY_GPP_STATE_CONTROL_LEVEL, getInstance().getLevel().ordinal());
        } catch (JSONException e) {
            String str = LOG_TAG;
            GosLog.e(str, "getAutoControlStatus " + e);
        }
        return jSONObject;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        SecureSettingHelper.getInstance().unregisterListener(SecureSettingConstants.KEY_AUTO_CONTROL, this);
        SecureSettingHelper.getInstance().unregisterListener(SecureSettingConstants.KEY_ALLOW_MORE_HEAT, this);
        super.finalize();
    }
}
