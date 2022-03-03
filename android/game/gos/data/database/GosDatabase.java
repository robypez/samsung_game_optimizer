package com.samsung.android.game.gos.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.samsung.android.game.gos.controller.EventPublisher;
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
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import java.util.Set;

public abstract class GosDatabase extends RoomDatabase {
    public static final String GOS_DATABASE_FILE_DB = "gos-db.db";
    public static final String GOS_DATABASE_FILE_DB_SHM = "gos-db.db-shm";
    public static final String GOS_DATABASE_FILE_DB_WAL = "gos-db.db-wal";
    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `new_SettingsAccessiblePackage` (`pkgName` TEXT NOT NULL, `featureName` TEXT NOT NULL, PRIMARY KEY(`pkgName`, `featureName`))");
            Cursor query = supportSQLiteDatabase.query("SELECT * FROM SettingsAccessiblePackage");
            ContentValues contentValues = new ContentValues();
            if (query != null) {
                while (query.moveToNext()) {
                    String string = query.getString(0);
                    String string2 = query.getString(1);
                    String[] split = string2 != null ? string2.split(",") : null;
                    if (split != null) {
                        for (String str : split) {
                            if (Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES.contains(str)) {
                                contentValues.put(EventPublisher.EXTRA_KEY_PKG_NAME, string);
                                contentValues.put("featureName", str);
                                supportSQLiteDatabase.insert("new_SettingsAccessiblePackage", 5, contentValues);
                            }
                        }
                    }
                }
            }
            supportSQLiteDatabase.execSQL("DROP TABLE SettingsAccessiblePackage");
            supportSQLiteDatabase.execSQL("ALTER TABLE new_SettingsAccessiblePackage RENAME TO SettingsAccessiblePackage");
        }
    };
    private static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE Global ADD COLUMN clearBGLruNum INTEGER NOT NULL DEFAULT 1");
            supportSQLiteDatabase.execSQL("ALTER TABLE Global ADD COLUMN clearBGSurviceApp TEXT");
        }
    };
    private static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE Global RENAME COLUMN clearBGSurviceApp TO clearBGSurviveAppFromServer");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `ClearBGSurviveApps` (`callerPkgName` TEXT NOT NULL, `surviveAppPkgName` TEXT NOT NULL, PRIMARY KEY(`callerPkgName`, `surviveAppPkgName`))");
            Cursor query = supportSQLiteDatabase.query("SELECT clearBGSurviveAppFromServer FROM Global WHERE id = 1");
            if (query != null && query.getCount() >= 1) {
                query.moveToFirst();
                String string = query.getString(0);
                query.close();
                Set<String> csvToStringSet = TypeConverter.csvToStringSet(string);
                if (csvToStringSet != null) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("callerPkgName", Constants.PACKAGE_NAME_GAME_PLUGINS);
                    supportSQLiteDatabase.beginTransaction();
                    try {
                        supportSQLiteDatabase.execSQL("UPDATE Global SET clearBGSurviveAppFromServer = '' WHERE id = 1");
                        for (String next : csvToStringSet) {
                            if (next != null && next.length() > 0) {
                                contentValues.put("surviveAppPkgName", next);
                                supportSQLiteDatabase.insert("ClearBGSurviveApps", 5, contentValues);
                            }
                        }
                        supportSQLiteDatabase.setTransactionSuccessful();
                    } catch (SQLException unused) {
                    } catch (Throwable th) {
                        supportSQLiteDatabase.endTransaction();
                        throw th;
                    }
                    supportSQLiteDatabase.endTransaction();
                }
            }
        }
    };
    private static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE Package ADD COLUMN installedUserIds TEXT NOT NULL DEFAULT ''");
            supportSQLiteDatabase.beginTransaction();
            try {
                supportSQLiteDatabase.execSQL("UPDATE Package SET installedUserIds = '0'");
                supportSQLiteDatabase.setTransactionSuccessful();
            } catch (SQLException unused) {
            } catch (Throwable th) {
                supportSQLiteDatabase.endTransaction();
                throw th;
            }
            supportSQLiteDatabase.endTransaction();
        }
    };
    private static final Migration MIGRATION_14_15 = new Migration(14, 15) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE Global ADD COLUMN drrAllowed INTEGER NOT NULL DEFAULT -1");
            supportSQLiteDatabase.execSQL("ALTER TABLE Package ADD COLUMN drrAllowed INTEGER NOT NULL DEFAULT -1 ");
        }
    };
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE new_Package (pkgName TEXT PRIMARY KEY NOT NULL,categoryCode TEXT NOT NULL DEFAULT 'undefined',serverCategory TEXT NOT NULL DEFAULT 'undefined',categorizedBy INTEGER NOT NULL DEFAULT 0,defaultSetBy INTEGER NOT NULL DEFAULT 0,pkgAddedTime INTEGER NOT NULL DEFAULT 0,serverPkgUpdatedTime INTEGER NOT NULL DEFAULT 0,eachModeTargetShortSide TEXT DEFAULT '-1,-1,-1,-1',eachModeDss TEXT DEFAULT '-1,-1,-1,-1',customResolutionMode INTEGER NOT NULL DEFAULT 1,customDss REAL NOT NULL DEFAULT 75,eachModeDfs TEXT DEFAULT '-1f,-1f,-1f,-1f',customDfsMode INTEGER NOT NULL DEFAULT 1,customDfs REAL NOT NULL DEFAULT 60,defaultCpuLevel INTEGER NOT NULL,defaultGpuLevel INTEGER NOT NULL,governorSettings TEXT,shiftTemperature INTEGER NOT NULL DEFAULT -1000,ipmPolicy TEXT,siopModePolicy TEXT,customSiopMode INTEGER NOT NULL DEFAULT 1,gameSdkSettings TEXT,sosPolicy TEXT,resumeBoostPolicy TEXT,launchBoostPolicy TEXT,wifiQosPolicy TEXT,aspectRatioValue TEXT,subscriberList TEXT,gfiPolicy TEXT,tspPolicy TEXT,appliedDss REAL NOT NULL,appliedCpuLevel INTEGER NOT NULL,appliedGpuLevel INTEGER NOT NULL,versionName TEXT,versionCode INTEGER NOT NULL DEFAULT -1,vrrMaxValue INTEGER NOT NULL DEFAULT 60,vrrMinValue INTEGER NOT NULL DEFAULT 48)");
            supportSQLiteDatabase.execSQL("INSERT INTO new_Package (pkgName, categoryCode, serverCategory, categorizedBy, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, aspectRatioValue, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue) SELECT pkgName, categoryCode, serverCategory, categorizedBy, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, aspectRatioValue, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue FROM Package");
            supportSQLiteDatabase.execSQL("DROP TABLE Package");
            supportSQLiteDatabase.execSQL("ALTER TABLE new_Package RENAME TO Package");
        }
    };
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE EventSubscription ADD COLUMN flags INTEGER NOT NULL DEFAULT 0");
        }
    };
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE EventSubscription ADD COLUMN receiver_type TEXT DEFAULT 'b'");
        }
    };
    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS CategoryUpdateReserved (pkgName TEXT PRIMARY KEY NOT NULL)");
        }
    };
    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS GosServiceUsage (command TEXT NOT NULL,callerPkgName TEXT NOT NULL,PRIMARY KEY(command, callerPkgName))");
        }
    };
    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS new_Global (`id` INTEGER NOT NULL, `initialized` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL, `fullyUpdateTime` INTEGER NOT NULL, `defaultCpuLevel` INTEGER NOT NULL, `defaultGpuLevel` INTEGER NOT NULL, `eachModeDss` TEXT, `eachModeDfs` TEXT, `resolutionMode` INTEGER NOT NULL, `customDss` REAL NOT NULL, `dfsMode` INTEGER NOT NULL, `customDfs` REAL NOT NULL, `ipmMode` INTEGER NOT NULL, `ipmTargetPower` INTEGER NOT NULL, `ipmTargetTemperature` INTEGER NOT NULL, `ipmUpdateTime` INTEGER NOT NULL, `ipmTrainingVersion` TEXT, `ipmCpuBottomFreq` INTEGER NOT NULL, `ipmFlag` TEXT, `siopMode` INTEGER NOT NULL, `eachModeTargetShortSide` TEXT, `governorSettings` TEXT, `resumeBoostPolicy` TEXT, `loggingPolicy` TEXT, `ipmPolicy` TEXT, `siopModePolicy` TEXT, `launchBoostPolicy` TEXT, `wifiQosPolicy` TEXT, `gfiPolicy` TEXT, `tspPolicy` TEXT, `uuid` TEXT, `registeredDevice` INTEGER NOT NULL, `gmsVersion` REAL NOT NULL, `showLogcat` INTEGER NOT NULL, `automaticUpdate` INTEGER NOT NULL, `targetServer` INTEGER NOT NULL, `deviceName` TEXT, `prevSiopModeByUser` INTEGER NOT NULL, `sosPolicyKeyCsv` TEXT, `gosSelfUpdateStatus` INTEGER NOT NULL, `dataReady` INTEGER NOT NULL, `dmaId` TEXT, PRIMARY KEY(`id`))");
            supportSQLiteDatabase.execSQL("INSERT INTO new_Global (`id`, `initialized`, `updateTime`, `fullyUpdateTime`, `defaultCpuLevel`, `defaultGpuLevel`, `eachModeDss`, `eachModeDfs`, `resolutionMode`, `customDss`, `dfsMode`, `customDfs`, `ipmMode`, `ipmTargetPower`, `ipmTargetTemperature`, `ipmUpdateTime`, `ipmTrainingVersion`, `ipmCpuBottomFreq`, `ipmFlag`, `siopMode`, `eachModeTargetShortSide`, `governorSettings`, `resumeBoostPolicy`, `loggingPolicy`, `ipmPolicy`, `siopModePolicy`, `launchBoostPolicy`, `wifiQosPolicy`, `gfiPolicy`, `tspPolicy`, `uuid`, `registeredDevice`, `gmsVersion`, `showLogcat`, `automaticUpdate`, `targetServer`, `deviceName`, `prevSiopModeByUser`, `sosPolicyKeyCsv`, `gosSelfUpdateStatus`, `dataReady`, `dmaId`) SELECT `id`, `initialized`, `updateTime`, `fullyUpdateTime`, `defaultCpuLevel`, `defaultGpuLevel`, `eachModeDss`, `eachModeDfs`, `resolutionMode`, `customDss`, `dfsMode`, `customDfs`, `ipmMode`, `ipmTargetPower`, `ipmTargetTemperature`, `ipmUpdateTime`, `ipmTrainingVersion`, `ipmCpuBottomFreq`, `ipmFlag`, `siopMode`, `eachModeTargetShortSide`, `governorSettings`, `resumeBoostPolicy`, `loggingPolicy`, `ipmPolicy`, `siopModePolicy`, `launchBoostPolicy`, `wifiQosPolicy`, `gfiPolicy`, `tspPolicy`, `uuid`, `registeredDevice`, `gmsVersion`, `showLogcat`, `automaticUpdate`, `targetServer`, `deviceName`, `prevSiopModeByUser`, `sosPolicyKeyCsv`, `gosSelfUpdateStatus`, `dataReady`, `dmaId` FROM Global");
            supportSQLiteDatabase.execSQL("DROP TABLE Global");
            supportSQLiteDatabase.execSQL("ALTER TABLE new_Global RENAME TO Global");
            supportSQLiteDatabase.execSQL("CREATE TABLE new_Package (pkgName TEXT PRIMARY KEY NOT NULL,categoryCode TEXT NOT NULL DEFAULT 'undefined',serverCategory TEXT NOT NULL DEFAULT 'undefined',categorizedBy INTEGER NOT NULL DEFAULT 0,defaultSetBy INTEGER NOT NULL DEFAULT 0,pkgAddedTime INTEGER NOT NULL DEFAULT 0,serverPkgUpdatedTime INTEGER NOT NULL DEFAULT 0,eachModeTargetShortSide TEXT DEFAULT '-1,-1,-1,-1',eachModeDss TEXT DEFAULT '-1,-1,-1,-1',customResolutionMode INTEGER NOT NULL DEFAULT 1,customDss REAL NOT NULL DEFAULT 75,eachModeDfs TEXT DEFAULT '-1f,-1f,-1f,-1f',customDfsMode INTEGER NOT NULL DEFAULT 1,customDfs REAL NOT NULL DEFAULT 60,defaultCpuLevel INTEGER NOT NULL,defaultGpuLevel INTEGER NOT NULL,governorSettings TEXT,shiftTemperature INTEGER NOT NULL DEFAULT -1000,ipmPolicy TEXT,siopModePolicy TEXT,customSiopMode INTEGER NOT NULL DEFAULT 1,gameSdkSettings TEXT,sosPolicy TEXT,resumeBoostPolicy TEXT,launchBoostPolicy TEXT,wifiQosPolicy TEXT,subscriberList TEXT,gfiPolicy TEXT,tspPolicy TEXT,appliedDss REAL NOT NULL,appliedCpuLevel INTEGER NOT NULL,appliedGpuLevel INTEGER NOT NULL,versionName TEXT,versionCode INTEGER NOT NULL DEFAULT -1,vrrMaxValue INTEGER NOT NULL DEFAULT 60,vrrMinValue INTEGER NOT NULL DEFAULT 48)");
            supportSQLiteDatabase.execSQL("INSERT INTO new_Package (pkgName, categoryCode, serverCategory, categorizedBy, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue) SELECT pkgName, categoryCode, serverCategory, categorizedBy, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue FROM Package");
            supportSQLiteDatabase.execSQL("DROP TABLE Package");
            supportSQLiteDatabase.execSQL("ALTER TABLE new_Package RENAME TO Package");
        }
    };
    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE Global ADD COLUMN ringlogPolicy TEXT");
        }
    };
    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("ALTER TABLE Global ADD COLUMN `vrrMaxValue` INTEGER NOT NULL DEFAULT 60");
            supportSQLiteDatabase.execSQL("ALTER TABLE Global ADD COLUMN `vrrMinValue` INTEGER NOT NULL DEFAULT 48");
            supportSQLiteDatabase.execSQL("UPDATE Global SET vrrMaxValue = ? WHERE id = 1", new Object[]{Integer.valueOf(Constants.VrrValues.getVrrMaxDefault())});
            supportSQLiteDatabase.execSQL("CREATE TABLE new_Package (pkgName TEXT PRIMARY KEY NOT NULL,categoryCode TEXT NOT NULL DEFAULT 'undefined',serverCategory TEXT NOT NULL DEFAULT 'undefined',categorizedBy INTEGER NOT NULL DEFAULT 0,defaultSetBy INTEGER NOT NULL DEFAULT 0,pkgAddedTime INTEGER NOT NULL DEFAULT 0,serverPkgUpdatedTime INTEGER NOT NULL DEFAULT 0,eachModeTargetShortSide TEXT DEFAULT '-1,-1,-1,-1',eachModeDss TEXT DEFAULT '-1,-1,-1,-1',customResolutionMode INTEGER NOT NULL DEFAULT 1,customDss REAL NOT NULL DEFAULT 75,eachModeDfs TEXT DEFAULT '-1f,-1f,-1f,-1f',customDfsMode INTEGER NOT NULL DEFAULT 1,customDfs REAL NOT NULL DEFAULT 60,defaultCpuLevel INTEGER NOT NULL,defaultGpuLevel INTEGER NOT NULL,governorSettings TEXT,shiftTemperature INTEGER NOT NULL DEFAULT -1000,ipmPolicy TEXT,siopModePolicy TEXT,customSiopMode INTEGER NOT NULL DEFAULT 1,gameSdkSettings TEXT,sosPolicy TEXT,resumeBoostPolicy TEXT,launchBoostPolicy TEXT,wifiQosPolicy TEXT,subscriberList TEXT,gfiPolicy TEXT,tspPolicy TEXT,appliedDss REAL NOT NULL,appliedCpuLevel INTEGER NOT NULL,appliedGpuLevel INTEGER NOT NULL,versionName TEXT,versionCode INTEGER NOT NULL DEFAULT -1,vrrMaxValue INTEGER NOT NULL DEFAULT -1,vrrMinValue INTEGER NOT NULL DEFAULT -1)");
            supportSQLiteDatabase.execSQL("INSERT INTO new_Package (pkgName, categoryCode, serverCategory, categorizedBy, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue) SELECT pkgName, categoryCode, serverCategory, categorizedBy, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue FROM Package");
            supportSQLiteDatabase.execSQL("DROP TABLE Package");
            supportSQLiteDatabase.execSQL("ALTER TABLE new_Package RENAME TO Package");
        }
    };
    private static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE new_Package (pkgName TEXT PRIMARY KEY NOT NULL,categoryCode TEXT NOT NULL DEFAULT 'undefined',defaultSetBy INTEGER NOT NULL DEFAULT 0,pkgAddedTime INTEGER NOT NULL DEFAULT 0,serverPkgUpdatedTime INTEGER NOT NULL DEFAULT 0,eachModeTargetShortSide TEXT DEFAULT '-1,-1,-1,-1',eachModeDss TEXT DEFAULT '-1,-1,-1,-1',customResolutionMode INTEGER NOT NULL DEFAULT 1,customDss REAL NOT NULL DEFAULT 75,eachModeDfs TEXT DEFAULT '-1f,-1f,-1f,-1f',customDfsMode INTEGER NOT NULL DEFAULT 1,customDfs REAL NOT NULL DEFAULT 60,defaultCpuLevel INTEGER NOT NULL,defaultGpuLevel INTEGER NOT NULL,governorSettings TEXT,shiftTemperature INTEGER NOT NULL DEFAULT -1000,ipmPolicy TEXT,siopModePolicy TEXT,customSiopMode INTEGER NOT NULL DEFAULT 1,gameSdkSettings TEXT,sosPolicy TEXT,resumeBoostPolicy TEXT,launchBoostPolicy TEXT,wifiQosPolicy TEXT,subscriberList TEXT,gfiPolicy TEXT,tspPolicy TEXT,appliedDss REAL NOT NULL,appliedCpuLevel INTEGER NOT NULL,appliedGpuLevel INTEGER NOT NULL,versionName TEXT,versionCode INTEGER NOT NULL DEFAULT -1,vrrMaxValue INTEGER NOT NULL DEFAULT -1,vrrMinValue INTEGER NOT NULL DEFAULT -1,targetGroupName TEXT)");
            supportSQLiteDatabase.execSQL("INSERT INTO new_Package (pkgName, categoryCode, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue) SELECT pkgName, categoryCode, defaultSetBy, pkgAddedTime, serverPkgUpdatedTime, eachModeTargetShortSide, eachModeDss, customResolutionMode, customDss, eachModeDfs, customDfsMode, customDfs, defaultCpuLevel, defaultGpuLevel, governorSettings, shiftTemperature, ipmPolicy, siopModePolicy, customSiopMode, gameSdkSettings, sosPolicy, resumeBoostPolicy, launchBoostPolicy, wifiQosPolicy, subscriberList, gfiPolicy, tspPolicy, appliedDss, appliedCpuLevel, appliedGpuLevel, versionName, versionCode, vrrMaxValue, vrrMinValue FROM Package");
            supportSQLiteDatabase.execSQL("DROP TABLE Package");
            supportSQLiteDatabase.execSQL("ALTER TABLE new_Package RENAME TO Package");
        }
    };

    public abstract CategoryUpdateReservedDao categoryUpdateReservedDao();

    public abstract ClearBGSurviveAppsDao clearBGSurviveAppsDao();

    public abstract EventSubscriptionDao eventSubscriptionDao();

    public abstract FeatureFlagDao featureFlagDao();

    public abstract GlobalDao globalDao();

    public abstract GlobalFeatureFlagDao globalFeatureFlagDao();

    public abstract GosServiceUsageDao gosServiceUsageDao();

    public abstract LocalLogDao localLogDao();

    public abstract MonitoredAppsDao monitoredAppsDao();

    public abstract PackageDao packageDao();

    public abstract PerfDataPermissionDao perfDataPermissionDao();

    public abstract SettingsAccessiblePackageDao settingsAccessiblePackageDao();

    public static GosDatabase build(Context context) {
        return Room.databaseBuilder(context, GosDatabase.class, GOS_DATABASE_FILE_DB).allowMainThreadQueries().addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12, MIGRATION_12_13, MIGRATION_13_14, MIGRATION_14_15).fallbackToDestructiveMigrationOnDowngrade().build();
    }
}
