package com.samsung.android.game.gos.endpoint;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Base64;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.TestDataManager;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.EventSubscriptionDbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.endpoint.GlobalCommand;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.feature.ipm.IpmFeature;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.BadHardcodedOperation;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class GlobalCommand {
    private static final String LOG_TAG = GlobalCommand.class.getSimpleName();
    /* access modifiers changed from: private */
    public static ApplyAllGamesAsyncTask asyncTaskToSetAllGames = null;

    GlobalCommand() {
    }

    /* access modifiers changed from: package-private */
    public String getGlobalDataWithJson() {
        String statistics;
        GosLog.d(LOG_TAG, "getGlobalDataWithJson()");
        try {
            JSONObject jSONObject = new JSONObject();
            GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
            Global global = DbHelper.getInstance().getGlobalDao().get();
            if (global == null) {
                GosLog.e(LOG_TAG, "getGlobalDataWithJson() currently global data is null, kill gos process.");
                LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, "getGlobalDataWithJson() currently global data is null, kill gos process.");
                Process.killProcess(Process.myPid());
                return null;
            }
            jSONObject.put(GosInterface.KeyName.DATA_READY, global.dataReady);
            if (globalFeatureFlagDao.isAvailable(Constants.V4FeatureFlag.RESOLUTION)) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_DSS, TypeConverter.floatsToCsv(GlobalDbHelper.getInstance().getEachModeDss()));
                jSONObject.put(GosInterface.KeyName.CUSTOM_RESOLUTION_MODE, global.resolutionMode);
                jSONObject.put(GosInterface.KeyName.CUSTOM_DSS_VALUE, (double) global.customDss);
            }
            if (globalFeatureFlagDao.isAvailable(Constants.V4FeatureFlag.DFS)) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_DFS, TypeConverter.floatsToCsv(GlobalDbHelper.getInstance().getEachModeDfs()));
                jSONObject.put(GosInterface.KeyName.CUSTOM_DFS_MODE, global.dfsMode);
                jSONObject.put(GosInterface.KeyName.CUSTOM_DFS_VALUE, (double) global.customDfs);
            }
            if (globalFeatureFlagDao.isAvailable("ipm")) {
                jSONObject.put("ipm", globalFeatureFlagDao.isEnabledFlagByUser("ipm"));
                jSONObject.put(GosInterface.KeyName.IPM_MODE, global.ipmMode);
                boolean[] csvToBooleans = TypeConverter.csvToBooleans(global.ipmFlag);
                char[] cArr = new char[csvToBooleans.length];
                for (int i = 0; i < csvToBooleans.length; i++) {
                    cArr[i] = csvToBooleans[i] ? '1' : '0';
                }
                jSONObject.put(GosInterface.KeyName.IPM_FLAGS, new String(cArr));
                jSONObject.put(GosInterface.KeyName.IPM_TARGET_POWER, global.ipmTargetPower);
                jSONObject.put(GosInterface.KeyName.IPM_TARGET_PST, global.ipmTargetTemperature);
                if (!AppVariable.isUnitTest() && ((PlatformUtil.isDebugBinary() || TestDataManager.isTestMode()) && (statistics = IpmCore.getInstance(AppContext.get()).getStatistics()) != null)) {
                    jSONObject.put("ipm_stats", Base64.encodeToString(statistics.getBytes(), 0));
                }
            }
            putEnabledFlagByUser(jSONObject, globalFeatureFlagDao, "volume_control", "volume_control");
            String cpuLevelsCsv = SeSysProp.getCpuGpuLevelInstance().getCpuLevelsCsv();
            String gpuLevelsCsv = SeSysProp.getCpuGpuLevelInstance().getGpuLevelsCsv();
            GosLog.d(LOG_TAG, "check CPU_AND_GPU_LEVEL. cpu:" + cpuLevelsCsv + ", gpu: " + gpuLevelsCsv);
            if (cpuLevelsCsv != null) {
                jSONObject.put(GosInterface.KeyName.AVAILABLE_CPU_LEVEL, cpuLevelsCsv);
            }
            if (gpuLevelsCsv != null) {
                jSONObject.put(GosInterface.KeyName.AVAILABLE_GPU_LEVEL, gpuLevelsCsv);
            }
            if (globalFeatureFlagDao.isAvailable("siop_mode")) {
                jSONObject.put(GosInterface.KeyName.MIN_SIOP_MODE, -1);
                jSONObject.put(GosInterface.KeyName.MAX_SIOP_MODE, 1);
                jSONObject.put("siop_mode", global.siopMode);
            }
            jSONObject.put(GosInterface.KeyName.CLEAR_BG_PROCESS_SUPPORTED, globalFeatureFlagDao.isAvailable(Constants.V4FeatureFlag.CLEAR_BG_PROCESS));
            putEnabledFlagByUser(jSONObject, globalFeatureFlagDao, GosInterface.KeyName.CLEAR_BG_PROCESS_ENABLED, Constants.V4FeatureFlag.CLEAR_BG_PROCESS);
            jSONObject.put(GosInterface.KeyName.LIMIT_BG_NETWORK_SUPPORTED, globalFeatureFlagDao.isAvailable("limit_bg_network"));
            putEnabledFlagByUser(jSONObject, globalFeatureFlagDao, GosInterface.KeyName.LIMIT_BG_NETWORK_ENABLED, "limit_bg_network");
            jSONObject.put(GosInterface.KeyName.LRU_NUM, global.clearBGLruNum);
            jSONObject.put(GosInterface.KeyName.SURVIVE_LIST, global.clearBGSurviveAppFromServer != null ? global.clearBGSurviveAppFromServer : BuildConfig.VERSION_NAME);
            jSONObject.put(GosInterface.KeyName.UUID, GlobalDbHelper.getInstance().getUUID());
            jSONObject.put(GosInterface.KeyName.DEVICE_NAME, global.deviceName);
            jSONObject.put(GosInterface.KeyName.IS_DEVICE_SUPPORTED_BY_SERVER, global.registeredDevice);
            jSONObject.put(GosInterface.KeyName.LAST_UPDATE_TIME, global.updateTime);
            jSONObject.put(GosInterface.KeyName.LAST_FULLY_UPDATE_TIME, global.fullyUpdateTime);
            jSONObject.put(GosInterface.KeyName.ALLOW_MORE_HEAT_AVAILABLE, IpmFeature.isAllowMoreHeatAvailable());
            putGlobalFeatureFlag(jSONObject);
            String jSONObject2 = jSONObject.toString();
            GosLog.i(LOG_TAG, "getGlobalDataWithJson(), response : " + jSONObject2);
            return jSONObject2;
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return null;
        }
    }

    private void putEnabledFlagByUser(JSONObject jSONObject, GlobalFeatureFlagDao globalFeatureFlagDao, String str, String str2) throws JSONException {
        if (globalFeatureFlagDao.isAvailable(str2)) {
            jSONObject.put(str, globalFeatureFlagDao.isEnabledFlagByUser(str2));
        }
    }

    private void putGlobalFeatureFlag(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        for (Map.Entry next : GlobalDbHelper.getInstance().getFeatureFlagMap().entrySet()) {
            String str = (String) next.getKey();
            GlobalFeatureFlag globalFeatureFlag = (GlobalFeatureFlag) next.getValue();
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.AVAILABLE, globalFeatureFlag.available);
            jSONObject3.put("inherited", globalFeatureFlag.inheritedFlag);
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.FORCED_BY_CODE, Global.DefaultGlobalData.isForcedByDefault(str));
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.FORCED, globalFeatureFlag.forcedFlag);
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.ENABLED_BY_CODE, Global.DefaultGlobalData.isEnabledByDefault(str));
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.ENABLED_BY_SERVER, globalFeatureFlag.enabledFlagByServer);
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.ENABLED_BY_USER, globalFeatureFlag.enabledFlagByUser);
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.USING_USER_VALUE, globalFeatureFlag.usingUserValue);
            jSONObject3.put(GosInterface.FeatureFlagKeyNames.USING_PKG_VALUE, globalFeatureFlag.usingPkgValue);
            jSONObject2.put(str, jSONObject3);
        }
        jSONObject.put(GosInterface.KeyName.FEATURE_FLAGS, jSONObject2);
    }

    /* access modifiers changed from: package-private */
    public String setGlobalDataWithJson(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        String str5 = LOG_TAG;
        GosLog.i(str5, "setGlobalDataWithJson(). " + str3);
        try {
            JSONObject jSONObject = new JSONObject(str3);
            JSONObject jSONObject2 = new JSONObject();
            StringBuilder sb = new StringBuilder();
            GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
            GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
            ArrayList arrayList = new ArrayList();
            boolean globalFeatureFlag = setGlobalFeatureFlag(false, jSONObject, globalFeatureFlagDao, sb, arrayList, str2);
            setEnabledFlagByUser("volume_control", "volume_control", jSONObject, globalFeatureFlagDao, sb, arrayList);
            boolean globalDfs = setGlobalDfs(setGlobalResolution(setGlobalSiopMode(globalFeatureFlag, jSONObject, globalDao, globalFeatureFlagDao, sb, arrayList), jSONObject, globalDao, globalFeatureFlagDao, sb, arrayList), jSONObject, globalDao, globalFeatureFlagDao, sb, arrayList);
            setEnabledFlagByUser(GosInterface.KeyName.CLEAR_BG_PROCESS_ENABLED, Constants.V4FeatureFlag.CLEAR_BG_PROCESS, jSONObject, globalFeatureFlagDao, sb, arrayList);
            setEnabledFlagByUser(GosInterface.KeyName.LIMIT_BG_NETWORK_ENABLED, "limit_bg_network", jSONObject, globalFeatureFlagDao, sb, arrayList);
            if (globalDfs) {
                if (asyncTaskToSetAllGames != null) {
                    asyncTaskToSetAllGames.cancel(true);
                }
                new Thread(new Runnable() {
                    public final void run() {
                        GlobalCommand.lambda$setGlobalDataWithJson$0(GlobalCommand.ApplyAllGamesAsyncTask.this);
                    }
                }).start();
            }
            if (sb.length() > 0) {
                jSONObject2.put(GosInterface.KeyName.SUCCESSFUL_ITEMS, sb.toString());
                Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.GLOBAL_DATA_UPDATED.toString());
                if (str4 != null) {
                    subscriberListOfEvent.remove(str4);
                }
                EventPublisher.publishEvent((Context) AppContext.get(), subscriberListOfEvent, EventSubscription.EVENTS.GLOBAL_DATA_UPDATED.toString(), (String) null, (Map<String, String>) null);
            }
            String jSONObject3 = jSONObject2.toString();
            GlobalDbHelper.getInstance().setSettingAccessiblePackage(str4, arrayList);
            return jSONObject3;
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return null;
        }
    }

    static /* synthetic */ void lambda$setGlobalDataWithJson$0(ApplyAllGamesAsyncTask applyAllGamesAsyncTask) {
        while (true) {
            ApplyAllGamesAsyncTask applyAllGamesAsyncTask2 = asyncTaskToSetAllGames;
            if (applyAllGamesAsyncTask2 == null || applyAllGamesAsyncTask2.getStatus() == AsyncTask.Status.FINISHED) {
                asyncTaskToSetAllGames = applyAllGamesAsyncTask;
                applyAllGamesAsyncTask.execute(new Void[0]);
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        asyncTaskToSetAllGames = applyAllGamesAsyncTask;
        applyAllGamesAsyncTask.execute(new Void[0]);
    }

    private boolean setGlobalFeatureFlag(boolean z, JSONObject jSONObject, GlobalFeatureFlagDao globalFeatureFlagDao, StringBuilder sb, List<String> list, String str) throws JSONException {
        JSONObject jSONObject2 = jSONObject;
        StringBuilder sb2 = sb;
        List<String> list2 = list;
        if (!jSONObject2.has(GosInterface.KeyName.FEATURE_FLAGS)) {
            return z;
        }
        Map<String, GlobalFeatureFlag> featureFlagMap = GlobalDbHelper.getInstance().getFeatureFlagMap();
        ArrayList arrayList = new ArrayList();
        JSONObject jSONObject3 = jSONObject2.getJSONObject(GosInterface.KeyName.FEATURE_FLAGS);
        if (BadHardcodedOperation.needsToBlockGfiSettingByGameBooster(str)) {
            jSONObject3.remove(Constants.V4FeatureFlag.GFI);
        }
        StringBuilder sb3 = new StringBuilder();
        ArrayList arrayList2 = new ArrayList();
        for (String next : Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES) {
            if (jSONObject3.has(next)) {
                GlobalFeatureFlag globalFeatureFlag = featureFlagMap.get(next);
                if (globalFeatureFlag == null) {
                    arrayList2.add(next);
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    JSONObject jSONObject4 = jSONObject3.getJSONObject(next);
                    if (jSONObject4.has(GosInterface.FeatureFlagKeyNames.ENABLED_BY_USER)) {
                        globalFeatureFlag.enabledFlagByUser = jSONObject4.getBoolean(GosInterface.FeatureFlagKeyNames.ENABLED_BY_USER);
                        sb4.append(GosInterface.FeatureFlagKeyNames.ENABLED_BY_USER);
                    }
                    if (jSONObject4.has(GosInterface.FeatureFlagKeyNames.USING_USER_VALUE)) {
                        globalFeatureFlag.usingUserValue = jSONObject4.getBoolean(GosInterface.FeatureFlagKeyNames.USING_USER_VALUE);
                        if (sb4.length() > 0) {
                            sb4.append(",");
                        }
                        sb4.append(GosInterface.FeatureFlagKeyNames.USING_USER_VALUE);
                    }
                    if (jSONObject4.has(GosInterface.FeatureFlagKeyNames.USING_PKG_VALUE)) {
                        globalFeatureFlag.usingPkgValue = jSONObject4.getBoolean(GosInterface.FeatureFlagKeyNames.USING_PKG_VALUE);
                        if (sb4.length() > 0) {
                            sb4.append(",");
                        }
                        sb4.append(GosInterface.FeatureFlagKeyNames.USING_PKG_VALUE);
                    }
                    if (sb4.length() > 0) {
                        arrayList.add(globalFeatureFlag);
                        if (sb3.length() > 0) {
                            sb3.append(",");
                        }
                        sb3.append(next);
                        sb3.append("(");
                        sb3.append(sb4.toString());
                        sb3.append(")");
                        if (!list2.contains(next)) {
                            list2.add(next);
                        }
                    }
                }
            }
        }
        if (arrayList2.size() > 0) {
            GosLog.w(LOG_TAG, String.format(Locale.US, "setGlobalDataWithJson(): %s is not in GFF table", new Object[]{TypeConverter.stringsToCsv((Iterable<String>) arrayList2)}));
        }
        if (sb3.length() <= 0) {
            return z;
        }
        globalFeatureFlagDao.insertOrUpdate((Collection<GlobalFeatureFlag>) arrayList);
        if (sb.length() > 0) {
            sb2.append(",");
        }
        sb2.append("feature_flags(");
        sb2.append(sb3.toString());
        sb2.append(")");
        return true;
    }

    private boolean setGlobalSiopMode(boolean z, JSONObject jSONObject, GlobalDao globalDao, GlobalFeatureFlagDao globalFeatureFlagDao, StringBuilder sb, List<String> list) throws JSONException {
        if (jSONObject.has("siop_mode") && globalFeatureFlagDao.isAvailable("siop_mode")) {
            int siopMode = globalDao.getSiopMode();
            int validSiopMode = ValidationUtil.getValidSiopMode(jSONObject.getInt("siop_mode"));
            if (validSiopMode != siopMode) {
                globalDao.setSiopMode(new Global.IdAndSiopMode(validSiopMode));
                z = true;
                globalDao.setPrevSiopModeByUser(new Global.IdAndPrevSiopModeByUser(validSiopMode));
            }
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("siop_mode");
            if (!list.contains("siop_mode")) {
                list.add("siop_mode");
            }
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006f, code lost:
        if (r10.getResolutionMode() == 4) goto L_0x0073;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean setGlobalResolution(boolean r8, org.json.JSONObject r9, com.samsung.android.game.gos.data.dao.GlobalDao r10, com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r11, java.lang.StringBuilder r12, java.util.List<java.lang.String> r13) throws org.json.JSONException {
        /*
            r7 = this;
            java.lang.String r0 = "resolution"
            boolean r1 = r11.isAvailable(r0)
            if (r1 != 0) goto L_0x0009
            return r8
        L_0x0009:
            boolean r11 = r11.isUsingUserValue(r0)
            java.lang.String r1 = "custom_resolution_mode"
            boolean r2 = r9.has(r1)
            r3 = 1
            java.lang.String r4 = ","
            if (r2 == 0) goto L_0x0046
            int r2 = r10.getResolutionMode()
            int r5 = r9.getInt(r1)
            int r5 = com.samsung.android.game.gos.util.ValidationUtil.getValidMode(r5)
            if (r5 == r2) goto L_0x0031
            com.samsung.android.game.gos.data.model.Global$IdAndResolutionMode r2 = new com.samsung.android.game.gos.data.model.Global$IdAndResolutionMode
            r2.<init>(r5)
            r10.setResolutionMode(r2)
            if (r11 == 0) goto L_0x0031
            r8 = r3
        L_0x0031:
            int r2 = r12.length()
            if (r2 <= 0) goto L_0x003a
            r12.append(r4)
        L_0x003a:
            r12.append(r1)
            boolean r1 = r13.contains(r0)
            if (r1 != 0) goto L_0x0046
            r13.add(r0)
        L_0x0046:
            java.lang.String r1 = "custom_dss_value"
            boolean r2 = r9.has(r1)
            if (r2 == 0) goto L_0x0089
            float r2 = r10.getCustomDss()
            double r5 = r9.getDouble(r1)
            float r9 = (float) r5
            float r9 = com.samsung.android.game.gos.util.ValidationUtil.getValidDss(r9)
            boolean r2 = com.samsung.android.game.gos.util.ValidationUtil.floatEqual(r9, r2)
            if (r2 != 0) goto L_0x0072
            com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper r2 = com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper.getInstance()
            r2.setCustomDss(r9)
            if (r11 == 0) goto L_0x0072
            int r9 = r10.getResolutionMode()
            r10 = 4
            if (r9 != r10) goto L_0x0072
            goto L_0x0073
        L_0x0072:
            r3 = r8
        L_0x0073:
            int r8 = r12.length()
            if (r8 <= 0) goto L_0x007c
            r12.append(r4)
        L_0x007c:
            r12.append(r1)
            boolean r8 = r13.contains(r0)
            if (r8 != 0) goto L_0x0088
            r13.add(r0)
        L_0x0088:
            r8 = r3
        L_0x0089:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.endpoint.GlobalCommand.setGlobalResolution(boolean, org.json.JSONObject, com.samsung.android.game.gos.data.dao.GlobalDao, com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao, java.lang.StringBuilder, java.util.List):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006f, code lost:
        if (r10.getDfsMode() == 4) goto L_0x0073;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean setGlobalDfs(boolean r8, org.json.JSONObject r9, com.samsung.android.game.gos.data.dao.GlobalDao r10, com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r11, java.lang.StringBuilder r12, java.util.List<java.lang.String> r13) throws org.json.JSONException {
        /*
            r7 = this;
            java.lang.String r0 = "dfs"
            boolean r1 = r11.isAvailable(r0)
            if (r1 != 0) goto L_0x0009
            return r8
        L_0x0009:
            boolean r11 = r11.isUsingUserValue(r0)
            java.lang.String r1 = "custom_dfs_mode"
            boolean r2 = r9.has(r1)
            r3 = 1
            java.lang.String r4 = ","
            if (r2 == 0) goto L_0x0046
            int r2 = r10.getDfsMode()
            int r5 = r9.getInt(r1)
            int r5 = com.samsung.android.game.gos.util.ValidationUtil.getValidMode(r5)
            if (r5 == r2) goto L_0x0031
            com.samsung.android.game.gos.data.model.Global$IdAndDfsMode r2 = new com.samsung.android.game.gos.data.model.Global$IdAndDfsMode
            r2.<init>(r5)
            r10.setDfsMode(r2)
            if (r11 == 0) goto L_0x0031
            r8 = r3
        L_0x0031:
            int r2 = r12.length()
            if (r2 <= 0) goto L_0x003a
            r12.append(r4)
        L_0x003a:
            r12.append(r1)
            boolean r1 = r13.contains(r0)
            if (r1 != 0) goto L_0x0046
            r13.add(r0)
        L_0x0046:
            java.lang.String r1 = "custom_dfs_value"
            boolean r2 = r9.has(r1)
            if (r2 == 0) goto L_0x0089
            float r2 = r10.getCustomDfs()
            double r5 = r9.getDouble(r1)
            float r9 = (float) r5
            float r9 = com.samsung.android.game.gos.util.ValidationUtil.getValidDfs(r9)
            boolean r2 = com.samsung.android.game.gos.util.ValidationUtil.floatEqual(r9, r2)
            if (r2 != 0) goto L_0x0072
            com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper r2 = com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper.getInstance()
            r2.setCustomDfs(r9)
            if (r11 == 0) goto L_0x0072
            int r9 = r10.getDfsMode()
            r10 = 4
            if (r9 != r10) goto L_0x0072
            goto L_0x0073
        L_0x0072:
            r3 = r8
        L_0x0073:
            int r8 = r12.length()
            if (r8 <= 0) goto L_0x007c
            r12.append(r4)
        L_0x007c:
            r12.append(r1)
            boolean r8 = r13.contains(r0)
            if (r8 != 0) goto L_0x0088
            r13.add(r0)
        L_0x0088:
            r8 = r3
        L_0x0089:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.endpoint.GlobalCommand.setGlobalDfs(boolean, org.json.JSONObject, com.samsung.android.game.gos.data.dao.GlobalDao, com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao, java.lang.StringBuilder, java.util.List):boolean");
    }

    private void setEnabledFlagByUser(String str, String str2, JSONObject jSONObject, GlobalFeatureFlagDao globalFeatureFlagDao, StringBuilder sb, List<String> list) throws JSONException {
        if (jSONObject.has(str) && globalFeatureFlagDao.isAvailable(str2)) {
            globalFeatureFlagDao.setEnabledFlagByUser(new GlobalFeatureFlag.NameAndEnabledFlagByUser(str2, jSONObject.getBoolean(str)));
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(str);
            if (!list.contains(str2)) {
                list.add(str2);
            }
        }
    }

    private static class ApplyAllGamesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ApplyAllGamesAsyncTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            GmsGlobalPackageDataSetter.getInstance().applyAllGames(false);
            ApplyAllGamesAsyncTask unused = GlobalCommand.asyncTaskToSetAllGames = null;
            return null;
        }
    }
}
