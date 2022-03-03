package com.samsung.android.game.gos.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;

public final class GlobalDao_Impl extends GlobalDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<Global> __insertionAdapterOfGlobal;
    private final EntityInsertionAdapter<Global> __insertionAdapterOfGlobal_1;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndAutomaticUpdate> __updateAdapterOfIdAndAutomaticUpdateAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndClearBGLruNum> __updateAdapterOfIdAndClearBGLruNumAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndClearBGSurviveAppFromServer> __updateAdapterOfIdAndClearBGSurviveAppFromServerAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndDataReady> __updateAdapterOfIdAndDataReadyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndDefaultCpuLevel> __updateAdapterOfIdAndDefaultCpuLevelAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndDefaultGpuLevel> __updateAdapterOfIdAndDefaultGpuLevelAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndDeviceName> __updateAdapterOfIdAndDeviceNameAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndDfsMode> __updateAdapterOfIdAndDfsModeAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndDmaId> __updateAdapterOfIdAndDmaIdAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndDrrAllowed> __updateAdapterOfIdAndDrrAllowedAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndEachModeDfs> __updateAdapterOfIdAndEachModeDfsAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndEachModeDss> __updateAdapterOfIdAndEachModeDssAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndEachModeTargetShortSide> __updateAdapterOfIdAndEachModeTargetShortSideAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndFullyUpdateTime> __updateAdapterOfIdAndFullyUpdateTimeAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndGfiPolicy> __updateAdapterOfIdAndGfiPolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndGmsVersion> __updateAdapterOfIdAndGmsVersionAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndGosSelfUpdateStatus> __updateAdapterOfIdAndGosSelfUpdateStatusAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndGovernorSettings> __updateAdapterOfIdAndGovernorSettingsAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndInitialized> __updateAdapterOfIdAndInitializedAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmCpuBottomFreq> __updateAdapterOfIdAndIpmCpuBottomFreqAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmFlag> __updateAdapterOfIdAndIpmFlagAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmMode> __updateAdapterOfIdAndIpmModeAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmPolicy> __updateAdapterOfIdAndIpmPolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmTargetPower> __updateAdapterOfIdAndIpmTargetPowerAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmTargetTemperature> __updateAdapterOfIdAndIpmTargetTemperatureAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmTrainingVersion> __updateAdapterOfIdAndIpmTrainingVersionAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndIpmUpdateTime> __updateAdapterOfIdAndIpmUpdateTimeAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndLaunchBoostPolicy> __updateAdapterOfIdAndLaunchBoostPolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndLoggingPolicy> __updateAdapterOfIdAndLoggingPolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndPrevSiopModeByUser> __updateAdapterOfIdAndPrevSiopModeByUserAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndRegisteredDevice> __updateAdapterOfIdAndRegisteredDeviceAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndResolutionMode> __updateAdapterOfIdAndResolutionModeAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndResumeBoostPolicy> __updateAdapterOfIdAndResumeBoostPolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndRinglogPolicy> __updateAdapterOfIdAndRinglogPolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndShowLogcat> __updateAdapterOfIdAndShowLogcatAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndSiopMode> __updateAdapterOfIdAndSiopModeAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndSiopModePolicy> __updateAdapterOfIdAndSiopModePolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndSosPolicyKeyCsv> __updateAdapterOfIdAndSosPolicyKeyCsvAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndTargetServer> __updateAdapterOfIdAndTargetServerAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndTspPolicy> __updateAdapterOfIdAndTspPolicyAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndUpdateTime> __updateAdapterOfIdAndUpdateTimeAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndUuid> __updateAdapterOfIdAndUuidAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndVrr> __updateAdapterOfIdAndVrrAsGlobal;
    private final EntityDeletionOrUpdateAdapter<Global.IdAndWifiQosPolicy> __updateAdapterOfIdAndWifiQosPolicyAsGlobal;

    public GlobalDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfGlobal = new EntityInsertionAdapter<Global>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR IGNORE INTO `Global` (`id`,`initialized`,`updateTime`,`fullyUpdateTime`,`defaultCpuLevel`,`defaultGpuLevel`,`eachModeDss`,`eachModeDfs`,`resolutionMode`,`customDss`,`dfsMode`,`customDfs`,`vrrMaxValue`,`vrrMinValue`,`drrAllowed`,`ipmMode`,`ipmTargetPower`,`ipmTargetTemperature`,`ipmUpdateTime`,`ipmTrainingVersion`,`ipmCpuBottomFreq`,`ipmFlag`,`siopMode`,`eachModeTargetShortSide`,`governorSettings`,`resumeBoostPolicy`,`loggingPolicy`,`ipmPolicy`,`siopModePolicy`,`launchBoostPolicy`,`wifiQosPolicy`,`gfiPolicy`,`tspPolicy`,`ringlogPolicy`,`uuid`,`registeredDevice`,`gmsVersion`,`showLogcat`,`automaticUpdate`,`targetServer`,`deviceName`,`prevSiopModeByUser`,`sosPolicyKeyCsv`,`gosSelfUpdateStatus`,`dataReady`,`dmaId`,`clearBGLruNum`,`clearBGSurviveAppFromServer`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global global) {
                supportSQLiteStatement.bindLong(1, (long) global.id);
                supportSQLiteStatement.bindLong(2, global.initialized ? 1 : 0);
                supportSQLiteStatement.bindLong(3, global.updateTime);
                supportSQLiteStatement.bindLong(4, global.fullyUpdateTime);
                supportSQLiteStatement.bindLong(5, (long) global.defaultCpuLevel);
                supportSQLiteStatement.bindLong(6, (long) global.defaultGpuLevel);
                if (global.eachModeDss == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, global.eachModeDss);
                }
                if (global.eachModeDfs == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, global.eachModeDfs);
                }
                supportSQLiteStatement.bindLong(9, (long) global.resolutionMode);
                supportSQLiteStatement.bindDouble(10, (double) global.customDss);
                supportSQLiteStatement.bindLong(11, (long) global.dfsMode);
                supportSQLiteStatement.bindDouble(12, (double) global.customDfs);
                supportSQLiteStatement.bindLong(13, (long) global.vrrMaxValue);
                supportSQLiteStatement.bindLong(14, (long) global.vrrMinValue);
                supportSQLiteStatement.bindLong(15, (long) global.drrAllowed);
                supportSQLiteStatement.bindLong(16, (long) global.ipmMode);
                supportSQLiteStatement.bindLong(17, (long) global.ipmTargetPower);
                supportSQLiteStatement.bindLong(18, (long) global.ipmTargetTemperature);
                supportSQLiteStatement.bindLong(19, global.ipmUpdateTime);
                if (global.ipmTrainingVersion == null) {
                    supportSQLiteStatement.bindNull(20);
                } else {
                    supportSQLiteStatement.bindString(20, global.ipmTrainingVersion);
                }
                supportSQLiteStatement.bindLong(21, global.ipmCpuBottomFreq);
                if (global.ipmFlag == null) {
                    supportSQLiteStatement.bindNull(22);
                } else {
                    supportSQLiteStatement.bindString(22, global.ipmFlag);
                }
                supportSQLiteStatement.bindLong(23, (long) global.siopMode);
                if (global.eachModeTargetShortSide == null) {
                    supportSQLiteStatement.bindNull(24);
                } else {
                    supportSQLiteStatement.bindString(24, global.eachModeTargetShortSide);
                }
                if (global.governorSettings == null) {
                    supportSQLiteStatement.bindNull(25);
                } else {
                    supportSQLiteStatement.bindString(25, global.governorSettings);
                }
                if (global.resumeBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(26);
                } else {
                    supportSQLiteStatement.bindString(26, global.resumeBoostPolicy);
                }
                if (global.loggingPolicy == null) {
                    supportSQLiteStatement.bindNull(27);
                } else {
                    supportSQLiteStatement.bindString(27, global.loggingPolicy);
                }
                if (global.ipmPolicy == null) {
                    supportSQLiteStatement.bindNull(28);
                } else {
                    supportSQLiteStatement.bindString(28, global.ipmPolicy);
                }
                if (global.siopModePolicy == null) {
                    supportSQLiteStatement.bindNull(29);
                } else {
                    supportSQLiteStatement.bindString(29, global.siopModePolicy);
                }
                if (global.launchBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(30);
                } else {
                    supportSQLiteStatement.bindString(30, global.launchBoostPolicy);
                }
                if (global.wifiQosPolicy == null) {
                    supportSQLiteStatement.bindNull(31);
                } else {
                    supportSQLiteStatement.bindString(31, global.wifiQosPolicy);
                }
                if (global.gfiPolicy == null) {
                    supportSQLiteStatement.bindNull(32);
                } else {
                    supportSQLiteStatement.bindString(32, global.gfiPolicy);
                }
                if (global.tspPolicy == null) {
                    supportSQLiteStatement.bindNull(33);
                } else {
                    supportSQLiteStatement.bindString(33, global.tspPolicy);
                }
                if (global.ringlogPolicy == null) {
                    supportSQLiteStatement.bindNull(34);
                } else {
                    supportSQLiteStatement.bindString(34, global.ringlogPolicy);
                }
                if (global.uuid == null) {
                    supportSQLiteStatement.bindNull(35);
                } else {
                    supportSQLiteStatement.bindString(35, global.uuid);
                }
                supportSQLiteStatement.bindLong(36, global.registeredDevice ? 1 : 0);
                supportSQLiteStatement.bindDouble(37, (double) global.gmsVersion);
                supportSQLiteStatement.bindLong(38, global.showLogcat ? 1 : 0);
                supportSQLiteStatement.bindLong(39, global.automaticUpdate ? 1 : 0);
                supportSQLiteStatement.bindLong(40, (long) global.targetServer);
                if (global.deviceName == null) {
                    supportSQLiteStatement.bindNull(41);
                } else {
                    supportSQLiteStatement.bindString(41, global.deviceName);
                }
                supportSQLiteStatement.bindLong(42, (long) global.prevSiopModeByUser);
                if (global.sosPolicyKeyCsv == null) {
                    supportSQLiteStatement.bindNull(43);
                } else {
                    supportSQLiteStatement.bindString(43, global.sosPolicyKeyCsv);
                }
                supportSQLiteStatement.bindLong(44, global.gosSelfUpdateStatus ? 1 : 0);
                supportSQLiteStatement.bindLong(45, global.dataReady ? 1 : 0);
                if (global.dmaId == null) {
                    supportSQLiteStatement.bindNull(46);
                } else {
                    supportSQLiteStatement.bindString(46, global.dmaId);
                }
                supportSQLiteStatement.bindLong(47, (long) global.clearBGLruNum);
                if (global.clearBGSurviveAppFromServer == null) {
                    supportSQLiteStatement.bindNull(48);
                } else {
                    supportSQLiteStatement.bindString(48, global.clearBGSurviveAppFromServer);
                }
            }
        };
        this.__insertionAdapterOfGlobal_1 = new EntityInsertionAdapter<Global>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `Global` (`id`,`initialized`,`updateTime`,`fullyUpdateTime`,`defaultCpuLevel`,`defaultGpuLevel`,`eachModeDss`,`eachModeDfs`,`resolutionMode`,`customDss`,`dfsMode`,`customDfs`,`vrrMaxValue`,`vrrMinValue`,`drrAllowed`,`ipmMode`,`ipmTargetPower`,`ipmTargetTemperature`,`ipmUpdateTime`,`ipmTrainingVersion`,`ipmCpuBottomFreq`,`ipmFlag`,`siopMode`,`eachModeTargetShortSide`,`governorSettings`,`resumeBoostPolicy`,`loggingPolicy`,`ipmPolicy`,`siopModePolicy`,`launchBoostPolicy`,`wifiQosPolicy`,`gfiPolicy`,`tspPolicy`,`ringlogPolicy`,`uuid`,`registeredDevice`,`gmsVersion`,`showLogcat`,`automaticUpdate`,`targetServer`,`deviceName`,`prevSiopModeByUser`,`sosPolicyKeyCsv`,`gosSelfUpdateStatus`,`dataReady`,`dmaId`,`clearBGLruNum`,`clearBGSurviveAppFromServer`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global global) {
                supportSQLiteStatement.bindLong(1, (long) global.id);
                supportSQLiteStatement.bindLong(2, global.initialized ? 1 : 0);
                supportSQLiteStatement.bindLong(3, global.updateTime);
                supportSQLiteStatement.bindLong(4, global.fullyUpdateTime);
                supportSQLiteStatement.bindLong(5, (long) global.defaultCpuLevel);
                supportSQLiteStatement.bindLong(6, (long) global.defaultGpuLevel);
                if (global.eachModeDss == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, global.eachModeDss);
                }
                if (global.eachModeDfs == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, global.eachModeDfs);
                }
                supportSQLiteStatement.bindLong(9, (long) global.resolutionMode);
                supportSQLiteStatement.bindDouble(10, (double) global.customDss);
                supportSQLiteStatement.bindLong(11, (long) global.dfsMode);
                supportSQLiteStatement.bindDouble(12, (double) global.customDfs);
                supportSQLiteStatement.bindLong(13, (long) global.vrrMaxValue);
                supportSQLiteStatement.bindLong(14, (long) global.vrrMinValue);
                supportSQLiteStatement.bindLong(15, (long) global.drrAllowed);
                supportSQLiteStatement.bindLong(16, (long) global.ipmMode);
                supportSQLiteStatement.bindLong(17, (long) global.ipmTargetPower);
                supportSQLiteStatement.bindLong(18, (long) global.ipmTargetTemperature);
                supportSQLiteStatement.bindLong(19, global.ipmUpdateTime);
                if (global.ipmTrainingVersion == null) {
                    supportSQLiteStatement.bindNull(20);
                } else {
                    supportSQLiteStatement.bindString(20, global.ipmTrainingVersion);
                }
                supportSQLiteStatement.bindLong(21, global.ipmCpuBottomFreq);
                if (global.ipmFlag == null) {
                    supportSQLiteStatement.bindNull(22);
                } else {
                    supportSQLiteStatement.bindString(22, global.ipmFlag);
                }
                supportSQLiteStatement.bindLong(23, (long) global.siopMode);
                if (global.eachModeTargetShortSide == null) {
                    supportSQLiteStatement.bindNull(24);
                } else {
                    supportSQLiteStatement.bindString(24, global.eachModeTargetShortSide);
                }
                if (global.governorSettings == null) {
                    supportSQLiteStatement.bindNull(25);
                } else {
                    supportSQLiteStatement.bindString(25, global.governorSettings);
                }
                if (global.resumeBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(26);
                } else {
                    supportSQLiteStatement.bindString(26, global.resumeBoostPolicy);
                }
                if (global.loggingPolicy == null) {
                    supportSQLiteStatement.bindNull(27);
                } else {
                    supportSQLiteStatement.bindString(27, global.loggingPolicy);
                }
                if (global.ipmPolicy == null) {
                    supportSQLiteStatement.bindNull(28);
                } else {
                    supportSQLiteStatement.bindString(28, global.ipmPolicy);
                }
                if (global.siopModePolicy == null) {
                    supportSQLiteStatement.bindNull(29);
                } else {
                    supportSQLiteStatement.bindString(29, global.siopModePolicy);
                }
                if (global.launchBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(30);
                } else {
                    supportSQLiteStatement.bindString(30, global.launchBoostPolicy);
                }
                if (global.wifiQosPolicy == null) {
                    supportSQLiteStatement.bindNull(31);
                } else {
                    supportSQLiteStatement.bindString(31, global.wifiQosPolicy);
                }
                if (global.gfiPolicy == null) {
                    supportSQLiteStatement.bindNull(32);
                } else {
                    supportSQLiteStatement.bindString(32, global.gfiPolicy);
                }
                if (global.tspPolicy == null) {
                    supportSQLiteStatement.bindNull(33);
                } else {
                    supportSQLiteStatement.bindString(33, global.tspPolicy);
                }
                if (global.ringlogPolicy == null) {
                    supportSQLiteStatement.bindNull(34);
                } else {
                    supportSQLiteStatement.bindString(34, global.ringlogPolicy);
                }
                if (global.uuid == null) {
                    supportSQLiteStatement.bindNull(35);
                } else {
                    supportSQLiteStatement.bindString(35, global.uuid);
                }
                supportSQLiteStatement.bindLong(36, global.registeredDevice ? 1 : 0);
                supportSQLiteStatement.bindDouble(37, (double) global.gmsVersion);
                supportSQLiteStatement.bindLong(38, global.showLogcat ? 1 : 0);
                supportSQLiteStatement.bindLong(39, global.automaticUpdate ? 1 : 0);
                supportSQLiteStatement.bindLong(40, (long) global.targetServer);
                if (global.deviceName == null) {
                    supportSQLiteStatement.bindNull(41);
                } else {
                    supportSQLiteStatement.bindString(41, global.deviceName);
                }
                supportSQLiteStatement.bindLong(42, (long) global.prevSiopModeByUser);
                if (global.sosPolicyKeyCsv == null) {
                    supportSQLiteStatement.bindNull(43);
                } else {
                    supportSQLiteStatement.bindString(43, global.sosPolicyKeyCsv);
                }
                supportSQLiteStatement.bindLong(44, global.gosSelfUpdateStatus ? 1 : 0);
                supportSQLiteStatement.bindLong(45, global.dataReady ? 1 : 0);
                if (global.dmaId == null) {
                    supportSQLiteStatement.bindNull(46);
                } else {
                    supportSQLiteStatement.bindString(46, global.dmaId);
                }
                supportSQLiteStatement.bindLong(47, (long) global.clearBGLruNum);
                if (global.clearBGSurviveAppFromServer == null) {
                    supportSQLiteStatement.bindNull(48);
                } else {
                    supportSQLiteStatement.bindString(48, global.clearBGSurviveAppFromServer);
                }
            }
        };
        this.__updateAdapterOfIdAndUuidAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndUuid>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `uuid` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndUuid idAndUuid) {
                if (idAndUuid.uuid == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndUuid.uuid);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndUuid.id);
                supportSQLiteStatement.bindLong(3, (long) idAndUuid.id);
            }
        };
        this.__updateAdapterOfIdAndDataReadyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndDataReady>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `dataReady` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndDataReady idAndDataReady) {
                supportSQLiteStatement.bindLong(1, idAndDataReady.dataReady ? 1 : 0);
                supportSQLiteStatement.bindLong(2, (long) idAndDataReady.id);
                supportSQLiteStatement.bindLong(3, (long) idAndDataReady.id);
            }
        };
        this.__updateAdapterOfIdAndInitializedAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndInitialized>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `initialized` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndInitialized idAndInitialized) {
                supportSQLiteStatement.bindLong(1, idAndInitialized.initialized ? 1 : 0);
                supportSQLiteStatement.bindLong(2, (long) idAndInitialized.id);
                supportSQLiteStatement.bindLong(3, (long) idAndInitialized.id);
            }
        };
        this.__updateAdapterOfIdAndUpdateTimeAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndUpdateTime>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `updateTime` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndUpdateTime idAndUpdateTime) {
                supportSQLiteStatement.bindLong(1, idAndUpdateTime.updateTime);
                supportSQLiteStatement.bindLong(2, (long) idAndUpdateTime.id);
                supportSQLiteStatement.bindLong(3, (long) idAndUpdateTime.id);
            }
        };
        this.__updateAdapterOfIdAndFullyUpdateTimeAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndFullyUpdateTime>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `fullyUpdateTime` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndFullyUpdateTime idAndFullyUpdateTime) {
                supportSQLiteStatement.bindLong(1, idAndFullyUpdateTime.fullyUpdateTime);
                supportSQLiteStatement.bindLong(2, (long) idAndFullyUpdateTime.id);
                supportSQLiteStatement.bindLong(3, (long) idAndFullyUpdateTime.id);
            }
        };
        this.__updateAdapterOfIdAndRegisteredDeviceAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndRegisteredDevice>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `registeredDevice` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndRegisteredDevice idAndRegisteredDevice) {
                supportSQLiteStatement.bindLong(1, idAndRegisteredDevice.registeredDevice ? 1 : 0);
                supportSQLiteStatement.bindLong(2, (long) idAndRegisteredDevice.id);
                supportSQLiteStatement.bindLong(3, (long) idAndRegisteredDevice.id);
            }
        };
        this.__updateAdapterOfIdAndGmsVersionAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndGmsVersion>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `gmsVersion` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndGmsVersion idAndGmsVersion) {
                supportSQLiteStatement.bindDouble(1, (double) idAndGmsVersion.gmsVersion);
                supportSQLiteStatement.bindLong(2, (long) idAndGmsVersion.id);
                supportSQLiteStatement.bindLong(3, (long) idAndGmsVersion.id);
            }
        };
        this.__updateAdapterOfIdAndTargetServerAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndTargetServer>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `targetServer` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndTargetServer idAndTargetServer) {
                supportSQLiteStatement.bindLong(1, (long) idAndTargetServer.targetServer);
                supportSQLiteStatement.bindLong(2, (long) idAndTargetServer.id);
                supportSQLiteStatement.bindLong(3, (long) idAndTargetServer.id);
            }
        };
        this.__updateAdapterOfIdAndDeviceNameAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndDeviceName>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `deviceName` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndDeviceName idAndDeviceName) {
                if (idAndDeviceName.deviceName == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndDeviceName.deviceName);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndDeviceName.id);
                supportSQLiteStatement.bindLong(3, (long) idAndDeviceName.id);
            }
        };
        this.__updateAdapterOfIdAndClearBGLruNumAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndClearBGLruNum>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `clearBGLruNum` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndClearBGLruNum idAndClearBGLruNum) {
                supportSQLiteStatement.bindLong(1, (long) idAndClearBGLruNum.clearBGLruNum);
                supportSQLiteStatement.bindLong(2, (long) idAndClearBGLruNum.id);
                supportSQLiteStatement.bindLong(3, (long) idAndClearBGLruNum.id);
            }
        };
        this.__updateAdapterOfIdAndClearBGSurviveAppFromServerAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndClearBGSurviveAppFromServer>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `clearBGSurviveAppFromServer` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndClearBGSurviveAppFromServer idAndClearBGSurviveAppFromServer) {
                if (idAndClearBGSurviveAppFromServer.clearBGSurviveAppFromServer == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndClearBGSurviveAppFromServer.clearBGSurviveAppFromServer);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndClearBGSurviveAppFromServer.id);
                supportSQLiteStatement.bindLong(3, (long) idAndClearBGSurviveAppFromServer.id);
            }
        };
        this.__updateAdapterOfIdAndPrevSiopModeByUserAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndPrevSiopModeByUser>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `prevSiopModeByUser` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndPrevSiopModeByUser idAndPrevSiopModeByUser) {
                supportSQLiteStatement.bindLong(1, (long) idAndPrevSiopModeByUser.prevSiopModeByUser);
                supportSQLiteStatement.bindLong(2, (long) idAndPrevSiopModeByUser.id);
                supportSQLiteStatement.bindLong(3, (long) idAndPrevSiopModeByUser.id);
            }
        };
        this.__updateAdapterOfIdAndSosPolicyKeyCsvAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndSosPolicyKeyCsv>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `sosPolicyKeyCsv` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndSosPolicyKeyCsv idAndSosPolicyKeyCsv) {
                if (idAndSosPolicyKeyCsv.sosPolicyKeyCsv == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndSosPolicyKeyCsv.sosPolicyKeyCsv);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndSosPolicyKeyCsv.id);
                supportSQLiteStatement.bindLong(3, (long) idAndSosPolicyKeyCsv.id);
            }
        };
        this.__updateAdapterOfIdAndAutomaticUpdateAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndAutomaticUpdate>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `automaticUpdate` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndAutomaticUpdate idAndAutomaticUpdate) {
                supportSQLiteStatement.bindLong(1, idAndAutomaticUpdate.automaticUpdate ? 1 : 0);
                supportSQLiteStatement.bindLong(2, (long) idAndAutomaticUpdate.id);
                supportSQLiteStatement.bindLong(3, (long) idAndAutomaticUpdate.id);
            }
        };
        this.__updateAdapterOfIdAndGosSelfUpdateStatusAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndGosSelfUpdateStatus>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `gosSelfUpdateStatus` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndGosSelfUpdateStatus idAndGosSelfUpdateStatus) {
                supportSQLiteStatement.bindLong(1, idAndGosSelfUpdateStatus.gosSelfUpdateStatus ? 1 : 0);
                supportSQLiteStatement.bindLong(2, (long) idAndGosSelfUpdateStatus.id);
                supportSQLiteStatement.bindLong(3, (long) idAndGosSelfUpdateStatus.id);
            }
        };
        this.__updateAdapterOfIdAndShowLogcatAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndShowLogcat>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `showLogcat` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndShowLogcat idAndShowLogcat) {
                supportSQLiteStatement.bindLong(1, idAndShowLogcat.showLogcat ? 1 : 0);
                supportSQLiteStatement.bindLong(2, (long) idAndShowLogcat.id);
                supportSQLiteStatement.bindLong(3, (long) idAndShowLogcat.id);
            }
        };
        this.__updateAdapterOfIdAndDmaIdAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndDmaId>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `dmaId` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndDmaId idAndDmaId) {
                if (idAndDmaId.dmaId == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndDmaId.dmaId);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndDmaId.id);
                supportSQLiteStatement.bindLong(3, (long) idAndDmaId.id);
            }
        };
        this.__updateAdapterOfIdAndWifiQosPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndWifiQosPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `wifiQosPolicy` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndWifiQosPolicy idAndWifiQosPolicy) {
                if (idAndWifiQosPolicy.wifiQosPolicy == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndWifiQosPolicy.wifiQosPolicy);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndWifiQosPolicy.id);
                supportSQLiteStatement.bindLong(3, (long) idAndWifiQosPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndLaunchBoostPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndLaunchBoostPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `launchBoostPolicy` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndLaunchBoostPolicy idAndLaunchBoostPolicy) {
                if (idAndLaunchBoostPolicy.launchBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndLaunchBoostPolicy.launchBoostPolicy);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndLaunchBoostPolicy.id);
                supportSQLiteStatement.bindLong(3, (long) idAndLaunchBoostPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndSiopModePolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndSiopModePolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `siopModePolicy` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndSiopModePolicy idAndSiopModePolicy) {
                if (idAndSiopModePolicy.siopModePolicy == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndSiopModePolicy.siopModePolicy);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndSiopModePolicy.id);
                supportSQLiteStatement.bindLong(3, (long) idAndSiopModePolicy.id);
            }
        };
        this.__updateAdapterOfIdAndGfiPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndGfiPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `gfiPolicy` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndGfiPolicy idAndGfiPolicy) {
                if (idAndGfiPolicy.gfiPolicy == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndGfiPolicy.gfiPolicy);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndGfiPolicy.id);
                supportSQLiteStatement.bindLong(3, (long) idAndGfiPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndIpmPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmPolicy` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmPolicy idAndIpmPolicy) {
                if (idAndIpmPolicy.ipmPolicy == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndIpmPolicy.ipmPolicy);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndIpmPolicy.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndLoggingPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndLoggingPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `loggingPolicy` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndLoggingPolicy idAndLoggingPolicy) {
                if (idAndLoggingPolicy.loggingPolicy == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndLoggingPolicy.loggingPolicy);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndLoggingPolicy.id);
                supportSQLiteStatement.bindLong(3, (long) idAndLoggingPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndResumeBoostPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndResumeBoostPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `resumeBoostPolicy` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndResumeBoostPolicy idAndResumeBoostPolicy) {
                if (idAndResumeBoostPolicy.resumeBoostPolicy == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndResumeBoostPolicy.resumeBoostPolicy);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndResumeBoostPolicy.id);
                supportSQLiteStatement.bindLong(3, (long) idAndResumeBoostPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndGovernorSettingsAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndGovernorSettings>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `governorSettings` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndGovernorSettings idAndGovernorSettings) {
                if (idAndGovernorSettings.governorSettings == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndGovernorSettings.governorSettings);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndGovernorSettings.id);
                supportSQLiteStatement.bindLong(3, (long) idAndGovernorSettings.id);
            }
        };
        this.__updateAdapterOfIdAndEachModeTargetShortSideAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndEachModeTargetShortSide>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `eachModeTargetShortSide` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndEachModeTargetShortSide idAndEachModeTargetShortSide) {
                if (idAndEachModeTargetShortSide.eachModeTargetShortSide == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndEachModeTargetShortSide.eachModeTargetShortSide);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndEachModeTargetShortSide.id);
                supportSQLiteStatement.bindLong(3, (long) idAndEachModeTargetShortSide.id);
            }
        };
        this.__updateAdapterOfIdAndSiopModeAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndSiopMode>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `siopMode` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndSiopMode idAndSiopMode) {
                supportSQLiteStatement.bindLong(1, (long) idAndSiopMode.siopMode);
                supportSQLiteStatement.bindLong(2, (long) idAndSiopMode.id);
                supportSQLiteStatement.bindLong(3, (long) idAndSiopMode.id);
            }
        };
        this.__updateAdapterOfIdAndIpmFlagAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmFlag>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmFlag` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmFlag idAndIpmFlag) {
                if (idAndIpmFlag.ipmFlag == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndIpmFlag.ipmFlag);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndIpmFlag.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmFlag.id);
            }
        };
        this.__updateAdapterOfIdAndIpmCpuBottomFreqAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmCpuBottomFreq>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmCpuBottomFreq` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmCpuBottomFreq idAndIpmCpuBottomFreq) {
                supportSQLiteStatement.bindLong(1, idAndIpmCpuBottomFreq.ipmCpuBottomFreq);
                supportSQLiteStatement.bindLong(2, (long) idAndIpmCpuBottomFreq.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmCpuBottomFreq.id);
            }
        };
        this.__updateAdapterOfIdAndIpmTrainingVersionAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmTrainingVersion>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmTrainingVersion` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmTrainingVersion idAndIpmTrainingVersion) {
                if (idAndIpmTrainingVersion.ipmTrainingVersion == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndIpmTrainingVersion.ipmTrainingVersion);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndIpmTrainingVersion.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmTrainingVersion.id);
            }
        };
        this.__updateAdapterOfIdAndIpmUpdateTimeAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmUpdateTime>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmUpdateTime` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmUpdateTime idAndIpmUpdateTime) {
                supportSQLiteStatement.bindLong(1, idAndIpmUpdateTime.ipmUpdateTime);
                supportSQLiteStatement.bindLong(2, (long) idAndIpmUpdateTime.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmUpdateTime.id);
            }
        };
        this.__updateAdapterOfIdAndIpmTargetTemperatureAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmTargetTemperature>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmTargetTemperature` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmTargetTemperature idAndIpmTargetTemperature) {
                supportSQLiteStatement.bindLong(1, (long) idAndIpmTargetTemperature.ipmTargetTemperature);
                supportSQLiteStatement.bindLong(2, (long) idAndIpmTargetTemperature.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmTargetTemperature.id);
            }
        };
        this.__updateAdapterOfIdAndIpmTargetPowerAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmTargetPower>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmTargetPower` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmTargetPower idAndIpmTargetPower) {
                supportSQLiteStatement.bindLong(1, (long) idAndIpmTargetPower.ipmTargetPower);
                supportSQLiteStatement.bindLong(2, (long) idAndIpmTargetPower.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmTargetPower.id);
            }
        };
        this.__updateAdapterOfIdAndIpmModeAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndIpmMode>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `ipmMode` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndIpmMode idAndIpmMode) {
                supportSQLiteStatement.bindLong(1, (long) idAndIpmMode.ipmMode);
                supportSQLiteStatement.bindLong(2, (long) idAndIpmMode.id);
                supportSQLiteStatement.bindLong(3, (long) idAndIpmMode.id);
            }
        };
        this.__updateAdapterOfIdAndEachModeDfsAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndEachModeDfs>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `eachModeDfs` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndEachModeDfs idAndEachModeDfs) {
                if (idAndEachModeDfs.eachModeDfs == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndEachModeDfs.eachModeDfs);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndEachModeDfs.id);
                supportSQLiteStatement.bindLong(3, (long) idAndEachModeDfs.id);
            }
        };
        this.__updateAdapterOfIdAndEachModeDssAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndEachModeDss>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `eachModeDss` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndEachModeDss idAndEachModeDss) {
                if (idAndEachModeDss.eachModeDss == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, idAndEachModeDss.eachModeDss);
                }
                supportSQLiteStatement.bindLong(2, (long) idAndEachModeDss.id);
                supportSQLiteStatement.bindLong(3, (long) idAndEachModeDss.id);
            }
        };
        this.__updateAdapterOfIdAndDefaultCpuLevelAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndDefaultCpuLevel>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `defaultCpuLevel` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndDefaultCpuLevel idAndDefaultCpuLevel) {
                supportSQLiteStatement.bindLong(1, (long) idAndDefaultCpuLevel.defaultCpuLevel);
                supportSQLiteStatement.bindLong(2, (long) idAndDefaultCpuLevel.id);
                supportSQLiteStatement.bindLong(3, (long) idAndDefaultCpuLevel.id);
            }
        };
        this.__updateAdapterOfIdAndDefaultGpuLevelAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndDefaultGpuLevel>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `defaultGpuLevel` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndDefaultGpuLevel idAndDefaultGpuLevel) {
                supportSQLiteStatement.bindLong(1, (long) idAndDefaultGpuLevel.defaultGpuLevel);
                supportSQLiteStatement.bindLong(2, (long) idAndDefaultGpuLevel.id);
                supportSQLiteStatement.bindLong(3, (long) idAndDefaultGpuLevel.id);
            }
        };
        this.__updateAdapterOfIdAndDfsModeAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndDfsMode>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `dfsMode` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndDfsMode idAndDfsMode) {
                supportSQLiteStatement.bindLong(1, (long) idAndDfsMode.dfsMode);
                supportSQLiteStatement.bindLong(2, (long) idAndDfsMode.id);
                supportSQLiteStatement.bindLong(3, (long) idAndDfsMode.id);
            }
        };
        this.__updateAdapterOfIdAndResolutionModeAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndResolutionMode>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `resolutionMode` = ?,`id` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndResolutionMode idAndResolutionMode) {
                supportSQLiteStatement.bindLong(1, (long) idAndResolutionMode.resolutionMode);
                supportSQLiteStatement.bindLong(2, (long) idAndResolutionMode.id);
                supportSQLiteStatement.bindLong(3, (long) idAndResolutionMode.id);
            }
        };
        this.__updateAdapterOfIdAndTspPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndTspPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `id` = ?,`tspPolicy` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndTspPolicy idAndTspPolicy) {
                supportSQLiteStatement.bindLong(1, (long) idAndTspPolicy.id);
                if (idAndTspPolicy.tspPolicy == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, idAndTspPolicy.tspPolicy);
                }
                supportSQLiteStatement.bindLong(3, (long) idAndTspPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndRinglogPolicyAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndRinglogPolicy>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `id` = ?,`ringlogPolicy` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndRinglogPolicy idAndRinglogPolicy) {
                supportSQLiteStatement.bindLong(1, (long) idAndRinglogPolicy.id);
                if (idAndRinglogPolicy.ringlogPolicy == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, idAndRinglogPolicy.ringlogPolicy);
                }
                supportSQLiteStatement.bindLong(3, (long) idAndRinglogPolicy.id);
            }
        };
        this.__updateAdapterOfIdAndVrrAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndVrr>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `id` = ?,`vrrMaxValue` = ?,`vrrMinValue` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndVrr idAndVrr) {
                supportSQLiteStatement.bindLong(1, (long) idAndVrr.id);
                supportSQLiteStatement.bindLong(2, (long) idAndVrr.vrrMaxValue);
                supportSQLiteStatement.bindLong(3, (long) idAndVrr.vrrMinValue);
                supportSQLiteStatement.bindLong(4, (long) idAndVrr.id);
            }
        };
        this.__updateAdapterOfIdAndDrrAllowedAsGlobal = new EntityDeletionOrUpdateAdapter<Global.IdAndDrrAllowed>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Global` SET `id` = ?,`drrAllowed` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Global.IdAndDrrAllowed idAndDrrAllowed) {
                supportSQLiteStatement.bindLong(1, (long) idAndDrrAllowed.id);
                supportSQLiteStatement.bindLong(2, (long) idAndDrrAllowed.drrAllowed);
                supportSQLiteStatement.bindLong(3, (long) idAndDrrAllowed.id);
            }
        };
    }

    /* access modifiers changed from: protected */
    public long _insert(Global global) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfGlobal.insertAndReturnId(global);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public long _insertOrUpdate(Global global) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfGlobal_1.insertAndReturnId(global);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setUUID(Global.IdAndUuid idAndUuid) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndUuidAsGlobal.handle(idAndUuid);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDataReady(Global.IdAndDataReady idAndDataReady) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndDataReadyAsGlobal.handle(idAndDataReady);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setInitialized(Global.IdAndInitialized idAndInitialized) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndInitializedAsGlobal.handle(idAndInitialized);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setUpdateTime(Global.IdAndUpdateTime idAndUpdateTime) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndUpdateTimeAsGlobal.handle(idAndUpdateTime);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setFullyUpdateTime(Global.IdAndFullyUpdateTime idAndFullyUpdateTime) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndFullyUpdateTimeAsGlobal.handle(idAndFullyUpdateTime);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setRegisteredDevice(Global.IdAndRegisteredDevice idAndRegisteredDevice) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndRegisteredDeviceAsGlobal.handle(idAndRegisteredDevice);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setGmsVersion(Global.IdAndGmsVersion idAndGmsVersion) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndGmsVersionAsGlobal.handle(idAndGmsVersion);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setTargetServer(Global.IdAndTargetServer idAndTargetServer) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndTargetServerAsGlobal.handle(idAndTargetServer);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDeviceName(Global.IdAndDeviceName idAndDeviceName) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndDeviceNameAsGlobal.handle(idAndDeviceName);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setClearBGLruNum(Global.IdAndClearBGLruNum idAndClearBGLruNum) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndClearBGLruNumAsGlobal.handle(idAndClearBGLruNum);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setClearBGSurviveAppFromServer(Global.IdAndClearBGSurviveAppFromServer idAndClearBGSurviveAppFromServer) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndClearBGSurviveAppFromServerAsGlobal.handle(idAndClearBGSurviveAppFromServer);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setPrevSiopModeByUser(Global.IdAndPrevSiopModeByUser idAndPrevSiopModeByUser) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndPrevSiopModeByUserAsGlobal.handle(idAndPrevSiopModeByUser);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setSosPolicyKeyCsv(Global.IdAndSosPolicyKeyCsv idAndSosPolicyKeyCsv) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndSosPolicyKeyCsvAsGlobal.handle(idAndSosPolicyKeyCsv);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setAutomaticUpdate(Global.IdAndAutomaticUpdate idAndAutomaticUpdate) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndAutomaticUpdateAsGlobal.handle(idAndAutomaticUpdate);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setGosSelfUpdateStatus(Global.IdAndGosSelfUpdateStatus idAndGosSelfUpdateStatus) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndGosSelfUpdateStatusAsGlobal.handle(idAndGosSelfUpdateStatus);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setShowLogcat(Global.IdAndShowLogcat idAndShowLogcat) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndShowLogcatAsGlobal.handle(idAndShowLogcat);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDmaId(Global.IdAndDmaId idAndDmaId) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndDmaIdAsGlobal.handle(idAndDmaId);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setWifiQosPolicy(Global.IdAndWifiQosPolicy idAndWifiQosPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndWifiQosPolicyAsGlobal.handle(idAndWifiQosPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setLaunchBoostPolicy(Global.IdAndLaunchBoostPolicy idAndLaunchBoostPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndLaunchBoostPolicyAsGlobal.handle(idAndLaunchBoostPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setSiopModePolicy(Global.IdAndSiopModePolicy idAndSiopModePolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndSiopModePolicyAsGlobal.handle(idAndSiopModePolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setGfiPolicy(Global.IdAndGfiPolicy idAndGfiPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndGfiPolicyAsGlobal.handle(idAndGfiPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmPolicy(Global.IdAndIpmPolicy idAndIpmPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmPolicyAsGlobal.handle(idAndIpmPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setLoggingPolicy(Global.IdAndLoggingPolicy idAndLoggingPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndLoggingPolicyAsGlobal.handle(idAndLoggingPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setResumeBoostPolicy(Global.IdAndResumeBoostPolicy idAndResumeBoostPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndResumeBoostPolicyAsGlobal.handle(idAndResumeBoostPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setGovernorSettings(Global.IdAndGovernorSettings idAndGovernorSettings) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndGovernorSettingsAsGlobal.handle(idAndGovernorSettings);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setEachModeTargetShortSide(Global.IdAndEachModeTargetShortSide idAndEachModeTargetShortSide) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndEachModeTargetShortSideAsGlobal.handle(idAndEachModeTargetShortSide);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setSiopMode(Global.IdAndSiopMode idAndSiopMode) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndSiopModeAsGlobal.handle(idAndSiopMode);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmFlag(Global.IdAndIpmFlag idAndIpmFlag) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmFlagAsGlobal.handle(idAndIpmFlag);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmCpuBottomFreq(Global.IdAndIpmCpuBottomFreq idAndIpmCpuBottomFreq) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmCpuBottomFreqAsGlobal.handle(idAndIpmCpuBottomFreq);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmTrainingVersion(Global.IdAndIpmTrainingVersion idAndIpmTrainingVersion) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmTrainingVersionAsGlobal.handle(idAndIpmTrainingVersion);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmUpdateTime(Global.IdAndIpmUpdateTime idAndIpmUpdateTime) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmUpdateTimeAsGlobal.handle(idAndIpmUpdateTime);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmTargetTemperature(Global.IdAndIpmTargetTemperature idAndIpmTargetTemperature) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmTargetTemperatureAsGlobal.handle(idAndIpmTargetTemperature);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmTargetPower(Global.IdAndIpmTargetPower idAndIpmTargetPower) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmTargetPowerAsGlobal.handle(idAndIpmTargetPower);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setIpmMode(Global.IdAndIpmMode idAndIpmMode) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndIpmModeAsGlobal.handle(idAndIpmMode);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setEachModeDfs(Global.IdAndEachModeDfs idAndEachModeDfs) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndEachModeDfsAsGlobal.handle(idAndEachModeDfs);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setEachModeDss(Global.IdAndEachModeDss idAndEachModeDss) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndEachModeDssAsGlobal.handle(idAndEachModeDss);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDefaultCpuLevel(Global.IdAndDefaultCpuLevel idAndDefaultCpuLevel) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndDefaultCpuLevelAsGlobal.handle(idAndDefaultCpuLevel);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDefaultGpuLevel(Global.IdAndDefaultGpuLevel idAndDefaultGpuLevel) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndDefaultGpuLevelAsGlobal.handle(idAndDefaultGpuLevel);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDfsMode(Global.IdAndDfsMode idAndDfsMode) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndDfsModeAsGlobal.handle(idAndDfsMode);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setResolutionMode(Global.IdAndResolutionMode idAndResolutionMode) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndResolutionModeAsGlobal.handle(idAndResolutionMode);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setTspPolicy(Global.IdAndTspPolicy idAndTspPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndTspPolicyAsGlobal.handle(idAndTspPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setRinglogPolicy(Global.IdAndRinglogPolicy idAndRinglogPolicy) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndRinglogPolicyAsGlobal.handle(idAndRinglogPolicy);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setVrrValues(Global.IdAndVrr idAndVrr) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndVrrAsGlobal.handle(idAndVrr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void _setDrrAllowed(Global.IdAndDrrAllowed idAndDrrAllowed) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfIdAndDrrAllowedAsGlobal.handle(idAndDrrAllowed);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public Global get() {
        RoomSQLiteQuery roomSQLiteQuery;
        Global global;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "initialized");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "updateTime");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "fullyUpdateTime");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "defaultCpuLevel");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "defaultGpuLevel");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDss");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "eachModeDfs");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "resolutionMode");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "customDss");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "dfsMode");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "customDfs");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "vrrMaxValue");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "vrrMinValue");
            roomSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "drrAllowed");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "ipmMode");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "ipmTargetPower");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "ipmTargetTemperature");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "ipmUpdateTime");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "ipmTrainingVersion");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "ipmCpuBottomFreq");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "ipmFlag");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "siopMode");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "eachModeTargetShortSide");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "governorSettings");
                int columnIndexOrThrow26 = CursorUtil.getColumnIndexOrThrow(query, "resumeBoostPolicy");
                int columnIndexOrThrow27 = CursorUtil.getColumnIndexOrThrow(query, "loggingPolicy");
                int columnIndexOrThrow28 = CursorUtil.getColumnIndexOrThrow(query, "ipmPolicy");
                int columnIndexOrThrow29 = CursorUtil.getColumnIndexOrThrow(query, "siopModePolicy");
                int columnIndexOrThrow30 = CursorUtil.getColumnIndexOrThrow(query, "launchBoostPolicy");
                int columnIndexOrThrow31 = CursorUtil.getColumnIndexOrThrow(query, "wifiQosPolicy");
                int columnIndexOrThrow32 = CursorUtil.getColumnIndexOrThrow(query, "gfiPolicy");
                int columnIndexOrThrow33 = CursorUtil.getColumnIndexOrThrow(query, "tspPolicy");
                int columnIndexOrThrow34 = CursorUtil.getColumnIndexOrThrow(query, "ringlogPolicy");
                int columnIndexOrThrow35 = CursorUtil.getColumnIndexOrThrow(query, GosInterface.KeyName.UUID);
                int columnIndexOrThrow36 = CursorUtil.getColumnIndexOrThrow(query, "registeredDevice");
                int columnIndexOrThrow37 = CursorUtil.getColumnIndexOrThrow(query, "gmsVersion");
                int columnIndexOrThrow38 = CursorUtil.getColumnIndexOrThrow(query, "showLogcat");
                int columnIndexOrThrow39 = CursorUtil.getColumnIndexOrThrow(query, "automaticUpdate");
                int columnIndexOrThrow40 = CursorUtil.getColumnIndexOrThrow(query, "targetServer");
                int columnIndexOrThrow41 = CursorUtil.getColumnIndexOrThrow(query, "deviceName");
                int columnIndexOrThrow42 = CursorUtil.getColumnIndexOrThrow(query, "prevSiopModeByUser");
                int columnIndexOrThrow43 = CursorUtil.getColumnIndexOrThrow(query, "sosPolicyKeyCsv");
                int columnIndexOrThrow44 = CursorUtil.getColumnIndexOrThrow(query, "gosSelfUpdateStatus");
                int columnIndexOrThrow45 = CursorUtil.getColumnIndexOrThrow(query, "dataReady");
                int columnIndexOrThrow46 = CursorUtil.getColumnIndexOrThrow(query, "dmaId");
                int columnIndexOrThrow47 = CursorUtil.getColumnIndexOrThrow(query, "clearBGLruNum");
                int columnIndexOrThrow48 = CursorUtil.getColumnIndexOrThrow(query, "clearBGSurviveAppFromServer");
                if (query.moveToFirst()) {
                    int i = columnIndexOrThrow48;
                    Global global2 = new Global();
                    global2.id = query.getInt(columnIndexOrThrow);
                    global2.initialized = query.getInt(columnIndexOrThrow2) != 0;
                    global2.updateTime = query.getLong(columnIndexOrThrow3);
                    global2.fullyUpdateTime = query.getLong(columnIndexOrThrow4);
                    global2.defaultCpuLevel = query.getInt(columnIndexOrThrow5);
                    global2.defaultGpuLevel = query.getInt(columnIndexOrThrow6);
                    global2.eachModeDss = query.getString(columnIndexOrThrow7);
                    global2.eachModeDfs = query.getString(columnIndexOrThrow8);
                    global2.resolutionMode = query.getInt(columnIndexOrThrow9);
                    global2.customDss = query.getFloat(columnIndexOrThrow10);
                    global2.dfsMode = query.getInt(columnIndexOrThrow11);
                    global2.customDfs = query.getFloat(columnIndexOrThrow12);
                    global2.vrrMaxValue = query.getInt(columnIndexOrThrow13);
                    global2.vrrMinValue = query.getInt(columnIndexOrThrow14);
                    global2.drrAllowed = query.getInt(columnIndexOrThrow15);
                    global2.ipmMode = query.getInt(columnIndexOrThrow16);
                    global2.ipmTargetPower = query.getInt(columnIndexOrThrow17);
                    global2.ipmTargetTemperature = query.getInt(columnIndexOrThrow18);
                    global2.ipmUpdateTime = query.getLong(columnIndexOrThrow19);
                    global2.ipmTrainingVersion = query.getString(columnIndexOrThrow20);
                    global2.ipmCpuBottomFreq = query.getLong(columnIndexOrThrow21);
                    global2.ipmFlag = query.getString(columnIndexOrThrow22);
                    global2.siopMode = query.getInt(columnIndexOrThrow23);
                    global2.eachModeTargetShortSide = query.getString(columnIndexOrThrow24);
                    global2.governorSettings = query.getString(columnIndexOrThrow25);
                    global2.resumeBoostPolicy = query.getString(columnIndexOrThrow26);
                    global2.loggingPolicy = query.getString(columnIndexOrThrow27);
                    global2.ipmPolicy = query.getString(columnIndexOrThrow28);
                    global2.siopModePolicy = query.getString(columnIndexOrThrow29);
                    global2.launchBoostPolicy = query.getString(columnIndexOrThrow30);
                    global2.wifiQosPolicy = query.getString(columnIndexOrThrow31);
                    global2.gfiPolicy = query.getString(columnIndexOrThrow32);
                    global2.tspPolicy = query.getString(columnIndexOrThrow33);
                    global2.ringlogPolicy = query.getString(columnIndexOrThrow34);
                    global2.uuid = query.getString(columnIndexOrThrow35);
                    global2.registeredDevice = query.getInt(columnIndexOrThrow36) != 0;
                    global2.gmsVersion = query.getFloat(columnIndexOrThrow37);
                    global2.showLogcat = query.getInt(columnIndexOrThrow38) != 0;
                    global2.automaticUpdate = query.getInt(columnIndexOrThrow39) != 0;
                    global2.targetServer = query.getInt(columnIndexOrThrow40);
                    global2.deviceName = query.getString(columnIndexOrThrow41);
                    global2.prevSiopModeByUser = query.getInt(columnIndexOrThrow42);
                    global2.sosPolicyKeyCsv = query.getString(columnIndexOrThrow43);
                    global2.gosSelfUpdateStatus = query.getInt(columnIndexOrThrow44) != 0;
                    global2.dataReady = query.getInt(columnIndexOrThrow45) != 0;
                    global2.dmaId = query.getString(columnIndexOrThrow46);
                    global2.clearBGLruNum = query.getInt(columnIndexOrThrow47);
                    global2.clearBGSurviveAppFromServer = query.getString(i);
                    global = global2;
                } else {
                    global = null;
                }
                query.close();
                roomSQLiteQuery.release();
                return global;
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

    public String getUUID() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT uuid FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isDataReady() {
        boolean z = false;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT dataReady FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isInitialized() {
        boolean z = false;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT initialized FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public long getUpdateTime() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT updateTime FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public long getFullyUpdateTime() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT fullyUpdateTime FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isRegisteredDevice() {
        boolean z = false;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT registeredDevice FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public float getGmsVersion() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT gmsVersion FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getFloat(0) : 0.0f;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getTargetServer() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT targetServer FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public String getDeviceName() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT deviceName FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getClearBGLruNum() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT clearBGLruNum FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public String getClearBGSurviveAppFromServer() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT clearBGSurviveAppFromServer FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getPrevSiopModeByUser() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT prevSiopModeByUser FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public boolean isAutomaticUpdate() {
        boolean z = false;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT automaticUpdate FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isGosSelfUpdateStatus() {
        boolean z = false;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT gosSelfUpdateStatus FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isShowLogcat() {
        boolean z = false;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT showLogcat FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getDmaId() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT dmaId FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getWifiQosPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT wifiQosPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getLaunchBoostPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT launchBoostPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getSiopModePolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT siopModePolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getGfiPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT gfiPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getIpmPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getLoggingPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT loggingPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getResumeBoostPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT resumeBoostPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getGovernorSettings() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT governorSettings FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getEachModeTargetShortSide() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT eachModeTargetShortSide FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getSiopMode() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT siopMode FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public String getIpmFlag() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmFlag FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public long getIpmCpuBottomFreq() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmCpuBottomFreq FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getIpmTrainingVersion() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmTrainingVersion FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public long getIpmUpdateTime() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmUpdateTime FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getIpmTargetTemperature() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmTargetTemperature FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public int getIpmTargetPower() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmTargetPower FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public int getIpmMode() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ipmMode FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public String getEachModeDfs() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT eachModeDfs FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getEachModeDss() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT eachModeDss FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getDefaultCpuLevel() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT defaultCpuLevel FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public int getDefaultGpuLevel() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT defaultGpuLevel FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public int getDfsMode() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT dfsMode FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public int getResolutionMode() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT resolutionMode FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public String getTspPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT tspPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public float getCustomDss() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT customDss FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getFloat(0) : 0.0f;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public float getCustomDfs() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT customDfs FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.getFloat(0) : 0.0f;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public String getRinglogPolicy() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ringlogPolicy FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getVrrMaxValue() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT vrrMaxValue FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public int getVrrMinValue() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT vrrMinValue FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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

    public int getDrrAllowed() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT drrAllowed FROM Global WHERE id = 1", 0);
        this.__db.assertNotSuspendingTransaction();
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
