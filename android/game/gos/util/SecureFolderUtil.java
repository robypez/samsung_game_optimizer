package com.samsung.android.game.gos.util;

import com.samsung.android.game.gos.data.dbhelper.DbHelper;

public class SecureFolderUtil {
    private static final String LOG_TAG = SecureFolderUtil.class.getSimpleName();
    static final float MIN_SUPPORT_GMS_VERSION = 110.045f;

    public static boolean isSecureFolderUserId(int i) {
        return i >= 150 && i <= 160;
    }

    public static boolean isSupportSfGMS() {
        if (DbHelper.getInstance().getGlobalDao().getGmsVersion() >= MIN_SUPPORT_GMS_VERSION) {
            return true;
        }
        GosLog.e(LOG_TAG, "isSupportSF(). secure folder does not supported.");
        return false;
    }
}
