package com.samsung.android.game.gos.controller;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Pair;
import com.google.gson.Gson;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.SystemDataHelper;
import com.samsung.android.game.gos.data.type.ReportData;
import com.samsung.android.game.gos.feature.dfs.FpsController;
import com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeAudioManager;
import com.samsung.android.game.gos.util.GosLog;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusCollector {
    private static final String LOG_TAG = "StatusCollector";
    private static StatusCollector mInstance;
    private RunningGame mRunningGame = null;

    private StatusCollector() {
    }

    public static StatusCollector getInstance() {
        if (mInstance == null) {
            mInstance = new StatusCollector();
            GosLog.d(LOG_TAG, "Create a StatusCollector");
        }
        return mInstance;
    }

    private static void putBatteryInfo(JSONObject jSONObject, int i, int i2, int i3, int i4) throws JSONException {
        if (i >= 0 && i <= 4) {
            jSONObject.put(ReportData.UserUsageKey.BATTERY_PLUGGED_RESUME, i);
        }
        if (i2 >= 0 && i2 <= 4) {
            jSONObject.put(ReportData.UserUsageKey.BATTERY_PLUGGED, i2);
        }
        if (i3 >= 0 && i3 <= 100) {
            jSONObject.put(ReportData.UserUsageKey.BATTERY_PERCENTAGE_RESUME, i3);
        }
        if (i4 >= 0 && i4 <= 100) {
            jSONObject.put(ReportData.UserUsageKey.BATTERY_PERCENTAGE, i4);
        }
    }

    static void putBrightnessInfo(JSONObject jSONObject, int i, int i2, int i3, int i4) throws JSONException {
        if (i >= 0 && i <= 1) {
            jSONObject.put(ReportData.UserUsageKey.SCREEN_BRIGHTNESS_MODE_RESUME, i);
        }
        if (i2 >= 0 && i2 <= 1) {
            jSONObject.put(ReportData.UserUsageKey.SCREEN_BRIGHTNESS_MODE, i2);
        }
        if (i3 >= 0 && i3 <= 255) {
            jSONObject.put(ReportData.UserUsageKey.SCREEN_BRIGHTNESS_RESUME, i3);
        }
        if (i4 >= 0 && i4 <= 255) {
            jSONObject.put(ReportData.UserUsageKey.SCREEN_BRIGHTNESS, i4);
        }
    }

    private static void putSystemInfo(JSONObject jSONObject, int i, int i2, int i3) throws JSONException {
        if (i >= 0 && i <= 1) {
            jSONObject.put(ReportData.UserUsageKey.AUDIO_PLUGGED, i);
        }
        if (i2 >= 0 && i2 <= 100) {
            jSONObject.put(ReportData.UserUsageKey.AUDIO_VOLUME, i2);
        }
        if (i3 >= 0 && i3 <= 1) {
            jSONObject.put(ReportData.UserUsageKey.WIFI_CONNECTED, i3);
        }
    }

    static void putBoostInfo(JSONObject jSONObject, int i, int i2) throws JSONException {
        jSONObject.put(ReportData.UserUsageKey.BOOST_RESUME_DURATION, i);
        jSONObject.put(ReportData.UserUsageKey.BOOST_LAUNCH_DURATION, i2);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Don't wrap MOVE or CONST insns: 0x00c1: MOVE  (r0v18 org.json.JSONArray) = (r45v0 org.json.JSONArray)
        	at jadx.core.dex.instructions.args.InsnArg.wrapArg(InsnArg.java:164)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.assignInline(CodeShrinkVisitor.java:133)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:118)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:65)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:43)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:35)
        */
    static java.lang.String getUserUsageJsonMsg(java.lang.String r16, java.lang.String r17, int r18, int r19, int r20, int r21, int r22, int r23, int r24, int r25, int r26, int r27, int r28, long r29, long r31, long r33, java.lang.String r35, long r36, int r38, java.lang.String r39, java.lang.String r40, int r41, int r42, int r43, java.lang.String r44, org.json.JSONArray r45, org.json.JSONObject r46) {
        /*
            r0 = r16
            r1 = r17
            r2 = r29
            r4 = r31
            r6 = r33
            r8 = r35
            r9 = r36
            r11 = r41
            r12 = r44
            r13 = r45
            r14 = r46
            org.json.JSONObject r15 = new org.json.JSONObject
            r15.<init>()
            java.lang.String r14 = "gos_version_name_full"
            java.lang.String r13 = "3.5.01.18"
            r15.put(r14, r13)     // Catch:{ JSONException -> 0x00e3 }
            java.lang.String r13 = "gos_version_code_full"
            r14 = 350100018(0x14de1a32, float:2.2426604E-26)
            r15.put(r13, r14)     // Catch:{ JSONException -> 0x00e3 }
            if (r1 == 0) goto L_0x0031
            java.lang.String r13 = "test_group_name"
            r15.put(r13, r1)     // Catch:{ JSONException -> 0x00e3 }
        L_0x0031:
            if (r0 == 0) goto L_0x00e1
            int r1 = r16.length()     // Catch:{ JSONException -> 0x00e3 }
            if (r1 <= 0) goto L_0x00e1
            java.lang.String r1 = "package_name"
            r15.put(r1, r0)     // Catch:{ JSONException -> 0x00e3 }
            if (r8 == 0) goto L_0x004d
            java.lang.String r0 = ""
            boolean r0 = r8.equals(r0)     // Catch:{ JSONException -> 0x00e3 }
            if (r0 != 0) goto L_0x004d
            java.lang.String r0 = "version_name"
            r15.put(r0, r8)     // Catch:{ JSONException -> 0x00e3 }
        L_0x004d:
            r0 = -1
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r0 == 0) goto L_0x0058
            java.lang.String r0 = "version_code"
            r15.put(r0, r9)     // Catch:{ JSONException -> 0x00e3 }
        L_0x0058:
            r0 = 0
            int r8 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r8 < 0) goto L_0x0063
            java.lang.String r8 = "resume_time"
            r15.put(r8, r4)     // Catch:{ JSONException -> 0x00e3 }
        L_0x0063:
            int r4 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r4 < 0) goto L_0x006c
            java.lang.String r4 = "reporting_time"
            r15.put(r4, r6)     // Catch:{ JSONException -> 0x00e3 }
        L_0x006c:
            int r0 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x007c
            r0 = 86400(0x15180, double:4.26873E-319)
            int r0 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r0 > 0) goto L_0x007c
            java.lang.String r0 = "duration"
            r15.put(r0, r2)     // Catch:{ JSONException -> 0x00e3 }
        L_0x007c:
            r0 = r18
            r1 = r19
            r2 = r20
            r3 = r21
            putBatteryInfo(r15, r1, r3, r0, r2)     // Catch:{ JSONException -> 0x00e3 }
            r0 = r22
            r1 = r23
            r2 = r24
            r3 = r25
            putBrightnessInfo(r15, r1, r3, r0, r2)     // Catch:{ JSONException -> 0x00e3 }
            r0 = r26
            r1 = r27
            r2 = r28
            putSystemInfo(r15, r2, r1, r0)     // Catch:{ JSONException -> 0x00e3 }
            java.lang.String r0 = "siop_mode"
            r1 = r38
            r15.put(r0, r1)     // Catch:{ JSONException -> 0x00e3 }
            java.lang.String r0 = "applied_resolution_type"
            r1 = r39
            r15.put(r0, r1)     // Catch:{ JSONException -> 0x00e3 }
            r0 = -1
            if (r11 == r0) goto L_0x00b1
            java.lang.String r0 = "applied_short_side"
            r15.put(r0, r11)     // Catch:{ JSONException -> 0x00e3 }
        L_0x00b1:
            java.lang.String r0 = "applied_dss"
            r1 = r40
            r15.put(r0, r1)     // Catch:{ JSONException -> 0x00e3 }
            r0 = r42
            r1 = r43
            putBoostInfo(r15, r0, r1)     // Catch:{ JSONException -> 0x00e3 }
            if (r12 == 0) goto L_0x00cf
            r0 = r45
            if (r0 == 0) goto L_0x00cf
            java.lang.String r1 = "external_sdk_type"
            r15.put(r1, r12)     // Catch:{ JSONException -> 0x00e3 }
            java.lang.String r1 = "external_sdk_events"
            r15.put(r1, r0)     // Catch:{ JSONException -> 0x00e3 }
        L_0x00cf:
            r0 = r46
            if (r0 == 0) goto L_0x00e9
            java.lang.String r1 = "spa_on"
            boolean r1 = r0.has(r1)     // Catch:{ JSONException -> 0x00e3 }
            if (r1 == 0) goto L_0x00e9
            java.lang.String r1 = "spa_status"
            r15.put(r1, r0)     // Catch:{ JSONException -> 0x00e3 }
            goto L_0x00e9
        L_0x00e1:
            r0 = 0
            return r0
        L_0x00e3:
            r0 = move-exception
            java.lang.String r1 = "StatusCollector"
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r1, (java.lang.Throwable) r0)
        L_0x00e9:
            java.lang.String r0 = r15.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.controller.StatusCollector.getUserUsageJsonMsg(java.lang.String, java.lang.String, int, int, int, int, int, int, int, int, int, int, int, long, long, long, java.lang.String, long, int, java.lang.String, java.lang.String, int, int, int, java.lang.String, org.json.JSONArray, org.json.JSONObject):java.lang.String");
    }

    private void debugLog(String str) {
        GosLog.v(LOG_TAG, str);
    }

    static class SystemInfo {
        int audioOutputDevice = 0;
        int audioVolume = 0;
        int batteryPercent = 0;
        int batteryPluggedType = 0;
        int brightness = -1;
        int brightnessMode = -1;
        long versionCode = -1;
        String versionName = null;
        boolean wifiConnected = false;

        SystemInfo() {
        }
    }

    /* access modifiers changed from: package-private */
    public void startCollecting(PkgData pkgData, String str) {
        String packageName = pkgData.getPackageName();
        GosLog.d(LOG_TAG, "startCollecting for " + packageName);
        if (!SystemDataHelper.isCollectingAgreedByUser(AppContext.get())) {
            GosLog.i(LOG_TAG, "startCollecting is cancelled because of no user agreement.");
            return;
        }
        SystemInfo systemInfo = (SystemInfo) new Gson().fromJson(str, SystemInfo.class);
        Pair<Integer, Integer> batteryInfo = getBatteryInfo(systemInfo, str);
        int intValue = ((Integer) batteryInfo.first).intValue();
        int intValue2 = ((Integer) batteryInfo.second).intValue();
        debugLog("batteryPercentResume: " + intValue);
        debugLog("cableConnectedResume: " + intValue2);
        Pair<Integer, Integer> brightnessInfo = getBrightnessInfo(systemInfo, str);
        int intValue3 = ((Integer) brightnessInfo.first).intValue();
        int intValue4 = ((Integer) brightnessInfo.second).intValue();
        debugLog("screenBrightnessResume: " + intValue3);
        debugLog("screenBrightnessModeResume: " + intValue4);
        this.mRunningGame = new RunningGame(packageName, pkgData, System.currentTimeMillis(), intValue, intValue2, intValue3, intValue4);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0264  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0284  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stopCollecting(java.lang.String r40) {
        /*
            r39 = this;
            r1 = r39
            r0 = r40
            java.lang.String r2 = "StatusCollector"
            java.lang.String r3 = "stopCollecting"
            com.samsung.android.game.gos.util.GosLog.d(r2, r3)
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r3 = r1.mRunningGame
            if (r3 != 0) goto L_0x0010
            return
        L_0x0010:
            long r14 = r3.mStartTime
            long r21 = java.lang.System.currentTimeMillis()
            long r3 = r21 - r14
            r5 = 1000(0x3e8, double:4.94E-321)
            long r12 = r3 / r5
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "duration (sec): "
            r3.append(r4)
            r3.append(r12)
            java.lang.String r3 = r3.toString()
            r1.debugLog(r3)
            com.samsung.android.game.gos.data.PreferenceHelper r3 = new com.samsung.android.game.gos.data.PreferenceHelper
            r3.<init>()
            java.lang.String r4 = "LAST_PAUSED_GAME_PKG_NAME"
            r11 = 0
            java.lang.String r4 = r3.getValue((java.lang.String) r4, (java.lang.String) r11)
            r7 = -1
            java.lang.String r9 = "LAST_PAUSED_GAME_TIME_STAMP"
            long r9 = r3.getValue((java.lang.String) r9, (long) r7)
            if (r4 == 0) goto L_0x0060
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r8 = r1.mRunningGame
            java.lang.String r8 = r8.mCurrentGame
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x0060
            r16 = 0
            int r4 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1))
            if (r4 < 0) goto L_0x0060
            long r16 = r21 - r9
            int r4 = (r16 > r5 ? 1 : (r16 == r5 ? 0 : -1))
            if (r4 >= 0) goto L_0x0060
            r4 = 1
            goto L_0x0061
        L_0x0060:
            r4 = 0
        L_0x0061:
            if (r4 == 0) goto L_0x02dd
            long r5 = r5 * r12
            r16 = 5000(0x1388, double:2.4703E-320)
            int r4 = (r5 > r16 ? 1 : (r5 == r16 ? 0 : -1))
            if (r4 >= 0) goto L_0x006c
            goto L_0x02dd
        L_0x006c:
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r4 = r1.mRunningGame
            java.lang.String r4 = r4.mCurrentGame
            com.google.gson.Gson r5 = new com.google.gson.Gson
            r5.<init>()
            java.lang.Class<com.samsung.android.game.gos.controller.StatusCollector$SystemInfo> r6 = com.samsung.android.game.gos.controller.StatusCollector.SystemInfo.class
            java.lang.Object r5 = r5.fromJson((java.lang.String) r0, r6)
            com.samsung.android.game.gos.controller.StatusCollector$SystemInfo r5 = (com.samsung.android.game.gos.controller.StatusCollector.SystemInfo) r5
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r6 = r1.mRunningGame
            int r6 = r6.mBatteryPercentResume
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r8 = r1.mRunningGame
            int r8 = r8.mCableConnectedResume
            android.util.Pair r11 = r1.getBatteryInfo(r5, r0)
            java.lang.Object r7 = r11.first
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r7 = r7.intValue()
            java.lang.Object r11 = r11.second
            java.lang.Integer r11 = (java.lang.Integer) r11
            int r11 = r11.intValue()
            r18 = r9
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "batteryPercentage: "
            r9.append(r10)
            r9.append(r7)
            java.lang.String r9 = r9.toString()
            r1.debugLog(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "cableConnected: "
            r9.append(r10)
            r9.append(r11)
            java.lang.String r9 = r9.toString()
            r1.debugLog(r9)
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r9 = r1.mRunningGame
            int r10 = r9.mScreenBrightnessResume
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r9 = r1.mRunningGame
            int r9 = r9.mScreenBrightnessModeResume
            r20 = r7
            android.util.Pair r7 = r1.getBrightnessInfo(r5, r0)
            r23 = r9
            java.lang.Object r9 = r7.first
            java.lang.Integer r9 = (java.lang.Integer) r9
            int r9 = r9.intValue()
            java.lang.Object r7 = r7.second
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r7 = r7.intValue()
            r24 = r12
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "screenBrightness: "
            r12.append(r13)
            r12.append(r9)
            java.lang.String r12 = r12.toString()
            r1.debugLog(r12)
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "automaticBrightness: "
            r12.append(r13)
            r12.append(r7)
            java.lang.String r12 = r12.toString()
            r1.debugLog(r12)
            int r13 = r1.getWifiConnected(r5, r0)
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r26 = r7
            java.lang.String r7 = "wifiConnected: "
            r12.append(r7)
            r12.append(r13)
            java.lang.String r7 = r12.toString()
            r1.debugLog(r7)
            android.util.Pair r7 = r1.getAudioInfo(r5, r0)
            java.lang.Object r12 = r7.first
            java.lang.Integer r12 = (java.lang.Integer) r12
            int r12 = r12.intValue()
            java.lang.Object r7 = r7.second
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r7 = r7.intValue()
            r27 = r9
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r28 = r13
            java.lang.String r13 = "volume: "
            r9.append(r13)
            r9.append(r12)
            java.lang.String r9 = r9.toString()
            r1.debugLog(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r13 = "audioOutputDevice: "
            r9.append(r13)
            r9.append(r7)
            java.lang.String r9 = r9.toString()
            r1.debugLog(r9)
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r9 = r1.mRunningGame
            com.samsung.android.game.gos.data.PkgData r9 = r9.mGameData
            android.util.Pair r0 = r1.getVersionInfo(r9, r5, r0)
            java.lang.Object r5 = r0.first
            r13 = r5
            java.lang.String r13 = (java.lang.String) r13
            java.lang.Object r0 = r0.second
            java.lang.Long r0 = (java.lang.Long) r0
            r29 = r14
            long r14 = r0.longValue()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "versionName: "
            r0.append(r5)
            r0.append(r13)
            java.lang.String r5 = ", versionCode: "
            r0.append(r5)
            r0.append(r14)
            java.lang.String r0 = r0.toString()
            r1.debugLog(r0)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r0 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalDao r0 = r0.getGlobalDao()
            int r31 = r0.getSiopMode()
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r0 = r1.mRunningGame
            com.samsung.android.game.gos.data.PkgData r0 = r0.mGameData
            com.samsung.android.game.gos.data.model.Package r0 = r0.getPkg()
            float r0 = r0.getAppliedDss()
            float r5 = com.samsung.android.game.gos.util.TypeConverter.roundOff(r0)
            java.lang.String r32 = java.lang.String.valueOf(r5)
            r5 = -1
            boolean r9 = com.samsung.android.game.gos.feature.dss.TssCore.isAvailable()
            if (r9 == 0) goto L_0x01d8
            com.samsung.android.game.gos.feature.dss.DssFeature r5 = com.samsung.android.game.gos.feature.dss.DssFeature.getInstance()
            int r5 = r5.getDisplayShortSide()
            float r5 = (float) r5
            float r5 = r5 * r0
            r0 = 1120403456(0x42c80000, float:100.0)
            float r5 = r5 / r0
            int r0 = (int) r5
            java.lang.String r5 = "tss"
            r34 = r0
            r33 = r5
            goto L_0x01de
        L_0x01d8:
            java.lang.String r0 = "dss"
            r33 = r0
            r34 = r5
        L_0x01de:
            com.samsung.android.game.gos.data.dbhelper.DbHelper r0 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalDao r0 = r0.getGlobalDao()
            java.lang.String r0 = r0.getResumeBoostPolicy()
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r5 = r1.mRunningGame
            com.samsung.android.game.gos.data.PkgData r5 = r5.mGameData
            com.samsung.android.game.gos.data.model.Package r5 = r5.getPkg()
            java.lang.String r5 = r5.getResumeBoostPolicy()
            int r35 = r1.getBoostInfo(r0, r5)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r0 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalDao r0 = r0.getGlobalDao()
            java.lang.String r0 = r0.getLaunchBoostPolicy()
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r5 = r1.mRunningGame
            com.samsung.android.game.gos.data.PkgData r5 = r5.mGameData
            com.samsung.android.game.gos.data.model.Package r5 = r5.getPkg()
            java.lang.String r5 = r5.getLaunchBoostPolicy()
            int r36 = r1.getBoostInfo(r0, r5)
            com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore r0 = com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore.getInstanceUnsafe()
            if (r0 == 0) goto L_0x022f
            com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore$ExternalSdkUsage r0 = r0.getSdkUsage()
            if (r0 == 0) goto L_0x022f
            java.lang.String r5 = r0.getType()
            org.json.JSONArray r0 = r0.getEvents()
            r38 = r0
            r37 = r5
            goto L_0x0233
        L_0x022f:
            r37 = 0
            r38 = 0
        L_0x0233:
            com.samsung.android.game.gos.feature.ipm.IpmCore r0 = com.samsung.android.game.gos.feature.ipm.IpmCore.getInstanceUnsafe()
            if (r0 == 0) goto L_0x023f
            org.json.JSONObject r0 = r0.printParametersUsedToJsonFormat()
            r5 = r0
            goto L_0x0240
        L_0x023f:
            r5 = 0
        L_0x0240:
            if (r5 != 0) goto L_0x025a
            org.json.JSONObject r9 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0252 }
            r9.<init>()     // Catch:{ JSONException -> 0x0252 }
            java.lang.String r0 = "spa_on"
            r5 = 0
            r9.put(r0, r5)     // Catch:{ JSONException -> 0x024f }
            r0 = r9
            goto L_0x025b
        L_0x024f:
            r0 = move-exception
            r5 = r9
            goto L_0x0253
        L_0x0252:
            r0 = move-exception
        L_0x0253:
            java.lang.String r0 = r0.getMessage()
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r2, (java.lang.String) r0)
        L_0x025a:
            r0 = r5
        L_0x025b:
            java.lang.String r2 = "PREF_SELECTIVE_TARGET_POLICY"
            r5 = 0
            boolean r2 = r3.getValue((java.lang.String) r2, (boolean) r5)
            if (r2 == 0) goto L_0x0284
            com.samsung.android.game.gos.controller.StatusCollector$RunningGame r2 = r1.mRunningGame
            com.samsung.android.game.gos.data.PkgData r2 = r2.mGameData
            com.samsung.android.game.gos.data.model.Package r2 = r2.getPkg()
            java.lang.String r2 = r2.targetGroupName
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "targetGroupName: "
            r3.append(r5)
            r3.append(r2)
            java.lang.String r3 = r3.toString()
            r1.debugLog(r3)
            r5 = r2
            goto L_0x0285
        L_0x0284:
            r5 = 0
        L_0x0285:
            com.samsung.android.game.gos.data.dbhelper.ReportDbHelper r2 = com.samsung.android.game.gos.data.dbhelper.ReportDbHelper.getInstance()
            r3 = r20
            r17 = r26
            r20 = r7
            r7 = r8
            r8 = r3
            r40 = r2
            r1 = r18
            r3 = r23
            r18 = r27
            r9 = r11
            r11 = r3
            r3 = r12
            r23 = r24
            r12 = r18
            r25 = r13
            r16 = r28
            r13 = r17
            r26 = r29
            r28 = r14
            r14 = r16
            r15 = r3
            r16 = r20
            r17 = r23
            r19 = r26
            r23 = r25
            r24 = r28
            r26 = r31
            r27 = r33
            r28 = r32
            r29 = r34
            r30 = r35
            r31 = r36
            r32 = r37
            r33 = r38
            r34 = r0
            java.lang.String r0 = getUserUsageJsonMsg(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r19, r21, r23, r24, r26, r27, r28, r29, r30, r31, r32, r33, r34)
            java.lang.String r3 = "user_usage"
            r4 = r40
            r4.addOrUpdateReportData(r3, r1, r0)
            r39.resetFeatureStatus()
            r2 = 0
            r1 = r39
            r1.mRunningGame = r2
            return
        L_0x02dd:
            r2 = r11
            r1.mRunningGame = r2
            r39.resetFeatureStatus()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.controller.StatusCollector.stopCollecting(java.lang.String):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.lang.String} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.util.Pair<java.lang.String, java.lang.Long> getVersionInfo(com.samsung.android.game.gos.data.PkgData r5, com.samsung.android.game.gos.controller.StatusCollector.SystemInfo r6, java.lang.String r7) {
        /*
            r4 = this;
            com.samsung.android.game.gos.data.model.Package r0 = r5.getPkg()
            java.lang.String r0 = r0.getVersionName()
            if (r0 == 0) goto L_0x0024
            android.util.Pair r6 = new android.util.Pair
            com.samsung.android.game.gos.data.model.Package r7 = r5.getPkg()
            java.lang.String r7 = r7.getVersionName()
            com.samsung.android.game.gos.data.model.Package r5 = r5.getPkg()
            long r0 = r5.getVersionCode()
            java.lang.Long r5 = java.lang.Long.valueOf(r0)
            r6.<init>(r7, r5)
            return r6
        L_0x0024:
            r0 = 0
            r1 = 0
            if (r6 == 0) goto L_0x0065
            java.lang.String r3 = "versionName"
            boolean r3 = r7.contains(r3)
            if (r3 == 0) goto L_0x0065
            java.lang.String r3 = "versionCode"
            boolean r7 = r7.contains(r3)
            if (r7 == 0) goto L_0x0065
            java.lang.String r0 = r6.versionName
            long r1 = r6.versionCode     // Catch:{ ArithmeticException -> 0x0043 }
            int r5 = java.lang.Math.toIntExact(r1)     // Catch:{ ArithmeticException -> 0x0043 }
            long r1 = (long) r5
            goto L_0x0082
        L_0x0043:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = "stopCollecting(): versionCode of '"
            r7.append(r1)
            java.lang.String r5 = r5.getPackageName()
            r7.append(r5)
            java.lang.String r5 = "' is too big as int."
            r7.append(r5)
            java.lang.String r5 = r7.toString()
            java.lang.String r7 = "StatusCollector"
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r7, (java.lang.String) r5)
            long r1 = r6.versionCode
            goto L_0x0082
        L_0x0065:
            com.samsung.android.game.gos.data.model.Package r5 = r5.getPkg()
            r6 = 4
            android.util.Pair r5 = com.samsung.android.game.gos.controller.SystemEventReactor.collectInstalledOrUpdatedGameInfo(r5, r6)
            if (r5 == 0) goto L_0x0082
            java.lang.Object r6 = r5.first
            r0 = r6
            java.lang.String r0 = (java.lang.String) r0
            java.lang.Object r6 = r5.second
            if (r6 == 0) goto L_0x0082
            java.lang.Object r5 = r5.second
            java.lang.Long r5 = (java.lang.Long) r5
            long r5 = r5.longValue()
            r1 = r5
        L_0x0082:
            android.util.Pair r5 = new android.util.Pair
            java.lang.Long r6 = java.lang.Long.valueOf(r1)
            r5.<init>(r0, r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.controller.StatusCollector.getVersionInfo(com.samsung.android.game.gos.data.PkgData, com.samsung.android.game.gos.controller.StatusCollector$SystemInfo, java.lang.String):android.util.Pair");
    }

    /* access modifiers changed from: package-private */
    public Pair<Integer, Integer> getBatteryInfo(SystemInfo systemInfo, String str) {
        if (systemInfo != null && str.contains("batteryPercent") && str.contains("batteryPluggedType")) {
            return new Pair<>(Integer.valueOf(systemInfo.batteryPercent), Integer.valueOf(systemInfo.batteryPluggedType));
        }
        Intent registerReceiver = AppContext.get().registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null) {
            return new Pair<>(0, 0);
        }
        return new Pair<>(Integer.valueOf((registerReceiver.getIntExtra("level", -1) * 100) / registerReceiver.getIntExtra(FpsController.TYPE_SCALE, -1)), Integer.valueOf(registerReceiver.getIntExtra("plugged", 0)));
    }

    /* access modifiers changed from: package-private */
    public Pair<Integer, Integer> getBrightnessInfo(SystemInfo systemInfo, String str) {
        int i;
        if (systemInfo != null && str.contains("brightness") && str.contains("brightnessMode")) {
            return new Pair<>(Integer.valueOf(systemInfo.brightness), Integer.valueOf(systemInfo.brightnessMode));
        }
        int i2 = -1;
        try {
            i = Settings.System.getInt(AppContext.get().getContentResolver(), ReportData.UserUsageKey.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException unused) {
            GosLog.i(LOG_TAG, "get screenBrightness failure");
            i = -1;
        }
        try {
            i2 = Settings.System.getInt(AppContext.get().getContentResolver(), ReportData.UserUsageKey.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException unused2) {
            GosLog.i(LOG_TAG, "get screenBrightnessMode failure");
        }
        return new Pair<>(Integer.valueOf(i), Integer.valueOf(i2));
    }

    /* access modifiers changed from: package-private */
    public Pair<Integer, Integer> getAudioInfo(SystemInfo systemInfo, String str) {
        if (systemInfo != null && str.contains("audioVolume") && str.contains("audioOutputDevice")) {
            return new Pair<>(Integer.valueOf(systemInfo.audioVolume), Integer.valueOf(systemInfo.audioOutputDevice));
        }
        int volume = SeAudioManager.getInstance().getVolume();
        int i = 0;
        int currentDeviceType = SeAudioManager.getInstance().getCurrentDeviceType();
        if (currentDeviceType == 3 || currentDeviceType == 4 || currentDeviceType == 22) {
            i = 1;
        } else if (currentDeviceType == 7 || currentDeviceType == 8) {
            i = 2;
        }
        GosLog.v(LOG_TAG, "current device type: " + currentDeviceType);
        return new Pair<>(Integer.valueOf(volume), Integer.valueOf(i));
    }

    /* access modifiers changed from: package-private */
    public int getWifiConnected(SystemInfo systemInfo, String str) {
        if (systemInfo != null && str.contains("wifiConnected")) {
            return systemInfo.wifiConnected ? 1 : 0;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.get().getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            if (networkInfo != null) {
                return networkInfo.isConnected() ? 1 : 0;
            }
            return -1;
        }
        GosLog.w(LOG_TAG, "getWifiConnected()-connectivityManager is null");
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int getBoostInfo(String str, String str2) {
        JSONObject jSONObject = null;
        if (str == null || str.equals(BuildConfig.VERSION_NAME)) {
            str = null;
        }
        if (str2 == null || str2.equals(BuildConfig.VERSION_NAME)) {
            str2 = str;
        }
        if (str2 != null) {
            try {
                jSONObject = new JSONObject(str2);
            } catch (JSONException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        if (jSONObject != null) {
            return jSONObject.optInt("duration", 10);
        }
        return 10;
    }

    private void resetFeatureStatus() {
        IpmCore instanceUnsafe = IpmCore.getInstanceUnsafe();
        if (instanceUnsafe != null) {
            instanceUnsafe.resetSpaOn();
        }
        ExternalSdkCore instanceUnsafe2 = ExternalSdkCore.getInstanceUnsafe();
        if (instanceUnsafe2 != null) {
            instanceUnsafe2.resetSdkUsage();
        }
    }

    static class RunningGame {
        int mBatteryPercentResume;
        int mCableConnectedResume;
        /* access modifiers changed from: private */
        public String mCurrentGame;
        PkgData mGameData;
        int mScreenBrightnessModeResume;
        int mScreenBrightnessResume;
        long mStartTime;

        RunningGame(String str, PkgData pkgData, long j, int i, int i2, int i3, int i4) {
            this.mCurrentGame = str;
            this.mGameData = pkgData;
            this.mStartTime = j;
            this.mBatteryPercentResume = i;
            this.mCableConnectedResume = i2;
            this.mScreenBrightnessResume = i3;
            this.mScreenBrightnessModeResume = i4;
        }
    }

    /* access modifiers changed from: package-private */
    public RunningGame getmRunningGame() {
        return this.mRunningGame;
    }

    /* access modifiers changed from: package-private */
    public void setmRunningGame(RunningGame runningGame) {
        this.mRunningGame = runningGame;
    }
}
