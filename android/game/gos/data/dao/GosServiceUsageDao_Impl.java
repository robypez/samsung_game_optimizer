package com.samsung.android.game.gos.data.dao;

import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.data.model.GosServiceUsage;

public final class GosServiceUsageDao_Impl extends GosServiceUsageDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<GosServiceUsage> __insertionAdapterOfGosServiceUsage;

    public GosServiceUsageDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfGosServiceUsage = new EntityInsertionAdapter<GosServiceUsage>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR IGNORE INTO `GosServiceUsage` (`command`,`callerPkgName`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, GosServiceUsage gosServiceUsage) {
                if (gosServiceUsage.command == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, gosServiceUsage.command);
                }
                if (gosServiceUsage.callerPkgName == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, gosServiceUsage.callerPkgName);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(GosServiceUsage gosServiceUsage) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfGosServiceUsage.insert(gosServiceUsage);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}
