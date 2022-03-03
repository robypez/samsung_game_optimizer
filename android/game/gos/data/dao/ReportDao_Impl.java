package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.core.app.NotificationCompat;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.data.model.Report;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.List;

public final class ReportDao_Impl extends ReportDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<Report> __insertionAdapterOfReport;
    private final SharedSQLiteStatement __preparedStmtOf_deleteAll;
    private final SharedSQLiteStatement __preparedStmtOf_deleteById;

    public ReportDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfReport = new EntityInsertionAdapter<Report>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `Report` (`id`,`tag`,`msg`,`ringlogMsg`,`gppRepAggregation`,`gppRepDataSchemeVersion`,`byteSize`) VALUES (?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Report report) {
                supportSQLiteStatement.bindLong(1, report.getId());
                if (report.getTag() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, report.getTag());
                }
                if (report.getMsg() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, report.getMsg());
                }
                if (report.getRinglogMsg() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, report.getRinglogMsg());
                }
                if (report.getGppRepAggregation() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, report.getGppRepAggregation());
                }
                if (report.getGppRepDataSchemeVersion() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, report.getGppRepDataSchemeVersion());
                }
                supportSQLiteStatement.bindLong(7, (long) report.getByteSize());
            }
        };
        this.__preparedStmtOf_deleteById = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM Report WHERE id = ?";
            }
        };
        this.__preparedStmtOf_deleteAll = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM Report";
            }
        };
    }

    /* access modifiers changed from: protected */
    public long _insertOrUpdate(Report report) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfReport.insertAndReturnId(report);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(List<Report> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfReport.insert(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _deleteById(long j) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_deleteById.acquire();
        acquire.bindLong(1, j);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_deleteById.release(acquire);
        }
    }

    /* access modifiers changed from: protected */
    public void _deleteAll() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_deleteAll.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_deleteAll.release(acquire);
        }
    }

    public List<Long> getAllId() {
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM Report ORDER BY id ASC", 0);
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

    public List<Report.IdAndSize> getAllIdAndSize_byReversedOrder() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, byteSize FROM Report ORDER BY id DESC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "byteSize");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                Report.IdAndSize idAndSize = new Report.IdAndSize();
                idAndSize.id = query.getLong(columnIndexOrThrow);
                idAndSize.size = query.getInt(columnIndexOrThrow2);
                arrayList.add(idAndSize);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public Report getById(long j) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM Report WHERE id = ?", 1);
        acquire.bindLong(1, j);
        this.__db.assertNotSuspendingTransaction();
        Report report = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, GosInterface.KeyName.TAG);
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_MESSAGE);
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "ringlogMsg");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "gppRepAggregation");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "gppRepDataSchemeVersion");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "byteSize");
            if (query.moveToFirst()) {
                report = new Report(query.getLong(columnIndexOrThrow));
                report.setTag(query.getString(columnIndexOrThrow2));
                report.setMsg(query.getString(columnIndexOrThrow3));
                report.setRinglogMsg(query.getString(columnIndexOrThrow4));
                report.setGppRepAggregation(query.getString(columnIndexOrThrow5));
                report.setGppRepDataSchemeVersion(query.getString(columnIndexOrThrow6));
                report.setByteSize(query.getInt(columnIndexOrThrow7));
            }
            return report;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<Long> getIdListByTag(String str) {
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM Report WHERE tag = ? ORDER BY id ASC", 1);
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

    public List<Long> getIdListBySize(int i) {
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM Report WHERE byteSize = ? ORDER BY id ASC", 1);
        acquire.bindLong(1, (long) i);
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

    /* access modifiers changed from: protected */
    public void _deleteByIdList(List<Long> list) {
        this.__db.assertNotSuspendingTransaction();
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("DELETE FROM Report WHERE id IN (");
        StringUtil.appendPlaceholders(newStringBuilder, list.size());
        newStringBuilder.append(")");
        SupportSQLiteStatement compileStatement = this.__db.compileStatement(newStringBuilder.toString());
        int i = 1;
        for (Long next : list) {
            if (next == null) {
                compileStatement.bindNull(i);
            } else {
                compileStatement.bindLong(i, next.longValue());
            }
            i++;
        }
        this.__db.beginTransaction();
        try {
            compileStatement.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}
