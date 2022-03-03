package com.samsung.android.game.gos.endpoint;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.IBinder;
import android.util.Pair;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.AidlPermissionHolder;
import com.samsung.android.game.gos.data.LocalCache;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.TestDataManager;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.util.SecSignatures;

class AidlService extends Service {
    private static final String LOG_TAG = AidlService.class.getSimpleName();
    private Signature mPlatformKey = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    AidlService() {
    }

    public void onCreate() {
        GosLog.d(LOG_TAG, "onCreate.");
        super.onCreate();
        String value = new PreferenceHelper().getValue(PreferenceHelper.PREF_SIGNATURE, (String) null);
        if (value == null) {
            this.mPlatformKey = getMySignature(getPackageManager());
            new PreferenceHelper().put(PreferenceHelper.PREF_SIGNATURE, this.mPlatformKey.toCharsString());
            return;
        }
        this.mPlatformKey = new Signature(value);
    }

    private Signature getMySignature(PackageManager packageManager) {
        Signature signature = null;
        if (packageManager != null) {
            try {
                for (Signature signature2 : packageManager.getPackageInfo(getPackageName(), 134217728).signingInfo.getApkContentsSigners()) {
                    if (signature2 != null) {
                        signature = signature2;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                GosLog.e(LOG_TAG, "failed to get my signature");
            }
        }
        return signature;
    }

    /* access modifiers changed from: protected */
    public boolean isAllowedSystemApp(String str, int i) {
        StringBuilder sb = new StringBuilder("isAllowedSystemApp(), ");
        boolean z = true;
        if ("android.uid.system:1000".equals(str) && i == 1000) {
            sb.append("it is a system app.");
        } else if ("android.uid.intelligenceservice:5010".equals(str) && i == 5010) {
            sb.append("it is Rubin.");
        } else if (str.contains("com.samsung.accessory.wmanager")) {
            sb.append("it is Buds+.");
        } else {
            z = false;
        }
        if (z) {
            GosLog.i(LOG_TAG, sb.toString());
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public Pair<Boolean, String> getPermissionInfo() {
        GosLog.d(LOG_TAG, "getPermissionInfo(), begin");
        PackageManager packageManager = getPackageManager();
        if (packageManager == null) {
            return new Pair<>(false, (Object) null);
        }
        LocalCache localCache = new LocalCache();
        int callingUid = Binder.getCallingUid();
        GosLog.i(LOG_TAG, "getPermissionInfo(), callerUid: " + callingUid);
        Pair<String, Boolean> info = AidlPermissionHolder.getInstance().getInfo(callingUid);
        logPkgNameToLocalLog(localCache, info);
        if (info != null) {
            return new Pair<>(info.second, info.first);
        }
        String nameForUid = packageManager.getNameForUid(callingUid);
        GosLog.i(LOG_TAG, "getPermissionInfo(), callerPkgName from Uid: " + nameForUid);
        if (nameForUid == null) {
            GosLog.e(LOG_TAG, "getPermissionInfo(), callerPkgName == null");
            return new Pair<>(false, (Object) null);
        } else if (isAllowedSystemApp(nameForUid, callingUid)) {
            AidlPermissionHolder.getInstance().add(nameForUid, callingUid, true);
            return new Pair<>(true, nameForUid);
        } else {
            if (nameForUid.contains(":")) {
                nameForUid = SeActivityManager.getInstance().getAppNameFromPid(AppContext.get(), Binder.getCallingPid());
                GosLog.i(LOG_TAG, "getPermissionInfo(), _pkgName from Pid: " + nameForUid);
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(nameForUid, 134217728);
                Signature[] otherKeys = new SecSignatures().getOtherKeys();
                for (Signature signature : packageInfo.signingInfo.getApkContentsSigners()) {
                    if (this.mPlatformKey.equals(signature)) {
                        GosLog.i(LOG_TAG, "getPermissionInfo(), platform signature");
                        AidlPermissionHolder.getInstance().add(nameForUid, callingUid, true);
                        return new Pair<>(true, nameForUid);
                    }
                    if (localCache.getSecGameFamilyPackageNames().contains(nameForUid)) {
                        for (Signature equals : otherKeys) {
                            if (equals.equals(signature)) {
                                GosLog.i(LOG_TAG, "getPermissionInfo(), samsung signature");
                                AidlPermissionHolder.getInstance().add(nameForUid, callingUid, true);
                                return new Pair<>(true, nameForUid);
                            }
                        }
                        continue;
                    }
                }
                if (!PlatformUtil.isDebugBinary()) {
                    if (!TestDataManager.isTestMode()) {
                        GosLog.e(LOG_TAG, "getPermissionInfo(), hash values are not matched.");
                        AidlPermissionHolder.getInstance().add(nameForUid, callingUid, false);
                        return new Pair<>(false, nameForUid);
                    }
                }
                GosLog.d(LOG_TAG, "getPermissionInfo(), allow every packages for debug");
                return new Pair<>(true, nameForUid);
            } catch (PackageManager.NameNotFoundException unused) {
                GosLog.e(LOG_TAG, "getPermissionInfo(), NameNotFoundException. callerPkgName: " + nameForUid);
                return new Pair<>(false, nameForUid);
            }
        }
    }

    private void logPkgNameToLocalLog(LocalCache localCache, Pair<String, Boolean> pair) {
        String str;
        if (pair != null) {
            str = (String) pair.first;
        } else {
            str = SeActivityManager.getInstance().getAppNameFromPid(AppContext.get(), Binder.getCallingPid());
        }
        String str2 = LOG_TAG;
        GosLog.d(str2, "getPermissionInfo(), currentPkgName from Pid: " + str);
        if (str != null) {
            localCache.getSecGameFamilyPackageNames().add("com.samsung.android.game.gos");
            if (!localCache.getSecGameFamilyPackageNames().contains(str)) {
                String str3 = LOG_TAG;
                GosLog.d(str3, "getPermissionInfo(), addLocalLog currentPkgName is " + str);
                LocalLogDbHelper instance = LocalLogDbHelper.getInstance();
                String str4 = LOG_TAG;
                instance.addLocalLog(str4, "bind service pkgName:" + str);
            }
        }
    }
}
