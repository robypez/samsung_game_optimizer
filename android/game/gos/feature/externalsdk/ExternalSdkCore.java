package com.samsung.android.game.gos.feature.externalsdk;

import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.feature.externalsdk.value.Const;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.json.JSONArray;

public class ExternalSdkCore implements ControlParametersListener {
    private static final String LOG_TAG = "ExternalSdkCore";
    static final int MAX_EVENT_COUNT = 200;
    private static volatile ExternalSdkCore sInstance;
    private List<IControlStrategy> mControl;
    private ControlParameters mControlParams = new ControlParameters(this);
    private String mGamePkgName = null;
    private HashSet<String> mGamePkgNameList = null;
    private boolean mIsForeground = false;
    private IExternalSdkListener mListener = null;
    private List<IReportStrategy> mReport;
    private Const.SdkType mSdkType = Const.SdkType.NONE;
    private ExternalSdkUsage mSdkUsage = null;

    private ExternalSdkCore() {
        ArrayList arrayList = new ArrayList();
        this.mControl = arrayList;
        arrayList.add(new SpaControl());
        this.mControl.add(new SsrmControl());
        ArrayList arrayList2 = new ArrayList();
        this.mReport = arrayList2;
        arrayList2.add(new SsrmReport());
        this.mGamePkgNameList = new HashSet<>();
    }

    public static synchronized ExternalSdkCore getInstance() {
        ExternalSdkCore externalSdkCore;
        synchronized (ExternalSdkCore.class) {
            if (sInstance == null) {
                sInstance = new ExternalSdkCore();
            }
            externalSdkCore = sInstance;
        }
        return externalSdkCore;
    }

    private static void setInstanceNull() {
        sInstance = null;
    }

    public static ExternalSdkCore getInstanceUnsafe() {
        return sInstance;
    }

    public void onFocusOut(PkgData pkgData) {
        GosLog.d(LOG_TAG, "onFocusOut()");
        this.mIsForeground = false;
        for (IReportStrategy stopWatching : this.mReport) {
            stopWatching.stopWatching();
        }
        for (IControlStrategy releaseAll : this.mControl) {
            releaseAll.releaseAll();
        }
    }

    public void onFocusIn(PkgData pkgData) {
        HashSet<String> hashSet = this.mGamePkgNameList;
        if (!(hashSet == null || pkgData == null)) {
            if (hashSet.contains(pkgData.getPackageName())) {
                this.mGamePkgName = pkgData.getPackageName();
                this.mIsForeground = true;
            } else {
                this.mGamePkgName = null;
                this.mIsForeground = false;
            }
        }
        GosLog.d(LOG_TAG, "onFocusIn(). mIsForeground: " + this.mIsForeground);
        if (this.mIsForeground) {
            this.mControlParams.paramsChanged();
            this.mSdkUsage = new ExternalSdkUsage();
            for (IReportStrategy startWatching : this.mReport) {
                startWatching.startWatching();
            }
            return;
        }
        GosLog.w(LOG_TAG, "mIsForeground is false. do nothing.");
        this.mSdkUsage = null;
    }

    public void restoreDefault() {
        GosLog.d(LOG_TAG, "restoreDefault()");
        this.mIsForeground = false;
        this.mGamePkgName = null;
        HashSet<String> hashSet = this.mGamePkgNameList;
        if (hashSet != null) {
            hashSet.clear();
        }
        this.mGamePkgNameList = null;
        for (IReportStrategy next : this.mReport) {
            next.stopWatching();
            next.setListener(0, (IExternalSdkListener) null);
        }
        for (IControlStrategy releaseAll : this.mControl) {
            releaseAll.releaseAll();
        }
        this.mSdkUsage = null;
        this.mListener = null;
        this.mSdkType = Const.SdkType.NONE;
        setInstanceNull();
    }

    public boolean initSdk(Const.SdkType sdkType, String str) {
        SeGameManager instance = SeGameManager.getInstance();
        String foregroundApp = instance.getForegroundApp();
        boolean isForegroundGame = instance.isForegroundGame();
        if (foregroundApp == null || !foregroundApp.equals(str) || !isForegroundGame) {
            GosLog.w(LOG_TAG, "failed to initSdk(). fgApp: " + foregroundApp + ", pkgName: " + str + ", isForegroundGame: " + isForegroundGame);
            this.mIsForeground = false;
            this.mGamePkgName = null;
            return false;
        } else if (isBlockList(PackageDbHelper.getInstance().getPkgData(str))) {
            return false;
        } else {
            return _init(sdkType, str);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isBlockList(PkgData pkgData) {
        if (pkgData != null && pkgData.isPositiveFeature(Constants.V4FeatureFlag.EXTERNAL_SDK) && GlobalDbHelper.getInstance().isPositiveFeatureFlag(Constants.V4FeatureFlag.EXTERNAL_SDK)) {
            return false;
        }
        GosLog.w(LOG_TAG, "gameData == null or EXTERNAL_SDK is not positive feature");
        this.mIsForeground = false;
        this.mGamePkgName = null;
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean _init(Const.SdkType sdkType, String str) {
        this.mIsForeground = true;
        HashSet<String> hashSet = this.mGamePkgNameList;
        if (hashSet != null) {
            hashSet.add(str);
            this.mGamePkgName = str;
        }
        if (sdkType == Const.SdkType.TENCENT_SCENE_SDK) {
            this.mSdkType = Const.SdkType.TENCENT_SCENE_SDK;
            ExternalSdkUsage externalSdkUsage = new ExternalSdkUsage();
            this.mSdkUsage = externalSdkUsage;
            externalSdkUsage.type = this.mSdkType.name();
            logEvent("initSdk()");
            return true;
        } else if (sdkType != Const.SdkType.NETEASE_SCENE_SDK) {
            return false;
        } else {
            this.mSdkType = Const.SdkType.NETEASE_SCENE_SDK;
            ExternalSdkUsage externalSdkUsage2 = new ExternalSdkUsage();
            this.mSdkUsage = externalSdkUsage2;
            externalSdkUsage2.type = this.mSdkType.name();
            logEvent("initSdk()");
            return true;
        }
    }

    public boolean setBoost(int i) {
        if (!this.mIsForeground) {
            GosLog.w(LOG_TAG, "failed to setBoost(). mIsForeground: false, mGamePkgName: " + this.mGamePkgName);
            return false;
        } else if (this.mSdkType == Const.SdkType.NONE) {
            return false;
        } else {
            this.mControlParams.setBoost(true, i);
            return true;
        }
    }

    public boolean setPerformanceLevel(Integer num, int i) {
        if (!this.mIsForeground) {
            GosLog.w(LOG_TAG, "failed to setPerformanceLevel(). mIsForeground: false, mGamePkgName: " + this.mGamePkgName);
            return false;
        } else if (this.mSdkType == Const.SdkType.NONE) {
            return false;
        } else {
            this.mControlParams.setPerformanceLevel(num, i);
            return true;
        }
    }

    public void controlChanged() {
        this.mControlParams.paramsChanged();
    }

    public void controlParamsChanged(boolean z, Integer num, Integer num2, Integer num3) {
        if (this.mIsForeground) {
            boolean z2 = false;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (IControlStrategy next : this.mControl) {
                String simpleName = next.getClass().getSimpleName();
                if (!next.isAvailable() || z2) {
                    next.releaseAll();
                } else {
                    z2 = true;
                    long controlFlags = next.getControlFlags();
                    if (!z) {
                        next.releaseBoost();
                    } else if (Const.ControlFlags.BOOST.isPresent(controlFlags)) {
                        next.setBoost();
                        logEvent(simpleName + ".setBoost()");
                    } else {
                        arrayList.add(next.getClass().getName() + " ignored setBoost");
                        next.releaseBoost();
                    }
                    if (num == null) {
                        next.releasePerformanceLevel();
                    } else if (Const.ControlFlags.PERFORMANCE_LEVEL.isPresent(controlFlags)) {
                        next.setPerformanceLevel(num.intValue());
                        logEvent(simpleName + ".setPerformanceLevel() {\"perfLevel\":" + num + "}");
                    } else {
                        arrayList2.add(next.getClass().getName() + " ignored setPerformanceLevel");
                        next.releasePerformanceLevel();
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                GosLog.d(LOG_TAG, arrayList.toString());
            }
            if (!arrayList2.isEmpty()) {
                GosLog.d(LOG_TAG, arrayList2.toString());
            }
        }
    }

    public boolean setExternalSdkListener(IExternalSdkListener iExternalSdkListener) {
        if (this.mSdkType == Const.SdkType.NONE) {
            GosLog.e(LOG_TAG, "initSdk may not invoked yet. mSdkType: " + this.mSdkType);
            return false;
        } else if (!this.mIsForeground) {
            GosLog.w(LOG_TAG, "failed to setExternalSdkListener(). mIsForeground: false, mGamePkgName: " + this.mGamePkgName);
            return false;
        } else {
            this.mListener = iExternalSdkListener;
            long value = Const.ReportFlags.SIOP_LEVEL.value();
            for (IReportStrategy stopWatching : this.mReport) {
                stopWatching.stopWatching();
            }
            for (IReportStrategy next : this.mReport) {
                if (next.isAvailable()) {
                    value = next.setListener(value, this.mListener);
                }
                if (value == 0) {
                    break;
                }
            }
            for (IReportStrategy startWatching : this.mReport) {
                startWatching.startWatching();
            }
            if (value == 0) {
                return true;
            }
            return false;
        }
    }

    public void logEvent(String str) {
        ExternalSdkUsage externalSdkUsage = this.mSdkUsage;
        if (externalSdkUsage != null && externalSdkUsage.events != null) {
            int length = this.mSdkUsage.events.length();
            if (length < 200) {
                JSONArray jSONArray = this.mSdkUsage.events;
                jSONArray.put(System.currentTimeMillis() + ":" + str);
            } else if (length == 200) {
                JSONArray jSONArray2 = this.mSdkUsage.events;
                jSONArray2.put(System.currentTimeMillis() + ":TOO_MANY_EVENTS");
            } else {
                GosLog.w(LOG_TAG, "after TOO_MANY_EVENTS. Do nothing");
            }
        }
    }

    public ExternalSdkUsage getSdkUsage() {
        return this.mSdkUsage;
    }

    public void resetSdkUsage() {
        this.mSdkUsage = null;
    }

    public String getCurGamePkgName() {
        return this.mGamePkgName;
    }

    public static class ExternalSdkUsage {
        JSONArray events = new JSONArray();
        String type = null;

        public String getType() {
            return this.type;
        }

        public JSONArray getEvents() {
            return this.events;
        }
    }

    public void setMaxFps(int i) {
        for (IControlStrategy maxFps : this.mControl) {
            maxFps.setMaxFps(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void setIsForeground(boolean z) {
        this.mIsForeground = z;
    }

    /* access modifiers changed from: package-private */
    public void setSdkType(Const.SdkType sdkType) {
        this.mSdkType = sdkType;
    }

    public void setSdkUsage(ExternalSdkUsage externalSdkUsage) {
        this.mSdkUsage = externalSdkUsage;
    }

    public static void putThisToIpmCoreAsCallback() {
        ExternalSdkCore instance = getInstance();
        instance.getClass();
        IpmCore.setOnStartStopCallback(new Runnable() {
            public final void run() {
                ExternalSdkCore.this.controlChanged();
            }
        });
    }

    public List<IControlStrategy> getControl() {
        return this.mControl;
    }

    public HashSet<String> getGamePkgNameList() {
        return this.mGamePkgNameList;
    }

    public boolean isForeground() {
        return this.mIsForeground;
    }
}
