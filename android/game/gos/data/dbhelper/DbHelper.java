package com.samsung.android.game.gos.data.dbhelper;

import com.samsung.android.game.gos.data.dao.CategoryUpdateReservedDao;
import com.samsung.android.game.gos.data.dao.ClearBGSurviveAppsDao;
import com.samsung.android.game.gos.data.dao.EventSubscriptionDao;
import com.samsung.android.game.gos.data.dao.FeatureFlagDao;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dao.GosServiceUsageDao;
import com.samsung.android.game.gos.data.dao.LocalLogDao;
import com.samsung.android.game.gos.data.dao.MonitoredAppsDao;
import com.samsung.android.game.gos.data.dao.PackageDao;
import com.samsung.android.game.gos.data.dao.PerfDataPermissionDao;
import com.samsung.android.game.gos.data.dao.SettingsAccessiblePackageDao;
import com.samsung.android.game.gos.data.database.GosDatabase;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DbHelper {
    private GosDatabase db;

    private DbHelper() {
    }

    public static DbHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DbHelper INSTANCE = new DbHelper();

        private SingletonHolder() {
        }
    }

    public void init(GosDatabase gosDatabase) {
        this.db = gosDatabase;
        this.db.globalDao().insert(Global.getDefaultGlobal());
        Set<String> set = Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES;
        Map<String, GlobalFeatureFlag> featureFlagMap = GlobalDbHelper.getInstance().getFeatureFlagMap();
        ArrayList arrayList = new ArrayList();
        for (String next : set) {
            if (!featureFlagMap.containsKey(next)) {
                arrayList.add(new GlobalFeatureFlag(next, "inherited"));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry next2 : featureFlagMap.entrySet()) {
            if (!set.contains(next2.getKey())) {
                arrayList2.add(next2.getValue());
            }
        }
        if (!arrayList.isEmpty()) {
            this.db.globalFeatureFlagDao().insertOrUpdate((Collection<GlobalFeatureFlag>) arrayList);
        }
        if (!arrayList2.isEmpty()) {
            this.db.globalFeatureFlagDao().delete(arrayList2);
        }
        List<String> featureNames = this.db.featureFlagDao().getFeatureNames();
        ArrayList arrayList3 = new ArrayList();
        for (String next3 : featureNames) {
            if (!set.contains(next3)) {
                arrayList3.add(next3);
            }
        }
        if (!arrayList3.isEmpty()) {
            this.db.featureFlagDao().deleteByFeatureName(arrayList3);
        }
    }

    public void close() {
        GosDatabase gosDatabase = this.db;
        if (gosDatabase != null) {
            gosDatabase.getOpenHelper().close();
            this.db.close();
        }
    }

    public EventSubscriptionDao getEventSubscriptionDao() {
        return this.db.eventSubscriptionDao();
    }

    public FeatureFlagDao getFeatureFlagDao() {
        return this.db.featureFlagDao();
    }

    public GlobalDao getGlobalDao() {
        return this.db.globalDao();
    }

    public GlobalFeatureFlagDao getGlobalFeatureFlagDao() {
        return this.db.globalFeatureFlagDao();
    }

    public LocalLogDao getLocalLogDao() {
        return this.db.localLogDao();
    }

    public MonitoredAppsDao getMonitoredAppsDao() {
        return this.db.monitoredAppsDao();
    }

    public PackageDao getPackageDao() {
        return this.db.packageDao();
    }

    public PerfDataPermissionDao getPerfDataPermissionDao() {
        return this.db.perfDataPermissionDao();
    }

    public SettingsAccessiblePackageDao getSettingsAccessiblePackageDao() {
        return this.db.settingsAccessiblePackageDao();
    }

    public CategoryUpdateReservedDao getCategoryUpdateReservedDao() {
        return this.db.categoryUpdateReservedDao();
    }

    public GosServiceUsageDao getGosServiceUsageDao() {
        return this.db.gosServiceUsageDao();
    }

    public ClearBGSurviveAppsDao getClearBGSurviveAppsDao() {
        return this.db.clearBGSurviveAppsDao();
    }
}
