package com.samsung.android.game.gos.data.model;

import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Global {
    public boolean automaticUpdate = true;
    public int clearBGLruNum = 1;
    public String clearBGSurviveAppFromServer;
    public float customDfs;
    public float customDss;
    public boolean dataReady = false;
    public int defaultCpuLevel;
    public int defaultGpuLevel;
    public String deviceName = AppVariable.getOriginalDeviceName();
    public int dfsMode;
    public String dmaId;
    public int drrAllowed = -1;
    public String eachModeDfs;
    public String eachModeDss;
    public String eachModeTargetShortSide;
    public long fullyUpdateTime;
    public String gfiPolicy;
    public float gmsVersion = -1.0f;
    public boolean gosSelfUpdateStatus = false;
    public String governorSettings;
    public int id;
    public boolean initialized;
    public long ipmCpuBottomFreq;
    public String ipmFlag;
    public int ipmMode;
    public String ipmPolicy;
    public int ipmTargetPower;
    public int ipmTargetTemperature;
    public String ipmTrainingVersion;
    public long ipmUpdateTime;
    public String launchBoostPolicy;
    @Deprecated
    public String loggingPolicy;
    public int prevSiopModeByUser = -1000;
    public boolean registeredDevice = false;
    public int resolutionMode;
    public String resumeBoostPolicy;
    public String ringlogPolicy;
    public boolean showLogcat = false;
    public int siopMode;
    public String siopModePolicy;
    public String sosPolicyKeyCsv;
    public int targetServer = 2;
    public String tspPolicy;
    public long updateTime;
    public String uuid;
    public int vrrMaxValue = Constants.VrrValues.getVrrMaxDefault();
    public int vrrMinValue = 48;
    public String wifiQosPolicy;

    public static Global getDefaultGlobal() {
        Global global = new Global();
        global.id = 1;
        global.initialized = false;
        global.updateTime = 0;
        global.fullyUpdateTime = 0;
        global.defaultCpuLevel = 0;
        global.defaultGpuLevel = 0;
        global.eachModeDss = TypeConverter.floatsToCsv(DefaultGlobalData.eachModeDss);
        global.eachModeDfs = TypeConverter.floatsToCsv(DefaultGlobalData.eachModeDfs);
        global.resolutionMode = 1;
        global.dfsMode = 1;
        global.customDss = DefaultGlobalData.eachModeDss[1];
        global.customDfs = DefaultGlobalData.eachModeDfs[1];
        global.ipmMode = 2;
        global.ipmTargetPower = -1;
        global.ipmTargetTemperature = DefaultGlobalData.ipmTargetTemperature;
        global.ipmUpdateTime = 0;
        global.ipmTrainingVersion = "0.0";
        global.ipmCpuBottomFreq = 0;
        global.ipmFlag = TypeConverter.booleansToCsv(DefaultGlobalData.ipmFlag);
        global.siopMode = 1;
        global.eachModeTargetShortSide = TypeConverter.intsToCsv(DefaultGlobalData.eachModeTargetShortSide);
        global.governorSettings = DefaultGlobalData.governorSettings;
        global.loggingPolicy = DefaultGlobalData.loggingPolicy;
        global.launchBoostPolicy = DefaultGlobalData.launchBoostPolicy;
        global.wifiQosPolicy = DefaultGlobalData.wifiQosPolicy;
        global.gfiPolicy = DefaultGlobalData.gfiPolicy;
        global.tspPolicy = DefaultGlobalData.tspPolicy;
        global.ringlogPolicy = null;
        global.uuid = BuildConfig.VERSION_NAME;
        global.registeredDevice = false;
        global.gmsVersion = -1.0f;
        global.showLogcat = false;
        global.automaticUpdate = true;
        global.targetServer = 2;
        global.deviceName = AppVariable.getOriginalDeviceName();
        global.prevSiopModeByUser = -1000;
        global.gosSelfUpdateStatus = false;
        global.dataReady = false;
        return global;
    }

    public static class DefaultGlobalData {
        private static final List<String> ORIGINAL_ENABLED_BY_SERVER_FEATURE_LIST = new ArrayList(Arrays.asList(new String[]{Constants.V4FeatureFlag.RESOLUTION, Constants.V4FeatureFlag.DFS, Constants.V4FeatureFlag.BOOST_TOUCH, Constants.V4FeatureFlag.GOVERNOR_SETTINGS, Constants.V4FeatureFlag.EXTERNAL_SDK, Constants.V4FeatureFlag.RESUME_BOOST, Constants.V4FeatureFlag.LAUNCH_BOOST, Constants.V4FeatureFlag.TSP, Constants.V4FeatureFlag.AUTO_CONTROL, "siop_mode", Constants.V4FeatureFlag.VRR, Constants.V4FeatureFlag.RINGLOG}));
        private static final List<String> ORIGINAL_FORCED_FEATURE_LIST = new ArrayList(Arrays.asList(new String[]{Constants.V4FeatureFlag.ALLOW_MORE_HEAT}));
        private static final int defaultCpuLevel = 0;
        private static final int defaultGpuLevel = 0;
        private static final int dfsMode = 1;
        /* access modifiers changed from: private */
        public static final float[] eachModeDfs = {60.0f, 60.0f, 60.0f, 30.0f};
        /* access modifiers changed from: private */
        public static final float[] eachModeDss = {100.0f, 75.0f, 50.0f, 50.0f};
        /* access modifiers changed from: private */
        public static int[] eachModeTargetShortSide = null;
        private static final long fullyUpdateTime = 0;
        /* access modifiers changed from: private */
        public static String gfiPolicy = null;
        /* access modifiers changed from: private */
        public static String governorSettings = null;
        private static final boolean initialized = false;
        private static final long ipmCpuBottomFreq = 0;
        private static final int ipmDefaultTemperature;
        /* access modifiers changed from: private */
        public static final boolean[] ipmFlag = {false, false, false, false, false};
        private static final int ipmMode = 2;
        private static final int ipmTargetPower = -1;
        /* access modifiers changed from: private */
        public static final int ipmTargetTemperature;
        private static final String ipmTrainingVersion = "0.0";
        private static final long ipmUpdateTime = 0;
        /* access modifiers changed from: private */
        public static String launchBoostPolicy = null;
        /* access modifiers changed from: private */
        public static String loggingPolicy = null;
        private static final int resolutionMode = 1;
        private static final int siopMode = 1;
        /* access modifiers changed from: private */
        public static String tspPolicy = null;
        private static final long updateTime = 0;
        /* access modifiers changed from: private */
        public static String wifiQosPolicy = null;

        public static int getDefaultCpuLevel() {
            return 0;
        }

        public static int getDefaultGpuLevel() {
            return 0;
        }

        public static int getIpmMode() {
            return 2;
        }

        public static int getIpmTargetPower() {
            return -1;
        }

        public static int getSiopMode() {
            return 1;
        }

        static {
            int ipmDefaultTemperature2 = getIpmDefaultTemperature();
            ipmDefaultTemperature = ipmDefaultTemperature2;
            ipmTargetTemperature = ipmDefaultTemperature2;
        }

        public static int getIpmDefaultTemperature() {
            if (AppVariable.getOriginalDeviceName().contains("dream")) {
                if (AppVariable.getOriginalDeviceName().contains("q")) {
                }
                return 520;
            } else if (!AppVariable.getOriginalDeviceName().contains("star")) {
                return Constants.IPM_TARGET_TEMP_DEFAULT;
            } else {
                if (AppVariable.getOriginalDeviceName().contains("q")) {
                }
                return 520;
            }
        }

        public static float[] getEachModeDss() {
            return eachModeDss;
        }

        public static float[] getEachModeDfs() {
            return eachModeDfs;
        }

        public static int[] getEachModeTargetShortSide() {
            return eachModeTargetShortSide;
        }

        public static int getIpmTargetTemperature() {
            return ipmTargetTemperature;
        }

        public static boolean isEnabledByDefault(String str) {
            return ORIGINAL_ENABLED_BY_SERVER_FEATURE_LIST.contains(str);
        }

        public static boolean isForcedByDefault(String str) {
            return ORIGINAL_FORCED_FEATURE_LIST.contains(str);
        }
    }

    public static class IdAndUuid {
        public int id = 1;
        public String uuid;

        public IdAndUuid(String str) {
            this.uuid = str;
        }
    }

    public static class IdAndDataReady {
        public boolean dataReady;
        public int id = 1;

        public IdAndDataReady(boolean z) {
            this.dataReady = z;
        }
    }

    public static class IdAndInitialized {
        public int id = 1;
        public boolean initialized;

        public IdAndInitialized(boolean z) {
            this.initialized = z;
        }
    }

    public static class IdAndUpdateTime {
        public int id = 1;
        public long updateTime;

        public IdAndUpdateTime(long j) {
            this.updateTime = j;
        }
    }

    public static class IdAndFullyUpdateTime {
        public long fullyUpdateTime;
        public int id = 1;

        public IdAndFullyUpdateTime(long j) {
            this.fullyUpdateTime = j;
        }
    }

    public static class IdAndGmsVersion {
        public float gmsVersion;
        public int id = 1;

        public IdAndGmsVersion(float f) {
            this.gmsVersion = f;
        }
    }

    public static class IdAndTargetServer {
        public int id = 1;
        public int targetServer;

        public IdAndTargetServer(int i) {
            this.targetServer = i;
        }
    }

    public static class IdAndRegisteredDevice {
        public int id = 1;
        public boolean registeredDevice;

        public IdAndRegisteredDevice(boolean z) {
            this.registeredDevice = z;
        }
    }

    public static class IdAndDeviceName {
        public String deviceName;
        public int id = 1;

        public IdAndDeviceName(String str) {
            this.deviceName = str;
        }
    }

    public static class IdAndClearBGLruNum {
        public int clearBGLruNum;
        public int id = 1;

        public IdAndClearBGLruNum(int i) {
            this.clearBGLruNum = i;
        }
    }

    public static class IdAndClearBGSurviveAppFromServer {
        public String clearBGSurviveAppFromServer;
        public int id = 1;

        public IdAndClearBGSurviveAppFromServer(String str) {
            this.clearBGSurviveAppFromServer = str;
        }
    }

    public static class IdAndPrevSiopModeByUser {
        public int id = 1;
        public int prevSiopModeByUser;

        public IdAndPrevSiopModeByUser(int i) {
            this.prevSiopModeByUser = i;
        }
    }

    public static class IdAndSosPolicyKeyCsv {
        public int id = 1;
        public String sosPolicyKeyCsv;

        public IdAndSosPolicyKeyCsv(String str) {
            this.sosPolicyKeyCsv = str;
        }
    }

    public static class IdAndAutomaticUpdate {
        public boolean automaticUpdate;
        public int id = 1;

        public IdAndAutomaticUpdate(boolean z) {
            this.automaticUpdate = z;
        }
    }

    public static class IdAndGosSelfUpdateStatus {
        public boolean gosSelfUpdateStatus;
        public int id = 1;

        public IdAndGosSelfUpdateStatus(boolean z) {
            this.gosSelfUpdateStatus = z;
        }
    }

    public static class IdAndUseGalaxyAppsStgServer {
        public int id = 1;
        public int useGalaxyAppsStgServer;

        public IdAndUseGalaxyAppsStgServer(int i) {
            this.useGalaxyAppsStgServer = i;
        }
    }

    public static class IdAndShowLogcat {
        public int id = 1;
        public boolean showLogcat;

        public IdAndShowLogcat(boolean z) {
            this.showLogcat = z;
        }
    }

    public static class IdAndDmaId {
        public String dmaId;
        public int id = 1;

        public IdAndDmaId(String str) {
            this.dmaId = str;
        }
    }

    public static class IdAndWifiQosPolicy {
        public int id = 1;
        public String wifiQosPolicy;

        public IdAndWifiQosPolicy(String str) {
            this.wifiQosPolicy = str;
        }
    }

    public static class IdAndLaunchBoostPolicy {
        public int id = 1;
        public String launchBoostPolicy;

        public IdAndLaunchBoostPolicy(String str) {
            this.launchBoostPolicy = str;
        }
    }

    public static class IdAndSiopModePolicy {
        public int id = 1;
        public String siopModePolicy;

        public IdAndSiopModePolicy(String str) {
            this.siopModePolicy = str;
        }
    }

    public static class IdAndGfiPolicy {
        public String gfiPolicy;
        public int id = 1;

        public IdAndGfiPolicy(String str) {
            this.gfiPolicy = str;
        }
    }

    public static class IdAndIpmPolicy {
        public int id = 1;
        public String ipmPolicy;

        public IdAndIpmPolicy(String str) {
            this.ipmPolicy = str;
        }
    }

    public static class IdAndLoggingPolicy {
        public int id = 1;
        public String loggingPolicy;

        public IdAndLoggingPolicy(String str) {
            this.loggingPolicy = str;
        }
    }

    public static class IdAndResumeBoostPolicy {
        public int id = 1;
        public String resumeBoostPolicy;

        public IdAndResumeBoostPolicy(String str) {
            this.resumeBoostPolicy = str;
        }
    }

    public static class IdAndShiftTemperature {
        public int id = 1;
        public int shiftTemperature;

        public IdAndShiftTemperature(int i) {
            this.shiftTemperature = i;
        }
    }

    public static class IdAndGovernorSettings {
        public String governorSettings;
        public int id = 1;

        public IdAndGovernorSettings(String str) {
            this.governorSettings = str;
        }
    }

    public static class IdAndEachModeTargetShortSide {
        public String eachModeTargetShortSide;
        public int id = 1;

        public IdAndEachModeTargetShortSide(String str) {
            this.eachModeTargetShortSide = str;
        }

        public IdAndEachModeTargetShortSide(int[] iArr) {
            this.eachModeTargetShortSide = TypeConverter.intsToCsv(iArr);
        }
    }

    public static class IdAndSiopMode {
        public int id = 1;
        public int siopMode;

        public IdAndSiopMode(int i) {
            this.siopMode = i;
        }
    }

    public static class IdAndIpmFlag {
        public int id = 1;
        public String ipmFlag;

        public IdAndIpmFlag(String str) {
            this.ipmFlag = str;
        }

        public IdAndIpmFlag(boolean[] zArr) {
            this.ipmFlag = TypeConverter.booleansToCsv(zArr);
        }
    }

    public static class IdAndIpmCpuBottomFreq {
        public int id = 1;
        public long ipmCpuBottomFreq;

        public IdAndIpmCpuBottomFreq(long j) {
            this.ipmCpuBottomFreq = j;
        }
    }

    public static class IdAndIpmTrainingVersion {
        public int id = 1;
        public String ipmTrainingVersion;

        public IdAndIpmTrainingVersion(String str) {
            this.ipmTrainingVersion = str;
        }
    }

    public static class IdAndIpmUpdateTime {
        public int id = 1;
        public long ipmUpdateTime;

        public IdAndIpmUpdateTime(long j) {
            this.ipmUpdateTime = j;
        }
    }

    public static class IdAndIpmTargetTemperature {
        public int id = 1;
        public int ipmTargetTemperature;

        public IdAndIpmTargetTemperature(int i) {
            this.ipmTargetTemperature = i;
        }
    }

    public static class IdAndIpmTargetPower {
        public int id = 1;
        public int ipmTargetPower;

        public IdAndIpmTargetPower(int i) {
            this.ipmTargetPower = i;
        }
    }

    public static class IdAndIpmMode {
        public int id = 1;
        public int ipmMode;

        public IdAndIpmMode(int i) {
            this.ipmMode = i;
        }
    }

    public static class IdAndEachModeDfs {
        public String eachModeDfs;
        public int id = 1;

        public IdAndEachModeDfs(String str) {
            this.eachModeDfs = str;
        }
    }

    public static class IdAndEachModeDss {
        public String eachModeDss;
        public int id = 1;

        public IdAndEachModeDss(String str) {
            this.eachModeDss = str;
        }
    }

    public static class IdAndDefaultCpuLevel {
        public int defaultCpuLevel;
        public int id = 1;

        public IdAndDefaultCpuLevel(int i) {
            this.defaultCpuLevel = i;
        }
    }

    public static class IdAndDefaultGpuLevel {
        public int defaultGpuLevel;
        public int id = 1;

        public IdAndDefaultGpuLevel(int i) {
            this.defaultGpuLevel = i;
        }
    }

    public static class IdAndDfsMode {
        public int dfsMode;
        public int id = 1;

        public IdAndDfsMode(int i) {
            this.dfsMode = i;
        }
    }

    public static class IdAndResolutionMode {
        public int id = 1;
        public int resolutionMode;

        public IdAndResolutionMode(int i) {
            this.resolutionMode = i;
        }
    }

    public static class IdAndTspPolicy {
        public int id = 1;
        public String tspPolicy;

        public IdAndTspPolicy(String str) {
            this.tspPolicy = str;
        }
    }

    public static class IdAndRinglogPolicy {
        public int id = 1;
        public String ringlogPolicy;

        public IdAndRinglogPolicy(String str) {
            this.ringlogPolicy = str;
        }
    }

    public static class IdAndVrr {
        public int id = 1;
        public int vrrMaxValue;
        public int vrrMinValue;

        public IdAndVrr(int i, int i2) {
            this.vrrMaxValue = i;
            this.vrrMinValue = i2;
        }
    }

    public static class IdAndDrrAllowed {
        public int drrAllowed;
        public int id = 1;

        public IdAndDrrAllowed(int i) {
            this.drrAllowed = i;
        }
    }
}
