package com.samsung.android.game.gos.data.model;

import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.Constants;

public class Package {
    public int appliedCpuLevel;
    public float appliedDss;
    public int appliedGpuLevel;
    public String categoryCode = Constants.CategoryCode.UNDEFINED;
    private float customDfs = Global.DefaultGlobalData.getEachModeDfs()[1];
    private int customDfsMode = 1;
    private float customDss = Global.DefaultGlobalData.getEachModeDss()[1];
    private int customResolutionMode = 1;
    public int customSiopMode = 1;
    public int defaultCpuLevel;
    public int defaultGpuLevel;
    public int defaultSetBy = 0;
    public int drrAllowed = -1;
    private String eachModeDfs = "-1f,-1f,-1f,-1f";
    private String eachModeDss = "-1f,-1f,-1f,-1f";
    private String eachModeTargetShortSide = "-1,-1,-1,-1";
    public String gameSdkSettings;
    public String gfiPolicy;
    public String governorSettings;
    public String installedUserIds = BuildConfig.VERSION_NAME;
    public String ipmPolicy;
    public String launchBoostPolicy;
    public long pkgAddedTime = 0;
    public String pkgName;
    public String resumeBoostPolicy;
    public long serverPkgUpdatedTime = 0;
    public int shiftTemperature = -1000;
    public String siopModePolicy;
    public String sosPolicy;
    public String subscriberList;
    public String targetGroupName;
    public String tspPolicy;
    public long versionCode = -1;
    public String versionName;
    public int vrrMaxValue = -1;
    public int vrrMinValue = -1;
    public String wifiQosPolicy;

    public Package() {
    }

    public Package(String str) {
        this.pkgName = str;
    }

    public Package(String str, String str2) {
        this.pkgName = str;
        this.categoryCode = str2;
    }

    public Package(String str, String str2, Integer[] numArr) {
        this.pkgName = str;
        this.categoryCode = str2;
        setInstalledUserIds(numArr);
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public String getCategoryCode() {
        return this.categoryCode;
    }

    public void setCategoryCode(String str) {
        this.categoryCode = str;
    }

    public Integer[] getInstalledUserIds() {
        return TypeConverter.csvToIntegers(this.installedUserIds);
    }

    public void setInstalledUserIds(Integer[] numArr) {
        this.installedUserIds = TypeConverter.integersToCsv(numArr);
    }

    public int getDefaultSetBy() {
        return this.defaultSetBy;
    }

    public void setDefaultSetBy(int i) {
        this.defaultSetBy = i;
    }

    public long getPkgAddedTime() {
        return this.pkgAddedTime;
    }

    public void setPkgAddedTime(long j) {
        this.pkgAddedTime = j;
    }

    public long getServerPkgUpdatedTime() {
        return this.serverPkgUpdatedTime;
    }

    public void setServerPkgUpdatedTime(long j) {
        this.serverPkgUpdatedTime = j;
    }

    public String getEachModeTargetShortSide() {
        return this.eachModeTargetShortSide;
    }

    public int[] getEachModeTargetShortSideArray() {
        return TypeConverter.csvToInts(this.eachModeTargetShortSide);
    }

    public void setEachModeTargetShortSide(String str) {
        this.eachModeTargetShortSide = str;
    }

    public void setEachModeTargetShortSide(int[] iArr) {
        this.eachModeTargetShortSide = TypeConverter.intsToCsv(iArr);
    }

    public String getEachModeDss() {
        return this.eachModeDss;
    }

    public float[] getEachModeDssArray() {
        return TypeConverter.csvToFloats(this.eachModeDss);
    }

    public void setEachModeDss(String str) {
        this.eachModeDss = str;
    }

    public void setEachModeDss(float[] fArr) {
        this.eachModeDss = TypeConverter.floatsToCsv(fArr);
    }

    public float getCustomDss() {
        return this.customDss;
    }

    public void setCustomDss(float f) {
        this.customDss = ValidationUtil.getValidDss(f);
    }

    public int getCustomResolutionMode() {
        return this.customResolutionMode;
    }

    public void setCustomResolutionMode(int i) {
        float f = (float) i;
        if ((f >= 0.0f && f <= 3.0f) || i == 4) {
            this.customResolutionMode = i;
        }
    }

    public String getEachModeDfs() {
        return this.eachModeDfs;
    }

    public float[] getEachModeDfsArray() {
        return TypeConverter.csvToFloats(this.eachModeDfs);
    }

    public void setEachModeDfs(String str) {
        this.eachModeDfs = str;
    }

    public void setEachModeDfs(float[] fArr) {
        this.eachModeDfs = TypeConverter.floatsToCsv(fArr);
    }

    public float getCustomDfs() {
        return this.customDfs;
    }

    public void setCustomDfs(float f) {
        this.customDfs = ValidationUtil.getValidDfs(f);
    }

    public int getDefaultCpuLevel() {
        return this.defaultCpuLevel;
    }

    public void setDefaultCpuLevel(int i) {
        this.defaultCpuLevel = i;
    }

    public int getDefaultGpuLevel() {
        return this.defaultGpuLevel;
    }

    public void setDefaultGpuLevel(int i) {
        this.defaultGpuLevel = i;
    }

    public String getGovernorSettings() {
        return this.governorSettings;
    }

    public void setGovernorSettings(String str) {
        this.governorSettings = str;
    }

    public int getShiftTemperature() {
        return this.shiftTemperature;
    }

    public void setShiftTemperature(int i) {
        this.shiftTemperature = i;
    }

    public String getIpmPolicy() {
        return this.ipmPolicy;
    }

    public void setIpmPolicy(String str) {
        this.ipmPolicy = str;
    }

    public String getSiopModePolicy() {
        return this.siopModePolicy;
    }

    public void setSiopModePolicy(String str) {
        this.siopModePolicy = str;
    }

    public int getCustomSiopMode() {
        return this.customSiopMode;
    }

    public void setCustomSiopMode(int i) {
        this.customSiopMode = i;
    }

    public String getGameSdkSettings() {
        return this.gameSdkSettings;
    }

    public void setGameSdkSettings(String str) {
        this.gameSdkSettings = str;
    }

    public String getSosPolicy() {
        return this.sosPolicy;
    }

    public void setSosPolicy(String str) {
        this.sosPolicy = str;
    }

    public String getWifiQosPolicy() {
        return this.wifiQosPolicy;
    }

    public void setWifiQosPolicy(String str) {
        this.wifiQosPolicy = str;
    }

    public String getSubscriberList() {
        return this.subscriberList;
    }

    public void setSubscriberList(String str) {
        this.subscriberList = str;
    }

    public String getGfiPolicy() {
        return this.gfiPolicy;
    }

    public void setGfiPolicy(String str) {
        this.gfiPolicy = str;
    }

    public float getAppliedDss() {
        return this.appliedDss;
    }

    public void setAppliedDss(float f) {
        this.appliedDss = f;
    }

    public int getAppliedCpuLevel() {
        return this.appliedCpuLevel;
    }

    public void setAppliedCpuLevel(int i) {
        this.appliedCpuLevel = i;
    }

    public int getAppliedGpuLevel() {
        return this.appliedGpuLevel;
    }

    public void setAppliedGpuLevel(int i) {
        this.appliedGpuLevel = i;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String str) {
        this.versionName = str;
    }

    public long getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(long j) {
        this.versionCode = j;
    }

    public int getVrrMaxValue() {
        return this.vrrMaxValue;
    }

    public void setVrrMaxValue(int i) {
        this.vrrMaxValue = i;
    }

    public int getVrrMinValue() {
        return this.vrrMinValue;
    }

    public void setVrrMinValue(int i) {
        this.vrrMinValue = i;
    }

    public int getDrrAllowed() {
        return this.drrAllowed;
    }

    public void setDrrAllowed(int i) {
        this.drrAllowed = i;
    }

    public int getCustomDfsMode() {
        return this.customDfsMode;
    }

    public void setCustomDfsMode(int i) {
        float f = (float) i;
        if ((f >= 0.0f && f <= 3.0f) || i == 4) {
            this.customDfsMode = i;
        }
    }

    public String getResumeBoostPolicy() {
        return this.resumeBoostPolicy;
    }

    public void setResumeBoostPolicy(String str) {
        this.resumeBoostPolicy = str;
    }

    public String getLaunchBoostPolicy() {
        return this.launchBoostPolicy;
    }

    public void setLaunchBoostPolicy(String str) {
        this.launchBoostPolicy = str;
    }

    public String getTspPolicy() {
        return this.tspPolicy;
    }

    public void setTspPolicy(String str) {
        this.tspPolicy = str;
    }

    public String getTargetGroupName() {
        return this.targetGroupName;
    }

    public void setTargetGroupName(String str) {
        this.targetGroupName = str;
    }

    public static class PkgNameAndAppliedDss {
        public float appliedDss;
        public String pkgName;

        public PkgNameAndAppliedDss(String str, float f) {
            this.pkgName = str;
            this.appliedDss = f;
        }
    }

    public static class PkgNameAndIpmPolicy {
        public String ipmPolicy;
        public String pkgName;

        public PkgNameAndIpmPolicy(String str, String str2) {
            this.pkgName = str;
            this.ipmPolicy = str2;
        }
    }

    public static class PkgNameAndAppliedCpuGpuLevel {
        public int appliedCpuLevel;
        public int appliedGpuLevel;
        public String pkgName;

        public PkgNameAndAppliedCpuGpuLevel(String str, int i, int i2) {
            this.pkgName = str;
            this.appliedCpuLevel = i;
            this.appliedGpuLevel = i2;
        }
    }

    public static class PkgNameAndCategoryCode {
        public String categoryCode;
        public String pkgName;

        public PkgNameAndCategoryCode(String str, String str2) {
            this.pkgName = str;
            this.categoryCode = str2;
        }
    }

    public static class PkgNameAndVersionInfo {
        public String pkgName;
        public long versionCode;
        public String versionName;

        public PkgNameAndVersionInfo(String str, String str2, long j) {
            this.pkgName = str;
            this.versionName = str2;
            this.versionCode = j;
        }
    }

    public static class PkgNameAndDefaultCpuLevel {
        public int defaultCpuLevel;
        public String pkgName;

        public PkgNameAndDefaultCpuLevel(String str, int i) {
            this.pkgName = str;
            this.defaultCpuLevel = i;
        }
    }

    public static class PkgNameAndDefaultGpuLevel {
        public int defaultGpuLevel;
        public String pkgName;

        public PkgNameAndDefaultGpuLevel(String str, int i) {
            this.pkgName = str;
            this.defaultGpuLevel = i;
        }
    }

    public static class PkgNameAndShiftTemperature {
        public String pkgName;
        public int shiftTemperature;

        public PkgNameAndShiftTemperature(String str, int i) {
            this.pkgName = str;
            this.shiftTemperature = i;
        }
    }

    public static class PkgNameAndSiopModePolicy {
        public String pkgName;
        public String siopModePolicy;

        public PkgNameAndSiopModePolicy(String str, String str2) {
            this.pkgName = str;
            this.siopModePolicy = str2;
        }
    }

    public static class PkgNameAndCustomDfs {
        public float customDfs;
        public String pkgName;

        public PkgNameAndCustomDfs(String str, float f) {
            this.pkgName = str;
            this.customDfs = f;
        }
    }

    public static class PkgNameAndVrrMaxValue {
        public String pkgName;
        public int vrrMaxValue;

        public PkgNameAndVrrMaxValue(String str, int i) {
            this.pkgName = str;
            this.vrrMaxValue = i;
        }
    }

    public static class PkgNameAndVrrMinValue {
        public String pkgName;
        public int vrrMinValue;

        public PkgNameAndVrrMinValue(String str, int i) {
            this.pkgName = str;
            this.vrrMinValue = i;
        }
    }

    public static class PkgNameAndTspPolicy {
        public String pkgName;
        public String tspPolicy;

        public PkgNameAndTspPolicy(String str, String str2) {
            this.pkgName = str;
            this.tspPolicy = str2;
        }
    }

    public static class PkgNameAndTargetGroupName {
        public String pkgName;
        public String targetGroupName;

        public PkgNameAndTargetGroupName(String str, String str2) {
            this.pkgName = str;
            this.targetGroupName = str2;
        }
    }
}
