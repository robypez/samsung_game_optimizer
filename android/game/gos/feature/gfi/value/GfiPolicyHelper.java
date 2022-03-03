package com.samsung.android.game.gos.feature.gfi.value;

import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.util.GosLog;
import org.json.JSONException;
import org.json.JSONObject;

public class GfiPolicyHelper {
    private static final int FRAME_CORRUPTION_FPS_LIMIT = 70;
    private static final String LOG_TAG = "GfiPolicyHelper";

    public void retrieveDefaultGFIPolicy(int i, JSONObject jSONObject) throws JSONException {
        int i2 = i;
        JSONObject jSONObject2 = jSONObject;
        GosLog.d(LOG_TAG, "GfiPolicyHelper::retrieveDefaultGFIPolicy, deviceMaxGameFps : " + i2);
        boolean z = 10 < i2;
        boolean has = jSONObject2.has(GfiPolicy.KEY_DFS_OFFSET);
        JSONObject jSONObject3 = null;
        JSONObject jSONObject4 = has ? jSONObject2.getJSONObject(GfiPolicy.KEY_DFS_OFFSET) : null;
        boolean has2 = jSONObject2.has(GfiPolicy.KEY_GFPS_OFFSET);
        if (has2) {
            jSONObject3 = jSONObject2.getJSONObject(GfiPolicy.KEY_GFPS_OFFSET);
        }
        jSONObject2.put("enabled", new Boolean(z));
        if (!z) {
            if (has) {
                jSONObject4.put("enabled", new Boolean(false));
            }
            if (has2) {
                jSONObject3.put("enabled", new Boolean(false));
                return;
            }
            return;
        }
        jSONObject2.put(GfiPolicy.KEY_INTERPOLATION_MODE, "retime");
        jSONObject2.put(GfiPolicy.KEY_AUTODELAY_ENABLED, new Boolean(true));
        jSONObject2.put(GfiPolicy.KEY_INTERPOLATION_FPS, new Integer(i2 - 10));
        jSONObject2.put(GfiPolicy.KEY_TARGET_DFS, new Double((double) i2));
        if (!has) {
            jSONObject4 = new JSONObject();
        }
        jSONObject4.put("enabled", new Boolean(true));
        jSONObject4.put("value", new Integer(10));
        jSONObject4.put(GfiPolicy.DfsOffset.KEY_SMOOTHNESS, new Double(0.8d));
        jSONObject4.put("minimum", new Integer(15));
        jSONObject4.put("maximum", new Integer(i2));
        if (!has) {
            jSONObject2.put(GfiPolicy.KEY_DFS_OFFSET, jSONObject4);
        }
        if (!has2) {
            jSONObject3 = new JSONObject();
        }
        jSONObject3.put("enabled", new Boolean(false));
        if (!has2) {
            jSONObject2.put(GfiPolicy.KEY_GFPS_OFFSET, jSONObject3);
        }
        GosLog.d(LOG_TAG, "GfiPolicyHelper::retrieveDefaultGFIPolicy, policy :" + jSONObject.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0079  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void applyGlobalPolicyforCreate(java.lang.String r9, org.json.JSONObject r10, java.lang.String r11) throws org.json.JSONException {
        /*
            r8 = this;
            java.lang.String r0 = "GfiPolicyHelper"
            java.lang.String r1 = "GfiPolicyHelper::applyGlobalPolicyforCreate"
            com.samsung.android.game.gos.util.GosLog.d(r0, r1)
            org.json.JSONObject r0 = new org.json.JSONObject
            r0.<init>(r9)
            int r9 = r11.hashCode()
            r1 = -1609594047(0xffffffffa00f8b41, float:-1.2158646E-19)
            r2 = 0
            r3 = 2
            r4 = 1
            java.lang.String r5 = "gfi_maximum_version"
            java.lang.String r6 = "gfi_minimum_version"
            java.lang.String r7 = "enabled"
            if (r9 == r1) goto L_0x0039
            r1 = 882980562(0x34a136d2, float:3.0028474E-7)
            if (r9 == r1) goto L_0x0031
            r1 = 1993419492(0x76d12ae4, float:2.1212109E33)
            if (r9 == r1) goto L_0x0029
            goto L_0x0041
        L_0x0029:
            boolean r9 = r11.equals(r5)
            if (r9 == 0) goto L_0x0041
            r9 = r4
            goto L_0x0042
        L_0x0031:
            boolean r9 = r11.equals(r6)
            if (r9 == 0) goto L_0x0041
            r9 = r2
            goto L_0x0042
        L_0x0039:
            boolean r9 = r11.equals(r7)
            if (r9 == 0) goto L_0x0041
            r9 = r3
            goto L_0x0042
        L_0x0041:
            r9 = -1
        L_0x0042:
            if (r9 == 0) goto L_0x0079
            if (r9 == r4) goto L_0x006b
            if (r9 == r3) goto L_0x0049
            goto L_0x0086
        L_0x0049:
            boolean r9 = r10.has(r7)
            if (r9 != 0) goto L_0x0086
            boolean r9 = r0.has(r7)
            if (r9 == 0) goto L_0x0062
            java.lang.Boolean r9 = new java.lang.Boolean
            boolean r11 = r0.getBoolean(r7)
            r9.<init>(r11)
            r10.put(r7, r9)
            goto L_0x0086
        L_0x0062:
            java.lang.Boolean r9 = new java.lang.Boolean
            r9.<init>(r2)
            r10.put(r7, r9)
            goto L_0x0086
        L_0x006b:
            boolean r9 = r0.has(r5)
            if (r9 == 0) goto L_0x0086
            java.lang.String r9 = r0.getString(r5)
            r10.put(r5, r9)
            goto L_0x0086
        L_0x0079:
            boolean r9 = r0.has(r6)
            if (r9 == 0) goto L_0x0086
            java.lang.String r9 = r0.getString(r6)
            r10.put(r6, r9)
        L_0x0086:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.value.GfiPolicyHelper.applyGlobalPolicyforCreate(java.lang.String, org.json.JSONObject, java.lang.String):void");
    }

    public void applyFlickeringFix(JSONObject jSONObject) throws JSONException {
        GosLog.d(LOG_TAG, "GfiPolicyHelper::applyFlickeringFix");
        jSONObject.put(GfiPolicy.KEY_NO_INTERP_WITH_EXTRA_LAYERS, new Boolean(true));
    }

    public void applyPriorityModeCheck(JSONObject jSONObject, boolean z) throws JSONException {
        if (!jSONObject.has("enabled")) {
            jSONObject.put("enabled", new Boolean(true));
            GosLog.d(LOG_TAG, "GfiPolicyHelper::applyPriorityModeCheck: priority : " + z + " policy :" + jSONObject.toString());
        }
    }
}
