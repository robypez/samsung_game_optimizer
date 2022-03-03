package com.samsung.android.game.gos.feature.gfi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.WindowManager;
import com.samsung.android.app.SemMultiWindowManager;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.dfs.DfsFeature;
import com.samsung.android.game.gos.feature.gfi.value.GfiDfsHelper;
import com.samsung.android.game.gos.feature.gfi.value.GfiMaxGameFps;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicyException;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicyHelper;
import com.samsung.android.game.gos.feature.gfi.value.GfiSettings;
import com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper;
import com.samsung.android.game.gos.feature.gfi.value.GfiVersion;
import com.samsung.android.game.gos.feature.gfi.value.GfiWatchdogWarningHandler;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeMediaRouter;
import com.samsung.android.game.gos.selibrary.SeServiceManager;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.SecureSettingConstants;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class GfiFeature implements RuntimeInterface {
    private static final String LOG_TAG = "GfiFeature";
    private static final String MINIMUM_VERSION_FOR_AVAILABILITY = "1.2.3";
    private static final String MINIMUM_VERSION_FOR_KEEP_TWO_HWC_LAYERS = "1.5.22";
    private static final int TRANSACTION_SUBMIT_SETTINGS = 1127;
    private static String mChipsetName;
    private static int mGfiDisabledCode;
    private static GfiFeature mInstance;
    private static IBinder mSurfaceFlinger;
    private GfiRinglogAggregator mAggregator = null;
    public boolean mApplyWatchdogMaxFpsLimit = false;
    private Context mContext = null;
    private Boolean mCurrentSessionExternallyControllable;
    private Boolean mHWOverlaysDisabled;
    private Map<String, Integer> mInGameFPSLimitMap = new HashMap();
    private Boolean mIsAvailable = null;
    private boolean mKeepTwoHwcLayers = false;
    private LogLevel mLogLevel = LogLevel.RELEASE;
    private SemMultiWindowManager mMultiWindowManager;
    private Set<String> mPackagesWithSettings = null;
    private Boolean mShowSurfaceUpdates;
    private Boolean mShowTapsEnabled;
    private ContentObserver mShowTapsObserver;
    private GfiSurfaceFlingerHelper mSurfaceFlingerHelper;
    private Boolean mTemporaryDisabledOSFlag = false;
    private GfiVersion mVersion = null;
    private GfiWatchdogWarningHandler mWarningHandler = null;

    public String getName() {
        return Constants.V4FeatureFlag.GFI;
    }

    public boolean isFrameCorruptionIssueChipset() {
        return false;
    }

    public enum LogLevel {
        RELEASE(0),
        DEBUG(1),
        VERBOSE(2);
        
        public final int level;

        private LogLevel(int i) {
            this.level = i;
        }

        public static LogLevel fromInteger(int i) {
            if (i == 0) {
                return RELEASE;
            }
            if (i == 1) {
                return DEBUG;
            }
            if (i != 2) {
                return RELEASE;
            }
            return VERBOSE;
        }
    }

    public static synchronized GfiFeature getInstance(Context context) {
        GfiFeature gfiFeature;
        synchronized (GfiFeature.class) {
            if (mInstance == null) {
                mInstance = new GfiFeature(context);
            }
            gfiFeature = mInstance;
        }
        return gfiFeature;
    }

    private void startAggregator(PkgData pkgData) {
        if (GfiSettings.getInstance(this.mContext).isSessionRecordingEnabled()) {
            try {
                String str = pkgData.getPkg().pkgName;
                GfiRinglogAggregator gfiRinglogAggregator = new GfiRinglogAggregator(str, getUid(str), this.mVersion);
                this.mAggregator = gfiRinglogAggregator;
                gfiRinglogAggregator.start();
            } catch (PackageManager.NameNotFoundException unused) {
                GosLog.e(LOG_TAG, "onResume: failed to start the ringlog aggregator");
            }
        }
    }

    private void checkInGameFPSLimit(Package packageR) {
        if (packageR == null) {
            GosLog.d(LOG_TAG, "checkInGameFPSLimit : pkg is null");
            return;
        }
        String str = packageR.pkgName;
        if (str.equals(BuildConfig.VERSION_NAME)) {
            GosLog.d(LOG_TAG, "checkInGameFPSLimit : pName is empty");
        } else if (this.mInGameFPSLimitMap.containsKey(str)) {
            Integer num = this.mInGameFPSLimitMap.get(str);
            if (num == null) {
                GosLog.d(LOG_TAG, "checkInGameFPSLimit : inGameFPSLimit is null, delete this value");
                this.mInGameFPSLimitMap.remove(str);
                return;
            }
            GosLog.d(LOG_TAG, "checkInGameFPSLimit : " + str + " has previous inGameFPSLimit value : " + num);
            updateInGameFPSLimit(str, num.intValue());
        }
    }

    private boolean isDfsUnder60(PkgData pkgData) {
        boolean isUsingUserValue = FeatureHelper.isUsingUserValue(Constants.V4FeatureFlag.DFS);
        boolean isUsingPkgValue = FeatureHelper.isUsingPkgValue(Constants.V4FeatureFlag.DFS);
        return Math.round(DfsFeature.getDfsValueByMode(DfsFeature.getActualDfsMode(pkgData, isUsingUserValue, isUsingPkgValue), pkgData, isUsingUserValue, isUsingPkgValue)) < 60;
    }

    private boolean checkMultiWindowScenario() throws RemoteException {
        SemMultiWindowManager semMultiWindowManager = this.mMultiWindowManager;
        if (semMultiWindowManager != null) {
            return semMultiWindowManager.getMode() == 0;
        }
        throw new RemoteException("No instance of SemMultiWindowManager.");
    }

    private boolean isPriorityMode() {
        if (new PreferenceHelper().getValue(SecureSettingConstants.KEY_GAME_BOOSTER_PRIORITY_MODE, 0) != 0) {
            GosLog.d(LOG_TAG, "Priority Mode: ON");
            return true;
        }
        GosLog.d(LOG_TAG, "Priority Mode: OFF");
        return false;
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        GosLog.i(LOG_TAG, "onResume: pkg=" + pkgData.getPackageName());
        boolean z2 = false;
        mGfiDisabledCode = 0;
        this.mCurrentSessionExternallyControllable = false;
        String orCreateGfiPolicy = getOrCreateGfiPolicy(pkgData.getPkg());
        if (orCreateGfiPolicy == null) {
            GosLog.d(LOG_TAG, "GfiPolicy is Null");
            mGfiDisabledCode = 1;
        } else if (!shouldEnable(pkgData.getPkg(), orCreateGfiPolicy)) {
            GosLog.d(LOG_TAG, "Interpolation should not be enabled from shouldEnable() check");
            stopInterpolation(pkgData.getPkg());
        } else {
            try {
                if (!checkMultiWindowScenario()) {
                    GosLog.d(LOG_TAG, "MultiWindow active. Disable FB");
                    mGfiDisabledCode = 4;
                    stopInterpolation(pkgData.getPkg());
                } else if (isDfsUnder60(pkgData)) {
                    GosLog.d(LOG_TAG, "DFS value is set under 60. Disable FB");
                    mGfiDisabledCode = 6;
                    stopInterpolation(pkgData.getPkg());
                } else if (SeMediaRouter.getInstance().hasRemotePresentationDisplay()) {
                    GosLog.d(LOG_TAG, "RemoteView is active. Disable FB");
                    mGfiDisabledCode = 7;
                    stopInterpolation(pkgData.getPkg());
                } else {
                    try {
                        if (isGFIAvailable() && GfiPolicy.isExternallControllable(new JSONObject(orCreateGfiPolicy))) {
                            z2 = true;
                        }
                        this.mCurrentSessionExternallyControllable = Boolean.valueOf(z2);
                    } catch (JSONException e) {
                        GosLog.e(LOG_TAG, "onResume: failed to check the current sessions controllability " + e);
                    }
                    setFrameInterpolation(pkgData.getPkg(), orCreateGfiPolicy);
                    startAggregator(pkgData);
                    checkInGameFPSLimit(pkgData.getPkg());
                }
            } catch (RemoteException unused) {
                GosLog.d(LOG_TAG, "MultiWindow status unknown. Disable for safety");
                mGfiDisabledCode = 5;
                stopInterpolation(pkgData.getPkg());
            }
        }
    }

    public void onFocusOut(PkgData pkgData) {
        GosLog.i(LOG_TAG, "onPause: pkg=" + pkgData.getPackageName() + ", Disabled Code = " + mGfiDisabledCode);
        this.mCurrentSessionExternallyControllable = false;
        if (pkgData != null) {
            stopInterpolation(pkgData.getPkg());
        } else {
            GosLog.d(LOG_TAG, "onPause : pkgData or pkgData.getPkg() is null");
        }
        stopAllInterpolation();
        GfiRinglogAggregator gfiRinglogAggregator = this.mAggregator;
        if (gfiRinglogAggregator != null) {
            gfiRinglogAggregator.stop();
            this.mAggregator = null;
        }
    }

    private boolean shouldEnable(Package packageR, String str) {
        boolean z = false;
        try {
            checkSFDebugOptions();
            if (!this.mWarningHandler.shouldEnableFB(getUid(packageR.pkgName))) {
                mGfiDisabledCode = 15;
            } else if (this.mHWOverlaysDisabled.booleanValue()) {
                GosLog.d(LOG_TAG, "Developer options [HW Overay Disabled] enabled. Disabling FB");
                mGfiDisabledCode = 8;
            } else if (this.mShowSurfaceUpdates.booleanValue()) {
                GosLog.d(LOG_TAG, "Developer options [Show Surface Updates] enabled. Disabling FB");
                mGfiDisabledCode = 9;
            } else if (this.mShowTapsEnabled.booleanValue()) {
                GosLog.d(LOG_TAG, "Developer options [Show Taps Enabled] enabled. Disabling FB");
                mGfiDisabledCode = 10;
            } else if (!IpmCore.getInstance(this.mContext).isRunning()) {
                mGfiDisabledCode = 12;
            }
            if (mGfiDisabledCode > 0) {
                return false;
            }
            if (!this.mTemporaryDisabledOSFlag.booleanValue() || !isTemporaryDisabledOS()) {
                if ("z3s".equals(AppVariable.getOriginalDeviceName())) {
                    boolean isHdResolution = isHdResolution();
                    if (isHdResolution) {
                        GosLog.d(LOG_TAG, "Disabling FB on Z3S at HD resolution to avoid composition issues.");
                        mGfiDisabledCode = 100;
                        return false;
                    }
                    GosLog.d(LOG_TAG, "NOT disabling FB (model name = " + AppVariable.getOriginalModelName() + ", HD = " + isHdResolution + ")");
                }
                JSONObject jSONObject = new JSONObject(str);
                if (GfiPolicy.isLowLatencyPolicy(jSONObject)) {
                    return true;
                }
                boolean isHighFrameRatePolicy = GfiPolicy.isHighFrameRatePolicy(jSONObject);
                boolean z2 = GfiMaxGameFps.getMaxGameFps(packageR) > 60;
                if (z2 || isHighFrameRatePolicy == z2) {
                    z = true;
                }
                if (!z) {
                    GosLog.d(LOG_TAG, "Target frame rate of policy does not match the settings of the device.");
                    mGfiDisabledCode = 13;
                }
                return z;
            }
            GosLog.d(LOG_TAG, "Temporary FB Disabled OS. Disabling FB");
            mGfiDisabledCode = 101;
            return false;
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "Exception during shouldEnable: " + e);
        }
    }

    public void restoreDefault(PkgData pkgData) {
        GosLog.d(LOG_TAG, "restoreDefault");
        mGfiDisabledCode = 16;
        stopAllInterpolation();
    }

    public boolean isAvailableForSystemHelper() {
        Boolean bool = this.mIsAvailable;
        if (bool != null) {
            return bool.booleanValue();
        }
        this.mIsAvailable = Boolean.valueOf(isGFIAvailable());
        GosLog.d(LOG_TAG, "isAvailable :" + this.mIsAvailable);
        if (!this.mIsAvailable.booleanValue()) {
            DbHelper.getInstance().getGlobalFeatureFlagDao().setEnabledFlagByUser(new GlobalFeatureFlag.NameAndEnabledFlagByUser(Constants.V4FeatureFlag.GFI, false));
            GlobalDbHelper.getInstance().setAvailable(Constants.V4FeatureFlag.GFI, false);
        }
        return this.mIsAvailable.booleanValue();
    }

    private void checkSFDebugOptions() {
        GfiSurfaceFlingerHelper.SurfaceFlingerConfig interrogateSF = this.mSurfaceFlingerHelper.interrogateSF();
        this.mHWOverlaysDisabled = Boolean.valueOf(interrogateSF.hwOverlaysDisabled);
        this.mShowSurfaceUpdates = Boolean.valueOf(interrogateSF.showSurfaceUpdates);
    }

    /* access modifiers changed from: private */
    public void checkShowTapsEnabled() {
        boolean z = false;
        this.mShowTapsEnabled = false;
        try {
            if (Settings.System.getInt(this.mContext.getContentResolver(), "show_touches") == 1) {
                z = true;
            }
            this.mShowTapsEnabled = Boolean.valueOf(z);
            StringBuilder sb = new StringBuilder();
            sb.append("GfiFeature: Show Taps is ");
            sb.append(this.mShowTapsEnabled.booleanValue() ? "enabled" : State.DISABLED);
            GosLog.d(LOG_TAG, sb.toString());
        } catch (Settings.SettingNotFoundException unused) {
            GosLog.d(LOG_TAG, "GfiFeature: Show Taps setting does not exist. Was never used");
        }
    }

    private String getChipsetName() {
        String str = mChipsetName;
        if (str != null) {
            return str;
        }
        String prop = SeSysProp.getProp("ro.hardware.chipname");
        mChipsetName = prop;
        return prop;
    }

    private boolean isFlagshipChipset() {
        return getChipsetName().equals("exynos990") || getChipsetName().equals("SM8250");
    }

    public boolean isFlickeringIssueChipset() {
        return getChipsetName().equals("SM8250") || getChipsetName().equals("SM8350");
    }

    public boolean isTemporaryDisabledOS() {
        return Build.VERSION.SDK_INT > 30;
    }

    private boolean isHdResolution() {
        Point point = new Point();
        ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRealSize(point);
        return (point.x <= 1600 && point.y <= 720) || (point.x <= 720 && point.y <= 1600);
    }

    private GfiFeature(Context context) {
        this.mContext = context;
        checkShowTapsEnabled();
        Uri uriFor = Settings.System.getUriFor("show_touches");
        this.mShowTapsObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public boolean deliverSelfNotifications() {
                return true;
            }

            public void onChange(boolean z) {
                GfiFeature.this.checkShowTapsEnabled();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(uriFor, false, this.mShowTapsObserver);
        this.mMultiWindowManager = new SemMultiWindowManager();
        this.mSurfaceFlingerHelper = new GfiSurfaceFlingerHelper(getSurfaceFlinger());
        this.mPackagesWithSettings = new HashSet();
        this.mWarningHandler = new GfiWatchdogWarningHandler();
    }

    public void finalize() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mShowTapsObserver);
    }

    public LogLevel getLogLevel() {
        return this.mLogLevel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setLogLevel(com.samsung.android.game.gos.feature.gfi.GfiFeature.LogLevel r7) {
        /*
            r6 = this;
            boolean r0 = r6.isGFIAvailable()
            java.lang.String r1 = "GfiFeature"
            if (r0 != 0) goto L_0x0030
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "setLogLevel: GFI version ("
            r7.append(r0)
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r0 = r6.getVersion()
            r7.append(r0)
            java.lang.String r0 = ") lower than global minimum ("
            r7.append(r0)
            java.lang.String r0 = "1.2.3"
            r7.append(r0)
            java.lang.String r0 = ")"
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            com.samsung.android.game.gos.util.GosLog.d(r1, r7)
            return
        L_0x0030:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "setLogLevel "
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.d(r1, r0)
            r0 = 0
            android.os.Parcel r2 = android.os.Parcel.obtain()     // Catch:{ RemoteException | SecurityException -> 0x007b }
            java.lang.String r3 = "android.ui.ISurfaceComposer"
            r2.writeInterfaceToken(r3)     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            r3 = 0
            r2.writeInt(r3)     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            r4 = 1
            r2.writeInt(r4)     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            r4 = 100
            r2.writeInt(r4)     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            int r4 = r7.level     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            r2.writeInt(r4)     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            android.os.IBinder r4 = getSurfaceFlinger()     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            r5 = 1127(0x467, float:1.579E-42)
            boolean r0 = r4.transact(r5, r2, r0, r3)     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
            if (r0 == 0) goto L_0x006e
            r6.mLogLevel = r7     // Catch:{ RemoteException | SecurityException -> 0x0077, all -> 0x0074 }
        L_0x006e:
            if (r2 == 0) goto L_0x0085
            r2.recycle()
            goto L_0x0085
        L_0x0074:
            r7 = move-exception
            r0 = r2
            goto L_0x0086
        L_0x0077:
            r0 = r2
            goto L_0x007b
        L_0x0079:
            r7 = move-exception
            goto L_0x0086
        L_0x007b:
            java.lang.String r7 = "setLogLevel: failed to set loglevel"
            com.samsung.android.game.gos.util.GosLog.d(r1, r7)     // Catch:{ all -> 0x0079 }
            if (r0 == 0) goto L_0x0085
            r0.recycle()
        L_0x0085:
            return
        L_0x0086:
            if (r0 == 0) goto L_0x008b
            r0.recycle()
        L_0x008b:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.GfiFeature.setLogLevel(com.samsung.android.game.gos.feature.gfi.GfiFeature$LogLevel):void");
    }

    public boolean getKeepTwoHwcLayers() {
        return this.mKeepTwoHwcLayers;
    }

    public void setKeepTwoHwcLayers(boolean z) {
        if (!getVersion().higherOrEqualThan(MINIMUM_VERSION_FOR_KEEP_TWO_HWC_LAYERS)) {
            GosLog.d(LOG_TAG, "setKeepTwoHwcLayers: GFI version (" + getVersion() + ") lower than minimum for this feature (" + MINIMUM_VERSION_FOR_KEEP_TWO_HWC_LAYERS + ")");
        } else if (this.mSurfaceFlingerHelper.setKeepTwoHwcLayers(z)) {
            this.mKeepTwoHwcLayers = z;
        }
    }

    public GfiVersion getVersion() {
        if (this.mVersion == null) {
            this.mVersion = GfiVersion.getVersion();
        }
        return this.mVersion;
    }

    protected static synchronized IBinder getSurfaceFlinger() {
        IBinder iBinder;
        synchronized (GfiFeature.class) {
            if (mSurfaceFlinger == null) {
                mSurfaceFlinger = SeServiceManager.getInstance().getService("SurfaceFlinger");
            }
            iBinder = mSurfaceFlinger;
        }
        return iBinder;
    }

    public String getVersionString() {
        return getVersion().getVersionString();
    }

    private int getUid(String str) throws PackageManager.NameNotFoundException {
        if (str == null) {
            return 0;
        }
        return this.mContext.getPackageManager().getApplicationInfo(str, 0).uid;
    }

    private boolean isGFIAvailable() {
        return getVersion().higherOrEqualThan(MINIMUM_VERSION_FOR_AVAILABILITY);
    }

    private boolean setFrameInterpolation(Package packageR, String str) {
        if (!isGFIAvailable()) {
            GosLog.d(LOG_TAG, "setFrameInterpolation: GFI version (" + getVersion() + ") lower than global minimum (" + MINIMUM_VERSION_FOR_AVAILABILITY + ")");
            return true;
        } else if (packageR == null) {
            GosLog.d(LOG_TAG, "setFrameInterpolation: No PkgData for interpolation");
            return false;
        } else {
            String str2 = packageR.pkgName;
            GosLog.d(LOG_TAG, "setFrameInterpolation: using GFI version " + getVersion() + ", pkgName: " + str2);
            try {
                int uid = getUid(str2);
                if (!isEffectivelyEmpty(packageR.gfiPolicy)) {
                    JSONObject jSONObject = new JSONObject(str);
                    if (!GfiPolicy.isEnabled(jSONObject)) {
                        new GfiPolicyHelper().applyPriorityModeCheck(jSONObject, isPriorityMode());
                        str = jSONObject.toString();
                    }
                }
                GosLog.d(LOG_TAG, "setFrameInterpolation: final policy = " + str);
                return applyInterpolation(str2, uid, str);
            } catch (PackageManager.NameNotFoundException | GfiPolicyException | JSONException e) {
                GosLog.e(LOG_TAG, "setFrameInterpolation failed with exception: " + e);
                return false;
            }
        }
    }

    public boolean updateInGameFPSLimit(String str, int i) {
        this.mInGameFPSLimitMap.put(str, Integer.valueOf(i));
        GosLog.d(LOG_TAG, "updateInGameFPSLimit: " + str + " store inGameFPSLimit " + i);
        if (!isGFIAvailable()) {
            GosLog.d(LOG_TAG, "updateInGameFPSLimit: GFI version (" + getVersion() + ") lower than global minimum (" + MINIMUM_VERSION_FOR_AVAILABILITY + ")");
            return false;
        }
        String str2 = DbHelper.getInstance().getPackageDao().getPackage(str).gfiPolicy;
        if (str2 == null) {
            GosLog.d(LOG_TAG, "updateInGameFPSLimit: No PkgData or Gfi policy");
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str2);
            int uid = getUid(str);
            if (!GfiPolicy.isEnabled(jSONObject)) {
                GosLog.d(LOG_TAG, "updateInGameFPSLimit: FB not enabled for this game. Nothing to do.");
                return false;
            } else if (GfiPolicy.isGFPSOffsetEnabled(jSONObject) || ((int) GfiPolicy.getRFPS(jSONObject)) == i) {
                return setInterpolation(str, uid, GfiPolicy.setRFPS(GfiPolicy.disableDFSOffset(GfiPolicy.setEnabledKey(new JSONObject(), true)), (double) i).toString());
            } else {
                GosLog.d(LOG_TAG, "updateInGameFPSLimit: GFPS Offset not enabled and inGameFPSLimit " + i + " != RFPS " + ((int) GfiPolicy.getRFPS(jSONObject)) + ", Disable FB.");
                mGfiDisabledCode = 14;
                return stopInterpolation(str, uid);
            }
        } catch (PackageManager.NameNotFoundException | GfiPolicyException | JSONException e) {
            GosLog.d(LOG_TAG, "updateInGameFPSLimit failed with exception: " + e);
        }
    }

    private boolean isEffectivelyEmpty(String str) throws JSONException {
        if (str == null || str.equals(BuildConfig.VERSION_NAME)) {
            return true;
        }
        JSONObject jSONObject = new JSONObject(str);
        if (jSONObject.length() == 0) {
            return true;
        }
        if (jSONObject.has("enabled")) {
            return false;
        }
        if (jSONObject.has(GfiPolicy.KEY_DFS_OFFSET)) {
            JSONObject jSONObject2 = jSONObject.getJSONObject(GfiPolicy.KEY_DFS_OFFSET);
            if (jSONObject2.has("enabled") && !jSONObject2.getBoolean("enabled")) {
                jSONObject2.remove("enabled");
            }
            if (jSONObject2.length() == 0) {
                jSONObject.remove(GfiPolicy.KEY_DFS_OFFSET);
            }
        }
        if (jSONObject.has(GfiPolicy.KEY_GFPS_OFFSET)) {
            JSONObject jSONObject3 = jSONObject.getJSONObject(GfiPolicy.KEY_GFPS_OFFSET);
            if (jSONObject3.has("enabled") && !jSONObject3.getBoolean("enabled")) {
                jSONObject3.remove("enabled");
            }
            if (jSONObject3.length() == 0) {
                jSONObject.remove(GfiPolicy.KEY_GFPS_OFFSET);
            }
        }
        if (jSONObject.length() == 0) {
            return true;
        }
        return false;
    }

    private String getOrCreateGfiPolicy(Package packageR) {
        boolean z;
        String str = packageR.gfiPolicy;
        try {
            z = isEffectivelyEmpty(str);
        } catch (JSONException e) {
            GosLog.e(LOG_TAG, "JSONException during isEffectivelyEmpty: " + e);
            z = false;
        }
        try {
            GfiPolicyHelper gfiPolicyHelper = new GfiPolicyHelper();
            if (z) {
                GosLog.d(LOG_TAG, "getOrCreateGfiPolicy: original policy = " + str);
                JSONObject jSONObject = new JSONObject();
                gfiPolicyHelper.retrieveDefaultGFIPolicy(GfiMaxGameFps.getMaxGameFps(packageR), jSONObject);
                String gfiPolicy = DbHelper.getInstance().getGlobalDao().getGfiPolicy();
                if (gfiPolicy != null) {
                    gfiPolicyHelper.applyGlobalPolicyforCreate(gfiPolicy, jSONObject, GfiPolicy.KEY_MINIMUM_VERSION);
                    gfiPolicyHelper.applyGlobalPolicyforCreate(gfiPolicy, jSONObject, GfiPolicy.KEY_MAXIMUM_VERSION);
                }
                str = jSONObject.toString();
                GosLog.d(LOG_TAG, "getOrCreateGfiPolicy: created policy = " + str);
            }
            if (!isFlickeringIssueChipset()) {
                return str;
            }
            if (!packageR.getPkgName().equals("com.netmarble.lineageII") && !packageR.getPkgName().equals("com.netmarble.revolutionjp") && !packageR.getPkgName().equals("com.netmarble.revolutionthm") && !packageR.getPkgName().equals("com.netmarble.lin2ws")) {
                return str;
            }
            JSONObject jSONObject2 = new JSONObject(str);
            gfiPolicyHelper.applyFlickeringFix(jSONObject2);
            return jSONObject2.toString();
        } catch (JSONException e2) {
            GosLog.e(LOG_TAG, "JSONException during GfiPolicyHelper function: " + e2);
            return "{ \"enabled\" : false }";
        }
    }

    public boolean setInterpolation(String str, int i, String str2) throws GfiPolicyException {
        if (this.mCurrentSessionExternallyControllable.booleanValue()) {
            return applyInterpolation(str, i, str2);
        }
        GosLog.d(LOG_TAG, "setInterpolation: GFI is not externally controllable for this session");
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v1, resolved type: android.os.Parcel} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v5, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v8, resolved type: android.os.Parcel} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v11, resolved type: android.os.Parcel} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v20, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v21, resolved type: android.os.Parcel} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v22, resolved type: android.os.Parcel} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0158  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x015d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean applyInterpolation(java.lang.String r11, int r12, java.lang.String r13) throws com.samsung.android.game.gos.feature.gfi.value.GfiPolicyException {
        /*
            r10 = this;
            java.lang.String r0 = "gfi_maximum_version"
            java.lang.String r1 = "gfi_minimum_version"
            java.lang.String r2 = "GfiFeature"
            if (r13 == 0) goto L_0x0161
            r3 = 0
            r4 = 0
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r5.<init>(r13)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            boolean r13 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.isEnabled(r5)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            if (r13 != 0) goto L_0x001a
            boolean r11 = r10.stopInterpolation(r11, r12)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            return r11
        L_0x001a:
            boolean r13 = r5.has(r1)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r6 = 1
            java.lang.String r7 = ") in policy"
            java.lang.String r8 = "applyInterpolation: GFI version ("
            if (r13 == 0) goto L_0x005c
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r13 = r10.getVersion()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r9 = r5.getString(r1)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            boolean r13 = r13.higherOrEqualThan(r9)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            if (r13 != 0) goto L_0x005c
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.<init>()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r8)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r12 = r10.getVersion()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r12)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r12 = ") is lower than minimum version ("
            r11.append(r12)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r12 = r5.getString(r1)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r12)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r7)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r11 = r11.toString()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            com.samsung.android.game.gos.util.GosLog.d(r2, r11)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11 = 2
            mGfiDisabledCode = r11     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            return r6
        L_0x005c:
            boolean r13 = r5.has(r0)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            if (r13 == 0) goto L_0x009a
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r13 = r10.getVersion()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r1 = r5.getString(r0)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            boolean r13 = r13.lowerOrEqualThan(r1)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            if (r13 != 0) goto L_0x009a
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.<init>()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r8)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r12 = r10.getVersion()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r12)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r12 = ") is higher than maximum version ("
            r11.append(r12)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r12 = r5.getString(r0)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r12)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11.append(r7)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            java.lang.String r11 = r11.toString()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            com.samsung.android.game.gos.util.GosLog.d(r2, r11)     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            r11 = 17
            mGfiDisabledCode = r11     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            return r6
        L_0x009a:
            android.os.Parcel r13 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x0121, NullPointerException -> 0x011f, SecurityException -> 0x011d, JSONException -> 0x0111, all -> 0x010e }
            android.os.Parcel r4 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            boolean r0 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.setPolicyToParcel(r12, r5, r13)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            if (r0 == 0) goto L_0x00bc
            boolean r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.hasCustomDfs(r5)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            if (r1 == 0) goto L_0x00bc
            boolean r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.isLatencyReductionEnabled(r5)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            if (r1 != 0) goto L_0x00bc
            float r0 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.getRFPS(r5)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            com.samsung.android.game.gos.feature.gfi.value.GfiDfsHelper.pushDfs(r11, r0)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            goto L_0x00c1
        L_0x00bc:
            if (r0 != 0) goto L_0x00c1
            com.samsung.android.game.gos.feature.gfi.value.GfiDfsHelper.popDfs(r11)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
        L_0x00c1:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            r0.<init>()     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            java.lang.String r1 = "setting interpolation for uid "
            r0.append(r1)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            r0.append(r12)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            java.lang.String r0 = r0.toString()     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            com.samsung.android.game.gos.util.GosLog.d(r2, r0)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            android.os.IBinder r0 = getSurfaceFlinger()     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            r1 = 1127(0x467, float:1.579E-42)
            boolean r3 = r0.transact(r1, r13, r4, r3)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            if (r3 == 0) goto L_0x00f6
            com.samsung.android.game.gos.feature.gfi.value.GfiWatchdogWarningHandler r0 = r10.mWarningHandler     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            r0.readWarningsFromParcel(r12, r4)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            java.util.Set<java.lang.String> r12 = r10.mPackagesWithSettings     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            r12.add(r11)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            if (r13 == 0) goto L_0x00f0
            r13.recycle()
        L_0x00f0:
            if (r4 == 0) goto L_0x0141
            r4.recycle()
            goto L_0x0141
        L_0x00f6:
            android.os.RemoteException r11 = new android.os.RemoteException     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            java.lang.String r12 = "Could not enable interpolation in SurfaceFlinger"
            r11.<init>(r12)     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
            throw r11     // Catch:{ RemoteException -> 0x010a, NullPointerException -> 0x0108, SecurityException -> 0x0106, JSONException -> 0x0102, all -> 0x00fe }
        L_0x00fe:
            r11 = move-exception
            r12 = r4
            r4 = r13
            goto L_0x0156
        L_0x0102:
            r11 = move-exception
            r12 = r4
            r4 = r13
            goto L_0x0113
        L_0x0106:
            r11 = move-exception
            goto L_0x010b
        L_0x0108:
            r11 = move-exception
            goto L_0x010b
        L_0x010a:
            r11 = move-exception
        L_0x010b:
            r12 = r4
            r4 = r13
            goto L_0x0123
        L_0x010e:
            r11 = move-exception
            r12 = r4
            goto L_0x0156
        L_0x0111:
            r11 = move-exception
            r12 = r4
        L_0x0113:
            com.samsung.android.game.gos.feature.gfi.value.GfiPolicyException r13 = new com.samsung.android.game.gos.feature.gfi.value.GfiPolicyException     // Catch:{ all -> 0x011b }
            java.lang.String r0 = "Malformed JSON"
            r13.<init>(r0, r11)     // Catch:{ all -> 0x011b }
            throw r13     // Catch:{ all -> 0x011b }
        L_0x011b:
            r11 = move-exception
            goto L_0x0156
        L_0x011d:
            r11 = move-exception
            goto L_0x0122
        L_0x011f:
            r11 = move-exception
            goto L_0x0122
        L_0x0121:
            r11 = move-exception
        L_0x0122:
            r12 = r4
        L_0x0123:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x011b }
            r13.<init>()     // Catch:{ all -> 0x011b }
            java.lang.String r0 = "applyInterpolation failed with exception: "
            r13.append(r0)     // Catch:{ all -> 0x011b }
            r13.append(r11)     // Catch:{ all -> 0x011b }
            java.lang.String r11 = r13.toString()     // Catch:{ all -> 0x011b }
            com.samsung.android.game.gos.util.GosLog.e(r2, r11)     // Catch:{ all -> 0x011b }
            if (r4 == 0) goto L_0x013c
            r4.recycle()
        L_0x013c:
            if (r12 == 0) goto L_0x0141
            r12.recycle()
        L_0x0141:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "applyInterpolation: end, success: "
            r11.append(r12)
            r11.append(r3)
            java.lang.String r11 = r11.toString()
            com.samsung.android.game.gos.util.GosLog.d(r2, r11)
            return r3
        L_0x0156:
            if (r4 == 0) goto L_0x015b
            r4.recycle()
        L_0x015b:
            if (r12 == 0) goto L_0x0160
            r12.recycle()
        L_0x0160:
            throw r11
        L_0x0161:
            java.lang.String r13 = "applyInterpolation: No GfiPolicy for interpolation"
            com.samsung.android.game.gos.util.GosLog.d(r2, r13)
            boolean r11 = r10.stopInterpolation(r11, r12)
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.GfiFeature.applyInterpolation(java.lang.String, int, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00bb  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean stopInterpolation(java.lang.String r6, int r7) {
        /*
            r5 = this;
            int r0 = mGfiDisabledCode
            java.lang.String r1 = "GfiFeature"
            if (r0 <= 0) goto L_0x001c
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "stopInterpolation: Disabled Code = "
            r0.append(r2)
            int r2 = mGfiDisabledCode
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r1, r0)
        L_0x001c:
            com.samsung.android.game.gos.feature.gfi.value.GfiDfsHelper.popDfs(r6)
            java.util.Set<java.lang.String> r0 = r5.mPackagesWithSettings
            r0.remove(r6)
            boolean r0 = r5.isGFIAvailable()
            if (r0 != 0) goto L_0x0053
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "stopInterpolation: GFI version ("
            r6.append(r7)
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r7 = r5.getVersion()
            r6.append(r7)
            java.lang.String r7 = ") lower than global minimum ("
            r6.append(r7)
            java.lang.String r7 = "1.2.3"
            r6.append(r7)
            java.lang.String r7 = ")"
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            com.samsung.android.game.gos.util.GosLog.d(r1, r6)
            r6 = 1
            return r6
        L_0x0053:
            r0 = 0
            r2 = 0
            android.os.Parcel r3 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x0097, NullPointerException -> 0x0095, SecurityException -> 0x0093, all -> 0x0090 }
            android.os.Parcel r2 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.setStopParcel(r6, r7, r3)     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            android.os.IBinder r6 = getSurfaceFlinger()     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            r4 = 1127(0x467, float:1.579E-42)
            boolean r0 = r6.transact(r4, r3, r2, r0)     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            if (r0 == 0) goto L_0x007c
            com.samsung.android.game.gos.feature.gfi.value.GfiWatchdogWarningHandler r6 = r5.mWarningHandler     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            r6.readWarningsFromParcel(r7, r2)     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            if (r3 == 0) goto L_0x0076
            r3.recycle()
        L_0x0076:
            if (r2 == 0) goto L_0x00b7
            r2.recycle()
            goto L_0x00b7
        L_0x007c:
            android.os.RemoteException r6 = new android.os.RemoteException     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            java.lang.String r7 = "Could not disable interpolation in SurfaceFlinger"
            r6.<init>(r7)     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
            throw r6     // Catch:{ RemoteException -> 0x008c, NullPointerException -> 0x008a, SecurityException -> 0x0088, all -> 0x0084 }
        L_0x0084:
            r6 = move-exception
            r7 = r2
            r2 = r3
            goto L_0x00b9
        L_0x0088:
            r6 = move-exception
            goto L_0x008d
        L_0x008a:
            r6 = move-exception
            goto L_0x008d
        L_0x008c:
            r6 = move-exception
        L_0x008d:
            r7 = r2
            r2 = r3
            goto L_0x0099
        L_0x0090:
            r6 = move-exception
            r7 = r2
            goto L_0x00b9
        L_0x0093:
            r6 = move-exception
            goto L_0x0098
        L_0x0095:
            r6 = move-exception
            goto L_0x0098
        L_0x0097:
            r6 = move-exception
        L_0x0098:
            r7 = r2
        L_0x0099:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b8 }
            r3.<init>()     // Catch:{ all -> 0x00b8 }
            java.lang.String r4 = "stopInterpolation failed with exception: "
            r3.append(r4)     // Catch:{ all -> 0x00b8 }
            r3.append(r6)     // Catch:{ all -> 0x00b8 }
            java.lang.String r6 = r3.toString()     // Catch:{ all -> 0x00b8 }
            com.samsung.android.game.gos.util.GosLog.e(r1, r6)     // Catch:{ all -> 0x00b8 }
            if (r2 == 0) goto L_0x00b2
            r2.recycle()
        L_0x00b2:
            if (r7 == 0) goto L_0x00b7
            r7.recycle()
        L_0x00b7:
            return r0
        L_0x00b8:
            r6 = move-exception
        L_0x00b9:
            if (r2 == 0) goto L_0x00be
            r2.recycle()
        L_0x00be:
            if (r7 == 0) goto L_0x00c3
            r7.recycle()
        L_0x00c3:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.GfiFeature.stopInterpolation(java.lang.String, int):boolean");
    }

    public boolean stopInterpolation(Package packageR) {
        if (packageR == null) {
            GosLog.d(LOG_TAG, "stopInterpolation: No PkgData");
            return false;
        }
        String str = packageR.pkgName;
        GfiDfsHelper.popDfs(str);
        if (!isGFIAvailable()) {
            GosLog.d(LOG_TAG, "stopInterpolation: GFI version (" + getVersion() + ") lower than global minimum (" + MINIMUM_VERSION_FOR_AVAILABILITY + ")");
            return true;
        }
        GosLog.d(LOG_TAG, "stopInterpolation: using GFI version " + getVersion());
        try {
            return stopInterpolation(str, getUid(str));
        } catch (PackageManager.NameNotFoundException e) {
            GosLog.e(LOG_TAG, "stopInterpolation failed with exception: " + e);
            return false;
        }
    }

    public void stopAllInterpolation() {
        for (String str : new HashSet(this.mPackagesWithSettings)) {
            try {
                stopInterpolation(str, getUid(str));
            } catch (PackageManager.NameNotFoundException unused) {
                GosLog.w(LOG_TAG, "Couldn't find UID for package " + str);
            }
        }
    }

    public void finalizeUTC() {
        finalize();
        this.mIsAvailable = null;
        mChipsetName = null;
        mSurfaceFlinger = null;
        mInstance = null;
        mGfiDisabledCode = 0;
        this.mPackagesWithSettings.clear();
    }

    public boolean setFrameInterpolationForTest(Package packageR) {
        if (packageR == null) {
            return setFrameInterpolation(packageR, BuildConfig.VERSION_NAME);
        }
        return setFrameInterpolation(packageR, packageR.gfiPolicy);
    }

    public String getOrCreateGfiPolicyTest(Package packageR) {
        return getOrCreateGfiPolicy(packageR);
    }
}
