package com.samsung.android.game.gos.feature.mdswitchwifiblock;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import org.json.JSONObject;

public class MDSwitchWifiBlockCore {
    private static final String LOG_TAG = "MDSwitchWifiBlockCore";
    private static boolean sBlockStatus = false;
    private static ConnectivityManager sConnManager = null;
    private static boolean sNeedBlock = false;
    private static WifiManager sWifiManager;

    private MDSwitchWifiBlockCore() {
        sWifiManager = (WifiManager) AppContext.get().getSystemService("wifi");
        sConnManager = (ConnectivityManager) AppContext.get().getSystemService("connectivity");
    }

    public static MDSwitchWifiBlockCore getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onFocusIn(PkgData pkgData) {
        GosLog.i(LOG_TAG, "onFocusIn");
        sBlockStatus = false;
    }

    public void onFocusOut(PkgData pkgData) {
        GosLog.i(LOG_TAG, "onFocusOut");
        reset();
    }

    public boolean reset() {
        if (!sBlockStatus) {
            return false;
        }
        boolean disableWifiBlock = disableWifiBlock();
        if (disableWifiBlock) {
            sBlockStatus = false;
        }
        return disableWifiBlock;
    }

    public boolean enableWifiBlock() {
        NetworkInfo networkInfo = sConnManager.getNetworkInfo(0);
        sNeedBlock = networkInfo != null && networkInfo.isConnected();
        GosLog.i(LOG_TAG, "enableWifiBlock: enableWifiConnectivityManager sNeedBlock:" + sNeedBlock);
        if (!sNeedBlock || sBlockStatus || sWifiManager == null) {
            return false;
        }
        try {
            String requestWithJson = SeGameManager.getInstance().requestWithJson(ManagerInterface.Command.CONTROL_WIFI_SWITCH, getJsonForRequest(false));
            if (requestWithJson == null) {
                return false;
            }
            JSONObject jSONObject = new JSONObject(requestWithJson);
            if (!jSONObject.has(ManagerInterface.KeyName.VALUE_BOOL_1) || !jSONObject.getBoolean(ManagerInterface.KeyName.VALUE_BOOL_1)) {
                return false;
            }
            sBlockStatus = true;
            return true;
        } catch (Exception e) {
            GosLog.i(LOG_TAG, "Unable to setAutoReconnectEnabled e:" + e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean disableWifiBlock() {
        boolean z = false;
        if (sBlockStatus && sWifiManager != null) {
            try {
                String requestWithJson = SeGameManager.getInstance().requestWithJson(ManagerInterface.Command.CONTROL_WIFI_SWITCH, getJsonForRequest(true));
                if (requestWithJson != null) {
                    JSONObject jSONObject = new JSONObject(requestWithJson);
                    if (jSONObject.has(ManagerInterface.KeyName.VALUE_BOOL_1) && jSONObject.getBoolean(ManagerInterface.KeyName.VALUE_BOOL_1)) {
                        sBlockStatus = false;
                        z = true;
                    }
                }
            } catch (Exception e) {
                GosLog.i(LOG_TAG, "Unable to setAutoReconnectEnabled e:" + e);
                e.printStackTrace();
            }
            sWifiManager.startScan();
        }
        return z;
    }

    public String getJsonForRequest(boolean z) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(ManagerInterface.KeyName.VALUE_BOOL_1, z);
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final MDSwitchWifiBlockCore INSTANCE = new MDSwitchWifiBlockCore();

        private SingletonHolder() {
        }
    }
}
