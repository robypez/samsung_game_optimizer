package com.samsung.android.game.gos.ipm;

public interface Sysfs {
    String[] getSysFsData(String[] strArr);

    String readSysFile(String str);
}
