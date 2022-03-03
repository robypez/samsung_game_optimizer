package com.samsung.android.game.gos.selibrary;

import android.app.ActivityManager;
import android.content.Context;
import android.os.IBinder;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeActivityManager {
    private static final String LOG_TAG = SeActivityManager.class.getSimpleName();
    private ActivityManager mActivityManager;

    public String getAppNameFromPid(Context context, int i) {
        String str = BuildConfig.VERSION_NAME;
        try {
            if (this.mActivityManager == null) {
                GosLog.w(LOG_TAG, "getAppNameFromPid()-ActivityManager is null");
                return str;
            }
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = this.mActivityManager.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                GosLog.w(LOG_TAG, "getAppNameFromPid()-RunningAppProcess list is null");
                return str;
            }
            for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
                try {
                    if (next.pid == i) {
                        str = next.processName;
                    }
                } catch (Exception unused) {
                }
            }
            return str;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            GosLog.w(str2, "getAppNameFromPid(). " + e.getMessage());
        }
    }

    public String[] getTopProcessPkg(Context context) {
        try {
            if (this.mActivityManager == null) {
                GosLog.w(LOG_TAG, "getTopProcessPkg()-ActivityManager is null");
                return null;
            }
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = this.mActivityManager.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                GosLog.w(LOG_TAG, "getTopProcessPkg()-RunningAppProcess list is null");
                return null;
            }
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(0);
            if (runningAppProcessInfo != null) {
                return runningAppProcessInfo.pkgList;
            }
            return null;
        } catch (Exception e) {
            String str = LOG_TAG;
            GosLog.w(str, "getTopProcessPkg(). " + e.getMessage());
            return null;
        }
    }

    public int getPid(Context context, String str) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = this.mActivityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return -1;
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (Objects.equals(next.processName, str)) {
                return next.pid;
            }
        }
        return -1;
    }

    public boolean forceStopPackage(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        return forceStopPackages(arrayList);
    }

    public boolean forceStopPackages(List<String> list) {
        if (list == null) {
            return false;
        }
        for (String _forceStopPackage_ : list) {
            _forceStopPackage_(_forceStopPackage_);
        }
        return true;
    }

    private SeActivityManager() {
        this.mActivityManager = (ActivityManager) AppContext.get().getSystemService("activity");
    }

    public static SeActivityManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public void _forceStopPackage_(String str) throws RuntimeException {
        if (this.mActivityManager != null && !AppVariable.isUnitTest()) {
            this.mActivityManager.semForceStopPackage(str);
        }
    }

    public void setProcessImportant(IBinder iBinder, int i, boolean z) throws RuntimeException {
        if (this.mActivityManager != null && !AppVariable.isUnitTest()) {
            this.mActivityManager.semSetProcessImportant(iBinder, i, z);
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeActivityManager INSTANCE = new SeActivityManager();

        private SingletonHolder() {
        }
    }
}
