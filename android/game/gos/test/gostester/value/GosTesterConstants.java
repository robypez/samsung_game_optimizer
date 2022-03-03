package com.samsung.android.game.gos.test.gostester.value;

import com.samsung.android.game.gos.value.Constants;

public class GosTesterConstants {
    public static final String TESTER_COMMAND_APPLY_GLOBAL_DATA = "applyGlobalData";
    public static final String TESTER_COMMAND_DELETE_IPM_TRAINING_DATA = "deleteIpmTrainingData";
    public static final String TESTER_COMMAND_GET_DEVICE_INFO = "getDeviceInfo";
    public static final String TESTER_COMMAND_GET_DEVICE_NAME = "getDeviceName";
    public static final String TESTER_COMMAND_GET_GAME_PKG_NAME_LIST = "getGamePkgNameList";
    public static final String TESTER_COMMAND_GET_GFI_PACKAGE_DATA = "getGfiData";
    public static final String TESTER_COMMAND_GET_GLOBAL_DATA = "getGlobalData";
    public static final String TESTER_COMMAND_GET_IPM_DATA = "getIpmData";
    public static final String TESTER_COMMAND_GET_PACKAGE_DATA = "getPackageData";
    public static final String TESTER_COMMAND_GET_RESUME_BOOST_DATA = "getResumeBoost";
    public static final String TESTER_COMMAND_GET_SERVER_GLOBAL_DATA = "getServerGlobalData";
    public static final String TESTER_COMMAND_GET_SYS_FILE_DATA = "getSysFileData";
    public static final String TESTER_COMMAND_GET_TARGET_SERVER = "getTargetServer";
    public static final String TESTER_COMMAND_ID = "tester_command_id";
    public static final String TESTER_COMMAND_IS_AUTOMATIC_SYNC = "isAutomaticSync";
    @Deprecated
    public static final String TESTER_COMMAND_IS_AUTOMATIC_UPDATE = "isAutomaticUpdate";
    public static final String TESTER_COMMAND_IS_USING_SERVER_DATA = "isUsingServerData";
    public static final String TESTER_COMMAND_MOVE_GOS_DB_TO_EXTERNAL = "moveGosDbToExternal";
    public static final String TESTER_COMMAND_PARAMETER_BOOLEAN = "parameter_boolean";
    public static final String TESTER_COMMAND_PARAMETER_FEATURE_FLAG = "parameter_feature_flag";
    public static final String TESTER_COMMAND_PARAMETER_FLOAT = "parameter_float";
    public static final String TESTER_COMMAND_PARAMETER_INT = "parameter_int";
    public static final String TESTER_COMMAND_PARAMETER_LONG = "parameter_long";
    public static final String TESTER_COMMAND_PARAMETER_PACKAGE = "parameter_package";
    public static final String TESTER_COMMAND_PARAMETER_STRING = "parameter_string";
    public static final String TESTER_COMMAND_PULL_IPM_TRAINING_DATA = "pullIpmTrainingData";
    public static final String TESTER_COMMAND_PUSH_IPM_TRAINING_DATA = "pushIpmTrainingData";
    public static final String TESTER_COMMAND_RESTORE_DATA = "restoreData";
    public static final String TESTER_COMMAND_SAVE_RINGLOG_DUMP_TO_EXTERNAL = "saveRinglogDumpToExternal";
    public static final String TESTER_COMMAND_SET_AUTOMATIC_SYNC = "setAutomaticSync";
    @Deprecated
    public static final String TESTER_COMMAND_SET_AUTOMATIC_UPDATE = "setAutomaticUpdate";
    public static final String TESTER_COMMAND_SET_DEVICE_NAME = "setDeviceName";
    public static final String TESTER_COMMAND_SET_ENABLE_FLAG_BY_USER = "setDefinedEnabledFeatureFlag";
    public static final String TESTER_COMMAND_SET_GFI_PACKAGE_DATA = "setGfiData";
    public static final String TESTER_COMMAND_SET_IPM_DATA = "setIpmData";
    public static final String TESTER_COMMAND_SET_IPM_GAME_POLICY = "setIpmGamePolicy";
    public static final String TESTER_COMMAND_SET_PACKAGE_DATA = "setPackageData";
    public static final String TESTER_COMMAND_SET_RESUME_BOOST_DATA = "setResumeBoost";
    public static final String TESTER_COMMAND_SET_SERVER_FEATURE_FLAG_POLICY = "setServerDefinedFeatureFlagPolicy";
    public static final String TESTER_COMMAND_SET_TARGET_SERVER = "setTargetServer";
    public static final String TESTER_COMMAND_SET_USING_SERVER_DATA = "setUsingServerData";
    public static final String TESTER_COMMAND_UPLOAD_COMBINATION_REPORT_DATA = "uploadCombinationReportData";

    public enum GlobalFeatureFlag {
        Resolution(Constants.V4FeatureFlag.RESOLUTION),
        Dfs(Constants.V4FeatureFlag.DFS),
        RenderThreadAffinity(Constants.V4FeatureFlag.RENDER_THREAD_AFFINITY),
        MdniBrightness(Constants.V4FeatureFlag.MDNIE),
        TouchBoost(Constants.V4FeatureFlag.BOOST_TOUCH),
        VolumeControl("volume_control"),
        CmhStop(Constants.V4FeatureFlag.GALLERY_CMH_STOP),
        ClearBgProcess(Constants.V4FeatureFlag.CLEAR_BG_PROCESS),
        SiopMode("siop_mode"),
        GovernorSettings(Constants.V4FeatureFlag.GOVERNOR_SETTINGS),
        Spa("ipm"),
        ExternalSdk(Constants.V4FeatureFlag.EXTERNAL_SDK),
        ResumeBoost(Constants.V4FeatureFlag.RESUME_BOOST),
        LaunchBoost(Constants.V4FeatureFlag.LAUNCH_BOOST),
        WifiQos(Constants.V4FeatureFlag.WIFI_QOS),
        LimitBGNetwork("limit_bg_network"),
        GFI(Constants.V4FeatureFlag.GFI),
        VRR(Constants.V4FeatureFlag.VRR),
        TSP(Constants.V4FeatureFlag.TSP),
        AutoControl(Constants.V4FeatureFlag.AUTO_CONTROL),
        MdSwitchWifiBlock(Constants.V4FeatureFlag.MD_SWITCH_WIFI),
        AllowMoreHeat(Constants.V4FeatureFlag.ALLOW_MORE_HEAT),
        Ringlog(Constants.V4FeatureFlag.RINGLOG);
        
        private String featureName;

        private GlobalFeatureFlag(String str) {
            this.featureName = str;
        }

        public String getFeatureName() {
            return this.featureName;
        }
    }

    public enum PackageFeatureFlag {
        Dss(Constants.V4FeatureFlag.RESOLUTION),
        Tss(Constants.V4FeatureFlag.RESOLUTION),
        Dfs(Constants.V4FeatureFlag.DFS),
        CpuLevel("siop_mode"),
        GpuLevel("siop_mode"),
        ShiftTemp("siop_mode"),
        LaunchMode("siop_mode"),
        ResumeBoost(Constants.V4FeatureFlag.RESUME_BOOST),
        LaunchBoost(Constants.V4FeatureFlag.LAUNCH_BOOST),
        VRR_MIN(Constants.V4FeatureFlag.VRR),
        VRR_MAX(Constants.V4FeatureFlag.VRR),
        TSP(Constants.V4FeatureFlag.TSP);
        
        private String featureName;

        private PackageFeatureFlag(String str) {
            this.featureName = str;
        }

        public String getFeatureName() {
            return this.featureName;
        }
    }
}
