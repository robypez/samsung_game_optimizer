package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.model.PerfDataPermission;

public final class PerfDataPermissionDao_Impl extends PerfDataPermissionDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<PerfDataPermission> __insertionAdapterOfPerfDataPermission;

    public PerfDataPermissionDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfPerfDataPermission = new EntityInsertionAdapter<PerfDataPermission>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `PerfDataPermission` (`pkgName`,`permType`,`paramListCsv`,`permPolicy`,`lastHandshakeTime`) VALUES (?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, PerfDataPermission perfDataPermission) {
                if (perfDataPermission.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, perfDataPermission.pkgName);
                }
                supportSQLiteStatement.bindLong(2, (long) perfDataPermission.permType);
                if (perfDataPermission.paramListCsv == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, perfDataPermission.paramListCsv);
                }
                supportSQLiteStatement.bindLong(4, (long) perfDataPermission.permPolicy);
                if (perfDataPermission.lastHandshakeTime == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, perfDataPermission.lastHandshakeTime);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(PerfDataPermission perfDataPermission) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfPerfDataPermission.insert(perfDataPermission);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public PerfDataPermission getPermissionsForPkgByClientPkg(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM PerfDataPermission WHERE pkgName = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        PerfDataPermission perfDataPermission = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "permType");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "paramListCsv");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "permPolicy");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "lastHandshakeTime");
            if (query.moveToFirst()) {
                perfDataPermission = new PerfDataPermission();
                perfDataPermission.pkgName = query.getString(columnIndexOrThrow);
                perfDataPermission.permType = query.getInt(columnIndexOrThrow2);
                perfDataPermission.paramListCsv = query.getString(columnIndexOrThrow3);
                perfDataPermission.permPolicy = query.getInt(columnIndexOrThrow4);
                perfDataPermission.lastHandshakeTime = query.getString(columnIndexOrThrow5);
            }
            return perfDataPermission;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
