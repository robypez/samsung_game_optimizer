package com.samsung.android.game.gos.controller;

import android.content.Context;
import com.samsung.android.game.gos.data.dbhelper.ReportDbHelper;
import com.samsung.android.game.gos.data.type.IntegratedReportData;
import com.samsung.android.game.gos.data.type.ReportData;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.NetworkUtil;
import com.samsung.android.game.gos.value.RinglogConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataUploader {
    private static final String LOG_TAG = DataUploader.class.getSimpleName();

    public static void uploadCombinationReportData(Context context, NetworkConnector networkConnector) {
        List<ReportData> reportDataBySize = ReportDbHelper.getInstance().getReportDataBySize(2097152, 100);
        int size = reportDataBySize.size();
        if (size == 0) {
            GosLog.i(LOG_TAG, "uploadCombinationReportData(), ReportDataTable empty or Db query failed !");
        } else if (networkConnector == null) {
            GosLog.i(LOG_TAG, "uploadCombinationReportData(), networkConnector is null!");
        } else {
            String str = LOG_TAG;
            GosLog.d(str, "uploadCombinationReportData(), curListSize: " + size);
            ArrayList arrayList = new ArrayList();
            while (size > 0) {
                if (!uploadReportData(reportDataBySize, networkConnector)) {
                    if (!NetworkUtil.isWifiConnected(context)) {
                        GosLog.w(LOG_TAG, "uploadCombinationReportData(), fail case!! wifi is not available now. just return");
                        return;
                    }
                    arrayList.add(BuildConfig.VERSION_NAME + size);
                }
                reportDataBySize = ReportDbHelper.getInstance().getReportDataBySize(2097152, 100);
                size = reportDataBySize.size();
            }
            if (!arrayList.isEmpty()) {
                String str2 = LOG_TAG;
                GosLog.w(str2, "uploadCombinationReportData(), failed when curListSize: " + arrayList);
            }
        }
    }

    static boolean uploadReportData(List<ReportData> list, NetworkConnector networkConnector) {
        if (list == null || networkConnector == null) {
            GosLog.w(LOG_TAG, "uploadReportData(), parameter is null!");
            return false;
        }
        String str = LOG_TAG;
        GosLog.d(str, "uploadReportData(), reportDataListToBeSent.size() : " + list.size());
        JSONObject uploadDataAsJsonObject = getUploadDataAsJsonObject(list);
        if (uploadDataAsJsonObject != null) {
            try {
                JSONObject jSONObject = uploadDataAsJsonObject.getJSONArray(ReportData.TAG_USER_USAGE).getJSONObject(0);
                String str2 = LOG_TAG;
                GosLog.d(str2, "uploadReportData(), names of user_usage: " + jSONObject.names());
            } catch (JSONException e) {
                String str3 = LOG_TAG;
                GosLog.d(str3, "uploadReportData() exception: " + e.getMessage());
            }
        }
        boolean postJson = networkConnector.postJson(uploadDataAsJsonObject);
        String str4 = LOG_TAG;
        GosLog.i(str4, "uploadReportData(), is server posting successful : " + postJson);
        for (ReportData id : list) {
            ReportDbHelper.getInstance().removeReportData(id.getId());
        }
        return postJson;
    }

    static JSONObject getUploadDataAsJsonObject(List<ReportData> list) {
        JSONArray jSONArray;
        HashMap hashMap = new HashMap();
        for (ReportData next : list) {
            String tag = next.getTag();
            String msg = next.getMsg();
            if (tag != null) {
                if (!hashMap.containsKey(tag)) {
                    jSONArray = new JSONArray();
                } else {
                    jSONArray = (JSONArray) hashMap.get(tag);
                    if (jSONArray == null) {
                    }
                }
                if (next instanceof IntegratedReportData) {
                    if (msg != null) {
                        if (!putIntegratedReportData(jSONArray, next, msg)) {
                        }
                    }
                } else if (msg != null) {
                    putMsg(jSONArray, msg);
                }
                hashMap.put(tag, jSONArray);
            }
        }
        JSONObject jSONObject = new JSONObject();
        try {
            ArrayList arrayList = new ArrayList();
            for (Map.Entry entry : hashMap.entrySet()) {
                jSONObject.put((String) entry.getKey(), entry.getValue());
                arrayList.add(((String) entry.getKey()) + "=" + ((JSONArray) entry.getValue()).length());
            }
            if (!arrayList.isEmpty()) {
                String str = LOG_TAG;
                GosLog.d(str, "uploadReportData()-getUploadDataAsJsonObject(), " + arrayList.toString());
            }
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return jSONObject;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0092 A[Catch:{ JSONException -> 0x00ad }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00a9 A[Catch:{ JSONException -> 0x00ad }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean putIntegratedReportData(org.json.JSONArray r7, com.samsung.android.game.gos.data.type.ReportData r8, java.lang.String r9) {
        /*
            com.samsung.android.game.gos.data.type.IntegratedReportData r8 = (com.samsung.android.game.gos.data.type.IntegratedReportData) r8
            java.lang.String r0 = r8.getGppRinglogMsg()
            java.lang.String r8 = r8.getGppRepMsg()
            r1 = 1
            java.lang.String r0 = getGppRinglogDataStr(r0)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r8 = getGppRepDataStr(r8)     // Catch:{ JSONException -> 0x00ad }
            com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper r2 = com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper.getInstance()     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r2 = r2.getRinglogMode()     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r3 = LOG_TAG     // Catch:{ JSONException -> 0x00ad }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x00ad }
            r4.<init>()     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r5 = "uploadReportData()-putIntegratedReportData(), gppRinglogDataStr: "
            r4.append(r5)     // Catch:{ JSONException -> 0x00ad }
            r4.append(r0)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r4 = r4.toString()     // Catch:{ JSONException -> 0x00ad }
            com.samsung.android.game.gos.util.GosLog.v(r3, r4)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r3 = LOG_TAG     // Catch:{ JSONException -> 0x00ad }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x00ad }
            r4.<init>()     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r5 = "uploadReportData()-putIntegratedReportData(), gppRepDataStr: "
            r4.append(r5)     // Catch:{ JSONException -> 0x00ad }
            r4.append(r8)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r4 = r4.toString()     // Catch:{ JSONException -> 0x00ad }
            com.samsung.android.game.gos.util.GosLog.v(r3, r4)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r3 = LOG_TAG     // Catch:{ JSONException -> 0x00ad }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x00ad }
            r4.<init>()     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r5 = "uploadReportData()-putIntegratedReportData(), ringlogUploadMode: "
            r4.append(r5)     // Catch:{ JSONException -> 0x00ad }
            r4.append(r2)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r4 = r4.toString()     // Catch:{ JSONException -> 0x00ad }
            com.samsung.android.game.gos.util.GosLog.v(r3, r4)     // Catch:{ JSONException -> 0x00ad }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00ad }
            r3.<init>(r9)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r9 = "raw"
            boolean r9 = java.util.Objects.equals(r2, r9)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r4 = "gpp_ringlog_data"
            r5 = 0
            if (r9 == 0) goto L_0x0074
            if (r0 == 0) goto L_0x008f
            r3.put(r4, r0)     // Catch:{ JSONException -> 0x00ad }
        L_0x0072:
            r8 = r1
            goto L_0x0090
        L_0x0074:
            java.lang.String r9 = "both"
            boolean r9 = java.util.Objects.equals(r2, r9)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r6 = "gpp_rep_data"
            if (r9 == 0) goto L_0x0089
            if (r0 != 0) goto L_0x0082
            if (r8 == 0) goto L_0x008f
        L_0x0082:
            r3.put(r4, r0)     // Catch:{ JSONException -> 0x00ad }
            r3.put(r6, r8)     // Catch:{ JSONException -> 0x00ad }
            goto L_0x0072
        L_0x0089:
            if (r8 == 0) goto L_0x008f
            r3.put(r6, r8)     // Catch:{ JSONException -> 0x00ad }
            goto L_0x0072
        L_0x008f:
            r8 = r5
        L_0x0090:
            if (r8 != 0) goto L_0x00a9
            java.lang.String r7 = LOG_TAG     // Catch:{ JSONException -> 0x00ad }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x00ad }
            r8.<init>()     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r9 = "uploadReportData()-putIntegratedReportData(), skip report due to no data, ringlogUploadMode="
            r8.append(r9)     // Catch:{ JSONException -> 0x00ad }
            r8.append(r2)     // Catch:{ JSONException -> 0x00ad }
            java.lang.String r8 = r8.toString()     // Catch:{ JSONException -> 0x00ad }
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r7, (java.lang.String) r8)     // Catch:{ JSONException -> 0x00ad }
            return r5
        L_0x00a9:
            r7.put(r3)     // Catch:{ JSONException -> 0x00ad }
            goto L_0x00b3
        L_0x00ad:
            r7 = move-exception
            java.lang.String r8 = LOG_TAG
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r8, (java.lang.Throwable) r7)
        L_0x00b3:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.controller.DataUploader.putIntegratedReportData(org.json.JSONArray, com.samsung.android.game.gos.data.type.ReportData, java.lang.String):boolean");
    }

    private static void putMsg(JSONArray jSONArray, String str) {
        try {
            jSONArray.put(new JSONObject(str));
        } catch (JSONException e) {
            try {
                JSONArray jSONArray2 = new JSONArray(str);
                for (int i = 0; i < jSONArray2.length(); i++) {
                    jSONArray.put(jSONArray2.get(i));
                }
            } catch (JSONException e2) {
                GosLog.w(LOG_TAG, (Throwable) e);
                GosLog.w(LOG_TAG, (Throwable) e2);
            }
        }
    }

    private static String getGppRinglogDataStr(String str) throws JSONException {
        if (str != null) {
            return new JSONObject(str).optString(RinglogConstants.SaveRinglogSession.DATABASE_KEY_RINGLOG_AND_GPP_REP, (String) null);
        }
        return null;
    }

    private static String getGppRepDataStr(String str) throws JSONException {
        if (str != null) {
            return new JSONObject(str).optString(RinglogConstants.SaveRinglogSession.DATABASE_KEY_RINGLOG_AND_GPP_REP, (String) null);
        }
        return null;
    }

    private DataUploader() {
    }
}
