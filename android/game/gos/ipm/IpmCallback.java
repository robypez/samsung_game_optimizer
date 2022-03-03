package com.samsung.android.game.gos.ipm;

public class IpmCallback {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected IpmCallback(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(IpmCallback ipmCallback) {
        if (ipmCallback == null) {
            return 0;
        }
        return ipmCallback.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_IpmCallback(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void swigDirectorDisconnect() {
        this.swigCMemOwn = false;
        delete();
    }

    public void swigReleaseOwnership() {
        this.swigCMemOwn = false;
        IpmWrapJNI.IpmCallback_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        IpmWrapJNI.IpmCallback_change_ownership(this, this.swigCPtr, true);
    }

    public Object getContext() {
        return IpmWrapJNI.IpmCallback_getContext(this.swigCPtr, this);
    }

    public String getGosVersion() {
        return IpmWrapJNI.IpmCallback_getGosVersion(this.swigCPtr, this);
    }

    public String getGosNamespace() {
        return IpmWrapJNI.IpmCallback_getGosNamespace(this.swigCPtr, this);
    }

    public String getFilesPath() {
        return IpmWrapJNI.IpmCallback_getFilesPath(this.swigCPtr, this);
    }

    public String getCachePath() {
        return IpmWrapJNI.IpmCallback_getCachePath(this.swigCPtr, this);
    }

    public int getApplicationUid() {
        return IpmWrapJNI.IpmCallback_getApplicationUid(this.swigCPtr, this);
    }

    public String getApplicationAbi() {
        return IpmWrapJNI.IpmCallback_getApplicationAbi(this.swigCPtr, this);
    }

    public String getApplicationName() {
        return IpmWrapJNI.IpmCallback_getApplicationName(this.swigCPtr, this);
    }

    public boolean isMinLockBoosterWorking() {
        return IpmWrapJNI.IpmCallback_isMinLockBoosterWorking(this.swigCPtr, this);
    }

    public int getBigTurboDurationSeconds() {
        return IpmWrapJNI.IpmCallback_getBigTurboDurationSeconds(this.swigCPtr, this);
    }

    public boolean isBigTurboWorking() {
        return IpmWrapJNI.IpmCallback_isBigTurboWorking(this.swigCPtr, this);
    }

    public int getMinLockBoosterDurationSeconds() {
        return IpmWrapJNI.IpmCallback_getMinLockBoosterDurationSeconds(this.swigCPtr, this);
    }

    public MapLongFloat getGpuEff() {
        return new MapLongFloat(IpmWrapJNI.IpmCallback_getGpuEff(this.swigCPtr, this), true);
    }

    public MapLongFloat getCpuEff(int i) {
        return new MapLongFloat(IpmWrapJNI.IpmCallback_getCpuEff(this.swigCPtr, this, i), true);
    }

    public int getClusterCount() {
        return IpmWrapJNI.IpmCallback_getClusterCount(this.swigCPtr, this);
    }

    public VectorString getSysFsData(VectorString vectorString) {
        return new VectorString(IpmWrapJNI.IpmCallback_getSysFsData(this.swigCPtr, this, VectorString.getCPtr(vectorString), vectorString), true);
    }

    public void onStarted() {
        IpmWrapJNI.IpmCallback_onStarted(this.swigCPtr, this);
    }

    public void onStopped() {
        IpmWrapJNI.IpmCallback_onStopped(this.swigCPtr, this);
    }

    public void onSystemCreated() {
        IpmWrapJNI.IpmCallback_onSystemCreated(this.swigCPtr, this);
    }

    public boolean getScenario() {
        return IpmWrapJNI.IpmCallback_getScenario(this.swigCPtr, this);
    }

    public boolean isScenarioDynamicChecked() {
        return IpmWrapJNI.IpmCallback_isScenarioDynamicChecked(this.swigCPtr, this);
    }

    public IpmCallback() {
        this(IpmWrapJNI.new_IpmCallback(), true);
        IpmWrapJNI.IpmCallback_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}
