package com.samsung.android.game.gos.data.dbhelper;

import android.util.ArrayMap;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dao.FeatureFlagDao;
import com.samsung.android.game.gos.data.dao.PackageDao;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageDbHelper {
    private static final String LOG_TAG = PackageDbHelper.class.getSimpleName();
    private final FeatureFlagDao featureFlagDao;
    private final PackageDao packageDao;

    public enum TimeStampOpt {
        NONE,
        UPDATE_PKG_DATA,
        ADD_PKG_DATA,
        ADD_MISSING_PKG_DATA
    }

    /* synthetic */ PackageDbHelper(AnonymousClass1 r1) {
        this();
    }

    private PackageDbHelper() {
        this.packageDao = DbHelper.getInstance().getPackageDao();
        this.featureFlagDao = DbHelper.getInstance().getFeatureFlagDao();
    }

    public static PackageDbHelper getInstance() {
        if (AppVariable.isUnitTest()) {
            return new PackageDbHelper();
        }
        return SingletonHolder.INSTANCE;
    }

    public void setServerPolicy(String str, String str2, String str3) {
        FeatureFlag byFeatureNameAndPkgName = this.featureFlagDao.getByFeatureNameAndPkgName(str, str2);
        if (byFeatureNameAndPkgName == null) {
            byFeatureNameAndPkgName = new FeatureFlag(str, str2, str3);
        } else {
            byFeatureNameAndPkgName.setState(str3);
        }
        this.featureFlagDao.insertOrUpdate(byFeatureNameAndPkgName);
    }

    public Map<String, PkgData> getPkgDataMap(Collection<String> collection) {
        GosLog.d(LOG_TAG, "getPkgDataMap(), begin");
        HashMap hashMap = new HashMap();
        if (collection == null) {
            return hashMap;
        }
        for (String next : collection) {
            Package packageR = this.packageDao.getPackage(next);
            if (packageR != null) {
                hashMap.put(next, new PkgData(packageR, getFeatureFlagMap(next)));
            } else {
                hashMap.put(next, (Object) null);
            }
        }
        return hashMap;
    }

    public PkgData getPkgData(String str) {
        Package packageR;
        GosLog.d(LOG_TAG, "getPkgData(), begin");
        if (str == null || (packageR = this.packageDao.getPackage(str)) == null) {
            return null;
        }
        return new PkgData(packageR, getFeatureFlagMap(str));
    }

    public Map<String, Package> getPackageMap(Collection<String> collection) {
        GosLog.d(LOG_TAG, "getPackageMap(), begin");
        HashMap hashMap = new HashMap();
        if (collection == null) {
            return hashMap;
        }
        for (String next : collection) {
            Package packageR = this.packageDao.getPackage(next);
            if (packageR != null) {
                hashMap.put(next, packageR);
            } else {
                hashMap.put(next, (Object) null);
            }
        }
        return hashMap;
    }

    public boolean removePkgList(List<String> list) {
        GosLog.d(LOG_TAG, "removePkgList(), begin");
        ArrayList arrayList = new ArrayList();
        for (String next : list) {
            if (next != null) {
                Package packageR = new Package(next);
                packageR.setCategoryCode("toRemove");
                arrayList.add(packageR);
            }
        }
        this.packageDao.insertOrUpdate((List<Package>) arrayList);
        List<Package> packageListByCategory = this.packageDao.getPackageListByCategory("toRemove");
        List<String> pkgNameListByCategory = this.packageDao.getPkgNameListByCategory("toRemove");
        int removePkgList = this.packageDao.removePkgList(packageListByCategory);
        removeFeatureFlagByPackageName(pkgNameListByCategory);
        return list.size() == removePkgList;
    }

    public void replaceTunableNonGameApps() {
        GosLog.d(LOG_TAG, "replaceTunableNonGameApps(), begin");
        List<Package> packageListByCategory = this.packageDao.getPackageListByCategory("tunable non-game");
        if (packageListByCategory != null) {
            ArrayList arrayList = new ArrayList();
            for (Package next : packageListByCategory) {
                next.setCategoryCode(Constants.CategoryCode.NON_GAME);
                arrayList.add(next);
            }
            this.packageDao.insertOrUpdate((List<Package>) arrayList);
        }
    }

    public void removeFeatureFlagByPackageName(String str) {
        GosLog.d(LOG_TAG, "removeByPackageName(), begin");
        this.featureFlagDao.delete(this.featureFlagDao.getByPkgName(str));
    }

    public void removeFeatureFlagByPackageName(List<String> list) {
        GosLog.d(LOG_TAG, "removeByPackageName(), begin");
        ArrayList arrayList = new ArrayList();
        for (String byPkgName : list) {
            arrayList.addAll(this.featureFlagDao.getByPkgName(byPkgName));
        }
        this.featureFlagDao.delete(arrayList);
    }

    public Map<String, FeatureFlag> getFeatureFlagMap(String str) {
        ArrayMap arrayMap = new ArrayMap(Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES.size());
        List<FeatureFlag> byPkgName = DbHelper.getInstance().getFeatureFlagDao().getByPkgName(str);
        if (byPkgName != null) {
            for (FeatureFlag next : byPkgName) {
                arrayMap.put(next.getName(), next);
            }
        }
        return arrayMap;
    }

    public void insertOrUpdate(Package packageR, TimeStampOpt timeStampOpt) {
        GosLog.d(LOG_TAG, String.format("insertOrUpdate() - TimeStampOpt.%s:", new Object[]{timeStampOpt.toString()}));
        updateTimeStamp(packageR, timeStampOpt);
        DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
    }

    public void insertOrUpdate(List<Package> list, TimeStampOpt timeStampOpt) {
        GosLog.d(LOG_TAG, String.format("insertOrUpdate() - TimeStampOpt.%s:", new Object[]{timeStampOpt.toString()}));
        for (Package updateTimeStamp : list) {
            updateTimeStamp(updateTimeStamp, timeStampOpt);
        }
        DbHelper.getInstance().getPackageDao().insertOrUpdate(list);
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final PackageDbHelper INSTANCE = new PackageDbHelper((AnonymousClass1) null);

        private SingletonHolder() {
        }
    }

    private void updateTimeStamp(Package packageR, TimeStampOpt timeStampOpt) {
        long j;
        long j2;
        Package packageR2 = DbHelper.getInstance().getPackageDao().getPackage(packageR.getPkgName());
        if (packageR2 != null) {
            j2 = packageR2.getPkgAddedTime();
            j = packageR2.getServerPkgUpdatedTime();
        } else {
            j2 = 0;
            j = 0;
        }
        int i = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$data$dbhelper$PackageDbHelper$TimeStampOpt[timeStampOpt.ordinal()];
        if (i == 1) {
            j = System.currentTimeMillis();
        } else if (i == 2) {
            if (j2 == 0) {
                j2 = System.currentTimeMillis();
            }
            j = System.currentTimeMillis();
        } else if (i == 3 && j2 == 0) {
            j2 = System.currentTimeMillis();
        }
        packageR.pkgAddedTime = j2;
        packageR.serverPkgUpdatedTime = j;
    }

    /* renamed from: com.samsung.android.game.gos.data.dbhelper.PackageDbHelper$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$data$dbhelper$PackageDbHelper$TimeStampOpt;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.samsung.android.game.gos.data.dbhelper.PackageDbHelper$TimeStampOpt[] r0 = com.samsung.android.game.gos.data.dbhelper.PackageDbHelper.TimeStampOpt.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$data$dbhelper$PackageDbHelper$TimeStampOpt = r0
                com.samsung.android.game.gos.data.dbhelper.PackageDbHelper$TimeStampOpt r1 = com.samsung.android.game.gos.data.dbhelper.PackageDbHelper.TimeStampOpt.UPDATE_PKG_DATA     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$data$dbhelper$PackageDbHelper$TimeStampOpt     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.data.dbhelper.PackageDbHelper$TimeStampOpt r1 = com.samsung.android.game.gos.data.dbhelper.PackageDbHelper.TimeStampOpt.ADD_PKG_DATA     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$data$dbhelper$PackageDbHelper$TimeStampOpt     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.data.dbhelper.PackageDbHelper$TimeStampOpt r1 = com.samsung.android.game.gos.data.dbhelper.PackageDbHelper.TimeStampOpt.ADD_MISSING_PKG_DATA     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.data.dbhelper.PackageDbHelper.AnonymousClass1.<clinit>():void");
        }
    }

    public static boolean isInstalledUserId(Package packageR, int i) {
        Integer[] installedUserIds = packageR.getInstalledUserIds();
        if (installedUserIds != null) {
            for (Integer num : installedUserIds) {
                if (num != null && num.intValue() == i) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Package removeInstalledUserId(Package packageR, int i) {
        if (!(packageR == null || packageR.getInstalledUserIds() == null)) {
            ArrayList arrayList = new ArrayList(Arrays.asList(packageR.getInstalledUserIds()));
            if (arrayList.contains(Integer.valueOf(i))) {
                arrayList.remove(Integer.valueOf(i));
            }
            packageR.setInstalledUserIds((Integer[]) arrayList.toArray(new Integer[0]));
        }
        return packageR;
    }
}
