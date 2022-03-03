package com.samsung.android.game.gos.feature.ipm;

import android.content.Context;
import android.os.Build;
import android.os.SemSystemProperties;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.ipm.Policy;
import com.samsung.android.game.gos.selibrary.SeDexManager;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.SecureSettingConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class IpmFeature implements RuntimeInterface {
    private static final String LOG_TAG = "IpmFeature";
    private static IpmFeature mInstance = null;
    public static boolean mUseJSONPolicy = true;
    private Context mContext = null;
    private boolean mIsStarted;
    private Policy mRestoreCfg = null;

    public String getName() {
        return "ipm";
    }

    public void restoreDefault(PkgData pkgData) {
    }

    public static synchronized IpmFeature getInstance(Context context) {
        IpmFeature ipmFeature;
        synchronized (IpmFeature.class) {
            if (mInstance == null) {
                mInstance = new IpmFeature(context);
            }
            ipmFeature = mInstance;
        }
        return ipmFeature;
    }

    private IpmFeature(Context context) {
        this.mContext = context;
    }

    public boolean isAvailableForSystemHelper() {
        if (!"1".equals(SeSysProp.getProp("dev.ssrm.init"))) {
            return true;
        }
        boolean canRun = IpmCore.getInstance(this.mContext).canRun();
        GosLog.d(LOG_TAG, "isAvailableFeatureFlag :" + canRun);
        return canRun;
    }

    public synchronized void onFocusIn(PkgData pkgData, boolean z) {
        if (pkgData != null) {
            if (SeDexManager.getInstance().isDexEnabled()) {
                GosLog.i(LOG_TAG, "onResume. Not starting SPA due to DeX mode");
            } else if (this.mIsStarted) {
                GosLog.i(LOG_TAG, "onResume called without corresponding onPause");
            } else {
                boolean z2 = true;
                this.mIsStarted = true;
                GosLog.v(LOG_TAG, "onResume. " + pkgData.getPackageName());
                IpmCore.getInstance(this.mContext).resetParametersUsed();
                addJsonPolicy(pkgData.getPkg());
                if (checkLaunchConditions(pkgData)) {
                    z2 = TypeConverter.csvToBooleans(DbHelper.getInstance().getGlobalDao().getIpmFlag())[4];
                }
                IpmCore.getInstance(this.mContext).start(pkgData, z2);
            }
        }
    }

    public void addJsonPolicy(Package packageR) {
        IpmCore instance = IpmCore.getInstance(this.mContext);
        instance.addJson("GlobalGfiPolicy", String.valueOf(packageR.getGfiPolicy()));
        instance.addJson("GovernorSettings", String.valueOf(packageR.getGovernorSettings()));
        instance.addJson("SosPolicy", String.valueOf(packageR.getSosPolicy()));
        instance.addJson("SiopModePolicy", String.valueOf(packageR.getSiopModePolicy()));
        instance.addJson("SiopMode", String.valueOf(packageR.getCustomSiopMode()));
        instance.addJson("PackageIpmPolicy", String.valueOf(packageR.getIpmPolicy()));
        instance.addJson("GlobalIpmPolicy", String.valueOf(DbHelper.getInstance().getGlobalDao().getIpmPolicy()));
        instance.addJson("Dss", String.valueOf(packageR.getAppliedDss()));
        instance.addJson("Dfs", String.valueOf(packageR.getCustomDfs()));
        int vrrMaxValue = DbHelper.getInstance().getGlobalDao().getVrrMaxValue();
        if (packageR.getVrrMaxValue() > 0) {
            vrrMaxValue = packageR.getVrrMaxValue();
        }
        instance.addJson("VrrMax", String.valueOf(vrrMaxValue));
        int vrrMinValue = DbHelper.getInstance().getGlobalDao().getVrrMinValue();
        if (packageR.getVrrMinValue() > 0) {
            vrrMinValue = packageR.getVrrMinValue();
        }
        instance.addJson("VrrMin", String.valueOf(vrrMinValue));
        instance.addJson("UseJSONPolicy", String.valueOf(mUseJSONPolicy));
    }

    public boolean checkLaunchConditions(PkgData pkgData) {
        IpmCore.getInstance(this.mContext).setEnableAnyMode(true);
        Policy policy = new Policy();
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        String ipmPolicy = globalDao.getIpmPolicy();
        if (ipmPolicy != null && !ipmPolicy.equals(BuildConfig.VERSION_NAME)) {
            GosLog.i(LOG_TAG, "onResume. globalIpmPolicy : " + ipmPolicy);
            try {
                policy.parse(new JSONObject(ipmPolicy));
            } catch (JSONException e) {
                GosLog.e(LOG_TAG, "Failed to parse global policy: " + e);
            }
        }
        String ipmPolicy2 = pkgData.getPkg().getIpmPolicy();
        if (ipmPolicy2 != null && !ipmPolicy2.equals(BuildConfig.VERSION_NAME)) {
            GosLog.i(LOG_TAG, "onResume. pkgIpmPolicy " + pkgData.getPackageName() + " : " + ipmPolicy2);
            try {
                policy.parse(new JSONObject(ipmPolicy2));
            } catch (JSONException e2) {
                GosLog.e(LOG_TAG, "Failed to parse package policy: " + e2);
            }
        }
        boolean z = false;
        int value = new PreferenceHelper().getValue(SecureSettingConstants.KEY_GAME_BOOSTER_PRIORITY_MODE, 0);
        GosLog.d(LOG_TAG, "onResume. priorityMode=" + value);
        if (value != 0) {
            globalDao.setIpmMode(new Global.IdAndIpmMode(2));
            policy.customProfileValues = null;
        } else if (!tryToSetIpmModeUsingSiopMode(pkgData, policy)) {
            return false;
        }
        if (mUseJSONPolicy) {
            GosLog.d(LOG_TAG, "onResume. ipmForAllGameEnabled: ");
            this.mRestoreCfg = IpmCore.getInstance(this.mContext).applyJsonPolicy(policy);
        }
        FeatureFlag featureFlag = pkgData.getFeatureFlagMap().get("ipm");
        if (featureFlag == null) {
            return false;
        }
        if (featureFlag.getState() == State.FORCIBLY_DISABLED || featureFlag.getState() == State.DISABLED) {
            z = true;
        }
        return !z;
    }

    private boolean tryToSetIpmModeUsingSiopMode(PkgData pkgData, Policy policy) {
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        if (GlobalDbHelper.getInstance().isPositiveFeatureFlag("siop_mode")) {
            int siopMode = globalDao.getSiopMode();
            GosLog.d(LOG_TAG, "onResume. global siopMode " + siopMode);
            if (GlobalDbHelper.getInstance().isUsingCustomValue("siop_mode")) {
                siopMode = pkgData.getCustomSiopMode();
                GosLog.d(LOG_TAG, "onResume. customSiopMode " + siopMode);
            }
            if (siopMode != -1) {
                if (siopMode != 1) {
                    if (!IpmCore.getInstance(this.mContext).getEnableAnyMode() && !policy.siopMode[1]) {
                        return false;
                    }
                    globalDao.setIpmMode(new Global.IdAndIpmMode(1));
                } else if (!IpmCore.getInstance(this.mContext).getEnableAnyMode() && !policy.siopMode[2]) {
                    return false;
                } else {
                    globalDao.setIpmMode(new Global.IdAndIpmMode(2));
                }
            } else if (!IpmCore.getInstance(this.mContext).getEnableAnyMode() && !policy.siopMode[0]) {
                return false;
            } else {
                globalDao.setIpmMode(new Global.IdAndIpmMode(0));
            }
        } else if (!IpmCore.getInstance(this.mContext).getEnableAnyMode() && policy.defaultMode < 0) {
            return false;
        } else {
            globalDao.setIpmMode(new Global.IdAndIpmMode(policy.defaultMode));
        }
        return true;
    }

    public int getMinimumSupportedSiopMode() {
        Policy policy = new Policy();
        String ipmPolicy = DbHelper.getInstance().getGlobalDao().getIpmPolicy();
        if (ipmPolicy != null && !ipmPolicy.equals(BuildConfig.VERSION_NAME)) {
            try {
                policy.parse(new JSONObject(ipmPolicy));
            } catch (JSONException e) {
                GosLog.e(LOG_TAG, "Failed to parse global policy: " + e);
            }
        }
        if (GlobalDbHelper.getInstance().isPositiveFeatureFlag("siop_mode") && IpmCore.getInstance(this.mContext).getEnableAnyMode()) {
            if (policy.siopMode[0]) {
                return -1;
            }
            if (policy.siopMode[1]) {
                return 0;
            }
            if (policy.siopMode[2]) {
            }
        }
        return 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0049, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onFocusOut(com.samsung.android.game.gos.data.PkgData r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            if (r4 == 0) goto L_0x001d
            java.lang.String r0 = "IpmFeature"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x004a }
            r1.<init>()     // Catch:{ all -> 0x004a }
            java.lang.String r2 = "onPause. "
            r1.append(r2)     // Catch:{ all -> 0x004a }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ all -> 0x004a }
            r1.append(r4)     // Catch:{ all -> 0x004a }
            java.lang.String r4 = r1.toString()     // Catch:{ all -> 0x004a }
            com.samsung.android.game.gos.util.GosLog.v(r0, r4)     // Catch:{ all -> 0x004a }
        L_0x001d:
            boolean r4 = r3.mIsStarted     // Catch:{ all -> 0x004a }
            if (r4 != 0) goto L_0x002a
            java.lang.String r4 = "IpmFeature"
            java.lang.String r0 = "onPause called without corresponding onResume"
            com.samsung.android.game.gos.util.GosLog.i(r4, r0)     // Catch:{ all -> 0x004a }
            monitor-exit(r3)
            return
        L_0x002a:
            r4 = 0
            r3.mIsStarted = r4     // Catch:{ all -> 0x004a }
            android.content.Context r4 = r3.mContext     // Catch:{ all -> 0x004a }
            com.samsung.android.game.gos.feature.ipm.IpmCore r4 = com.samsung.android.game.gos.feature.ipm.IpmCore.getInstance(r4)     // Catch:{ all -> 0x004a }
            r4.stop()     // Catch:{ all -> 0x004a }
            com.samsung.android.game.gos.ipm.Policy r4 = r3.mRestoreCfg     // Catch:{ all -> 0x004a }
            if (r4 == 0) goto L_0x0048
            android.content.Context r4 = r3.mContext     // Catch:{ all -> 0x004a }
            com.samsung.android.game.gos.feature.ipm.IpmCore r4 = com.samsung.android.game.gos.feature.ipm.IpmCore.getInstance(r4)     // Catch:{ all -> 0x004a }
            com.samsung.android.game.gos.ipm.Policy r0 = r3.mRestoreCfg     // Catch:{ all -> 0x004a }
            r4.restoreJsonPolicy(r0)     // Catch:{ all -> 0x004a }
            r4 = 0
            r3.mRestoreCfg = r4     // Catch:{ all -> 0x004a }
        L_0x0048:
            monitor-exit(r3)
            return
        L_0x004a:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ipm.IpmFeature.onFocusOut(com.samsung.android.game.gos.data.PkgData):void");
    }

    public static boolean isAllowMoreHeatAvailable() {
        GlobalFeatureFlag globalFeatureFlag = DbHelper.getInstance().getGlobalFeatureFlagDao().get(Constants.V4FeatureFlag.ALLOW_MORE_HEAT);
        if ((globalFeatureFlag != null && !globalFeatureFlag.forcedFlag) || Build.PRODUCT.equalsIgnoreCase("r1qks") || Build.PRODUCT.equalsIgnoreCase("a50szc")) {
            return true;
        }
        String countryIso = SemSystemProperties.getCountryIso();
        if (!Build.PRODUCT.equalsIgnoreCase("a51nsxx") || !"IN".equalsIgnoreCase(countryIso)) {
            return false;
        }
        return true;
    }

    public void setDynamicRefreshRate(int i) {
        if (this.mIsStarted) {
            IpmCore.getInstance(this.mContext).setDynamicRefreshRate((float) i);
        }
    }
}
