package com.samsung.android.game.gos.feature.volumecontrol;

import android.content.pm.PackageManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.selibrary.SeAudioManager;
import com.samsung.android.game.gos.util.GosLog;

public class VolumeControlFeature implements RuntimeInterface {
    private static final String LOG_TAG = "VolumeControlFeature";

    public String getName() {
        return "volume_control";
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    private VolumeControlFeature() {
    }

    public static VolumeControlFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static boolean enableVolumeLimit(String str, boolean z, PackageManager packageManager) {
        if (packageManager == null) {
            GosLog.e(LOG_TAG, "enableVolumeLimit. packageManager == null. do nothing");
            return false;
        }
        try {
            int packageUid = packageManager.getPackageUid(str, 0);
            GosLog.i(LOG_TAG, "enableVolumeLimit invoke setVolumeLimitEnabled(), " + packageUid + ", " + z);
            SeAudioManager.getInstance().setVolumeLimitEnabled(packageUid, z);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            GosLog.w(LOG_TAG, "enableVolumeLimit. NameNotFoundException!!", e);
            return false;
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        GosLog.v(LOG_TAG, "onResume(). " + pkgData.getPackageName());
        enableVolumeLimit(pkgData.getPackageName(), true, AppContext.get().getPackageManager());
    }

    public void onFocusOut(PkgData pkgData) {
        GosLog.v(LOG_TAG, "onPause(). " + pkgData.getPackageName());
        enableVolumeLimit(pkgData.getPackageName(), false, AppContext.get().getPackageManager());
    }

    public void restoreDefault(PkgData pkgData) {
        if (pkgData == null) {
            GosLog.e(LOG_TAG, "pkgData is null.");
        } else {
            enableVolumeLimit(pkgData.getPackageName(), false, AppContext.get().getPackageManager());
        }
    }

    private static class SingletonHolder {
        public static VolumeControlFeature INSTANCE = new VolumeControlFeature();

        private SingletonHolder() {
        }
    }
}
