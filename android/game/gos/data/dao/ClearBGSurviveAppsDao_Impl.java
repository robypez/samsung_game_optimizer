package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.data.model.ClearBGSurviveApps;
import java.util.ArrayList;
import java.util.List;

public final class ClearBGSurviveAppsDao_Impl extends ClearBGSurviveAppsDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<ClearBGSurviveApps> __insertionAdapterOfClearBGSurviveApps;
    private final SharedSQLiteStatement __preparedStmtOf_deleteWithCallerPkgName;

    public ClearBGSurviveAppsDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfClearBGSurviveApps = new EntityInsertionAdapter<ClearBGSurviveApps>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `ClearBGSurviveApps` (`callerPkgName`,`surviveAppPkgName`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, ClearBGSurviveApps clearBGSurviveApps) {
                if (clearBGSurviveApps.callerPkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, clearBGSurviveApps.callerPkgName);
                }
                if (clearBGSurviveApps.surviveAppPkgName == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, clearBGSurviveApps.surviveAppPkgName);
                }
            }
        };
        this.__preparedStmtOf_deleteWithCallerPkgName = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM ClearBGSurviveApps WHERE callerPkgName = ?";
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertAll(List<ClearBGSurviveApps> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfClearBGSurviveApps.insert(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void setSurviveList(String str, List<String> list) {
        this.__db.beginTransaction();
        try {
            super.setSurviveList(str, list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _deleteWithCallerPkgName(String str) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_deleteWithCallerPkgName.acquire();
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_deleteWithCallerPkgName.release(acquire);
        }
    }

    public List<String> getSurviveAppList() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT surviveAppPkgName FROM ClearBGSurviveApps", 0);
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
}
