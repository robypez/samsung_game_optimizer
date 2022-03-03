package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.model.CategoryInfo;
import com.samsung.android.game.gos.feature.dfs.FpsController;

public final class CategoryInfoDao_Impl extends CategoryInfoDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<CategoryInfo> __insertionAdapterOfCategoryInfo;
    private final SharedSQLiteStatement __preparedStmtOf_delete;

    public CategoryInfoDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfCategoryInfo = new EntityInsertionAdapter<CategoryInfo>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `CategoryInfo` (`pkgName`,`category`,`fixed`) VALUES (?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, CategoryInfo categoryInfo) {
                if (categoryInfo.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, categoryInfo.pkgName);
                }
                if (categoryInfo.category == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, categoryInfo.category);
                }
                supportSQLiteStatement.bindLong(3, (long) categoryInfo.fixed);
            }
        };
        this.__preparedStmtOf_delete = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM CategoryInfo WHERE pkgName = ?";
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(CategoryInfo categoryInfo) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfCategoryInfo.insert(categoryInfo);
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

    public CategoryInfo getPackage(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM CategoryInfo WHERE pkgName = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        CategoryInfo categoryInfo = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "category");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, FpsController.TYPE_FIXED);
            if (query.moveToFirst()) {
                categoryInfo = new CategoryInfo();
                categoryInfo.pkgName = query.getString(columnIndexOrThrow);
                categoryInfo.category = query.getString(columnIndexOrThrow2);
                categoryInfo.fixed = query.getInt(columnIndexOrThrow3);
            }
            return categoryInfo;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
