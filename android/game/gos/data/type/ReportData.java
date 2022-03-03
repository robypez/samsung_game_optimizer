package com.samsung.android.game.gos.data.type;

import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportData {
    private static final String LOG_TAG = "ReportData";
    public static final String TAG_GAME_INFO = "game_info";
    public static final String TAG_USER_USAGE = "user_usage";
    private long mId;
    private String mMsg;
    private String mTag;

    public static final class GameInfoKey {
        public static final String APK_SIZE = "apk_size";
        public static final String GAME_ENGINE_CSV = "game_engine_csv";
        public static final String INSTALLER_PACKAGE_NAME = "installer_package_name";
        public static final String PACKAGE_NAME = "package_name";
        public static final String REPORTING_TIME = "reporting_time";
        public static final String UPDATE_TYPE = "update_type";
        public static final String VERSION_CODE = "version_code";
        public static final String VERSION_NAME = "version_name";
    }

    public static final class UserUsageKey {
        public static final String APPLIED_DSS = "applied_dss";
        public static final String APPLIED_RESOLUTION_TYPE = "applied_resolution_type";
        public static final String APPLIED_SHORT_SIDE = "applied_short_side";
        public static final String AUDIO_PLUGGED = "audio_plugged";
        public static final String AUDIO_VOLUME = "audio_volume";
        public static final String BATTERY_PERCENTAGE = "battery_percentage";
        public static final String BATTERY_PERCENTAGE_RESUME = "battery_percentage_resume";
        public static final String BATTERY_PLUGGED = "battery_plugged";
        public static final String BATTERY_PLUGGED_RESUME = "battery_plugged_resume";
        public static final String BOOST_LAUNCH_DURATION = "boost_launch_duration";
        public static final String BOOST_RESUME_DURATION = "boost_resume_duration";
        public static final String DURATION = "duration";
        public static final String EXTERNAL_SDK_EVENTS = "external_sdk_events";
        public static final String EXTERNAL_SDK_TYPE = "external_sdk_type";
        public static final String GOS_VERSION_CODE_FULL = "gos_version_code_full";
        public static final String GOS_VERSION_NAME_FULL = "gos_version_name_full";
        public static final String GPP_REP_DATA = "gpp_rep_data";
        public static final String GPP_RINGLOG_DATA = "gpp_ringlog_data";
        public static final String PACKAGE_NAME = "package_name";
        public static final String REPORTING_TIME = "reporting_time";
        public static final String RESUME_TIME = "resume_time";
        public static final String SCREEN_BRIGHTNESS = "screen_brightness";
        public static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
        public static final String SCREEN_BRIGHTNESS_MODE_RESUME = "screen_brightness_mode_resume";
        public static final String SCREEN_BRIGHTNESS_RESUME = "screen_brightness_resume";
        public static final String SIOP_MODE = "siop_mode";
        public static final String SPA_STATUS = "spa_status";
        public static final String TEST_GROUP_NAME = "test_group_name";
        public static final String VERSION_CODE = "version_code";
        public static final String VERSION_NAME = "version_name";
        public static final String WIFI_CONNECTED = "wifi_connected";
    }

    public ReportData(long j, String str, String str2) {
        this.mId = j;
        this.mTag = str;
        this.mMsg = str2;
    }

    public static String createServerConnectionLogJsonMsg(String str, String str2, String str3, int i, Exception exc, String str4) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(GosInterface.KeyName.COMMENT, str4);
            jSONObject.put("response_code", i);
            jSONObject.put("url", str2);
            if (exc != null) {
                jSONObject.put("exception", exc.toString());
            }
            jSONObject.put("header", str);
            if (str3 != null && str3.length() > 2048) {
                str3 = str3.substring(0, 2048);
            }
            jSONObject.put("parameter", str3);
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return jSONObject.toString();
    }

    public long getId() {
        return this.mId;
    }

    public String getTag() {
        return this.mTag;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public int getSize() {
        String str = this.mMsg;
        if (str != null) {
            return str.getBytes().length;
        }
        return 0;
    }
}
