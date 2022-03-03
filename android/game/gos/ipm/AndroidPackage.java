package com.samsung.android.game.gos.ipm;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AndroidPackage {
    private static final String LOG_TAG = "AndroidPackage";
    private volatile String mApplicationAbi = BuildConfig.VERSION_NAME;
    private volatile int mApplicationUid = -1;
    private volatile String mName = BuildConfig.VERSION_NAME;
    private final PackageManager mPackageManager;

    public AndroidPackage(PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    public void setName(String str) {
        try {
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(str, 0);
            this.mName = str;
            this.mApplicationUid = applicationInfo.uid;
            String[] split = applicationInfo.nativeLibraryDir.split("/");
            this.mApplicationAbi = split[split.length - 1];
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e(LOG_TAG, String.format("Package '%s' not found.", new Object[]{str}));
        }
    }

    public String getName() {
        return this.mName;
    }

    public int getApplicationUid() {
        return this.mApplicationUid;
    }

    public String getApplicationAbi() {
        return this.mApplicationAbi;
    }
}
