package com.samsung.android.game.gos.data.type;

import com.samsung.android.game.gos.value.RinglogPermission;

public class PerfPermissionData {
    private String mLastHandshakeTime;
    private String mParamListCsv;
    private RinglogPermission.PERM_POLICY mPermPolicy;
    private RinglogPermission.PERM_TYPES mPermType;
    private String mPkgName;

    public String getPkgName() {
        return this.mPkgName;
    }

    public void setPkgName(String str) {
        this.mPkgName = str;
    }

    public RinglogPermission.PERM_TYPES getPermType() {
        return this.mPermType;
    }

    public void setPermType(RinglogPermission.PERM_TYPES perm_types) {
        this.mPermType = perm_types;
    }

    public RinglogPermission.PERM_POLICY getPermPolicy() {
        return this.mPermPolicy;
    }

    public void setPermPolicy(RinglogPermission.PERM_POLICY perm_policy) {
        this.mPermPolicy = perm_policy;
    }

    public String getParamListCsv() {
        return this.mParamListCsv;
    }

    public void setParamListCsv(String str) {
        this.mParamListCsv = str;
    }

    public String getLastHandshakeTime() {
        return this.mLastHandshakeTime;
    }

    public void setLastHandshakeTime(String str) {
        this.mLastHandshakeTime = str;
    }

    public String toString() {
        return "PerfPermissionData{mPkgName='" + this.mPkgName + '\'' + ", mPermType=" + this.mPermType + ", mPermPolicy=" + this.mPermPolicy + ", mParamListCsv='" + this.mParamListCsv + '\'' + ", mLastHandshakeTime=" + this.mLastHandshakeTime + '}';
    }
}
