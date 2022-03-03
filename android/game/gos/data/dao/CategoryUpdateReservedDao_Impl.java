package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.model.CategoryUpdateReserved;
import java.util.ArrayList;
import java.util.List;

public final class CategoryUpdateReservedDao_Impl extends CategoryUpdateReservedDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<CategoryUpdateReserved> __deletionAdapterOfCategoryUpdateReserved;
    private final EntityInsertionAdapter<CategoryUpdateReserved> __insertionAdapterOfCategoryUpdateReserved;
    private final SharedSQLiteStatement __preparedStmtOf_delete;
    private final SharedSQLiteStatement __preparedStmtOf_deleteAll;

    public CategoryUpdateReservedDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfCategoryUpdateReserved = new EntityInsertionAdapter<CategoryUpdateReserved>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `CategoryUpdateReserved` (`pkgName`) VALUES (?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, CategoryUpdateReserved categoryUpdateReserved) {
                if (categoryUpdateReserved.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, categoryUpdateReserved.pkgName);
                }
            }
        };
        this.__deletionAdapterOfCategoryUpdateReserved = new EntityDeletionOrUpdateAdapter<CategoryUpdateReserved>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `CategoryUpdateReserved` WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, CategoryUpdateReserved categoryUpdateReserved) {
                if (categoryUpdateReserved.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, categoryUpdateReserved.pkgName);
                }
            }
        };
        this.__preparedStmtOf_delete = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM CategoryUpdateReserved WHERE pkgName = ?";
            }
        };
        this.__preparedStmtOf_deleteAll = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM CategoryUpdateReserved";
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(CategoryUpdateReserved categoryUpdateReserved) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfCategoryUpdateReserved.insert(categoryUpdateReserved);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public int _delete(List<CategoryUpdateReserved> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            int handleMultiple = this.__deletionAdapterOfCategoryUpdateReserved.handleMultiple(list) + 0;
            this.__db.setTransactionSuccessful();
            return handleMultiple;
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

    /* access modifiers changed from: protected */
    public int _deleteAll() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_deleteAll.acquire();
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_deleteAll.release(acquire);
        }
    }

    public CategoryUpdateReserved getPackage(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM CategoryUpdateReserved WHERE pkgName = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        CategoryUpdateReserved categoryUpdateReserved = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            if (query.moveToFirst()) {
                categoryUpdateReserved = new CategoryUpdateReserved();
                categoryUpdateReserved.pkgName = query.getString(columnIndexOrThrow);
            }
            return categoryUpdateReserved;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<CategoryUpdateReserved> getAll() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM CategoryUpdateReserved", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                CategoryUpdateReserved categoryUpdateReserved = new CategoryUpdateReserved();
                categoryUpdateReserved.pkgName = query.getString(columnIndexOrThrow);
                arrayList.add(categoryUpdateReserved);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
