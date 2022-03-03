package com.samsung.android.game.gos.data.dbhelper;

import android.os.Build;
import android.util.ArrayMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dao.PerfDataPermissionDao;
import com.samsung.android.game.gos.data.dao.SettingsAccessiblePackageDao;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.PerfDataPermission;
import com.samsung.android.game.gos.data.model.SettingsAccessiblePackage;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.BadHardcodedConstant;
import com.samsung.android.game.gos.value.RinglogPermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class GlobalDbHelper {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = GlobalDbHelper.class.getSimpleName();
    private final GlobalDao globalDao;
    private final GlobalFeatureFlagDao globalFFDao;
    private String mRinglogMode;
    /* access modifiers changed from: private */
    public final PerfDataPermissionDao perfDataPermissionDao;
    private final SettingsAccessiblePackageDao settingsAccessiblePackageDao;

    private GlobalDbHelper() {
        this.mRinglogMode = null;
        this.globalDao = DbHelper.getInstance().getGlobalDao();
        this.globalFFDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
        this.perfDataPermissionDao = DbHelper.getInstance().getPerfDataPermissionDao();
        this.settingsAccessiblePackageDao = DbHelper.getInstance().getSettingsAccessiblePackageDao();
        updateRinglogPolicy();
    }

    public static GlobalDbHelper getInstance() {
        if (AppVariable.isUnitTest()) {
            return new GlobalDbHelper();
        }
        return SingletonHolder.INSTANCE;
    }

    public void setSettingAccessiblePackage(String str, Collection<String> collection) {
        if (str != null && collection != null && !AppVariable.isUnitTest() && ((long) Build.VERSION.SEM_PLATFORM_INT) >= BadHardcodedConstant.SEP_VERSION_12_1) {
            for (String settingsAccessiblePackage : collection) {
                DbHelper.getInstance().getSettingsAccessiblePackageDao().insertOrUpdate(new SettingsAccessiblePackage(str, settingsAccessiblePackage));
            }
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final GlobalDbHelper INSTANCE = new GlobalDbHelper();

        private SingletonHolder() {
        }
    }

    public void setAllEnabledFlagByUserAsDefault() {
        List<GlobalFeatureFlag> all = this.globalFFDao.getAll();
        for (GlobalFeatureFlag next : all) {
            if (next.inheritedFlag) {
                next.enabledFlagByUser = Global.DefaultGlobalData.isEnabledByDefault(next.name);
            } else {
                next.enabledFlagByUser = next.enabledFlagByServer;
            }
        }
        this.globalFFDao.insertOrUpdate((Collection<GlobalFeatureFlag>) all);
    }

    public Map<String, GlobalFeatureFlag> getFeatureFlagMap() {
        List<GlobalFeatureFlag> all = this.globalFFDao.getAll();
        ArrayMap arrayMap = new ArrayMap(all.size());
        for (GlobalFeatureFlag next : all) {
            arrayMap.put(next.name, next);
        }
        return arrayMap;
    }

    public void mergeFeatureFlagList(Map<String, FeatureFlag> map) {
        Collection<GlobalFeatureFlag> values = getFeatureFlagMap().values();
        ArrayList arrayList = new ArrayList();
        for (GlobalFeatureFlag next : values) {
            FeatureFlag featureFlag = map.get(next.name);
            if (featureFlag != null) {
                next.setState(featureFlag.getState());
                map.remove(next.name);
                arrayList.add(next);
            }
        }
        if (map.size() > 0) {
            for (FeatureFlag next2 : map.values()) {
                arrayList.add(new GlobalFeatureFlag(next2.getName(), next2.getState()));
            }
        }
        this.globalFFDao.insertOrUpdate((Collection<GlobalFeatureFlag>) arrayList);
    }

    public void setAvailable(String str, boolean z) {
        GlobalFeatureFlag globalFeatureFlag = this.globalFFDao.get(str);
        if (globalFeatureFlag == null) {
            String str2 = LOG_TAG;
            GosLog.w(str2, "setAvailable : " + str + " not exist.");
            globalFeatureFlag = new GlobalFeatureFlag(str, "inherited");
        }
        globalFeatureFlag.available = z;
        this.globalFFDao.insertOrUpdate(globalFeatureFlag);
    }

    @Deprecated
    public void setUsingCustomValue(String str, boolean z) {
        this.globalFFDao.setUsingUserValue(new GlobalFeatureFlag.NameAndUsingUserValue(str, z));
        this.globalFFDao.setUsingPkgValue(new GlobalFeatureFlag.NameAndUsingPkgValue(str, z));
    }

    public boolean isDefaultFeatureFlag(String str) {
        GlobalFeatureFlag globalFeatureFlag = this.globalFFDao.get(str);
        if (globalFeatureFlag == null) {
            return false;
        }
        if (globalFeatureFlag.inheritedFlag) {
            return Global.DefaultGlobalData.isEnabledByDefault(str);
        }
        return globalFeatureFlag.enabledFlagByServer;
    }

    public boolean isPositiveFeatureFlag(String str) {
        GlobalFeatureFlag globalFeatureFlag = this.globalFFDao.get(str);
        if (globalFeatureFlag == null) {
            String str2 = LOG_TAG;
            GosLog.e(str2, "isPositiveFeatureFlag()-featureName is not in DB: " + str);
            return false;
        } else if (!globalFeatureFlag.available || !globalFeatureFlag.isPositiveState()) {
            return false;
        } else {
            return true;
        }
    }

    public void setFeatureFlagState(String str, String str2) {
        if (State.isValidV4State(str2)) {
            this.globalFFDao.setState(new GlobalFeatureFlag.NameAndState(str, str2));
        }
    }

    @Deprecated
    public boolean isUsingCustomValue(String str) {
        GlobalFeatureFlag globalFeatureFlag = this.globalFFDao.get(str);
        if (globalFeatureFlag == null) {
            String str2 = LOG_TAG;
            GosLog.w(str2, "isUsingCustomValue()-" + str + " not exist");
            return false;
        } else if (!globalFeatureFlag.usingPkgValue || !globalFeatureFlag.usingUserValue) {
            return false;
        } else {
            return true;
        }
    }

    public float[] getEachModeDss() {
        float[] csvToFloats = TypeConverter.csvToFloats(this.globalDao.getEachModeDss());
        if (csvToFloats != null) {
            return csvToFloats;
        }
        GosLog.e(LOG_TAG, "getEachModeDss(): null. return Default eachModeDss");
        return Global.DefaultGlobalData.getEachModeDss();
    }

    public void setEachModeDss(float[] fArr) {
        if (fArr == null || fArr.length != 4) {
            GosLog.e(LOG_TAG, "setEachModeDss(): wrong parameter");
            return;
        }
        this.globalDao.setEachModeDss(new Global.IdAndEachModeDss(TypeConverter.floatsToCsv(fArr)));
    }

    public float[] getEachModeDfs() {
        float[] csvToFloats = TypeConverter.csvToFloats(this.globalDao.getEachModeDfs());
        if (csvToFloats != null) {
            return csvToFloats;
        }
        GosLog.e(LOG_TAG, "getEachModeDfs(): null. return Default eachModeDfs");
        return Global.DefaultGlobalData.getEachModeDfs();
    }

    public void setEachModeDfs(float[] fArr) {
        if (fArr == null || fArr.length != 4) {
            GosLog.e(LOG_TAG, "setEachModeDfs(): wrong parameter");
            return;
        }
        this.globalDao.setEachModeDfs(new Global.IdAndEachModeDfs(TypeConverter.floatsToCsv(fArr)));
    }

    public void setCustomDss(float f) {
        Global global = this.globalDao.get();
        if (global != null) {
            global.customDss = ValidationUtil.getValidDss(f);
            this.globalDao.insertOrUpdate(global);
        }
    }

    public void setCustomDfs(float f) {
        Global global = this.globalDao.get();
        if (global != null) {
            global.customDfs = ValidationUtil.getValidDfs(f);
            this.globalDao.insertOrUpdate(global);
        }
    }

    public String getTestGroupName() {
        return new LoggingPolicy(this.globalDao.getLoggingPolicy()).getTestGroupName();
    }

    public boolean addPermissionData(String str, RinglogPermission.PERM_POLICY perm_policy, RinglogPermission.PERM_TYPES perm_types, String str2, long j) {
        if (str == null || perm_policy == null || perm_types == null) {
            return false;
        }
        PerfDataPermission permissionsForPkgByClientPkg = this.perfDataPermissionDao.getPermissionsForPkgByClientPkg(str);
        if (permissionsForPkgByClientPkg == null) {
            permissionsForPkgByClientPkg = new PerfDataPermission();
            permissionsForPkgByClientPkg.pkgName = str;
        }
        permissionsForPkgByClientPkg.permPolicy = perm_policy.ordinal();
        permissionsForPkgByClientPkg.permType = perm_types.ordinal();
        permissionsForPkgByClientPkg.lastHandshakeTime = TypeConverter.getDateFormattedTime(j);
        if (perm_policy == RinglogPermission.PERM_POLICY.SERVER || perm_policy == RinglogPermission.PERM_POLICY.LOCAL_LIST) {
            if (str2 != null) {
                permissionsForPkgByClientPkg.paramListCsv = str2;
            } else {
                permissionsForPkgByClientPkg.paramListCsv = BuildConfig.VERSION_NAME;
            }
        }
        this.perfDataPermissionDao.insertOrUpdate(permissionsForPkgByClientPkg);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.samsung.android.game.gos.data.type.PerfPermissionData getPermissionsForPkg(final java.lang.String r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper$1 r1 = new com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper$1
            r1.<init>(r4)
            r4 = 0
            java.lang.Void[] r4 = new java.lang.Void[r4]     // Catch:{ ExecutionException -> 0x001c, InterruptedException -> 0x0017 }
            android.os.AsyncTask r4 = r1.execute(r4)     // Catch:{ ExecutionException -> 0x001c, InterruptedException -> 0x0017 }
            java.lang.Object r4 = r4.get()     // Catch:{ ExecutionException -> 0x001c, InterruptedException -> 0x0017 }
            com.samsung.android.game.gos.data.model.PerfDataPermission r4 = (com.samsung.android.game.gos.data.model.PerfDataPermission) r4     // Catch:{ ExecutionException -> 0x001c, InterruptedException -> 0x0017 }
            goto L_0x0021
        L_0x0017:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0020
        L_0x001c:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0020:
            r4 = r0
        L_0x0021:
            if (r4 == 0) goto L_0x004d
            com.samsung.android.game.gos.data.type.PerfPermissionData r0 = new com.samsung.android.game.gos.data.type.PerfPermissionData
            r0.<init>()
            java.lang.String r1 = r4.pkgName
            r0.setPkgName(r1)
            com.samsung.android.game.gos.value.RinglogPermission$PERM_POLICY[] r1 = com.samsung.android.game.gos.value.RinglogPermission.PERM_POLICY.values()
            int r2 = r4.permPolicy
            r1 = r1[r2]
            r0.setPermPolicy(r1)
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES[] r1 = com.samsung.android.game.gos.value.RinglogPermission.PERM_TYPES.values()
            int r2 = r4.permType
            r1 = r1[r2]
            r0.setPermType(r1)
            java.lang.String r1 = r4.paramListCsv
            r0.setParamListCsv(r1)
            java.lang.String r4 = r4.lastHandshakeTime
            r0.setLastHandshakeTime(r4)
        L_0x004d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper.getPermissionsForPkg(java.lang.String):com.samsung.android.game.gos.data.type.PerfPermissionData");
    }

    public boolean removeSettingsAccessiblePackage(String str) {
        return this.settingsAccessiblePackageDao.delete(str) > 0;
    }

    public List<String> getSettableFeatures(String str) {
        ArrayList arrayList = new ArrayList();
        List<String> features = this.settingsAccessiblePackageDao.getFeatures(str);
        if (features != null) {
            arrayList.addAll(features);
        }
        return arrayList;
    }

    public long getFeatureSettersCount(String str) {
        return this.settingsAccessiblePackageDao.getSettingsAccessiblePackageCount(str);
    }

    private static class LoggingPolicy {
        private static final String KEY_GAME_PERFORMANCE = "game_performance";
        private static final String KEY_MAX_SESSION = "max_session";
        private static final String KEY_TEST_GROUP_NAME = "test_group_name";
        private static final int MAX_SESSION_IN_SEC_DEFAULT = 3600;
        private int mMaxSessionInSec = 3600;
        private String mPolicyStr = null;
        private String mTestGroupName = null;

        LoggingPolicy(String str) {
            JSONObject jSONObject = null;
            this.mPolicyStr = str;
            if (str != null) {
                try {
                    jSONObject = new JSONObject(this.mPolicyStr);
                } catch (JSONException unused) {
                    String access$300 = GlobalDbHelper.LOG_TAG;
                    GosLog.w(access$300, "Failed to instantiate JSONObject. loggingPolicyStr: " + str);
                }
            }
            if (jSONObject != null) {
                if (jSONObject.has("test_group_name")) {
                    this.mTestGroupName = jSONObject.optString("test_group_name");
                }
                if (jSONObject.has(KEY_GAME_PERFORMANCE)) {
                    JSONObject optJSONObject = jSONObject.optJSONObject(KEY_GAME_PERFORMANCE);
                    if (optJSONObject.has(KEY_MAX_SESSION)) {
                        this.mMaxSessionInSec = optJSONObject.optInt(KEY_MAX_SESSION, 3600);
                    }
                }
            }
            String access$3002 = GlobalDbHelper.LOG_TAG;
            GosLog.d(access$3002, "LoggingPolicy(). mTestGroupName: " + this.mTestGroupName + ", mMaxSessionInSec: " + this.mMaxSessionInSec);
        }

        public String getPolicyStr() {
            return this.mPolicyStr;
        }

        public String getTestGroupName() {
            return this.mTestGroupName;
        }

        public int getMaxSessionInSec() {
            return this.mMaxSessionInSec;
        }
    }

    public int getIpmDefaultTemperature() {
        return Global.DefaultGlobalData.getIpmDefaultTemperature();
    }

    public String getUUID() {
        String uuid = this.globalDao.getUUID();
        if (uuid == null || uuid.equals(BuildConfig.VERSION_NAME) || uuid.contains("-")) {
            uuid = UUID.randomUUID().toString().replace("-", BuildConfig.VERSION_NAME);
            this.globalDao.setUUID(new Global.IdAndUuid(uuid));
        }
        String str = LOG_TAG;
        GosLog.d(str, "getUUID(), UUID: " + uuid);
        return uuid;
    }

    public String getRinglogMode() {
        return this.mRinglogMode;
    }

    public void updateRinglogPolicy() {
        JsonElement jsonElement;
        String ringlogPolicy = this.globalDao.getRinglogPolicy();
        String str = null;
        if (ringlogPolicy != null) {
            String str2 = LOG_TAG;
            GosLog.d(str2, "updateRinglogPolicy(), ringlogPolicy: " + ringlogPolicy);
            try {
                JsonElement parse = new JsonParser().parse(ringlogPolicy);
                if (!(parse == null || (jsonElement = parse.getAsJsonObject().get(GfiPolicy.KEY_INTERPOLATION_MODE)) == null)) {
                    str = jsonElement.getAsString();
                }
            } catch (Exception e) {
                String str3 = LOG_TAG;
                GosLog.w(str3, "updateRinglogPolicy(), " + e.getMessage());
            }
        }
        this.mRinglogMode = str;
        String str4 = LOG_TAG;
        GosLog.d(str4, "updateRinglogPolicy(), mode: " + this.mRinglogMode);
    }

    public void toStringForDebug() {
        GosLog.d(LOG_TAG, "\n========= GlobalSettings =================================== \n| IpmMode: " + this.globalDao.getIpmMode() + ", IpmFlag: " + this.globalDao.getIpmFlag() + "\n| EachModeDss: " + this.globalDao.getEachModeDss() + ", EachModeDfs: " + this.globalDao.getEachModeDfs() + "\n============================================================ \n");
    }
}
