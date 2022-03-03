package com.samsung.android.game.gos.test.gostester.entity;

public class GfiPackageDataEntity {
    public FeatureDataEntity GFPSOffset = new FeatureDataEntity();
    public FeatureDataEntity GFPSOffsetMin = new FeatureDataEntity();
    public FeatureDataEntity dfsOffset = new FeatureDataEntity();
    public FeatureDataEntity dfsOffsetMax = new FeatureDataEntity();
    public FeatureDataEntity dfsSmoothness = new FeatureDataEntity();
    public boolean enableExternalControl;
    public boolean enableSessionRecording;
    public FeatureDataEntity gameDFS = new FeatureDataEntity();
    public FeatureDataEntity gameFPS = new FeatureDataEntity();
    public int interpolationMode;
    public boolean keepTwoHwcLayers;
    public int logLevel;
    public int maxConsecutiveGlComposition;
    public FeatureDataEntity maxGLComposition = new FeatureDataEntity();
    public int minAcceptableFps;
    public FeatureDataEntity minRegalStability = new FeatureDataEntity();
    public String minVersion;
    public String package_name;
    public boolean useDFSOffset;
    public boolean useGFPSOffset;
    public boolean useInterpolation;
    public boolean useSmartDelay;
    public boolean useWriteToFrameTracker;
    public String version;
    public int watchdogExpire;
}
