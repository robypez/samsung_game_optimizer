package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import java.util.Collection;
import java.util.List;

public abstract class FeatureFlagDao {
    /* access modifiers changed from: protected */
    public abstract int _delete(List<FeatureFlag> list);

    /* access modifiers changed from: protected */
    public abstract void _deleteByFeatureName(List<String> list);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(Collection<FeatureFlag> collection);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(FeatureFlag... featureFlagArr);

    public abstract FeatureFlag getByFeatureNameAndPkgName(String str, String str2);

    public abstract List<FeatureFlag> getByPkgName(String str);

    public abstract List<String> getFeatureNames();

    public void insertOrUpdate(FeatureFlag... featureFlagArr) {
        try {
            _insertOrUpdate(featureFlagArr);
        } catch (SQLiteFullException unused) {
        }
    }

    public void insertOrUpdate(Collection<FeatureFlag> collection) {
        try {
            _insertOrUpdate(collection);
        } catch (SQLiteFullException unused) {
        }
    }

    public int delete(List<FeatureFlag> list) {
        try {
            return _delete(list);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }

    public void deleteByFeatureName(List<String> list) {
        try {
            _deleteByFeatureName(list);
        } catch (SQLiteFullException unused) {
        }
    }
}
