package com.samsung.android.game.gos.ipm;

public class IpmJava {
    private Display mDisplay;
    private GameManager mGameManager;
    private IpmCallback mIpmCallback;
    private SurfaceFlinger mSurfaceFlinger;
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected IpmJava(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(IpmJava ipmJava) {
        if (ipmJava == null) {
            return 0;
        }
        return ipmJava.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_IpmJava(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public IpmJava(IpmCallback ipmCallback, Display display, SurfaceFlinger surfaceFlinger, GameManager gameManager) {
        this(IpmWrapJNI.new_IpmJava(IpmCallback.getCPtr(ipmCallback), ipmCallback, Display.getCPtr(display), display, SurfaceFlinger.getCPtr(surfaceFlinger), surfaceFlinger, GameManager.getCPtr(gameManager), gameManager), true);
        this.mIpmCallback = ipmCallback;
        this.mDisplay = display;
        this.mSurfaceFlinger = surfaceFlinger;
        this.mGameManager = gameManager;
    }

    public void start(IntelMode intelMode, String str) {
        IpmWrapJNI.IpmJava_start__SWIG_0(this.swigCPtr, this, intelMode.swigValue(), str);
    }

    public void start(IntelMode intelMode) {
        IpmWrapJNI.IpmJava_start__SWIG_1(this.swigCPtr, this, intelMode.swigValue());
    }

    public void stop() {
        IpmWrapJNI.IpmJava_stop(this.swigCPtr, this);
    }

    public void addJson(String str, String str2) {
        IpmWrapJNI.IpmJava_addJson(this.swigCPtr, this, str, str2);
    }

    public void setProfile(Profile profile) {
        IpmWrapJNI.IpmJava_setProfile(this.swigCPtr, this, profile.swigValue());
    }

    public Profile getProfile() {
        return Profile.swigToEnum(IpmWrapJNI.IpmJava_getProfile(this.swigCPtr, this));
    }

    public void setCustomProfile(float f, float f2, float f3, float f4) {
        IpmWrapJNI.IpmJava_setCustomProfile(this.swigCPtr, this, f, f2, f3, f4);
    }

    public void setLogLevel(LogLevel logLevel) {
        IpmWrapJNI.IpmJava_setLogLevel(this.swigCPtr, this, logLevel.swigValue());
    }

    public void setSupertrain(boolean z) {
        IpmWrapJNI.IpmJava_setSupertrain(this.swigCPtr, this, z);
    }

    public boolean getSupertrain() {
        return IpmWrapJNI.IpmJava_getSupertrain(this.swigCPtr, this);
    }

    public void setTargetPST(int i) {
        IpmWrapJNI.IpmJava_setTargetPST(this.swigCPtr, this, i);
    }

    public int getTargetPST() {
        return IpmWrapJNI.IpmJava_getTargetPST(this.swigCPtr, this);
    }

    public void setTargetTemperature(int i) {
        IpmWrapJNI.IpmJava_setTargetTemperature(this.swigCPtr, this, i);
    }

    public void setTargetLRPST(int i) {
        IpmWrapJNI.IpmJava_setTargetLRPST(this.swigCPtr, this, i);
    }

    public void setCustomTfpsFlags(int i) {
        IpmWrapJNI.IpmJava_setCustomTfpsFlags(this.swigCPtr, this, i);
    }

    public void setFixedTargetFps(float f) {
        IpmWrapJNI.IpmJava_setFixedTargetFps(this.swigCPtr, this, f);
    }

    public void resetFixedTargetFps() {
        IpmWrapJNI.IpmJava_resetFixedTargetFps(this.swigCPtr, this);
    }

    public void setCPUBottomFreq(long j) {
        IpmWrapJNI.IpmJava_setCPUBottomFreq(this.swigCPtr, this, j);
    }

    public void setOnlyCapture(boolean z) {
        IpmWrapJNI.IpmJava_setOnlyCapture(this.swigCPtr, this, z);
    }

    public boolean getOnlyCapture() {
        return IpmWrapJNI.IpmJava_getOnlyCapture(this.swigCPtr, this);
    }

    public String getStatistics() {
        return IpmWrapJNI.IpmJava_getStatistics(this.swigCPtr, this);
    }

    public int getVersion() {
        return IpmWrapJNI.IpmJava_getVersion(this.swigCPtr, this);
    }

    public boolean canRun() {
        return IpmWrapJNI.IpmJava_canRun(this.swigCPtr, this);
    }

    public void setMinFreqs(long j, long j2) {
        IpmWrapJNI.IpmJava_setMinFreqs(this.swigCPtr, this, j, j2);
    }

    public void setMaxFreqs(long j, long j2) {
        IpmWrapJNI.IpmJava_setMaxFreqs(this.swigCPtr, this, j, j2);
    }

    public void setCpuGap(int i) {
        IpmWrapJNI.IpmJava_setCpuGap(this.swigCPtr, this, i);
    }

    public void setGpuGap(int i) {
        IpmWrapJNI.IpmJava_setGpuGap(this.swigCPtr, this, i);
    }

    public void setGpuMinBoost(int i) {
        IpmWrapJNI.IpmJava_setGpuMinBoost(this.swigCPtr, this, i);
    }

    public void setBusGap(int i) {
        IpmWrapJNI.IpmJava_setBusGap(this.swigCPtr, this, i);
    }

    public String getID() {
        return IpmWrapJNI.IpmJava_getID(this.swigCPtr, this);
    }

    public void releaseMem() {
        IpmWrapJNI.IpmJava_releaseMem(this.swigCPtr, this);
    }

    public void setUseSsrm(boolean z) {
        IpmWrapJNI.IpmJava_setUseSsrm(this.swigCPtr, this, z);
    }

    public void setInputTempType(int i) {
        IpmWrapJNI.IpmJava_setInputTempType(this.swigCPtr, this, i);
    }

    public int getMaxFpsGuess() {
        return IpmWrapJNI.IpmJava_getMaxFpsGuess(this.swigCPtr, this);
    }

    public boolean isGameSdkEnabledForPkg(String str) {
        return IpmWrapJNI.IpmJava_isGameSdkEnabledForPkg(this.swigCPtr, this, str);
    }

    public boolean isClusterControlAvailable() {
        return IpmWrapJNI.IpmJava_isClusterControlAvailable(this.swigCPtr, this);
    }

    public float getCurrentTargetTemp() {
        return IpmWrapJNI.IpmJava_getCurrentTargetTemp(this.swigCPtr, this);
    }

    public float getCurrentTemp() {
        return IpmWrapJNI.IpmJava_getCurrentTemp(this.swigCPtr, this);
    }

    public void pauseActions(boolean z) {
        IpmWrapJNI.IpmJava_pauseActions(this.swigCPtr, this, z);
    }

    public void handOverControlToGameSDK(String str, boolean z) {
        IpmWrapJNI.IpmJava_handOverControlToGameSDK(this.swigCPtr, this, str, z);
    }

    public void setLowLatencySceneSDK(int i, String str, int i2, boolean z) {
        IpmWrapJNI.IpmJava_setLowLatencySceneSDK(this.swigCPtr, this, i, str, i2, z);
    }

    public void setEnableToTGPA(boolean z) {
        IpmWrapJNI.IpmJava_setEnableToTGPA(this.swigCPtr, this, z);
    }

    public String getToTGPA() {
        return IpmWrapJNI.IpmJava_getToTGPA(this.swigCPtr, this);
    }

    public void setThreadControl(int i) {
        IpmWrapJNI.IpmJava_setThreadControl(this.swigCPtr, this, i);
    }

    public void setThermalControl(boolean z) {
        IpmWrapJNI.IpmJava_setThermalControl(this.swigCPtr, this, z);
    }

    public void setAllowMlOff(boolean z) {
        IpmWrapJNI.IpmJava_setAllowMlOff(this.swigCPtr, this, z);
    }

    public void setEnableBusFreq(boolean z) {
        IpmWrapJNI.IpmJava_setEnableBusFreq(this.swigCPtr, this, z);
    }

    public void setHighStabilityMode(boolean z) {
        IpmWrapJNI.IpmJava_setHighStabilityMode(this.swigCPtr, this, z);
    }

    public void enableFrameInterpolation(float f, float f2, float f3) {
        IpmWrapJNI.IpmJava_enableFrameInterpolation(this.swigCPtr, this, f, f2, f3);
    }

    public void disableFrameInterpolation() {
        IpmWrapJNI.IpmJava_disableFrameInterpolation(this.swigCPtr, this);
    }

    public String readSessionsJSON() {
        return IpmWrapJNI.IpmJava_readSessionsJSON__SWIG_0(this.swigCPtr, this);
    }

    public String readSessionsJSON(VectorInt vectorInt) {
        return IpmWrapJNI.IpmJava_readSessionsJSON__SWIG_1(this.swigCPtr, this, VectorInt.getCPtr(vectorInt), vectorInt);
    }

    public String readDataJSON(VectorParameterRequest vectorParameterRequest, int i, long j, long j2) {
        return IpmWrapJNI.IpmJava_readDataJSON(this.swigCPtr, this, VectorParameterRequest.getCPtr(vectorParameterRequest), vectorParameterRequest, i, j, j2);
    }

    public void setDynamicPowerMode(boolean z) {
        IpmWrapJNI.IpmJava_setDynamicPowerMode(this.swigCPtr, this, z);
    }
}
