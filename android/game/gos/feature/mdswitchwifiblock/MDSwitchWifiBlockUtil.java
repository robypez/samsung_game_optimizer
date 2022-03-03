package com.samsung.android.game.gos.feature.mdswitchwifiblock;

import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import org.json.JSONException;
import org.json.JSONObject;

public class MDSwitchWifiBlockUtil {
    private static final String LOG_TAG = MDSwitchWifiBlockUtil.class.getSimpleName();

    private MDSwitchWifiBlockUtil() {
    }

    public static MDSwitchWifiBlockUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final MDSwitchWifiBlockUtil INSTANCE = new MDSwitchWifiBlockUtil();

        private SingletonHolder() {
        }
    }

    public static String setBlockSwitchStatus(String str) {
        boolean z;
        JSONObject jSONObject = new JSONObject();
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
            if (new JSONObject(str).optInt(GosInterface.KeyName.BLOCK_MD_SWITCH_WIFI, 0) == 1) {
                z = MDSwitchWifiBlockCore.getInstance().enableWifiBlock();
            } else {
                z = MDSwitchWifiBlockCore.getInstance().reset();
            }
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, z);
            return jSONObject.toString();
        }
    }
}
