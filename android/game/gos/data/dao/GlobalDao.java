package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.util.GosLog;

public abstract class GlobalDao {
    private static final String LOG_TAG = GlobalDao.class.getSimpleName();

    /* access modifiers changed from: protected */
    public abstract long _insert(Global global);

    /* access modifiers changed from: protected */
    public abstract long _insertOrUpdate(Global global);

    /* access modifiers changed from: protected */
    public abstract void _setAutomaticUpdate(Global.IdAndAutomaticUpdate idAndAutomaticUpdate);

    /* access modifiers changed from: protected */
    public abstract void _setClearBGLruNum(Global.IdAndClearBGLruNum idAndClearBGLruNum);

    /* access modifiers changed from: protected */
    public abstract void _setClearBGSurviveAppFromServer(Global.IdAndClearBGSurviveAppFromServer idAndClearBGSurviveAppFromServer);

    /* access modifiers changed from: protected */
    public abstract void _setDataReady(Global.IdAndDataReady idAndDataReady);

    /* access modifiers changed from: protected */
    public abstract void _setDefaultCpuLevel(Global.IdAndDefaultCpuLevel idAndDefaultCpuLevel);

    /* access modifiers changed from: protected */
    public abstract void _setDefaultGpuLevel(Global.IdAndDefaultGpuLevel idAndDefaultGpuLevel);

    /* access modifiers changed from: protected */
    public abstract void _setDeviceName(Global.IdAndDeviceName idAndDeviceName);

    /* access modifiers changed from: protected */
    public abstract void _setDfsMode(Global.IdAndDfsMode idAndDfsMode);

    /* access modifiers changed from: protected */
    public abstract void _setDmaId(Global.IdAndDmaId idAndDmaId);

    /* access modifiers changed from: protected */
    public abstract void _setDrrAllowed(Global.IdAndDrrAllowed idAndDrrAllowed);

    /* access modifiers changed from: protected */
    public abstract void _setEachModeDfs(Global.IdAndEachModeDfs idAndEachModeDfs);

    /* access modifiers changed from: protected */
    public abstract void _setEachModeDss(Global.IdAndEachModeDss idAndEachModeDss);

    /* access modifiers changed from: protected */
    public abstract void _setEachModeTargetShortSide(Global.IdAndEachModeTargetShortSide idAndEachModeTargetShortSide);

    /* access modifiers changed from: protected */
    public abstract void _setFullyUpdateTime(Global.IdAndFullyUpdateTime idAndFullyUpdateTime);

    /* access modifiers changed from: protected */
    public abstract void _setGfiPolicy(Global.IdAndGfiPolicy idAndGfiPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setGmsVersion(Global.IdAndGmsVersion idAndGmsVersion);

    /* access modifiers changed from: protected */
    public abstract void _setGosSelfUpdateStatus(Global.IdAndGosSelfUpdateStatus idAndGosSelfUpdateStatus);

    /* access modifiers changed from: protected */
    public abstract void _setGovernorSettings(Global.IdAndGovernorSettings idAndGovernorSettings);

    /* access modifiers changed from: protected */
    public abstract void _setInitialized(Global.IdAndInitialized idAndInitialized);

    /* access modifiers changed from: protected */
    public abstract void _setIpmCpuBottomFreq(Global.IdAndIpmCpuBottomFreq idAndIpmCpuBottomFreq);

    /* access modifiers changed from: protected */
    public abstract void _setIpmFlag(Global.IdAndIpmFlag idAndIpmFlag);

    /* access modifiers changed from: protected */
    public abstract void _setIpmMode(Global.IdAndIpmMode idAndIpmMode);

    /* access modifiers changed from: protected */
    public abstract void _setIpmPolicy(Global.IdAndIpmPolicy idAndIpmPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setIpmTargetPower(Global.IdAndIpmTargetPower idAndIpmTargetPower);

    /* access modifiers changed from: protected */
    public abstract void _setIpmTargetTemperature(Global.IdAndIpmTargetTemperature idAndIpmTargetTemperature);

    /* access modifiers changed from: protected */
    public abstract void _setIpmTrainingVersion(Global.IdAndIpmTrainingVersion idAndIpmTrainingVersion);

    /* access modifiers changed from: protected */
    public abstract void _setIpmUpdateTime(Global.IdAndIpmUpdateTime idAndIpmUpdateTime);

    /* access modifiers changed from: protected */
    public abstract void _setLaunchBoostPolicy(Global.IdAndLaunchBoostPolicy idAndLaunchBoostPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setLoggingPolicy(Global.IdAndLoggingPolicy idAndLoggingPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setPrevSiopModeByUser(Global.IdAndPrevSiopModeByUser idAndPrevSiopModeByUser);

    /* access modifiers changed from: protected */
    public abstract void _setRegisteredDevice(Global.IdAndRegisteredDevice idAndRegisteredDevice);

    /* access modifiers changed from: protected */
    public abstract void _setResolutionMode(Global.IdAndResolutionMode idAndResolutionMode);

    /* access modifiers changed from: protected */
    public abstract void _setResumeBoostPolicy(Global.IdAndResumeBoostPolicy idAndResumeBoostPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setRinglogPolicy(Global.IdAndRinglogPolicy idAndRinglogPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setShowLogcat(Global.IdAndShowLogcat idAndShowLogcat);

    /* access modifiers changed from: protected */
    public abstract void _setSiopMode(Global.IdAndSiopMode idAndSiopMode);

    /* access modifiers changed from: protected */
    public abstract void _setSiopModePolicy(Global.IdAndSiopModePolicy idAndSiopModePolicy);

    /* access modifiers changed from: protected */
    public abstract void _setSosPolicyKeyCsv(Global.IdAndSosPolicyKeyCsv idAndSosPolicyKeyCsv);

    /* access modifiers changed from: protected */
    public abstract void _setTargetServer(Global.IdAndTargetServer idAndTargetServer);

    /* access modifiers changed from: protected */
    public abstract void _setTspPolicy(Global.IdAndTspPolicy idAndTspPolicy);

    /* access modifiers changed from: protected */
    public abstract void _setUUID(Global.IdAndUuid idAndUuid);

    /* access modifiers changed from: protected */
    public abstract void _setUpdateTime(Global.IdAndUpdateTime idAndUpdateTime);

    /* access modifiers changed from: protected */
    public abstract void _setVrrValues(Global.IdAndVrr idAndVrr);

    /* access modifiers changed from: protected */
    public abstract void _setWifiQosPolicy(Global.IdAndWifiQosPolicy idAndWifiQosPolicy);

    public abstract Global get();

    public abstract int getClearBGLruNum();

    public abstract String getClearBGSurviveAppFromServer();

    public abstract float getCustomDfs();

    public abstract float getCustomDss();

    public abstract int getDefaultCpuLevel();

    public abstract int getDefaultGpuLevel();

    public abstract String getDeviceName();

    public abstract int getDfsMode();

    public abstract String getDmaId();

    public abstract int getDrrAllowed();

    public abstract String getEachModeDfs();

    public abstract String getEachModeDss();

    public abstract String getEachModeTargetShortSide();

    public abstract long getFullyUpdateTime();

    public abstract String getGfiPolicy();

    public abstract float getGmsVersion();

    public abstract String getGovernorSettings();

    public abstract long getIpmCpuBottomFreq();

    public abstract String getIpmFlag();

    public abstract int getIpmMode();

    public abstract String getIpmPolicy();

    public abstract int getIpmTargetPower();

    public abstract int getIpmTargetTemperature();

    public abstract String getIpmTrainingVersion();

    public abstract long getIpmUpdateTime();

    public abstract String getLaunchBoostPolicy();

    public abstract String getLoggingPolicy();

    public abstract int getPrevSiopModeByUser();

    public abstract int getResolutionMode();

    public abstract String getResumeBoostPolicy();

    public abstract String getRinglogPolicy();

    public abstract int getSiopMode();

    public abstract String getSiopModePolicy();

    public abstract int getTargetServer();

    public abstract String getTspPolicy();

    public abstract String getUUID();

    public abstract long getUpdateTime();

    public abstract int getVrrMaxValue();

    public abstract int getVrrMinValue();

    public abstract String getWifiQosPolicy();

    public abstract boolean isAutomaticUpdate();

    public abstract boolean isDataReady();

    public abstract boolean isGosSelfUpdateStatus();

    public abstract boolean isInitialized();

    public abstract boolean isRegisteredDevice();

    public abstract boolean isShowLogcat();

    public long insert(Global global) {
        try {
            return _insert(global);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return -1;
        }
    }

    public long insertOrUpdate(Global global) {
        try {
            return _insertOrUpdate(global);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return -1;
        }
    }

    public void setUUID(Global.IdAndUuid idAndUuid) {
        try {
            _setUUID(idAndUuid);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setDataReady(Global.IdAndDataReady idAndDataReady) {
        try {
            _setDataReady(idAndDataReady);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setInitialized(Global.IdAndInitialized idAndInitialized) {
        try {
            _setInitialized(idAndInitialized);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setUpdateTime(Global.IdAndUpdateTime idAndUpdateTime) {
        try {
            _setUpdateTime(idAndUpdateTime);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setFullyUpdateTime(Global.IdAndFullyUpdateTime idAndFullyUpdateTime) {
        try {
            _setFullyUpdateTime(idAndFullyUpdateTime);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setRegisteredDevice(Global.IdAndRegisteredDevice idAndRegisteredDevice) {
        try {
            _setRegisteredDevice(idAndRegisteredDevice);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setGmsVersion(Global.IdAndGmsVersion idAndGmsVersion) {
        try {
            _setGmsVersion(idAndGmsVersion);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setTargetServer(Global.IdAndTargetServer idAndTargetServer) {
        try {
            _setTargetServer(idAndTargetServer);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setDeviceName(Global.IdAndDeviceName idAndDeviceName) {
        try {
            _setDeviceName(idAndDeviceName);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setClearBGLruNum(Global.IdAndClearBGLruNum idAndClearBGLruNum) {
        try {
            _setClearBGLruNum(idAndClearBGLruNum);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setClearBGSurviveAppFromServer(Global.IdAndClearBGSurviveAppFromServer idAndClearBGSurviveAppFromServer) {
        try {
            _setClearBGSurviveAppFromServer(idAndClearBGSurviveAppFromServer);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setPrevSiopModeByUser(Global.IdAndPrevSiopModeByUser idAndPrevSiopModeByUser) {
        try {
            _setPrevSiopModeByUser(idAndPrevSiopModeByUser);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setSosPolicyKeyCsv(Global.IdAndSosPolicyKeyCsv idAndSosPolicyKeyCsv) {
        try {
            _setSosPolicyKeyCsv(idAndSosPolicyKeyCsv);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setAutomaticUpdate(Global.IdAndAutomaticUpdate idAndAutomaticUpdate) {
        try {
            _setAutomaticUpdate(idAndAutomaticUpdate);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setGosSelfUpdateStatus(Global.IdAndGosSelfUpdateStatus idAndGosSelfUpdateStatus) {
        try {
            _setGosSelfUpdateStatus(idAndGosSelfUpdateStatus);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setShowLogcat(Global.IdAndShowLogcat idAndShowLogcat) {
        try {
            _setShowLogcat(idAndShowLogcat);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setDmaId(Global.IdAndDmaId idAndDmaId) {
        try {
            _setDmaId(idAndDmaId);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setWifiQosPolicy(Global.IdAndWifiQosPolicy idAndWifiQosPolicy) {
        try {
            _setWifiQosPolicy(idAndWifiQosPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setLaunchBoostPolicy(Global.IdAndLaunchBoostPolicy idAndLaunchBoostPolicy) {
        try {
            _setLaunchBoostPolicy(idAndLaunchBoostPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setSiopModePolicy(Global.IdAndSiopModePolicy idAndSiopModePolicy) {
        try {
            _setSiopModePolicy(idAndSiopModePolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setGfiPolicy(Global.IdAndGfiPolicy idAndGfiPolicy) {
        try {
            _setGfiPolicy(idAndGfiPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmPolicy(Global.IdAndIpmPolicy idAndIpmPolicy) {
        try {
            _setIpmPolicy(idAndIpmPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setLoggingPolicy(Global.IdAndLoggingPolicy idAndLoggingPolicy) {
        try {
            _setLoggingPolicy(idAndLoggingPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setResumeBoostPolicy(Global.IdAndResumeBoostPolicy idAndResumeBoostPolicy) {
        try {
            _setResumeBoostPolicy(idAndResumeBoostPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setGovernorSettings(Global.IdAndGovernorSettings idAndGovernorSettings) {
        try {
            _setGovernorSettings(idAndGovernorSettings);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setEachModeTargetShortSide(Global.IdAndEachModeTargetShortSide idAndEachModeTargetShortSide) {
        try {
            _setEachModeTargetShortSide(idAndEachModeTargetShortSide);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setSiopMode(Global.IdAndSiopMode idAndSiopMode) {
        try {
            _setSiopMode(idAndSiopMode);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmFlag(Global.IdAndIpmFlag idAndIpmFlag) {
        try {
            _setIpmFlag(idAndIpmFlag);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmCpuBottomFreq(Global.IdAndIpmCpuBottomFreq idAndIpmCpuBottomFreq) {
        try {
            _setIpmCpuBottomFreq(idAndIpmCpuBottomFreq);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmTrainingVersion(Global.IdAndIpmTrainingVersion idAndIpmTrainingVersion) {
        try {
            _setIpmTrainingVersion(idAndIpmTrainingVersion);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmUpdateTime(Global.IdAndIpmUpdateTime idAndIpmUpdateTime) {
        try {
            _setIpmUpdateTime(idAndIpmUpdateTime);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmTargetTemperature(Global.IdAndIpmTargetTemperature idAndIpmTargetTemperature) {
        try {
            _setIpmTargetTemperature(idAndIpmTargetTemperature);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmTargetPower(Global.IdAndIpmTargetPower idAndIpmTargetPower) {
        try {
            _setIpmTargetPower(idAndIpmTargetPower);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setIpmMode(Global.IdAndIpmMode idAndIpmMode) {
        try {
            _setIpmMode(idAndIpmMode);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setEachModeDfs(Global.IdAndEachModeDfs idAndEachModeDfs) {
        try {
            _setEachModeDfs(idAndEachModeDfs);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setEachModeDss(Global.IdAndEachModeDss idAndEachModeDss) {
        try {
            _setEachModeDss(idAndEachModeDss);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setDefaultCpuLevel(Global.IdAndDefaultCpuLevel idAndDefaultCpuLevel) {
        try {
            _setDefaultCpuLevel(idAndDefaultCpuLevel);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setDefaultGpuLevel(Global.IdAndDefaultGpuLevel idAndDefaultGpuLevel) {
        try {
            _setDefaultGpuLevel(idAndDefaultGpuLevel);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setDfsMode(Global.IdAndDfsMode idAndDfsMode) {
        try {
            _setDfsMode(idAndDfsMode);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setResolutionMode(Global.IdAndResolutionMode idAndResolutionMode) {
        try {
            _setResolutionMode(idAndResolutionMode);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setTspPolicy(Global.IdAndTspPolicy idAndTspPolicy) {
        try {
            _setTspPolicy(idAndTspPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setRinglogPolicy(Global.IdAndRinglogPolicy idAndRinglogPolicy) {
        try {
            _setRinglogPolicy(idAndRinglogPolicy);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setVrrValues(Global.IdAndVrr idAndVrr) {
        try {
            _setVrrValues(idAndVrr);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void setDrrAllowed(Global.IdAndDrrAllowed idAndDrrAllowed) {
        try {
            _setDrrAllowed(idAndDrrAllowed);
        } catch (SQLiteFullException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }
}
