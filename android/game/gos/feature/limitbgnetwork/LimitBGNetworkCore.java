package com.samsung.android.game.gos.feature.limitbgnetwork;

import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LimitBGNetworkCore {
    private static final String LOG_TAG = LimitBGNetworkCore.class.getSimpleName();
    private static boolean sBlockStatus = false;
    private static String sCurrentPackageName = null;
    private JSONObject requestJson;

    private LimitBGNetworkCore() {
        this.requestJson = null;
    }

    public static LimitBGNetworkCore getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onFocusIn(PkgData pkgData) {
        GosLog.d(LOG_TAG, "onFocusIn");
        sCurrentPackageName = pkgData.getPackageName();
    }

    public boolean onFocusOut() {
        JSONObject jSONObject;
        GosLog.d(LOG_TAG, "onFocusOut");
        if (!sBlockStatus || (jSONObject = this.requestJson) == null) {
            return true;
        }
        try {
            jSONObject.put(ManagerInterface.KeyName.VALUE_BOOL_1, true);
            blockNetworkByUid(this.requestJson.toString());
            this.requestJson = null;
            sCurrentPackageName = null;
            return true;
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return false;
        }
    }

    public boolean reset() {
        JSONObject jSONObject;
        GosLog.d(LOG_TAG, "reset");
        if (!sBlockStatus || (jSONObject = this.requestJson) == null) {
            sBlockStatus = false;
            this.requestJson = null;
            sCurrentPackageName = null;
            return true;
        }
        try {
            jSONObject.put(ManagerInterface.KeyName.VALUE_BOOL_1, true);
            boolean blockNetworkByUid = blockNetworkByUid(this.requestJson.toString());
            this.requestJson = null;
            return blockNetworkByUid;
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return false;
        }
    }

    public boolean blockNetworkByUid(String str) {
        String str2 = LOG_TAG;
        GosLog.d(str2, "blockNetworkByUid jsonParam:" + str);
        if (str == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.put(ManagerInterface.KeyName.VALUE_STRING_1, sCurrentPackageName);
            boolean optBoolean = jSONObject.optBoolean(ManagerInterface.KeyName.VALUE_BOOL_1);
            String requestWithJson = SeGameManager.getInstance().requestWithJson(ManagerInterface.Command.BLOCK_BG_NET, jSONObject.toString());
            String str3 = LOG_TAG;
            GosLog.d(str3, "blockNetworkByUid response:" + requestWithJson);
            if (requestWithJson == null) {
                return false;
            }
            JSONObject jSONObject2 = new JSONObject(requestWithJson);
            if (!jSONObject2.has(ManagerInterface.KeyName.VALUE_BOOL_1) || !jSONObject2.getBoolean(ManagerInterface.KeyName.VALUE_BOOL_1)) {
                return false;
            }
            if (!optBoolean) {
                this.requestJson = jSONObject;
                sBlockStatus = true;
            } else {
                this.requestJson = null;
                sBlockStatus = false;
            }
            return true;
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return false;
        }
    }

    /* JADX INFO: finally extract failed */
    public String getJsonForArrayForTest() {
        ArrayList arrayList = new ArrayList();
        if (arrayList.size() == 0) {
            if (!arrayList.contains("com.wandoujia.phoenix2")) {
                arrayList.add("com.wandoujia.phoenix2");
            }
            if (!arrayList.contains("com.tencent.android.qqdownloader")) {
                arrayList.add("com.tencent.android.qqdownloader");
            }
            GosLog.d(LOG_TAG, "getJsonForArrayForTest mPkgNames.size:" + arrayList.size());
        }
        try {
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("package_name", arrayList.get(i));
                jSONArray.put(i, jSONObject);
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(ManagerInterface.KeyName.PACKAGE_NAME_LIST, jSONArray);
            jSONObject2.put(ManagerInterface.KeyName.VALUE_BOOL_1, false);
            String jSONObject3 = jSONObject2.toString();
            arrayList.clear();
            return jSONObject3;
        } catch (Exception e) {
            e.printStackTrace();
            arrayList.clear();
            return null;
        } catch (Throwable th) {
            arrayList.clear();
            throw th;
        }
    }

    public String getCurrentGamePkg() {
        return sCurrentPackageName;
    }

    public boolean resumeOk() {
        return sCurrentPackageName != null;
    }

    public boolean pauseOk() {
        return !sBlockStatus;
    }

    public boolean resetOk() {
        return !sBlockStatus;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final LimitBGNetworkCore INSTANCE = new LimitBGNetworkCore();

        private SingletonHolder() {
        }
    }
}
