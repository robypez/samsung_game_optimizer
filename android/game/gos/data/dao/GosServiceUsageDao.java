package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.GosServiceUsage;

public abstract class GosServiceUsageDao {
    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(GosServiceUsage gosServiceUsage);

    public void insertOrUpdate(GosServiceUsage gosServiceUsage) {
        try {
            _insertOrUpdate(gosServiceUsage);
        } catch (SQLiteFullException unused) {
        }
    }
}
