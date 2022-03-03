package com.samsung.android.game.gos.feature.resumeboost;

import android.app.Application;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.selibrary.SeDvfsInterfaceImpl;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;

public class LaunchBoostFeature implements RuntimeInterface {
    private static final String LOG_TAG = "LaunchBoostFeature";

    public String getName() {
        return Constants.V4FeatureFlag.LAUNCH_BOOST;
    }

    public void restoreDefault(PkgData pkgData) {
    }

    private LaunchBoostFeature() {
    }

    public static LaunchBoostFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    static class SingletonHolder {
        static LaunchBoostFeature INSTANCE = new LaunchBoostFeature();

        SingletonHolder() {
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        if (z) {
            GosLog.v(LOG_TAG, "onFocusIn()-calling ResumeBoostCore.onResume()");
            ResumeBoostCore.getInstance().onFocusIn(pkgData != null ? pkgData.getPkg() : null, true);
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

    public void changePackageSettings(Package packageR, int i, int i2, int i3) {
        ResumeBoostCore.getInstance().changePackageSettings(Constants.BoostType.Launch, packageR, i, i2, i3);
    }
}
