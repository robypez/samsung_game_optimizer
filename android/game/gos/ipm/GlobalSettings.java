package com.samsung.android.game.gos.ipm;

public interface GlobalSettings {
    long getCpuBottomFrequency();

    int getDefaultTemperature();

    int getMode();

    String getPolicy();

    Profile getProfile();

    int getSiopMode();

    int getTargetTemperature();

    String getTrainingVersion();

    long getUpdateTime();

    boolean isOnlyCaptureEnabled();

    boolean isSupertrainEnabled();

    boolean isThreadManagementSupported();

    boolean isVerbose();

    void setCpuBottomFrequency(long j);

    void setIpmEnabled(boolean z);

    void setMode(int i);

    void setOnlyCapture(boolean z);

    void setProfile(Profile profile);

    void setSupertrainEnabled(boolean z);

    void setTargetTemperature(int i);

    void setVerbose(boolean z);
}
