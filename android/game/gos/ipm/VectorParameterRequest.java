package com.samsung.android.game.gos.ipm;

public class VectorParameterRequest {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected VectorParameterRequest(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(VectorParameterRequest vectorParameterRequest) {
        if (vectorParameterRequest == null) {
            return 0;
        }
        return vectorParameterRequest.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_VectorParameterRequest(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public VectorParameterRequest() {
        this(IpmWrapJNI.new_VectorParameterRequest(), true);
    }

    public int size() {
        return IpmWrapJNI.VectorParameterRequest_size(this.swigCPtr, this);
    }

    public boolean isEmpty() {
        return IpmWrapJNI.VectorParameterRequest_isEmpty(this.swigCPtr, this);
    }

    public void ensureCapacity(long j) {
        IpmWrapJNI.VectorParameterRequest_ensureCapacity(this.swigCPtr, this, j);
    }

    public void clear() {
        IpmWrapJNI.VectorParameterRequest_clear(this.swigCPtr, this);
    }

    public void add(ParameterRequest parameterRequest) {
        IpmWrapJNI.VectorParameterRequest_add(this.swigCPtr, this, ParameterRequest.getCPtr(parameterRequest), parameterRequest);
    }

    public ParameterRequest get(long j) {
        return new ParameterRequest(IpmWrapJNI.VectorParameterRequest_get(this.swigCPtr, this, j), true);
    }

    public void set(int i, ParameterRequest parameterRequest) {
        IpmWrapJNI.VectorParameterRequest_set(this.swigCPtr, this, i, ParameterRequest.getCPtr(parameterRequest), parameterRequest);
    }
}
