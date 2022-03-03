package com.samsung.android.game.gos.ipm;

import com.samsung.android.game.gos.ipm.system.SemGameManager;

public class AndroidGameManager extends GameManager {
    private final SemGameManager mSemGameManager;

    public AndroidGameManager(SemGameManager semGameManager) {
        this.mSemGameManager = semGameManager;
    }

    public String requestWithJson(String str, String str2) {
        String requestWithJson = this.mSemGameManager.requestWithJson(str, str2);
        return requestWithJson != null ? requestWithJson : BuildConfig.VERSION_NAME;
    }
}
