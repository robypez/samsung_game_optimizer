package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.data.model.SettingsAccessiblePackage;
import java.util.ArrayList;
import java.util.List;

public final class SettingsAccessiblePackageDao_Impl extends SettingsAccessiblePackageDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<SettingsAccessiblePackage> __insertionAdapterOfSettingsAccessiblePackage;
    private final SharedSQLiteStatement __preparedStmtOf_delete;

    public SettingsAccessiblePackageDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfSettingsAccessiblePackage = new EntityInsertionAdapter<SettingsAccessiblePackage>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `SettingsAccessiblePackage` (`pkgName`,`featureName`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, SettingsAccessiblePackage settingsAccessiblePackage) {
                if (settingsAccessiblePackage.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, settingsAccessiblePackage.pkgName);
                }
                if (settingsAccessiblePackage.featureName == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, settingsAccessiblePackage.featureName);
                }
            }
        };
        this.__preparedStmtOf_delete = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM SettingsAccessiblePackage WHERE pkgName = ?";
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(SettingsAccessiblePackage settingsAccessiblePackage) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfSettingsAccessiblePackage.insert(settingsAccessiblePackage);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public int _delete(String str) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_delete.acquire();
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_delete.release(acquire);
        }
    }

    public List<String> getFeatures(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT featureName FROM SettingsAccessiblePackage WHERE pkgName = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public long getSettingsAccessiblePackageCount(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM SettingsAccessiblePackage WHERE featureName = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
