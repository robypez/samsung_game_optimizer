package com.samsung.android.game.gos.value.jsoninterface;

public class GosInterface {
    private GosInterface() {
    }

    public static class Command {
        public static final String GET_GLOBAL_DATA = "get_global_data";
        public static final String GET_GPP_STATE = "get_gpp_state";
        public static final String GET_PACKAGE_DATA = "get_package_data";
        public static final String GET_PACKAGE_NAMES = "get_package_names";
        public static final String GET_REPORT = "get_report";
        public static final String PERF_DATA_GET_AVAILABLE_PARAMETERS = "perf_data_get_available_parameters";
        public static final String PERF_DATA_GET_AVAILABLE_SESSIONS = "perf_data_get_available_sessions";
        public static final String PERF_DATA_GET_SYSTEM_STATUS = "perf_data_get_system_status";
        public static final String PERF_DATA_REGISTER = "perf_data_register";
        public static final String PERF_DATA_SIMPLE_REQUEST = "perf_data_simple_request";
        public static final String SET_BLOCK_MD_WIFI_DATA = "set_block_md_wifi_data";
        public static final String SET_FEATURE_ACCESSIBILITY = "set_feature_accessibility";
        public static final String SET_FPS_VALUE = "set_fps_value";
        public static final String SET_GLOBAL_DATA = "set_global_data";
        public static final String SET_LIMIT_BG_NETWORK_DATA = "set_limit_bg_network_data";
        public static final String SET_LRU_NUM = "set_lru_num";
        public static final String SET_MONITORED_APPS = "set_monitored_apps";
        public static final String SET_PACKAGE_DATA = "set_package_data";
        public static final String SET_SURVIVE_LIST = "set_survive_list";
        public static final String STOP_PACKAGES = "stop_packages";
        public static final String SUBSCRIBE_EVENTS = "subscribe_events";

        private Command() {
        }
    }

    public static class KeyName {
        public static final String AGG_MODE = "agg_mode";
        public static final String ALLOW_MORE_HEAT_AVAILABLE = "allow_more_heat_available";
        public static final String APPLIED_LAUNCH_BOOST_DURATION = "applied_launch_boost_duration";
        public static final String APPLIED_RESUME_BOOST_DURATION = "applied_resume_boost_duration";
        public static final String AUTO_CONTROL_STATUS = "auto_control_status";
        public static final String AVAILABLE_CPU_LEVEL = "available_cpu_level";
        public static final String AVAILABLE_DSS = "available_dss";
        public static final String AVAILABLE_GPU_LEVEL = "available_gpu_level";
        public static final String BEGIN_REPORTING_TIME = "begin_reporting_time";
        public static final String BLOCK_MD_SWITCH_WIFI = "block_md_switch_wifi";
        public static final String CATEGORY_CODE = "category_code";
        public static final String CLEAR_BG_PROCESS_ENABLED = "clear_bg_process_enabled";
        public static final String CLEAR_BG_PROCESS_SUPPORTED = "clear_bg_process_supported";
        public static final String COMMENT = "comment";
        public static final String CUSTOM_DFS_MODE = "custom_dfs_mode";
        public static final String CUSTOM_DFS_VALUE = "custom_dfs_value";
        public static final String CUSTOM_DSS_VALUE = "custom_dss_value";
        public static final String CUSTOM_RESOLUTION_MODE = "custom_resolution_mode";
        public static final String CUSTOM_SIOP_MODE = "custom_siop_mode";
        public static final String DATA_END = "data_end";
        public static final String DATA_END_MS = "data_end_ms";
        public static final String DATA_READY = "data_ready";
        public static final String DATA_START = "data_start";
        public static final String DATA_START_MS = "data_start_ms";
        public static final String DEFAULT_CPU_LEVEL = "default_cpu_level";
        public static final String DEFAULT_DFS_VALUE = "default_dfs_value";
        public static final String DEFAULT_DSS_VALUE = "default_dss_value";
        public static final String DEFAULT_GPU_LEVEL = "default_gpu_level";
        public static final String DEFAULT_TARGET_SHORT_SIDE = "default_target_short_side";
        public static final String DEVICE_NAME = "device_name";
        public static final String EACH_MODE_DFS = "each_mode_dfs";
        public static final String EACH_MODE_DSS = "each_mode_dss";
        public static final String EACH_MODE_TARGET_SHORT_SIDE = "each_mode_target_short_side";
        public static final String EACH_SIOP_MODE_CPU_LEVEL = "each_siop_mode_cpu_level";
        public static final String EACH_SIOP_MODE_GPU_LEVEL = "each_siop_mode_gpu_level";
        public static final String END = "end";
        public static final String END_REPORTING_TIME = "end_reporting_time";
        public static final String EVENTS = "events";
        public static final String FEATURE_FLAGS = "feature_flags";
        public static final String FLAGS = "flags";
        public static final String GAME_BATTERY_CONSUMED = "game_battery_consumed";
        public static final String GAME_PLAY_TIME_BATTERY_ONLY = "game_play_time_battery_only";
        public static final String GAME_PLAY_TIME_TOTAL = "game_play_time_total";
        public static final String GRADE = "grade";
        public static final String GSON_COMPATIBLE_RESPONSE = "gson_compatible_response";
        public static final String INCLUDE_RAW_DATA = "include_raw_data";
        public static final String INDEX = "index";
        public static final String INTEL_MODE = "intel_mode";
        public static final String INTENT_ACTION_NAME = "intent_action_name";
        public static final String IPM = "ipm";
        public static final String IPM_FLAGS = "ipm_flags";
        public static final String IPM_MODE = "ipm_mode";
        public static final String IPM_TARGET_LRPST = "ipm_target_lrpst";
        public static final String IPM_TARGET_POWER = "ipm_target_power";
        public static final String IPM_TARGET_PST = "ipm_target_temp";
        public static final String IS_DEVICE_SUPPORTED_BY_SERVER = "is_device_supported_by_server";
        public static final String LAST_FULLY_UPDATE_TIME = "last_fully_update_time";
        public static final String LAST_UPDATE_TIME = "last_update_time";
        public static final String LATEST_SESSION = "latest_session";
        public static final String LIMIT_BG_NETWORK_ENABLED = "limit_bg_network_enabled";
        public static final String LIMIT_BG_NETWORK_SUPPORTED = "limit_bg_network_supported";
        public static final String LRU_NUM = "lru_num";
        public static final String MAX_RESPONSE_TIME = "max_response_time";
        public static final String MAX_SESSIONS = "max_sessions";
        public static final String MAX_SIOP_MODE = "max_siop_mode";
        public static final String MD_SWITCH_WIFI_BLOCK_ENABLED = "md_switch_wifi_block_enabled";
        public static final String MD_SWITCH_WIFI_BLOCK_SUPPORTED = "md_switch_wifi_block_supported";
        public static final String MIN_SIOP_MODE = "min_siop_mode";
        public static final String PACKAGE_NAME = "package_name";
        public static final String PACKAGE_NAMES = "package_names";
        public static final String PACKAGE_NAMES_SECURE_FOLDER = "package_names_secure_folder";
        public static final String PARAM = "param";
        public static final String PARAMS = "params";
        public static final String PARAM_DESCRIPTION = "param_description";
        public static final String PARAM_TYPE = "param_type";
        public static final String PROFILE = "profile";
        public static final String RATE = "rate";
        public static final String RECEIVER_TYPE = "receiver_type";
        public static final String REPORT_MSG = "report_msg";
        public static final String REPORT_TAG = "report_tag";
        public static final String REQUEST_CODE = "request_code";
        @Deprecated
        public static final String SERVER_CATEGORY = "server_category";
        public static final String SESSION = "session";
        public static final String SESSION_DATA = "session_data";
        public static final String SETTABLE_FEATURES = "settable_features";
        public static final String SIOP_MODE = "siop_mode";
        public static final String SPEED_LIMIT_LEVEL = "speed_limit_level";
        public static final String START = "start";
        public static final String SUBSCRIBER_NAME = "subscriber_name";
        public static final String SUCCESSFUL = "successful";
        public static final String SUCCESSFUL_ITEMS = "successful_items";
        public static final String SURVIVE_LIST = "survive_list";
        public static final String TAG = "tag";
        public static final String TARGET_LRPST = "targetLRPST";
        public static final String TARGET_PST = "targetPst";
        public static final String TYPE = "type";
        public static final String UUID = "uuid";
        public static final String VAL = "val";
        public static final String VERSION = "version";
        public static final String VOLUME_CONTROL = "volume_control";

        private KeyName() {
        }
    }

    public static class FeatureFlagKeyNames {
        public static final String AVAILABLE = "available";
        public static final String ENABLED = "enabled";
        public static final String ENABLED_BY_CODE = "enabled_by_code";
        public static final String ENABLED_BY_SERVER = "enabled_by_server";
        public static final String ENABLED_BY_USER = "enabled_by_user";
        public static final String FORCED = "forced";
        public static final String FORCED_BY_CODE = "forced_by_code";
        public static final String INHERITED = "inherited";
        public static final String USING_PKG_VALUE = "using_pkg_value";
        public static final String USING_USER_VALUE = "using_user_value";

        private FeatureFlagKeyNames() {
        }
    }
}
