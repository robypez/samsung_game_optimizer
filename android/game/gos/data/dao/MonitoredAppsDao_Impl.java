package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.data.model.MonitoredApps;
import java.util.ArrayList;
import java.util.List;

public final class MonitoredAppsDao_Impl extends MonitoredAppsDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<MonitoredApps.SubscriberPkgName> __deletionAdapterOfSubscriberPkgNameAsMonitoredApps;
    private final EntityInsertionAdapter<MonitoredApps> __insertionAdapterOfMonitoredApps;

    public MonitoredAppsDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfMonitoredApps = new EntityInsertionAdapter<MonitoredApps>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `MonitoredApps` (`pkgName`,`subscriberPkgName`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, MonitoredApps monitoredApps) {
                if (monitoredApps.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, monitoredApps.pkgName);
                }
                if (monitoredApps.subscriberPkgName == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, monitoredApps.subscriberPkgName);
                }
            }
        };
        this.__deletionAdapterOfSubscriberPkgNameAsMonitoredApps = new EntityDeletionOrUpdateAdapter<MonitoredApps.SubscriberPkgName>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `MonitoredApps` WHERE `subscriberPkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, MonitoredApps.SubscriberPkgName subscriberPkgName) {
                if (subscriberPkgName.subscriberPkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, subscriberPkgName.subscriberPkgName);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertAll(List<MonitoredApps> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfMonitoredApps.insert(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _deleteSubscriber(MonitoredApps.SubscriberPkgName subscriberPkgName) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfSubscriberPkgNameAsMonitoredApps.handle(subscriberPkgName);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public List<String> getMonitoredAppsBySubscriberPkgName(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT pkgName FROM MonitoredApps WHERE subscriberPkgName = ?", 1);
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

    public List<String> getSubscriberAppsByMonitoredAppPkgName(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT subscriberPkgName FROM MonitoredApps WHERE pkgName = ?", 1);
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
}
