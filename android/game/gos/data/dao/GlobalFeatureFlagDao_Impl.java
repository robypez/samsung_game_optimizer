package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GlobalFeatureFlagDao_Impl extends GlobalFeatureFlagDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<GlobalFeatureFlag> __deletionAdapterOfGlobalFeatureFlag;
    private final EntityInsertionAdapter<GlobalFeatureFlag> __insertionAdapterOfGlobalFeatureFlag;
    private final EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndEnabledFlagByUser> __updateAdapterOfNameAndEnabledFlagByUserAsGlobalFeatureFlag;
    private final EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndState> __updateAdapterOfNameAndStateAsGlobalFeatureFlag;
    private final EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndUsingPkgValue> __updateAdapterOfNameAndUsingPkgValueAsGlobalFeatureFlag;
    private final EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndUsingUserValue> __updateAdapterOfNameAndUsingUserValueAsGlobalFeatureFlag;

    public GlobalFeatureFlagDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfGlobalFeatureFlag = new EntityInsertionAdapter<GlobalFeatureFlag>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `GlobalFeatureFlag` (`name`,`available`,`usingUserValue`,`usingPkgValue`,`state`,`inheritedFlag`,`forcedFlag`,`enabledFlagByServer`,`enabledFlagByUser`) VALUES (?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, GlobalFeatureFlag globalFeatureFlag) {
                if (globalFeatureFlag.name == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, globalFeatureFlag.name);
                }
                supportSQLiteStatement.bindLong(2, globalFeatureFlag.available ? 1 : 0);
                supportSQLiteStatement.bindLong(3, globalFeatureFlag.usingUserValue ? 1 : 0);
                supportSQLiteStatement.bindLong(4, globalFeatureFlag.usingPkgValue ? 1 : 0);
                if (globalFeatureFlag.getState() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, globalFeatureFlag.getState());
                }
                supportSQLiteStatement.bindLong(6, globalFeatureFlag.inheritedFlag ? 1 : 0);
                supportSQLiteStatement.bindLong(7, globalFeatureFlag.forcedFlag ? 1 : 0);
                supportSQLiteStatement.bindLong(8, globalFeatureFlag.enabledFlagByServer ? 1 : 0);
                supportSQLiteStatement.bindLong(9, globalFeatureFlag.enabledFlagByUser ? 1 : 0);
            }
        };
        this.__deletionAdapterOfGlobalFeatureFlag = new EntityDeletionOrUpdateAdapter<GlobalFeatureFlag>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `GlobalFeatureFlag` WHERE `name` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, GlobalFeatureFlag globalFeatureFlag) {
                if (globalFeatureFlag.name == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, globalFeatureFlag.name);
                }
            }
        };
        this.__updateAdapterOfNameAndUsingUserValueAsGlobalFeatureFlag = new EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndUsingUserValue>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `GlobalFeatureFlag` SET `name` = ?,`usingUserValue` = ? WHERE `name` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, GlobalFeatureFlag.NameAndUsingUserValue nameAndUsingUserValue) {
                if (nameAndUsingUserValue.name == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, nameAndUsingUserValue.name);
                }
                supportSQLiteStatement.bindLong(2, nameAndUsingUserValue.usingUserValue ? 1 : 0);
                if (nameAndUsingUserValue.name == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, nameAndUsingUserValue.name);
                }
            }
        };
        this.__updateAdapterOfNameAndUsingPkgValueAsGlobalFeatureFlag = new EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndUsingPkgValue>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `GlobalFeatureFlag` SET `name` = ?,`usingPkgValue` = ? WHERE `name` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, GlobalFeatureFlag.NameAndUsingPkgValue nameAndUsingPkgValue) {
                if (nameAndUsingPkgValue.name == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, nameAndUsingPkgValue.name);
                }
                supportSQLiteStatement.bindLong(2, nameAndUsingPkgValue.usingPkgValue ? 1 : 0);
                if (nameAndUsingPkgValue.name == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, nameAndUsingPkgValue.name);
                }
            }
        };
        this.__updateAdapterOfNameAndStateAsGlobalFeatureFlag = new EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndState>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `GlobalFeatureFlag` SET `name` = ?,`state` = ?,`inheritedFlag` = ?,`forcedFlag` = ?,`enabledFlagByServer` = ? WHERE `name` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, GlobalFeatureFlag.NameAndState nameAndState) {
                if (nameAndState.name == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, nameAndState.name);
                }
                if (nameAndState.state == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, nameAndState.state);
                }
                supportSQLiteStatement.bindLong(3, nameAndState.inheritedFlag ? 1 : 0);
                supportSQLiteStatement.bindLong(4, nameAndState.forcedFlag ? 1 : 0);
                supportSQLiteStatement.bindLong(5, nameAndState.enabledFlagByServer ? 1 : 0);
                if (nameAndState.name == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, nameAndState.name);
                }
            }
        };
        this.__updateAdapterOfNameAndEnabledFlagByUserAsGlobalFeatureFlag = new EntityDeletionOrUpdateAdapter<GlobalFeatureFlag.NameAndEnabledFlagByUser>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `GlobalFeatureFlag` SET `name` = ?,`enabledFlagByUser` = ? WHERE `name` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, GlobalFeatureFlag.NameAndEnabledFlagByUser nameAndEnabledFlagByUser) {
                if (nameAndEnabledFlagByUser.name == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, nameAndEnabledFlagByUser.name);
                }
                supportSQLiteStatement.bindLong(2, nameAndEnabledFlagByUser.enabledFlagByUser ? 1 : 0);
                if (nameAndEnabledFlagByUser.name == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, nameAndEnabledFlagByUser.name);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(GlobalFeatureFlag... globalFeatureFlagArr) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfGlobalFeatureFlag.insert((T[]) globalFeatureFlagArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(Collection<GlobalFeatureFlag> collection) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfGlobalFeatureFlag.insert(collection);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _delete(Collection<GlobalFeatureFlag> collection) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfGlobalFeatureFlag.handleMultiple(collection);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setUsingUserValue(GlobalFeatureFlag.NameAndUsingUserValue nameAndUsingUserValue) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfNameAndUsingUserValueAsGlobalFeatureFlag.handle(nameAndUsingUserValue);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setUsingPkgValue(GlobalFeatureFlag.NameAndUsingPkgValue nameAndUsingPkgValue) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfNameAndUsingPkgValueAsGlobalFeatureFlag.handle(nameAndUsingPkgValue);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setState(GlobalFeatureFlag.NameAndState nameAndState) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfNameAndStateAsGlobalFeatureFlag.handle(nameAndState);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setEnabledFlagByUser(GlobalFeatureFlag.NameAndEnabledFlagByUser... nameAndEnabledFlagByUserArr) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfNameAndEnabledFlagByUserAsGlobalFeatureFlag.handleMultiple((T[]) nameAndEnabledFlagByUserArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public GlobalFeatureFlag get(String str) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM GlobalFeatureFlag WHERE name = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        GlobalFeatureFlag globalFeatureFlag = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, ManagerInterface.KeyName.REQUEST_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, GosInterface.FeatureFlagKeyNames.AVAILABLE);
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "usingUserValue");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "usingPkgValue");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "inheritedFlag");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "forcedFlag");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByServer");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByUser");
            if (query.moveToFirst()) {
                globalFeatureFlag = new GlobalFeatureFlag();
                globalFeatureFlag.name = query.getString(columnIndexOrThrow);
                globalFeatureFlag.available = query.getInt(columnIndexOrThrow2) != 0;
                globalFeatureFlag.usingUserValue = query.getInt(columnIndexOrThrow3) != 0;
                globalFeatureFlag.usingPkgValue = query.getInt(columnIndexOrThrow4) != 0;
                globalFeatureFlag.setState(query.getString(columnIndexOrThrow5));
                globalFeatureFlag.inheritedFlag = query.getInt(columnIndexOrThrow6) != 0;
                globalFeatureFlag.forcedFlag = query.getInt(columnIndexOrThrow7) != 0;
                globalFeatureFlag.enabledFlagByServer = query.getInt(columnIndexOrThrow8) != 0;
                if (query.getInt(columnIndexOrThrow9) == 0) {
                    z = false;
                }
                globalFeatureFlag.enabledFlagByUser = z;
            }
            return globalFeatureFlag;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<GlobalFeatureFlag> getAll() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM GlobalFeatureFlag", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, ManagerInterface.KeyName.REQUEST_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, GosInterface.FeatureFlagKeyNames.AVAILABLE);
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "usingUserValue");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "usingPkgValue");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "inheritedFlag");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "forcedFlag");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByServer");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "enabledFlagByUser");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                GlobalFeatureFlag globalFeatureFlag = new GlobalFeatureFlag();
                globalFeatureFlag.name = query.getString(columnIndexOrThrow);
                globalFeatureFlag.available = query.getInt(columnIndexOrThrow2) != 0;
                globalFeatureFlag.usingUserValue = query.getInt(columnIndexOrThrow3) != 0;
                globalFeatureFlag.usingPkgValue = query.getInt(columnIndexOrThrow4) != 0;
                globalFeatureFlag.setState(query.getString(columnIndexOrThrow5));
                globalFeatureFlag.inheritedFlag = query.getInt(columnIndexOrThrow6) != 0;
                globalFeatureFlag.forcedFlag = query.getInt(columnIndexOrThrow7) != 0;
                globalFeatureFlag.enabledFlagByServer = query.getInt(columnIndexOrThrow8) != 0;
                globalFeatureFlag.enabledFlagByUser = query.getInt(columnIndexOrThrow9) != 0;
                arrayList.add(globalFeatureFlag);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isAvailable(String str) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT available FROM GlobalFeatureFlag WHERE name = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        boolean z2 = false;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                if (query.getInt(0) == 0) {
                    z = false;
                }
                z2 = z;
            }
            return z2;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isUsingUserValue(String str) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT usingUserValue FROM GlobalFeatureFlag WHERE name = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        boolean z2 = false;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                if (query.getInt(0) == 0) {
                    z = false;
                }
                z2 = z;
            }
            return z2;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isUsingPkgValue(String str) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT usingPkgValue FROM GlobalFeatureFlag WHERE name = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        boolean z2 = false;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                if (query.getInt(0) == 0) {
                    z = false;
                }
                z2 = z;
            }
            return z2;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getState(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT state FROM GlobalFeatureFlag WHERE name = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        String str2 = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str2 = query.getString(0);
            }
            return str2;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isEnabledFlagByServer(String str) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT enabledFlagByServer FROM GlobalFeatureFlag WHERE name = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        boolean z2 = false;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                if (query.getInt(0) == 0) {
                    z = false;
                }
                z2 = z;
            }
            return z2;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isEnabledFlagByUser(String str) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT enabledFlagByUser FROM GlobalFeatureFlag WHERE name = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        boolean z2 = false;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                if (query.getInt(0) == 0) {
                    z = false;
                }
                z2 = z;
            }
            return z2;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
