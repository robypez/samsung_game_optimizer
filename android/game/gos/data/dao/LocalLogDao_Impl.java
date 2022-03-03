package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public final class LocalLogDao_Impl extends LocalLogDao {
    private final RoomDatabase __db;
    private final SharedSQLiteStatement __preparedStmtOf_deleteByIdBetween;
    private final SharedSQLiteStatement __preparedStmtOf_insert;

    public LocalLogDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__preparedStmtOf_insert = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "INSERT INTO LocalLog (timeStamp, time, msg, tag) VALUES (?, ?, ?, ?)";
            }
        };
        this.__preparedStmtOf_deleteByIdBetween = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM LocalLog WHERE id BETWEEN ? AND ?";
            }
        };
    }

    /* access modifiers changed from: protected */
    public long _insert(long j, String str, String str2, String str3) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_insert.acquire();
        acquire.bindLong(1, j);
        if (str == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, str);
        }
        if (str2 == null) {
            acquire.bindNull(3);
        } else {
            acquire.bindString(3, str2);
        }
        if (str3 == null) {
            acquire.bindNull(4);
        } else {
            acquire.bindString(4, str3);
        }
        this.__db.beginTransaction();
        try {
            long executeInsert = acquire.executeInsert();
            this.__db.setTransactionSuccessful();
            return executeInsert;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_insert.release(acquire);
        }
    }

    /* access modifiers changed from: protected */
    public void _deleteByIdBetween(long j, long j2) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_deleteByIdBetween.acquire();
        acquire.bindLong(1, j);
        acquire.bindLong(2, j2);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_deleteByIdBetween.release(acquire);
        }
    }

    public List<Long> getAllId() {
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM LocalLog ORDER BY id ASC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                if (query.isNull(0)) {
                    l = null;
                } else {
                    l = Long.valueOf(query.getLong(0));
                }
                arrayList.add(l);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
