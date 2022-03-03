package com.samsung.android.game.gos.data.database;

import androidx.core.app.NotificationCompat;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.dao.CategoryUpdateReservedDao;
import com.samsung.android.game.gos.data.dao.CategoryUpdateReservedDao_Impl;
import com.samsung.android.game.gos.data.dao.ClearBGSurviveAppsDao;
import com.samsung.android.game.gos.data.dao.ClearBGSurviveAppsDao_Impl;
import com.samsung.android.game.gos.data.dao.EventSubscriptionDao;
import com.samsung.android.game.gos.data.dao.EventSubscriptionDao_Impl;
import com.samsung.android.game.gos.data.dao.FeatureFlagDao;
import com.samsung.android.game.gos.data.dao.FeatureFlagDao_Impl;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dao.GlobalDao_Impl;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao_Impl;
import com.samsung.android.game.gos.data.dao.GosServiceUsageDao;
import com.samsung.android.game.gos.data.dao.GosServiceUsageDao_Impl;
import com.samsung.android.game.gos.data.dao.LocalLogDao;
import com.samsung.android.game.gos.data.dao.LocalLogDao_Impl;
import com.samsung.android.game.gos.data.dao.MonitoredAppsDao;
import com.samsung.android.game.gos.data.dao.MonitoredAppsDao_Impl;
import com.samsung.android.game.gos.data.dao.PackageDao;
import com.samsung.android.game.gos.data.dao.PackageDao_Impl;
import com.samsung.android.game.gos.data.dao.PerfDataPermissionDao;
import com.samsung.android.game.gos.data.dao.PerfDataPermissionDao_Impl;
import com.samsung.android.game.gos.data.dao.SettingsAccessiblePackageDao;
import com.samsung.android.game.gos.data.dao.SettingsAccessiblePackageDao_Impl;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.HashMap;
import java.util.HashSet;

public final class GosDatabase_Impl extends GosDatabase {
    private volatile CategoryUpdateReservedDao _categoryUpdateReservedDao;
    private volatile ClearBGSurviveAppsDao _clearBGSurviveAppsDao;
    private volatile EventSubscriptionDao _eventSubscriptionDao;
    private volatile FeatureFlagDao _featureFlagDao;
    private volatile GlobalDao _globalDao;
    private volatile GlobalFeatureFlagDao _globalFeatureFlagDao;
    private volatile GosServiceUsageDao _gosServiceUsageDao;
    private volatile LocalLogDao _localLogDao;
    private volatile MonitoredAppsDao _monitoredAppsDao;
    private volatile PackageDao _packageDao;
    private volatile PerfDataPermissionDao _perfDataPermissionDao;
    private volatile SettingsAccessiblePackageDao _settingsAccessiblePackageDao;

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new RoomOpenHelper.Delegate(15) {
            public void onPostMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            }

            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `EventSubscription` (`eventName` TEXT NOT NULL, `subscriberPkgName` TEXT NOT NULL, `intentActionName` TEXT, `flags` INTEGER NOT NULL DEFAULT 0, `receiver_type` TEXT DEFAULT 'b', PRIMARY KEY(`eventName`, `subscriberPkgName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `FeatureFlag` (`name` TEXT NOT NULL, `pkgName` TEXT NOT NULL, `state` TEXT, `inheritedFlag` INTEGER NOT NULL, `forcedFlag` INTEGER NOT NULL, `enabledFlagByServer` INTEGER NOT NULL, `enabledFlagByUser` INTEGER NOT NULL, `enabled` INTEGER NOT NULL, PRIMARY KEY(`name`, `pkgName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Global` (`id` INTEGER NOT NULL, `initialized` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL, `fullyUpdateTime` INTEGER NOT NULL, `defaultCpuLevel` INTEGER NOT NULL, `defaultGpuLevel` INTEGER NOT NULL, `eachModeDss` TEXT, `eachModeDfs` TEXT, `resolutionMode` INTEGER NOT NULL, `customDss` REAL NOT NULL, `dfsMode` INTEGER NOT NULL, `customDfs` REAL NOT NULL, `vrrMaxValue` INTEGER NOT NULL DEFAULT 60, `vrrMinValue` INTEGER NOT NULL DEFAULT 48, `drrAllowed` INTEGER NOT NULL, `ipmMode` INTEGER NOT NULL, `ipmTargetPower` INTEGER NOT NULL, `ipmTargetTemperature` INTEGER NOT NULL, `ipmUpdateTime` INTEGER NOT NULL, `ipmTrainingVersion` TEXT, `ipmCpuBottomFreq` INTEGER NOT NULL, `ipmFlag` TEXT, `siopMode` INTEGER NOT NULL, `eachModeTargetShortSide` TEXT, `governorSettings` TEXT, `resumeBoostPolicy` TEXT, `loggingPolicy` TEXT, `ipmPolicy` TEXT, `siopModePolicy` TEXT, `launchBoostPolicy` TEXT, `wifiQosPolicy` TEXT, `gfiPolicy` TEXT, `tspPolicy` TEXT, `ringlogPolicy` TEXT, `uuid` TEXT, `registeredDevice` INTEGER NOT NULL, `gmsVersion` REAL NOT NULL, `showLogcat` INTEGER NOT NULL, `automaticUpdate` INTEGER NOT NULL, `targetServer` INTEGER NOT NULL, `deviceName` TEXT, `prevSiopModeByUser` INTEGER NOT NULL, `sosPolicyKeyCsv` TEXT, `gosSelfUpdateStatus` INTEGER NOT NULL, `dataReady` INTEGER NOT NULL, `dmaId` TEXT, `clearBGLruNum` INTEGER NOT NULL DEFAULT 1, `clearBGSurviveAppFromServer` TEXT, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `GlobalFeatureFlag` (`name` TEXT NOT NULL, `available` INTEGER NOT NULL, `usingUserValue` INTEGER NOT NULL, `usingPkgValue` INTEGER NOT NULL, `state` TEXT NOT NULL, `inheritedFlag` INTEGER NOT NULL, `forcedFlag` INTEGER NOT NULL, `enabledFlagByServer` INTEGER NOT NULL, `enabledFlagByUser` INTEGER NOT NULL, PRIMARY KEY(`name`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `LocalLog` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timeStamp` INTEGER NOT NULL, `time` TEXT, `tag` TEXT, `msg` TEXT)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `MonitoredApps` (`pkgName` TEXT NOT NULL, `subscriberPkgName` TEXT NOT NULL, PRIMARY KEY(`pkgName`, `subscriberPkgName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Package` (`pkgName` TEXT NOT NULL, `categoryCode` TEXT NOT NULL DEFAULT 'undefined', `installedUserIds` TEXT NOT NULL DEFAULT '', `defaultSetBy` INTEGER NOT NULL DEFAULT 0, `pkgAddedTime` INTEGER NOT NULL DEFAULT 0, `serverPkgUpdatedTime` INTEGER NOT NULL DEFAULT 0, `eachModeTargetShortSide` TEXT DEFAULT '-1,-1,-1,-1', `eachModeDss` TEXT DEFAULT '-1,-1,-1,-1', `customResolutionMode` INTEGER NOT NULL DEFAULT 1, `customDss` REAL NOT NULL DEFAULT 75, `eachModeDfs` TEXT, `customDfsMode` INTEGER NOT NULL DEFAULT 1, `customDfs` REAL NOT NULL DEFAULT 60, `defaultCpuLevel` INTEGER NOT NULL, `defaultGpuLevel` INTEGER NOT NULL, `governorSettings` TEXT, `shiftTemperature` INTEGER NOT NULL DEFAULT -1000, `ipmPolicy` TEXT, `siopModePolicy` TEXT, `customSiopMode` INTEGER NOT NULL DEFAULT 1, `gameSdkSettings` TEXT, `sosPolicy` TEXT, `resumeBoostPolicy` TEXT, `launchBoostPolicy` TEXT, `wifiQosPolicy` TEXT, `subscriberList` TEXT, `gfiPolicy` TEXT, `tspPolicy` TEXT, `appliedDss` REAL NOT NULL, `appliedCpuLevel` INTEGER NOT NULL, `appliedGpuLevel` INTEGER NOT NULL, `versionName` TEXT, `versionCode` INTEGER NOT NULL DEFAULT -1, `vrrMaxValue` INTEGER NOT NULL DEFAULT -1, `vrrMinValue` INTEGER NOT NULL DEFAULT -1, `drrAllowed` INTEGER NOT NULL, `targetGroupName` TEXT, PRIMARY KEY(`pkgName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `PerfDataPermission` (`pkgName` TEXT NOT NULL, `permType` INTEGER NOT NULL, `paramListCsv` TEXT, `permPolicy` INTEGER NOT NULL, `lastHandshakeTime` TEXT, PRIMARY KEY(`pkgName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `SettingsAccessiblePackage` (`pkgName` TEXT NOT NULL, `featureName` TEXT NOT NULL, PRIMARY KEY(`pkgName`, `featureName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `CategoryUpdateReserved` (`pkgName` TEXT NOT NULL, PRIMARY KEY(`pkgName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `GosServiceUsage` (`command` TEXT NOT NULL, `callerPkgName` TEXT NOT NULL, PRIMARY KEY(`command`, `callerPkgName`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `ClearBGSurviveApps` (`callerPkgName` TEXT NOT NULL, `surviveAppPkgName` TEXT NOT NULL, PRIMARY KEY(`callerPkgName`, `surviveAppPkgName`))");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '20f2e28c4ed036def34550df26f03354')");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `EventSubscription`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `FeatureFlag`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `Global`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `GlobalFeatureFlag`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `LocalLog`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `MonitoredApps`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `Package`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `PerfDataPermission`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `SettingsAccessiblePackage`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `CategoryUpdateReserved`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `GosServiceUsage`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `ClearBGSurviveApps`");
                if (GosDatabase_Impl.this.mCallbacks != null) {
                    int size = GosDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) GosDatabase_Impl.this.mCallbacks.get(i)).onDestructiveMigration(supportSQLiteDatabase);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (GosDatabase_Impl.this.mCallbacks != null) {
                    int size = GosDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) GosDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase unused = GosDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                GosDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (GosDatabase_Impl.this.mCallbacks != null) {
                    int size = GosDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) GosDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            public void onPreMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
                DBUtil.dropFtsSyncTriggers(supportSQLiteDatabase);
            }

            /* access modifiers changed from: protected */
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase supportSQLiteDatabase2 = supportSQLiteDatabase;
                HashMap hashMap = new HashMap(5);
                hashMap.put("eventName", new TableInfo.Column("eventName", "TEXT", true, 1, (String) null, 1));
                hashMap.put("subscriberPkgName", new TableInfo.Column("subscriberPkgName", "TEXT", true, 2, (String) null, 1));
                hashMap.put("intentActionName", new TableInfo.Column("intentActionName", "TEXT", false, 0, (String) null, 1));
                hashMap.put(GosInterface.KeyName.FLAGS, new TableInfo.Column(GosInterface.KeyName.FLAGS, "INTEGER", true, 0, "0", 1));
                hashMap.put(GosInterface.KeyName.RECEIVER_TYPE, new TableInfo.Column(GosInterface.KeyName.RECEIVER_TYPE, "TEXT", false, 0, "'b'", 1));
                TableInfo tableInfo = new TableInfo("EventSubscription", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase2, "EventSubscription");
                if (!tableInfo.equals(read)) {
                    return new RoomOpenHelper.ValidationResult(false, "EventSubscription(com.samsung.android.game.gos.data.model.EventSubscription).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
                }
                HashMap hashMap2 = new HashMap(8);
                hashMap2.put(ManagerInterface.KeyName.REQUEST_NAME, new TableInfo.Column(ManagerInterface.KeyName.REQUEST_NAME, "TEXT", true, 1, (String) null, 1));
                hashMap2.put(EventPublisher.EXTRA_KEY_PKG_NAME, new TableInfo.Column(EventPublisher.EXTRA_KEY_PKG_NAME, "TEXT", true, 2, (String) null, 1));
                hashMap2.put("state", new TableInfo.Column("state", "TEXT", false, 0, (String) null, 1));
                hashMap2.put("inheritedFlag", new TableInfo.Column("inheritedFlag", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("forcedFlag", new TableInfo.Column("forcedFlag", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("enabledFlagByServer", new TableInfo.Column("enabledFlagByServer", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("enabledFlagByUser", new TableInfo.Column("enabledFlagByUser", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, (String) null, 1));
                TableInfo tableInfo2 = new TableInfo("FeatureFlag", hashMap2, new HashSet(0), new HashSet(0));
                TableInfo read2 = TableInfo.read(supportSQLiteDatabase2, "FeatureFlag");
                if (!tableInfo2.equals(read2)) {
                    return new RoomOpenHelper.ValidationResult(false, "FeatureFlag(com.samsung.android.game.gos.data.model.FeatureFlag).\n Expected:\n" + tableInfo2 + "\n Found:\n" + read2);
                }
                HashMap hashMap3 = new HashMap(48);
                hashMap3.put("id", new TableInfo.Column("id", "INTEGER", true, 1, (String) null, 1));
                hashMap3.put("initialized", new TableInfo.Column("initialized", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("fullyUpdateTime", new TableInfo.Column("fullyUpdateTime", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("defaultCpuLevel", new TableInfo.Column("defaultCpuLevel", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("defaultGpuLevel", new TableInfo.Column("defaultGpuLevel", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("eachModeDss", new TableInfo.Column("eachModeDss", "TEXT", false, 0, (String) null, 1));
                Object obj = "defaultGpuLevel";
                hashMap3.put("eachModeDfs", new TableInfo.Column("eachModeDfs", "TEXT", false, 0, (String) null, 1));
                Object obj2 = "defaultCpuLevel";
                hashMap3.put("resolutionMode", new TableInfo.Column("resolutionMode", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("customDss", new TableInfo.Column("customDss", "REAL", true, 0, (String) null, 1));
                Object obj3 = "eachModeDfs";
                hashMap3.put("dfsMode", new TableInfo.Column("dfsMode", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("customDfs", new TableInfo.Column("customDfs", "REAL", true, 0, (String) null, 1));
                Object obj4 = "customDfs";
                hashMap3.put("vrrMaxValue", new TableInfo.Column("vrrMaxValue", "INTEGER", true, 0, "60", 1));
                Object obj5 = "vrrMaxValue";
                hashMap3.put("vrrMinValue", new TableInfo.Column("vrrMinValue", "INTEGER", true, 0, "48", 1));
                hashMap3.put("drrAllowed", new TableInfo.Column("drrAllowed", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("ipmMode", new TableInfo.Column("ipmMode", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("ipmTargetPower", new TableInfo.Column("ipmTargetPower", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("ipmTargetTemperature", new TableInfo.Column("ipmTargetTemperature", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("ipmUpdateTime", new TableInfo.Column("ipmUpdateTime", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("ipmTrainingVersion", new TableInfo.Column("ipmTrainingVersion", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("ipmCpuBottomFreq", new TableInfo.Column("ipmCpuBottomFreq", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("ipmFlag", new TableInfo.Column("ipmFlag", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("siopMode", new TableInfo.Column("siopMode", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("eachModeTargetShortSide", new TableInfo.Column("eachModeTargetShortSide", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("governorSettings", new TableInfo.Column("governorSettings", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("resumeBoostPolicy", new TableInfo.Column("resumeBoostPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("loggingPolicy", new TableInfo.Column("loggingPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("ipmPolicy", new TableInfo.Column("ipmPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("siopModePolicy", new TableInfo.Column("siopModePolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("launchBoostPolicy", new TableInfo.Column("launchBoostPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("wifiQosPolicy", new TableInfo.Column("wifiQosPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("gfiPolicy", new TableInfo.Column("gfiPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("tspPolicy", new TableInfo.Column("tspPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("ringlogPolicy", new TableInfo.Column("ringlogPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap3.put(GosInterface.KeyName.UUID, new TableInfo.Column(GosInterface.KeyName.UUID, "TEXT", false, 0, (String) null, 1));
                hashMap3.put("registeredDevice", new TableInfo.Column("registeredDevice", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("gmsVersion", new TableInfo.Column("gmsVersion", "REAL", true, 0, (String) null, 1));
                hashMap3.put("showLogcat", new TableInfo.Column("showLogcat", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("automaticUpdate", new TableInfo.Column("automaticUpdate", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("targetServer", new TableInfo.Column("targetServer", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("deviceName", new TableInfo.Column("deviceName", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("prevSiopModeByUser", new TableInfo.Column("prevSiopModeByUser", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("sosPolicyKeyCsv", new TableInfo.Column("sosPolicyKeyCsv", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("gosSelfUpdateStatus", new TableInfo.Column("gosSelfUpdateStatus", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("dataReady", new TableInfo.Column("dataReady", "INTEGER", true, 0, (String) null, 1));
                hashMap3.put("dmaId", new TableInfo.Column("dmaId", "TEXT", false, 0, (String) null, 1));
                hashMap3.put("clearBGLruNum", new TableInfo.Column("clearBGLruNum", "INTEGER", true, 0, "1", 1));
                hashMap3.put("clearBGSurviveAppFromServer", new TableInfo.Column("clearBGSurviveAppFromServer", "TEXT", false, 0, (String) null, 1));
                Object obj6 = "customDss";
                Object obj7 = "eachModeDss";
                TableInfo tableInfo3 = new TableInfo("Global", hashMap3, new HashSet(0), new HashSet(0));
                TableInfo read3 = TableInfo.read(supportSQLiteDatabase2, "Global");
                if (!tableInfo3.equals(read3)) {
                    return new RoomOpenHelper.ValidationResult(false, "Global(com.samsung.android.game.gos.data.model.Global).\n Expected:\n" + tableInfo3 + "\n Found:\n" + read3);
                }
                HashMap hashMap4 = new HashMap(9);
                hashMap4.put(ManagerInterface.KeyName.REQUEST_NAME, new TableInfo.Column(ManagerInterface.KeyName.REQUEST_NAME, "TEXT", true, 1, (String) null, 1));
                hashMap4.put(GosInterface.FeatureFlagKeyNames.AVAILABLE, new TableInfo.Column(GosInterface.FeatureFlagKeyNames.AVAILABLE, "INTEGER", true, 0, (String) null, 1));
                hashMap4.put("usingUserValue", new TableInfo.Column("usingUserValue", "INTEGER", true, 0, (String) null, 1));
                hashMap4.put("usingPkgValue", new TableInfo.Column("usingPkgValue", "INTEGER", true, 0, (String) null, 1));
                hashMap4.put("state", new TableInfo.Column("state", "TEXT", true, 0, (String) null, 1));
                hashMap4.put("inheritedFlag", new TableInfo.Column("inheritedFlag", "INTEGER", true, 0, (String) null, 1));
                hashMap4.put("forcedFlag", new TableInfo.Column("forcedFlag", "INTEGER", true, 0, (String) null, 1));
                hashMap4.put("enabledFlagByServer", new TableInfo.Column("enabledFlagByServer", "INTEGER", true, 0, (String) null, 1));
                hashMap4.put("enabledFlagByUser", new TableInfo.Column("enabledFlagByUser", "INTEGER", true, 0, (String) null, 1));
                TableInfo tableInfo4 = new TableInfo("GlobalFeatureFlag", hashMap4, new HashSet(0), new HashSet(0));
                TableInfo read4 = TableInfo.read(supportSQLiteDatabase2, "GlobalFeatureFlag");
                if (!tableInfo4.equals(read4)) {
                    return new RoomOpenHelper.ValidationResult(false, "GlobalFeatureFlag(com.samsung.android.game.gos.data.model.GlobalFeatureFlag).\n Expected:\n" + tableInfo4 + "\n Found:\n" + read4);
                }
                HashMap hashMap5 = new HashMap(5);
                hashMap5.put("id", new TableInfo.Column("id", "INTEGER", true, 1, (String) null, 1));
                hashMap5.put("timeStamp", new TableInfo.Column("timeStamp", "INTEGER", true, 0, (String) null, 1));
                hashMap5.put("time", new TableInfo.Column("time", "TEXT", false, 0, (String) null, 1));
                hashMap5.put(GosInterface.KeyName.TAG, new TableInfo.Column(GosInterface.KeyName.TAG, "TEXT", false, 0, (String) null, 1));
                hashMap5.put(NotificationCompat.CATEGORY_MESSAGE, new TableInfo.Column(NotificationCompat.CATEGORY_MESSAGE, "TEXT", false, 0, (String) null, 1));
                TableInfo tableInfo5 = new TableInfo("LocalLog", hashMap5, new HashSet(0), new HashSet(0));
                TableInfo read5 = TableInfo.read(supportSQLiteDatabase2, "LocalLog");
                if (!tableInfo5.equals(read5)) {
                    return new RoomOpenHelper.ValidationResult(false, "LocalLog(com.samsung.android.game.gos.data.model.LocalLog).\n Expected:\n" + tableInfo5 + "\n Found:\n" + read5);
                }
                HashMap hashMap6 = new HashMap(2);
                hashMap6.put(EventPublisher.EXTRA_KEY_PKG_NAME, new TableInfo.Column(EventPublisher.EXTRA_KEY_PKG_NAME, "TEXT", true, 1, (String) null, 1));
                hashMap6.put("subscriberPkgName", new TableInfo.Column("subscriberPkgName", "TEXT", true, 2, (String) null, 1));
                TableInfo tableInfo6 = new TableInfo("MonitoredApps", hashMap6, new HashSet(0), new HashSet(0));
                TableInfo read6 = TableInfo.read(supportSQLiteDatabase2, "MonitoredApps");
                if (!tableInfo6.equals(read6)) {
                    return new RoomOpenHelper.ValidationResult(false, "MonitoredApps(com.samsung.android.game.gos.data.model.MonitoredApps).\n Expected:\n" + tableInfo6 + "\n Found:\n" + read6);
                }
                HashMap hashMap7 = new HashMap(37);
                hashMap7.put(EventPublisher.EXTRA_KEY_PKG_NAME, new TableInfo.Column(EventPublisher.EXTRA_KEY_PKG_NAME, "TEXT", true, 1, (String) null, 1));
                hashMap7.put("categoryCode", new TableInfo.Column("categoryCode", "TEXT", true, 0, "'undefined'", 1));
                hashMap7.put("installedUserIds", new TableInfo.Column("installedUserIds", "TEXT", true, 0, "''", 1));
                hashMap7.put("defaultSetBy", new TableInfo.Column("defaultSetBy", "INTEGER", true, 0, "0", 1));
                hashMap7.put("pkgAddedTime", new TableInfo.Column("pkgAddedTime", "INTEGER", true, 0, "0", 1));
                hashMap7.put("serverPkgUpdatedTime", new TableInfo.Column("serverPkgUpdatedTime", "INTEGER", true, 0, "0", 1));
                hashMap7.put("eachModeTargetShortSide", new TableInfo.Column("eachModeTargetShortSide", "TEXT", false, 0, "'-1,-1,-1,-1'", 1));
                hashMap7.put(obj7, new TableInfo.Column("eachModeDss", "TEXT", false, 0, "'-1,-1,-1,-1'", 1));
                hashMap7.put("customResolutionMode", new TableInfo.Column("customResolutionMode", "INTEGER", true, 0, "1", 1));
                hashMap7.put(obj6, new TableInfo.Column("customDss", "REAL", true, 0, "75", 1));
                hashMap7.put(obj3, new TableInfo.Column("eachModeDfs", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("customDfsMode", new TableInfo.Column("customDfsMode", "INTEGER", true, 0, "1", 1));
                hashMap7.put(obj4, new TableInfo.Column("customDfs", "REAL", true, 0, "60", 1));
                hashMap7.put(obj2, new TableInfo.Column("defaultCpuLevel", "INTEGER", true, 0, (String) null, 1));
                hashMap7.put(obj, new TableInfo.Column("defaultGpuLevel", "INTEGER", true, 0, (String) null, 1));
                hashMap7.put("governorSettings", new TableInfo.Column("governorSettings", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("shiftTemperature", new TableInfo.Column("shiftTemperature", "INTEGER", true, 0, "-1000", 1));
                hashMap7.put("ipmPolicy", new TableInfo.Column("ipmPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("siopModePolicy", new TableInfo.Column("siopModePolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("customSiopMode", new TableInfo.Column("customSiopMode", "INTEGER", true, 0, "1", 1));
                hashMap7.put("gameSdkSettings", new TableInfo.Column("gameSdkSettings", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("sosPolicy", new TableInfo.Column("sosPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("resumeBoostPolicy", new TableInfo.Column("resumeBoostPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("launchBoostPolicy", new TableInfo.Column("launchBoostPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("wifiQosPolicy", new TableInfo.Column("wifiQosPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("subscriberList", new TableInfo.Column("subscriberList", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("gfiPolicy", new TableInfo.Column("gfiPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("tspPolicy", new TableInfo.Column("tspPolicy", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("appliedDss", new TableInfo.Column("appliedDss", "REAL", true, 0, (String) null, 1));
                hashMap7.put("appliedCpuLevel", new TableInfo.Column("appliedCpuLevel", "INTEGER", true, 0, (String) null, 1));
                hashMap7.put("appliedGpuLevel", new TableInfo.Column("appliedGpuLevel", "INTEGER", true, 0, (String) null, 1));
                hashMap7.put("versionName", new TableInfo.Column("versionName", "TEXT", false, 0, (String) null, 1));
                hashMap7.put("versionCode", new TableInfo.Column("versionCode", "INTEGER", true, 0, "-1", 1));
                hashMap7.put(obj5, new TableInfo.Column("vrrMaxValue", "INTEGER", true, 0, "-1", 1));
                hashMap7.put("vrrMinValue", new TableInfo.Column("vrrMinValue", "INTEGER", true, 0, "-1", 1));
                hashMap7.put("drrAllowed", new TableInfo.Column("drrAllowed", "INTEGER", true, 0, (String) null, 1));
                hashMap7.put("targetGroupName", new TableInfo.Column("targetGroupName", "TEXT", false, 0, (String) null, 1));
                TableInfo tableInfo7 = new TableInfo("Package", hashMap7, new HashSet(0), new HashSet(0));
                TableInfo read7 = TableInfo.read(supportSQLiteDatabase2, "Package");
                if (!tableInfo7.equals(read7)) {
                    return new RoomOpenHelper.ValidationResult(false, "Package(com.samsung.android.game.gos.data.model.Package).\n Expected:\n" + tableInfo7 + "\n Found:\n" + read7);
                }
                HashMap hashMap8 = new HashMap(5);
                hashMap8.put(EventPublisher.EXTRA_KEY_PKG_NAME, new TableInfo.Column(EventPublisher.EXTRA_KEY_PKG_NAME, "TEXT", true, 1, (String) null, 1));
                hashMap8.put("permType", new TableInfo.Column("permType", "INTEGER", true, 0, (String) null, 1));
                hashMap8.put("paramListCsv", new TableInfo.Column("paramListCsv", "TEXT", false, 0, (String) null, 1));
                hashMap8.put("permPolicy", new TableInfo.Column("permPolicy", "INTEGER", true, 0, (String) null, 1));
                hashMap8.put("lastHandshakeTime", new TableInfo.Column("lastHandshakeTime", "TEXT", false, 0, (String) null, 1));
                TableInfo tableInfo8 = new TableInfo("PerfDataPermission", hashMap8, new HashSet(0), new HashSet(0));
                TableInfo read8 = TableInfo.read(supportSQLiteDatabase2, "PerfDataPermission");
                if (!tableInfo8.equals(read8)) {
                    return new RoomOpenHelper.ValidationResult(false, "PerfDataPermission(com.samsung.android.game.gos.data.model.PerfDataPermission).\n Expected:\n" + tableInfo8 + "\n Found:\n" + read8);
                }
                HashMap hashMap9 = new HashMap(2);
                hashMap9.put(EventPublisher.EXTRA_KEY_PKG_NAME, new TableInfo.Column(EventPublisher.EXTRA_KEY_PKG_NAME, "TEXT", true, 1, (String) null, 1));
                hashMap9.put("featureName", new TableInfo.Column("featureName", "TEXT", true, 2, (String) null, 1));
                TableInfo tableInfo9 = new TableInfo("SettingsAccessiblePackage", hashMap9, new HashSet(0), new HashSet(0));
                TableInfo read9 = TableInfo.read(supportSQLiteDatabase2, "SettingsAccessiblePackage");
                if (!tableInfo9.equals(read9)) {
                    return new RoomOpenHelper.ValidationResult(false, "SettingsAccessiblePackage(com.samsung.android.game.gos.data.model.SettingsAccessiblePackage).\n Expected:\n" + tableInfo9 + "\n Found:\n" + read9);
                }
                HashMap hashMap10 = new HashMap(1);
                hashMap10.put(EventPublisher.EXTRA_KEY_PKG_NAME, new TableInfo.Column(EventPublisher.EXTRA_KEY_PKG_NAME, "TEXT", true, 1, (String) null, 1));
                TableInfo tableInfo10 = new TableInfo("CategoryUpdateReserved", hashMap10, new HashSet(0), new HashSet(0));
                TableInfo read10 = TableInfo.read(supportSQLiteDatabase2, "CategoryUpdateReserved");
                if (!tableInfo10.equals(read10)) {
                    return new RoomOpenHelper.ValidationResult(false, "CategoryUpdateReserved(com.samsung.android.game.gos.data.model.CategoryUpdateReserved).\n Expected:\n" + tableInfo10 + "\n Found:\n" + read10);
                }
                HashMap hashMap11 = new HashMap(2);
                hashMap11.put("command", new TableInfo.Column("command", "TEXT", true, 1, (String) null, 1));
                hashMap11.put("callerPkgName", new TableInfo.Column("callerPkgName", "TEXT", true, 2, (String) null, 1));
                TableInfo tableInfo11 = new TableInfo("GosServiceUsage", hashMap11, new HashSet(0), new HashSet(0));
                TableInfo read11 = TableInfo.read(supportSQLiteDatabase2, "GosServiceUsage");
                if (!tableInfo11.equals(read11)) {
                    return new RoomOpenHelper.ValidationResult(false, "GosServiceUsage(com.samsung.android.game.gos.data.model.GosServiceUsage).\n Expected:\n" + tableInfo11 + "\n Found:\n" + read11);
                }
                HashMap hashMap12 = new HashMap(2);
                hashMap12.put("callerPkgName", new TableInfo.Column("callerPkgName", "TEXT", true, 1, (String) null, 1));
                hashMap12.put("surviveAppPkgName", new TableInfo.Column("surviveAppPkgName", "TEXT", true, 2, (String) null, 1));
                TableInfo tableInfo12 = new TableInfo("ClearBGSurviveApps", hashMap12, new HashSet(0), new HashSet(0));
                TableInfo read12 = TableInfo.read(supportSQLiteDatabase2, "ClearBGSurviveApps");
                if (tableInfo12.equals(read12)) {
                    return new RoomOpenHelper.ValidationResult(true, (String) null);
                }
                return new RoomOpenHelper.ValidationResult(false, "ClearBGSurviveApps(com.samsung.android.game.gos.data.model.ClearBGSurviveApps).\n Expected:\n" + tableInfo12 + "\n Found:\n" + read12);
            }
        }, "20f2e28c4ed036def34550df26f03354", "2b0cad8a9b94f58657b3b37b0f773dbf")).build());
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "EventSubscription", "FeatureFlag", "Global", "GlobalFeatureFlag", "LocalLog", "MonitoredApps", "Package", "PerfDataPermission", "SettingsAccessiblePackage", "CategoryUpdateReserved", "GosServiceUsage", "ClearBGSurviveApps");
    }

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `EventSubscription`");
            writableDatabase.execSQL("DELETE FROM `FeatureFlag`");
            writableDatabase.execSQL("DELETE FROM `Global`");
            writableDatabase.execSQL("DELETE FROM `GlobalFeatureFlag`");
            writableDatabase.execSQL("DELETE FROM `LocalLog`");
            writableDatabase.execSQL("DELETE FROM `MonitoredApps`");
            writableDatabase.execSQL("DELETE FROM `Package`");
            writableDatabase.execSQL("DELETE FROM `PerfDataPermission`");
            writableDatabase.execSQL("DELETE FROM `SettingsAccessiblePackage`");
            writableDatabase.execSQL("DELETE FROM `CategoryUpdateReserved`");
            writableDatabase.execSQL("DELETE FROM `GosServiceUsage`");
            writableDatabase.execSQL("DELETE FROM `ClearBGSurviveApps`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    public EventSubscriptionDao eventSubscriptionDao() {
        EventSubscriptionDao eventSubscriptionDao;
        if (this._eventSubscriptionDao != null) {
            return this._eventSubscriptionDao;
        }
        synchronized (this) {
            if (this._eventSubscriptionDao == null) {
                this._eventSubscriptionDao = new EventSubscriptionDao_Impl(this);
            }
            eventSubscriptionDao = this._eventSubscriptionDao;
        }
        return eventSubscriptionDao;
    }

    public FeatureFlagDao featureFlagDao() {
        FeatureFlagDao featureFlagDao;
        if (this._featureFlagDao != null) {
            return this._featureFlagDao;
        }
        synchronized (this) {
            if (this._featureFlagDao == null) {
                this._featureFlagDao = new FeatureFlagDao_Impl(this);
            }
            featureFlagDao = this._featureFlagDao;
        }
        return featureFlagDao;
    }

    public GlobalDao globalDao() {
        GlobalDao globalDao;
        if (this._globalDao != null) {
            return this._globalDao;
        }
        synchronized (this) {
            if (this._globalDao == null) {
                this._globalDao = new GlobalDao_Impl(this);
            }
            globalDao = this._globalDao;
        }
        return globalDao;
    }

    public GlobalFeatureFlagDao globalFeatureFlagDao() {
        GlobalFeatureFlagDao globalFeatureFlagDao;
        if (this._globalFeatureFlagDao != null) {
            return this._globalFeatureFlagDao;
        }
        synchronized (this) {
            if (this._globalFeatureFlagDao == null) {
                this._globalFeatureFlagDao = new GlobalFeatureFlagDao_Impl(this);
            }
            globalFeatureFlagDao = this._globalFeatureFlagDao;
        }
        return globalFeatureFlagDao;
    }

    public LocalLogDao localLogDao() {
        LocalLogDao localLogDao;
        if (this._localLogDao != null) {
            return this._localLogDao;
        }
        synchronized (this) {
            if (this._localLogDao == null) {
                this._localLogDao = new LocalLogDao_Impl(this);
            }
            localLogDao = this._localLogDao;
        }
        return localLogDao;
    }

    public MonitoredAppsDao monitoredAppsDao() {
        MonitoredAppsDao monitoredAppsDao;
        if (this._monitoredAppsDao != null) {
            return this._monitoredAppsDao;
        }
        synchronized (this) {
            if (this._monitoredAppsDao == null) {
                this._monitoredAppsDao = new MonitoredAppsDao_Impl(this);
            }
            monitoredAppsDao = this._monitoredAppsDao;
        }
        return monitoredAppsDao;
    }

    public PackageDao packageDao() {
        PackageDao packageDao;
        if (this._packageDao != null) {
            return this._packageDao;
        }
        synchronized (this) {
            if (this._packageDao == null) {
                this._packageDao = new PackageDao_Impl(this);
            }
            packageDao = this._packageDao;
        }
        return packageDao;
    }

    public PerfDataPermissionDao perfDataPermissionDao() {
        PerfDataPermissionDao perfDataPermissionDao;
        if (this._perfDataPermissionDao != null) {
            return this._perfDataPermissionDao;
        }
        synchronized (this) {
            if (this._perfDataPermissionDao == null) {
                this._perfDataPermissionDao = new PerfDataPermissionDao_Impl(this);
            }
            perfDataPermissionDao = this._perfDataPermissionDao;
        }
        return perfDataPermissionDao;
    }

    public SettingsAccessiblePackageDao settingsAccessiblePackageDao() {
        SettingsAccessiblePackageDao settingsAccessiblePackageDao;
        if (this._settingsAccessiblePackageDao != null) {
            return this._settingsAccessiblePackageDao;
        }
        synchronized (this) {
            if (this._settingsAccessiblePackageDao == null) {
                this._settingsAccessiblePackageDao = new SettingsAccessiblePackageDao_Impl(this);
            }
            settingsAccessiblePackageDao = this._settingsAccessiblePackageDao;
        }
        return settingsAccessiblePackageDao;
    }

    public CategoryUpdateReservedDao categoryUpdateReservedDao() {
        CategoryUpdateReservedDao categoryUpdateReservedDao;
        if (this._categoryUpdateReservedDao != null) {
            return this._categoryUpdateReservedDao;
        }
        synchronized (this) {
            if (this._categoryUpdateReservedDao == null) {
                this._categoryUpdateReservedDao = new CategoryUpdateReservedDao_Impl(this);
            }
            categoryUpdateReservedDao = this._categoryUpdateReservedDao;
        }
        return categoryUpdateReservedDao;
    }

    public GosServiceUsageDao gosServiceUsageDao() {
        GosServiceUsageDao gosServiceUsageDao;
        if (this._gosServiceUsageDao != null) {
            return this._gosServiceUsageDao;
        }
        synchronized (this) {
            if (this._gosServiceUsageDao == null) {
                this._gosServiceUsageDao = new GosServiceUsageDao_Impl(this);
            }
            gosServiceUsageDao = this._gosServiceUsageDao;
        }
        return gosServiceUsageDao;
    }

    public ClearBGSurviveAppsDao clearBGSurviveAppsDao() {
        ClearBGSurviveAppsDao clearBGSurviveAppsDao;
        if (this._clearBGSurviveAppsDao != null) {
            return this._clearBGSurviveAppsDao;
        }
        synchronized (this) {
            if (this._clearBGSurviveAppsDao == null) {
                this._clearBGSurviveAppsDao = new ClearBGSurviveAppsDao_Impl(this);
            }
            clearBGSurviveAppsDao = this._clearBGSurviveAppsDao;
        }
        return clearBGSurviveAppsDao;
    }
}
