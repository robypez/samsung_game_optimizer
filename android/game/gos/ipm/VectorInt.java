package com.samsung.android.game.gos.ipm;

public class VectorInt {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected VectorInt(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(VectorInt vectorInt) {
        if (vectorInt == null) {
            return 0;
        }
        return vectorInt.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_VectorInt(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public VectorInt() {
        this(IpmWrapJNI.new_VectorInt(), true);
    }

    public int size() {
        return IpmWrapJNI.VectorInt_size(this.swigCPtr, this);
    }

    public boolean isEmpty() {
        return IpmWrapJNI.VectorInt_isEmpty(this.swigCPtr, this);
    }

    public void ensureCapacity(long j) {
        IpmWrapJNI.VectorInt_ensureCapacity(this.swigCPtr, this, j);
    }

    public void clear() {
        IpmWrapJNI.VectorInt_clear(this.swigCPtr, this);
    }

    public void add(int i) {
        IpmWrapJNI.VectorInt_add(this.swigCPtr, this, i);
    }

    public int get(long j) {
        return IpmWrapJNI.VectorInt_get(this.swigCPtr, this, j);
    }

    public void set(int i, int i2) {
        IpmWrapJNI.VectorInt_set(this.swigCPtr, this, i, i2);
    }
}
