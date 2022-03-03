package com.samsung.android.game.gos.data.model;

public class Report {
    private int byteSize;
    private String gppRepAggregation;
    private String gppRepDataSchemeVersion;
    private long id;
    private String msg;
    private String ringlogMsg;
    private String tag;

    public static class IdAndSize {
        public long id;
        public int size;
    }

    public Report(long j) {
        this.id = j;
    }

    public long getId() {
        return this.id;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getRinglogMsg() {
        return this.ringlogMsg;
    }

    public void setRinglogMsg(String str) {
        this.ringlogMsg = str;
    }

    public int getByteSize() {
        return this.byteSize;
    }

    public void setByteSize(int i) {
        this.byteSize = i;
    }

    public String getGppRepAggregation() {
        return this.gppRepAggregation;
    }

    public void setGppRepAggregation(String str) {
        this.gppRepAggregation = str;
    }

    public String getGppRepDataSchemeVersion() {
        return this.gppRepDataSchemeVersion;
    }

    public void setGppRepDataSchemeVersion(String str) {
        this.gppRepDataSchemeVersion = str;
    }
}
