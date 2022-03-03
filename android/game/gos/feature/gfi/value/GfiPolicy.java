package com.samsung.android.game.gos.feature.gfi.value;

import android.os.Build;
import android.os.Parcel;
import com.samsung.android.game.gos.util.GosLog;
import org.json.JSONException;
import org.json.JSONObject;

public class GfiPolicy {
    public static final float DEFAULT_AUTODELAY_ADJUSTMENT_RATE = 5.0f;
    public static final float DEFAULT_AUTODELAY_ADJUSTMENT_TARGET = 0.991f;
    public static final int DEFAULT_AUTODELAY_CHANGE_RATE = 2;
    public static final boolean DEFAULT_AUTODELAY_ENABLED = true;
    public static final float DEFAULT_AUTODELAY_THRESHOLD = 0.008f;
    public static final int DEFAULT_DELAY = 64;
    public static final int DEFAULT_DFS_OFFSET = 10;
    public static final boolean DEFAULT_DFS_OFFSET_ENABLED = false;
    public static final double DEFAULT_DFS_SMOOTHNESS = 0.8d;
    public static final boolean DEFAULT_ENABLED = false;
    public static final boolean DEFAULT_EXTERNAL_CONTROL = false;
    public static final int DEFAULT_GFPS_OFFSET = 0;
    public static final boolean DEFAULT_GFPS_OFFSET_ENABLED = false;
    public static final int DEFAULT_INTERPOLATION_FPS = 0;
    public static final GfiMode DEFAULT_INTERPOLATION_MODE = GfiMode.RETIME;
    public static final int DEFAULT_MAXIMUM_DFS = 60;
    public static final int DEFAULT_MAXIMUM_GFPS = 60;
    public static final int DEFAULT_MAX_ACCEPTABLE_FPS = -1;
    public static final int DEFAULT_MAX_CONSECUTIVE_GL_COMPOSITIONS = 600;
    public static final double DEFAULT_MAX_PERCENT_GL_COMPOSITIONS = 1.0d;
    public static final String DEFAULT_MAX_VERSION = "999.999.99999";
    public static final int DEFAULT_MINIMUM_DFS = 15;
    public static final int DEFAULT_MINIMUM_GFPS = 15;
    public static final double DEFAULT_MINIMUM_REGAL_STABILITY = 0.9d;
    public static final int DEFAULT_MIN_ACCEPTABLE_FPS = 0;
    public static final String DEFAULT_MIN_VERSION = "1.0.0";
    public static final boolean DEFAULT_NO_INTERP_WITH_EXTRA_LAYERS = false;
    public static final boolean DEFAULT_WRITE_TO_FRAME_TRACKER = true;
    public static final String FEATURE_FLAG_POLICY = "feature_flag_policy";
    public static final String KEY_AUTODELAY_ENABLED = "smart_delay";
    public static final String KEY_DFS_OFFSET = "dfs_offset";
    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_EXTERNAL_CONTROL = "allow_external_control";
    public static final String KEY_GFPS_OFFSET = "gfps_offset";
    public static final String KEY_INTERPOLATION_FPS = "game_fps_limit";
    public static final String KEY_INTERPOLATION_MODE = "mode";
    public static final String KEY_MAXIMUM_VERSION = "gfi_maximum_version";
    public static final String KEY_MAX_ACCEPTABLE_FPS = "max_acceptable_fps";
    public static final String KEY_MAX_CONSECUTIVE_GL_COMPOSITIONS = "max_consecutive_gl_compositions";
    public static final String KEY_MAX_PERCENT_GL_COMPOSITIONS = "max_percent_gl_compositions";
    public static final String KEY_MINIMUM_REGAL_STABILITY = "minimum_regal_stability";
    public static final String KEY_MINIMUM_VERSION = "gfi_minimum_version";
    public static final String KEY_MIN_ACCEPTABLE_FPS = "min_acceptable_fps";
    public static final String KEY_NO_INTERP_WITH_EXTRA_LAYERS = "no_interp_with_extra_layers";
    public static final String KEY_TARGET_DFS = "target_dfs";
    public static final String KEY_WRITE_TO_FRAMETRACKER = "write_to_frametracker";
    private static final String LOG_TAG = "GfiPolicy";

    public static class DfsOffset {
        public static final String KEY_ENABLED = "enabled";
        public static final String KEY_MAXIMUM = "maximum";
        public static final String KEY_MINIMUM = "minimum";
        public static final String KEY_SMOOTHNESS = "smoothness";
        public static final String KEY_VALUE = "value";
    }

    public static class GfpsOffset {
        public static final String KEY_ENABLED = "enabled";
        public static final String KEY_MAXIMUM = "maximum";
        public static final String KEY_MINIMUM = "minimum";
        public static final String KEY_VALUE = "value";
    }

    public enum GfiMode {
        RETIME(0),
        FILLIN(1),
        NONE(2),
        LATENCY_REDUCTION(3),
        UNLIMITED(4),
        ML_LATENCY_REDUCTION(5);
        
        public final int mode;

        private GfiMode(int i) {
            this.mode = i;
        }

        public static GfiMode fromString(String str) throws GfiPolicyException {
            if (str.compareTo("fillin") == 0) {
                return FILLIN;
            }
            if (str.compareTo("retime") == 0) {
                return RETIME;
            }
            if (str.compareTo("none") == 0) {
                return NONE;
            }
            if (str.compareTo("latred") == 0) {
                return LATENCY_REDUCTION;
            }
            if (str.compareTo("mllatred") == 0) {
                return ML_LATENCY_REDUCTION;
            }
            if (str.compareTo("unlimited") != 0) {
                throw new GfiPolicyException("No GfiMode " + str);
            } else if (Build.TYPE.compareTo("user") != 0) {
                return UNLIMITED;
            } else {
                throw new GfiPolicyException("GfiMode " + str + " disabled in user builds");
            }
        }

        public static GfiMode fromInt(int i) throws GfiPolicyException {
            for (GfiMode gfiMode : values()) {
                if (gfiMode.mode == i) {
                    return gfiMode;
                }
            }
            throw new GfiPolicyException("No GFI mode matches int " + i);
        }

        public boolean isLatencyReductionMode() {
            int i = this.mode;
            return i == LATENCY_REDUCTION.mode || i == ML_LATENCY_REDUCTION.mode;
        }
    }

    public static boolean isExternallControllable(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has(KEY_EXTERNAL_CONTROL)) {
            return jSONObject.getBoolean(KEY_EXTERNAL_CONTROL);
        }
        return false;
    }

    public static boolean isGFPSOffsetEnabled(JSONObject jSONObject) throws JSONException {
        if (!jSONObject.has(KEY_GFPS_OFFSET)) {
            return false;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject(KEY_GFPS_OFFSET);
        if (!jSONObject2.has("enabled") || !jSONObject2.getBoolean("enabled") || !jSONObject2.has("value") || jSONObject2.getInt("value") <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isDFSOffsetEnabled(JSONObject jSONObject) throws JSONException {
        if (!jSONObject.has(KEY_DFS_OFFSET)) {
            return false;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject(KEY_DFS_OFFSET);
        if (!jSONObject2.has("enabled") || !jSONObject2.getBoolean("enabled") || !jSONObject2.has("value") || jSONObject2.getInt("value") <= 0) {
            return false;
        }
        return true;
    }

    public static JSONObject disableDFSOffset(JSONObject jSONObject) throws JSONException {
        jSONObject.put(KEY_DFS_OFFSET, new JSONObject("{\"enabled\": false, \"value\": 0 }"));
        return jSONObject;
    }

    public static JSONObject setRFPS(JSONObject jSONObject, double d) throws JSONException {
        jSONObject.put(KEY_TARGET_DFS, d);
        return jSONObject;
    }

    public static float getRFPS(JSONObject jSONObject) {
        return (float) jSONObject.optDouble(KEY_TARGET_DFS, 120.0d);
    }

    public static boolean isHighFrameRatePolicy(JSONObject jSONObject) throws JSONException {
        return (jSONObject.has(KEY_INTERPOLATION_FPS) && jSONObject.getInt(KEY_INTERPOLATION_FPS) > 60) || (jSONObject.has(KEY_TARGET_DFS) && jSONObject.getInt(KEY_TARGET_DFS) > 60);
    }

    public static boolean isLowLatencyPolicy(JSONObject jSONObject) throws Exception {
        return jSONObject.has(KEY_INTERPOLATION_MODE) && GfiMode.fromString(jSONObject.getString(KEY_INTERPOLATION_MODE)) == GfiMode.LATENCY_REDUCTION;
    }

    public static boolean hasCustomDfs(JSONObject jSONObject) throws JSONException {
        return jSONObject.has(KEY_TARGET_DFS) && jSONObject.getDouble(KEY_TARGET_DFS) < 120.0d;
    }

    public static boolean isEnabled(JSONObject jSONObject) throws JSONException, GfiPolicyException {
        return isInterpolationEnabled(jSONObject) || isLatencyReductionEnabled(jSONObject);
    }

    public static boolean isInterpolationEnabled(JSONObject jSONObject) throws JSONException {
        return jSONObject.has("enabled") && jSONObject.getBoolean("enabled") && jSONObject.has(KEY_INTERPOLATION_FPS) && jSONObject.getInt(KEY_INTERPOLATION_FPS) > 0;
    }

    public static JSONObject setEnabledKey(JSONObject jSONObject, boolean z) throws JSONException {
        jSONObject.put("enabled", Boolean.valueOf(z));
        return jSONObject;
    }

    public static boolean isLatencyReductionEnabled(JSONObject jSONObject) throws JSONException, GfiPolicyException {
        if (!jSONObject.has("enabled") || !jSONObject.getBoolean("enabled") || !jSONObject.has(KEY_INTERPOLATION_MODE)) {
            return false;
        }
        return getMode(jSONObject).isLatencyReductionMode();
    }

    public static int getGFPS(JSONObject jSONObject) throws JSONException {
        return jSONObject.getInt(KEY_INTERPOLATION_FPS);
    }

    public static boolean setPolicyToParcel(int i, JSONObject jSONObject, Parcel parcel) {
        return GfiConversion.writePolicyToParcel(i, jSONObject, parcel);
    }

    public static GfiMode getMode(JSONObject jSONObject) throws GfiPolicyException, JSONException {
        if (jSONObject.get(KEY_INTERPOLATION_MODE) instanceof String) {
            return GfiMode.fromString(jSONObject.getString(KEY_INTERPOLATION_MODE));
        }
        return GfiMode.fromInt(jSONObject.getInt(KEY_INTERPOLATION_MODE));
    }

    public static void setStopParcel(String str, int i, Parcel parcel) {
        GfiConversion.writeStopParcel(i, parcel);
        try {
            GfiDfsHelper.popDfs(str);
        } catch (NullPointerException unused) {
            GosLog.d(LOG_TAG, "setStopParcel Exception in popDfs");
        }
    }
}
