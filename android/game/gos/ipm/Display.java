package com.samsung.android.game.gos.ipm;

public class Display {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected Display(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Display display) {
        if (display == null) {
            return 0;
        }
        return display.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_Display(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public Display() {
        this(IpmWrapJNI.new_Display(), true);
    }

    public float getRefreshRate() {
        return IpmWrapJNI.Display_getRefreshRate(this.swigCPtr, this);
    }

    public void onRefreshRateChanged(float f, float f2) {
        IpmWrapJNI.Display_onRefreshRateChanged(this.swigCPtr, this, f, f2);
    }
}
