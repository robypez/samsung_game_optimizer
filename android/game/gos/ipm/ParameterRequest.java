package com.samsung.android.game.gos.ipm;

import android.os.Parcel;
import android.os.Parcelable;

public class ParameterRequest implements Parcelable {
    public static final Parcelable.Creator<ParameterRequest> CREATOR = new Parcelable.Creator<ParameterRequest>() {
        public ParameterRequest createFromParcel(Parcel parcel) {
            return new ParameterRequest(parcel);
        }

        public ParameterRequest[] newArray(int i) {
            return new ParameterRequest[i];
        }
    };
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public int describeContents() {
        return 0;
    }

    protected ParameterRequest(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(ParameterRequest parameterRequest) {
        if (parameterRequest == null) {
            return 0;
        }
        return parameterRequest.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_ParameterRequest(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    private ParameterRequest(Parcel parcel) {
        this(parcel.readString(), Aggregation.fromInt(parcel.readInt()), parcel.readLong());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getParameter());
        parcel.writeInt(getAggregation().toInt());
        parcel.writeLong(getRate());
    }

    public String toString() {
        return String.format("{parameter=%s, aggregation=%s, rate=%s}", new Object[]{getParameter(), getAggregation(), Long.valueOf(getRate())});
    }

    public void setParameter(String str) {
        IpmWrapJNI.ParameterRequest_parameter_set(this.swigCPtr, this, str);
    }

    public String getParameter() {
        return IpmWrapJNI.ParameterRequest_parameter_get(this.swigCPtr, this);
    }

    public void setAggregation(Aggregation aggregation) {
        IpmWrapJNI.ParameterRequest_aggregation_set(this.swigCPtr, this, aggregation.swigValue());
    }

    public Aggregation getAggregation() {
        return Aggregation.swigToEnum(IpmWrapJNI.ParameterRequest_aggregation_get(this.swigCPtr, this));
    }

    public void setRate(long j) {
        IpmWrapJNI.ParameterRequest_rate_set(this.swigCPtr, this, j);
    }

    public long getRate() {
        return IpmWrapJNI.ParameterRequest_rate_get(this.swigCPtr, this);
    }

    public ParameterRequest() {
        this(IpmWrapJNI.new_ParameterRequest__SWIG_0(), true);
    }

    public ParameterRequest(String str, Aggregation aggregation, long j) {
        this(IpmWrapJNI.new_ParameterRequest__SWIG_1(str, aggregation.swigValue(), j), true);
    }
}
