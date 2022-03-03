package com.samsung.android.game.gos.data;

import android.content.Context;
import android.provider.Settings;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;

public class SystemDataHelper {
    private static final String LOG_TAG = SystemDataHelper.class.getSimpleName();
    private static final String SETTING_NAME_FOR_SURVEY_LOG = "samsung_errorlog_agree";
    private static int sTestAgree = 0;

    public static int getSamsungErrorlogAgree(Context context) {
        return Settings.System.getInt(context.getContentResolver(), SETTING_NAME_FOR_SURVEY_LOG, 0);
    }

    public static boolean isCollectingAgreedByUser(Context context) {
        int samsungErrorlogAgree = getSamsungErrorlogAgree(context);
        String str = LOG_TAG;
        GosLog.d(str, "isCollectingAgreedByUser(), samsung_errorlog_agree: " + samsungErrorlogAgree);
        if (AppVariable.isUnitTest()) {
            samsungErrorlogAgree = sTestAgree;
        }
        return samsungErrorlogAgree == 1;
    }

    public static void setTestAgree(int i) {
        sTestAgree = i;
    }
}
