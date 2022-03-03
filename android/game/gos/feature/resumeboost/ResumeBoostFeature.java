package com.samsung.android.game.gos.feature.resumeboost;

import android.app.Application;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostCore;
import com.samsung.android.game.gos.selibrary.SeDvfsInterfaceImpl;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class ResumeBoostFeature implements RuntimeInterface {
    private static final String LOG_TAG = "ResumeBoostFeature";

    public String getName() {
        return Constants.V4FeatureFlag.RESUME_BOOST;
    }

    public void restoreDefault(PkgData pkgData) {
    }

    private ResumeBoostFeature() {
    }

    public static ResumeBoostFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    static class SingletonHolder {
        static ResumeBoostFeature INSTANCE = new ResumeBoostFeature();

        SingletonHolder() {
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        if (!z) {
            GosLog.v(LOG_TAG, "onFocusIn()-calling ResumeBoostCore.onResume()");
            ResumeBoostCore.getInstance().onFocusIn(pkgData != null ? pkgData.getPkg() : null, false);
        }
    }

    public void onFocusOut(PkgData pkgData) {
        ResumeBoostCore.getInstance().onFocusOut();
    }

    public boolean isAvailableForSystemHelper() {
        Application application = AppContext.get();
        boolean z = SeDvfsInterfaceImpl.createInstance(application, SeDvfsInterfaceImpl.TYPE_CPU_MIN).getSupportedFrequency() != null;
        boolean z2 = SeDvfsInterfaceImpl.createInstance(application, SeDvfsInterfaceImpl.TYPE_BUS_MIN).getSupportedFrequency() != null;
        if (z || z2) {
            return true;
        }
        return false;
    }

    public int getCurrentDurationSec() {
        return ResumeBoostCore.getInstance().getCurrentDurationSec();
    }

    public int getCurrentCpuIndex() {
        return ResumeBoostCore.getInstance().getCurrentCpuIndex();
    }

    public int getCurrentBusIndex() {
        return ResumeBoostCore.getInstance().getCurrentBusIndex();
    }

    public void changeSettings(int i, int i2, int i3, int i4) {
        ResumeBoostCore.getInstance().changeSettings(i, i2, i3, i4);
    }

    public String getSupportedFreq(int i) {
        if (i == SeDvfsInterfaceImpl.TYPE_CPU_MIN || i == SeDvfsInterfaceImpl.TYPE_BUS_MIN) {
            int[] supportedFrequency = SeDvfsInterfaceImpl.createInstance(AppContext.get(), i).getSupportedFrequency();
            if (supportedFrequency == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            int length = supportedFrequency.length;
            for (int i2 = 0; i2 < length; i2++) {
                sb.append("[");
                sb.append(i2);
                sb.append("]");
                sb.append(supportedFrequency[i2]);
                if (i2 < length - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        GosLog.e(LOG_TAG, "getSupportedFreq(). unexpected type: " + i);
        return null;
    }

    public boolean isMinLockBoosterWorking() {
        return ResumeBoostCore.getInstance().isMinLockBoosterWorking();
    }

    public boolean isBigTurboWorking() {
        return ResumeBoostCore.getInstance().isBigTurboWorking();
    }

    public int getCurrentBigTurboDurationSec() {
        return ResumeBoostCore.getInstance().getCurrentBigTurboDurationSec();
    }

    public int getCpuIndexFromServer(Constants.BoostType boostType) {
        String str;
        int i = 0;
        try {
            if (boostType == Constants.BoostType.Resume) {
                str = DbHelper.getInstance().getGlobalDao().getResumeBoostPolicy();
            } else {
                str = DbHelper.getInstance().getGlobalDao().getLaunchBoostPolicy();
            }
            JSONObject jSONObject = null;
            if (str != null) {
                jSONObject = new JSONObject(str);
            }
            if (jSONObject != null && jSONObject.has(ResumeBoostCore.Policy.CPU_FREQ)) {
                i = jSONObject.optInt(ResumeBoostCore.Policy.CPU_FREQ);
            }
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return ResumeBoostCore.getInstance().getCpuIndexFromServer(i);
    }

    public int getBusIndexFromServer(Constants.BoostType boostType) {
        String str;
        int i = 0;
        try {
            if (boostType == Constants.BoostType.Resume) {
                str = DbHelper.getInstance().getGlobalDao().getResumeBoostPolicy();
            } else {
                str = DbHelper.getInstance().getGlobalDao().getLaunchBoostPolicy();
            }
            JSONObject jSONObject = null;
            if (str != null) {
                jSONObject = new JSONObject(str);
            }
            if (jSONObject != null && jSONObject.has(ResumeBoostCore.Policy.BUS_FREQ)) {
                i = jSONObject.optInt(ResumeBoostCore.Policy.BUS_FREQ);
            }
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return ResumeBoostCore.getInstance().getBusIndexFromServer(i);
    }

    public int getResumeBoostDuration(Constants.BoostType boostType) {
        String str;
        if (boostType == Constants.BoostType.Resume) {
            str = DbHelper.getInstance().getGlobalDao().getResumeBoostPolicy();
        } else {
            str = DbHelper.getInstance().getGlobalDao().getLaunchBoostPolicy();
        }
        JSONObject jSONObject = null;
        if (str != null) {
            try {
                jSONObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jSONObject != null) {
            return jSONObject.optInt("duration", 10);
        }
        return 10;
    }

    public void updatePolicy() {
        ResumeBoostCore.getInstance().updatePolicy((Package) null, true);
    }

    public void changePackageSettings(Package packageR, int i, int i2, int i3) {
        ResumeBoostCore.getInstance().changePackageSettings(Constants.BoostType.Resume, packageR, i, i2, i3);
    }
}
