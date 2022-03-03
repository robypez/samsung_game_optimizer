package com.samsung.android.game.gos.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.ArrayMap;
import android.util.Pair;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUpdater;
import com.samsung.android.game.gos.data.AidlPermissionHolder;
import com.samsung.android.game.gos.data.DataManager;
import com.samsung.android.game.gos.data.FeatureFlagUtil;
import com.samsung.android.game.gos.data.GameInfoCollector;
import com.samsung.android.game.gos.data.LocalCache;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.SystemDataHelper;
import com.samsung.android.game.gos.data.dao.FeatureFlagDao;
import com.samsung.android.game.gos.data.dbhelper.CategoryInfoDbHelper;
import com.samsung.android.game.gos.data.dbhelper.CategoryUpdateReservedDbHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.dbhelper.ReportDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.MonitoredApps;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.type.ReportData;
import com.samsung.android.game.gos.feature.StaticInterface;
import com.samsung.android.game.gos.feature.clearbgprocess.ClearBGProcessUtil;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostCore;
import com.samsung.android.game.gos.feature.siopmode.SiopModeFeature;
import com.samsung.android.game.gos.feature.vrr.DrrCore;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.network.response.BaseResponse;
import com.samsung.android.game.gos.network.response.CategoryResponse;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SystemEventReactor {
    private static final String LOG_TAG = "SystemEventReactor";

    private SystemEventReactor() {
    }

    private static void updateGmsVersion() {
        float version = SeGameManager.getInstance().getVersion();
        DbHelper.getInstance().getGlobalDao().setGmsVersion(new Global.IdAndGmsVersion(version));
        ResumeBoostCore.getInstance().updateGmsOk(version);
    }

    private static void updateSystemInfo() {
        updateGmsVersion();
        DmaIdSetter.getInstance().bindToDmaService(AppContext.get().getPackageManager());
        for (Map.Entry next : FeatureSetManager.getAvailableFeatureFlagMap(AppContext.get()).entrySet()) {
            GlobalDbHelper.getInstance().setAvailable((String) next.getKey(), ((Boolean) next.getValue()).booleanValue());
        }
        String sosPolicyKeysCsv = SeGameManager.getInstance().getSosPolicyKeysCsv();
        if (sosPolicyKeysCsv != null) {
            DbHelper.getInstance().getGlobalDao().setSosPolicyKeyCsv(new Global.IdAndSosPolicyKeyCsv(sosPolicyKeysCsv));
        }
    }

    public static void onBootCompleted(Context context, NetworkConnector networkConnector) {
        GosLog.d(LOG_TAG, "onBootCompleted(), begin");
        LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, "onBootCompleted(), begin");
        updateSystemInfo();
        List<String> reservedCategoryList = CategoryUpdateReservedDbHelper.getInstance().getReservedCategoryList();
        if (reservedCategoryList != null && reservedCategoryList.size() > 0) {
            DbHelper.getInstance().getCategoryUpdateReservedDao().deleteAll();
        }
        GmsGlobalPackageDataSetter.getInstance().applyAllGames(true);
        DataUpdater.updateGlobalAndPkgData(context, DataUpdater.PkgUpdateType.ALL, false, networkConnector, Constants.CallTrigger.BOOT_COMPLETED);
        if (!DbHelper.getInstance().getGlobalDao().isGosSelfUpdateStatus()) {
            collectAlreadyInstalledGameInfo(3);
            DbHelper.getInstance().getGlobalDao().setGosSelfUpdateStatus(new Global.IdAndGosSelfUpdateStatus(true));
        }
    }

    public static void onMyPackageReplaced() {
        GosLog.d(LOG_TAG, "onMyPackageReplaced(), begin");
        LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, "onMyPackageReplaced(), begin");
        updateSystemInfo();
        Map<String, GlobalFeatureFlag> featureFlagMap = GlobalDbHelper.getInstance().getFeatureFlagMap();
        int i = 0;
        for (String next : Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES) {
            if (!featureFlagMap.containsKey(next)) {
                featureFlagMap.put(next, new GlobalFeatureFlag(next, "inherited"));
                i++;
            }
        }
        if (i > 0) {
            DbHelper.getInstance().getGlobalFeatureFlagDao().insertOrUpdate(featureFlagMap.values());
        }
        PackageDbHelper instance = PackageDbHelper.getInstance();
        FeatureFlagDao featureFlagDao = DbHelper.getInstance().getFeatureFlagDao();
        for (String next2 : DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME)) {
            Map<String, FeatureFlag> featureFlagMap2 = instance.getFeatureFlagMap(next2);
            if (FeatureFlagUtil.fillMissingFeatureFlag(featureFlagMap2, next2)) {
                featureFlagDao.insertOrUpdate(featureFlagMap2.values());
            }
        }
        if (!DbHelper.getInstance().getGlobalDao().isGosSelfUpdateStatus()) {
            collectAlreadyInstalledGameInfo(3);
            DbHelper.getInstance().getGlobalDao().setGosSelfUpdateStatus(new Global.IdAndGosSelfUpdateStatus(true));
        }
    }

    public static void onDesktopModeChanged() {
        GosLog.d(LOG_TAG, "onDesktopModeChanged");
        GmsGlobalPackageDataSetter.getInstance().applyAllGames(false);
    }

    public static void onResolutionChanged() {
        GosLog.d(LOG_TAG, "onResolutionChanged");
        DssFeature.getInstance().updateDisplayMetrics();
        if (PlatformUtil.isMultiResolutionSupported()) {
            GosLog.i(LOG_TAG, "onResolutionChanged. MultiResolution is supported");
            GmsGlobalPackageDataSetter.getInstance().applyAllGames(false);
            return;
        }
        GosLog.i(LOG_TAG, "onResolutionChanged. MultiResolution is not supported. do nothing");
    }

    public static void onPackageInstallStarted(Context context, NetworkConnector networkConnector, String str, int i) {
        GosLog.d(LOG_TAG, "onPackageInstallStarted(). packageName: " + str);
        if (DbHelper.getInstance().getPackageDao().getPackage(str) == null) {
            boolean isRegisteredDevice = DbHelper.getInstance().getGlobalDao().isRegisteredDevice();
            boolean value = new PreferenceHelper(AppContext.get().createDeviceProtectedStorageContext()).getValue(PreferenceHelper.PREF_HAS_CHECKED_DEVICE_REGISTRATION_TO_SERVER, false);
            if (!isRegisteredDevice && !value) {
                DataUpdater.updateGlobalAndPkgData(context, DataUpdater.PkgUpdateType.ALL, false, networkConnector, Constants.CallTrigger.PKG_INSTALL_STARTED);
            }
            if (isRegisteredDevice) {
                addPkgDataFromServer(context, networkConnector, str, i, Constants.CallTrigger.PKG_INSTALL_STARTED);
            } else {
                GosLog.w(LOG_TAG, "onPackageInstallStarted(). device unsupported, not query to server.");
                LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, "onPackageInstallStarted(). device unsupported, not query to server.");
            }
            PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str);
            if (pkgData == null) {
                return;
            }
            if (pkgData.isGame() || pkgData.isManagedApp()) {
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(str);
            }
        }
    }

    public static void onPackageInstalled(Context context, String str, int i, NetworkConnector networkConnector) {
        GosLog.d(LOG_TAG, "onPackageInstalled(). packageName: " + str + ", userId: " + i);
        if (str != null) {
            PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str);
            if (pkgData == null) {
                GosLog.i(LOG_TAG, "onPackageInstalled(). pkgData is null");
                sendSamsungVrBroadcast(context, str);
                return;
            }
            ArrayList arrayList = null;
            if (pkgData.getInstalledUserIds() != null) {
                arrayList = new ArrayList(Arrays.asList(pkgData.getInstalledUserIds()));
            }
            if (arrayList == null || !arrayList.contains(Integer.valueOf(i))) {
                pkgData.addInstalledUserId(i);
                DbHelper.getInstance().getPackageDao().insertOrUpdate(pkgData.getPkg());
            }
            boolean hasLaunchIntentAsUser = PackageUtil.hasLaunchIntentAsUser(context, str, i);
            if (AppVariable.isUnitTest() || hasLaunchIntentAsUser) {
                if (DbHelper.getInstance().getGlobalDao().isRegisteredDevice()) {
                    checkReservedCategory(networkConnector, Constants.CallTrigger.PKG_INSTALLED);
                }
                registerPackageInfo(pkgData);
                sendSamsungVrBroadcast(context, str);
                return;
            }
            GosLog.d(LOG_TAG, "onPackageInstalled(). remove: " + str + ", isLaunchablePkg: " + hasLaunchIntentAsUser + ", userId: " + i);
            DbHelper.getInstance().getPackageDao().removePkg(str);
            PackageDbHelper.getInstance().removeFeatureFlagByPackageName(str);
            sendSamsungVrBroadcast(context, str);
        }
    }

    static void setGameData(String str, PkgData pkgData) {
        GmsGlobalPackageDataSetter.getInstance().applySingleGame(str);
        GosLog.d(LOG_TAG, "setGameData(). a game was added, " + str);
        collectInstalledOrUpdatedGameInfo(pkgData.getPkg(), 1);
    }

    static void setSecGameFamilyData(String str, PkgData pkgData) {
        SeGameManager.getInstance().notifyCategoryToGameManagerNow(pkgData.getPackageName(), Constants.CategoryCode.SEC_GAME_FAMILY);
        GosLog.d(LOG_TAG, "setSecGameFamilyData(). a member of SecGameFamily was added, " + str);
        if (Objects.equals(str, Constants.PACKAGE_NAME_GAME_TOOLS) && GlobalDbHelper.getInstance().isPositiveFeatureFlag("siop_mode")) {
            GosLog.d(LOG_TAG, "setSecGameFamilyData(). Game Booster is enabled. SIOP_MODE is positive");
            if (DbHelper.getInstance().getGlobalDao().getSiopMode() != 1) {
                DbHelper.getInstance().getGlobalDao().setSiopMode(new Global.IdAndSiopMode(1));
                GmsGlobalPackageDataSetter.getInstance().applyAllGames(false);
            }
        }
    }

    private static void sendSamsungVrBroadcast(Context context, String str) {
        if (str.equals(Constants.PACKAGE_NAME_SAMSUNG_VR)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.PACKAGE_NAME_SAMSUNG_VR, "com.samsung.vrvideo.Installed"));
            context.sendBroadcast(intent);
            GosLog.d(LOG_TAG, "Sends a broadcast to Samsung VR application.");
        }
    }

    public static void onPackageRemoved(Context context, String str, int i, NetworkConnector networkConnector) {
        PkgData pkgData;
        GosLog.d(LOG_TAG, "onPackageRemoved(), packageName: " + str + ", userId: " + i);
        if (str == null) {
            GosLog.w(LOG_TAG, "onPackageRemoved(), packageName is null");
            return;
        }
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        if (packageR == null) {
            GosLog.w(LOG_TAG, "onPackageRemoved(), pkg is null");
            return;
        }
        boolean z = false;
        if (PackageDbHelper.isInstalledUserId(packageR, i)) {
            packageR = PackageDbHelper.removeInstalledUserId(packageR, i);
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        }
        Integer[] installedUserIds = packageR.getInstalledUserIds();
        if (installedUserIds == null || installedUserIds.length == 0) {
            z = true;
        }
        if (z) {
            GosLog.d(LOG_TAG, "onPackageRemoved(), remove from AidlPermissionHolder: " + AidlPermissionHolder.getInstance().remove(str));
            if (unregisterPackageInfo(str, true, "onPackageRemoved(), ")) {
                DataUpdater.updateGlobalSettingsFromServer(networkConnector, Constants.CallTrigger.PKG_REMOVED);
                DataUpdater.updatePackageList(context, networkConnector, DbHelper.getInstance().getPackageDao().getAllPkgNameList(), Constants.CallTrigger.PKG_REMOVED);
                return;
            }
            boolean equals = Constants.CategoryCode.GAME.equals(packageR.categoryCode);
            if (equals) {
                DrrCore.getInstance().updateLastTargetFPSTreeMap(packageR);
            }
            if (DbHelper.getInstance().getPackageDao().removePkg(str) > 0) {
                pkgRemovedRestore(equals, str, i);
            }
            if (DbHelper.getInstance().getCategoryUpdateReservedDao().delete(str) > 0) {
                GosLog.d(LOG_TAG, "onPackageRemoved(), " + str + ": reserved is canceled!");
            }
        } else if (i == SeUserHandleManager.getInstance().getMyUserId() && (pkgData = PackageDbHelper.getInstance().getPkgData(str)) != null) {
            registerPackageInfo(pkgData);
        }
    }

    public static void onPackageEnabled(Context context, String str, int i, NetworkConnector networkConnector) {
        if (str == null) {
            GosLog.w(LOG_TAG, "onPackageEnabled(), pkgName is null");
            return;
        }
        GosLog.d(LOG_TAG, "onPackageEnabled(). pkgName: " + str);
        PackageManager packageManager = context.getPackageManager();
        if (AppVariable.isUnitTest() || packageManager.getLaunchIntentForPackage(str) != null) {
            if (DbHelper.getInstance().getPackageDao().getPackage(str) == null) {
                if (DbHelper.getInstance().getGlobalDao().isRegisteredDevice()) {
                    addPkgDataFromServer(context, networkConnector, str, i, Constants.CallTrigger.PKG_ENABLED);
                } else {
                    GosLog.w(LOG_TAG, "onPackageEnabled(). device unsupported, not query to server.");
                    LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, "onPackageEnabled(). device unsupported, not query to server.");
                }
            }
            registerPackageInfo(PackageDbHelper.getInstance().getPkgData(str));
            return;
        }
        DbHelper.getInstance().getPackageDao().removePkg(str);
        PackageDbHelper.getInstance().removeFeatureFlagByPackageName(str);
    }

    static void registerPackageInfo(PkgData pkgData) {
        if (pkgData != null) {
            String packageName = pkgData.getPackageName();
            ClearBGProcessUtil.observerInstallPkg(packageName);
            if (pkgData.isSecGameFamily()) {
                setSecGameFamilyData(packageName, pkgData);
            } else if (pkgData.isGame()) {
                setGameData(packageName, pkgData);
            } else if (pkgData.isManagedApp()) {
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(packageName);
                GosLog.i(LOG_TAG, "registerPackageInfo(). a ManagedApp: " + packageName);
            }
        }
    }

    public static void onPackageDisabled(String str) {
        if (str == null) {
            GosLog.w(LOG_TAG, "onPackageDisabled(), pkgName is null");
            return;
        }
        GosLog.d(LOG_TAG, "onPackageDisabled(), pkgName : " + str);
        unregisterPackageInfo(str, false, "onPackageDisabled(), ");
    }

    public static void onUserRemoved(Intent intent) {
        if (intent == null) {
            GosLog.w(LOG_TAG, "onUserRemoved(), intent is null.");
            return;
        }
        int intExtra = intent.getIntExtra(EventPublisher.EXTRA_KEY_USER_ID, -10000);
        GosLog.i(LOG_TAG, "onUserRemoved(), userId: " + intExtra);
        if (intExtra < 0) {
            GosLog.w(LOG_TAG, "onUserRemoved(), userId: " + intExtra + " do nothing.");
            return;
        }
        List<String> allPkgNameList = DbHelper.getInstance().getPackageDao().getAllPkgNameList();
        if (allPkgNameList == null) {
            GosLog.w(LOG_TAG, "onUserRemoved(), installedPkgNameList is null.");
            return;
        }
        for (String next : allPkgNameList) {
            Package packageR = DbHelper.getInstance().getPackageDao().getPackage(next);
            if (!(packageR == null || packageR.getInstalledUserIds() == null || !new ArrayList(Arrays.asList(packageR.getInstalledUserIds())).contains(Integer.valueOf(intExtra)))) {
                onPackageRemoved(AppContext.get(), next, intExtra, (NetworkConnector) null);
            }
        }
    }

    static boolean unregisterPackageInfo(String str, boolean z, String str2) {
        ClearBGProcessUtil.observerRemovePkg(str);
        DbHelper.getInstance().getEventSubscriptionDao().deleteSubscriber(new EventSubscription.SubscriberPkgName(str));
        DbHelper.getInstance().getMonitoredAppsDao().deleteSubscriber(new MonitoredApps.SubscriberPkgName(str));
        String format = String.format("unregisterPackageInfo()-(pkgName=%s)", new Object[]{str});
        GosLog.i(LOG_TAG, format);
        LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, format);
        if (Constants.PACKAGE_NAME_GAME_TOOLS.equals(str) && FeatureHelper.isAvailable("siop_mode")) {
            resetSIOPMode();
        }
        return isSettingsAccessiblePackage(str, str2, z);
    }

    static void pkgRemovedRestore(boolean z, String str, int i) {
        PackageDbHelper.getInstance().removeFeatureFlagByPackageName(str);
        GosLog.d(LOG_TAG, "onPackageRemoved(), PackageChanged: an app is removed, " + str);
        if (z && DbHelper.getInstance().getPackageDao().getPkgCountByCategory(Constants.CategoryCode.GAME) == 0) {
            DataManager.getInstance().setModes(1, 1, false, false, false);
        }
    }

    private static void resetSIOPMode() {
        GosLog.d(LOG_TAG, "onPackageRemoved(), Game Booster is disabled. reset SIOP_MODE");
        DbHelper.getInstance().getGlobalDao().setPrevSiopModeByUser(new Global.IdAndPrevSiopModeByUser(-1000));
        int defaultSiopMode = SiopModeFeature.getDefaultSiopMode();
        if (DbHelper.getInstance().getGlobalDao().getSiopMode() != defaultSiopMode) {
            DbHelper.getInstance().getGlobalDao().setSiopMode(new Global.IdAndSiopMode(defaultSiopMode));
            GmsGlobalPackageDataSetter.getInstance().applyAllGames(false);
        }
    }

    private static boolean isSettingsAccessiblePackage(String str, String str2, boolean z) {
        List<String> settableFeatures = GlobalDbHelper.getInstance().getSettableFeatures(str);
        boolean z2 = false;
        if (!GlobalDbHelper.getInstance().removeSettingsAccessiblePackage(str)) {
            return false;
        }
        if (settableFeatures != null) {
            ArrayList arrayList = new ArrayList();
            for (String next : settableFeatures) {
                if (GlobalDbHelper.getInstance().getFeatureSettersCount(next) == 0) {
                    arrayList.add(next);
                }
            }
            DataUpdater.restoreFeatureSettingsToDefault(AppContext.get(), (String[]) arrayList.toArray(new String[arrayList.size()]));
            z2 = isInvokeApplyAllGamesNeeded(arrayList);
        }
        GosLog.i(LOG_TAG, str2 + "PackageChanged: a settingApp removed, " + str);
        LocalLogDbHelper instance = LocalLogDbHelper.getInstance();
        instance.addLocalLog(LOG_TAG, str2 + "PackageChanged: a settingApp removed, " + str);
        if (z) {
            DbHelper.getInstance().getPackageDao().removePkg(str);
            PackageDbHelper.getInstance().removeFeatureFlagByPackageName(str);
        }
        GosLog.d(LOG_TAG, str2 + "invoke applyAllGames. invokeApplyAllGames: " + z2);
        if (z2) {
            GosLog.d(LOG_TAG, str2 + "restore static values");
            SeActivityManager.getInstance().forceStopPackages(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
            GmsGlobalPackageDataSetter.getInstance().applyAllGames(true);
        }
        return true;
    }

    static boolean isInvokeApplyAllGamesNeeded(ArrayList<String> arrayList) {
        Map<String, StaticInterface> staticFeatureMap = FeatureSetManager.getStaticFeatureMap(AppContext.get());
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            Iterator<String> it2 = staticFeatureMap.keySet().iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (it2.next().equals(next)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static void addPkgDataFromServer(Context context, NetworkConnector networkConnector, String str, int i, String str2) {
        PkgData pkgData;
        CategoryResponse categoryResponse;
        PkgData pkgData2;
        GosLog.d(LOG_TAG, "addPkgDataFromServer(), packageName : " + str);
        PackageDbHelper.TimeStampOpt timeStampOpt = PackageDbHelper.TimeStampOpt.ADD_MISSING_PKG_DATA;
        LocalCache localCache = new LocalCache();
        PkgData pkgData3 = null;
        if (localCache.getSecGameFamilyPackageNames().contains(str)) {
            pkgData = new PkgData(new Package(str), (Map<String, FeatureFlag>) null);
            pkgData.setCategoryCode(Constants.CategoryCode.SEC_GAME_FAMILY);
            GosLog.i(LOG_TAG, "addPkgDataFromServer(), Set a package category sec_game_family by local cache : " + str);
        } else if (PackageUtil.isVrApp(context, str)) {
            pkgData = new PkgData(new Package(str), (Map<String, FeatureFlag>) null);
            pkgData.setCategoryCode(Constants.CategoryCode.VR_GAME);
            GosLog.i(LOG_TAG, "addPkgDataFromServer(), Set a package category vr_game by manifest : " + str);
        } else if (PackageUtil.isNonGamePackage(str)) {
            pkgData = new PkgData(new Package(str), (Map<String, FeatureFlag>) null);
            pkgData.setCategoryCode(Constants.CategoryCode.NON_GAME);
            GosLog.i(LOG_TAG, "addPkgDataFromServer(), Set a non-game category, pkgName: " + str);
        } else {
            String category = CategoryInfoDbHelper.getInstance().getCategory(str);
            if (category != null) {
                categoryResponse = new CategoryResponse(str, category);
                if (CategoryInfoDbHelper.getInstance().isFixed(str)) {
                    GosLog.i(LOG_TAG, "addPkgDataFromServer(), use pre category: " + category + ", and skip category query.");
                } else {
                    GosLog.i(LOG_TAG, "addPkgDataFromServer(), use pre category: " + category);
                    CategoryUpdateReservedDbHelper.getInstance().addReservedCategory(str);
                }
            } else if (networkConnector != null) {
                categoryResponse = networkConnector.getSingleCategoryResponse(str, str2);
                StringBuilder sb = new StringBuilder();
                sb.append("addPkgDataFromServer(), use server category: ");
                sb.append(categoryResponse != null ? categoryResponse.getPkgType() : null);
                GosLog.i(LOG_TAG, sb.toString());
            } else {
                categoryResponse = null;
            }
            if (categoryResponse == null) {
                GosLog.e(LOG_TAG, "addPkgDataFromServer(), response is null!");
                return;
            }
            PkgData pkgData4 = new PkgData(new Package(str), new ArrayMap());
            if (categoryResponse.getPkgType() == null || !categoryResponse.getPkgType().equals(Constants.CategoryCode.GAME)) {
                pkgData3 = DataUpdater.mergePackageAndNotify(pkgData4, categoryResponse);
                timeStampOpt = PackageDbHelper.TimeStampOpt.ADD_PKG_DATA;
            } else {
                BaseResponse singlePkgPolicyResponse = networkConnector != null ? networkConnector.getSinglePkgPolicyResponse(categoryResponse, str2) : null;
                if (singlePkgPolicyResponse != null) {
                    pkgData3 = DataUpdater.mergePackageAndNotify(pkgData4, singlePkgPolicyResponse);
                    timeStampOpt = PackageDbHelper.TimeStampOpt.ADD_PKG_DATA;
                }
            }
            if (pkgData3 == null && DbHelper.getInstance().getPackageDao().getPackage(str) == null) {
                pkgData2 = new PkgData(new Package(str), new ArrayMap());
                pkgData2.setCategoryCode(Constants.CategoryCode.UNDEFINED);
            } else {
                pkgData2 = pkgData3;
            }
            if (pkgData2 != null && ((pkgData2.getCategoryCode().equals(Constants.CategoryCode.UNDEFINED) || pkgData2.getCategoryCode().equals("unknown")) && localCache.getGamePackageNames().contains(str))) {
                pkgData2.setCategoryCode(Constants.CategoryCode.GAME);
                timeStampOpt = PackageDbHelper.TimeStampOpt.ADD_MISSING_PKG_DATA;
            }
            pkgData = pkgData2;
        }
        if (pkgData != null) {
            pkgData.addInstalledUserId(i);
            PackageDbHelper.getInstance().insertOrUpdate(pkgData.getPkg(), timeStampOpt);
            Map<String, FeatureFlag> featureFlagMap = pkgData.getFeatureFlagMap();
            if (featureFlagMap != null) {
                FeatureFlagUtil.fillMissingFeatureFlag(featureFlagMap, pkgData.getPackageName());
                DbHelper.getInstance().getFeatureFlagDao().insertOrUpdate(featureFlagMap.values());
            }
            GosLog.i(LOG_TAG, "addPkgDataFromServer(), A package was added : " + str + " as " + pkgData.getCategoryCode());
        }
    }

    public static void onPackageUpdated(String str) {
        Package packageR;
        GosLog.d(LOG_TAG, "onPackageUpdated(). packageName: " + str + ", versionCode: " + PackageUtil.getPackageVersionCode(AppContext.get(), str));
        if (str != null && (packageR = DbHelper.getInstance().getPackageDao().getPackage(str)) != null && Constants.CategoryCode.GAME.equalsIgnoreCase(packageR.categoryCode)) {
            collectInstalledOrUpdatedGameInfo(packageR, 2);
        }
    }

    private static void collectAlreadyInstalledGameInfo(int i) {
        for (String str : DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME)) {
            collectInstalledOrUpdatedGameInfo(DbHelper.getInstance().getPackageDao().getPackage(str), i);
        }
    }

    static Pair<String, Long> collectInstalledOrUpdatedGameInfo(Package packageR, int i) {
        if (packageR == null) {
            return null;
        }
        GameInfoCollector gameInfoCollector = new GameInfoCollector(AppContext.get(), packageR.pkgName);
        String versionName = gameInfoCollector.getVersionName();
        long versionCode = gameInfoCollector.getVersionCode();
        Pair<String, Long> pair = new Pair<>(versionName, Long.valueOf(versionCode));
        if (!Constants.CategoryCode.GAME.equalsIgnoreCase(packageR.categoryCode)) {
            GosLog.e(LOG_TAG, "collectInstalledOrUpdatedGameInfo()-no game: " + packageR.pkgName);
            return pair;
        }
        DbHelper.getInstance().getPackageDao().setVersionInfo(new Package.PkgNameAndVersionInfo(packageR.pkgName, versionName, versionCode));
        if (SystemDataHelper.isCollectingAgreedByUser(AppContext.get())) {
            String gameInfoJsonMsg = gameInfoCollector.getGameInfoJsonMsg(i);
            ReportDbHelper.getInstance().addOrUpdateReportData(ReportData.TAG_GAME_INFO, System.currentTimeMillis(), gameInfoJsonMsg);
            GosLog.i(LOG_TAG, "collectInstalledOrUpdatedGameInfo()-msg: " + gameInfoJsonMsg);
        }
        return pair;
    }

    static void checkReservedCategory(NetworkConnector networkConnector, String str) {
        CategoryResponse singleCategoryResponse;
        List<String> reservedCategoryList = CategoryUpdateReservedDbHelper.getInstance().getReservedCategoryList();
        if (reservedCategoryList == null || reservedCategoryList.size() == 0) {
            GosLog.i(LOG_TAG, "checkReservedCategory() - reservedList is null or empty!");
            return;
        }
        GosLog.i(LOG_TAG, "checkReservedCategory() - size: " + reservedCategoryList.size() + ", reservedList: " + reservedCategoryList.toString());
        ArrayList<Pair> arrayList = new ArrayList<>();
        if (reservedCategoryList.size() != 1) {
            for (String categoryFromServer : networkConnector.getPackageNamesForQuery(reservedCategoryList)) {
                List<CategoryResponse> categoryFromServer2 = networkConnector.getCategoryFromServer(categoryFromServer, str);
                if (categoryFromServer2 != null) {
                    for (CategoryResponse next : categoryFromServer2) {
                        arrayList.add(new Pair(next.getPkgName(), next.getPkgType()));
                    }
                }
            }
        } else if (!(reservedCategoryList.get(0) == null || (singleCategoryResponse = networkConnector.getSingleCategoryResponse(reservedCategoryList.get(0), str)) == null)) {
            arrayList.add(new Pair(singleCategoryResponse.getPkgName(), singleCategoryResponse.getPkgType()));
        }
        for (Pair pair : arrayList) {
            String str2 = (String) pair.first;
            String str3 = (String) pair.second;
            if (str2 == null || str3 == null) {
                break;
            }
            Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str2);
            String str4 = null;
            if (packageR != null) {
                str4 = packageR.getCategoryCode();
            }
            if (str3.equals(str4)) {
                reservedCategoryList.remove(str2);
                DbHelper.getInstance().getCategoryUpdateReservedDao().delete(str2);
            }
        }
        if (reservedCategoryList.size() != 0) {
            GosLog.i(LOG_TAG, "checkReservedCategory() - size: " + reservedCategoryList.size() + ", remains reservedList: " + reservedCategoryList.toString());
            DataUpdater.updatePackageList(AppContext.get(), networkConnector, reservedCategoryList, str);
            List<String> pkgNameListByCategory = DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME);
            if (pkgNameListByCategory != null) {
                for (String next2 : reservedCategoryList) {
                    if (pkgNameListByCategory.contains(next2)) {
                        GmsGlobalPackageDataSetter.getInstance().applySingleGame(next2);
                    }
                }
            }
            DbHelper.getInstance().getCategoryUpdateReservedDao().deleteAll();
        }
    }
}
