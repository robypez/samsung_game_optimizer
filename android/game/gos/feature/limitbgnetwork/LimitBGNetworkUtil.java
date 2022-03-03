package com.samsung.android.game.gos.feature.limitbgnetwork;

import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import org.json.JSONException;
import org.json.JSONObject;

public class LimitBGNetworkUtil {
    private static final String LOG_TAG = LimitBGNetworkUtil.class.getSimpleName();

    private LimitBGNetworkUtil() {
    }

    public static String setLimitBGNetwork(String str) {
        JSONObject jSONObject = new JSONObject();
        boolean z = false;
        if (str == null) {
            try {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Invalid parameters.");
                GosLog.e(LOG_TAG, "setBlockSwitchStatus invalid request");
                return jSONObject.toString();
            } catch (JSONException e) {
                String str2 = LOG_TAG;
                GosLog.e(str2, "Exception during setBlockSwitchStatus " + e.getMessage());
            }
        } else {
            if (FeatureHelper.isAvailable("limit_bg_network")) {
                z = LimitBGNetworkCore.getInstance().blockNetworkByUid(str);
            }
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, z);
            return jSONObject.toString();
        }
    }
}
