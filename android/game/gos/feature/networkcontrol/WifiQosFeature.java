package com.samsung.android.game.gos.feature.networkcontrol;

import android.content.Intent;
import android.content.pm.PackageManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.gfi.GfiRinglogAggregator;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class WifiQosFeature implements RuntimeInterface {
    static final String INTENT_WIFI_QOS_CONTROL_END = "com.samsung.android.game.intent.action.WIFI_QOS_CONTROL_END";
    private static final String INTENT_WIFI_QOS_CONTROL_START = "com.samsung.android.game.intent.action.WIFI_QOS_CONTROL_START";
    private static final String LOG_TAG = "WifiQosFeature";
    private static final int TID_BE = 0;
    private static final int TID_DEFAULT = 0;
    private int mTid;

    public String getName() {
        return Constants.V4FeatureFlag.WIFI_QOS;
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    public void restoreDefault(PkgData pkgData) {
    }

    public static WifiQosFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private WifiQosFeature() {
        this.mTid = 0;
        updatePolicy((PkgData) null);
    }

    /* access modifiers changed from: package-private */
    public void updatePolicy(PkgData pkgData) {
        String wifiQosPolicy;
        String wifiQosPolicy2 = DbHelper.getInstance().getGlobalDao().getWifiQosPolicy();
        JSONObject jSONObject = null;
        if (wifiQosPolicy2 == null || wifiQosPolicy2.equals(BuildConfig.VERSION_NAME)) {
            wifiQosPolicy2 = null;
        } else {
            GosLog.d(LOG_TAG, "updatePolicy(). globalPolicy : " + wifiQosPolicy2);
        }
        if (!(pkgData == null || (wifiQosPolicy = pkgData.getPkg().getWifiQosPolicy()) == null || wifiQosPolicy.equals(BuildConfig.VERSION_NAME))) {
            GosLog.d(LOG_TAG, "updatePolicy(). pkgPolicy of " + pkgData.getPackageName() + " : " + wifiQosPolicy);
            wifiQosPolicy2 = wifiQosPolicy;
        }
        if (wifiQosPolicy2 != null) {
            try {
                jSONObject = new JSONObject(wifiQosPolicy2);
            } catch (JSONException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        int i = 0;
        if (jSONObject != null && jSONObject.has("tid")) {
            i = jSONObject.optInt("tid", 0);
        }
        this.mTid = i;
        GosLog.i(LOG_TAG, "updatePolicy(). mTid: " + this.mTid);
    }

    /* access modifiers changed from: package-private */
    public int putUidToIntent(Intent intent, PackageManager packageManager, String str) {
        int i = -1;
        if (packageManager == null) {
            return -1;
        }
        try {
            i = packageManager.getPackageUid(str, 0);
            intent.putExtra(GfiRinglogAggregator.UID, i);
            return i;
        } catch (PackageManager.NameNotFoundException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return i;
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        updatePolicy(pkgData);
        if (this.mTid != 0) {
            Intent intent = new Intent();
            intent.setAction(INTENT_WIFI_QOS_CONTROL_START);
            intent.putExtra("tid", this.mTid);
            int putUidToIntent = putUidToIntent(intent, AppContext.get().getPackageManager(), pkgData.getPackageName());
            AppContext.get().sendBroadcast(intent, "android.permission.HARDWARE_TEST");
            GosLog.i(LOG_TAG, "onResume(), broadcast WIFI_QOS_CONTROL_START intent for " + pkgData.getPackageName() + " (tid: " + this.mTid + ", uid: " + putUidToIntent + ")");
        }
    }

    public void onFocusOut(PkgData pkgData) {
        Intent intent = new Intent();
        intent.setAction(INTENT_WIFI_QOS_CONTROL_END);
        AppContext.get().sendBroadcast(intent, "android.permission.HARDWARE_TEST");
        GosLog.i(LOG_TAG, "onPause(), broadcast WIFI_QOS_CONTROL_END intent");
    }

    /* access modifiers changed from: package-private */
    public int getTid() {
        return this.mTid;
    }

    private static final class Policy {
        static final String TID = "tid";

        private Policy() {
        }
    }

    private static class SingletonHolder {
        public static WifiQosFeature INSTANCE = new WifiQosFeature();

        private SingletonHolder() {
        }
    }
}
