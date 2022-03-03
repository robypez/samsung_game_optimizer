package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FeatureFlagDao_Impl extends FeatureFlagDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<FeatureFlag> __deletionAdapterOfFeatureFlag;
    private final EntityInsertionAdapter<FeatureFlag> __insertionAdapterOfFeatureFlag;

    public FeatureFlagDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfFeatureFlag = new EntityInsertionAdapter<FeatureFlag>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `FeatureFlag` (`name`,`pkgName`,`state`,`inheritedFlag`,`forcedFlag`,`enabledFlagByServer`,`enabledFlagByUser`,`enabled`) VALUES (?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, FeatureFlag featureFlag) {
                if (featureFlag.getName() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, featureFlag.getName());
                }
                if (featureFlag.getPkgName() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, featureFlag.getPkgName());
                }
                if (featureFlag.getState() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, featureFlag.getState());
                }
                supportSQLiteStatement.bindLong(4, featureFlag.isInheritedFlag() ? 1 : 0);
                supportSQLiteStatement.bindLong(5, featureFlag.isForcedFlag() ? 1 : 0);
                supportSQLiteStatement.bindLong(6, featureFlag.isEnabledFlagByServer() ? 1 : 0);
                supportSQLiteStatement.bindLong(7, featureFlag.isEnabledFlagByUser() ? 1 : 0);
                supportSQLiteStatement.bindLong(8, featureFlag.isEnabled() ? 1 : 0);
            }
        };
        this.__deletionAdapterOfFeatureFlag = new EntityDeletionOrUpdateAdapter<FeatureFlag>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `FeatureFlag` WHERE `name` = ? AND `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, FeatureFlag featureFlag) {
                if (featureFlag.getName() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, featureFlag.getName());
                }
                if (featureFlag.getPkgName() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, featureFlag.getPkgName());
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(FeatureFlag... featureFlagArr) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfFeatureFlag.insert((T[]) featureFlagArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(Collection<FeatureFlag> collection) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfFeatureFlag.insert(collection);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public int _delete(List<FeatureFlag> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            int handleMultiple = this.__deletionAdapterOfFeatureFlag.handleMultiple(list) + 0;
            this.__db.setTransactionSuccessful();
            return handleMultiple;
        } finally {
            this.__db.endTransaction();
        }
    }

    public List<FeatureFlag> getByPkgName(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM FeatureFlag WHERE pkgName = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, ManagerInterface.KeyName.REQUEST_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "inheritedFlag");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "forcedFlag");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByServer");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByUser");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "enabled");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                FeatureFlag featureFlag = new FeatureFlag();
                featureFlag.setName(query.getString(columnIndexOrThrow));
                featureFlag.setPkgName(query.getString(columnIndexOrThrow2));
                featureFlag.setState(query.getString(columnIndexOrThrow3));
                featureFlag.setInheritedFlag(query.getInt(columnIndexOrThrow4) != 0);
                featureFlag.setForcedFlag(query.getInt(columnIndexOrThrow5) != 0);
                featureFlag.setEnabledFlagByServer(query.getInt(columnIndexOrThrow6) != 0);
                featureFlag.setEnabledFlagByUser(query.getInt(columnIndexOrThrow7) != 0);
                featureFlag.setEnabled(query.getInt(columnIndexOrThrow8) != 0);
                arrayList.add(featureFlag);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public FeatureFlag getByFeatureNameAndPkgName(String str, String str2) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM FeatureFlag WHERE name = ? AND pkgName = ?", 2);
        boolean z = true;
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        if (str2 == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, str2);
        }
        this.__db.assertNotSuspendingTransaction();
        FeatureFlag featureFlag = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, ManagerInterface.KeyName.REQUEST_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "inheritedFlag");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "forcedFlag");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByServer");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByUser");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "enabled");
            if (query.moveToFirst()) {
                featureFlag = new FeatureFlag();
                featureFlag.setName(query.getString(columnIndexOrThrow));
                featureFlag.setPkgName(query.getString(columnIndexOrThrow2));
                featureFlag.setState(query.getString(columnIndexOrThrow3));
                featureFlag.setInheritedFlag(query.getInt(columnIndexOrThrow4) != 0);
                featureFlag.setForcedFlag(query.getInt(columnIndexOrThrow5) != 0);
                featureFlag.setEnabledFlagByServer(query.getInt(columnIndexOrThrow6) != 0);
                featureFlag.setEnabledFlagByUser(query.getInt(columnIndexOrThrow7) != 0);
                if (query.getInt(columnIndexOrThrow8) == 0) {
                    z = false;
                }
                featureFlag.setEnabled(z);
            }
            return featureFlag;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<String> getFeatureNames() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT DISTINCT name FROM FeatureFlag", 0);
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

    /* access modifiers changed from: protected */
    public void _deleteByFeatureName(List<String> list) {
        this.__db.assertNotSuspendingTransaction();
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("DELETE FROM FeatureFlag WHERE name IN (");
        StringUtil.appendPlaceholders(newStringBuilder, list.size());
        newStringBuilder.append(")");
        SupportSQLiteStatement compileStatement = this.__db.compileStatement(newStringBuilder.toString());
        int i = 1;
        for (String next : list) {
            if (next == null) {
                compileStatement.bindNull(i);
            } else {
                compileStatement.bindString(i, next);
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
