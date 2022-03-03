package com.samsung.android.game.gos.network.response;

import com.samsung.android.game.gos.util.GosLog;
import org.json.JSONObject;

public class PostResponse extends BaseResponse {
    private static final String DESCRIPTION = "message";
    private static final String LOG_TAG = PostResponse.class.getSimpleName();
    private static final String RESULT_CODE = "code";
    private static final int STATUS_CODE_SUCCESS = 201001;

    public static boolean parseResponseSuccess(String str) {
        GosLog.d(LOG_TAG, "parseResponseSuccess()");
        if (str == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            int i = jSONObject.getInt(RESULT_CODE);
            String str2 = LOG_TAG;
            GosLog.d(str2, "code: " + i);
            String string = jSONObject.getString(DESCRIPTION);
            String str3 = LOG_TAG;
            GosLog.d(str3, "message: " + string);
            if (i == STATUS_CODE_SUCCESS) {
                return true;
            }
            String str4 = LOG_TAG;
            GosLog.i(str4, "Response status code: " + i + ", message: " + string);
            return false;
        } catch (Exception e) {
            String str5 = LOG_TAG;
            GosLog.w(str5, "response: " + str, e);
            return false;
        }
    }
}
