package com.samsung.android.game.gos.ipm;

public class MapLongFloat {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected MapLongFloat(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(MapLongFloat mapLongFloat) {
        if (mapLongFloat == null) {
            return 0;
        }
        return mapLongFloat.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_MapLongFloat(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public MapLongFloat() {
        this(IpmWrapJNI.new_MapLongFloat(), true);
    }

    public long size() {
        return IpmWrapJNI.MapLongFloat_size(this.swigCPtr, this);
    }

    public boolean isEmpty() {
        return IpmWrapJNI.MapLongFloat_isEmpty(this.swigCPtr, this);
    }

    public void clear() {
        IpmWrapJNI.MapLongFloat_clear(this.swigCPtr, this);
    }

    public float get(long j) {
        return IpmWrapJNI.MapLongFloat_get(this.swigCPtr, this, j);
    }

    public void put(long j, float f) {
        IpmWrapJNI.MapLongFloat_put(this.swigCPtr, this, j, f);
    }

    public void remove(long j) {
        IpmWrapJNI.MapLongFloat_remove(this.swigCPtr, this, j);
    }

    public boolean containsKey(long j) {
        return IpmWrapJNI.MapLongFloat_containsKey(this.swigCPtr, this, j);
    }
}
