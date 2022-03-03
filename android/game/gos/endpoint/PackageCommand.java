package com.samsung.android.game.gos.endpoint;

import android.app.Application;
import android.content.Context;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.FeatureFlagUtil;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.EventSubscriptionDbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.dfs.DfsFeature;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostCore;
import com.samsung.android.game.gos.feature.siopmode.SiopModeFeature;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.util.BadHardcodedOperation;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.util.SecureFolderUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class PackageCommand {
    private static final String LOG_TAG = PackageCommand.class.getSimpleName();

    PackageCommand() {
    }

    /* access modifiers changed from: package-private */
    public String getPackageNames(String str) {
        Integer[] installedUserIds;
        GosLog.i(LOG_TAG, "getPackageNames. jsonParam: " + str);
        JSONObject jSONObject = new JSONObject();
        if (str == null) {
            try {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Your jsonParam is null.");
            } catch (JSONException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        } else {
            List<String> pkgNameListByCategory = DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(new JSONObject(str).getString(GosInterface.KeyName.CATEGORY_CODE));
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (pkgNameListByCategory != null) {
                for (String next : pkgNameListByCategory) {
                    PkgData pkgData = PackageDbHelper.getInstance().getPkgData(next);
                    if (!(pkgData == null || (installedUserIds = pkgData.getInstalledUserIds()) == null || installedUserIds.length <= 0)) {
                        for (Integer num : installedUserIds) {
                            if (num.intValue() == SeUserHandleManager.getInstance().getMyUserId()) {
                                arrayList.add(next);
                            } else if (SecureFolderUtil.isSecureFolderUserId(num.intValue())) {
                                arrayList2.add(next);
                            }
                        }
                    }
                }
            }
            jSONObject.put(GosInterface.KeyName.PACKAGE_NAMES, TypeConverter.stringsToCsv((Iterable<String>) arrayList));
            if (!arrayList2.isEmpty()) {
                jSONObject.put(GosInterface.KeyName.PACKAGE_NAMES_SECURE_FOLDER, TypeConverter.stringsToCsv((Iterable<String>) arrayList2));
            }
        }
        return jSONObject.toString();
    }

    /* access modifiers changed from: package-private */
    public String getPkgDataWithJson(String str) {
        String str2 = LOG_TAG;
        GosLog.i(str2, "getPkgDataWithJson(). jsonParam: " + str);
        String str3 = null;
        try {
            String string = new JSONObject(str).getString("package_name");
            String str4 = LOG_TAG;
            GosLog.d(str4, "getPkgDataWithJson(). pkgName: " + string);
            if (string == null) {
                return null;
            }
            PkgData pkgData = PackageDbHelper.getInstance().getPkgData(string);
            if (pkgData == null) {
                String str5 = LOG_TAG;
                GosLog.w(str5, "getPkgDataWithJson(). couldn't find PkgData with: " + string);
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(GosInterface.KeyName.CATEGORY_CODE, pkgData.getCategoryCode());
            jSONObject.put(GosInterface.KeyName.SERVER_CATEGORY, pkgData.getCategoryCode());
            jSONObject.put(GosInterface.KeyName.DEFAULT_DSS_VALUE, (double) DssFeature.getDefaultDss(pkgData));
            jSONObject.put(GosInterface.KeyName.DEFAULT_DFS_VALUE, (double) DfsFeature.getDefaultDfs(pkgData.getPkg()));
            jSONObject.put(GosInterface.KeyName.DEFAULT_CPU_LEVEL, pkgData.getDefaultCpuLevel());
            jSONObject.put(GosInterface.KeyName.DEFAULT_GPU_LEVEL, pkgData.getDefaultGpuLevel());
            int[] eachModeTargetShortSideArray = pkgData.getPkg().getEachModeTargetShortSideArray();
            if (eachModeTargetShortSideArray != null) {
                jSONObject.put(GosInterface.KeyName.DEFAULT_TARGET_SHORT_SIDE, eachModeTargetShortSideArray[1]);
            } else {
                jSONObject.put(GosInterface.KeyName.DEFAULT_TARGET_SHORT_SIDE, -1);
            }
            String floatsToCsv = TypeConverter.floatsToCsv(DssFeature.getMergedEachModeDss(pkgData));
            if (floatsToCsv != null) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_DSS, floatsToCsv);
            } else if (pkgData.isGame()) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_DSS, TypeConverter.floatsToCsv(GlobalDbHelper.getInstance().getEachModeDss()));
            }
            String floatsToCsv2 = TypeConverter.floatsToCsv(DfsFeature.getMergedEachModeDfs(pkgData.getPkg()));
            if (floatsToCsv2 != null) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_DFS, floatsToCsv2);
            } else if (pkgData.isGame()) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_DFS, TypeConverter.floatsToCsv(GlobalDbHelper.getInstance().getEachModeDfs()));
            }
            String eachModeTargetShortSide = pkgData.getPkg().getEachModeTargetShortSide();
            if (eachModeTargetShortSide != null) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_TARGET_SHORT_SIDE, eachModeTargetShortSide);
            } else if (pkgData.isGame()) {
                jSONObject.put(GosInterface.KeyName.EACH_MODE_TARGET_SHORT_SIDE, DbHelper.getInstance().getGlobalDao().getEachModeTargetShortSide());
            }
            jSONObject.put(GosInterface.KeyName.EACH_SIOP_MODE_CPU_LEVEL, TypeConverter.intsToCsv(SiopModeFeature.getInstance().getEachSiopModeCpuLevel(pkgData)));
            jSONObject.put(GosInterface.KeyName.EACH_SIOP_MODE_GPU_LEVEL, TypeConverter.intsToCsv(SiopModeFeature.getEachSiopModeGpuLevel(pkgData)));
            jSONObject.put(GosInterface.KeyName.APPLIED_LAUNCH_BOOST_DURATION, ResumeBoostCore.getInstance().getDuration(pkgData.getPkg(), true));
            jSONObject.put(GosInterface.KeyName.APPLIED_RESUME_BOOST_DURATION, ResumeBoostCore.getInstance().getDuration(pkgData.getPkg(), false));
            jSONObject.put(GosInterface.KeyName.AVAILABLE_DSS, TypeConverter.floatsToCsv(getAvailableDss(string)));
            if (pkgData.getFeatureFlagMap() != null) {
                JSONObject jSONObject2 = new JSONObject();
                Map<String, GlobalFeatureFlag> featureFlagMap = GlobalDbHelper.getInstance().getFeatureFlagMap();
                for (Map.Entry next : pkgData.getFeatureFlagMap().entrySet()) {
                    String str6 = (String) next.getKey();
                    FeatureFlag featureFlag = (FeatureFlag) next.getValue();
                    if (featureFlagMap.get(str6) != null) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put(GosInterface.FeatureFlagKeyNames.FORCED, featureFlag.isForcedFlag());
                        jSONObject3.put("inherited", featureFlag.isInheritedFlag());
                        jSONObject3.put(GosInterface.FeatureFlagKeyNames.ENABLED_BY_SERVER, featureFlag.isEnabledFlagByServer());
                        jSONObject3.put(GosInterface.FeatureFlagKeyNames.ENABLED_BY_USER, featureFlag.isEnabledFlagByUser());
                        featureFlag.setEnabled(FeatureFlagUtil.calculateFinalEnabled(featureFlag, DbHelper.getInstance().getGlobalFeatureFlagDao()));
                        jSONObject3.put("enabled", featureFlag.isEnabled());
                        jSONObject2.put(str6, jSONObject3);
                    }
                }
                jSONObject.put(GosInterface.KeyName.FEATURE_FLAGS, jSONObject2);
            }
            jSONObject.put(GosInterface.KeyName.CUSTOM_DSS_VALUE, (double) pkgData.getPkg().getCustomDss());
            jSONObject.put(GosInterface.KeyName.CUSTOM_DFS_VALUE, (double) pkgData.getPkg().getCustomDfs());
            jSONObject.put(GosInterface.KeyName.CUSTOM_SIOP_MODE, pkgData.getCustomSiopMode());
            jSONObject.put(GosInterface.KeyName.CUSTOM_RESOLUTION_MODE, pkgData.getPkg().getCustomResolutionMode());
            jSONObject.put(GosInterface.KeyName.CUSTOM_DFS_MODE, pkgData.getPkg().getCustomDfsMode());
            str3 = jSONObject.toString();
            String str7 = LOG_TAG;
            GosLog.i(str7, "getPkgDataWithJson(). response: " + str3);
            return str3;
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    private float[] getAvailableDss(String str) {
        float[] fArr;
        if (str == null) {
            return null;
        }
        PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str);
        if (pkgData != null) {
            fArr = pkgData.getPkg().getEachModeDssArray();
            if (pkgData.isGame()) {
                fArr = DssFeature.getMergedEachModeDss(pkgData);
            }
        } else {
            fArr = null;
        }
        if (fArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (float f : fArr) {
            if (!arrayList.contains(Float.valueOf(f))) {
                arrayList.add(Float.valueOf(f));
            }
        }
        GosLog.i(LOG_TAG, "getEachModeDss(), packageName: " + str + ", result: " + arrayList.toString());
        return TypeConverter.floatListToArray(arrayList);
    }

    /* access modifiers changed from: package-private */
    public String setPkgDataWithJson(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        String str5 = LOG_TAG;
        GosLog.i(str5, "setPkgDataWithJson(). jsonParam: " + str3);
        Application application = AppContext.get();
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject(str3);
            GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
            ArrayList arrayList = new ArrayList();
            String string = jSONObject2.getString("package_name");
            if (string == null) {
                GosLog.e(LOG_TAG, "setPkgDataWithJson(). pkgName is null");
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "pkgName is null");
                return jSONObject.toString();
            }
            Package packageR = DbHelper.getInstance().getPackageDao().getPackage(string);
            if (packageR == null) {
                GosLog.w(LOG_TAG, "setPkgDataWithJson(). pkg is null");
                if (PackageUtil.hasLaunchIntent(application, string)) {
                    GosLog.i(LOG_TAG, "setPkgDataWithJson(). The package has launchIntent. Add a record.");
                    packageR = new Package(string, Constants.CategoryCode.UNDEFINED);
                } else {
                    jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                    jSONObject.put(GosInterface.KeyName.COMMENT, "pkg is null");
                    GosLog.e(LOG_TAG, "setPkgDataWithJson(). pkg is null");
                    return jSONObject.toString();
                }
            }
            Package packageR2 = packageR;
            StringBuilder sb = new StringBuilder();
            setPkgCustomValues(jSONObject2, packageR2, globalFeatureFlagDao, sb, arrayList);
            String str6 = GosInterface.KeyName.SUCCESSFUL;
            Package packageR3 = packageR2;
            String str7 = GosInterface.KeyName.COMMENT;
            setPkgFeatureFlag(jSONObject2, packageR2, sb, arrayList, str2);
            if (sb.length() > 0) {
                DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR3);
                jSONObject.put(str6, true);
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL_ITEMS, sb.toString());
                String str8 = LOG_TAG;
                GosLog.d(str8, "setPkgDataWithJson(). successful_items: " + sb.toString());
                if (Constants.CategoryCode.GAME.equalsIgnoreCase(packageR3.categoryCode)) {
                    GmsGlobalPackageDataSetter.getInstance().applySingleGame(string);
                    Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.PACKAGE_DATA_UPDATED.toString());
                    if (str4 != null) {
                        subscriberListOfEvent.remove(str4);
                    }
                    EventPublisher.publishEvent((Context) AppContext.get(), subscriberListOfEvent, EventSubscription.EVENTS.PACKAGE_DATA_UPDATED.toString(), packageR3.pkgName, (Map<String, String>) null);
                } else {
                    GmsGlobalPackageDataSetter.getInstance().restoreSingleGameNow(string, SeUserHandleManager.getInstance().getMyUserId());
                }
            } else {
                jSONObject.put(str6, false);
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL_ITEMS, BuildConfig.VERSION_NAME);
                jSONObject.put(str7, "There is no successful items.");
            }
            GlobalDbHelper.getInstance().setSettingAccessiblePackage(str4, arrayList);
            return jSONObject.toString();
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, "setPkgDataWithJson()", e);
        }
    }

    private void setPkgCustomValues(JSONObject jSONObject, Package packageR, GlobalFeatureFlagDao globalFeatureFlagDao, StringBuilder sb, List<String> list) throws JSONException {
        if (jSONObject.has(GosInterface.KeyName.CUSTOM_DSS_VALUE) && globalFeatureFlagDao.isAvailable(Constants.V4FeatureFlag.RESOLUTION)) {
            packageR.setCustomDss((float) jSONObject.getDouble(GosInterface.KeyName.CUSTOM_DSS_VALUE));
            packageR.setCustomResolutionMode(4);
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(GosInterface.KeyName.CUSTOM_DSS_VALUE);
            if (!list.contains(Constants.V4FeatureFlag.RESOLUTION)) {
                list.add(Constants.V4FeatureFlag.RESOLUTION);
            }
        }
        if (jSONObject.has(GosInterface.KeyName.CUSTOM_DFS_VALUE) && globalFeatureFlagDao.isAvailable(Constants.V4FeatureFlag.DFS)) {
            packageR.setCustomDfs((float) jSONObject.getDouble(GosInterface.KeyName.CUSTOM_DFS_VALUE));
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(GosInterface.KeyName.CUSTOM_DFS_VALUE);
            if (!list.contains(Constants.V4FeatureFlag.DFS)) {
                list.add(Constants.V4FeatureFlag.DFS);
            }
        }
        if (jSONObject.has(GosInterface.KeyName.CUSTOM_SIOP_MODE) && globalFeatureFlagDao.isAvailable("siop_mode")) {
            packageR.setCustomSiopMode(jSONObject.getInt(GosInterface.KeyName.CUSTOM_SIOP_MODE));
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(GosInterface.KeyName.CUSTOM_SIOP_MODE);
            if (!list.contains("siop_mode")) {
                list.add("siop_mode");
            }
        }
        if (jSONObject.has(GosInterface.KeyName.CUSTOM_RESOLUTION_MODE)) {
            packageR.setCustomResolutionMode(jSONObject.getInt(GosInterface.KeyName.CUSTOM_RESOLUTION_MODE));
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(GosInterface.KeyName.CUSTOM_RESOLUTION_MODE);
            if (!list.contains(Constants.V4FeatureFlag.RESOLUTION)) {
                list.add(Constants.V4FeatureFlag.RESOLUTION);
            }
        }
        if (jSONObject.has(GosInterface.KeyName.CUSTOM_DFS_MODE)) {
            packageR.setCustomDfsMode(jSONObject.getInt(GosInterface.KeyName.CUSTOM_DFS_MODE));
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(GosInterface.KeyName.CUSTOM_DFS_MODE);
            if (!list.contains(Constants.V4FeatureFlag.DFS)) {
                list.add(Constants.V4FeatureFlag.DFS);
            }
        }
    }

    private void setPkgFeatureFlag(JSONObject jSONObject, Package packageR, StringBuilder sb, List<String> list, String str) throws JSONException {
        if (jSONObject.has(GosInterface.KeyName.FEATURE_FLAGS)) {
            Map<String, FeatureFlag> featureFlagMap = PackageDbHelper.getInstance().getFeatureFlagMap(packageR.pkgName);
            ArrayList arrayList = new ArrayList();
            JSONObject jSONObject2 = jSONObject.getJSONObject(GosInterface.KeyName.FEATURE_FLAGS);
            if (BadHardcodedOperation.needsToBlockGfiSettingByGameBooster(str)) {
                jSONObject2.remove(Constants.V4FeatureFlag.GFI);
            }
            for (String next : Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES) {
                if (jSONObject2.has(next) && featureFlagMap.containsKey(next)) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject(next);
                    if (jSONObject3.has(GosInterface.FeatureFlagKeyNames.ENABLED_BY_USER)) {
                        boolean z = jSONObject3.getBoolean(GosInterface.FeatureFlagKeyNames.ENABLED_BY_USER);
                        FeatureFlag featureFlag = featureFlagMap.get(next);
                        featureFlag.setEnabledFlagByUser(z);
                        arrayList.add(featureFlag);
                        if (!list.contains(featureFlag.getName())) {
                            list.add(featureFlag.getName());
                        }
                    }
                }
            }
            DbHelper.getInstance().getFeatureFlagDao().insertOrUpdate((Collection<FeatureFlag>) arrayList);
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(GosInterface.KeyName.FEATURE_FLAGS);
        }
    }

    /* access modifiers changed from: package-private */
    public String stopPackages(String str) {
        String str2 = LOG_TAG;
        GosLog.i(str2, "stopPackages(), " + str);
        JSONObject jSONObject = new JSONObject();
        if (str == null) {
            try {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Your jsonParam is null.");
            } catch (JSONException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        } else {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, SeActivityManager.getInstance().forceStopPackages(TypeConverter.csvToStringList(new JSONObject(str).getString(GosInterface.KeyName.PACKAGE_NAMES))));
        }
        return jSONObject.toString();
    }
}
