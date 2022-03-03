package com.samsung.android.game.gos.controller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.ArrayMap;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.FeatureFlagUtil;
import com.samsung.android.game.gos.data.LocalCache;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dao.PackageDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.EventSubscriptionDbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.feature.clearbgprocess.ClearBGProcessFeature;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.ipm.Profile;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.network.response.BaseResponse;
import com.samsung.android.game.gos.network.response.CategoryResponse;
import com.samsung.android.game.gos.network.response.PerfPolicyResponse;
import com.samsung.android.game.gos.network.response.SosPolicyResponse;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.NetworkUtil;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.util.SecureFolderUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.BadHardcodedConstant;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.SiopModeConstant;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DataUpdater {
    private static final String LOG_TAG = "DataUpdater";

    public enum PkgUpdateType {
        ALL,
        MANAGED_PACKAGES_AND_UNIDENTIFIED_AND_UNKNOWN,
        UNIDENTIFIED_AND_UNKNOWN
    }

    private static boolean isNotEqual(int i, int i2) {
        return i != i2;
    }

    public static boolean updateGlobalSettingsFromServer(NetworkConnector networkConnector, String str) {
        int i;
        int i2 = 0;
        if (networkConnector == null) {
            GosLog.e(LOG_TAG, "updateGlobalSettingsFromServer(), networkConnector is null!");
            return false;
        }
        GosLog.v(LOG_TAG, "updateGlobalSettingsFromServer()");
        PerfPolicyResponse globalPerfPolicyData = networkConnector.getGlobalPerfPolicyData(str);
        if (globalPerfPolicyData == null) {
            return false;
        }
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        PackageDao packageDao = DbHelper.getInstance().getPackageDao();
        Gson gson = new Gson();
        ArrayList arrayList = new ArrayList();
        for (String str2 : packageDao.getAllPkgNameList()) {
            Package packageR = packageDao.getPackage(str2);
            if (packageR != null) {
                arrayList.add(packageR);
            }
        }
        Map<String, FeatureFlag> serverFeatureFlag = globalPerfPolicyData.getServerFeatureFlag();
        GlobalDbHelper.getInstance().mergeFeatureFlagList(serverFeatureFlag);
        GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() newFeatureFlag: " + printFeatureFlagLog(serverFeatureFlag.values()));
        _updateGlobalDssDfs(globalPerfPolicyData, globalDao);
        _updateGlobalSsrm(globalPerfPolicyData, globalDao, arrayList);
        _updateGlobalGovernor(globalPerfPolicyData, globalDao);
        _updateGlobalSpa(globalPerfPolicyData, globalDao, gson);
        _updateGlobalBoost(globalPerfPolicyData, globalDao, gson);
        _updateGlobalWifiQos(globalPerfPolicyData, globalDao, gson);
        _updateGlobalRinglog(globalPerfPolicyData, globalDao, gson);
        _updateGlobalSelectiveTarget(globalPerfPolicyData, gson);
        if (globalPerfPolicyData.loggingPolicy != null) {
            String jsonObject = globalPerfPolicyData.loggingPolicy.toString();
            if (isNotEqual(globalDao.getLoggingPolicy(), jsonObject)) {
                GosLog.d(LOG_TAG, "logging_policy is changed");
                globalDao.setLoggingPolicy(new Global.IdAndLoggingPolicy(jsonObject));
            }
        } else {
            globalDao.setLoggingPolicy(new Global.IdAndLoggingPolicy((String) null));
            GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback loggingPolicy!");
        }
        if (globalPerfPolicyData.gfiResponse != null) {
            String json = gson.toJson((Object) globalPerfPolicyData.gfiResponse);
            if (gson.toJsonTree(globalPerfPolicyData.gfiResponse).isJsonObject()) {
                JsonObject jsonObject2 = (JsonObject) gson.toJsonTree(globalPerfPolicyData.gfiResponse);
                jsonObject2.remove("state");
                json = gson.toJson((JsonElement) jsonObject2);
                if (json.length() < 3) {
                    GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback gfiPolicy!");
                    json = null;
                }
            }
            if (isNotEqual(globalDao.getGfiPolicy(), json)) {
                GosLog.d(LOG_TAG, "gfi is changed");
                globalDao.setGfiPolicy(new Global.IdAndGfiPolicy(json));
            }
        } else {
            globalDao.setGfiPolicy(new Global.IdAndGfiPolicy((String) null));
            GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback gfiPolicy!");
        }
        if (globalPerfPolicyData.vrrResponse == null || globalPerfPolicyData.vrrResponse.displayHz == null) {
            i = 0;
        } else {
            int intValue = globalPerfPolicyData.vrrResponse.displayHz.max != null ? globalPerfPolicyData.vrrResponse.displayHz.max.intValue() : 0;
            if (globalPerfPolicyData.vrrResponse.displayHz.min != null) {
                i2 = globalPerfPolicyData.vrrResponse.displayHz.min.intValue();
            }
            int i3 = intValue;
            i = i2;
            i2 = i3;
        }
        globalDao.setVrrValues(new Global.IdAndVrr(i2, i));
        int i4 = -1;
        if (!(globalPerfPolicyData.vrrResponse == null || globalPerfPolicyData.vrrResponse.drrAllowed == null)) {
            i4 = globalPerfPolicyData.vrrResponse.drrAllowed.booleanValue();
        }
        globalDao.setDrrAllowed(new Global.IdAndDrrAllowed(i4));
        if (globalPerfPolicyData.tspResponse != null) {
            String json2 = gson.toJson((Object) globalPerfPolicyData.tspResponse);
            if (gson.toJsonTree(globalPerfPolicyData.tspResponse).isJsonObject()) {
                JsonObject jsonObject3 = (JsonObject) gson.toJsonTree(globalPerfPolicyData.tspResponse);
                jsonObject3.remove("state");
                json2 = gson.toJson((JsonElement) jsonObject3);
            }
            if (isNotEqual(globalDao.getTspPolicy(), json2)) {
                GosLog.d(LOG_TAG, "tspPolicy is changed");
                globalDao.setTspPolicy(new Global.IdAndTspPolicy(json2));
            }
        } else {
            globalDao.setTspPolicy(new Global.IdAndTspPolicy((String) null));
            GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback tspPolicy!");
        }
        if (globalPerfPolicyData.clearBgProcess == null || globalPerfPolicyData.clearBgProcess.surviveAppList == null) {
            globalDao.setClearBGSurviveAppFromServer(new Global.IdAndClearBGSurviveAppFromServer(BuildConfig.VERSION_NAME));
            GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback clearBGSurviveAppFromServer!");
        } else {
            String stringsToCsv = TypeConverter.stringsToCsv((Iterable<String>) globalPerfPolicyData.clearBgProcess.surviveAppList);
            if (isNotEqual(globalDao.getClearBGSurviveAppFromServer(), stringsToCsv)) {
                GosLog.d(LOG_TAG, "clearBGSurviveAppFromServer is changed");
                globalDao.setClearBGSurviveAppFromServer(new Global.IdAndClearBGSurviveAppFromServer(stringsToCsv));
            }
        }
        ClearBGProcessFeature.getInstance().reloadSurviveAppList();
        GosLog.d(LOG_TAG, "GlobalData is changed");
        GosLog.v(LOG_TAG, "packageList size: " + arrayList.size());
        DbHelper.getInstance().getPackageDao().insertOrUpdate((List<Package>) arrayList);
        return true;
    }

    private static void _updateGlobalDssDfs(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao) {
        if (perfPolicyResponse.resolution == null || perfPolicyResponse.resolution.type == null) {
            GlobalDbHelper.getInstance().setEachModeDss(Global.DefaultGlobalData.getEachModeDss());
            globalDao.setEachModeTargetShortSide(new Global.IdAndEachModeTargetShortSide(Global.DefaultGlobalData.getEachModeTargetShortSide()));
        } else if (Constants.RESOLUTION_TYPE_DSS.equals(perfPolicyResponse.resolution.type)) {
            globalDao.setEachModeTargetShortSide(new Global.IdAndEachModeTargetShortSide((String) null));
            if (perfPolicyResponse.resolution.modeValues != null) {
                String eachModeFloatValueCsv = perfPolicyResponse.resolution.getEachModeFloatValueCsv();
                if (isNotEqual(globalDao.getEachModeDss(), eachModeFloatValueCsv)) {
                    globalDao.setEachModeDss(new Global.IdAndEachModeDss(eachModeFloatValueCsv));
                }
            } else {
                GlobalDbHelper.getInstance().setEachModeDss(Global.DefaultGlobalData.getEachModeDss());
            }
        } else if (perfPolicyResponse.resolution.modeValues != null) {
            String eachModeIntValueCsv = perfPolicyResponse.resolution.getEachModeIntValueCsv();
            if (isNotEqual(globalDao.getEachModeTargetShortSide(), eachModeIntValueCsv)) {
                GosLog.d(LOG_TAG, "EachModeTargetShortSide is changed");
                globalDao.setEachModeTargetShortSide(new Global.IdAndEachModeTargetShortSide(eachModeIntValueCsv));
            }
        } else {
            globalDao.setEachModeTargetShortSide(new Global.IdAndEachModeTargetShortSide(Global.DefaultGlobalData.getEachModeTargetShortSide()));
        }
        if (perfPolicyResponse.dfs == null) {
            GlobalDbHelper.getInstance().setEachModeDfs(Global.DefaultGlobalData.getEachModeDfs());
        } else if (perfPolicyResponse.dfs.modeValues != null) {
            String eachModeFloatValueCsv2 = perfPolicyResponse.dfs.getEachModeFloatValueCsv();
            if (isNotEqual(globalDao.getEachModeDfs(), eachModeFloatValueCsv2)) {
                globalDao.setEachModeDfs(new Global.IdAndEachModeDfs(eachModeFloatValueCsv2));
            }
        } else {
            GlobalDbHelper.getInstance().setEachModeDfs(Global.DefaultGlobalData.getEachModeDfs());
        }
    }

    private static void _updateGlobalSsrm(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao, List<Package> list) {
        boolean z;
        String str = null;
        boolean z2 = true;
        if (perfPolicyResponse.siopResponse != null) {
            JsonObject jsonObject = new JsonObject();
            if (perfPolicyResponse.siopResponse.defaultMode != null) {
                if (isNotEqual(globalDao.getSiopMode(), perfPolicyResponse.siopResponse.defaultMode.intValue())) {
                    if (globalDao.getPrevSiopModeByUser() == -1000) {
                        GosLog.d(LOG_TAG, "default_mode of SiopMode is changed and user has never set.");
                        globalDao.setSiopMode(new Global.IdAndSiopMode(perfPolicyResponse.siopResponse.defaultMode.intValue()));
                    } else {
                        GosLog.d(LOG_TAG, "SiopMode is set by user. ");
                    }
                }
                jsonObject.addProperty(SiopModeConstant.POLICY_KEY_DEFAULT_MODE, (Number) perfPolicyResponse.siopResponse.defaultMode);
            }
            boolean __updateGlobalCpuLevel = __updateGlobalCpuLevel(perfPolicyResponse, globalDao, jsonObject, false);
            if (perfPolicyResponse.siopResponse.gpu == null || perfPolicyResponse.siopResponse.gpu.defaultLevel == null) {
                globalDao.setDefaultGpuLevel(new Global.IdAndDefaultGpuLevel(Global.DefaultGlobalData.getDefaultGpuLevel()));
            } else {
                int intValue = perfPolicyResponse.siopResponse.gpu.defaultLevel.intValue();
                if (isNotEqual(globalDao.getDefaultGpuLevel(), intValue)) {
                    GosLog.d(LOG_TAG, "DefaultGpuLevel is changed");
                    globalDao.setDefaultGpuLevel(new Global.IdAndDefaultGpuLevel(intValue));
                } else {
                    z2 = false;
                }
            }
            String jsonObject2 = jsonObject.toString();
            if (jsonObject.entrySet().size() != 0) {
                str = jsonObject2;
            }
            if (isNotEqual(globalDao.getSiopModePolicy(), str)) {
                GosLog.d(LOG_TAG, "game_performance_siop is changed");
                globalDao.setSiopModePolicy(new Global.IdAndSiopModePolicy(str));
            }
            z = z2;
            z2 = __updateGlobalCpuLevel;
        } else {
            globalDao.setSiopModePolicy(new Global.IdAndSiopModePolicy((String) null));
            globalDao.setDefaultCpuLevel(new Global.IdAndDefaultCpuLevel(Global.DefaultGlobalData.getDefaultCpuLevel()));
            globalDao.setDefaultGpuLevel(new Global.IdAndDefaultGpuLevel(Global.DefaultGlobalData.getDefaultGpuLevel()));
            GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback siopModePolicy!");
            z = true;
        }
        if (z2) {
            for (Package next : list) {
                if (next != null) {
                    next.setDefaultCpuLevel(globalDao.getDefaultCpuLevel());
                }
            }
        }
        if (z) {
            for (Package next2 : list) {
                if (next2 != null) {
                    next2.setDefaultGpuLevel(globalDao.getDefaultGpuLevel());
                }
            }
        }
    }

    static boolean __updateGlobalCpuLevel(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao, JsonObject jsonObject, boolean z) {
        if (perfPolicyResponse.siopResponse.cpu == null || perfPolicyResponse.siopResponse.cpu.defaultLevel == null) {
            globalDao.setDefaultCpuLevel(new Global.IdAndDefaultCpuLevel(Global.DefaultGlobalData.getDefaultCpuLevel()));
            return true;
        }
        int intValue = perfPolicyResponse.siopResponse.cpu.defaultLevel.intValue();
        if (isNotEqual(globalDao.getDefaultCpuLevel(), intValue)) {
            GosLog.d(LOG_TAG, "DefaultCpuLevel is changed");
            globalDao.setDefaultCpuLevel(new Global.IdAndDefaultCpuLevel(intValue));
            z = true;
        }
        if (perfPolicyResponse.siopResponse.cpu.modeValues != null) {
            if (perfPolicyResponse.siopResponse.cpu.modeValues.plus != null) {
                jsonObject.addProperty("cpu_level_for_mode_1", perfPolicyResponse.siopResponse.cpu.modeValues.plus);
            }
            if (perfPolicyResponse.siopResponse.cpu.modeValues.zero != null) {
                jsonObject.addProperty("cpu_level_for_mode_0", perfPolicyResponse.siopResponse.cpu.modeValues.zero);
            }
            if (perfPolicyResponse.siopResponse.cpu.modeValues.minus != null) {
                jsonObject.addProperty("cpu_level_for_mode_-1", perfPolicyResponse.siopResponse.cpu.modeValues.minus);
            }
        }
        return z;
    }

    static void _updateGlobalGovernor(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao) {
        String str = null;
        if (perfPolicyResponse.governorSettings != null) {
            perfPolicyResponse.governorSettings.remove("state");
            String jsonObject = perfPolicyResponse.governorSettings.toString();
            if (perfPolicyResponse.governorSettings.entrySet().size() != 0) {
                str = jsonObject;
            }
            if ("c9ltechn".equalsIgnoreCase(AppVariable.getOriginalDeviceName()) && str == null) {
                str = "{\"little_hispeed_freq\":\"1190400\",\"little_target_loads\":\"80\"}";
                GosLog.d(LOG_TAG, "c9ltechn device with no server value. use predefined value: " + str);
            }
            if (isNotEqual(globalDao.getGovernorSettings(), str)) {
                GosLog.d(LOG_TAG, "default GovernorSettings is changed");
                globalDao.setGovernorSettings(new Global.IdAndGovernorSettings(str));
                return;
            }
            return;
        }
        globalDao.setGovernorSettings(new Global.IdAndGovernorSettings((String) null));
        GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback governorSettings!");
    }

    static void _updateGlobalSpa(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao, Gson gson) {
        int i;
        if (perfPolicyResponse.spaResponse != null) {
            String json = gson.toJson((Object) perfPolicyResponse.spaResponse);
            if (isNotEqual(globalDao.getIpmPolicy(), json)) {
                GosLog.d(LOG_TAG, "game_performance_spa is changed");
                globalDao.setIpmPolicy(new Global.IdAndIpmPolicy(json));
            }
            IpmCore instanceUnsafe = IpmCore.getInstanceUnsafe();
            if (globalDao.getSiopMode() != 0 || instanceUnsafe == null) {
                i = globalDao.getIpmMode();
            } else {
                i = instanceUnsafe.getProfile().toInt();
            }
            if (perfPolicyResponse.spaResponse.getMode() == null) {
                globalDao.setIpmMode(new Global.IdAndIpmMode(Global.DefaultGlobalData.getIpmMode()));
            } else if (isNotEqual(i, perfPolicyResponse.spaResponse.getMode().intValue())) {
                globalDao.setIpmMode(new Global.IdAndIpmMode(perfPolicyResponse.spaResponse.getMode().intValue()));
            }
            if (instanceUnsafe != null) {
                instanceUnsafe.setProfile(Profile.fromInt(globalDao.getIpmMode()));
            }
            if (perfPolicyResponse.spaResponse.getIpmTargetPstDefault() == null) {
                globalDao.setIpmTargetTemperature(new Global.IdAndIpmTargetTemperature(Global.DefaultGlobalData.getIpmTargetTemperature()));
            } else if (isNotEqual(globalDao.getIpmTargetTemperature(), Integer.parseInt(perfPolicyResponse.spaResponse.getIpmTargetPstDefault()))) {
                globalDao.setIpmTargetTemperature(new Global.IdAndIpmTargetTemperature(Integer.parseInt(perfPolicyResponse.spaResponse.getIpmTargetPstDefault())));
            }
        } else {
            globalDao.setIpmPolicy(new Global.IdAndIpmPolicy((String) null));
            globalDao.setIpmMode(new Global.IdAndIpmMode(Global.DefaultGlobalData.getIpmMode()));
            IpmCore instanceUnsafe2 = IpmCore.getInstanceUnsafe();
            if (instanceUnsafe2 != null) {
                instanceUnsafe2.setProfile(Profile.fromInt(globalDao.getIpmMode()));
            }
            globalDao.setIpmTargetPower(new Global.IdAndIpmTargetPower(Global.DefaultGlobalData.getIpmTargetPower()));
            globalDao.setIpmTargetTemperature(new Global.IdAndIpmTargetTemperature(Global.DefaultGlobalData.getIpmTargetTemperature()));
            GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback ipmPolicy!");
        }
    }

    private static void _updateGlobalBoost(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao, Gson gson) {
        String str = null;
        if (perfPolicyResponse.boostResume != null) {
            String json = gson.toJson((Object) perfPolicyResponse.boostResume);
            if (gson.toJsonTree(perfPolicyResponse.boostResume).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.boostResume);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
                if (json.length() < 3) {
                    GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback resumeBoostPolicy!");
                    json = null;
                }
            }
            if (Arrays.asList(BadHardcodedConstant.BOOST_RESUME_NO_POLICY_DEVICE_LIST.split(",")).contains(globalDao.getDeviceName())) {
                json = null;
            }
            if (isNotEqual(globalDao.getResumeBoostPolicy(), json)) {
                GosLog.d(LOG_TAG, "boost_resume is changed");
                globalDao.setResumeBoostPolicy(new Global.IdAndResumeBoostPolicy(json));
            }
        } else {
            globalDao.setResumeBoostPolicy(new Global.IdAndResumeBoostPolicy((String) null));
            GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback resumeBoostPolicy!");
        }
        if (perfPolicyResponse.boostLaunch != null) {
            String json2 = gson.toJson((Object) perfPolicyResponse.boostLaunch);
            if (gson.toJsonTree(perfPolicyResponse.boostLaunch).isJsonObject()) {
                JsonObject jsonObject2 = (JsonObject) gson.toJsonTree(perfPolicyResponse.boostLaunch);
                jsonObject2.remove("state");
                String json3 = gson.toJson((JsonElement) jsonObject2);
                if (json3.length() < 3) {
                    GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback launchBoostPolicy!");
                } else {
                    str = json3;
                }
            } else {
                str = json2;
            }
            if (isNotEqual(globalDao.getLaunchBoostPolicy(), str)) {
                GosLog.d(LOG_TAG, "boost_launch is changed");
                globalDao.setLaunchBoostPolicy(new Global.IdAndLaunchBoostPolicy(str));
                return;
            }
            return;
        }
        globalDao.setLaunchBoostPolicy(new Global.IdAndLaunchBoostPolicy((String) null));
        GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback launchBoostPolicy!");
    }

    private static void _updateGlobalWifiQos(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao, Gson gson) {
        String str = null;
        if (perfPolicyResponse.networkWifiQos != null) {
            String json = gson.toJson((Object) perfPolicyResponse.networkWifiQos);
            if (gson.toJsonTree(perfPolicyResponse.networkWifiQos).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.networkWifiQos);
                jsonObject.remove("state");
                String json2 = gson.toJson((JsonElement) jsonObject);
                if (json2.length() < 3) {
                    GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback wifiQosPolicy!");
                } else {
                    str = json2;
                }
            } else {
                str = json;
            }
            if (isNotEqual(globalDao.getWifiQosPolicy(), str)) {
                GosLog.d(LOG_TAG, "network_wifi_qos is changed");
                globalDao.setWifiQosPolicy(new Global.IdAndWifiQosPolicy(str));
                return;
            }
            return;
        }
        globalDao.setWifiQosPolicy(new Global.IdAndWifiQosPolicy((String) null));
        GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer() rollback wifiQosPolicy!");
    }

    static void _updateGlobalRinglog(PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao, Gson gson) {
        if (perfPolicyResponse.ringlog != null) {
            String json = gson.toJson((Object) perfPolicyResponse.ringlog);
            if (gson.toJsonTree(perfPolicyResponse.ringlog).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.ringlog);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
                GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer(), received ringlogPolicy: " + json);
            }
            if (isNotEqual(globalDao.getRinglogPolicy(), json)) {
                GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer(), ringlogPolicy is changed");
                globalDao.setRinglogPolicy(new Global.IdAndRinglogPolicy(json));
                GlobalDbHelper.getInstance().updateRinglogPolicy();
                return;
            }
            return;
        }
        globalDao.setRinglogPolicy(new Global.IdAndRinglogPolicy((String) null));
        GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer(), rollback ringlogPolicy!");
        GlobalDbHelper.getInstance().updateRinglogPolicy();
    }

    private static void _updateGlobalSelectiveTarget(PerfPolicyResponse perfPolicyResponse, Gson gson) {
        PreferenceHelper preferenceHelper = new PreferenceHelper();
        boolean z = perfPolicyResponse.selectiveTargetResponse != null && State.FORCIBLY_ENABLED.equalsIgnoreCase(perfPolicyResponse.selectiveTargetResponse.state);
        GosLog.d(LOG_TAG, "updateGlobalSettingsFromServer(), hasSelectiveTargetPolicy: " + z);
        preferenceHelper.put(PreferenceHelper.PREF_SELECTIVE_TARGET_POLICY, z);
    }

    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r7v1, types: [boolean] */
    /* JADX WARNING: type inference failed for: r7v2 */
    public static void restoreFeatureSettingsToDefault(Context context, String[] strArr) {
        char c;
        GlobalFeatureFlag globalFeatureFlag;
        String[] strArr2 = strArr;
        GosLog.v(LOG_TAG, "restoreFeatureSettingsToDefault()");
        List<String> allPkgNameList = DbHelper.getInstance().getPackageDao().getAllPkgNameList();
        Global defaultGlobal = Global.getDefaultGlobal();
        GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
        ArrayList arrayList = new ArrayList();
        int length = strArr2.length;
        ? r7 = 0;
        int i = 0;
        while (i < length) {
            String str = strArr2[i];
            if (str != null) {
                globalFeatureFlagDao.setUsingUserValue(new GlobalFeatureFlag.NameAndUsingUserValue(str, r7));
                globalFeatureFlagDao.setUsingPkgValue(new GlobalFeatureFlag.NameAndUsingPkgValue(str, r7));
                if (new ArrayList(Arrays.asList(new String[]{Constants.V4FeatureFlag.GALLERY_CMH_STOP, Constants.V4FeatureFlag.EXTERNAL_SDK, "volume_control", Constants.V4FeatureFlag.BOOST_TOUCH, Constants.V4FeatureFlag.CLEAR_BG_PROCESS, "limit_bg_network"})).contains(str) && (globalFeatureFlag = globalFeatureFlagDao.get(str)) != null) {
                    GlobalFeatureFlag.NameAndEnabledFlagByUser[] nameAndEnabledFlagByUserArr = new GlobalFeatureFlag.NameAndEnabledFlagByUser[1];
                    nameAndEnabledFlagByUserArr[r7] = new GlobalFeatureFlag.NameAndEnabledFlagByUser(str, getProperDefaultValueOf(globalFeatureFlag, str));
                    globalFeatureFlagDao.setEnabledFlagByUser(nameAndEnabledFlagByUserArr);
                    arrayList.add(str);
                }
                if (str.equals(Constants.V4FeatureFlag.CLEAR_BG_PROCESS)) {
                    DbHelper.getInstance().getGlobalDao().setClearBGLruNum(new Global.IdAndClearBGLruNum(1));
                    DbHelper.getInstance().getGlobalDao().setClearBGSurviveAppFromServer(new Global.IdAndClearBGSurviveAppFromServer(BuildConfig.VERSION_NAME));
                }
                if (str.equals(Constants.V4FeatureFlag.RESOLUTION) || str.equals(Constants.RESOLUTION_TYPE_DSS) || str.equals(Constants.V4FeatureFlag.DFS) || str.equals("siop_mode")) {
                    ArrayList arrayList2 = new ArrayList();
                    for (String str2 : allPkgNameList) {
                        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str2);
                        if (str.equals(Constants.V4FeatureFlag.RESOLUTION) || str.equals(Constants.RESOLUTION_TYPE_DSS)) {
                            c = 1;
                            packageR.setCustomDss(TypeConverter.csvToFloats(defaultGlobal.eachModeDss)[1]);
                            packageR.setCustomResolutionMode(4);
                        } else {
                            c = 1;
                        }
                        if (str.equals(Constants.V4FeatureFlag.DFS)) {
                            packageR.setCustomDfs(TypeConverter.csvToFloats(defaultGlobal.eachModeDfs)[c]);
                            packageR.setCustomDfsMode(4);
                        }
                        if (str.equals("siop_mode")) {
                            packageR.setCustomSiopMode(defaultGlobal.siopMode);
                        }
                        arrayList2.add(packageR);
                        String[] strArr3 = strArr;
                        char c2 = c;
                    }
                    if (!arrayList2.isEmpty()) {
                        DbHelper.getInstance().getPackageDao().insertOrUpdate((List<Package>) arrayList2);
                    }
                }
            }
            i++;
            strArr2 = strArr;
            r7 = 0;
        }
        GosLog.d(LOG_TAG, "restoreFeatureSettingsToDefault()-Changed features: " + arrayList.toString());
    }

    static boolean getProperDefaultValueOf(GlobalFeatureFlag globalFeatureFlag, String str) {
        if (!globalFeatureFlag.inheritedFlag) {
            return globalFeatureFlag.enabledFlagByServer;
        }
        return Global.DefaultGlobalData.isEnabledByDefault(str);
    }

    static List<String> getInstalledPkgList(PackageManager packageManager) {
        if (packageManager == null) {
            GosLog.e(LOG_TAG, "pm is null");
            return null;
        }
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        if (installedPackages == null || installedPackages.size() == 0) {
            GosLog.e(LOG_TAG, "pkgAppsList size is 0");
            return null;
        }
        LocalCache localCache = new LocalCache();
        List<String> secGameFamilyPackageNames = localCache.getSecGameFamilyPackageNames();
        List<String> gamePackageNames = localCache.getGamePackageNames();
        List<String> allPkgNameList = DbHelper.getInstance().getPackageDao().getAllPkgNameList();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (PackageInfo next : installedPackages) {
            if (!(next == null || next.packageName == null)) {
                boolean z = packageManager.getLaunchIntentForPackage(next.packageName) != null;
                if (!z) {
                    arrayList2.add(next.packageName);
                }
                if (z || secGameFamilyPackageNames.contains(next.packageName) || gamePackageNames.contains(next.packageName) || allPkgNameList.contains(next.packageName)) {
                    arrayList.add(next.packageName);
                    Package packageR = DbHelper.getInstance().getPackageDao().getPackage(next.packageName);
                    if (packageR == null) {
                        packageR = new Package(next.packageName);
                    }
                    packageR.setInstalledUserIds(new Integer[]{Integer.valueOf(SeUserHandleManager.getInstance().getMyUserId())});
                    PackageDbHelper.getInstance().insertOrUpdate(packageR, PackageDbHelper.TimeStampOpt.NONE);
                }
            }
        }
        if (SecureFolderUtil.isSupportSfGMS()) {
            addInstalledSecureFolderPkgList(packageManager, arrayList, arrayList2);
        }
        return arrayList;
    }

    static void addInstalledSecureFolderPkgList(PackageManager packageManager, List<String> list, List<String> list2) {
        ArrayList<PackageInfo> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        int i = Constants.SecureFolderUserId.SECURE_FOLDER_MIN_USER_ID;
        while (true) {
            if (i > 160) {
                break;
            }
            try {
                List list3 = (List) packageManager.getClass().getMethod("getInstalledPackagesAsUser", new Class[]{Integer.TYPE, Integer.TYPE}).invoke(packageManager, new Object[]{0, Integer.valueOf(i)});
                if (list3 != null && list3.size() != 0) {
                    arrayList2.add(Integer.valueOf(i));
                    arrayList.addAll(list3);
                    break;
                }
                i++;
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        removeSecureFolderUserIdFromDb(arrayList2);
        if (arrayList.size() > 0) {
            LocalCache localCache = new LocalCache();
            List<String> secGameFamilyPackageNames = localCache.getSecGameFamilyPackageNames();
            List<String> gamePackageNames = localCache.getGamePackageNames();
            List<String> allPkgNameList = DbHelper.getInstance().getPackageDao().getAllPkgNameList();
            for (PackageInfo packageInfo : arrayList) {
                if (!(packageInfo == null || packageInfo.packageName == null || list2.contains(packageInfo.packageName))) {
                    ArrayList arrayList3 = new ArrayList(arrayList2);
                    if ((secGameFamilyPackageNames.contains(packageInfo.packageName) || gamePackageNames.contains(packageInfo.packageName) || allPkgNameList.contains(packageInfo.packageName)) && list.contains(packageInfo.packageName)) {
                        arrayList3.add(Integer.valueOf(SeUserHandleManager.getInstance().getMyUserId()));
                    }
                    Package packageR = DbHelper.getInstance().getPackageDao().getPackage(packageInfo.packageName);
                    if (packageR == null) {
                        packageR = new Package(packageInfo.packageName);
                    }
                    list.add(packageInfo.packageName);
                    packageR.setInstalledUserIds((Integer[]) arrayList3.toArray(new Integer[0]));
                    PackageDbHelper.getInstance().insertOrUpdate(packageR, PackageDbHelper.TimeStampOpt.NONE);
                }
            }
        }
    }

    static void removeSecureFolderUserIdFromDb(List<Integer> list) {
        for (String next : DbHelper.getInstance().getPackageDao().getAllPkgNameList()) {
            Package packageR = DbHelper.getInstance().getPackageDao().getPackage(next);
            if (!(packageR == null || packageR.getInstalledUserIds() == null)) {
                for (Integer intValue : packageR.getInstalledUserIds()) {
                    int intValue2 = intValue.intValue();
                    if (intValue2 != SeUserHandleManager.getInstance().getMyUserId() && !list.contains(Integer.valueOf(intValue2))) {
                        SystemEventReactor.onPackageRemoved(AppContext.get(), next, intValue2, (NetworkConnector) null);
                    }
                }
            }
        }
    }

    static List<Package> preparePkgListToBeAdded(List<String> list) {
        LocalCache localCache = new LocalCache();
        ArrayList arrayList = new ArrayList();
        Map<String, Package> packageMap = PackageDbHelper.getInstance().getPackageMap(list);
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry next : packageMap.entrySet()) {
            String str = (String) next.getKey();
            Package packageR = (Package) next.getValue();
            if (packageR == null || packageR.getCategoryCode().equals(Constants.CategoryCode.UNDEFINED)) {
                if (packageR == null) {
                    packageR = new Package(str, Constants.CategoryCode.UNDEFINED);
                }
                Integer[] numArr = {Integer.valueOf(SeUserHandleManager.getInstance().getMyUserId())};
                if (packageR.getInstalledUserIds() == null || packageR.getInstalledUserIds().length == 0) {
                    packageR.setInstalledUserIds(numArr);
                }
                if (localCache.getSecGameFamilyPackageNames().contains(str)) {
                    packageR.setCategoryCode(Constants.CategoryCode.SEC_GAME_FAMILY);
                    arrayList2.add(str + " as sec_game_family");
                } else if (localCache.getGamePackageNames().contains(str)) {
                    packageR.setCategoryCode(Constants.CategoryCode.GAME);
                    arrayList2.add(str + " as game");
                } else if (PackageUtil.isVrApp(AppContext.get(), str)) {
                    packageR.setCategoryCode(Constants.CategoryCode.VR_GAME);
                    arrayList2.add(str + " as vr_game");
                }
                arrayList.add(packageR);
            } else if (localCache.getSecGameFamilyPackageNames().contains(str) && !packageR.getCategoryCode().equals(Constants.CategoryCode.SEC_GAME_FAMILY)) {
                arrayList.add(new Package(str, Constants.CategoryCode.SEC_GAME_FAMILY, new Integer[]{Integer.valueOf(SeUserHandleManager.getInstance().getMyUserId())}));
            }
        }
        if (!arrayList2.isEmpty()) {
            GosLog.d(LOG_TAG, "preparePkgListToBeAdded(), Set by local_cache/manifest: " + arrayList2.toString());
        }
        return arrayList;
    }

    static void syncPackageList() {
        GosLog.d(LOG_TAG, "syncPackageList()");
        List<String> installedPkgList = getInstalledPkgList(AppContext.get().getPackageManager());
        if (installedPkgList != null) {
            ArrayList arrayList = new ArrayList();
            for (String next : DbHelper.getInstance().getPackageDao().getAllPkgNameList()) {
                if (!installedPkgList.contains(next)) {
                    arrayList.add(next);
                }
            }
            PackageDbHelper.getInstance().removePkgList(arrayList);
            List<Package> preparePkgListToBeAdded = preparePkgListToBeAdded(installedPkgList);
            GosLog.i(LOG_TAG, "request insert for " + preparePkgListToBeAdded.size());
            PackageDbHelper.getInstance().insertOrUpdate(preparePkgListToBeAdded, PackageDbHelper.TimeStampOpt.ADD_MISSING_PKG_DATA);
        }
    }

    private static boolean checkDeviceRegistrationStatus(GlobalDao globalDao, NetworkConnector networkConnector, String str) {
        boolean isRegisteredDevice = globalDao.isRegisteredDevice();
        GosLog.i(LOG_TAG, "updateGlobalAndPkgData(), isRegisteredDevice: " + isRegisteredDevice);
        if (isRegisteredDevice) {
            return true;
        }
        boolean z = 200 == networkConnector.isSupportedDeviceFromServer(str);
        if (z) {
            GosLog.i(LOG_TAG, "updateGlobalAndPkgData(), setRegisteredDevice is call!");
            globalDao.setRegisteredDevice(new Global.IdAndRegisteredDevice(true));
        } else {
            GosLog.i(LOG_TAG, "Not registered");
        }
        return z;
    }

    public static boolean updateGlobalAndPkgData(Context context, PkgUpdateType pkgUpdateType, boolean z, NetworkConnector networkConnector, String str) {
        boolean z2;
        boolean z3;
        GosLog.d(LOG_TAG, "updateGlobalAndPkgData(), begin");
        LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, "updateGlobalAndPkgData(), begin");
        if (pkgUpdateType == null) {
            GosLog.w(LOG_TAG, "updateGlobalAndPkgData(), pkgUpdateType is null");
            return false;
        } else if (networkConnector == null) {
            GosLog.w(LOG_TAG, "updateGlobalAndPkgData(), networkConnector is null");
            return false;
        } else {
            if (!AppVariable.isUnitTest()) {
                syncPackageList();
            }
            GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
            boolean isAutomaticUpdate = globalDao.isAutomaticUpdate();
            if (!z && !isAutomaticUpdate) {
                return false;
            }
            boolean checkDeviceRegistrationStatus = checkDeviceRegistrationStatus(globalDao, networkConnector, str);
            long currentTimeMillis = System.currentTimeMillis();
            if (checkDeviceRegistrationStatus) {
                z2 = updateGlobalSettingsFromServer(networkConnector, str);
                int i = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$controller$DataUpdater$PkgUpdateType[pkgUpdateType.ordinal()];
                if (i == 1) {
                    List<String> allPkgNameList = DbHelper.getInstance().getPackageDao().getAllPkgNameList();
                    if (allPkgNameList != null && updatePackageList(context, networkConnector, allPkgNameList, str)) {
                        GosLog.i(LOG_TAG, "updateGlobalAndPkgData(), fully updated at " + currentTimeMillis);
                        globalDao.setFullyUpdateTime(new Global.IdAndFullyUpdateTime(currentTimeMillis));
                    }
                    z3 = false;
                    GmsGlobalPackageDataSetter.getInstance().applyAllGames(true);
                } else if (i != 2) {
                    if (i == 3) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.UNDEFINED));
                        arrayList.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory("unknown"));
                        if (arrayList.size() > 0 && updatePackageList(context, networkConnector, arrayList, str)) {
                            GosLog.i(LOG_TAG, "updateGlobalAndPkgData(), unidentified pkg list is updated at " + currentTimeMillis);
                        }
                    }
                    z3 = false;
                    GmsGlobalPackageDataSetter.getInstance().applyAllGames(true);
                } else {
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
                    arrayList2.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.MANAGED_APP));
                    arrayList2.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.UNDEFINED));
                    arrayList2.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory("unknown"));
                    if (arrayList2.size() > 0 && updatePackageList(context, networkConnector, arrayList2, str)) {
                        GosLog.i(LOG_TAG, "updateGlobalAndPkgData(), game and unidentified pkg list is updated at " + currentTimeMillis);
                    }
                    z3 = false;
                    GmsGlobalPackageDataSetter.getInstance().applyAllGames(true);
                }
                z3 = true;
                GmsGlobalPackageDataSetter.getInstance().applyAllGames(true);
            } else {
                GosLog.w(LOG_TAG, "updateGlobalAndPkgData(). device unsupported, not query to server for packages.");
                LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, "updateGlobalAndPkgData(). device unsupported, not query to server for packages.");
                z3 = false;
                z2 = false;
            }
            if (z2) {
                globalDao.setUpdateTime(new Global.IdAndUpdateTime(currentTimeMillis));
                GosLog.i(LOG_TAG, "updateGlobalAndPkgData(), update was successful.");
            }
            if (z2 && z3) {
                EventPublisher.publishEvent((Context) AppContext.get(), EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.SERVER_SYNCED.toString()), EventSubscription.EVENTS.SERVER_SYNCED.toString(), (String) null, (Map<String, String>) null);
            }
            GlobalDbHelper.getInstance().toStringForDebug();
            if (!z2 || !z3) {
                return false;
            }
            return true;
        }
    }

    /* renamed from: com.samsung.android.game.gos.controller.DataUpdater$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$controller$DataUpdater$PkgUpdateType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.samsung.android.game.gos.controller.DataUpdater$PkgUpdateType[] r0 = com.samsung.android.game.gos.controller.DataUpdater.PkgUpdateType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$controller$DataUpdater$PkgUpdateType = r0
                com.samsung.android.game.gos.controller.DataUpdater$PkgUpdateType r1 = com.samsung.android.game.gos.controller.DataUpdater.PkgUpdateType.ALL     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$controller$DataUpdater$PkgUpdateType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.controller.DataUpdater$PkgUpdateType r1 = com.samsung.android.game.gos.controller.DataUpdater.PkgUpdateType.MANAGED_PACKAGES_AND_UNIDENTIFIED_AND_UNKNOWN     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$controller$DataUpdater$PkgUpdateType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.controller.DataUpdater$PkgUpdateType r1 = com.samsung.android.game.gos.controller.DataUpdater.PkgUpdateType.UNIDENTIFIED_AND_UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.controller.DataUpdater.AnonymousClass1.<clinit>():void");
        }
    }

    private static List<String> filterAndGetInstallerPkgNameList(Context context, List<String> list) {
        ArrayList arrayList = new ArrayList();
        List<String> secGameFamilyPackageNames = new LocalCache().getSecGameFamilyPackageNames();
        PackageManager packageManager = context.getPackageManager();
        String[] strArr = (String[]) list.toArray(new String[0]);
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (String str : strArr) {
            if (Objects.equals(str, "com.samsung.android.game.gos") || secGameFamilyPackageNames.contains(str)) {
                list.remove(str);
            } else if (!AppVariable.isUnitTest() && packageManager.getLaunchIntentForPackage(str) == null) {
                PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str);
                if (pkgData == null || !pkgData.isInstalledSecureFolder()) {
                    list.remove(str);
                    arrayList2.add(str);
                }
            } else if (PackageUtil.isVrApp(context, str)) {
                list.remove(str);
            } else {
                String str2 = null;
                try {
                    str2 = packageManager.getInstallerPackageName(str);
                } catch (Exception e) {
                    arrayList3.add(Log.getStackTraceString(e));
                }
                arrayList.add(str2);
            }
        }
        if (!arrayList2.isEmpty()) {
            PackageDbHelper.getInstance().removePkgList(arrayList2);
        }
        if (!arrayList3.isEmpty()) {
            GosLog.w(LOG_TAG, "filterAndGetInstallerPkgNameList()-exceptions: " + arrayList3.toString());
        }
        return arrayList;
    }

    public static boolean updatePackageList(Context context, NetworkConnector networkConnector, List<String> list, String str) {
        List<BaseResponse> list2;
        String str2;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList arrayList5;
        Context context2 = context;
        NetworkConnector networkConnector2 = networkConnector;
        List<String> list3 = list;
        if (list3 == null) {
            GosLog.w(LOG_TAG, "updatePackageList(), pkgNameList is null");
            return false;
        } else if (networkConnector2 == null) {
            GosLog.w(LOG_TAG, "updatePackageList(), networkConnector is null");
            return false;
        } else {
            GosLog.i(LOG_TAG, "updatePackageList(), src pkgNameList, size: " + list.size() + ", " + list3);
            if (!NetworkUtil.isNetworkConnected()) {
                return false;
            }
            List<String> filterAndGetInstallerPkgNameList = filterAndGetInstallerPkgNameList(context2, list3);
            if (list.isEmpty()) {
                GosLog.i(LOG_TAG, "updatePackageList(), there is no packages to update.");
                return true;
            }
            GosLog.i(LOG_TAG, String.format(Locale.US, "updatePackageList(), filtered pkgNameList, size: %s, %s %nfiltered installerPkgNameList, size: %s, %s", new Object[]{Integer.valueOf(list.size()), list3, Integer.valueOf(filterAndGetInstallerPkgNameList.size()), filterAndGetInstallerPkgNameList}));
            boolean isRegisteredDevice = DbHelper.getInstance().getGlobalDao().isRegisteredDevice();
            if (isRegisteredDevice || AppVariable.isUnitTest()) {
                list2 = networkConnector.getPkgResponseList(list, str);
            } else {
                list2 = null;
            }
            GosLog.i(LOG_TAG, "updatePackageList(), isRegisteredDevice: " + isRegisteredDevice);
            if (list2 == null || list2.isEmpty()) {
                GosLog.w(LOG_TAG, "updatePackageList(), pkgResponseList is wrong: " + list2);
                return false;
            }
            ArrayList arrayList6 = new ArrayList();
            ArrayList arrayList7 = new ArrayList();
            ArrayList arrayList8 = new ArrayList();
            ArrayList arrayList9 = new ArrayList();
            ArrayList arrayList10 = new ArrayList();
            ArrayList arrayList11 = new ArrayList();
            for (BaseResponse next : list2) {
                if (next != null) {
                    if (next instanceof CategoryResponse) {
                        str2 = ((CategoryResponse) next).getPkgName();
                    } else if (next instanceof PerfPolicyResponse) {
                        str2 = ((PerfPolicyResponse) next).getPkgName();
                    } else {
                        str2 = next instanceof SosPolicyResponse ? ((SosPolicyResponse) next).getPkgName() : null;
                    }
                    if (str2 != null) {
                        PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str2);
                        if (pkgData != null) {
                            arrayList = arrayList11;
                            arrayList3 = arrayList10;
                            arrayList2 = arrayList9;
                            arrayList5 = arrayList8;
                            arrayList4 = arrayList7;
                            whenLocalPkgDataExist(pkgData, next, arrayList6, arrayList7, arrayList8, arrayList9, arrayList3, arrayList);
                        } else {
                            arrayList = arrayList11;
                            arrayList3 = arrayList10;
                            arrayList2 = arrayList9;
                            arrayList5 = arrayList8;
                            arrayList4 = arrayList7;
                            createPkgDataFromServerData(str2, next, arrayList6, arrayList4, arrayList5);
                        }
                        arrayList11 = arrayList;
                        arrayList8 = arrayList5;
                        arrayList7 = arrayList4;
                        arrayList10 = arrayList3;
                        arrayList9 = arrayList2;
                    }
                }
            }
            ArrayList arrayList12 = arrayList11;
            ArrayList arrayList13 = arrayList10;
            ArrayList arrayList14 = arrayList9;
            PackageDbHelper.getInstance().insertOrUpdate((List<Package>) arrayList6, PackageDbHelper.TimeStampOpt.UPDATE_PKG_DATA);
            DbHelper.getInstance().getFeatureFlagDao().insertOrUpdate((Collection<FeatureFlag>) arrayList7);
            DbHelper.getInstance().getFeatureFlagDao().delete(arrayList8);
            if (new PreferenceHelper().getValue(PreferenceHelper.PREF_SELECTIVE_TARGET_POLICY, false) && !arrayList6.isEmpty()) {
                updateSelectiveTargetPackageList(networkConnector2, str);
            }
            new Thread(new Runnable(context2, arrayList14, arrayList13, arrayList12) {
                public final /* synthetic */ Context f$0;
                public final /* synthetic */ List f$1;
                public final /* synthetic */ List f$2;
                public final /* synthetic */ List f$3;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                public final void run() {
                    EventPublisher.publishCategoryChangedEventList(this.f$0, this.f$1, this.f$2, this.f$3);
                }
            }).start();
            GosLog.i(LOG_TAG, "updatePackageList(), successful: true");
            return true;
        }
    }

    private static void updateSelectiveTargetPackageList(NetworkConnector networkConnector, String str) {
        String pkgName;
        PkgData pkgData;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (networkConnector == null) {
            GosLog.w(LOG_TAG, "updateSelectiveTargetPackageList(), networkConnector is null");
        } else if (!NetworkUtil.isNetworkConnected()) {
            GosLog.w(LOG_TAG, "updateSelectiveTargetPackageList(), network not connected");
        } else {
            List<PerfPolicyResponse> list = null;
            if (DbHelper.getInstance().getGlobalDao().isRegisteredDevice() || AppVariable.isUnitTest()) {
                list = networkConnector.getSelectiveTargetPolicyFromServer(str);
            }
            if (list == null || list.isEmpty()) {
                GosLog.w(LOG_TAG, "updateSelectiveTargetPackageList(), pkgResponseList is wrong");
                return;
            }
            for (PerfPolicyResponse next : list) {
                if (!(next == null || (pkgName = next.getPkgName()) == null || (pkgData = PackageDbHelper.getInstance().getPkgData(pkgName)) == null)) {
                    mergeLocalPkgDataAndPolicyPkgData(pkgData, next, arrayList, arrayList2);
                }
            }
            if (arrayList.size() > 0 && arrayList2.size() > 0) {
                PackageDbHelper.getInstance().insertOrUpdate((List<Package>) arrayList, PackageDbHelper.TimeStampOpt.UPDATE_PKG_DATA);
                DbHelper.getInstance().getFeatureFlagDao().insertOrUpdate((Collection<FeatureFlag>) arrayList2);
            }
        }
    }

    static void whenLocalPkgDataExist(PkgData pkgData, BaseResponse baseResponse, List<Package> list, Collection<FeatureFlag> collection, List<FeatureFlag> list2, List<String> list3, List<String> list4, List<String> list5) {
        String categoryCode = pkgData.getCategoryCode();
        PkgData mergePackageAndNotify = mergePackageAndNotify(pkgData, baseResponse);
        String categoryCode2 = mergePackageAndNotify.getCategoryCode();
        if (!categoryCode.equalsIgnoreCase(categoryCode2)) {
            list3.add(mergePackageAndNotify.getPackageName());
            list4.add(categoryCode);
            list5.add(categoryCode2);
        }
        if (Constants.CategoryCode.GAME.equalsIgnoreCase(mergePackageAndNotify.getCategoryCode())) {
            list.add(mergePackageAndNotify.getPkg());
            Map<String, FeatureFlag> featureFlagMap = mergePackageAndNotify.getFeatureFlagMap();
            if (featureFlagMap != null) {
                FeatureFlagUtil.fillMissingFeatureFlag(featureFlagMap, mergePackageAndNotify.getPackageName());
                collection.addAll(featureFlagMap.values());
                return;
            }
            return;
        }
        list.add(mergePackageAndNotify.getPkg());
        List<FeatureFlag> byPkgName = DbHelper.getInstance().getFeatureFlagDao().getByPkgName(mergePackageAndNotify.getPackageName());
        if (byPkgName.size() > 0) {
            list2.addAll(byPkgName);
        }
    }

    private static void mergeLocalPkgDataAndPolicyPkgData(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, List<Package> list, Collection<FeatureFlag> collection) {
        PkgData mergePackageAndNotifyForSTP = mergePackageAndNotifyForSTP(pkgData, perfPolicyResponse);
        if (Constants.CategoryCode.GAME.equalsIgnoreCase(mergePackageAndNotifyForSTP.getCategoryCode())) {
            list.add(mergePackageAndNotifyForSTP.getPkg());
            Map<String, FeatureFlag> featureFlagMap = mergePackageAndNotifyForSTP.getFeatureFlagMap();
            FeatureFlagUtil.fillMissingFeatureFlag(featureFlagMap, mergePackageAndNotifyForSTP.getPackageName());
            Map<String, FeatureFlag> featureFlagMap2 = PackageDbHelper.getInstance().getFeatureFlagMap(mergePackageAndNotifyForSTP.getPackageName());
            ArrayList arrayList = new ArrayList();
            if (featureFlagMap != null && featureFlagMap2 != null) {
                for (Map.Entry next : featureFlagMap.entrySet()) {
                    if (((FeatureFlag) next.getValue()).getState().equals("none")) {
                        arrayList.add(featureFlagMap2.get(next.getKey()));
                    } else {
                        arrayList.add(next.getValue());
                    }
                }
                collection.addAll(arrayList);
            }
        }
    }

    static void createPkgDataFromServerData(String str, BaseResponse baseResponse, List<Package> list, Collection<FeatureFlag> collection, List<FeatureFlag> list2) {
        PkgData mergePackageAndNotify = mergePackageAndNotify(new PkgData(new Package(str), new ArrayMap()), baseResponse);
        list.add(mergePackageAndNotify.getPkg());
        if (Constants.CategoryCode.GAME.equalsIgnoreCase(mergePackageAndNotify.getCategoryCode())) {
            Map<String, FeatureFlag> featureFlagMap = mergePackageAndNotify.getFeatureFlagMap();
            if (featureFlagMap != null) {
                FeatureFlagUtil.fillMissingFeatureFlag(featureFlagMap, mergePackageAndNotify.getPackageName());
                collection.addAll(featureFlagMap.values());
                return;
            }
            return;
        }
        List<FeatureFlag> byPkgName = DbHelper.getInstance().getFeatureFlagDao().getByPkgName(mergePackageAndNotify.getPackageName());
        if (byPkgName.size() > 0) {
            list2.addAll(byPkgName);
        }
    }

    public static PkgData mergePackageAndNotify(PkgData pkgData, BaseResponse baseResponse) {
        String str;
        int i;
        String categoryCode = pkgData.getCategoryCode();
        if (baseResponse instanceof CategoryResponse) {
            str = ((CategoryResponse) baseResponse).getPkgType();
        } else if (baseResponse instanceof SosPolicyResponse) {
            SosPolicyResponse sosPolicyResponse = (SosPolicyResponse) baseResponse;
            pkgData.getPkg().setSosPolicy(sosPolicyResponse.getSosPkgPolicy() != null ? sosPolicyResponse.getSosPkgPolicy().toString() : null);
            pkgData.getPkg().setDefaultSetBy(2);
            str = Constants.CategoryCode.MANAGED_APP;
        } else if (baseResponse instanceof PerfPolicyResponse) {
            PerfPolicyResponse perfPolicyResponse = (PerfPolicyResponse) baseResponse;
            GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
            Gson gson = new Gson();
            pkgData.getPkg().setDefaultSetBy(2);
            pkgData.mergeFeatureFlagList(perfPolicyResponse.getServerFeatureFlag());
            Map<String, FeatureFlag> featureFlagMap = pkgData.getFeatureFlagMap();
            if (featureFlagMap != null) {
                GosLog.v(LOG_TAG, "mergePackageAndNotify()-newFeatureFlag: " + printFeatureFlagLog(featureFlagMap.values()));
            }
            _mergePackageTargetGroupName(pkgData, perfPolicyResponse);
            _mergePackageDssDfs(pkgData, perfPolicyResponse);
            _mergePackageSsrm(pkgData, perfPolicyResponse, globalDao);
            _mergePackageGovernor(pkgData, perfPolicyResponse);
            if (perfPolicyResponse.shiftTemperature == null || perfPolicyResponse.shiftTemperature.value == null) {
                pkgData.getPkg().setShiftTemperature(-1000);
                GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback shiftTemperature!");
            } else {
                pkgData.getPkg().setShiftTemperature(perfPolicyResponse.shiftTemperature.value.intValue());
            }
            _mergePackageSpa(pkgData, perfPolicyResponse, gson);
            _mergePackageGameSdk(pkgData, perfPolicyResponse);
            _mergePackageBoost(pkgData, perfPolicyResponse, gson);
            _mergePackageWifiQos(pkgData, perfPolicyResponse, gson);
            _mergePackageGfi(pkgData, perfPolicyResponse, gson);
            int i2 = 0;
            if (perfPolicyResponse.vrrResponse == null || perfPolicyResponse.vrrResponse.displayHz == null) {
                i = 0;
            } else {
                int intValue = perfPolicyResponse.vrrResponse.displayHz.max != null ? perfPolicyResponse.vrrResponse.displayHz.max.intValue() : 0;
                if (perfPolicyResponse.vrrResponse.displayHz.min != null) {
                    i2 = perfPolicyResponse.vrrResponse.displayHz.min.intValue();
                }
                int i3 = i2;
                i2 = intValue;
                i = i3;
            }
            pkgData.getPkg().vrrMaxValue = i2;
            pkgData.getPkg().vrrMinValue = i;
            int i4 = -1;
            if (!(perfPolicyResponse.vrrResponse == null || perfPolicyResponse.vrrResponse.drrAllowed == null)) {
                i4 = perfPolicyResponse.vrrResponse.drrAllowed.booleanValue();
            }
            pkgData.getPkg().setDrrAllowed(i4);
            _mergePackageTsp(pkgData, perfPolicyResponse, gson);
            GosLog.v(LOG_TAG, "mergePackageAndNotify(): " + BaseResponse.getCircuitLog(pkgData));
            str = Constants.CategoryCode.GAME;
        } else {
            str = null;
        }
        _updateCategoryAndNotify(pkgData, str, categoryCode);
        pkgData.getPkg().setSubscriberList((String) null);
        if (BadHardcodedConstant.PACKAGE_NAME_FORTNITE_INSTALLER.equals(pkgData.getPackageName())) {
            pkgData.getPkg().setSubscriberList(Constants.PACKAGE_NAME_GAME_HOME);
        }
        return pkgData;
    }

    public static PkgData mergePackageAndNotifyForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        Gson gson = new Gson();
        pkgData.mergeFeatureFlagList(perfPolicyResponse.getSTPServerFeatureFlag());
        Map<String, FeatureFlag> featureFlagMap = pkgData.getFeatureFlagMap();
        if (featureFlagMap != null) {
            GosLog.v(LOG_TAG, "mergePackageAndNotifyForSTP()-newFeatureFlag: " + printFeatureFlagLog(featureFlagMap.values()));
        }
        _mergePackageTargetGroupName(pkgData, perfPolicyResponse);
        _mergePackageDssDfsForSTP(pkgData, perfPolicyResponse);
        _mergePackageSsrmForSTP(pkgData, perfPolicyResponse, globalDao);
        _mergePackageGovernorForSTP(pkgData, perfPolicyResponse);
        if (!(perfPolicyResponse.shiftTemperature == null || perfPolicyResponse.shiftTemperature.value == null)) {
            pkgData.getPkg().setShiftTemperature(perfPolicyResponse.shiftTemperature.value.intValue());
        }
        _mergePackageSpaForSTP(pkgData, perfPolicyResponse, gson);
        _mergePackageGameSdkForSTP(pkgData, perfPolicyResponse);
        _mergePackageBoostForSTP(pkgData, perfPolicyResponse, gson);
        _mergePackageWifiQosForSTP(pkgData, perfPolicyResponse, gson);
        _mergePackageGfiForSTP(pkgData, perfPolicyResponse, gson);
        if (!(perfPolicyResponse.vrrResponse == null || perfPolicyResponse.vrrResponse.displayHz == null)) {
            if (perfPolicyResponse.vrrResponse.displayHz.max != null) {
                pkgData.getPkg().vrrMaxValue = perfPolicyResponse.vrrResponse.displayHz.max.intValue();
            }
            if (perfPolicyResponse.vrrResponse.displayHz.min != null) {
                pkgData.getPkg().vrrMinValue = perfPolicyResponse.vrrResponse.displayHz.min.intValue();
            }
        }
        if (perfPolicyResponse.vrrResponse != null) {
            if (perfPolicyResponse.vrrResponse.drrAllowed != null) {
                pkgData.getPkg().setDrrAllowed(perfPolicyResponse.vrrResponse.drrAllowed.booleanValue() ? 1 : 0);
            } else {
                pkgData.getPkg().setDrrAllowed(-1);
            }
        }
        _mergePackageTspForSTP(pkgData, perfPolicyResponse, gson);
        GosLog.v(LOG_TAG, "mergePackageAndNotifyForSTP(): " + BaseResponse.getCircuitLog(pkgData));
        return pkgData;
    }

    private static void _updateCategoryAndNotify(PkgData pkgData, String str, String str2) {
        GosLog.d(LOG_TAG, "mergePackageAndNotify(), " + pkgData.getPackageName());
        if (new LocalCache().getGamePackageNames().contains(pkgData.getPackageName())) {
            pkgData.setCategoryCode(Constants.CategoryCode.GAME);
        } else {
            pkgData.setCategoryCode(str);
            String str3 = "mergePackageAndNotify(), category change: category(" + str2 + "->" + str + ")";
            if (!Objects.equals(str2, str)) {
                GosLog.d(LOG_TAG, str3);
            } else {
                GosLog.v(LOG_TAG, str3);
            }
        }
        if (Constants.CategoryCode.GAME.equals(str2) && !Constants.CategoryCode.GAME.equals(pkgData.getCategoryCode())) {
            SeGameManager.getInstance().notifyCategoryToGameManagerNow(pkgData.getPackageName(), pkgData.getCategoryCode());
        }
    }

    private static void _mergePackageDssDfs(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        if (perfPolicyResponse.resolution == null || perfPolicyResponse.resolution.type == null) {
            pkgData.getPkg().setEachModeDss(new float[]{-1.0f, -1.0f, -1.0f, -1.0f});
            pkgData.getPkg().setEachModeTargetShortSide(new int[]{-1, -1, -1, -1});
        } else if (Constants.RESOLUTION_TYPE_DSS.equals(perfPolicyResponse.resolution.type)) {
            if (perfPolicyResponse.resolution.modeValues != null) {
                pkgData.getPkg().setEachModeDss(perfPolicyResponse.resolution.getEachModeFloatValueCsv());
            } else {
                pkgData.getPkg().setEachModeDss(new float[]{-1.0f, -1.0f, -1.0f, -1.0f});
            }
        } else if (perfPolicyResponse.resolution.modeValues != null) {
            pkgData.getPkg().setEachModeTargetShortSide(perfPolicyResponse.resolution.getEachModeIntValueCsv());
        } else {
            pkgData.getPkg().setEachModeTargetShortSide(new int[]{-1, -1, -1, -1});
        }
        if (perfPolicyResponse.dfs == null) {
            pkgData.getPkg().setEachModeDfs(new float[]{-1.0f, -1.0f, -1.0f, -1.0f});
        } else if (perfPolicyResponse.dfs.modeValues != null) {
            pkgData.getPkg().setEachModeDfs(perfPolicyResponse.dfs.getEachModeFloatValueCsv());
        } else {
            pkgData.getPkg().setEachModeDfs(new float[]{-1.0f, -1.0f, -1.0f, -1.0f});
        }
    }

    private static void _mergePackageDssDfsForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        if (!(perfPolicyResponse.resolution == null || perfPolicyResponse.resolution.type == null)) {
            if (Constants.RESOLUTION_TYPE_DSS.equals(perfPolicyResponse.resolution.type)) {
                if (perfPolicyResponse.resolution.modeValues != null) {
                    pkgData.getPkg().setEachModeDss(perfPolicyResponse.resolution.getEachModeFloatValueCsv());
                    GosLog.d(LOG_TAG, "_mergePackageDssDfsForSTP DSS : " + perfPolicyResponse.resolution.getEachModeFloatValueCsv());
                } else {
                    pkgData.getPkg().setEachModeDss(new float[]{-1.0f, -1.0f, -1.0f, -1.0f});
                }
            } else if (perfPolicyResponse.resolution.modeValues != null) {
                pkgData.getPkg().setEachModeTargetShortSide(perfPolicyResponse.resolution.getEachModeIntValueCsv());
                GosLog.d(LOG_TAG, "_mergePackageDssDfsForSTP TSS : " + perfPolicyResponse.resolution.getEachModeIntValueCsv());
            } else {
                pkgData.getPkg().setEachModeTargetShortSide(new int[]{-1, -1, -1, -1});
            }
        }
        if (perfPolicyResponse.dfs == null) {
            return;
        }
        if (perfPolicyResponse.dfs.modeValues != null) {
            pkgData.getPkg().setEachModeDfs(perfPolicyResponse.dfs.getEachModeFloatValueCsv());
            return;
        }
        pkgData.getPkg().setEachModeDfs(new float[]{-1.0f, -1.0f, -1.0f, -1.0f});
    }

    private static void _mergePackageTargetGroupName(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        if (perfPolicyResponse.targetGroupName != null) {
            GosLog.d(LOG_TAG, "_mergePackageTargetGroupName = " + perfPolicyResponse.targetGroupName);
            pkgData.getPkg().setTargetGroupName(perfPolicyResponse.targetGroupName);
            return;
        }
        pkgData.getPkg().setTargetGroupName((String) null);
    }

    private static void _mergePackageSsrm(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao) {
        String str = null;
        if (perfPolicyResponse.siopResponse != null) {
            JsonObject jsonObject = new JsonObject();
            if (perfPolicyResponse.siopResponse.cpu == null || perfPolicyResponse.siopResponse.cpu.defaultLevel == null) {
                pkgData.getPkg().setDefaultCpuLevel(globalDao.getDefaultCpuLevel());
            } else {
                pkgData.getPkg().setDefaultCpuLevel(perfPolicyResponse.siopResponse.cpu.defaultLevel.intValue());
                if (perfPolicyResponse.siopResponse.cpu.modeValues != null) {
                    if (perfPolicyResponse.siopResponse.cpu.modeValues.plus != null) {
                        jsonObject.addProperty("cpu_level_for_mode_1", perfPolicyResponse.siopResponse.cpu.modeValues.plus);
                    }
                    if (perfPolicyResponse.siopResponse.cpu.modeValues.zero != null) {
                        jsonObject.addProperty("cpu_level_for_mode_0", perfPolicyResponse.siopResponse.cpu.modeValues.zero);
                    }
                    if (perfPolicyResponse.siopResponse.cpu.modeValues.minus != null) {
                        jsonObject.addProperty("cpu_level_for_mode_-1", perfPolicyResponse.siopResponse.cpu.modeValues.minus);
                    }
                }
            }
            if (perfPolicyResponse.siopResponse.gpu == null || perfPolicyResponse.siopResponse.gpu.defaultLevel == null) {
                pkgData.getPkg().setDefaultGpuLevel(globalDao.getDefaultGpuLevel());
            } else {
                pkgData.getPkg().setDefaultGpuLevel(perfPolicyResponse.siopResponse.gpu.defaultLevel.intValue());
            }
            String jsonObject2 = jsonObject.toString();
            if (jsonObject.entrySet().size() != 0) {
                str = jsonObject2;
            }
            pkgData.getPkg().setSiopModePolicy(str);
            return;
        }
        pkgData.getPkg().setSiopModePolicy((String) null);
        pkgData.getPkg().setDefaultCpuLevel(globalDao.getDefaultCpuLevel());
        pkgData.getPkg().setDefaultGpuLevel(globalDao.getDefaultGpuLevel());
        GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback siopModePolicy!");
    }

    private static void _mergePackageSsrmForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, GlobalDao globalDao) {
        if (perfPolicyResponse.siopResponse != null) {
            JsonObject jsonObject = new JsonObject();
            if (perfPolicyResponse.siopResponse.cpu == null || perfPolicyResponse.siopResponse.cpu.defaultLevel == null) {
                pkgData.getPkg().setDefaultCpuLevel(globalDao.getDefaultCpuLevel());
            } else {
                pkgData.getPkg().setDefaultCpuLevel(perfPolicyResponse.siopResponse.cpu.defaultLevel.intValue());
                if (perfPolicyResponse.siopResponse.cpu.modeValues != null) {
                    if (perfPolicyResponse.siopResponse.cpu.modeValues.plus != null) {
                        jsonObject.addProperty("cpu_level_for_mode_1", perfPolicyResponse.siopResponse.cpu.modeValues.plus);
                    }
                    if (perfPolicyResponse.siopResponse.cpu.modeValues.zero != null) {
                        jsonObject.addProperty("cpu_level_for_mode_0", perfPolicyResponse.siopResponse.cpu.modeValues.zero);
                    }
                    if (perfPolicyResponse.siopResponse.cpu.modeValues.minus != null) {
                        jsonObject.addProperty("cpu_level_for_mode_-1", perfPolicyResponse.siopResponse.cpu.modeValues.minus);
                    }
                }
            }
            if (perfPolicyResponse.siopResponse.gpu == null || perfPolicyResponse.siopResponse.gpu.defaultLevel == null) {
                pkgData.getPkg().setDefaultGpuLevel(globalDao.getDefaultGpuLevel());
            } else {
                pkgData.getPkg().setDefaultGpuLevel(perfPolicyResponse.siopResponse.gpu.defaultLevel.intValue());
            }
            String jsonObject2 = jsonObject.toString();
            if (jsonObject.entrySet().size() == 0) {
                jsonObject2 = null;
            }
            pkgData.getPkg().setSiopModePolicy(jsonObject2);
        }
    }

    private static void _mergePackageGovernor(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        String str = null;
        if (perfPolicyResponse.governorSettings != null) {
            perfPolicyResponse.governorSettings.remove("state");
            if (perfPolicyResponse.governorSettings.entrySet().size() != 0) {
                str = perfPolicyResponse.governorSettings.toString();
            }
            pkgData.getPkg().setGovernorSettings(str);
            return;
        }
        pkgData.getPkg().setGovernorSettings((String) null);
        GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback governorSettings!");
    }

    private static void _mergePackageGovernorForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        if (perfPolicyResponse.governorSettings != null) {
            perfPolicyResponse.governorSettings.remove("state");
            String str = null;
            if (perfPolicyResponse.governorSettings.entrySet().size() != 0) {
                str = perfPolicyResponse.governorSettings.toString();
            }
            pkgData.getPkg().setGovernorSettings(str);
        }
    }

    private static void _mergePackageSpa(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        String str = null;
        if (perfPolicyResponse.spaResponse != null) {
            String json = gson.toJson((Object) perfPolicyResponse.spaResponse);
            if (gson.toJsonTree(perfPolicyResponse.spaResponse).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.spaResponse);
                jsonObject.remove("state");
                String json2 = gson.toJson((JsonElement) jsonObject);
                if (json2.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback ipmPolicy!");
                } else {
                    str = json2;
                }
            } else {
                str = json;
            }
            pkgData.getPkg().setIpmPolicy(str);
            return;
        }
        pkgData.getPkg().setIpmPolicy((String) null);
        GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback ipmPolicy!");
    }

    private static void _mergePackageSpaForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        if (perfPolicyResponse.spaResponse != null) {
            String json = gson.toJson((Object) perfPolicyResponse.spaResponse);
            if (gson.toJsonTree(perfPolicyResponse.spaResponse).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.spaResponse);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
                if (json.length() < 3) {
                    json = null;
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback ipmPolicy!");
                }
            }
            pkgData.getPkg().setIpmPolicy(json);
        }
    }

    private static void _mergePackageGameSdk(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        String str = null;
        if (perfPolicyResponse.gameSdkResponse != null) {
            perfPolicyResponse.gameSdkResponse.remove("state");
            if (perfPolicyResponse.gameSdkResponse.entrySet().size() != 0) {
                str = perfPolicyResponse.gameSdkResponse.toString();
            }
            pkgData.getPkg().setGameSdkSettings(str);
            GosLog.d(LOG_TAG, "mergePackageAndNotify() - gameSdkSettings: " + str);
            return;
        }
        pkgData.getPkg().setGameSdkSettings((String) null);
        GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback gameSdkPolicy!");
    }

    private static void _mergePackageGameSdkForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse) {
        if (perfPolicyResponse.gameSdkResponse != null) {
            perfPolicyResponse.gameSdkResponse.remove("state");
            String str = null;
            if (perfPolicyResponse.gameSdkResponse.entrySet().size() != 0) {
                str = perfPolicyResponse.gameSdkResponse.toString();
            }
            pkgData.getPkg().setGameSdkSettings(str);
            GosLog.d(LOG_TAG, "_mergePackageGameSdkForSTP() - gameSdkSettings: " + str);
        }
    }

    private static void _mergePackageBoost(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        String str = null;
        if (perfPolicyResponse.boostResume != null) {
            String json = gson.toJson((Object) perfPolicyResponse.boostResume);
            if (gson.toJsonTree(perfPolicyResponse.boostResume).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.boostResume);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
                if (json.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback resumeBoostPolicy!");
                    json = null;
                }
            }
            pkgData.getPkg().setResumeBoostPolicy(json);
        } else {
            pkgData.getPkg().setResumeBoostPolicy((String) null);
            GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback resumeBoostPolicy!");
        }
        if (perfPolicyResponse.boostLaunch != null) {
            String json2 = gson.toJson((Object) perfPolicyResponse.boostLaunch);
            if (gson.toJsonTree(perfPolicyResponse.boostLaunch).isJsonObject()) {
                JsonObject jsonObject2 = (JsonObject) gson.toJsonTree(perfPolicyResponse.boostLaunch);
                jsonObject2.remove("state");
                String json3 = gson.toJson((JsonElement) jsonObject2);
                if (json3.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback launchBoostPolicy!");
                } else {
                    str = json3;
                }
            } else {
                str = json2;
            }
            pkgData.getPkg().setLaunchBoostPolicy(str);
            return;
        }
        pkgData.getPkg().setLaunchBoostPolicy((String) null);
        GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback launchBoostPolicy!");
    }

    private static void _mergePackageBoostForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        String str = null;
        if (perfPolicyResponse.boostResume != null) {
            String json = gson.toJson((Object) perfPolicyResponse.boostResume);
            if (gson.toJsonTree(perfPolicyResponse.boostResume).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.boostResume);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
                if (json.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback resumeBoostPolicy!");
                    json = null;
                }
            }
            pkgData.getPkg().setResumeBoostPolicy(json);
        }
        if (perfPolicyResponse.boostLaunch != null) {
            String json2 = gson.toJson((Object) perfPolicyResponse.boostLaunch);
            if (gson.toJsonTree(perfPolicyResponse.boostLaunch).isJsonObject()) {
                JsonObject jsonObject2 = (JsonObject) gson.toJsonTree(perfPolicyResponse.boostLaunch);
                jsonObject2.remove("state");
                String json3 = gson.toJson((JsonElement) jsonObject2);
                if (json3.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback launchBoostPolicy!");
                } else {
                    str = json3;
                }
            } else {
                str = json2;
            }
            pkgData.getPkg().setLaunchBoostPolicy(str);
        }
    }

    private static void _mergePackageWifiQos(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        String str = null;
        if (perfPolicyResponse.networkWifiQos != null) {
            String json = gson.toJson((Object) perfPolicyResponse.networkWifiQos);
            if (gson.toJsonTree(perfPolicyResponse.networkWifiQos).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.networkWifiQos);
                jsonObject.remove("state");
                String json2 = gson.toJson((JsonElement) jsonObject);
                if (json2.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback wifiQosPolicy!");
                } else {
                    str = json2;
                }
            } else {
                str = json;
            }
            pkgData.getPkg().setWifiQosPolicy(str);
            return;
        }
        pkgData.getPkg().setWifiQosPolicy((String) null);
        GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback wifiQosPolicy!");
    }

    private static void _mergePackageWifiQosForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        if (perfPolicyResponse.networkWifiQos != null) {
            String json = gson.toJson((Object) perfPolicyResponse.networkWifiQos);
            if (gson.toJsonTree(perfPolicyResponse.networkWifiQos).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.networkWifiQos);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
                if (json.length() < 3) {
                    json = null;
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback wifiQosPolicy!");
                }
            }
            pkgData.getPkg().setWifiQosPolicy(json);
        }
    }

    private static void _mergePackageGfi(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        boolean z;
        if (pkgData == null) {
            GosLog.w(LOG_TAG, "_mergePackageGfi(), localPkgData is wrong.");
            return;
        }
        String str = null;
        if (perfPolicyResponse.gfiResponse != null) {
            String json = gson.toJson((Object) perfPolicyResponse.gfiResponse);
            if (gson.toJsonTree(perfPolicyResponse.gfiResponse).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.gfiResponse);
                if (State.FORCIBLY_ENABLED.equalsIgnoreCase(perfPolicyResponse.gfiResponse.state) || "enabled".equalsIgnoreCase(perfPolicyResponse.gfiResponse.state)) {
                    z = true;
                } else if (State.FORCIBLY_DISABLED.equalsIgnoreCase(perfPolicyResponse.gfiResponse.state) || State.DISABLED.equalsIgnoreCase(perfPolicyResponse.gfiResponse.state)) {
                    z = false;
                } else {
                    z = null;
                }
                jsonObject.remove("state");
                if (z != null) {
                    jsonObject.addProperty("enabled", z);
                }
                String json2 = gson.toJson((JsonElement) jsonObject);
                if (json2.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback gfiPolicy!");
                } else {
                    str = json2;
                }
            } else {
                str = json;
            }
            pkgData.getPkg().setGfiPolicy(str);
            return;
        }
        pkgData.getPkg().setGfiPolicy((String) null);
        GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback gfiPolicy!");
    }

    private static void _mergePackageGfiForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        boolean z;
        if (pkgData == null) {
            GosLog.w(LOG_TAG, "_mergePackageGfiForSTP(), localPkgData is wrong.");
        } else if (perfPolicyResponse.gfiResponse != null) {
            String json = gson.toJson((Object) perfPolicyResponse.gfiResponse);
            if (gson.toJsonTree(perfPolicyResponse.gfiResponse).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.gfiResponse);
                if (State.FORCIBLY_ENABLED.equalsIgnoreCase(perfPolicyResponse.gfiResponse.state) || "enabled".equalsIgnoreCase(perfPolicyResponse.gfiResponse.state)) {
                    z = true;
                } else if (State.FORCIBLY_DISABLED.equalsIgnoreCase(perfPolicyResponse.gfiResponse.state) || State.DISABLED.equalsIgnoreCase(perfPolicyResponse.gfiResponse.state)) {
                    z = false;
                } else {
                    z = null;
                }
                jsonObject.remove("state");
                if (z != null) {
                    jsonObject.addProperty("enabled", z);
                }
                json = gson.toJson((JsonElement) jsonObject);
                if (json.length() < 3) {
                    GosLog.d(LOG_TAG, "mergePackageAndNotify() rollback gfiPolicy!");
                    json = null;
                }
            }
            pkgData.getPkg().setGfiPolicy(json);
        }
    }

    private static void _mergePackageTsp(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        if (perfPolicyResponse.tspResponse != null) {
            String json = gson.toJson((Object) perfPolicyResponse.tspResponse);
            if (gson.toJsonTree(perfPolicyResponse.tspResponse).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.tspResponse);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
            }
            pkgData.getPkg().tspPolicy = json;
            return;
        }
        pkgData.getPkg().tspPolicy = null;
        GosLog.d(LOG_TAG, "mergePackageROAndNotify() rollback tspPolicy!");
    }

    private static void _mergePackageTspForSTP(PkgData pkgData, PerfPolicyResponse perfPolicyResponse, Gson gson) {
        if (perfPolicyResponse.tspResponse != null) {
            String json = gson.toJson((Object) perfPolicyResponse.tspResponse);
            if (gson.toJsonTree(perfPolicyResponse.tspResponse).isJsonObject()) {
                JsonObject jsonObject = (JsonObject) gson.toJsonTree(perfPolicyResponse.tspResponse);
                jsonObject.remove("state");
                json = gson.toJson((JsonElement) jsonObject);
            }
            pkgData.getPkg().tspPolicy = json;
        }
    }

    private static String printFeatureFlagLog(Collection<FeatureFlag> collection) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (FeatureFlag next : collection) {
            if (next != null) {
                if (i == 0 && next.getPkgName() != null) {
                    sb.append("\n");
                    sb.append("pkgName: ");
                    sb.append(next.getPkgName());
                    sb.append("\n");
                }
                sb.append("for: ");
                char c = 'T';
                sb.append(next.isForcedFlag() ? 'T' : 'F');
                sb.append(", ");
                sb.append("ena: ");
                sb.append(next.isEnabledFlagByServer() ? 'T' : 'F');
                sb.append(", ");
                sb.append("inh: ");
                sb.append(next.isInheritedFlag() ? 'T' : 'F');
                sb.append(", ");
                sb.append("ebu: ");
                sb.append(next.isEnabledFlagByUser() ? 'T' : 'F');
                sb.append(", ");
                sb.append("fin: ");
                if (!next.isEnabled()) {
                    c = 'F';
                }
                sb.append(c);
                sb.append(", ");
                if (next.getName() != null) {
                    sb.append("name: ");
                    sb.append(next.getName());
                    sb.append(", ");
                }
                if (next.getState() != null) {
                    sb.append("state: ");
                    sb.append(next.getState());
                    sb.append(", ");
                }
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }

    private static boolean isNotEqual(String str, String str2) {
        return !Objects.equals(str, str2);
    }

    private DataUpdater() {
    }
}
