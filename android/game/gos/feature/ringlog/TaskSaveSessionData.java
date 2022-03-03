package com.samsung.android.game.gos.feature.ringlog;

import android.util.Base64;
import android.util.Pair;
import com.google.gson.Gson;
import com.samsung.android.game.gos.data.dbhelper.ReportDbHelper;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.RinglogConstants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import datautil.IDataUtil;
import datautil.IDataUtilImpl;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskSaveSessionData implements Runnable {
    private static final int COLLECTION_RATE = 1000;
    private static final String TAG = "TaskSaveSessionData";
    private long mReportTimestampKey;
    private String mSessionInfo;
    private RinglogConstants.SessionWrapper mSessionWrapper;

    /* synthetic */ TaskSaveSessionData(RinglogConstants.SessionWrapper sessionWrapper, AnonymousClass1 r2) {
        this(sessionWrapper);
    }

    private TaskSaveSessionData(RinglogConstants.SessionWrapper sessionWrapper) {
        this.mSessionWrapper = sessionWrapper;
        if (sessionWrapper != null) {
            GosLog.i(TAG, "TaskSaveSessionData id " + sessionWrapper.id);
        }
    }

    /* access modifiers changed from: private */
    public void setSessionInfoStr(String str) {
        this.mSessionInfo = str;
    }

    /* access modifiers changed from: private */
    public void setLastGamePauseTime(long j) {
        this.mReportTimestampKey = j;
    }

    public void run() {
        RinglogConstants.SessionWrapper sessionWrapper = this.mSessionWrapper;
        int i = sessionWrapper != null ? sessionWrapper.id : -1;
        if (i < 0) {
            GosLog.e(TAG, "run, no session to save");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            GosLog.i(TAG, "run mSessionInfo=" + this.mSessionInfo + ", mSessionWrapper=" + this.mSessionWrapper);
            if (this.mSessionInfo == null) {
                StringBuilder sb = new StringBuilder();
                RinglogUtil.getSessionInfo("com.samsung.android.game.gos", i, sb);
                this.mSessionInfo = sb.toString();
            }
            if (this.mSessionWrapper == null) {
                this.mSessionWrapper = (RinglogConstants.SessionWrapper) new Gson().fromJson(this.mSessionInfo, RinglogConstants.SessionWrapper.class);
            }
            if (!(this.mSessionInfo == null || this.mSessionWrapper == null)) {
                if (this.mSessionWrapper.info != null) {
                    ReportDbHelper.getInstance().updateReportSizeAll();
                    addOrUpdateGppData(getSessionInfo(), getPerformanceData(), this.mReportTimestampKey, currentTimeMillis);
                    return;
                }
            }
            GosLog.i(TAG, "run, invalid sessionInfo or sessionWrapper, return");
        } catch (UnsupportedEncodingException | NullPointerException | JSONException e) {
            GosLog.e(TAG, "run, " + e);
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public void addOrUpdateGppData(JSONObject jSONObject, JSONObject jSONObject2, long j, long j2) throws UnsupportedEncodingException, JSONException {
        ReportDbHelper instance = ReportDbHelper.getInstance();
        JSONObject compressedRinglogSummaryJson = getCompressedRinglogSummaryJson(jSONObject, jSONObject2);
        if (compressedRinglogSummaryJson == null) {
            GosLog.e(TAG, "run, compressedRinglog is empty");
            return;
        }
        instance.addOrUpdateGameSessionGppRinglogData(j, compressedRinglogSummaryJson.toString());
        GosLog.i(TAG, "run ringLog logged, timestamp=" + j + ", time taken=" + (System.currentTimeMillis() - j2));
        instance.addOrUpdateGameSessionGppRepData(j, getCompressedGppRepWithGspJson(jSONObject2).toString(), IDataUtil.DATA_SCHEME_VERSION_VALUE);
        GosLog.i(TAG, "run gppRepAggregation logged, data_scheme_version=2021.09.01");
    }

    private JSONObject getCompressedRinglogSummaryJson(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException, UnsupportedEncodingException {
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("session", jSONObject);
        jSONObject3.put(RinglogConstants.SaveRinglogSession.SERVER_KEY_PERFORMANCE_DATA_COLLECTION_RATE, 1000);
        jSONObject3.put(RinglogConstants.SaveRinglogSession.SERVER_KEY_PERFORMANCE_DATA, jSONObject2);
        byte[] compressString = compressString(jSONObject3.toString());
        if (compressString == null) {
            return null;
        }
        String encodeBytes = encodeBytes(compressString);
        JSONObject jSONObject4 = new JSONObject();
        jSONObject4.put(RinglogConstants.SaveRinglogSession.DATABASE_KEY_RINGLOG_AND_GPP_REP, encodeBytes);
        return jSONObject4;
    }

    private JSONObject getSessionInfo() throws JSONException {
        return convertKeysToSnakeCase(filterJsonObject(new JSONObject(this.mSessionInfo), RinglogConstants.SaveRinglogSession.SessionKeysExcluded));
    }

    private JSONObject getPerformanceData() throws JSONException {
        String str;
        try {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            for (RinglogConstants.PerfParams name : RinglogConstants.SaveRinglogSession.ParameterKeys) {
                jSONArray.put(new JSONObject().put(GosInterface.KeyName.PARAM, name.getName()).put(GosInterface.KeyName.AGG_MODE, 0).put(GosInterface.KeyName.RATE, 1000));
            }
            jSONObject.put(GosInterface.KeyName.PARAMS, jSONArray).put("start", 0).put(GosInterface.KeyName.END, 0).put("session", this.mSessionWrapper.id);
            str = RinglogInterface.getInstance().readDataSimpleRequestJSON("com.samsung.android.game.gos", jSONObject.toString());
        } catch (JSONException e) {
            GosLog.e(TAG, "getPerformanceData " + e);
            str = "{}";
        }
        JSONObject jSONObject2 = new JSONObject(str);
        jSONObject2.remove(GosInterface.KeyName.SUCCESSFUL);
        return convertKeysToSnakeCase(jSONObject2);
    }

    private JSONObject getCompressedGppRepWithGspJson(JSONObject jSONObject) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            IDataUtilImpl instance = IDataUtilImpl.getInstance();
            instance.setPSTGapByModelName(AppVariable.getOriginalModelName());
            ArrayList arrayList = new ArrayList();
            for (Pair<RinglogConstants.PerfParams, IDataUtil.AggType[]> pair : RinglogConstants.SaveRinglogSession.ParameterGspRepAggregationPairs) {
                RinglogConstants.PerfParams perfParams = (RinglogConstants.PerfParams) pair.first;
                arrayList.add(instance.getAggregatedValues((IDataUtil.AggType[]) pair.second, getListFromJsonArray(jSONObject.optJSONArray(perfParams.getName()), perfParams.getParameterType())));
            }
            jSONObject2.put(RinglogConstants.SaveRinglogSession.DATABASE_KEY_RINGLOG_AND_GPP_REP, encodeBytes(compressString((String) arrayList.stream().collect(Collectors.joining(",", "{", "}")))));
        } catch (Exception e) {
            GosLog.e(TAG, "getAggregationWithGsp " + e);
        }
        return jSONObject2;
    }

    private static List<Object> getListFromJsonArray(JSONArray jSONArray, RinglogConstants.ParameterDataType parameterDataType) throws JSONException {
        ArrayList arrayList = new ArrayList();
        if (jSONArray == null) {
            return arrayList;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            int i2 = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$ParameterDataType[parameterDataType.ordinal()];
            if (i2 == 1) {
                arrayList.add(Long.valueOf(jSONArray.getLong(i)));
            } else if (i2 != 2) {
                arrayList.add(Double.valueOf(jSONArray.getDouble(i)));
            } else {
                arrayList.add(Integer.valueOf(jSONArray.getInt(i)));
            }
        }
        return arrayList;
    }

    /* renamed from: com.samsung.android.game.gos.feature.ringlog.TaskSaveSessionData$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$ParameterDataType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType[] r0 = com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$ParameterDataType = r0
                com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType r1 = com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType.LONG     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$ParameterDataType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType r1 = com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType.INTEGER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$ParameterDataType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType r1 = com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType.FLOAT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ringlog.TaskSaveSessionData.AnonymousClass1.<clinit>():void");
        }
    }

    private static byte[] compressString(String str) {
        return TypeConverter.compressBytesGzip(str.getBytes(StandardCharsets.UTF_8));
    }

    private static String encodeBytes(byte[] bArr) {
        return Base64.encodeToString(bArr, 0);
    }

    private static JSONObject filterJsonObject(JSONObject jSONObject, String[] strArr) {
        if (!(jSONObject == null || strArr == null || strArr.length <= 0)) {
            for (String remove : strArr) {
                jSONObject.remove(remove);
            }
        }
        return jSONObject;
    }

    private JSONObject convertKeysToSnakeCase(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return null;
        }
        JSONObject jSONObject2 = new JSONObject(jSONObject.toString());
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            String lowerCase = next.replaceAll("([0-9]*[a-z]+[0-9]*)([0-9]*[A-Z]+[0-9]*)", "$1_$2").toLowerCase(Locale.ROOT);
            if (!next.equals(lowerCase)) {
                jSONObject2.remove(next);
                jSONObject2.put(lowerCase, jSONObject.get(next));
            }
        }
        return jSONObject2;
    }

    static class Builder {
        TaskSaveSessionData mTaskSaveSessionData;

        Builder(RinglogConstants.SessionWrapper sessionWrapper) {
            this.mTaskSaveSessionData = new TaskSaveSessionData(sessionWrapper, (AnonymousClass1) null);
        }

        /* access modifiers changed from: package-private */
        public Builder setSessionInfoStr(String str) {
            this.mTaskSaveSessionData.setSessionInfoStr(str);
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setLastGamePauseTime(long j) {
            this.mTaskSaveSessionData.setLastGamePauseTime(j);
            return this;
        }

        /* access modifiers changed from: package-private */
        public TaskSaveSessionData build() {
            return this.mTaskSaveSessionData;
        }
    }
}
