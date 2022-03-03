package com.samsung.android.game.gos.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUpdater;
import com.samsung.android.game.gos.controller.DataUploader;
import com.samsung.android.game.gos.controller.FeatureSetManager;
import com.samsung.android.game.gos.data.SystemDataHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.ReportDbHelper;
import com.samsung.android.game.gos.feature.ScheduledInterface;
import com.samsung.android.game.gos.network.INetworkTaskCallback;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.NetworkUtil;
import com.samsung.android.game.gos.value.Constants;
import java.util.concurrent.ThreadLocalRandom;

public class AlarmController implements INetworkTaskCallback {
    private static final String LOG_TAG = AlarmController.class.getSimpleName();

    public static DataUpdater.PkgUpdateType onUpdateAlarm(Context context, NetworkConnector networkConnector, Constants.IntentType intentType) {
        DataUpdater.PkgUpdateType pkgUpdateType;
        String str = LOG_TAG;
        GosLog.i(str, "onUpdateAlarm(), start!, IntentType: " + intentType.name());
        long currentTimeMillis = System.currentTimeMillis();
        long updateTime = currentTimeMillis - DbHelper.getInstance().getGlobalDao().getUpdateTime();
        long fullyUpdateTime = currentTimeMillis - DbHelper.getInstance().getGlobalDao().getFullyUpdateTime();
        int i = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$value$Constants$IntentType[intentType.ordinal()];
        if (i != 1) {
            if (i == 2) {
                GosLog.i(LOG_TAG, "onUpdateAlarm(), Server connection fail alarm does forcibly update.");
                pkgUpdateType = DataUpdater.PkgUpdateType.ALL;
            } else if (i != 3) {
                GosLog.e(LOG_TAG, "onUpdateAlarm(), IntentType is not correct.");
                return null;
            } else if (fullyUpdateTime >= Constants.UPDATE_INTERVAL_10DAYS) {
                printUpdateAlarmLog("fully update is available.", true);
                pkgUpdateType = DataUpdater.PkgUpdateType.ALL;
            } else if (updateTime >= 86400000) {
                printUpdateAlarmLog("update is available.", false);
                pkgUpdateType = DataUpdater.PkgUpdateType.MANAGED_PACKAGES_AND_UNIDENTIFIED_AND_UNKNOWN;
            } else {
                printUpdateAlarmLog("too early. skip update.", false);
                return null;
            }
        } else if (fullyUpdateTime <= Constants.UPDATE_INTERVAL_10DAYS) {
            printUpdateAlarmLog("too early. skip full update.", true);
            if (updateTime <= 86400000) {
                printUpdateAlarmLog("too early. skip normal update.", false);
                return null;
            }
            if (updateTime > Constants.UPDATE_INTERVAL_2DAYS) {
                printUpdateAlarmLog("network update is available.", false);
            } else if (!NetworkUtil.isWifiConnected(context)) {
                printUpdateAlarmLog("Wi-Fi is not available.", false);
                return null;
            } else {
                printUpdateAlarmLog("Wi-Fi update is available.", false);
            }
            pkgUpdateType = DataUpdater.PkgUpdateType.MANAGED_PACKAGES_AND_UNIDENTIFIED_AND_UNKNOWN;
        } else {
            printUpdateAlarmLog("fully update is available.", true);
            pkgUpdateType = DataUpdater.PkgUpdateType.ALL;
        }
        for (ScheduledInterface next : FeatureSetManager.getScheduledFeatureMap(context).values()) {
            if (FeatureHelper.isEnabledFlagByUser(next.getName())) {
                next.onUpdateAlarm();
            }
        }
        DataUpdater.updateGlobalAndPkgData(context, pkgUpdateType, false, networkConnector, "alarm");
        if (!SystemDataHelper.isCollectingAgreedByUser(context)) {
            boolean removeAll = ReportDbHelper.getInstance().removeAll();
            String str2 = LOG_TAG;
            GosLog.i(str2, "onUpdateAlarm(), User not agreed, remove Ringlog data. successful: " + removeAll);
        } else if (DbHelper.getInstance().getGlobalDao().isRegisteredDevice() && NetworkUtil.isWifiConnected(context)) {
            DataUploader.uploadCombinationReportData(context, networkConnector);
        }
        String str3 = LOG_TAG;
        GosLog.i(str3, "onUpdateAlarm(), end!, update type: " + pkgUpdateType.name());
        return pkgUpdateType;
    }

    private static void printUpdateAlarmLog(String str, boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder("onUpdateAlarm() - ");
        sb.append(str);
        if (z) {
            sb.append(" millisFromLastFullyUpdate: ");
            sb.append(currentTimeMillis - DbHelper.getInstance().getGlobalDao().getFullyUpdateTime());
        } else {
            sb.append(" millisFromLastUpdate: ");
            sb.append(currentTimeMillis - DbHelper.getInstance().getGlobalDao().getUpdateTime());
        }
        GosLog.i(LOG_TAG, sb.toString());
    }

    public static void setUpdateAlarm(Context context, Constants.AlarmIntentType alarmIntentType) {
        Intent intent = new Intent(context, MainIntentService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        if (alarmManager == null) {
            GosLog.w(LOG_TAG, "setUpdateAlarm() - mAlarmManager is null!");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        int i = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$value$Constants$AlarmIntentType[alarmIntentType.ordinal()];
        if (i == 1) {
            intent.putExtra("type", Constants.IntentType.ON_SERVER_CONNECTION_FAIL_ALARM.val());
            alarmManager.setExactAndAllowWhileIdle(1, currentTimeMillis + ThreadLocalRandom.current().nextLong(Constants.UPDATE_CHECK_INTERVAL_ONE_HOUR) + Constants.UPDATE_CHECK_INTERVAL_30MINUTE, PendingIntent.getService(context, 1001, intent, 134217728));
        } else if (i != 2) {
            GosLog.e(LOG_TAG, "setUpdateAlarm(), IntentType is not correct.");
            return;
        } else {
            intent.putExtra("type", Constants.IntentType.ON_ALARM.val());
            alarmManager.setInexactRepeating(1, currentTimeMillis + Constants.UPDATE_CHECK_INTERVAL_ONE_HOUR, Constants.UPDATE_CHECK_INTERVAL_ONE_HOUR, PendingIntent.getService(context, 1000, intent, 134217728));
        }
        String str = LOG_TAG;
        GosLog.i(str, "setUpdateAlarm(), type: " + alarmIntentType.name());
    }

    /* renamed from: com.samsung.android.game.gos.service.AlarmController$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$value$Constants$AlarmIntentType;
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$value$Constants$IntentType;

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0038 */
        static {
            /*
                com.samsung.android.game.gos.value.Constants$AlarmIntentType[] r0 = com.samsung.android.game.gos.value.Constants.AlarmIntentType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$value$Constants$AlarmIntentType = r0
                r1 = 1
                com.samsung.android.game.gos.value.Constants$AlarmIntentType r2 = com.samsung.android.game.gos.value.Constants.AlarmIntentType.ON_FAIL     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$com$samsung$android$game$gos$value$Constants$AlarmIntentType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.value.Constants$AlarmIntentType r3 = com.samsung.android.game.gos.value.Constants.AlarmIntentType.UPDATE_CHECK     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                com.samsung.android.game.gos.value.Constants$IntentType[] r2 = com.samsung.android.game.gos.value.Constants.IntentType.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                $SwitchMap$com$samsung$android$game$gos$value$Constants$IntentType = r2
                com.samsung.android.game.gos.value.Constants$IntentType r3 = com.samsung.android.game.gos.value.Constants.IntentType.ON_ALARM     // Catch:{ NoSuchFieldError -> 0x002e }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x002e }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x002e }
            L_0x002e:
                int[] r1 = $SwitchMap$com$samsung$android$game$gos$value$Constants$IntentType     // Catch:{ NoSuchFieldError -> 0x0038 }
                com.samsung.android.game.gos.value.Constants$IntentType r2 = com.samsung.android.game.gos.value.Constants.IntentType.ON_SERVER_CONNECTION_FAIL_ALARM     // Catch:{ NoSuchFieldError -> 0x0038 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0038 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0038 }
            L_0x0038:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$Constants$IntentType     // Catch:{ NoSuchFieldError -> 0x0043 }
                com.samsung.android.game.gos.value.Constants$IntentType r1 = com.samsung.android.game.gos.value.Constants.IntentType.WIFI_CONNECTED     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.service.AlarmController.AnonymousClass1.<clinit>():void");
        }
    }

    public void onFail() {
        setUpdateAlarm(AppContext.get(), Constants.AlarmIntentType.ON_FAIL);
    }
}
