package com.samsung.android.game.gos.data.model;

public class GosServiceUsage {
    public String callerPkgName;
    public String command;

    public GosServiceUsage(String str, String str2) {
        this.command = str;
        this.callerPkgName = str2;
    }
}
