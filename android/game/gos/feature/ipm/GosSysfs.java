package com.samsung.android.game.gos.feature.ipm;

import com.samsung.android.game.gos.ipm.Sysfs;
import com.samsung.android.game.gos.selibrary.SeGameManager;

public class GosSysfs implements Sysfs {
    private SeGameManager mSeGameManager;

    public GosSysfs(SeGameManager seGameManager) {
        this.mSeGameManager = seGameManager;
    }

    public String readSysFile(String str) {
        return this.mSeGameManager.readSysFile(str);
    }

    public String[] getSysFsData(String[] strArr) {
        return this.mSeGameManager.getSysFsData(strArr);
    }
}
