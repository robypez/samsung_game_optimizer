package com.samsung.android.game.gos.feature.tsp;

import android.content.Intent;
import android.view.WindowManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.vrr.VrrFeature;
import com.samsung.android.game.gos.selibrary.SeInputManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.Constants;
import java.util.HashMap;
import java.util.Map;

public class TspFeature implements RuntimeInterface {
    static final int HZ_TO_CHECK_ENABLING = 240;
    static final int INPUT_FEATURE_ENABLE_VRR = 8;
    static final String INTENT_ACTION = "com.samsung.android.game.gos.action.TSP";
    static final String KEY_SET_GAME_MODE = "set_game_mode";
    static final String KEY_SET_SCAN_RATE = "set_scan_rate";
    private static final String LOG_TAG = TspFeature.class.getSimpleName();
    static final String VALUE_DISABLE = "0";
    static final String VALUE_ENABLE = "1";
    static final String VALUE_ENABLE_0 = "1,0";
    static final String VALUE_ENABLE_1 = "1,1";
    static final String VALUE_ENABLE_2 = "1,2";
    static final String VALUE_ENABLE_3 = "1,3";
    static final String VALUE_ENABLE_4 = "1,4";
    private boolean mForVrr;

    public String getName() {
        return Constants.V4FeatureFlag.TSP;
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    public static TspFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static TspFeature INSTANCE = new TspFeature();

        private SingletonHolder() {
        }
    }

    private TspFeature() {
        this.mForVrr = false;
        SeInputManager instance = SeInputManager.getInstance();
        if (instance != null) {
            this.mForVrr = isForVrr(instance.checkInputFeature());
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        HashMap hashMap = new HashMap();
        if (this.mForVrr) {
            int maxTouchHz = getMaxTouchHz(DbHelper.getInstance().getGlobalDao().getTspPolicy(), pkgData.getPkg().getTspPolicy());
            float f = 60.0f;
            WindowManager windowManager = (WindowManager) AppContext.get().getSystemService("window");
            if (windowManager != null) {
                f = windowManager.getDefaultDisplay().getMode().getRefreshRate();
            }
            String calculateTspPolicyByVrrCondition = calculateTspPolicyByVrrCondition(maxTouchHz, VrrFeature.getInstance().isHighRefreshRateMode(), f);
            if (calculateTspPolicyByVrrCondition != null) {
                hashMap.put(KEY_SET_SCAN_RATE, calculateTspPolicyByVrrCondition);
            }
        }
        hashMap.put(KEY_SET_GAME_MODE, VALUE_ENABLE);
        sendTspPolicyIntent(hashMap);
    }

    /* access modifiers changed from: package-private */
    public String calculateTspPolicyByVrrCondition(int i, boolean z, float f) {
        String str;
        if (i == HZ_TO_CHECK_ENABLING) {
            if (z) {
                if (ValidationUtil.floatEqual(f, 60.0f)) {
                    str = VALUE_ENABLE_1;
                } else if (ValidationUtil.floatEqual(f, 96.0f)) {
                    str = VALUE_ENABLE_4;
                } else if (ValidationUtil.floatEqual(f, 120.0f)) {
                    str = VALUE_ENABLE_2;
                }
            } else if (ValidationUtil.floatEqual(f, 48.0f)) {
                str = VALUE_ENABLE_3;
            } else if (ValidationUtil.floatEqual(f, 60.0f)) {
                str = VALUE_ENABLE_0;
            }
            str = null;
        } else {
            str = VALUE_DISABLE;
        }
        String str2 = LOG_TAG;
        GosLog.d(str2, "calculateTspPolicyByVrrCondition(), maxTspHz: " + i + ", highRRMode: " + z + ", appliedVrrHz: " + f + ", tspPolicy: " + str);
        return str;
    }

    public void onFocusOut(PkgData pkgData) {
        HashMap hashMap = new HashMap();
        if (this.mForVrr) {
            hashMap.put(KEY_SET_SCAN_RATE, VALUE_DISABLE);
        }
        hashMap.put(KEY_SET_GAME_MODE, VALUE_DISABLE);
        sendTspPolicyIntent(hashMap);
    }

    public void restoreDefault(PkgData pkgData) {
        if (pkgData != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(KEY_SET_SCAN_RATE, VALUE_DISABLE);
            hashMap.put(KEY_SET_GAME_MODE, VALUE_DISABLE);
            sendTspPolicyIntent(hashMap);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isForVrr(Integer num) {
        if (num == null) {
            return false;
        }
        boolean z = (num.intValue() & 8) == 8;
        String str = LOG_TAG;
        GosLog.i(str, "isAvailableForSystemHelper(), tspVrrInputAvailable: " + z);
        boolean isAvailableForSystemHelper = VrrFeature.getInstance().isAvailableForSystemHelper();
        String str2 = LOG_TAG;
        GosLog.i(str2, "isAvailableForSystemHelper(), vrrAvailable: " + isAvailableForSystemHelper);
        if (!z || !isAvailableForSystemHelper) {
            return false;
        }
        return true;
    }

    private void sendTspPolicyIntent(Map<String, String> map) {
        String str = LOG_TAG;
        GosLog.i(str, "sendTspPolicyIntent(), policyMap: " + map);
        if (map != null && !map.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(INTENT_ACTION);
            for (Map.Entry next : map.entrySet()) {
                intent.putExtra((String) next.getKey(), (String) next.getValue());
            }
            String str2 = LOG_TAG;
            GosLog.d(str2, "sendTspPolicyIntent(), intent extras: " + intent.getExtras());
            AppContext.get().sendBroadcast(intent);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0007, code lost:
        r3 = (com.samsung.android.game.gos.network.response.TspResponse) r0.fromJson(r3, com.samsung.android.game.gos.network.response.TspResponse.class);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getMaxTouchHz(java.lang.String r3, java.lang.String r4) {
        /*
            r2 = this;
            com.google.gson.Gson r0 = new com.google.gson.Gson
            r0.<init>()
            if (r3 == 0) goto L_0x001e
            java.lang.Class<com.samsung.android.game.gos.network.response.TspResponse> r1 = com.samsung.android.game.gos.network.response.TspResponse.class
            java.lang.Object r3 = r0.fromJson((java.lang.String) r3, r1)
            com.samsung.android.game.gos.network.response.TspResponse r3 = (com.samsung.android.game.gos.network.response.TspResponse) r3
            if (r3 == 0) goto L_0x001e
            com.samsung.android.game.gos.network.response.TspResponse$TouchHz r1 = r3.touchHz
            if (r1 == 0) goto L_0x001e
            com.samsung.android.game.gos.network.response.TspResponse$TouchHz r3 = r3.touchHz
            java.lang.Integer r3 = r3.max
            int r3 = r3.intValue()
            goto L_0x0020
        L_0x001e:
            r3 = 240(0xf0, float:3.36E-43)
        L_0x0020:
            if (r4 == 0) goto L_0x0038
            java.lang.Class<com.samsung.android.game.gos.network.response.TspResponse> r1 = com.samsung.android.game.gos.network.response.TspResponse.class
            java.lang.Object r4 = r0.fromJson((java.lang.String) r4, r1)
            com.samsung.android.game.gos.network.response.TspResponse r4 = (com.samsung.android.game.gos.network.response.TspResponse) r4
            if (r4 == 0) goto L_0x0038
            com.samsung.android.game.gos.network.response.TspResponse$TouchHz r0 = r4.touchHz
            if (r0 == 0) goto L_0x0038
            com.samsung.android.game.gos.network.response.TspResponse$TouchHz r3 = r4.touchHz
            java.lang.Integer r3 = r3.max
            int r3 = r3.intValue()
        L_0x0038:
            java.lang.String r4 = LOG_TAG
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "getMaxTouchHz(), maxTouchHz: "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r4, r0)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.tsp.TspFeature.getMaxTouchHz(java.lang.String, java.lang.String):int");
    }
}
