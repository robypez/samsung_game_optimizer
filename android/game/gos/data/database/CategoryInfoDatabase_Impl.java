package com.samsung.android.game.gos.data.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.dao.CategoryInfoDao;
import com.samsung.android.game.gos.data.dao.CategoryInfoDao_Impl;
import com.samsung.android.game.gos.feature.dfs.FpsController;
import java.util.HashMap;
import java.util.HashSet;

public final class CategoryInfoDatabase_Impl extends CategoryInfoDatabase {
    private volatile CategoryInfoDao _categoryInfoDao;

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new RoomOpenHelper.Delegate(2) {
            public void onPostMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            }

            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `CategoryInfo` (`pkgName` TEXT NOT NULL, `category` TEXT NOT NULL DEFAULT 'undefined', `fixed` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`pkgName`))");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b5c3879387e5f0649ff1660d6f09446f')");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `CategoryInfo`");
                if (CategoryInfoDatabase_Impl.this.mCallbacks != null) {
                    int size = CategoryInfoDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) CategoryInfoDatabase_Impl.this.mCallbacks.get(i)).onDestructiveMigration(supportSQLiteDatabase);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (CategoryInfoDatabase_Impl.this.mCallbacks != null) {
                    int size = CategoryInfoDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) CategoryInfoDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase unused = CategoryInfoDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                CategoryInfoDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (CategoryInfoDatabase_Impl.this.mCallbacks != null) {
                    int size = CategoryInfoDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) CategoryInfoDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            public void onPreMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
                DBUtil.dropFtsSyncTriggers(supportSQLiteDatabase);
            }

            /* access modifiers changed from: protected */
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(3);
                hashMap.put(EventPublisher.EXTRA_KEY_PKG_NAME, new TableInfo.Column(EventPublisher.EXTRA_KEY_PKG_NAME, "TEXT", true, 1, (String) null, 1));
                hashMap.put("category", new TableInfo.Column("category", "TEXT", true, 0, "'undefined'", 1));
                hashMap.put(FpsController.TYPE_FIXED, new TableInfo.Column(FpsController.TYPE_FIXED, "INTEGER", true, 0, "0", 1));
                TableInfo tableInfo = new TableInfo("CategoryInfo", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "CategoryInfo");
                if (tableInfo.equals(read)) {
                    return new RoomOpenHelper.ValidationResult(true, (String) null);
                }
                return new RoomOpenHelper.ValidationResult(false, "CategoryInfo(com.samsung.android.game.gos.data.model.CategoryInfo).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
            }
        }, "b5c3879387e5f0649ff1660d6f09446f", "bad958af89e0fe527e6706f364311f18")).build());
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "CategoryInfo");
    }

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `CategoryInfo`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    public CategoryInfoDao categoryInfoDao() {
        CategoryInfoDao categoryInfoDao;
        if (this._categoryInfoDao != null) {
            return this._categoryInfoDao;
        }
        synchronized (this) {
            if (this._categoryInfoDao == null) {
                this._categoryInfoDao = new CategoryInfoDao_Impl(this);
            }
            categoryInfoDao = this._categoryInfoDao;
        }
        return categoryInfoDao;
    }
}
