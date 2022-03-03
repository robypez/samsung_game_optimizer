package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.CategoryInfo;

public abstract class CategoryInfoDao {
    /* access modifiers changed from: protected */
    public abstract int _delete(String str);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(CategoryInfo categoryInfo);

    public abstract CategoryInfo getPackage(String str);

    public void insertOrUpdate(CategoryInfo categoryInfo) {
        try {
            _insertOrUpdate(categoryInfo);
        } catch (SQLiteFullException unused) {
        }
    }

    public int delete(String str) {
        try {
            return _delete(str);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }
}
