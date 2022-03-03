package com.samsung.android.game.gos.feature.externalsdk;

interface IControlStrategy {
    long getControlFlags();

    boolean isAvailable();

    void releaseAll();

    void releaseBoost();

    void releaseCpuLevel();

    void releaseGpuLevel();

    void releasePerformanceLevel();

    boolean setBoost();

    boolean setCpuLevel(int i);

    boolean setGpuLevel(int i);

    void setMaxFps(int i);

    boolean setPerformanceLevel(int i);
}
