package com.samsung.android.game.gos.data.type;

public class IntegratedReportData extends ReportData {
    private String mGppRepMsg;
    private String mGppRinglogMsg;

    public IntegratedReportData(long j, String str, String str2, String str3) {
        super(j, ReportData.TAG_USER_USAGE, str);
        this.mGppRinglogMsg = str2;
        this.mGppRepMsg = str3;
    }

    public String getGppRinglogMsg() {
        return this.mGppRinglogMsg;
    }

    public String getGppRepMsg() {
        return this.mGppRepMsg;
    }

    public int getSize() {
        int size = super.getSize();
        String str = this.mGppRinglogMsg;
        if (str != null) {
            size += str.getBytes().length;
        }
        String str2 = this.mGppRepMsg;
        return str2 != null ? size + str2.getBytes().length : size;
    }
}
