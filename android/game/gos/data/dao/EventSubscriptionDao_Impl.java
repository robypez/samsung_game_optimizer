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
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.List;

public final class EventSubscriptionDao_Impl extends EventSubscriptionDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<EventSubscription.SubscriberPkgName> __deletionAdapterOfSubscriberPkgNameAsEventSubscription;
    private final EntityInsertionAdapter<EventSubscription> __insertionAdapterOfEventSubscription;

    public EventSubscriptionDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfEventSubscription = new EntityInsertionAdapter<EventSubscription>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `EventSubscription` (`eventName`,`subscriberPkgName`,`intentActionName`,`flags`,`receiver_type`) VALUES (?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, EventSubscription eventSubscription) {
                if (eventSubscription.getEventName() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, eventSubscription.getEventName());
                }
                if (eventSubscription.getSubscriberPkgName() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, eventSubscription.getSubscriberPkgName());
                }
                if (eventSubscription.intentActionName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, eventSubscription.intentActionName);
                }
                supportSQLiteStatement.bindLong(4, (long) eventSubscription.flags);
                if (eventSubscription.receiver_type == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, eventSubscription.receiver_type);
                }
            }
        };
        this.__deletionAdapterOfSubscriberPkgNameAsEventSubscription = new EntityDeletionOrUpdateAdapter<EventSubscription.SubscriberPkgName>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `EventSubscription` WHERE `subscriberPkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, EventSubscription.SubscriberPkgName subscriberPkgName) {
                if (subscriberPkgName.subscriberPkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, subscriberPkgName.subscriberPkgName);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertAll(List<EventSubscription> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfEventSubscription.insert(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _deleteSubscriber(EventSubscription.SubscriberPkgName subscriberPkgName) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfSubscriberPkgNameAsEventSubscription.handle(subscriberPkgName);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public List<EventSubscription> getSubscriberListOfEvent(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM EventSubscription WHERE eventName = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "eventName");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "subscriberPkgName");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "intentActionName");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, GosInterface.KeyName.FLAGS);
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, GosInterface.KeyName.RECEIVER_TYPE);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                EventSubscription eventSubscription = new EventSubscription(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2));
                eventSubscription.intentActionName = query.getString(columnIndexOrThrow3);
                eventSubscription.flags = query.getInt(columnIndexOrThrow4);
                eventSubscription.receiver_type = query.getString(columnIndexOrThrow5);
                arrayList.add(eventSubscription);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
