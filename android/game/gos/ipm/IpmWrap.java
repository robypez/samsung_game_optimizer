package com.samsung.android.game.gos.ipm;

public class IpmWrap {
    public static int getKMlStateCount() {
        return IpmWrapJNI.kMlStateCount_get();
    }

    public static int getKMlActionCount() {
        return IpmWrapJNI.kMlActionCount_get();
    }
}
