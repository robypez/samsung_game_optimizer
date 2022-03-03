package com.samsung.android.game.gos.data.model;

public class LocalLog {
    public long id;
    public String msg;
    public String tag;
    public String time;
    public long timeStamp;

    public static class Info {
        public String msg;
        public String tag;
        public String time;
        public long timeStamp;

        public Info(long j, String str, String str2, String str3) {
            this.timeStamp = j;
            this.time = str;
            this.tag = str2;
            this.msg = str3;
        }
    }
}
