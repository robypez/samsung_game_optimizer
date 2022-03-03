package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import java.util.Collection;
import java.util.List;

public abstract class GlobalFeatureFlagDao {
    /* access modifiers changed from: protected */
    public abstract void _delete(Collection<GlobalFeatureFlag> collection);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(Collection<GlobalFeatureFlag> collection);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(GlobalFeatureFlag... globalFeatureFlagArr);

    /* access modifiers changed from: protected */
    public abstract void _setEnabledFlagByUser(GlobalFeatureFlag.NameAndEnabledFlagByUser... nameAndEnabledFlagByUserArr);

    /* access modifiers changed from: protected */
    public abstract void _setState(GlobalFeatureFlag.NameAndState nameAndState);

    /* access modifiers changed from: protected */
    public abstract void _setUsingPkgValue(GlobalFeatureFlag.NameAndUsingPkgValue nameAndUsingPkgValue);

    /* access modifiers changed from: protected */
    public abstract void _setUsingUserValue(GlobalFeatureFlag.NameAndUsingUserValue nameAndUsingUserValue);

    public abstract GlobalFeatureFlag get(String str);

    public abstract List<GlobalFeatureFlag> getAll();

    public abstract String getState(String str);

    public abstract boolean isAvailable(String str);

    public abstract boolean isEnabledFlagByServer(String str);

    public abstract boolean isEnabledFlagByUser(String str);

    public abstract boolean isUsingPkgValue(String str);

    public abstract boolean isUsingUserValue(String str);

    public void insertOrUpdate(GlobalFeatureFlag... globalFeatureFlagArr) {
        try {
            _insertOrUpdate(globalFeatureFlagArr);
        } catch (SQLiteFullException unused) {
        }
    }

    public void insertOrUpdate(Collection<GlobalFeatureFlag> collection) {
        try {
            _insertOrUpdate(collection);
        } catch (SQLiteFullException unused) {
        }
    }

    public void delete(Collection<GlobalFeatureFlag> collection) {
        try {
            _delete(collection);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setUsingUserValue(GlobalFeatureFlag.NameAndUsingUserValue nameAndUsingUserValue) {
        try {
            _setUsingUserValue(nameAndUsingUserValue);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setUsingPkgValue(GlobalFeatureFlag.NameAndUsingPkgValue nameAndUsingPkgValue) {
        try {
            _setUsingPkgValue(nameAndUsingPkgValue);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setState(GlobalFeatureFlag.NameAndState nameAndState) {
        try {
            _setState(nameAndState);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setEnabledFlagByUser(GlobalFeatureFlag.NameAndEnabledFlagByUser... nameAndEnabledFlagByUserArr) {
        try {
            _setEnabledFlagByUser(nameAndEnabledFlagByUserArr);
        } catch (SQLiteFullException unused) {
        }
    }
}
