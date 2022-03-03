package com.samsung.android.game.gos.feature.ipm;

import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.ipm.GlobalSettings;
import com.samsung.android.game.gos.ipm.Profile;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;

public class GosGlobalSettings implements GlobalSettings {
    private static final String LOG_TAG = "GlobalSettings";
    private final GlobalDao mGlobalDao;
    private final GlobalDbHelper mGlobalDbHelper;

    public GosGlobalSettings(GlobalDao globalDao, GlobalDbHelper globalDbHelper) {
        this.mGlobalDao = globalDao;
        this.mGlobalDbHelper = globalDbHelper;
    }

    public void setIpmEnabled(boolean z) {
        this.mGlobalDbHelper.setAvailable("ipm", z);
    }

    public void setTargetTemperature(int i) {
        this.mGlobalDao.setIpmTargetTemperature(new Global.IdAndIpmTargetTemperature(i));
    }

    public int getTargetTemperature() {
        return this.mGlobalDao.getIpmTargetTemperature();
    }

    public void setCpuBottomFrequency(long j) {
        this.mGlobalDao.setIpmCpuBottomFreq(new Global.IdAndIpmCpuBottomFreq(j));
    }

    public long getCpuBottomFrequency() {
        return this.mGlobalDao.getIpmCpuBottomFreq();
    }

    public void setProfile(Profile profile) {
        this.mGlobalDao.setIpmMode(new Global.IdAndIpmMode(profile.toInt()));
    }

    public Profile getProfile() {
        return Profile.fromInt(this.mGlobalDao.getIpmMode());
    }

    public boolean isThreadManagementSupported() {
        return this.mGlobalDao.getGmsVersion() >= 110.015f;
    }

    public int getDefaultTemperature() {
        return this.mGlobalDbHelper.getIpmDefaultTemperature();
    }

    public int getSiopMode() {
        return this.mGlobalDao.getSiopMode();
    }

    public String getPolicy() {
        return this.mGlobalDao.getIpmPolicy();
    }

    public void setMode(int i) {
        this.mGlobalDao.setIpmMode(new Global.IdAndIpmMode(i));
    }

    public int getMode() {
        return this.mGlobalDao.getIpmMode();
    }

    public String getTrainingVersion() {
        return this.mGlobalDao.getIpmTrainingVersion();
    }

    public long getUpdateTime() {
        return this.mGlobalDao.getIpmUpdateTime();
    }

    public void setSupertrainEnabled(boolean z) {
        setFlag(0, z);
    }

    public boolean isSupertrainEnabled() {
        return getFlag(0);
    }

    public void setVerbose(boolean z) {
        setFlag(1, z);
    }

    public boolean isVerbose() {
        return getFlag(1);
    }

    public void setOnlyCapture(boolean z) {
        setFlag(4, z);
    }

    public boolean isOnlyCaptureEnabled() {
        return getFlag(4);
    }

    private void setFlag(int i, boolean z) {
        boolean[] csvToBooleans = TypeConverter.csvToBooleans(this.mGlobalDao.getIpmFlag());
        if (csvToBooleans != null) {
            csvToBooleans[i] = z;
            this.mGlobalDao.setIpmFlag(new Global.IdAndIpmFlag(csvToBooleans));
            return;
        }
        GosLog.e(LOG_TAG, String.format("Failed to set flag %d", new Object[]{Integer.valueOf(i)}));
    }

    private boolean getFlag(int i) {
        boolean[] csvToBooleans = TypeConverter.csvToBooleans(this.mGlobalDao.getIpmFlag());
        if (csvToBooleans != null) {
            return csvToBooleans[i];
        }
        GosLog.e(LOG_TAG, String.format("Failed to get flag %d", new Object[]{Integer.valueOf(i)}));
        return false;
    }
}
