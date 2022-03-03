package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.CategoryUpdateReserved;
import java.util.List;

public abstract class CategoryUpdateReservedDao {
    /* access modifiers changed from: protected */
    public abstract int _delete(String str);

    /* access modifiers changed from: protected */
    public abstract int _delete(List<CategoryUpdateReserved> list);

    /* access modifiers changed from: protected */
    public abstract int _deleteAll();

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(CategoryUpdateReserved categoryUpdateReserved);

    public abstract List<CategoryUpdateReserved> getAll();

    public abstract CategoryUpdateReserved getPackage(String str);

    public void insertOrUpdate(CategoryUpdateReserved categoryUpdateReserved) {
        try {
            _insertOrUpdate(categoryUpdateReserved);
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

    public int delete(List<CategoryUpdateReserved> list) {
        try {
            return _delete(list);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }

    public int deleteAll() {
        try {
            return _deleteAll();
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }
}
