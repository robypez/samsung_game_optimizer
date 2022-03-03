package com.samsung.android.game.gos.feature.ringlog;

import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.ipm.Aggregation;
import com.samsung.android.game.gos.ipm.ParameterRequest;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.RinglogConstants;
import com.samsung.android.game.gos.value.RinglogPermission;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RinglogInterface {
    private static final String TAG = "RinglogInterface";
    private RinglogPermissionHandler mPermissionHandler;
    private String mTestIpmSessions;

    private RinglogInterface() {
        this.mTestIpmSessions = "{\"latest_session\":1,\"1\":{}}";
        this.mPermissionHandler = RinglogPermissionHandler.getInstance();
    }

    public static RinglogInterface getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final RinglogInterface INSTANCE = new RinglogInterface();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: package-private */
    public void restoreTestIpmSessions() {
        this.mTestIpmSessions = "{\"latest_session\":1,\"1\":{}}";
    }

    /* access modifiers changed from: package-private */
    public void setTestIpmSessions(String str) {
        this.mTestIpmSessions = str;
    }

    public String handshakeJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            boolean handshake = handshake();
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, handshake);
            if (!handshake) {
                jSONObject.put(GosInterface.KeyName.COMMENT, "Permission denied.");
            }
        } catch (JSONException e) {
            GosLog.e(TAG, "handshakeJSON " + e);
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "handshakeJSON response = " + jSONObject.toString());
        }
        return jSONObject.toString();
    }

    public String getAvailableParametersJSON() {
        String callerPkgName = RinglogPermissionHandler.getCallerPkgName();
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.mPermissionHandler.isCallerDisallowed()) {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Must register via PERF_DATA_REGISTER(handshake) before this API.");
                return jSONObject.toString();
            }
            JSONArray jSONArray = new JSONArray();
            Set<RinglogConstants.PerfParams> allowedParams = this.mPermissionHandler.getAllowedParams(callerPkgName);
            for (RinglogConstants.PerfParams next : allowedParams) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(GosInterface.KeyName.PARAM, next.getName());
                jSONObject2.put(GosInterface.KeyName.PARAM_TYPE, next.getParameterType().ordinal());
                jSONObject2.put(GosInterface.KeyName.PARAM_DESCRIPTION, next.getDescription());
                jSONArray.put(jSONObject2);
            }
            if (allowedParams.isEmpty()) {
                jSONObject.put(GosInterface.KeyName.COMMENT, "Permission may be denied.");
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            } else {
                jSONObject.put(GosInterface.KeyName.PARAMS, jSONArray);
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, true);
            }
            if (RinglogConstants.DEBUG) {
                GosLog.i(TAG, "getAvailableParametersJSON response = " + jSONObject.toString());
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            GosLog.e(TAG, "exception during getAvailableParametersJSON " + e);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:26|27|28|29|30|44|41|24) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0098 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getAvailableSessionsJSON(java.lang.String r9, java.lang.String r10) {
        /*
            r8 = this;
            java.lang.String r0 = "RinglogInterface"
            java.lang.String r1 = "successful"
            org.json.JSONObject r2 = new org.json.JSONObject
            r2.<init>()
            r3 = 0
            r2.put(r1, r3)     // Catch:{ JSONException -> 0x00aa }
            com.samsung.android.game.gos.feature.ringlog.RinglogPermissionHandler r4 = r8.mPermissionHandler     // Catch:{ JSONException -> 0x00aa }
            boolean r9 = r4.isCallerDisallowed(r9)     // Catch:{ JSONException -> 0x00aa }
            if (r9 == 0) goto L_0x0021
            java.lang.String r9 = "comment"
            java.lang.String r10 = "Must register via PERF_DATA_REGISTER(handshake) before this API."
            r2.put(r9, r10)     // Catch:{ JSONException -> 0x00aa }
            java.lang.String r9 = r2.toString()     // Catch:{ JSONException -> 0x00aa }
            return r9
        L_0x0021:
            r9 = 0
            java.lang.String r4 = "session"
            if (r10 == 0) goto L_0x0051
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00aa }
            r5.<init>(r10)     // Catch:{ JSONException -> 0x00aa }
            boolean r10 = r5.has(r4)     // Catch:{ JSONException -> 0x00aa }
            if (r10 == 0) goto L_0x004b
            org.json.JSONArray r9 = r5.getJSONArray(r4)     // Catch:{ JSONException -> 0x00aa }
            int r10 = r9.length()     // Catch:{ JSONException -> 0x00aa }
            int[] r10 = new int[r10]     // Catch:{ JSONException -> 0x00aa }
        L_0x003b:
            int r6 = r9.length()     // Catch:{ JSONException -> 0x00aa }
            if (r3 >= r6) goto L_0x004a
            int r6 = r9.getInt(r3)     // Catch:{ JSONException -> 0x00aa }
            r10[r3] = r6     // Catch:{ JSONException -> 0x00aa }
            int r3 = r3 + 1
            goto L_0x003b
        L_0x004a:
            r9 = r10
        L_0x004b:
            java.lang.String r10 = "gson_compatible_response"
            boolean r3 = r5.optBoolean(r10)     // Catch:{ JSONException -> 0x00aa }
        L_0x0051:
            boolean r10 = com.samsung.android.game.gos.value.AppVariable.isUnitTest()     // Catch:{ JSONException -> 0x00aa }
            if (r10 == 0) goto L_0x005a
            java.lang.String r9 = r8.mTestIpmSessions     // Catch:{ JSONException -> 0x00aa }
            goto L_0x0066
        L_0x005a:
            android.app.Application r10 = com.samsung.android.game.gos.context.AppContext.get()     // Catch:{ JSONException -> 0x00aa }
            com.samsung.android.game.gos.feature.ipm.IpmCore r10 = com.samsung.android.game.gos.feature.ipm.IpmCore.getInstance(r10)     // Catch:{ JSONException -> 0x00aa }
            java.lang.String r9 = r10.readSessionsJSON(r9)     // Catch:{ JSONException -> 0x00aa }
        L_0x0066:
            if (r3 != 0) goto L_0x006f
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00aa }
            r10.<init>(r9)     // Catch:{ JSONException -> 0x00aa }
            r2 = r10
            goto L_0x00a5
        L_0x006f:
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00aa }
            r10.<init>(r9)     // Catch:{ JSONException -> 0x00aa }
            java.util.Iterator r9 = r10.keys()     // Catch:{ JSONException -> 0x00aa }
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ JSONException -> 0x00aa }
            r3.<init>()     // Catch:{ JSONException -> 0x00aa }
        L_0x007d:
            boolean r5 = r9.hasNext()     // Catch:{ JSONException -> 0x00aa }
            if (r5 == 0) goto L_0x00a0
            java.lang.Object r5 = r9.next()     // Catch:{ JSONException -> 0x00aa }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ JSONException -> 0x00aa }
            int r6 = java.lang.Integer.parseInt(r5)     // Catch:{ NumberFormatException -> 0x0098 }
            org.json.JSONObject r7 = r10.getJSONObject(r5)     // Catch:{ NumberFormatException -> 0x0098 }
            r7.put(r4, r6)     // Catch:{ NumberFormatException -> 0x0098 }
            r3.put(r7)     // Catch:{ NumberFormatException -> 0x0098 }
            goto L_0x007d
        L_0x0098:
            java.lang.Object r6 = r10.get(r5)     // Catch:{ JSONException -> 0x00aa }
            r2.put(r5, r6)     // Catch:{ JSONException -> 0x00aa }
            goto L_0x007d
        L_0x00a0:
            java.lang.String r9 = "session_data"
            r2.put(r9, r3)     // Catch:{ JSONException -> 0x00aa }
        L_0x00a5:
            r9 = 1
            r2.put(r1, r9)     // Catch:{ JSONException -> 0x00aa }
            goto L_0x00bf
        L_0x00aa:
            r9 = move-exception
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r1 = "Exception during readSessionsJSON"
            r10.append(r1)
            r10.append(r9)
            java.lang.String r9 = r10.toString()
            com.samsung.android.game.gos.util.GosLog.e(r0, r9)
        L_0x00bf:
            boolean r9 = com.samsung.android.game.gos.value.RinglogConstants.DEBUG
            if (r9 == 0) goto L_0x00db
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "readSessionsJSON response = "
            r9.append(r10)
            java.lang.String r10 = r2.toString()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            com.samsung.android.game.gos.util.GosLog.i(r0, r9)
        L_0x00db:
            java.lang.String r9 = r2.toString()
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ringlog.RinglogInterface.getAvailableSessionsJSON(java.lang.String, java.lang.String):java.lang.String");
    }

    public String readDataSimpleRequestJSON(String str, String str2) {
        if (str2 == null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Invalid parameters.");
                return jSONObject.toString();
            } catch (JSONException e) {
                GosLog.e(TAG, "Exception during readDataSimpleRequestJSON " + e);
                return "{}";
            }
        } else {
            JSONObject jSONObject2 = new JSONObject(str2);
            JSONArray jSONArray = jSONObject2.getJSONArray(GosInterface.KeyName.PARAMS);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                String string = jSONObject3.getString(GosInterface.KeyName.PARAM);
                int i2 = jSONObject3.getInt(GosInterface.KeyName.AGG_MODE);
                arrayList.add(new ParameterRequest(string, Aggregation.fromInt(i2), jSONObject3.getLong(GosInterface.KeyName.RATE)));
            }
            return readDataSimpleRequestJSON(str, arrayList, jSONObject2.getInt("session"), jSONObject2.getLong("start"), jSONObject2.getLong(GosInterface.KeyName.END));
        }
    }

    public String readDataSimpleRequestJSON(String str, List<ParameterRequest> list, int i, long j, long j2) {
        String str2;
        String str3 = str;
        int i2 = i;
        long j3 = j;
        long j4 = j2;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            if (this.mPermissionHandler.isCallerDisallowed(str3)) {
                jSONObject.put(GosInterface.KeyName.COMMENT, "Must register via perf_data_register(handshake) before this API.");
                return jSONObject.toString();
            }
            if (list != null) {
                if (!list.isEmpty()) {
                    if (RinglogConstants.DEBUG) {
                        GosLog.i(TAG, "readDataSimpleRequestJSON request= " + Arrays.toString(list.toArray()) + ", sessionId=" + i2 + ", start=" + j3 + ", endTime=" + j4);
                    } else {
                        GosLog.i(TAG, "simpleReq, " + i2 + ", " + j3 + "," + j4);
                    }
                    Set<String> allowedParamsStr = this.mPermissionHandler.getAllowedParamsStr(str3);
                    ArrayList arrayList = new ArrayList();
                    for (ParameterRequest next : list) {
                        if (next.getParameter() == null || !allowedParamsStr.contains(next.getParameter()) || !RinglogUtil.validateAggr(next.getAggregation().toInt()) || !RinglogUtil.validateRate(next.getRate())) {
                            jSONObject.put(GosInterface.KeyName.COMMENT, "Some parameters are invalid or permission is denied.");
                        } else {
                            arrayList.add(next);
                        }
                    }
                    if (AppVariable.isUnitTest()) {
                        str2 = "{\"temp_lrpst\":[42],\"temp_pst\":[42]}";
                    } else {
                        str2 = IpmCore.getInstance(AppContext.get()).readDataJSON(arrayList, i, j, j2);
                    }
                    JSONObject jSONObject2 = new JSONObject(str2);
                    Iterator<String> keys = jSONObject2.keys();
                    while (keys.hasNext()) {
                        String next2 = keys.next();
                        jSONObject.put(next2, modifySpaResponse(jSONObject2, next2));
                    }
                    jSONObject.put(GosInterface.KeyName.SUCCESSFUL, true);
                    if (RinglogConstants.DEBUG) {
                        GosLog.i(TAG, "readDataSimpleRequestJSON response= " + jSONObject.toString());
                    }
                    return jSONObject.toString();
                }
            }
            jSONObject.put(GosInterface.KeyName.COMMENT, "Invalid parameters.");
            return jSONObject.toString();
        } catch (JSONException e) {
            GosLog.e(TAG, "Exception during readDataSimpleRequestJSON " + e);
        }
    }

    /* access modifiers changed from: package-private */
    public Object modifySpaResponse(JSONObject jSONObject, String str) throws JSONException {
        Object obj = jSONObject.get(str);
        if (!str.equals(Constants.RingLog.Parameter.BATTERY_PREDICTION) || !(obj instanceof Integer)) {
            return obj;
        }
        int intValue = ((Integer) obj).intValue();
        if (intValue > 72000) {
            if (RinglogConstants.DEBUG) {
                GosLog.i(TAG, "modifySpaResponse from " + intValue + ", to " + 0);
            }
            intValue = 0;
        }
        return Integer.valueOf(intValue);
    }

    private boolean handshake() {
        String callerPkgName = RinglogPermissionHandler.getCallerPkgName();
        if (this.mPermissionHandler.match_signature()) {
            this.mPermissionHandler.assignPermission(callerPkgName, RinglogPermission.PERM_POLICY.SIGNATURE, RinglogPermission.PERM_TYPES.ALLOW_ALL, (List<RinglogConstants.PerfParams>) null);
        }
        Set<String> allowedParamsStr = this.mPermissionHandler.getAllowedParamsStr(callerPkgName);
        return allowedParamsStr != null && !allowedParamsStr.isEmpty();
    }
}
