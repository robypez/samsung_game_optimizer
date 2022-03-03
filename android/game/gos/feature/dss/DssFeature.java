package com.samsung.android.game.gos.feature.dss;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.StaticInterface;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import java.util.Arrays;
import java.util.Locale;
import org.json.JSONObject;

public class DssFeature implements StaticInterface {
    private static final String LOG_TAG = "DssFeature";
    private int mShortSide;

    public String getName() {
        return Constants.V4FeatureFlag.RESOLUTION;
    }

    private DssFeature() {
        this.mShortSide = -1;
        updateDisplayMetrics();
    }

    public static DssFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int getDisplayShortSide() {
        if (this.mShortSide < 0) {
            updateDisplayMetrics();
        }
        return this.mShortSide;
    }

    public void updateDisplayMetrics() {
        WindowManager windowManager;
        Application application = AppContext.get();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (!(application == null || (windowManager = (WindowManager) application.getSystemService("window")) == null)) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        int i = displayMetrics.densityDpi;
        int max = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
        this.mShortSide = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        GosLog.i(LOG_TAG, "DPI: " + i + ", LongSide: " + max + ", ShortSide: " + this.mShortSide);
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DssFeature INSTANCE = new DssFeature();

        private SingletonHolder() {
        }
    }

    public SemPackageConfiguration getUpdatedConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        return Dss.getInstance().getUpdatedConfig(pkgData, semPackageConfiguration);
    }

    public SemPackageConfiguration getDefaultConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        return Dss.getInstance().getDefaultConfig(semPackageConfiguration);
    }

    public boolean isAvailableForSystemHelper() {
        return SeGameManager.getInstance().isDynamicSurfaceScalingSupported();
    }

    /* access modifiers changed from: package-private */
    public void checkDpiUpdateDssValues(WindowManager windowManager) {
        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            GosLog.i(LOG_TAG, "dpi: " + displayMetrics.densityDpi + ", width: " + displayMetrics.widthPixels + ", height: " + displayMetrics.heightPixels);
            this.mShortSide = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            _updateDssValues(displayMetrics);
        }
    }

    /* access modifiers changed from: package-private */
    public void _updateDssValues(DisplayMetrics displayMetrics) {
        float[] fArr;
        if (displayMetrics.widthPixels <= 720 || displayMetrics.heightPixels <= 720) {
            fArr = new float[]{100.0f, 100.0f, 100.0f, 100.0f};
        } else {
            fArr = (displayMetrics.densityDpi <= 240 || displayMetrics.widthPixels <= 1080 || displayMetrics.heightPixels <= 1080) ? new float[]{100.0f, 100.0f, 75.0f, 50.0f} : null;
        }
        if (fArr != null) {
            GlobalDbHelper.getInstance().setEachModeDss(fArr);
            float[] eachModeDss = GlobalDbHelper.getInstance().getEachModeDss();
            GosLog.i(LOG_TAG, String.format(Locale.US, "init EachModeDss %s, DefaultDss %s", new Object[]{Arrays.toString(eachModeDss), Float.valueOf(eachModeDss[1])}));
        }
    }

    public void onDbInitialized(Context context) {
        checkDpiUpdateDssValues((WindowManager) context.getSystemService("window"));
    }

    public static float getDefaultDss(PkgData pkgData) {
        return getDefaultDss(pkgData.getPkg());
    }

    public static float getDefaultDss(Package packageR) {
        float[] mergedEachModeDss = getMergedEachModeDss(packageR);
        if (mergedEachModeDss.length > 1) {
            return mergedEachModeDss[1];
        }
        return 100.0f;
    }

    public static float[] getMergedEachModeDss(PkgData pkgData) {
        return getMergedEachModeDss(pkgData.getPkg());
    }

    public static float[] getMergedEachModeDss(Package packageR) {
        float[] eachModeDss = Global.DefaultGlobalData.getEachModeDss();
        float[] copyOf = Arrays.copyOf(eachModeDss, eachModeDss.length);
        mergeDssList(copyOf, GlobalDbHelper.getInstance().getEachModeDss());
        mergeDssList(copyOf, packageR.getEachModeDssArray());
        GosLog.i(LOG_TAG, "getMergedEachModeDss()-merged: " + Arrays.toString(copyOf) + ", " + packageR.pkgName);
        return copyOf;
    }

    static void mergeDssList(float[] fArr, float[] fArr2) {
        if (fArr != null && fArr2 != null) {
            int i = 0;
            while (i < fArr2.length && i < fArr.length) {
                if (fArr2[i] >= 1.0f && fArr2[i] <= 100.0f) {
                    fArr[i] = fArr2[i];
                }
                if (i > 0) {
                    int i2 = i - 1;
                    if (fArr[i2] < fArr[i]) {
                        fArr[i] = fArr[i2];
                    }
                }
                i++;
            }
        }
    }
}
