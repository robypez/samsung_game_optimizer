package com.samsung.android.game.gos.value;

import android.os.Build;
import androidx.collection.ArraySet;
import java.util.Arrays;
import java.util.Set;

public class Constants {
    public static final int ALARM_REQUEST_CODE = 1000;
    public static final int CUSTOM_NO_SETTING_INT = -1000;
    public static final int DEFAULT_SHIFT_TEMPERATURE_UNDEFINED = -1000;
    public static final float DFS_MAX = 120.0f;
    public static final float DFS_MIN = 1.0f;
    public static final float DSS_MAX = 100.0f;
    public static final float DSS_MIN = 1.0f;
    public static final int EACH_MODE_DEFAULT = 1;
    public static final int FALSE_MARK = 0;
    public static final int IPM_CPU_BOTTOM_FREQ_DEFAULT = 0;
    public static final int IPM_MAX = 3;
    public static final int IPM_MIN = -1;
    public static final int IPM_TARGET_POWER_DEFAULT = -1;
    public static final int IPM_TARGET_TEMP_DEFAULT = 480;
    public static final int JSON_SIZE_LIMIT_MAX = 409600;
    public static final float LEVELED_MODE_MAX = 3.0f;
    public static final float LEVELED_MODE_MIN = 0.0f;
    public static final float MODE_MAX = 3.0f;
    public static final float MODE_MIN = 0.0f;
    public static final String MULTI_RESOLUTION = "WQHD,FHD,HD";
    public static final int ON_SERVER_CONNECTION_FAIL_ALARM_CODE = 1001;
    public static final String PACKAGE_NAME_GAME_HOME = "com.samsung.android.game.gamehome";
    public static final String PACKAGE_NAME_GAME_PLUGINS = "com.samsung.android.game.gamelab";
    public static final String PACKAGE_NAME_GAME_SERVICE = "com.samsung.android.game.gos";
    public static final String PACKAGE_NAME_GAME_TOOLS = "com.samsung.android.game.gametools";
    public static final String PACKAGE_NAME_SAMSUNG_APPS = "com.sec.android.app.samsungapps";
    public static final String PACKAGE_NAME_SAMSUNG_VR = "com.samsung.vrvideo";
    public static final int PERFORMANCE_CPU_LEVEL_METRIC = 2;
    public static final String RESOLUTION_TYPE_DSS = "dss";
    public static final String RESOLUTION_TYPE_TSS = "tss";
    public static final int SAVE_POWER_CPU_LEVEL = -2;
    public static final long SEC_GAME_FAMILY_USAGE_COLLECTING_MIN_TIME = 2000;
    public static final int TRUE_MARK = 1;
    public static final long UPDATE_CHECK_INTERVAL_30MINUTE = 1800000;
    public static final long UPDATE_CHECK_INTERVAL_ONE_HOUR = 3600000;
    public static final long UPDATE_INTERVAL_10DAYS = 864000000;
    public static final long UPDATE_INTERVAL_1DAY = 86400000;
    public static final long UPDATE_INTERVAL_2DAYS = 172800000;
    public static final long UPDATE_INTERVAL_WIFI = 86400000;
    public static final long USAGE_COLLECTING_MIN_TIME = 5000;
    public static final int VALUE_ARRAY_LENGTH = 4;

    public enum AlarmIntentType {
        UPDATE_CHECK,
        ON_FAIL
    }

    public enum BoostType {
        Resume,
        Launch
    }

    private Constants() {
    }

    public static final class IpmFlags {
        public static final int ONLY_CAPTURE = 4;
        public static final int SUPERTRAIN = 0;
        public static final int VERBOSE = 1;

        private IpmFlags() {
        }
    }

    public static final class IpmMode {
        public static final int CRITICAL = 4;
        public static final int CUSTOM = 3;
        public static final int HIGH = 1;
        public static final int LOW = 0;
        public static final int MID = 5;
        public static final int ULTRA = 2;

        private IpmMode() {
        }
    }

    public static final class RingLog {
        private RingLog() {
        }

        public static final class Aggregation {
            public static final int MAX = 4;
            public static final int MEAN = 0;
            public static final int MEDIAN = 1;
            public static final int MIN = 3;
            public static final int MODE = 2;
            public static final int SUM = 5;

            private Aggregation() {
            }
        }

        public static final class Parameter {
            public static final String BATTERY = "battery";
            public static final String BATTERY_PREDICTION = "batt_prediction";
            public static final String BATTERY_PREDICTION_BENEFIT = "batt_prediction_benefit";
            public static final String BATTERY_PREDICTION_BENEFIT_LOW = "batt_prediction_benefit_low";
            public static final String BATTERY_PREDICTION_BENEFIT_PERCENT = "batt_prediction_benefit_percent";
            public static final String BATTERY_PREDICTION_BENEFIT_PERCENT_LOW = "batt_prediction_benefit_percent_low";
            public static final String CPU_F = "cpu_f";
            public static final String CPU_F2 = "cpu_f2";
            public static final String CPU_F3 = "cpu_f3";
            public static final String CPU_LOAD = "cpu_load";
            public static final String CPU_LOAD_LABEL = "cpu_load_label";
            public static final String CPU_LOAD_TOTAL = "cpu_load_total";
            public static final String CPU_MIN_SET_F = "cpu_min_set_f";
            public static final String CPU_MIN_WANTED_F = "cpu_min_wanted_f";
            public static final String CPU_SET_F = "cpu_set_f";
            public static final String CPU_WANTED_F = "cpu_wanted_f";
            public static final String FPS = "fps";
            public static final String FPS_BENEFIT_PERCENT = "fps_benefit_percent";
            public static final String FPS_BENEFIT_PERCENT_LOW = "fps_benefit_percent_low";
            public static final String GPU_F = "gpu_f";
            public static final String GPU_LOAD = "gpu_load";
            public static final String GPU_LOAD_LABEL = "gpu_load_label";
            public static final String GPU_MIN_SET_F = "gpu_min_set_f";
            public static final String GPU_MIN_WANTED_F = "gpu_min_wanted_f";
            public static final String GPU_SET_F = "gpu_set_f";
            public static final String GPU_WANTED_F = "gpu_wanted_f";
            public static final String MAX_GUESS_FPS = "fps_max_guess";
            public static final String MEMORY = "memory";
            public static final String ML_EXPLORATION = "exploration";
            public static final String ML_RANDOM = "random";
            public static final String POWER = "power";
            public static final String REFRESH_RATE = "refresh_rate";
            public static final String TEMP_AP = "temp_ap";
            public static final String TEMP_LRPST = "temp_lrpst";
            public static final String TEMP_PST = "temp_pst";
            public static final String TFPS = "tfps";

            private Parameter() {
            }
        }

        public static final class Flags {
            public static final int ALLOW_ML_OFF = 4194304;
            public static final int CAPTURING = 1024;
            public static final int CFM_MODE0 = 32;
            public static final int CFM_MODE1 = 64;
            public static final int CFM_MODE2 = 128;
            public static final int CFM_MODE3 = 256;
            public static final int DYNAMIC_DECISIONS = 1048576;
            public static final int ISNOT_THERMAL_CONTROL = 524288;
            public static final int IS_CONFIG_CH = 16;
            public static final int IS_ONLYCAPTURE = 65536;
            public static final int IS_POWER_SAVE = 262144;
            public static final int IS_QC = 4;
            public static final int IS_QC_FAST_SYSFS = 32768;
            public static final int IS_SF_FENCES = 131072;
            public static final int IS_SLSI = 8;
            public static final int LOGLEVEL = 2048;
            public static final int LOGLEVEL2 = 4096;
            public static final int RECORDING = 512;
            public static final int SHADOW = 16384;
            public static final int SUPERTRAIN = 8192;
            public static final int TABLES_READY = 2;
            public static final int USING_LRPST = 2097152;
            public static final int VALID = 1;

            private Flags() {
            }
        }
    }

    public static final class V4FeatureFlag {
        public static final String ALLOW_MORE_HEAT = "allow_more_heat";
        public static final String AUTO_CONTROL = "auto_control";
        public static final String BOOST_TOUCH = "boost_touch";
        public static final String CLEAR_BG_PROCESS = "clear_bg_process";
        public static final String DFS = "dfs";
        public static final String EXTERNAL_SDK = "external_sdk";
        public static final String GALLERY_CMH_STOP = "gallery_cmh_stop";
        public static final String GFI = "gfi";
        public static final String GOVERNOR_SETTINGS = "governor_settings";
        public static final String LAUNCH_BOOST = "launch_boost";
        public static final String LIMIT_BG_NETWORK = "limit_bg_network";
        public static final String MDNIE = "mdnie";
        public static final String MD_SWITCH_WIFI = "md_switch_wifi";
        public static final String RENDER_THREAD_AFFINITY = "render_thread_affinity";
        public static final String RESOLUTION = "resolution";
        public static final String RESUME_BOOST = "resume_boost";
        public static final String RINGLOG = "ringlog";
        public static final String SIOP_MODE = "siop_mode";
        public static final String SPA = "ipm";
        public static final String TSP = "tsp";
        public static final Set<String> V4_FEATURE_FLAG_NAMES = new ArraySet(Arrays.asList(new String[]{RESOLUTION, DFS, RENDER_THREAD_AFFINITY, MDNIE, BOOST_TOUCH, "volume_control", GALLERY_CMH_STOP, CLEAR_BG_PROCESS, "siop_mode", GOVERNOR_SETTINGS, "ipm", EXTERNAL_SDK, RESUME_BOOST, LAUNCH_BOOST, WIFI_QOS, "limit_bg_network", GFI, VRR, TSP, AUTO_CONTROL, MD_SWITCH_WIFI, ALLOW_MORE_HEAT, RINGLOG}));
        public static final String VOLUME_CONTROL = "volume_control";
        public static final String VRR = "vrr";
        public static final String WIFI_QOS = "wifi_qos";

        private V4FeatureFlag() {
        }
    }

    public static final class VrrValues {
        public static final int VRR_120HZ = 120;
        public static final int VRR_48HZ = 48;
        public static final int VRR_60HZ = 60;
        public static final int VRR_96HZ = 96;
        public static final int VRR_MIN_DEFAULT = 48;

        private VrrValues() {
        }

        public static int getVrrMaxDefault() {
            return Build.VERSION.SDK_INT > 29 ? 120 : 60;
        }
    }

    public static final class CategoryCode {
        public static final String GAME = "game";
        public static final String MANAGED_APP = "managed_app";
        public static final String NON_GAME = "non-game";
        public static final String SEC_GAME_FAMILY = "sec_game_family";
        public static final String UNDEFINED = "undefined";
        public static final String UNKNOWN = "unknown";
        public static final String VR_GAME = "vr_game";

        private CategoryCode() {
        }
    }

    public static final class Mode {
        public static final int CUSTOM = 4;
        public static final int EXTREME_SAVING = 3;
        public static final int POWER_SAVING = 2;
        public static final int STANDARD = 1;
        public static final int UNMANAGED = 0;

        private Mode() {
        }
    }

    public static final class UpdateType {
        public static final int EMPTY = 4;
        public static final int GAME_INSTALLED = 1;
        public static final int GAME_LAUNCHED = 0;
        public static final int GAME_UPDATED = 2;
        public static final int GOS_UPDATED = 3;

        private UpdateType() {
        }
    }

    public static final class SetBy {
        public static final int DEVICE_DEFAULT = 0;
        public static final int ENGINE = 1;
        public static final int SERVER = 2;
        public static final int USER = 3;

        private SetBy() {
        }
    }

    public enum IntentType {
        UNKNOWN(-1),
        BOOT_COMPLETED(0),
        MY_PACKAGE_REPLACED(1),
        WIFI_CONNECTED(3),
        ON_ALARM(4),
        UPDATE_BUTTON(6),
        DESKTOP_MODE_CHANGED(9),
        ON_SERVER_CONNECTION_FAIL_ALARM(10),
        MAKE_DATA_READY(200),
        INIT_GOS(201),
        TEST_GPP_TEMPERATURE_REACTOR(300),
        STOP_COMMAND(100),
        JSON_COMMAND_TEST(2000);
        
        private int val;

        private IntentType(int i) {
            this.val = i;
        }

        public int val() {
            return this.val;
        }

        public static IntentType valueOf(int i) {
            IntentType intentType = UNKNOWN;
            IntentType intentType2 = BOOT_COMPLETED;
            if (i != intentType2.val) {
                intentType2 = MY_PACKAGE_REPLACED;
                if (i != intentType2.val) {
                    intentType2 = WIFI_CONNECTED;
                    if (i != intentType2.val) {
                        intentType2 = ON_ALARM;
                        if (i != intentType2.val) {
                            intentType2 = ON_SERVER_CONNECTION_FAIL_ALARM;
                            if (i != intentType2.val) {
                                intentType2 = UPDATE_BUTTON;
                                if (i != intentType2.val) {
                                    intentType2 = DESKTOP_MODE_CHANGED;
                                    if (i != intentType2.val) {
                                        intentType2 = MAKE_DATA_READY;
                                        if (i != intentType2.val) {
                                            intentType2 = INIT_GOS;
                                            if (i != intentType2.val) {
                                                intentType2 = TEST_GPP_TEMPERATURE_REACTOR;
                                                if (i != intentType2.val) {
                                                    intentType2 = STOP_COMMAND;
                                                    if (i != intentType2.val) {
                                                        intentType2 = JSON_COMMAND_TEST;
                                                        if (i != intentType2.val) {
                                                            return intentType;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return intentType2;
        }
    }

    public enum PackageIntentType {
        UNKNOWN(-1),
        INSTALLED(0),
        REMOVED(1),
        INSTALL_STARTED(2),
        ENABLED(4),
        DISABLED(5),
        REPLACED(8);
        
        private int val;

        private PackageIntentType(int i) {
            this.val = i;
        }

        public int val() {
            return this.val;
        }

        public static PackageIntentType valueOf(int i) {
            PackageIntentType packageIntentType = UNKNOWN;
            PackageIntentType packageIntentType2 = INSTALLED;
            if (i != packageIntentType2.val) {
                packageIntentType2 = REMOVED;
                if (i != packageIntentType2.val) {
                    packageIntentType2 = INSTALL_STARTED;
                    if (i != packageIntentType2.val) {
                        packageIntentType2 = ENABLED;
                        if (i != packageIntentType2.val) {
                            packageIntentType2 = DISABLED;
                            if (i != packageIntentType2.val) {
                                packageIntentType2 = REPLACED;
                                if (i != packageIntentType2.val) {
                                    return packageIntentType;
                                }
                            }
                        }
                    }
                }
            }
            return packageIntentType2;
        }
    }

    public static final class GameIntentType {
        public static final int BATTERY_LEVEL_CHANGED = 6;
        public static final int FOCUS_IN = 0;
        public static final int FOCUS_OUT = 1;
        public static final int GOS_ENABLED = 19;
        public static final int MEDIA_SERVER_REBOOTED = 9;
        public static final int PACKAGE_CHANGED = 15;
        public static final int RESOLUTION_CHANGED = 5;
        public static final int UNKNOWN = -1;
        public static final int USER_REMOVED = 17;
        public static final int USER_SWITCHED = 18;
        public static final int VRR_SETTING_CHANGED = 14;
        public static final int WIFI_CONNECTED = 16;

        private GameIntentType() {
        }
    }

    public static final class ChipsetType {
        public static final String MSM8996 = "msm8996";
        public static final String universal7420 = "universal7420";
        public static final String universal8890 = "universal8890";
        public static final String universal8895 = "universal8895";
        public static final String universal9810 = "universal9810";

        private ChipsetType() {
        }
    }

    public static final class SiopMode {
        public static final int DEFAULT = 1;
        public static final int HIGH_PERFORMANCE = 1;
        public static final int MAX = 1;
        public static final int MIN = -1;
        public static final int NORMAL_PERFORMANCE = 0;
        public static final int OFF = -1000;
        public static final int SAVE_POWER = -1;

        private SiopMode() {
        }
    }

    public static final class MultiUser {
        public static final int DEFAULT_USER = 0;
        public static final int UNKNOWN = -1;

        private MultiUser() {
        }
    }

    public static final class SecureFolderUserId {
        public static final int SECURE_FOLDER_MAX_USER_ID = 160;
        public static final int SECURE_FOLDER_MIN_USER_ID = 150;

        private SecureFolderUserId() {
        }
    }

    public static final class CallTrigger {
        public static final String ALARM = "alarm";
        public static final String BOOT_COMPLETED = "boot_completed";
        public static final String GOS_ENABLED = "gos_enabled";
        public static final String MAKE_DATA_READY = "make_data_ready";
        public static final String PKG_ENABLED = "pkg_enabled";
        public static final String PKG_INSTALLED = "pkg_installed";
        public static final String PKG_INSTALL_STARTED = "pkg_install_started";
        public static final String PKG_REMOVED = "pkg_removed";
        public static final String TEST = "test";
        public static final String USER_SWITCHED = "user_switched";

        private CallTrigger() {
        }
    }
}
