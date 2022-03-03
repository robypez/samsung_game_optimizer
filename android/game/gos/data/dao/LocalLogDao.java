package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import java.util.List;

public abstract class LocalLogDao {
    /* access modifiers changed from: protected */
    public abstract void _deleteByIdBetween(long j, long j2);

    /* access modifiers changed from: protected */
    public abstract long _insert(long j, String str, String str2, String str3);

    public abstract List<Long> getAllId();

    public long insert(long j, String str, String str2, String str3) {
        try {
            return _insert(j, str, str2, str3);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }

    public void deleteByIdBetween(long j, long j2) {
        try {
            _deleteByIdBetween(j, j2);
        } catch (SQLiteFullException unused) {
        }
    }
}
