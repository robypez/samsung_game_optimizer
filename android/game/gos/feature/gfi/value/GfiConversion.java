package com.samsung.android.game.gos.feature.gfi.value;

import android.os.Parcel;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.GosLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class GfiConversion {
    private static final String LOG_TAG = "GfiConversion";
    public static final int SETTINGS_AUTODELAY_ADJUSTMENT_RATE = 3;
    public static final int SETTINGS_AUTODELAY_ADJUSTMENT_TARGET = 4;
    public static final int SETTINGS_AUTODELAY_CHANGE_RATE = 5;
    public static final int SETTINGS_AUTODELAY_ENABLED = 2;
    public static final int SETTINGS_AUTODELAY_THRESHOLD = 6;
    public static final int SETTINGS_DEFAULT_DELAY = 102;
    public static final int SETTINGS_DELAY = 1;
    public static final int SETTINGS_DFS_OFFSET = 9;
    public static final int SETTINGS_DFS_OFFSET_ENABLED = -2;
    public static final int SETTINGS_DFS_SMOOTHNESS = 10;
    public static final int SETTINGS_GFPS_OFFSET = 13;
    public static final int SETTINGS_GFPS_OFFSET_ENABLED = 12;
    public static final int SETTINGS_INTERPOLATION_FPS = 0;
    public static final int SETTINGS_INTERPOLATION_MODE = 7;
    public static final int SETTINGS_KEEP_TWO_HWC_LAYERS = 115;
    public static final int SETTINGS_LOGLEVEL = 100;
    public static final int SETTINGS_MAXIMUM_DFS = 17;
    public static final int SETTINGS_MAXIMUM_GFPS = 15;
    public static final int SETTINGS_MAX_ACCEPTABLE_FPS = 27;
    public static final int SETTINGS_MAX_CONSECUTIVE_GL_COMPOSITIONS = 24;
    public static final int SETTINGS_MAX_PERCENT_GL_COMPOSITIONS = 25;
    public static final int SETTINGS_MINIMUM_DFS = 16;
    public static final int SETTINGS_MINIMUM_GFPS = 14;
    public static final int SETTINGS_MINIMUM_REGAL_STABILITY = 23;
    public static final int SETTINGS_MIN_ACCEPTABLE_FPS = 26;
    public static final int SETTINGS_NON_PLATFORM = -1;
    public static final int SETTINGS_NO_INTERP_WITH_EXTRA_LAYERS = 28;
    public static final int SETTINGS_QUEUESIZE = 101;
    public static final int SETTINGS_REFRESH_FPS = 18;
    public static final int SETTINGS_WRITE_TO_FRAMETRACKER = 8;
    public static final List<Integer> mBooleanSettings;
    public static final Map<String, Integer> mDfsOffsetKeyToIndexMap;
    public static final List<Integer> mFloatSettings;
    public static final Map<String, Integer> mGfpsOffsetKeyToIndexMap;
    public static final List<Integer> mIntegerSettings;
    public static final Map<String, Integer> mKeyToIndexMap;
    public static final Map<String, Map<String, Integer>> mSuperKeyMap;

    private interface Setting {
        void writeToParcel(Parcel parcel);
    }

    static {
        HashMap hashMap = new HashMap();
        mKeyToIndexMap = hashMap;
        hashMap.put(GfiPolicy.KEY_INTERPOLATION_FPS, 0);
        mKeyToIndexMap.put(GfiPolicy.KEY_INTERPOLATION_MODE, 7);
        mKeyToIndexMap.put(GfiPolicy.KEY_AUTODELAY_ENABLED, 2);
        mKeyToIndexMap.put(GfiPolicy.KEY_WRITE_TO_FRAMETRACKER, 8);
        mKeyToIndexMap.put(GfiPolicy.KEY_TARGET_DFS, 18);
        mKeyToIndexMap.put(GfiPolicy.KEY_MINIMUM_REGAL_STABILITY, 23);
        mKeyToIndexMap.put(GfiPolicy.KEY_MAX_CONSECUTIVE_GL_COMPOSITIONS, 24);
        mKeyToIndexMap.put(GfiPolicy.KEY_MAX_PERCENT_GL_COMPOSITIONS, 25);
        mKeyToIndexMap.put(GfiPolicy.KEY_MIN_ACCEPTABLE_FPS, 26);
        mKeyToIndexMap.put(GfiPolicy.KEY_MAX_ACCEPTABLE_FPS, 27);
        mKeyToIndexMap.put(GfiPolicy.KEY_NO_INTERP_WITH_EXTRA_LAYERS, 28);
        mKeyToIndexMap.put(GfiPolicy.FEATURE_FLAG_POLICY, -1);
        mKeyToIndexMap.put("enabled", -1);
        mKeyToIndexMap.put(GfiPolicy.KEY_EXTERNAL_CONTROL, -1);
        mKeyToIndexMap.put(GfiPolicy.KEY_MINIMUM_VERSION, -1);
        mKeyToIndexMap.put(GfiPolicy.KEY_MAXIMUM_VERSION, -1);
        HashMap hashMap2 = new HashMap();
        mGfpsOffsetKeyToIndexMap = hashMap2;
        hashMap2.put("enabled", 12);
        mGfpsOffsetKeyToIndexMap.put("value", 13);
        mGfpsOffsetKeyToIndexMap.put("minimum", 14);
        mGfpsOffsetKeyToIndexMap.put("maximum", 15);
        HashMap hashMap3 = new HashMap();
        mDfsOffsetKeyToIndexMap = hashMap3;
        hashMap3.put("enabled", -2);
        mDfsOffsetKeyToIndexMap.put("value", 9);
        mDfsOffsetKeyToIndexMap.put(GfiPolicy.DfsOffset.KEY_SMOOTHNESS, 10);
        mDfsOffsetKeyToIndexMap.put("minimum", 16);
        mDfsOffsetKeyToIndexMap.put("maximum", 17);
        HashMap hashMap4 = new HashMap();
        mSuperKeyMap = hashMap4;
        hashMap4.put(BuildConfig.VERSION_NAME, mKeyToIndexMap);
        mSuperKeyMap.put(GfiPolicy.KEY_GFPS_OFFSET, mGfpsOffsetKeyToIndexMap);
        mSuperKeyMap.put(GfiPolicy.KEY_DFS_OFFSET, mDfsOffsetKeyToIndexMap);
        ArrayList arrayList = new ArrayList();
        mIntegerSettings = arrayList;
        arrayList.add(0);
        mIntegerSettings.add(18);
        mIntegerSettings.add(17);
        mIntegerSettings.add(16);
        mIntegerSettings.add(15);
        mIntegerSettings.add(14);
        mIntegerSettings.add(13);
        mIntegerSettings.add(24);
        mIntegerSettings.add(26);
        mIntegerSettings.add(27);
        ArrayList arrayList2 = new ArrayList();
        mFloatSettings = arrayList2;
        arrayList2.add(10);
        mFloatSettings.add(23);
        mFloatSettings.add(25);
        ArrayList arrayList3 = new ArrayList();
        mBooleanSettings = arrayList3;
        arrayList3.add(2);
        mBooleanSettings.add(8);
        mBooleanSettings.add(12);
        mBooleanSettings.add(28);
    }

    private static abstract class SimpleSetting<T> implements Setting {
        protected int mKey;
        protected T mValue;

        SimpleSetting(int i, T t) {
            if (t != null) {
                this.mKey = i;
                this.mValue = t;
                return;
            }
            throw new NullPointerException("Attempted to create Setting (with key " + i + ") with a null value.");
        }

        public String toString() {
            return this.mKey + ", " + this.mValue + ", ";
        }
    }

    private static class SimpleIntSetting extends SimpleSetting<Integer> {
        SimpleIntSetting(int i, Integer num) {
            super(i, num);
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        SimpleIntSetting(int i, Boolean bool) {
            super(i, Integer.valueOf((bool == null || !bool.booleanValue()) ? 0 : 1));
        }

        public void writeToParcel(Parcel parcel) {
            parcel.writeInt(this.mKey);
            parcel.writeInt(((Integer) this.mValue).intValue());
        }
    }

    private static class SimpleFloatSetting extends SimpleSetting<Float> {
        SimpleFloatSetting(int i, Float f) {
            super(i, f);
        }

        public void writeToParcel(Parcel parcel) {
            parcel.writeInt(this.mKey);
            parcel.writeFloat(((Float) this.mValue).floatValue());
        }
    }

    private static class GfiModeSetting extends SimpleIntSetting {
        GfiModeSetting(String str) throws ClassCastException, IllegalArgumentException, GfiPolicyException {
            super(7, Integer.valueOf(GfiPolicy.GfiMode.fromString(str).mode));
        }

        GfiModeSetting(Integer num) throws NullPointerException, IllegalArgumentException, GfiPolicyException {
            super(7, Integer.valueOf(GfiPolicy.GfiMode.fromInt(num.intValue()).mode));
        }
    }

    private static Integer getSettingsIndex(String str, String str2) {
        Map map = mSuperKeyMap.get(str);
        if (map == null) {
            GosLog.w(LOG_TAG, "Unrecognised super-key: " + str);
            return null;
        }
        Integer num = (Integer) map.get(str2);
        if (num == null) {
            GosLog.w(LOG_TAG, "Unrecognised key: " + str2 + " (for super-key: " + str + ")");
        }
        return num;
    }

    private static Setting getSetting(JSONObject jSONObject, int i, Object obj) throws JSONException, ClassCastException, NullPointerException, IllegalArgumentException, GfiPolicyException {
        if (i == 7) {
            if (obj instanceof String) {
                return new GfiModeSetting((String) obj);
            }
            return new GfiModeSetting((Integer) obj);
        } else if (i == -2) {
            if (jSONObject.has("value")) {
                return null;
            }
            if (jSONObject.getBoolean("enabled")) {
                GosLog.w(LOG_TAG, "DFS offset marked as enabled, but has no value");
            }
            return new SimpleIntSetting(9, (Integer) -1);
        } else if (i == 9) {
            if (!jSONObject.getBoolean("enabled")) {
                return new SimpleIntSetting(9, (Integer) -1);
            }
            if (obj instanceof String) {
                return new SimpleIntSetting(9, Integer.valueOf(Integer.parseInt((String) obj)));
            }
            return new SimpleIntSetting(9, (Integer) obj);
        } else if (mIntegerSettings.contains(Integer.valueOf(i))) {
            if (obj instanceof String) {
                return new SimpleIntSetting(i, Integer.valueOf((int) Float.parseFloat((String) obj)));
            }
            return new SimpleIntSetting(i, (Integer) obj);
        } else if (mBooleanSettings.contains(Integer.valueOf(i))) {
            if (obj instanceof String) {
                return new SimpleIntSetting(i, Boolean.valueOf(Boolean.parseBoolean((String) obj)));
            }
            if (obj instanceof Boolean) {
                return new SimpleIntSetting(i, (Boolean) obj);
            }
            Integer num = (Integer) obj;
            int intValue = num.intValue();
            if (intValue == 0 || intValue == 1) {
                return new SimpleIntSetting(i, num);
            }
            throw new ClassCastException("Boolean setting index " + i + " without a boolean value " + obj);
        } else if (!mFloatSettings.contains(Integer.valueOf(i))) {
            throw new ClassCastException("Unrecognised setting index " + i + " with value " + obj);
        } else if (obj instanceof String) {
            return new SimpleFloatSetting(i, Float.valueOf(Float.parseFloat((String) obj)));
        } else {
            if (obj instanceof Float) {
                return new SimpleFloatSetting(i, Float.valueOf(((Float) obj).floatValue()));
            }
            if (obj instanceof Integer) {
                obj = Double.valueOf((double) ((Integer) obj).intValue());
            }
            return new SimpleFloatSetting(i, Float.valueOf((float) ((Double) obj).doubleValue()));
        }
    }

    private static ArrayList<Setting> getStopSettingsList() {
        ArrayList<Setting> arrayList = new ArrayList<>();
        arrayList.add(new SimpleIntSetting(0, (Integer) 0));
        return arrayList;
    }

    private static ArrayList<Setting> getLatencyReductionSettingsList(JSONObject jSONObject) throws GfiPolicyException, JSONException {
        if (!GfiVersion.getVersion().higherOrEqualThan("1.4.17")) {
            GosLog.d(LOG_TAG, "Frame Booster version " + GfiVersion.getVersion() + " lower than 1.4.17. Latency Reduction not available.");
            return getStopSettingsList();
        }
        GosLog.v(LOG_TAG, "Setting Latency Reduction mode and MAX GFPS");
        ArrayList<Setting> arrayList = new ArrayList<>();
        arrayList.add(new SimpleIntSetting(0, (Integer) 120));
        GfiPolicy.GfiMode mode = GfiPolicy.getMode(jSONObject);
        if (mode != GfiPolicy.GfiMode.ML_LATENCY_REDUCTION || GfiVersion.getVersion().higherOrEqualThan("1.5.19")) {
            arrayList.add(new GfiModeSetting(Integer.valueOf(mode.mode)));
            return arrayList;
        }
        GosLog.d(LOG_TAG, "Frame Booster version " + GfiVersion.getVersion() + " lower than 1.5.19. ML Latency Reduction not available.");
        return getStopSettingsList();
    }

    private static ArrayList<Setting> convertPolicyToSettings(JSONObject jSONObject) throws JSONException, ClassCastException, NullPointerException, GfiPolicyException {
        GosLog.v(LOG_TAG, "Policy: " + jSONObject);
        if (!jSONObject.has("enabled") || !jSONObject.getBoolean("enabled")) {
            GosLog.v(LOG_TAG, "Interpolation not marked as enabled");
            return getStopSettingsList();
        } else if (GfiPolicy.isLatencyReductionEnabled(jSONObject)) {
            return getLatencyReductionSettingsList(jSONObject);
        } else {
            ArrayList<Setting> arrayList = new ArrayList<>();
            convertPolicyToSettingsRecursive(BuildConfig.VERSION_NAME, jSONObject, arrayList);
            if (arrayList.size() != 0) {
                return arrayList;
            }
            throw new NullPointerException("No valid Settings!");
        }
    }

    private static void convertPolicyToSettingsRecursive(String str, JSONObject jSONObject, ArrayList<Setting> arrayList) throws JSONException, ClassCastException, NullPointerException, GfiPolicyException {
        Setting setting;
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            Object obj = jSONObject.get(next);
            if (obj instanceof JSONObject) {
                convertPolicyToSettingsRecursive(next, (JSONObject) obj, arrayList);
            } else {
                Integer settingsIndex = getSettingsIndex(str, next);
                if (settingsIndex == null) {
                    throw new JSONException("Invalid JSON key: " + next);
                } else if (!(settingsIndex.intValue() == -1 || (setting = getSetting(jSONObject, settingsIndex.intValue(), obj)) == null)) {
                    arrayList.add(setting);
                }
            }
        }
    }

    private static void writeSettingsListToParcel(int i, ArrayList<Setting> arrayList, Parcel parcel) {
        parcel.writeInt(i);
        parcel.writeInt(arrayList.size());
        StringBuilder sb = new StringBuilder(i + ", " + arrayList.size() + ", ");
        Iterator<Setting> it = arrayList.iterator();
        while (it.hasNext()) {
            Setting next = it.next();
            next.writeToParcel(parcel);
            sb.append(next.toString());
        }
        GosLog.v(LOG_TAG, "Parcel write: " + sb);
    }

    public static void writeStopParcel(int i, Parcel parcel) {
        parcel.setDataPosition(0);
        parcel.writeInterfaceToken(GfiSurfaceFlingerHelper.SURFACEFLINGER_INTERFACE_TOKEN);
        writeSettingsListToParcel(i, getStopSettingsList(), parcel);
    }

    public static boolean writePolicyToParcel(int i, JSONObject jSONObject, Parcel parcel) {
        if (jSONObject == null || parcel == null) {
            GosLog.e(LOG_TAG, "Policy or Parcel is null - can't write policy!");
            return false;
        }
        parcel.writeInterfaceToken(GfiSurfaceFlingerHelper.SURFACEFLINGER_INTERFACE_TOKEN);
        try {
            writeSettingsListToParcel(i, convertPolicyToSettings(jSONObject), parcel);
            return true;
        } catch (GfiPolicyException | ClassCastException | IllegalArgumentException | NullPointerException | JSONException e) {
            GosLog.e(LOG_TAG, "Exception occurred during policy conversion - writing stop parcel instead: " + e);
            writeStopParcel(i, parcel);
            return false;
        }
    }
}
