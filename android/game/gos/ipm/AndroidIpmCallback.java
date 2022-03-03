package com.samsung.android.game.gos.ipm;

import android.content.Context;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AndroidIpmCallback extends IpmCallback {
    private final AndroidDeviceSettings mAndroidDeviceSettings;
    private final AndroidPackage mAndroidPackage;
    private final Context mContext;
    private final String mGosVersion;
    private final Listener mListener;
    private final ResumeBooster mResumeBooster;
    private final Sysfs mSysfs;

    public interface Listener {
        void onStarted();

        void onStopped();

        void onSystemCreated();
    }

    public AndroidIpmCallback(Context context, AndroidPackage androidPackage, AndroidDeviceSettings androidDeviceSettings, Sysfs sysfs, ResumeBooster resumeBooster, Listener listener, String str) {
        this.mContext = context;
        this.mAndroidPackage = androidPackage;
        this.mAndroidDeviceSettings = androidDeviceSettings;
        this.mSysfs = sysfs;
        this.mResumeBooster = resumeBooster;
        this.mListener = listener;
        this.mGosVersion = str;
    }

    public Object getContext() {
        return this.mContext;
    }

    public String getGosVersion() {
        return this.mGosVersion;
    }

    public String getGosNamespace() {
        return this.mContext.getPackageName().replace('.', '/');
    }

    public String getFilesPath() {
        return this.mContext.getFilesDir().getAbsolutePath();
    }

    public String getCachePath() {
        File cacheDir = this.mContext.getCacheDir();
        return cacheDir != null ? cacheDir.getAbsolutePath() : BuildConfig.VERSION_NAME;
    }

    public int getApplicationUid() {
        return this.mAndroidPackage.getApplicationUid();
    }

    public String getApplicationAbi() {
        return this.mAndroidPackage.getApplicationAbi();
    }

    public String getApplicationName() {
        return this.mAndroidPackage.getName();
    }

    public boolean isMinLockBoosterWorking() {
        return this.mResumeBooster.isMinLockBoosterWorking();
    }

    public int getBigTurboDurationSeconds() {
        return (int) this.mResumeBooster.getBigTurboDuration(TimeUnit.SECONDS);
    }

    public boolean isBigTurboWorking() {
        return this.mResumeBooster.isBigTurboWorking();
    }

    public int getMinLockBoosterDurationSeconds() {
        return (int) this.mResumeBooster.getMinLockBoosterDuration(TimeUnit.SECONDS);
    }

    public MapLongFloat getGpuEff() {
        return WrapUtility.toMapLongFloat(this.mAndroidDeviceSettings.getGpuEff());
    }

    public MapLongFloat getCpuEff(int i) {
        Map<Long, Float> cpuEff = this.mAndroidDeviceSettings.getCpuEff(i);
        return cpuEff != null ? WrapUtility.toMapLongFloat(cpuEff) : new MapLongFloat();
    }

    public int getClusterCount() {
        return this.mAndroidDeviceSettings.getClusterSize();
    }

    public VectorString getSysFsData(VectorString vectorString) {
        return WrapUtility.toVectorString(this.mSysfs.getSysFsData(WrapUtility.toArray(vectorString)));
    }

    public void onStarted() {
        this.mListener.onStarted();
    }

    public void onStopped() {
        this.mListener.onStopped();
    }

    public void onSystemCreated() {
        this.mListener.onSystemCreated();
    }

    public boolean getScenario() {
        return this.mAndroidDeviceSettings.getIpmScenario();
    }

    public boolean isScenarioDynamicChecked() {
        return this.mAndroidDeviceSettings.isIpmScenarioDynamicChecked();
    }
}
