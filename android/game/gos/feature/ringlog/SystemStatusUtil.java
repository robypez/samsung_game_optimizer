package com.samsung.android.game.gos.feature.ringlog;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.samsung.android.game.gos.feature.dfs.FpsController;
import com.samsung.android.game.gos.ipm.Aggregation;
import com.samsung.android.game.gos.ipm.ParameterRequest;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.NetworkConstants;
import com.samsung.android.game.gos.value.RinglogConstants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemStatusUtil {
    private static final int GAME_HEAVY_UPDATE_INTERVAL_MS = 60000;
    public static int LOW_MEMORY_TRIM_LEVEL = 15;
    private static final String TAG = "SystemStatusUtil";
    private static int sLastGameHeavyGrade = 0;
    private static long sLastGameHeavyUpdateTime = -1;

    static int getGradeFromBatteryPercent(double d) {
        if (d == -1.0d) {
            return -1;
        }
        if (d > 70.0d) {
            return 4;
        }
        if (d > 50.0d) {
            return 3;
        }
        if (d > 30.0d) {
            return 2;
        }
        return d > 15.0d ? 1 : 0;
    }

    static int getGradeFromMLLearnStatusJSON(boolean z, int i, long j, int i2, double d) {
        if (!z || i < 0 || j < 0) {
            return -1;
        }
        if (j < ((long) i2)) {
            return -2;
        }
        if (d <= 10.0d) {
            return 4;
        }
        if (d <= 30.0d) {
            return 3;
        }
        if (d <= 60.0d) {
            return 2;
        }
        return d <= 90.0d ? 1 : 0;
    }

    static int getGradeFromSignalStrength(int i) {
        if (i > 80) {
            return 4;
        }
        if (i > 70) {
            return 3;
        }
        if (i > 50) {
            return 2;
        }
        if (i > 30) {
            return 1;
        }
        return i >= 0 ? 0 : -1;
    }

    static int getGradeFromTemperatureJSON(boolean z, double d, double d2, long j, double d3) {
        if (!z || d <= 0.0d || d2 <= 0.0d || j < 0) {
            return -1;
        }
        if (j < RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS) {
            return -2;
        }
        if (d3 <= -6.0d) {
            return 4;
        }
        if (d3 <= -4.0d) {
            return 3;
        }
        if (d3 <= -2.0d) {
            return 2;
        }
        return d3 <= 0.0d ? 1 : 0;
    }

    private static double getBatteryLevel(Context context) {
        Intent registerReceiver = context.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        int intExtra = registerReceiver.getIntExtra("level", -1);
        int intExtra2 = registerReceiver.getIntExtra(FpsController.TYPE_SCALE, -1);
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getBatteryLevel " + registerReceiver.getExtras() + ", level=" + intExtra + ", scale=" + intExtra2);
        }
        if (intExtra == -1 || intExtra2 == -1) {
            return -1.0d;
        }
        return (((double) intExtra) / ((double) intExtra2)) * 100.0d;
    }

    static JSONObject getBatteryLevelJSON(Context context, boolean z) throws JSONException {
        double batteryLevel = getBatteryLevel(context);
        int gradeFromBatteryPercent = getGradeFromBatteryPercent(batteryLevel);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(GosInterface.KeyName.GRADE, gradeFromBatteryPercent);
        if (z) {
            jSONObject.put(RinglogConstants.SYSTEM_STATUS_RAW_BATTERY_LEVEL, batteryLevel);
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getBatteryLevelJSON " + batteryLevel + ", grade=" + gradeFromBatteryPercent);
        }
        return jSONObject;
    }

    private static int getWifiSignalLevel(Context context) {
        WifiInfo connectionInfo;
        int i = -1;
        if (context == null) {
            return -1;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (!(wifiManager == null || (connectionInfo = wifiManager.getConnectionInfo()) == null)) {
            int rssi = connectionInfo.getRssi();
            i = WifiManager.calculateSignalLevel(rssi, 100);
            if (RinglogConstants.DEBUG) {
                GosLog.i(TAG, "getWifiSignalLevel rssi=" + rssi + ", level=" + i);
            }
        }
        return i;
    }

    private static int getNetworkLevel(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null || !connectivityManager.getNetworkCapabilities(activeNetwork).hasTransport(1)) {
            return -1;
        }
        return getWifiSignalLevel(context);
    }

    static JSONObject getNetworkLevelJSON(Context context, boolean z) throws JSONException {
        int networkLevel = getNetworkLevel(context);
        int gradeFromSignalStrength = getGradeFromSignalStrength(networkLevel);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(GosInterface.KeyName.GRADE, gradeFromSignalStrength);
        if (z) {
            jSONObject.put(RinglogConstants.SYSTEM_STATUS_RAW_NETWORK_LEVEL, networkLevel);
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getNetworkLevelJSON " + networkLevel + ", grade=" + gradeFromSignalStrength);
        }
        return jSONObject;
    }

    public static JSONObject getMemoryJSON(Context context, boolean z) throws JSONException {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        long j = memoryInfo.availMem;
        double d = memoryInfo.totalMem > 0 ? (((double) j) / ((double) memoryInfo.totalMem)) * 100.0d : -1.0d;
        ActivityManager.RunningAppProcessInfo myProcessInfo = getMyProcessInfo();
        int gradeFromFreeMemPercent = getGradeFromFreeMemPercent(d, j, myProcessInfo);
        GosLog.i(TAG, "getMemoryJSON freeMem=" + j + ", tot=" + memoryInfo.totalMem + ", freeMemPercent=" + d + ", grade=" + gradeFromFreeMemPercent + ", lastTrimLevel=" + myProcessInfo.lastTrimLevel + ", lowMemory=" + memoryInfo.lowMemory);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(GosInterface.KeyName.GRADE, gradeFromFreeMemPercent);
        if (z) {
            jSONObject.put(RinglogConstants.SYSTEM_STATUS_RAW_FREE_MEMORY, j);
            jSONObject.put(RinglogConstants.SYSTEM_STATUS_RAW_TOTAL_MEMORY, memoryInfo.totalMem);
            jSONObject.put(RinglogConstants.SYSTEM_STATUS_RAW_FREE_MEMORY_LEVEL, d);
        }
        return jSONObject;
    }

    static int getGradeFromFreeMemPercent(double d, long j, ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        int i = (d == -1.0d || j < 0) ? -1 : d > 25.0d ? 4 : d > 20.0d ? 3 : d > 15.0d ? 2 : (d <= 10.0d && j <= 524288000) ? 0 : 1;
        if (d <= 10.0d) {
            if (runningAppProcessInfo.lastTrimLevel >= LOW_MEMORY_TRIM_LEVEL) {
                return 0;
            }
            if (runningAppProcessInfo.lastTrimLevel >= 10) {
                return 1;
            }
        }
        return i;
    }

    public static ActivityManager.RunningAppProcessInfo getMyProcessInfo() {
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(runningAppProcessInfo);
        return runningAppProcessInfo;
    }

    private static String readGppForSystemStatus(String str, RinglogConstants.SessionWrapper sessionWrapper, int i, int i2) {
        long j = (long) (-RinglogUtil.LCM(i, i2));
        if (sessionWrapper == null || sessionWrapper.info == null) {
            return "{}";
        }
        boolean isOngoingSession = RinglogUtil.isOngoingSession(sessionWrapper);
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getSessionFpsMaxFpsPstMle id=" + sessionWrapper.id + ", start=" + sessionWrapper.info.data_start_ms + ", end=" + sessionWrapper.info.data_end_ms + "isOngoing=" + isOngoingSession + " s=" + j + ",e=" + 0 + ", systemTime=" + System.currentTimeMillis());
        }
        if (!isOngoingSession) {
            return "{}";
        }
        ArrayList arrayList = new ArrayList();
        long j2 = (long) i;
        arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.TEMP_LRPST.getName(), Aggregation.MEAN, j2));
        arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.TEMP_PST.getName(), Aggregation.MEAN, j2));
        arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.FPS.getName(), Aggregation.MEAN, j2));
        arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.MAX_GUESS_FPS.getName(), Aggregation.MEAN, j2));
        arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.ML_EXPLORATION.getName(), Aggregation.SUM, (long) i2));
        arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.BATTERY_PREDICTION_BENEFIT_PERCENT_LOW.getName(), Aggregation.MEAN, j2));
        arrayList.add(new ParameterRequest(RinglogConstants.PerfParams.FPS_BENEFIT_PERCENT_LOW.getName(), Aggregation.MEAN, j2));
        return RinglogInterface.getInstance().readDataSimpleRequestJSON(str, arrayList, sessionWrapper.id, j, 0);
    }

    static JSONObject getTemperatureJSON(JSONObject jSONObject, RinglogConstants.SessionWrapper sessionWrapper, boolean z) throws JSONException {
        int i;
        boolean z2;
        double d;
        JSONObject jSONObject2 = jSONObject;
        RinglogConstants.SessionWrapper sessionWrapper2 = sessionWrapper;
        JSONObject jSONObject3 = new JSONObject();
        boolean isUsingLrpst = sessionWrapper2.info.isUsingLrpst();
        boolean isOngoingSession = RinglogUtil.isOngoingSession(sessionWrapper);
        double d2 = -1.0d;
        if (isUsingLrpst) {
            JSONArray optJSONArray = jSONObject2.optJSONArray(RinglogConstants.PerfParams.TEMP_LRPST.getName());
            if (optJSONArray != null && optJSONArray.length() > 0) {
                d2 = optJSONArray.getDouble(optJSONArray.length() - 1);
            }
            i = sessionWrapper2.info.targetLRPST;
        } else {
            JSONArray optJSONArray2 = jSONObject2.optJSONArray(RinglogConstants.PerfParams.TEMP_PST.getName());
            if (optJSONArray2 != null && optJSONArray2.length() > 0) {
                d2 = optJSONArray2.getDouble(optJSONArray2.length() - 1);
            }
            i = sessionWrapper2.info.targetPst;
        }
        double d3 = d2;
        double d4 = (double) i;
        long j = sessionWrapper2.info.data_end_ms - sessionWrapper2.info.data_start_ms;
        double d5 = d3 - (d4 / 10.0d);
        JSONObject jSONObject4 = jSONObject3;
        boolean z3 = isUsingLrpst;
        double d6 = d4;
        int gradeFromTemperatureJSON = getGradeFromTemperatureJSON(isOngoingSession, d4, d3, j, d5);
        double d7 = d3 * 10.0d;
        if (RinglogConstants.DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("getTemperatureJSON currentTempX10=");
            sb.append(d7);
            sb.append(", targetTemp=");
            sb.append(d6);
            sb.append(", sessionLen=");
            sb.append(j);
            sb.append(", tmpDiff=");
            d = d5;
            sb.append(d);
            sb.append(", isLrpstMode=");
            z2 = z3;
            sb.append(z2);
            GosLog.i(TAG, sb.toString());
        } else {
            d = d5;
            z2 = z3;
        }
        JSONObject jSONObject5 = jSONObject4;
        jSONObject5.put(GosInterface.KeyName.GRADE, gradeFromTemperatureJSON);
        if (z) {
            jSONObject5.put(RinglogConstants.SYSTEM_STATUS_RAW_TEMP, d7);
            jSONObject5.put(RinglogConstants.SYSTEM_STATUS_RAW_TARGET_TEMP, d6);
            jSONObject5.put(RinglogConstants.SYSTEM_STATUS_RAW_TEMP_DIFF, d);
            jSONObject5.put(RinglogConstants.SYSTEM_STATUS_RAW_SESSION_LEN, j);
            jSONObject5.put(RinglogConstants.SYSTEM_STATUS_RAW_IS_LRPST_MODE, z2);
        }
        return jSONObject5;
    }

    static int calcGrade(boolean z, double d, double d2, long j, long j2) {
        if (!z || Math.abs(d - -1.0d) < 1.0E-7d || d2 < 0.0d || d2 > 60.0d || j < 0) {
            return -1;
        }
        if (j < j2) {
            return -2;
        }
        if (d2 < 15.0d) {
            return 0;
        }
        if (d > 80.0d) {
            return 4;
        }
        if (d > 70.0d) {
            return 3;
        }
        if (d > 50.0d) {
            return 2;
        }
        return d > 30.0d ? 1 : 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0025, code lost:
        r1 = r0.optJSONArray(com.samsung.android.game.gos.value.RinglogConstants.PerfParams.FPS.getName());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.json.JSONObject getGamePerformanceJSON(org.json.JSONObject r20, com.samsung.android.game.gos.value.RinglogConstants.SessionWrapper r21, boolean r22) throws org.json.JSONException {
        /*
            r0 = r20
            r1 = r21
            org.json.JSONObject r2 = new org.json.JSONObject
            r2.<init>()
            boolean r3 = com.samsung.android.game.gos.feature.ringlog.RinglogUtil.isOngoingSession(r21)
            com.samsung.android.game.gos.value.RinglogConstants$SessionInfo r4 = r1.info
            long r4 = r4.data_end_ms
            com.samsung.android.game.gos.value.RinglogConstants$SessionInfo r1 = r1.info
            long r6 = r1.data_start_ms
            long r12 = r4 - r6
            com.samsung.android.game.gos.value.RinglogConstants$PerfParams r1 = com.samsung.android.game.gos.value.RinglogConstants.PerfParams.FPS
            java.lang.String r1 = r1.getName()
            boolean r1 = r0.has(r1)
            r4 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            if (r1 == 0) goto L_0x0043
            com.samsung.android.game.gos.value.RinglogConstants$PerfParams r1 = com.samsung.android.game.gos.value.RinglogConstants.PerfParams.FPS
            java.lang.String r1 = r1.getName()
            org.json.JSONArray r1 = r0.optJSONArray(r1)
            if (r1 == 0) goto L_0x0043
            int r6 = r1.length()
            if (r6 <= 0) goto L_0x0043
            int r6 = r1.length()
            int r6 = r6 + -1
            double r6 = r1.getDouble(r6)
            r14 = r6
            goto L_0x0044
        L_0x0043:
            r14 = r4
        L_0x0044:
            com.samsung.android.game.gos.value.RinglogConstants$PerfParams r1 = com.samsung.android.game.gos.value.RinglogConstants.PerfParams.MAX_GUESS_FPS
            java.lang.String r1 = r1.getName()
            boolean r1 = r0.has(r1)
            if (r1 == 0) goto L_0x006d
            com.samsung.android.game.gos.value.RinglogConstants$PerfParams r1 = com.samsung.android.game.gos.value.RinglogConstants.PerfParams.MAX_GUESS_FPS
            java.lang.String r1 = r1.getName()
            org.json.JSONArray r0 = r0.optJSONArray(r1)
            if (r0 == 0) goto L_0x006d
            int r1 = r0.length()
            if (r1 <= 0) goto L_0x006d
            int r1 = r0.length()
            int r1 = r1 + -1
            int r0 = r0.getInt(r1)
            goto L_0x006e
        L_0x006d:
            r0 = -1
        L_0x006e:
            if (r0 <= 0) goto L_0x0075
            r4 = 4636737291354636288(0x4059000000000000, double:100.0)
            double r4 = r4 * r14
            double r6 = (double) r0
            double r4 = r4 / r6
        L_0x0075:
            r10 = r4
            boolean r1 = com.samsung.android.game.gos.value.RinglogConstants.DEBUG
            if (r1 == 0) goto L_0x00a8
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = "getGamePerformanceJSON currentFps="
            r1.append(r4)
            r1.append(r14)
            java.lang.String r4 = ", maxGuessFps="
            r1.append(r4)
            r1.append(r0)
            java.lang.String r4 = ", sessionLenMs="
            r1.append(r4)
            r1.append(r12)
            java.lang.String r4 = ", fpsPercent="
            r1.append(r4)
            r1.append(r10)
            java.lang.String r1 = r1.toString()
            java.lang.String r4 = "SystemStatusUtil"
            com.samsung.android.game.gos.util.GosLog.i(r4, r1)
        L_0x00a8:
            r16 = 10000(0x2710, double:4.9407E-320)
            r4 = r10
            r6 = r14
            r8 = r12
            r18 = r12
            r12 = r10
            r10 = r16
            int r1 = calcGrade(r3, r4, r6, r8, r10)
            java.lang.String r3 = "grade"
            r2.put(r3, r1)
            if (r22 == 0) goto L_0x00d3
            java.lang.String r1 = "fps"
            r2.put(r1, r14)
            java.lang.String r1 = "fps_max_guess"
            r2.put(r1, r0)
            java.lang.String r0 = "fps_level"
            r2.put(r0, r12)
            java.lang.String r0 = "session_length"
            r4 = r18
            r2.put(r0, r4)
        L_0x00d3:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ringlog.SystemStatusUtil.getGamePerformanceJSON(org.json.JSONObject, com.samsung.android.game.gos.value.RinglogConstants$SessionWrapper, boolean):org.json.JSONObject");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0025, code lost:
        r0 = r15.optJSONArray(com.samsung.android.game.gos.value.RinglogConstants.PerfParams.ML_EXPLORATION.getName());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static org.json.JSONObject getMLLearnStatusJSON(org.json.JSONObject r15, int r16, com.samsung.android.game.gos.value.RinglogConstants.SessionWrapper r17, boolean r18) throws org.json.JSONException {
        /*
            r0 = r15
            r7 = r16
            r1 = r17
            org.json.JSONObject r8 = new org.json.JSONObject
            r8.<init>()
            com.samsung.android.game.gos.value.RinglogConstants$SessionInfo r2 = r1.info
            long r2 = r2.data_end_ms
            com.samsung.android.game.gos.value.RinglogConstants$SessionInfo r4 = r1.info
            long r4 = r4.data_start_ms
            long r9 = r2 - r4
            boolean r1 = com.samsung.android.game.gos.feature.ringlog.RinglogUtil.isOngoingSession(r17)
            com.samsung.android.game.gos.value.RinglogConstants$PerfParams r2 = com.samsung.android.game.gos.value.RinglogConstants.PerfParams.ML_EXPLORATION
            java.lang.String r2 = r2.getName()
            boolean r2 = r15.has(r2)
            r11 = -1
            if (r2 == 0) goto L_0x0043
            com.samsung.android.game.gos.value.RinglogConstants$PerfParams r2 = com.samsung.android.game.gos.value.RinglogConstants.PerfParams.ML_EXPLORATION
            java.lang.String r2 = r2.getName()
            org.json.JSONArray r0 = r15.optJSONArray(r2)
            if (r0 == 0) goto L_0x0043
            int r2 = r0.length()
            if (r2 <= 0) goto L_0x0043
            int r2 = r0.length()
            int r2 = r2 + -1
            int r0 = r0.getInt(r2)
            r12 = r0
            goto L_0x0044
        L_0x0043:
            r12 = r11
        L_0x0044:
            double r2 = (double) r12
            r4 = 4652007308841189376(0x408f400000000000, double:1000.0)
            double r2 = r2 * r4
            double r4 = (double) r7
            double r2 = r2 / r4
            r4 = 4636737291354636288(0x4059000000000000, double:100.0)
            double r13 = r2 * r4
            r0 = r1
            r1 = r12
            r2 = r9
            r4 = r16
            r5 = r13
            int r0 = getGradeFromMLLearnStatusJSON(r0, r1, r2, r4, r5)
            if (r0 == r11) goto L_0x0060
            r1 = -2
            if (r0 != r1) goto L_0x0062
        L_0x0060:
            r13 = -4616189618054758400(0xbff0000000000000, double:-1.0)
        L_0x0062:
            boolean r1 = com.samsung.android.game.gos.value.RinglogConstants.DEBUG
            if (r1 == 0) goto L_0x00a5
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getMLLearnStatusJSON mlExploreSum="
            r1.append(r2)
            r1.append(r12)
            java.lang.String r2 = ", exploreDurationMs="
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = ", sessionLen="
            r1.append(r2)
            r1.append(r9)
            java.lang.String r2 = ", explorePercent="
            r1.append(r2)
            float r2 = (float) r12
            r3 = 1148846080(0x447a0000, float:1000.0)
            float r2 = r2 * r3
            float r3 = (float) r7
            float r2 = r2 / r3
            r3 = 1120403456(0x42c80000, float:100.0)
            float r2 = r2 * r3
            r1.append(r2)
            java.lang.String r2 = ", grade="
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "SystemStatusUtil"
            com.samsung.android.game.gos.util.GosLog.i(r2, r1)
        L_0x00a5:
            java.lang.String r1 = "grade"
            r8.put(r1, r0)
            if (r18 == 0) goto L_0x00c0
            java.lang.String r0 = "exploration_sum"
            r8.put(r0, r12)
            java.lang.String r0 = "exploration_duration"
            r8.put(r0, r7)
            java.lang.String r0 = "exploration_level"
            r8.put(r0, r13)
            java.lang.String r0 = "session_length"
            r8.put(r0, r9)
        L_0x00c0:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ringlog.SystemStatusUtil.getMLLearnStatusJSON(org.json.JSONObject, int, com.samsung.android.game.gos.value.RinglogConstants$SessionWrapper, boolean):org.json.JSONObject");
    }

    private static JSONObject getGameHeavyStatus(String str, JSONObject jSONObject, RinglogConstants.SessionWrapper sessionWrapper, boolean z, int i) throws JSONException {
        int i2;
        JSONObject jSONObject2 = jSONObject;
        RinglogConstants.SessionWrapper sessionWrapper2 = sessionWrapper;
        int i3 = i;
        JSONObject jSONObject3 = new JSONObject();
        long j = sessionWrapper2.info.data_end_ms - sessionWrapper2.info.data_start_ms;
        boolean isOngoingSession = RinglogUtil.isOngoingSession(sessionWrapper);
        RinglogConstants.GameStatus gameStatus = RinglogConstants.GameStatus.Unknown;
        if (!isOngoingSession) {
            i2 = -1;
        } else if (j < ((long) i3)) {
            i2 = -2;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            long j2 = sLastGameHeavyUpdateTime;
            if (j2 == -1 || currentTimeMillis - j2 > 60000) {
                i2 = getGameStatus(str, jSONObject2, sessionWrapper2, i3).getGrade();
                sLastGameHeavyGrade = i2;
                sLastGameHeavyUpdateTime = currentTimeMillis;
            } else {
                i2 = sLastGameHeavyGrade;
            }
        }
        jSONObject3.put(GosInterface.KeyName.GRADE, i2);
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getGameHeavyStatus grade=" + i2 + ", sLastGameHeavyUpdateTime=" + sLastGameHeavyUpdateTime);
        }
        if (z) {
            double optInt = ((double) jSONObject2.optInt(RinglogConstants.PerfParams.BATTERY_PREDICTION_BENEFIT_PERCENT_LOW.getName(), 0)) / 100.0d;
            Random random = new Random(System.currentTimeMillis());
            double beautifyNumber = beautifyNumber(random, optInt, 0.03d, 0.2d);
            jSONObject3.put(RinglogConstants.SYSTEM_STATUS_RAW_FPS_BENEFIT, beautifyNumber(random, jSONObject2.optDouble(RinglogConstants.PerfParams.FPS_BENEFIT_PERCENT_LOW.getName(), 0.0d) / 100.0d, 0.03d, 0.2d));
            jSONObject3.put(RinglogConstants.SYSTEM_STATUS_RAW_PLAYTIME_BENEFIT, beautifyNumber);
        }
        return jSONObject3;
    }

    private static double beautifyNumber(Random random, double d, double d2, double d3) {
        if (d < d2) {
            return d2 + (((double) random.nextInt(3)) / 100.0d);
        }
        return d > d3 ? d3 - (((double) random.nextInt(4)) / 100.0d) : d;
    }

    private static String readTempForGameHeavyStatus(String str, RinglogConstants.SessionWrapper sessionWrapper, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ParameterRequest(sessionWrapper.info.isUsingLrpst() ? Constants.RingLog.Parameter.TEMP_LRPST : Constants.RingLog.Parameter.TEMP_PST, Aggregation.MAX, 1000));
        return RinglogInterface.getInstance().readDataSimpleRequestJSON(str, arrayList, sessionWrapper.id, (long) (-i), 0);
    }

    private static RinglogConstants.GameStatus getGameStatus(String str, JSONObject jSONObject, RinglogConstants.SessionWrapper sessionWrapper, int i) throws JSONException {
        double d;
        double d2;
        RinglogConstants.GameStatus gameStatus;
        JSONArray optJSONArray = jSONObject.optJSONArray(RinglogConstants.PerfParams.ML_EXPLORATION.getName());
        int i2 = (optJSONArray == null || optJSONArray.length() <= 0) ? -1 : optJSONArray.getInt(optJSONArray.length() - 1);
        JSONObject jSONObject2 = new JSONObject(readTempForGameHeavyStatus(str, sessionWrapper, i));
        boolean isUsingLrpst = sessionWrapper.info.isUsingLrpst();
        RinglogConstants.SessionInfo sessionInfo = sessionWrapper.info;
        if (isUsingLrpst) {
            d = ((double) sessionInfo.targetLRPST) / 10.0d;
            d2 = 3.0d;
        } else {
            d = ((double) sessionInfo.targetPst) / 10.0d;
            d2 = 4.0d;
        }
        double d3 = d - d2;
        JSONArray optJSONArray2 = jSONObject2.optJSONArray((isUsingLrpst ? RinglogConstants.PerfParams.TEMP_LRPST : RinglogConstants.PerfParams.TEMP_PST).getName());
        if (optJSONArray2 == null || optJSONArray2.length() == 0 || i2 == -1 || i2 >= 6) {
            gameStatus = RinglogConstants.GameStatus.Unknown;
        } else {
            gameStatus = RinglogConstants.GameStatus.Light;
            int i3 = 0;
            while (true) {
                if (i3 >= optJSONArray2.length()) {
                    break;
                } else if (optJSONArray2.getDouble(i3) > d3) {
                    gameStatus = RinglogConstants.GameStatus.Heavy;
                    break;
                } else {
                    i3++;
                }
            }
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getGameStatus=" + gameStatus.name() + ", jsonArray=" + optJSONArray2 + ", target=" + d3 + ", explorationCount=" + i2);
        }
        return gameStatus;
    }

    public static String getSystemStatusJSON(Context context, String str) {
        boolean z = false;
        if (str != null) {
            try {
                z = new JSONObject(str).optBoolean(GosInterface.KeyName.INCLUDE_RAW_DATA);
            } catch (JSONException e) {
                GosLog.e(TAG, "getSystemStatusJSON " + e);
            }
        }
        return getSystemStatusJSON(context, z);
    }

    public static String getSystemStatusJSON(Context context, boolean z) {
        JSONObject jSONObject = new JSONObject();
        try {
            String callerPkgName = RinglogPermissionHandler.getCallerPkgName();
            RinglogPermissionHandler instance = RinglogPermissionHandler.getInstance();
            boolean z2 = false;
            boolean z3 = true;
            if (instance.isPermissionGrantedSystemStatus(callerPkgName)) {
                jSONObject.put(RinglogConstants.SystemStatusParams.BATTERY.getPrefixedName(), getBatteryLevelJSON(context, z));
                jSONObject.put(RinglogConstants.SystemStatusParams.NETWORK.getPrefixedName(), getNetworkLevelJSON(context, z));
                jSONObject.put(RinglogConstants.SystemStatusParams.MEMORY.getPrefixedName(), getMemoryJSON(context, z));
                boolean isPermissionGranted = instance.isPermissionGranted(callerPkgName, RinglogConstants.PerfParams.FPS);
                boolean isPermissionGranted2 = instance.isPermissionGranted(callerPkgName, RinglogConstants.PerfParams.TEMP_PST);
                boolean isPermissionGranted3 = instance.isPermissionGranted(callerPkgName, RinglogConstants.PerfParams.ML_EXPLORATION);
                RinglogConstants.SessionWrapper latestSessionInfo = RinglogUtil.getLatestSessionInfo(callerPkgName);
                if (latestSessionInfo == null || latestSessionInfo.info == null || (!isPermissionGranted && !isPermissionGranted2 && !isPermissionGranted3)) {
                    z2 = true;
                } else {
                    if (RinglogConstants.DEBUG) {
                        GosLog.i(TAG, "getSystemStatusJSON , rate=" + NetworkConstants.CONNECTION_TIMEOUT + ", mlExploreRateMs=" + 20000 + ", session=" + latestSessionInfo);
                    }
                    JSONObject jSONObject2 = new JSONObject(readGppForSystemStatus(callerPkgName, latestSessionInfo, NetworkConstants.CONNECTION_TIMEOUT, 20000));
                    if (isPermissionGranted) {
                        jSONObject.put(RinglogConstants.SystemStatusParams.GAME_PERF.getPrefixedName(), getGamePerformanceJSON(jSONObject2, latestSessionInfo, z));
                    }
                    if (isPermissionGranted2) {
                        jSONObject.put(RinglogConstants.SystemStatusParams.TEMPERATURE.getPrefixedName(), getTemperatureJSON(jSONObject2, latestSessionInfo, z));
                    }
                    if (isPermissionGranted3) {
                        jSONObject.put(RinglogConstants.SystemStatusParams.SPA_LEARNING.getPrefixedName(), getMLLearnStatusJSON(jSONObject2, 20000, latestSessionInfo, z));
                    }
                    if (isPermissionGranted3 && isPermissionGranted2) {
                        jSONObject.put(RinglogConstants.SystemStatusParams.GAME_HEAVY.getPrefixedName(), getGameHeavyStatus(callerPkgName, jSONObject2, latestSessionInfo, z, 20000));
                    }
                }
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, true);
                z3 = z2;
            } else {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            }
            if (z3) {
                jSONObject.put(GosInterface.KeyName.COMMENT, "Some requests have failed to process or permission denied.");
            }
        } catch (JSONException e) {
            GosLog.e(TAG, "getSystemInfoJson " + e);
            e.printStackTrace();
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getSystemStatusJSON " + jSONObject.toString());
        }
        return jSONObject.toString();
    }
}
