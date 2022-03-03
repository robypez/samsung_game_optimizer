package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.SettingsAccessiblePackage;
import java.util.List;

public abstract class SettingsAccessiblePackageDao {
    /* access modifiers changed from: protected */
    public abstract int _delete(String str);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(SettingsAccessiblePackage settingsAccessiblePackage);

    public abstract List<String> getFeatures(String str);

    public abstract long getSettingsAccessiblePackageCount(String str);

    public int delete(String str) {
        try {
            return _delete(str);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }

    public void insertOrUpdate(SettingsAccessiblePackage settingsAccessiblePackage) {
        try {
            _insertOrUpdate(settingsAccessiblePackage);
        } catch (SQLiteFullException unused) {
        }
    }
}
