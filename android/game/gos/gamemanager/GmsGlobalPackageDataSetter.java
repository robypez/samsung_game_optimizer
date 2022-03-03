package com.samsung.android.game.gos.gamemanager;

import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.FeatureSetManager;
import com.samsung.android.game.gos.data.IResolutionModeChangedListener;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.StaticInterface;
import com.samsung.android.game.gos.feature.governorsettings.GovernorSettingsFeature;
import com.samsung.android.game.gos.feature.vrr.VrrFeature;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.SecureFolderUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class GmsGlobalPackageDataSetter implements IResolutionModeChangedListener {
    private static final String LOG_TAG = "GmsGlobalPackageDataSetter";
    private Collection<StaticInterface> mStaticFeatures;

    private GmsGlobalPackageDataSetter() {
        this.mStaticFeatures = null;
    }

    public static GmsGlobalPackageDataSetter getInstance() {
        SingletonHolder.INSTANCE.init();
        return SingletonHolder.INSTANCE;
    }

    public void onResolutionModeChanged(int i, boolean z) {
        try {
            applyAllGames(false);
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    private void init() {
        this.mStaticFeatures = FeatureSetManager.getStaticFeatureMap(AppContext.get()).values();
    }

    public void applyAllGames(boolean z) {
        HashMap hashMap;
        HashMap hashMap2;
        String str;
        GosLog.i(LOG_TAG, "applyAllGames(), begin, needManagerInit: " + z);
        SeGameManager instance = SeGameManager.getInstance();
        if (!z) {
            hashMap2 = null;
            hashMap = null;
        } else if (SecureFolderUtil.isSupportSfGMS()) {
            hashMap = new HashMap();
            hashMap2 = null;
        } else {
            hashMap2 = new HashMap();
            hashMap = null;
        }
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        hashSet.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
        hashSet.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.SEC_GAME_FAMILY));
        hashSet.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.MANAGED_APP));
        String str2 = "applyAllGames(), targetPkgNameSet.size(): " + hashSet.size() + ", targetPkgNameSet: " + hashSet;
        GosLog.d(LOG_TAG, str2);
        LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, str2);
        for (PkgData next : PackageDbHelper.getInstance().getPkgDataMap(hashSet).values()) {
            if (next != null) {
                applySingleGame(hashMap2, hashMap, next, arrayList);
            }
        }
        if (z && instance != null) {
            if (SecureFolderUtil.isSupportSfGMS()) {
                instance.syncGameList(hashMap);
                StringBuilder sb = new StringBuilder();
                sb.append("applyAllGames(), refreshGameMap.size(): ");
                sb.append(hashMap.size());
                sb.append(", refreshGameMap: {");
                int i = 0;
                for (String str3 : hashMap.keySet()) {
                    sb.append(str3);
                    sb.append("- category: ");
                    sb.append(((List) hashMap.get(str3)).get(0));
                    sb.append(", installedUserIds: ");
                    sb.append(((List) hashMap.get(str3)).subList(1, ((List) hashMap.get(str3)).size() - 1).toString());
                    i++;
                    if (hashMap.size() == i) {
                        sb.append("}");
                    } else {
                        sb.append(", ");
                    }
                }
                str = sb.toString();
            } else {
                instance.init(hashMap2);
                str = "applyAllGames(), gameMap.size(): " + hashMap2.size() + ", gameMap: " + hashMap2;
            }
            GosLog.d(LOG_TAG, str);
            LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, str);
        }
        if (instance != null) {
            GosLog.d(LOG_TAG, "invoke sendRequestToGameManagerService");
            sendRequestToGameManagerService(arrayList);
            String str4 = "applyAllGames(), configList.size(): " + arrayList.size();
            GosLog.d(LOG_TAG, str4);
            LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, str4);
        }
        VrrFeature.getInstance().setVrr((String) null);
    }

    public boolean applySingleGame(String str) {
        if (str == null) {
            GosLog.w(LOG_TAG, "applySingleGame(), pkgName is null");
            return false;
        }
        PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str);
        if (pkgData != null) {
            return applySingleGame((Map<String, Integer>) null, (Map<String, List<Integer>>) null, pkgData, (ArrayList<SemPackageConfiguration>) null);
        }
        GosLog.w(LOG_TAG, "applySingleGame(), pkgData == null. do nothing. pkgName: " + str);
        return false;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void putToGameMap(java.util.Map<java.lang.String, java.lang.Integer> r9, java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> r10, java.lang.String r11, java.lang.String r12, java.lang.Integer[] r13) {
        /*
            r8 = this;
            r0 = 0
            java.lang.String r1 = "GmsGlobalPackageDataSetter"
            if (r12 != 0) goto L_0x001a
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "putToGameMap(), categoryStr is null. categoryStr="
            r9.append(r10)
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r1, (java.lang.String) r9)
            return
        L_0x001a:
            int r2 = r12.hashCode()
            r3 = 0
            r4 = 2
            r5 = -1
            r6 = 3
            r7 = 1
            switch(r2) {
                case 3165170: goto L_0x0045;
                case 26661249: goto L_0x003b;
                case 1358199538: goto L_0x0031;
                case 2072199523: goto L_0x0027;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x004f
        L_0x0027:
            java.lang.String r2 = "sec_game_family"
            boolean r12 = r12.equals(r2)
            if (r12 == 0) goto L_0x004f
            r12 = r6
            goto L_0x0050
        L_0x0031:
            java.lang.String r2 = "non-game"
            boolean r12 = r12.equals(r2)
            if (r12 == 0) goto L_0x004f
            r12 = r7
            goto L_0x0050
        L_0x003b:
            java.lang.String r2 = "managed_app"
            boolean r12 = r12.equals(r2)
            if (r12 == 0) goto L_0x004f
            r12 = r4
            goto L_0x0050
        L_0x0045:
            java.lang.String r2 = "game"
            boolean r12 = r12.equals(r2)
            if (r12 == 0) goto L_0x004f
            r12 = r3
            goto L_0x0050
        L_0x004f:
            r12 = r5
        L_0x0050:
            if (r12 == 0) goto L_0x005f
            if (r12 == r7) goto L_0x0060
            if (r12 == r4) goto L_0x005c
            if (r12 == r6) goto L_0x005a
            r3 = r5
            goto L_0x0060
        L_0x005a:
            r3 = r6
            goto L_0x0060
        L_0x005c:
            r3 = 10
            goto L_0x0060
        L_0x005f:
            r3 = r7
        L_0x0060:
            boolean r12 = com.samsung.android.game.gos.util.SecureFolderUtil.isSupportSfGMS()
            if (r12 == 0) goto L_0x0090
            if (r10 == 0) goto L_0x007b
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.lang.Integer r12 = java.lang.Integer.valueOf(r3)
            r9.add(r12)
            java.util.Collections.addAll(r9, r13)
            r10.put(r11, r9)
            goto L_0x00ae
        L_0x007b:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "putToGameMap(), refreshGameMap is null. refreshGameMap="
            r9.append(r10)
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r1, (java.lang.String) r9)
            goto L_0x00ae
        L_0x0090:
            if (r9 == 0) goto L_0x009a
            java.lang.Integer r10 = java.lang.Integer.valueOf(r3)
            r9.put(r11, r10)
            goto L_0x00ae
        L_0x009a:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "putToGameMap(), gameMap is null. gameMap="
            r9.append(r10)
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r1, (java.lang.String) r9)
        L_0x00ae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter.putToGameMap(java.util.Map, java.util.Map, java.lang.String, java.lang.String, java.lang.Integer[]):void");
    }

    private void validateCpuGpuLevel(JSONObject jSONObject, String str) {
        if (jSONObject.has(ManagerInterface.KeyName.CPU_LEVEL) && jSONObject.has(ManagerInterface.KeyName.GPU_LEVEL)) {
            int optInt = jSONObject.optInt(ManagerInterface.KeyName.CPU_LEVEL);
            int optInt2 = jSONObject.optInt(ManagerInterface.KeyName.GPU_LEVEL);
            int convertToValidCpuLevel = SeSysProp.getCpuGpuLevelInstance().convertToValidCpuLevel(optInt);
            int convertToValidGpuLevel = SeSysProp.getCpuGpuLevelInstance().convertToValidGpuLevel(optInt2);
            if (!(convertToValidCpuLevel == optInt && convertToValidGpuLevel == optInt2)) {
                GosLog.i(LOG_TAG, "applySingleGame(), after validation, CPU level : " + optInt + ", GPU level : " + optInt2);
                try {
                    jSONObject.put(ManagerInterface.KeyName.CPU_LEVEL, convertToValidCpuLevel);
                    jSONObject.put(ManagerInterface.KeyName.GPU_LEVEL, convertToValidGpuLevel);
                } catch (JSONException e) {
                    GosLog.w(LOG_TAG, (Throwable) e);
                }
            }
            DbHelper.getInstance().getPackageDao().setAppliedCpuGpuLevel(new Package.PkgNameAndAppliedCpuGpuLevel(str, convertToValidCpuLevel, convertToValidGpuLevel));
        }
    }

    private JSONObject getSsrmPolicyJson(SemPackageConfiguration semPackageConfiguration, PkgData pkgData, boolean z) {
        JSONObject jSONObject = new JSONObject();
        if (z) {
            jSONObject = updateSsrmPolicy(jSONObject, semPackageConfiguration, pkgData, pkgData.getDefaultCpuLevel(), pkgData.getDefaultGpuLevel());
            Map<String, Boolean> actualFeatureFlagMap = pkgData.getActualFeatureFlagMap();
            for (StaticInterface next : this.mStaticFeatures) {
                Boolean bool = actualFeatureFlagMap.get(next.getName());
                if (bool != null && bool.booleanValue()) {
                    semPackageConfiguration = next.getUpdatedConfig(pkgData, semPackageConfiguration, jSONObject);
                } else if (next.getName().equals(Constants.V4FeatureFlag.RESOLUTION)) {
                    semPackageConfiguration = next.getDefaultConfig(pkgData, semPackageConfiguration, jSONObject);
                } else if (next.getName().equals("siop_mode")) {
                    if (!GlobalDbHelper.getInstance().isPositiveFeatureFlag("siop_mode") || !pkgData.isPositiveFeature("siop_mode")) {
                        semPackageConfiguration = next.getDefaultConfig(pkgData, semPackageConfiguration, jSONObject);
                    } else {
                        semPackageConfiguration = next.getUpdatedConfig(pkgData, semPackageConfiguration, jSONObject);
                    }
                } else if (next.getName().equals(Constants.V4FeatureFlag.GOVERNOR_SETTINGS)) {
                    semPackageConfiguration = next.getDefaultConfig(pkgData, semPackageConfiguration, jSONObject);
                }
            }
        }
        try {
            jSONObject.put(ManagerInterface.KeyName.SOS_POLICY, pkgData.getPkg().getSosPolicy());
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return jSONObject;
    }

    private boolean applySingleGame(Map<String, Integer> map, Map<String, List<Integer>> map2, PkgData pkgData, ArrayList<SemPackageConfiguration> arrayList) {
        PkgData pkgData2 = pkgData;
        ArrayList<SemPackageConfiguration> arrayList2 = arrayList;
        if (pkgData2 == null) {
            GosLog.w(LOG_TAG, "applySingleGame(), pkgData is null");
            return false;
        }
        String packageName = pkgData.getPackageName();
        String categoryCode = pkgData.getCategoryCode();
        Integer[] installedUserIds = pkgData.getInstalledUserIds();
        if (installedUserIds == null) {
            installedUserIds = new Integer[]{Integer.valueOf(SeUserHandleManager.getInstance().getMyUserId())};
        }
        Integer[] numArr = installedUserIds;
        if (packageName == null || categoryCode == null) {
            GosLog.w(LOG_TAG, "applySingleGame(). pkgName == null || category == null");
            return false;
        }
        putToGameMap(map, map2, packageName, categoryCode, numArr);
        for (Integer intValue : numArr) {
            int intValue2 = intValue.intValue();
            SemPackageConfiguration semPackageConfiguration = new SemPackageConfiguration(packageName);
            semPackageConfiguration.setCategory(TypeConverter.getCategoryIntValue(pkgData.getCategoryCode()));
            semPackageConfiguration.setCategoryByUser(TypeConverter.getCategoryIntValue(pkgData.getCategoryCode()));
            GosLog.w(LOG_TAG, "applySingleGame(), start");
            if (setConfigurationUserId(semPackageConfiguration, packageName, intValue2, "applySingleGame(), ") || intValue2 == SeUserHandleManager.getInstance().getMyUserId()) {
                JSONObject ssrmPolicyJson = getSsrmPolicyJson(semPackageConfiguration, pkgData2, categoryCode.equals(Constants.CategoryCode.GAME));
                validateCpuGpuLevel(ssrmPolicyJson, packageName);
                semPackageConfiguration.setPerformancePolicyForSsrm(ssrmPolicyJson.toString());
                if (arrayList2 == null) {
                    VrrFeature.getInstance().setVrr(packageName);
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(semPackageConfiguration);
                    sendRequestToGameManagerService(arrayList3);
                } else {
                    GosLog.d(LOG_TAG, "config for gamemanager " + semPackageConfiguration.toString());
                    arrayList2.add(semPackageConfiguration);
                }
            }
        }
        return true;
    }

    private boolean setConfigurationUserId(SemPackageConfiguration semPackageConfiguration, String str, int i, String str2) {
        try {
            Method method = semPackageConfiguration.getClass().getMethod("setUserId", new Class[]{String.class});
            GosLog.d(LOG_TAG, str2 + str + ", " + i);
            method.invoke(semPackageConfiguration, new Object[]{String.valueOf(i)});
            return true;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            GosLog.w(LOG_TAG, str2 + e.getMessage());
            return false;
        }
    }

    private JSONObject updateSsrmPolicy(JSONObject jSONObject, SemPackageConfiguration semPackageConfiguration, PkgData pkgData, int i, int i2) {
        try {
            jSONObject.put(ManagerInterface.KeyName.CPU_LEVEL, i);
            jSONObject.put(ManagerInterface.KeyName.GPU_LEVEL, i2);
            int shiftTemperature = pkgData.getPkg().getShiftTemperature();
            if (shiftTemperature != -1000) {
                jSONObject.put(ManagerInterface.KeyName.SHIFT_TEMPERATURE, shiftTemperature);
            }
            String gameSdkSettings = pkgData.getPkg().getGameSdkSettings();
            if (gameSdkSettings != null) {
                jSONObject.put(ManagerInterface.KeyName.GAME_SDK_SETTING, gameSdkSettings);
            }
            GovernorSettingsFeature.getInstance().getUpdatedConfig(pkgData, semPackageConfiguration, jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public void restoreSingleGameNow(String str, int i) {
        SemPackageConfiguration semPackageConfiguration = new SemPackageConfiguration(str);
        setConfigurationUserId(semPackageConfiguration, str, i, "restoreSingleGameNow(), ");
        ArrayList arrayList = new ArrayList();
        arrayList.add(semPackageConfiguration);
        setPkgData(arrayList);
    }

    private void sendRequestToGameManagerService(ArrayList<SemPackageConfiguration> arrayList) {
        SeGameManager instance = SeGameManager.getInstance();
        if (instance != null) {
            instance.setPerformanceMode(DbHelper.getInstance().getGlobalDao().getSiopMode(), BuildConfig.VERSION_NAME);
            if (arrayList != null) {
                GosLog.d(LOG_TAG, "sendRequestToGameManagerService(),  configList Size " + arrayList.toString().getBytes(StandardCharsets.UTF_8).length + " configList Count : " + arrayList.size());
                if (arrayList.toString().getBytes(StandardCharsets.UTF_8).length > 409600) {
                    sendConfigListInChunks(arrayList);
                } else {
                    setPkgData(arrayList);
                }
            }
        }
    }

    private void sendConfigListInChunks(List<SemPackageConfiguration> list) {
        if (list != null) {
            try {
                int size = list.size();
                List<SemPackageConfiguration> configListBySize = getConfigListBySize(list);
                ArrayList arrayList = new ArrayList();
                int i = size;
                while (i > 0 && configListBySize.size() > 0) {
                    arrayList.add(Integer.valueOf(configListBySize.size()));
                    setPkgData(configListBySize);
                    list = list.subList(configListBySize.size(), i);
                    i = list.size();
                    configListBySize = getConfigListBySize(list);
                }
                GosLog.d(LOG_TAG, "sendConfigListInChunks(), totalCount : " + size + " sent by: " + arrayList.toString());
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
    }

    private void setPkgData(List<SemPackageConfiguration> list) {
        boolean packageConfigurations = SeGameManager.getInstance().setPackageConfigurations(list);
        GosLog.i(LOG_TAG, "setPkgData(), ret: " + packageConfigurations);
    }

    private List<SemPackageConfiguration> getConfigListBySize(List<SemPackageConfiguration> list) {
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        int i = 0;
        for (int i2 = 0; i2 < size && i < 409600; i2++) {
            SemPackageConfiguration semPackageConfiguration = list.get(i2);
            arrayList.add(semPackageConfiguration);
            i += semPackageConfiguration.toString().getBytes(StandardCharsets.UTF_8).length;
        }
        GosLog.d(LOG_TAG, "getConfigListBySize(), resultArray count : " + arrayList.size() + " currentSize : " + i);
        return arrayList;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final GmsGlobalPackageDataSetter INSTANCE = new GmsGlobalPackageDataSetter();

        private SingletonHolder() {
        }
    }
}
