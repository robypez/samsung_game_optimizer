package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.Package;
import java.util.List;

public abstract class PackageDao {
    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(List<Package> list);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(Package... packageArr);

    /* access modifiers changed from: protected */
    public abstract int _removePkg(String str);

    /* access modifiers changed from: protected */
    public abstract int _removePkgList(List<Package> list);

    /* access modifiers changed from: protected */
    public abstract void _setAppliedCpuGpuLevel(Package.PkgNameAndAppliedCpuGpuLevel pkgNameAndAppliedCpuGpuLevel);

    /* access modifiers changed from: protected */
    public abstract void _setAppliedDss(Package.PkgNameAndAppliedDss pkgNameAndAppliedDss);

    /* access modifiers changed from: protected */
    public abstract void _setCategoryCode(Package.PkgNameAndCategoryCode pkgNameAndCategoryCode);

    /* access modifiers changed from: protected */
    public abstract void _setCustomDfs(Package.PkgNameAndCustomDfs pkgNameAndCustomDfs);

    /* access modifiers changed from: protected */
    public abstract void _setDefaultCpuLevel(Package.PkgNameAndDefaultCpuLevel pkgNameAndDefaultCpuLevel);

    /* access modifiers changed from: protected */
    public abstract void _setDefaultGpuLevel(Package.PkgNameAndDefaultGpuLevel pkgNameAndDefaultGpuLevel);

    /* access modifiers changed from: protected */
    public abstract void _setIpmPolicy(Package.PkgNameAndIpmPolicy pkgNameAndIpmPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setShiftTemperature(Package.PkgNameAndShiftTemperature pkgNameAndShiftTemperature);

    /* access modifiers changed from: protected */
    public abstract void _setSiopModePolicy(Package.PkgNameAndSiopModePolicy pkgNameAndSiopModePolicy);

    /* access modifiers changed from: protected */
    public abstract void _setTspPolicy(Package.PkgNameAndTspPolicy pkgNameAndTspPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setVersionInfo(Package.PkgNameAndVersionInfo pkgNameAndVersionInfo);

    /* access modifiers changed from: protected */
    public abstract void _setVrrMaxValue(Package.PkgNameAndVrrMaxValue pkgNameAndVrrMaxValue);

    /* access modifiers changed from: protected */
    public abstract void _setVrrMinValue(Package.PkgNameAndVrrMinValue pkgNameAndVrrMinValue);

    public abstract List<String> getAllPkgNameList();

    public abstract Package getPackage(String str);

    public abstract List<Package> getPackageListByCategory(String str);

    public abstract List<Package> getPackageListNotSyncedWithServer();

    public abstract int getPkgCountByCategory(String str);

    public abstract List<String> getPkgNameListByCategory(String str);

    public void insertOrUpdate(Package... packageArr) {
        try {
            _insertOrUpdate(packageArr);
        } catch (SQLiteFullException unused) {
        }
    }

    public void insertOrUpdate(List<Package> list) {
        try {
            _insertOrUpdate(list);
        } catch (SQLiteFullException unused) {
        }
    }

    public int removePkg(String str) {
        try {
            return _removePkg(str);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }

    public int removePkgList(List<Package> list) {
        try {
            return _removePkgList(list);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }

    public void setCategoryCode(Package.PkgNameAndCategoryCode pkgNameAndCategoryCode) {
        try {
            _setCategoryCode(pkgNameAndCategoryCode);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setDefaultCpuLevel(Package.PkgNameAndDefaultCpuLevel pkgNameAndDefaultCpuLevel) {
        try {
            _setDefaultCpuLevel(pkgNameAndDefaultCpuLevel);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setDefaultGpuLevel(Package.PkgNameAndDefaultGpuLevel pkgNameAndDefaultGpuLevel) {
        try {
            _setDefaultGpuLevel(pkgNameAndDefaultGpuLevel);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setShiftTemperature(Package.PkgNameAndShiftTemperature pkgNameAndShiftTemperature) {
        try {
            _setShiftTemperature(pkgNameAndShiftTemperature);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setIpmPolicy(Package.PkgNameAndIpmPolicy pkgNameAndIpmPolicy) {
        try {
            _setIpmPolicy(pkgNameAndIpmPolicy);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setSiopModePolicy(Package.PkgNameAndSiopModePolicy pkgNameAndSiopModePolicy) {
        try {
            _setSiopModePolicy(pkgNameAndSiopModePolicy);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setVersionInfo(Package.PkgNameAndVersionInfo pkgNameAndVersionInfo) {
        try {
            _setVersionInfo(pkgNameAndVersionInfo);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setCustomDfs(Package.PkgNameAndCustomDfs pkgNameAndCustomDfs) {
        try {
            _setCustomDfs(pkgNameAndCustomDfs);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setAppliedDss(Package.PkgNameAndAppliedDss pkgNameAndAppliedDss) {
        try {
            _setAppliedDss(pkgNameAndAppliedDss);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setAppliedCpuGpuLevel(Package.PkgNameAndAppliedCpuGpuLevel pkgNameAndAppliedCpuGpuLevel) {
        try {
            _setAppliedCpuGpuLevel(pkgNameAndAppliedCpuGpuLevel);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setVrrMaxValue(Package.PkgNameAndVrrMaxValue pkgNameAndVrrMaxValue) {
        try {
            _setVrrMaxValue(pkgNameAndVrrMaxValue);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setVrrMinValue(Package.PkgNameAndVrrMinValue pkgNameAndVrrMinValue) {
        try {
            _setVrrMinValue(pkgNameAndVrrMinValue);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setTspPolicy(Package.PkgNameAndTspPolicy pkgNameAndTspPolicy) {
        try {
            _setTspPolicy(pkgNameAndTspPolicy);
        } catch (SQLiteFullException unused) {
        }
    }
}
