package com.samsung.android.game;

public class ManagerInterface {

    public static class Command {
        public static final String BLOCK_BG_NET = "block_bg_network";
        public static final String BOOST_ACQUIRE = "boost_acquire";
        public static final String BOOST_RELEASE = "boost_release";
        public static final String BOOST_SET_DVFS_VALUE = "boost_set_dvfs_value";
        public static final String CONTROL_WIFI_SWITCH = "control_wifi_switch";
        public static final String GET_AVAILABLE_REFRESH_RATE_LIST = "get_available_refresh_rate_list";
        public static final String GET_NETWORK_STATS = "get_network_stats";
        public static final String GET_PACKAGE_UID_PID = "get_pid_uid";
        public static final String GET_SOS_POLICY_KEYS = "get_sos_policy_keys";
        public static final String GET_SYSFS_DATA = "get_sysfs_data";
        public static final String GET_THREAD_DATA = "get_thread_data";
        public static final String GET_THREAD_INFO = "get_thread_info";
        public static final String GET_THREAD_NAMES = "get_thread_names";
        public static final String IS_VARIABLE_REFRESH_RATE_SUPPORTED = "is_variable_refresh_rate_supported";
        public static final String REQUEST_TO_REMOVE_REFRESH_RATE = "request_to_remove_refresh_rate";
        public static final String REQUEST_TO_SET_REFRESH_RATE = "request_to_set_refresh_rate";
        public static final String SET_RENDER_THREAD_AFFINITY = "set_render_thread_affinity";
        public static final String SET_THREADS_AFFINITIES = "set_threads_affinities";
        public static final String SET_THREAD_AFFINITY_BIGCORE = "set_thread_affinity_bigcore";
        public static final String SET_THREAD_AFFINITY_LITTLECORE = "set_thread_affinity_littlecore";
        public static final String UNSET_RENDER_THREAD_AFFINITY = "unset_render_thread_affinity";
        public static final String WRITE_FILE = "write_file";
    }

    public static class GameSdkKey {
        public static final String CPU_MIN_PERCENT = "cpu_min_percent";
        public static final String GPU_MIN_PERCENT = "gpu_min_percent";
        public static final String IS_SUPPORTED = "is_supported";
    }

    public static class KeyName {
        public static final String BIG = "big";
        public static final String CONTENT = "content";
        public static final String CPU_LEVEL = "cpu_level";
        public static final String DISPLAY_MODE_ID_LIST = "display_mode_id_list";
        public static final String DRR_ENABLED_STATE_LIST = "drr_enabled_state_list";
        public static final String GAME_SDK_SETTING = "game_sdk_setting";
        public static final String GOVERNOR_SETTING = "governor_setting";
        public static final String GPU_LEVEL = "gpu_level";
        public static final String LITTLE = "little";
        public static final String NETWORK_STATS_DOWN = "network_stats_down";
        public static final String NETWORK_STATS_UP = "network_stats_up";
        public static final String PACKAGE_NAME = "package_name";
        public static final String PACKAGE_NAME_LIST = "package_name_list";
        public static final String PACKAGE_PID = "package_pid";
        public static final String PACKAGE_UID = "package_uid";
        public static final String PATHNAME = "pathname";
        public static final String REQUEST_CPU_SUM = "cpu_sum";
        public static final String REQUEST_NAME = "name";
        public static final String SHIFT_TEMPERATURE = "shift_temperature";
        public static final String SIOP_MODE = "siop_mode";
        public static final String SOS_POLICY = "sos_policy";
        public static final String SOS_POLICY_KEYS = "sos_policy_keys";
        public static final String THREAD_ID = "thread_id";
        public static final String THREAD_IDS = "thread_ids";
        public static final String VALUE_ARRAY_1 = "value_array_1";
        public static final String VALUE_BOOL_1 = "value_bool_1";
        public static final String VALUE_FLOAT_1 = "value_float_1";
        public static final String VALUE_INT_1 = "value_int_1";
        public static final String VALUE_STRING_1 = "value_string_1";
    }
}
