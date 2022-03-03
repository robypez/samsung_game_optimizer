package com.samsung.android.game.gos.ipm;

public class VectorString {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected VectorString(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(VectorString vectorString) {
        if (vectorString == null) {
            return 0;
        }
        return vectorString.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_VectorString(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public VectorString() {
        this(IpmWrapJNI.new_VectorString(), true);
    }

    public int size() {
        return IpmWrapJNI.VectorString_size(this.swigCPtr, this);
    }

    public boolean isEmpty() {
        return IpmWrapJNI.VectorString_isEmpty(this.swigCPtr, this);
    }

    public void ensureCapacity(long j) {
        IpmWrapJNI.VectorString_ensureCapacity(this.swigCPtr, this, j);
    }

    public void clear() {
        IpmWrapJNI.VectorString_clear(this.swigCPtr, this);
    }

    public void add(String str) {
        IpmWrapJNI.VectorString_add(this.swigCPtr, this, str);
    }

    public String get(long j) {
        return IpmWrapJNI.VectorString_get(this.swigCPtr, this, j);
    }

    public void set(int i, String str) {
        IpmWrapJNI.VectorString_set(this.swigCPtr, this, i, str);
    }
}
