package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.PerfDataPermission;

public abstract class PerfDataPermissionDao {
    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(PerfDataPermission perfDataPermission);

    public abstract PerfDataPermission getPermissionsForPkgByClientPkg(String str);

    public void insertOrUpdate(PerfDataPermission perfDataPermission) {
        try {
            _insertOrUpdate(perfDataPermission);
        } catch (SQLiteFullException unused) {
        }
    }
}
