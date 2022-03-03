package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.model.Package;
import java.util.ArrayList;
import java.util.List;

public final class PackageDao_Impl extends PackageDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<Package> __deletionAdapterOfPackage;
    private final EntityInsertionAdapter<Package> __insertionAdapterOfPackage;
    private final SharedSQLiteStatement __preparedStmtOf_removePkg;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndAppliedCpuGpuLevel> __updateAdapterOfPkgNameAndAppliedCpuGpuLevelAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndAppliedDss> __updateAdapterOfPkgNameAndAppliedDssAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndCategoryCode> __updateAdapterOfPkgNameAndCategoryCodeAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndCustomDfs> __updateAdapterOfPkgNameAndCustomDfsAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndDefaultCpuLevel> __updateAdapterOfPkgNameAndDefaultCpuLevelAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndDefaultGpuLevel> __updateAdapterOfPkgNameAndDefaultGpuLevelAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndIpmPolicy> __updateAdapterOfPkgNameAndIpmPolicyAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndShiftTemperature> __updateAdapterOfPkgNameAndShiftTemperatureAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndSiopModePolicy> __updateAdapterOfPkgNameAndSiopModePolicyAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndTspPolicy> __updateAdapterOfPkgNameAndTspPolicyAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndVersionInfo> __updateAdapterOfPkgNameAndVersionInfoAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndVrrMaxValue> __updateAdapterOfPkgNameAndVrrMaxValueAsPackage;
    private final EntityDeletionOrUpdateAdapter<Package.PkgNameAndVrrMinValue> __updateAdapterOfPkgNameAndVrrMinValueAsPackage;

    public PackageDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfPackage = new EntityInsertionAdapter<Package>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `Package` (`pkgName`,`categoryCode`,`installedUserIds`,`defaultSetBy`,`pkgAddedTime`,`serverPkgUpdatedTime`,`eachModeTargetShortSide`,`eachModeDss`,`customResolutionMode`,`customDss`,`eachModeDfs`,`customDfsMode`,`customDfs`,`defaultCpuLevel`,`defaultGpuLevel`,`governorSettings`,`shiftTemperature`,`ipmPolicy`,`siopModePolicy`,`customSiopMode`,`gameSdkSettings`,`sosPolicy`,`resumeBoostPolicy`,`launchBoostPolicy`,`wifiQosPolicy`,`subscriberList`,`gfiPolicy`,`tspPolicy`,`appliedDss`,`appliedCpuLevel`,`appliedGpuLevel`,`versionName`,`versionCode`,`vrrMaxValue`,`vrrMinValue`,`drrAllowed`,`targetGroupName`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package packageR) {
                if (packageR.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, packageR.pkgName);
                }
                if (packageR.categoryCode == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, packageR.categoryCode);
                }
                if (packageR.installedUserIds == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, packageR.installedUserIds);
                }
                supportSQLiteStatement.bindLong(4, (long) packageR.defaultSetBy);
                supportSQLiteStatement.bindLong(5, packageR.pkgAddedTime);
                supportSQLiteStatement.bindLong(6, packageR.serverPkgUpdatedTime);
                if (packageR.getEachModeTargetShortSide() == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, packageR.getEachModeTargetShortSide());
                }
                if (packageR.getEachModeDss() == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, packageR.getEachModeDss());
                }
                supportSQLiteStatement.bindLong(9, (long) packageR.getCustomResolutionMode());
                supportSQLiteStatement.bindDouble(10, (double) packageR.getCustomDss());
                if (packageR.getEachModeDfs() == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindString(11, packageR.getEachModeDfs());
                }
                supportSQLiteStatement.bindLong(12, (long) packageR.getCustomDfsMode());
                supportSQLiteStatement.bindDouble(13, (double) packageR.getCustomDfs());
                supportSQLiteStatement.bindLong(14, (long) packageR.defaultCpuLevel);
                supportSQLiteStatement.bindLong(15, (long) packageR.defaultGpuLevel);
                if (packageR.governorSettings == null) {
                    supportSQLiteStatement.bindNull(16);
                } else {
                    supportSQLiteStatement.bindString(16, packageR.governorSettings);
                }
                supportSQLiteStatement.bindLong(17, (long) packageR.shiftTemperature);
                if (packageR.ipmPolicy == null) {
                    supportSQLiteStatement.bindNull(18);
                } else {
                    supportSQLiteStatement.bindString(18, packageR.ipmPolicy);
                }
                if (packageR.siopModePolicy == null) {
                    supportSQLiteStatement.bindNull(19);
                } else {
                    supportSQLiteStatement.bindString(19, packageR.siopModePolicy);
                }
                supportSQLiteStatement.bindLong(20, (long) packageR.customSiopMode);
                if (packageR.gameSdkSettings == null) {
                    supportSQLiteStatement.bindNull(21);
                } else {
                    supportSQLiteStatement.bindString(21, packageR.gameSdkSettings);
                }
                if (packageR.sosPolicy == null) {
                    supportSQLiteStatement.bindNull(22);
                } else {
                    supportSQLiteStatement.bindString(22, packageR.sosPolicy);
                }
                if (packageR.resumeBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(23);
                } else {
                    supportSQLiteStatement.bindString(23, packageR.resumeBoostPolicy);
                }
                if (packageR.launchBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(24);
                } else {
                    supportSQLiteStatement.bindString(24, packageR.launchBoostPolicy);
                }
                if (packageR.wifiQosPolicy == null) {
                    supportSQLiteStatement.bindNull(25);
                } else {
                    supportSQLiteStatement.bindString(25, packageR.wifiQosPolicy);
                }
                if (packageR.subscriberList == null) {
                    supportSQLiteStatement.bindNull(26);
                } else {
                    supportSQLiteStatement.bindString(26, packageR.subscriberList);
                }
                if (packageR.gfiPolicy == null) {
                    supportSQLiteStatement.bindNull(27);
                } else {
                    supportSQLiteStatement.bindString(27, packageR.gfiPolicy);
                }
                if (packageR.tspPolicy == null) {
                    supportSQLiteStatement.bindNull(28);
                } else {
                    supportSQLiteStatement.bindString(28, packageR.tspPolicy);
                }
                supportSQLiteStatement.bindDouble(29, (double) packageR.appliedDss);
                supportSQLiteStatement.bindLong(30, (long) packageR.appliedCpuLevel);
                supportSQLiteStatement.bindLong(31, (long) packageR.appliedGpuLevel);
                if (packageR.versionName == null) {
                    supportSQLiteStatement.bindNull(32);
                } else {
                    supportSQLiteStatement.bindString(32, packageR.versionName);
                }
                supportSQLiteStatement.bindLong(33, packageR.versionCode);
                supportSQLiteStatement.bindLong(34, (long) packageR.vrrMaxValue);
                supportSQLiteStatement.bindLong(35, (long) packageR.vrrMinValue);
                supportSQLiteStatement.bindLong(36, (long) packageR.drrAllowed);
                if (packageR.targetGroupName == null) {
                    supportSQLiteStatement.bindNull(37);
                } else {
                    supportSQLiteStatement.bindString(37, packageR.targetGroupName);
                }
            }
        };
        this.__deletionAdapterOfPackage = new EntityDeletionOrUpdateAdapter<Package>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `Package` WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package packageR) {
                if (packageR.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, packageR.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndCategoryCodeAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndCategoryCode>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`categoryCode` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndCategoryCode pkgNameAndCategoryCode) {
                if (pkgNameAndCategoryCode.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndCategoryCode.pkgName);
                }
                if (pkgNameAndCategoryCode.categoryCode == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, pkgNameAndCategoryCode.categoryCode);
                }
                if (pkgNameAndCategoryCode.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndCategoryCode.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndDefaultCpuLevelAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndDefaultCpuLevel>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`defaultCpuLevel` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndDefaultCpuLevel pkgNameAndDefaultCpuLevel) {
                if (pkgNameAndDefaultCpuLevel.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndDefaultCpuLevel.pkgName);
                }
                supportSQLiteStatement.bindLong(2, (long) pkgNameAndDefaultCpuLevel.defaultCpuLevel);
                if (pkgNameAndDefaultCpuLevel.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndDefaultCpuLevel.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndDefaultGpuLevelAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndDefaultGpuLevel>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`defaultGpuLevel` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndDefaultGpuLevel pkgNameAndDefaultGpuLevel) {
                if (pkgNameAndDefaultGpuLevel.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndDefaultGpuLevel.pkgName);
                }
                supportSQLiteStatement.bindLong(2, (long) pkgNameAndDefaultGpuLevel.defaultGpuLevel);
                if (pkgNameAndDefaultGpuLevel.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndDefaultGpuLevel.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndShiftTemperatureAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndShiftTemperature>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`shiftTemperature` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndShiftTemperature pkgNameAndShiftTemperature) {
                if (pkgNameAndShiftTemperature.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndShiftTemperature.pkgName);
                }
                supportSQLiteStatement.bindLong(2, (long) pkgNameAndShiftTemperature.shiftTemperature);
                if (pkgNameAndShiftTemperature.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndShiftTemperature.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndIpmPolicyAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndIpmPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`ipmPolicy` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndIpmPolicy pkgNameAndIpmPolicy) {
                if (pkgNameAndIpmPolicy.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndIpmPolicy.pkgName);
                }
                if (pkgNameAndIpmPolicy.ipmPolicy == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, pkgNameAndIpmPolicy.ipmPolicy);
                }
                if (pkgNameAndIpmPolicy.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndIpmPolicy.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndSiopModePolicyAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndSiopModePolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`siopModePolicy` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndSiopModePolicy pkgNameAndSiopModePolicy) {
                if (pkgNameAndSiopModePolicy.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndSiopModePolicy.pkgName);
                }
                if (pkgNameAndSiopModePolicy.siopModePolicy == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, pkgNameAndSiopModePolicy.siopModePolicy);
                }
                if (pkgNameAndSiopModePolicy.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndSiopModePolicy.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndVersionInfoAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndVersionInfo>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`versionName` = ?,`versionCode` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndVersionInfo pkgNameAndVersionInfo) {
                if (pkgNameAndVersionInfo.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndVersionInfo.pkgName);
                }
                if (pkgNameAndVersionInfo.versionName == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, pkgNameAndVersionInfo.versionName);
                }
                supportSQLiteStatement.bindLong(3, pkgNameAndVersionInfo.versionCode);
                if (pkgNameAndVersionInfo.pkgName == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, pkgNameAndVersionInfo.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndCustomDfsAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndCustomDfs>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`customDfs` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndCustomDfs pkgNameAndCustomDfs) {
                if (pkgNameAndCustomDfs.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndCustomDfs.pkgName);
                }
                supportSQLiteStatement.bindDouble(2, (double) pkgNameAndCustomDfs.customDfs);
                if (pkgNameAndCustomDfs.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndCustomDfs.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndAppliedDssAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndAppliedDss>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`appliedDss` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndAppliedDss pkgNameAndAppliedDss) {
                if (pkgNameAndAppliedDss.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndAppliedDss.pkgName);
                }
                supportSQLiteStatement.bindDouble(2, (double) pkgNameAndAppliedDss.appliedDss);
                if (pkgNameAndAppliedDss.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndAppliedDss.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndAppliedCpuGpuLevelAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndAppliedCpuGpuLevel>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`appliedCpuLevel` = ?,`appliedGpuLevel` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndAppliedCpuGpuLevel pkgNameAndAppliedCpuGpuLevel) {
                if (pkgNameAndAppliedCpuGpuLevel.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndAppliedCpuGpuLevel.pkgName);
                }
                supportSQLiteStatement.bindLong(2, (long) pkgNameAndAppliedCpuGpuLevel.appliedCpuLevel);
                supportSQLiteStatement.bindLong(3, (long) pkgNameAndAppliedCpuGpuLevel.appliedGpuLevel);
                if (pkgNameAndAppliedCpuGpuLevel.pkgName == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, pkgNameAndAppliedCpuGpuLevel.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndVrrMaxValueAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndVrrMaxValue>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`vrrMaxValue` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndVrrMaxValue pkgNameAndVrrMaxValue) {
                if (pkgNameAndVrrMaxValue.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndVrrMaxValue.pkgName);
                }
                supportSQLiteStatement.bindLong(2, (long) pkgNameAndVrrMaxValue.vrrMaxValue);
                if (pkgNameAndVrrMaxValue.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndVrrMaxValue.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndVrrMinValueAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndVrrMinValue>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`vrrMinValue` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndVrrMinValue pkgNameAndVrrMinValue) {
                if (pkgNameAndVrrMinValue.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndVrrMinValue.pkgName);
                }
                supportSQLiteStatement.bindLong(2, (long) pkgNameAndVrrMinValue.vrrMinValue);
                if (pkgNameAndVrrMinValue.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndVrrMinValue.pkgName);
                }
            }
        };
        this.__updateAdapterOfPkgNameAndTspPolicyAsPackage = new EntityDeletionOrUpdateAdapter<Package.PkgNameAndTspPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Package` SET `pkgName` = ?,`tspPolicy` = ? WHERE `pkgName` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Package.PkgNameAndTspPolicy pkgNameAndTspPolicy) {
                if (pkgNameAndTspPolicy.pkgName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, pkgNameAndTspPolicy.pkgName);
                }
                if (pkgNameAndTspPolicy.tspPolicy == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, pkgNameAndTspPolicy.tspPolicy);
                }
                if (pkgNameAndTspPolicy.pkgName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, pkgNameAndTspPolicy.pkgName);
                }
            }
        };
        this.__preparedStmtOf_removePkg = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM Package WHERE pkgName = ?";
            }
        };
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(Package... packageArr) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfPackage.insert((T[]) packageArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _insertOrUpdate(List<Package> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfPackage.insert(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public int _removePkgList(List<Package> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            int handleMultiple = this.__deletionAdapterOfPackage.handleMultiple(list) + 0;
            this.__db.setTransactionSuccessful();
            return handleMultiple;
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setCategoryCode(Package.PkgNameAndCategoryCode pkgNameAndCategoryCode) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndCategoryCodeAsPackage.handle(pkgNameAndCategoryCode);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDefaultCpuLevel(Package.PkgNameAndDefaultCpuLevel pkgNameAndDefaultCpuLevel) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndDefaultCpuLevelAsPackage.handle(pkgNameAndDefaultCpuLevel);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDefaultGpuLevel(Package.PkgNameAndDefaultGpuLevel pkgNameAndDefaultGpuLevel) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndDefaultGpuLevelAsPackage.handle(pkgNameAndDefaultGpuLevel);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setShiftTemperature(Package.PkgNameAndShiftTemperature pkgNameAndShiftTemperature) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndShiftTemperatureAsPackage.handle(pkgNameAndShiftTemperature);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmPolicy(Package.PkgNameAndIpmPolicy pkgNameAndIpmPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndIpmPolicyAsPackage.handle(pkgNameAndIpmPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setSiopModePolicy(Package.PkgNameAndSiopModePolicy pkgNameAndSiopModePolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndSiopModePolicyAsPackage.handle(pkgNameAndSiopModePolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setVersionInfo(Package.PkgNameAndVersionInfo pkgNameAndVersionInfo) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndVersionInfoAsPackage.handle(pkgNameAndVersionInfo);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setCustomDfs(Package.PkgNameAndCustomDfs pkgNameAndCustomDfs) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndCustomDfsAsPackage.handle(pkgNameAndCustomDfs);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setAppliedDss(Package.PkgNameAndAppliedDss pkgNameAndAppliedDss) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndAppliedDssAsPackage.handle(pkgNameAndAppliedDss);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setAppliedCpuGpuLevel(Package.PkgNameAndAppliedCpuGpuLevel pkgNameAndAppliedCpuGpuLevel) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndAppliedCpuGpuLevelAsPackage.handle(pkgNameAndAppliedCpuGpuLevel);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setVrrMaxValue(Package.PkgNameAndVrrMaxValue pkgNameAndVrrMaxValue) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndVrrMaxValueAsPackage.handle(pkgNameAndVrrMaxValue);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setVrrMinValue(Package.PkgNameAndVrrMinValue pkgNameAndVrrMinValue) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndVrrMinValueAsPackage.handle(pkgNameAndVrrMinValue);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setTspPolicy(Package.PkgNameAndTspPolicy pkgNameAndTspPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPkgNameAndTspPolicyAsPackage.handle(pkgNameAndTspPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public int _removePkg(String str) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOf_removePkg.acquire();
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOf_removePkg.release(acquire);
        }
    }

    public List<String> getAllPkgNameList() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT pkgName FROM Package", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public Package getPackage(String str) {
        RoomSQLiteQuery roomSQLiteQuery;
        Package packageR;
        String str2 = str;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM Package WHERE pkgName = ?", 1);
        if (str2 == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str2);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "categoryCode");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "installedUserIds");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "defaultSetBy");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "pkgAddedTime");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "serverPkgUpdatedTime");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "eachModeTargetShortSide");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDss");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "customResolutionMode");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "customDss");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDfs");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "customDfsMode");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "customDfs");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "defaultCpuLevel");
            roomSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "defaultGpuLevel");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "governorSettings");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "shiftTemperature");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "ipmPolicy");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "siopModePolicy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "customSiopMode");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "gameSdkSettings");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "sosPolicy");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "resumeBoostPolicy");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "launchBoostPolicy");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "wifiQosPolicy");
                int columnIndexOrThrow26 = CursorUtil.getColumnIndexOrThrow(query, "subscriberList");
                int columnIndexOrThrow27 = CursorUtil.getColumnIndexOrThrow(query, "gfiPolicy");
                int columnIndexOrThrow28 = CursorUtil.getColumnIndexOrThrow(query, "tspPolicy");
                int columnIndexOrThrow29 = CursorUtil.getColumnIndexOrThrow(query, "appliedDss");
                int columnIndexOrThrow30 = CursorUtil.getColumnIndexOrThrow(query, "appliedCpuLevel");
                int columnIndexOrThrow31 = CursorUtil.getColumnIndexOrThrow(query, "appliedGpuLevel");
                int columnIndexOrThrow32 = CursorUtil.getColumnIndexOrThrow(query, "versionName");
                int columnIndexOrThrow33 = CursorUtil.getColumnIndexOrThrow(query, "versionCode");
                int columnIndexOrThrow34 = CursorUtil.getColumnIndexOrThrow(query, "vrrMaxValue");
                int columnIndexOrThrow35 = CursorUtil.getColumnIndexOrThrow(query, "vrrMinValue");
                int columnIndexOrThrow36 = CursorUtil.getColumnIndexOrThrow(query, "drrAllowed");
                int columnIndexOrThrow37 = CursorUtil.getColumnIndexOrThrow(query, "targetGroupName");
                if (query.moveToFirst()) {
                    int i = columnIndexOrThrow37;
                    Package packageR2 = new Package();
                    packageR2.pkgName = query.getString(columnIndexOrThrow);
                    packageR2.categoryCode = query.getString(columnIndexOrThrow2);
                    packageR2.installedUserIds = query.getString(columnIndexOrThrow3);
                    packageR2.defaultSetBy = query.getInt(columnIndexOrThrow4);
                    packageR2.pkgAddedTime = query.getLong(columnIndexOrThrow5);
                    packageR2.serverPkgUpdatedTime = query.getLong(columnIndexOrThrow6);
                    packageR2.setEachModeTargetShortSide(query.getString(columnIndexOrThrow7));
                    packageR2.setEachModeDss(query.getString(columnIndexOrThrow8));
                    packageR2.setCustomResolutionMode(query.getInt(columnIndexOrThrow9));
                    packageR2.setCustomDss(query.getFloat(columnIndexOrThrow10));
                    packageR2.setEachModeDfs(query.getString(columnIndexOrThrow11));
                    packageR2.setCustomDfsMode(query.getInt(columnIndexOrThrow12));
                    packageR2.setCustomDfs(query.getFloat(columnIndexOrThrow13));
                    packageR2.defaultCpuLevel = query.getInt(columnIndexOrThrow14);
                    packageR2.defaultGpuLevel = query.getInt(columnIndexOrThrow15);
                    packageR2.governorSettings = query.getString(columnIndexOrThrow16);
                    packageR2.shiftTemperature = query.getInt(columnIndexOrThrow17);
                    packageR2.ipmPolicy = query.getString(columnIndexOrThrow18);
                    packageR2.siopModePolicy = query.getString(columnIndexOrThrow19);
                    packageR2.customSiopMode = query.getInt(columnIndexOrThrow20);
                    packageR2.gameSdkSettings = query.getString(columnIndexOrThrow21);
                    packageR2.sosPolicy = query.getString(columnIndexOrThrow22);
                    packageR2.resumeBoostPolicy = query.getString(columnIndexOrThrow23);
                    packageR2.launchBoostPolicy = query.getString(columnIndexOrThrow24);
                    packageR2.wifiQosPolicy = query.getString(columnIndexOrThrow25);
                    packageR2.subscriberList = query.getString(columnIndexOrThrow26);
                    packageR2.gfiPolicy = query.getString(columnIndexOrThrow27);
                    packageR2.tspPolicy = query.getString(columnIndexOrThrow28);
                    packageR2.appliedDss = query.getFloat(columnIndexOrThrow29);
                    packageR2.appliedCpuLevel = query.getInt(columnIndexOrThrow30);
                    packageR2.appliedGpuLevel = query.getInt(columnIndexOrThrow31);
                    packageR2.versionName = query.getString(columnIndexOrThrow32);
                    packageR2.versionCode = query.getLong(columnIndexOrThrow33);
                    packageR2.vrrMaxValue = query.getInt(columnIndexOrThrow34);
                    packageR2.vrrMinValue = query.getInt(columnIndexOrThrow35);
                    packageR2.drrAllowed = query.getInt(columnIndexOrThrow36);
                    packageR2.targetGroupName = query.getString(i);
                    packageR = packageR2;
                } else {
                    packageR = null;
                }
                query.close();
                roomSQLiteQuery.release();
                return packageR;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<Package> getPackageListByCategory(String str) {
        RoomSQLiteQuery roomSQLiteQuery;
        String str2 = str;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM Package WHERE categoryCode = ?", 1);
        if (str2 == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str2);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "categoryCode");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "installedUserIds");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "defaultSetBy");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "pkgAddedTime");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "serverPkgUpdatedTime");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "eachModeTargetShortSide");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDss");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "customResolutionMode");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "customDss");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDfs");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "customDfsMode");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "customDfs");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "defaultCpuLevel");
            roomSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "defaultGpuLevel");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "governorSettings");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "shiftTemperature");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "ipmPolicy");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "siopModePolicy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "customSiopMode");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "gameSdkSettings");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "sosPolicy");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "resumeBoostPolicy");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "launchBoostPolicy");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "wifiQosPolicy");
                int columnIndexOrThrow26 = CursorUtil.getColumnIndexOrThrow(query, "subscriberList");
                int columnIndexOrThrow27 = CursorUtil.getColumnIndexOrThrow(query, "gfiPolicy");
                int columnIndexOrThrow28 = CursorUtil.getColumnIndexOrThrow(query, "tspPolicy");
                int columnIndexOrThrow29 = CursorUtil.getColumnIndexOrThrow(query, "appliedDss");
                int columnIndexOrThrow30 = CursorUtil.getColumnIndexOrThrow(query, "appliedCpuLevel");
                int columnIndexOrThrow31 = CursorUtil.getColumnIndexOrThrow(query, "appliedGpuLevel");
                int columnIndexOrThrow32 = CursorUtil.getColumnIndexOrThrow(query, "versionName");
                int columnIndexOrThrow33 = CursorUtil.getColumnIndexOrThrow(query, "versionCode");
                int columnIndexOrThrow34 = CursorUtil.getColumnIndexOrThrow(query, "vrrMaxValue");
                int columnIndexOrThrow35 = CursorUtil.getColumnIndexOrThrow(query, "vrrMinValue");
                int columnIndexOrThrow36 = CursorUtil.getColumnIndexOrThrow(query, "drrAllowed");
                int columnIndexOrThrow37 = CursorUtil.getColumnIndexOrThrow(query, "targetGroupName");
                int i = columnIndexOrThrow14;
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    Package packageR = new Package();
                    ArrayList arrayList2 = arrayList;
                    packageR.pkgName = query.getString(columnIndexOrThrow);
                    packageR.categoryCode = query.getString(columnIndexOrThrow2);
                    packageR.installedUserIds = query.getString(columnIndexOrThrow3);
                    packageR.defaultSetBy = query.getInt(columnIndexOrThrow4);
                    int i2 = columnIndexOrThrow2;
                    int i3 = columnIndexOrThrow3;
                    packageR.pkgAddedTime = query.getLong(columnIndexOrThrow5);
                    packageR.serverPkgUpdatedTime = query.getLong(columnIndexOrThrow6);
                    packageR.setEachModeTargetShortSide(query.getString(columnIndexOrThrow7));
                    packageR.setEachModeDss(query.getString(columnIndexOrThrow8));
                    packageR.setCustomResolutionMode(query.getInt(columnIndexOrThrow9));
                    packageR.setCustomDss(query.getFloat(columnIndexOrThrow10));
                    packageR.setEachModeDfs(query.getString(columnIndexOrThrow11));
                    packageR.setCustomDfsMode(query.getInt(columnIndexOrThrow12));
                    packageR.setCustomDfs(query.getFloat(columnIndexOrThrow13));
                    int i4 = i;
                    packageR.defaultCpuLevel = query.getInt(i4);
                    int i5 = columnIndexOrThrow15;
                    int i6 = columnIndexOrThrow;
                    packageR.defaultGpuLevel = query.getInt(i5);
                    int i7 = columnIndexOrThrow16;
                    int i8 = i2;
                    packageR.governorSettings = query.getString(i7);
                    int i9 = columnIndexOrThrow17;
                    int i10 = i7;
                    packageR.shiftTemperature = query.getInt(i9);
                    int i11 = columnIndexOrThrow18;
                    int i12 = i9;
                    packageR.ipmPolicy = query.getString(i11);
                    int i13 = columnIndexOrThrow19;
                    int i14 = i11;
                    packageR.siopModePolicy = query.getString(i13);
                    int i15 = columnIndexOrThrow20;
                    int i16 = i13;
                    packageR.customSiopMode = query.getInt(i15);
                    int i17 = columnIndexOrThrow21;
                    int i18 = i15;
                    packageR.gameSdkSettings = query.getString(i17);
                    int i19 = columnIndexOrThrow22;
                    int i20 = i17;
                    packageR.sosPolicy = query.getString(i19);
                    int i21 = columnIndexOrThrow23;
                    int i22 = i19;
                    packageR.resumeBoostPolicy = query.getString(i21);
                    int i23 = columnIndexOrThrow24;
                    int i24 = i21;
                    packageR.launchBoostPolicy = query.getString(i23);
                    int i25 = columnIndexOrThrow25;
                    int i26 = i23;
                    packageR.wifiQosPolicy = query.getString(i25);
                    int i27 = columnIndexOrThrow26;
                    int i28 = i25;
                    packageR.subscriberList = query.getString(i27);
                    int i29 = columnIndexOrThrow27;
                    int i30 = i27;
                    packageR.gfiPolicy = query.getString(i29);
                    int i31 = columnIndexOrThrow28;
                    int i32 = i29;
                    packageR.tspPolicy = query.getString(i31);
                    int i33 = columnIndexOrThrow29;
                    int i34 = i31;
                    packageR.appliedDss = query.getFloat(i33);
                    int i35 = columnIndexOrThrow30;
                    int i36 = i33;
                    packageR.appliedCpuLevel = query.getInt(i35);
                    int i37 = columnIndexOrThrow31;
                    int i38 = i35;
                    packageR.appliedGpuLevel = query.getInt(i37);
                    int i39 = columnIndexOrThrow32;
                    int i40 = i37;
                    packageR.versionName = query.getString(i39);
                    i = i4;
                    int i41 = columnIndexOrThrow33;
                    int i42 = i5;
                    packageR.versionCode = query.getLong(i41);
                    int i43 = columnIndexOrThrow34;
                    packageR.vrrMaxValue = query.getInt(i43);
                    int i44 = i39;
                    int i45 = columnIndexOrThrow35;
                    packageR.vrrMinValue = query.getInt(i45);
                    int i46 = i41;
                    int i47 = columnIndexOrThrow36;
                    packageR.drrAllowed = query.getInt(i47);
                    columnIndexOrThrow36 = i47;
                    int i48 = columnIndexOrThrow37;
                    packageR.targetGroupName = query.getString(i48);
                    ArrayList arrayList3 = arrayList2;
                    arrayList3.add(packageR);
                    columnIndexOrThrow37 = i48;
                    arrayList = arrayList3;
                    columnIndexOrThrow = i6;
                    columnIndexOrThrow15 = i42;
                    columnIndexOrThrow33 = i46;
                    columnIndexOrThrow35 = i45;
                    columnIndexOrThrow3 = i3;
                    int i49 = i44;
                    columnIndexOrThrow34 = i43;
                    columnIndexOrThrow2 = i8;
                    columnIndexOrThrow16 = i10;
                    columnIndexOrThrow17 = i12;
                    columnIndexOrThrow18 = i14;
                    columnIndexOrThrow19 = i16;
                    columnIndexOrThrow20 = i18;
                    columnIndexOrThrow21 = i20;
                    columnIndexOrThrow22 = i22;
                    columnIndexOrThrow23 = i24;
                    columnIndexOrThrow24 = i26;
                    columnIndexOrThrow25 = i28;
                    columnIndexOrThrow26 = i30;
                    columnIndexOrThrow27 = i32;
                    columnIndexOrThrow28 = i34;
                    columnIndexOrThrow29 = i36;
                    columnIndexOrThrow30 = i38;
                    columnIndexOrThrow31 = i40;
                    columnIndexOrThrow32 = i49;
                }
                ArrayList arrayList4 = arrayList;
                query.close();
                roomSQLiteQuery.release();
                return arrayList4;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<Package> getPackageListNotSyncedWithServer() {
        RoomSQLiteQuery roomSQLiteQuery;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM Package WHERE serverPkgUpdatedTime < 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, EventPublisher.EXTRA_KEY_PKG_NAME);
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "categoryCode");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "installedUserIds");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "defaultSetBy");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "pkgAddedTime");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "serverPkgUpdatedTime");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "eachModeTargetShortSide");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDss");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "customResolutionMode");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "customDss");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDfs");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "customDfsMode");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "customDfs");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "defaultCpuLevel");
            roomSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "defaultGpuLevel");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "governorSettings");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "shiftTemperature");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "ipmPolicy");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "siopModePolicy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "customSiopMode");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "gameSdkSettings");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "sosPolicy");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "resumeBoostPolicy");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "launchBoostPolicy");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "wifiQosPolicy");
                int columnIndexOrThrow26 = CursorUtil.getColumnIndexOrThrow(query, "subscriberList");
                int columnIndexOrThrow27 = CursorUtil.getColumnIndexOrThrow(query, "gfiPolicy");
                int columnIndexOrThrow28 = CursorUtil.getColumnIndexOrThrow(query, "tspPolicy");
                int columnIndexOrThrow29 = CursorUtil.getColumnIndexOrThrow(query, "appliedDss");
                int columnIndexOrThrow30 = CursorUtil.getColumnIndexOrThrow(query, "appliedCpuLevel");
                int columnIndexOrThrow31 = CursorUtil.getColumnIndexOrThrow(query, "appliedGpuLevel");
                int columnIndexOrThrow32 = CursorUtil.getColumnIndexOrThrow(query, "versionName");
                int columnIndexOrThrow33 = CursorUtil.getColumnIndexOrThrow(query, "versionCode");
                int columnIndexOrThrow34 = CursorUtil.getColumnIndexOrThrow(query, "vrrMaxValue");
                int columnIndexOrThrow35 = CursorUtil.getColumnIndexOrThrow(query, "vrrMinValue");
                int columnIndexOrThrow36 = CursorUtil.getColumnIndexOrThrow(query, "drrAllowed");
                int columnIndexOrThrow37 = CursorUtil.getColumnIndexOrThrow(query, "targetGroupName");
                int i = columnIndexOrThrow14;
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    Package packageR = new Package();
                    ArrayList arrayList2 = arrayList;
                    packageR.pkgName = query.getString(columnIndexOrThrow);
                    packageR.categoryCode = query.getString(columnIndexOrThrow2);
                    packageR.installedUserIds = query.getString(columnIndexOrThrow3);
                    packageR.defaultSetBy = query.getInt(columnIndexOrThrow4);
                    int i2 = columnIndexOrThrow2;
                    packageR.pkgAddedTime = query.getLong(columnIndexOrThrow5);
                    packageR.serverPkgUpdatedTime = query.getLong(columnIndexOrThrow6);
                    packageR.setEachModeTargetShortSide(query.getString(columnIndexOrThrow7));
                    packageR.setEachModeDss(query.getString(columnIndexOrThrow8));
                    packageR.setCustomResolutionMode(query.getInt(columnIndexOrThrow9));
                    packageR.setCustomDss(query.getFloat(columnIndexOrThrow10));
                    packageR.setEachModeDfs(query.getString(columnIndexOrThrow11));
                    packageR.setCustomDfsMode(query.getInt(columnIndexOrThrow12));
                    packageR.setCustomDfs(query.getFloat(columnIndexOrThrow13));
                    int i3 = i;
                    packageR.defaultCpuLevel = query.getInt(i3);
                    int i4 = columnIndexOrThrow15;
                    int i5 = columnIndexOrThrow;
                    packageR.defaultGpuLevel = query.getInt(i4);
                    i = i3;
                    int i6 = columnIndexOrThrow16;
                    packageR.governorSettings = query.getString(i6);
                    columnIndexOrThrow16 = i6;
                    int i7 = columnIndexOrThrow17;
                    packageR.shiftTemperature = query.getInt(i7);
                    columnIndexOrThrow17 = i7;
                    int i8 = columnIndexOrThrow18;
                    packageR.ipmPolicy = query.getString(i8);
                    columnIndexOrThrow18 = i8;
                    int i9 = columnIndexOrThrow19;
                    packageR.siopModePolicy = query.getString(i9);
                    columnIndexOrThrow19 = i9;
                    int i10 = columnIndexOrThrow20;
                    packageR.customSiopMode = query.getInt(i10);
                    columnIndexOrThrow20 = i10;
                    int i11 = columnIndexOrThrow21;
                    packageR.gameSdkSettings = query.getString(i11);
                    columnIndexOrThrow21 = i11;
                    int i12 = columnIndexOrThrow22;
                    packageR.sosPolicy = query.getString(i12);
                    columnIndexOrThrow22 = i12;
                    int i13 = columnIndexOrThrow23;
                    packageR.resumeBoostPolicy = query.getString(i13);
                    columnIndexOrThrow23 = i13;
                    int i14 = columnIndexOrThrow24;
                    packageR.launchBoostPolicy = query.getString(i14);
                    columnIndexOrThrow24 = i14;
                    int i15 = columnIndexOrThrow25;
                    packageR.wifiQosPolicy = query.getString(i15);
                    columnIndexOrThrow25 = i15;
                    int i16 = columnIndexOrThrow26;
                    packageR.subscriberList = query.getString(i16);
                    columnIndexOrThrow26 = i16;
                    int i17 = columnIndexOrThrow27;
                    packageR.gfiPolicy = query.getString(i17);
                    columnIndexOrThrow27 = i17;
                    int i18 = columnIndexOrThrow28;
                    packageR.tspPolicy = query.getString(i18);
                    columnIndexOrThrow28 = i18;
                    int i19 = columnIndexOrThrow29;
                    packageR.appliedDss = query.getFloat(i19);
                    columnIndexOrThrow29 = i19;
                    int i20 = columnIndexOrThrow30;
                    packageR.appliedCpuLevel = query.getInt(i20);
                    columnIndexOrThrow30 = i20;
                    int i21 = columnIndexOrThrow31;
                    packageR.appliedGpuLevel = query.getInt(i21);
                    columnIndexOrThrow31 = i21;
                    int i22 = columnIndexOrThrow32;
                    packageR.versionName = query.getString(i22);
                    int i23 = columnIndexOrThrow3;
                    int i24 = columnIndexOrThrow33;
                    int i25 = i4;
                    packageR.versionCode = query.getLong(i24);
                    int i26 = columnIndexOrThrow34;
                    packageR.vrrMaxValue = query.getInt(i26);
                    int i27 = i22;
                    int i28 = columnIndexOrThrow35;
                    packageR.vrrMinValue = query.getInt(i28);
                    int i29 = i24;
                    int i30 = columnIndexOrThrow36;
                    packageR.drrAllowed = query.getInt(i30);
                    columnIndexOrThrow36 = i30;
                    int i31 = columnIndexOrThrow37;
                    packageR.targetGroupName = query.getString(i31);
                    ArrayList arrayList3 = arrayList2;
                    arrayList3.add(packageR);
                    columnIndexOrThrow37 = i31;
                    arrayList = arrayList3;
                    columnIndexOrThrow = i5;
                    columnIndexOrThrow15 = i25;
                    columnIndexOrThrow33 = i29;
                    columnIndexOrThrow35 = i28;
                    columnIndexOrThrow3 = i23;
                    columnIndexOrThrow32 = i27;
                    columnIndexOrThrow34 = i26;
                    columnIndexOrThrow2 = i2;
                }
                ArrayList arrayList4 = arrayList;
                query.close();
                roomSQLiteQuery.release();
                return arrayList4;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<String> getPkgNameListByCategory(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT pkgName FROM Package WHERE categoryCode = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getPkgCountByCategory(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(pkgName) FROM Package WHERE categoryCode = ?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        int i = 0;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                i = query.getInt(0);
            }
            return i;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
