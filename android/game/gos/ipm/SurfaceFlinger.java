package com.samsung.android.game.gos.ipm;

public class SurfaceFlinger {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SurfaceFlinger(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(SurfaceFlinger surfaceFlinger) {
        if (surfaceFlinger == null) {
            return 0;
        }
        return surfaceFlinger.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_SurfaceFlinger(this.swigCPtr);
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
        IpmWrapJNI.SurfaceFlinger_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        IpmWrapJNI.SurfaceFlinger_change_ownership(this, this.swigCPtr, true);
    }

    public boolean dumpPipe(int i, VectorString vectorString) {
        return IpmWrapJNI.SurfaceFlinger_dumpPipe(this.swigCPtr, this, i, VectorString.getCPtr(vectorString), vectorString);
    }

    public int connectPipe(int i, int i2) {
        return IpmWrapJNI.SurfaceFlinger_connectPipe(this.swigCPtr, this, i, i2);
    }

    public int flipCount() {
        return IpmWrapJNI.SurfaceFlinger_flipCount(this.swigCPtr, this);
    }

    public boolean isUsingGfi(int i) {
        return IpmWrapJNI.SurfaceFlinger_isUsingGfi(this.swigCPtr, this, i);
    }

    public boolean enableGfi(float f) {
        return IpmWrapJNI.SurfaceFlinger_enableGfi(this.swigCPtr, this, f);
    }

    public boolean registerForFrameStats(int i, int i2) {
        return IpmWrapJNI.SurfaceFlinger_registerForFrameStats(this.swigCPtr, this, i, i2);
    }

    public SurfaceFlinger() {
        this(IpmWrapJNI.new_SurfaceFlinger(), true);
        IpmWrapJNI.SurfaceFlinger_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}
