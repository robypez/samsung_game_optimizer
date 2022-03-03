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
import com.samsung.android.game.gos.data.dao.ReportDao;
import com.samsung.android.game.gos.data.dao.ReportDao_Impl;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.HashMap;
import java.util.HashSet;

public final class ReportDatabase_Impl extends ReportDatabase {
    private volatile ReportDao _reportDao;

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new RoomOpenHelper.Delegate(2) {
            public void onPostMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            }

            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Report` (`id` INTEGER NOT NULL, `tag` TEXT, `msg` TEXT, `ringlogMsg` TEXT, `gppRepAggregation` TEXT, `gppRepDataSchemeVersion` TEXT, `byteSize` INTEGER NOT NULL, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '11b0e8049606344af06380929aecca0b')");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `Report`");
                if (ReportDatabase_Impl.this.mCallbacks != null) {
                    int size = ReportDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) ReportDatabase_Impl.this.mCallbacks.get(i)).onDestructiveMigration(supportSQLiteDatabase);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (ReportDatabase_Impl.this.mCallbacks != null) {
                    int size = ReportDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) ReportDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase unused = ReportDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                ReportDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (ReportDatabase_Impl.this.mCallbacks != null) {
                    int size = ReportDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) ReportDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            public void onPreMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
                DBUtil.dropFtsSyncTriggers(supportSQLiteDatabase);
            }

            /* access modifiers changed from: protected */
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(7);
                hashMap.put("id", new TableInfo.Column("id", "INTEGER", true, 1, (String) null, 1));
                hashMap.put(GosInterface.KeyName.TAG, new TableInfo.Column(GosInterface.KeyName.TAG, "TEXT", false, 0, (String) null, 1));
                hashMap.put(NotificationCompat.CATEGORY_MESSAGE, new TableInfo.Column(NotificationCompat.CATEGORY_MESSAGE, "TEXT", false, 0, (String) null, 1));
                hashMap.put("ringlogMsg", new TableInfo.Column("ringlogMsg", "TEXT", false, 0, (String) null, 1));
                hashMap.put("gppRepAggregation", new TableInfo.Column("gppRepAggregation", "TEXT", false, 0, (String) null, 1));
                hashMap.put("gppRepDataSchemeVersion", new TableInfo.Column("gppRepDataSchemeVersion", "TEXT", false, 0, (String) null, 1));
                hashMap.put("byteSize", new TableInfo.Column("byteSize", "INTEGER", true, 0, (String) null, 1));
                TableInfo tableInfo = new TableInfo("Report", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "Report");
                if (tableInfo.equals(read)) {
                    return new RoomOpenHelper.ValidationResult(true, (String) null);
                }
                return new RoomOpenHelper.ValidationResult(false, "Report(com.samsung.android.game.gos.data.model.Report).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
            }
        }, "11b0e8049606344af06380929aecca0b", "3b1bae05706fd6fb31c7d484b9f67655")).build());
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "Report");
    }

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `Report`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    public ReportDao reportDao() {
        ReportDao reportDao;
        if (this._reportDao != null) {
            return this._reportDao;
        }
        synchronized (this) {
            if (this._reportDao == null) {
                this._reportDao = new ReportDao_Impl(this);
            }
            reportDao = this._reportDao;
        }
        return reportDao;
    }
}
