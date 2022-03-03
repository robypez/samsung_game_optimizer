package com.samsung.android.game.gos.endpoint;

import android.content.Intent;
import android.os.IBinder;
import android.util.Pair;
import com.samsung.android.game.gos.IGosService;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.ISecureSettingChangeListener;
import com.samsung.android.game.gos.data.SecureSettingHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.GosServiceUsage;
import com.samsung.android.game.gos.selibrary.SeSecureSetting;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.SecureSettingConstants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GosService extends AidlService implements ISecureSettingChangeListener {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = GosService.class.getSimpleName();
    private final IGosService.Stub mBinder = new IGosService.Stub() {
        public String requestWithJson(String str, String str2) {
            String access$000 = GosService.LOG_TAG;
            GosLog.d(access$000, "requestWithJson. requestCommand: " + str);
            Pair<Boolean, String> permissionInfo = GosService.this.getPermissionInfo();
            if (!((Boolean) permissionInfo.first).booleanValue()) {
                return null;
            }
            GosService.this.logCommand(str, (String) permissionInfo.second);
            return MethodsForJsonCommand.respondWithJson(str, str2, (String) permissionInfo.second);
        }
    };

    public void onSecureSettingChanged(String str, Object obj) {
        List list;
        GosLog.d(LOG_TAG, "onSecureSettingChanged(), " + str + "=" + obj);
        if (Objects.equals(str, SecureSettingConstants.KEY_ALLOW_MORE_HEAT)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.US);
            String str2 = simpleDateFormat.format(new Date(System.currentTimeMillis())) + "-" + obj;
            GosLog.d(LOG_TAG, "onSecureSettingChanged(), data=" + str2);
            String string = new SeSecureSetting().getString(AppContext.get().getContentResolver(), SeSecureSetting.KEY_ALLOW_MORE_HEAT_HISTORY);
            if (string != null) {
                list = Arrays.asList(string.split(","));
            } else {
                list = new ArrayList();
            }
            ArrayList arrayList = new ArrayList();
            int i = 0;
            int size = list.size();
            if (list.size() >= 5) {
                i = list.size() - 4;
                size = 5;
            }
            while (i < size) {
                arrayList.add(list.get(i));
                i++;
            }
            arrayList.add(str2);
            String stringsToCsv = TypeConverter.stringsToCsv((Iterable<String>) arrayList);
            new SeSecureSetting().putString(AppContext.get().getContentResolver(), SeSecureSetting.KEY_ALLOW_MORE_HEAT_HISTORY, TypeConverter.stringsToCsv((Iterable<String>) arrayList));
            GosLog.d(LOG_TAG, "onSecureSettingChanged(), allow_more_heat_history=" + stringsToCsv);
        }
    }

    public IBinder onBind(Intent intent) {
        GosLog.i(LOG_TAG, "onBind.");
        if (((Boolean) getPermissionInfo().first).booleanValue()) {
            return this.mBinder;
        }
        GosLog.e(LOG_TAG, "onBind()-bind fail - wrong permissionInfo");
        return null;
    }

    public void onRebind(Intent intent) {
        GosLog.i(LOG_TAG, "onRebind.");
    }

    /* access modifiers changed from: package-private */
    public void logCommand(String str, String str2) {
        if (str != null && !str.isEmpty() && str2 != null && !str2.isEmpty()) {
            DbHelper.getInstance().getGosServiceUsageDao().insertOrUpdate(new GosServiceUsage(str, str2));
        }
    }

    public boolean onUnbind(Intent intent) {
        GosLog.i(LOG_TAG, "onUnbind.");
        return true;
    }

    public void onCreate() {
        GosLog.i(LOG_TAG, "onCreate.");
        super.onCreate();
        GosLog.d(LOG_TAG, getApplicationContext().toString());
        SecureSettingHelper.getInstance().registerListener(SecureSettingConstants.KEY_ALLOW_MORE_HEAT, this);
    }

    public void onDestroy() {
        GosLog.i(LOG_TAG, "onDestroy.");
        SecureSettingHelper.getInstance().unregisterListener(SecureSettingConstants.KEY_ALLOW_MORE_HEAT, this);
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        GosLog.d(LOG_TAG, "onStartCommand.");
        if (intent != null) {
            GosLog.i(LOG_TAG, "onStartCommand. " + intent.toString());
            Constants.IntentType valueOf = Constants.IntentType.valueOf(intent.getIntExtra("type", Constants.IntentType.UNKNOWN.val()));
            if (valueOf == Constants.IntentType.STOP_COMMAND) {
                GosLog.i(LOG_TAG, "received stop command");
                stopSelf();
                return 2;
            } else if (valueOf != Constants.IntentType.JSON_COMMAND_TEST) {
                return 3;
            } else {
                String str = LOG_TAG + "[JSON_COMMAND_TEST]";
                String stringExtra = intent.getStringExtra("command");
                String stringExtra2 = intent.getStringExtra("jsonParam");
                intent.getStringExtra("callerPkg");
                GosLog.d(str, "command: " + stringExtra + ", jsonParam: " + stringExtra2);
                String respondWithJson = MethodsForJsonCommand.respondWithJson(stringExtra, stringExtra2, (String) null);
                StringBuilder sb = new StringBuilder();
                sb.append("retStr: ");
                sb.append(respondWithJson);
                GosLog.d(str, sb.toString());
                return 3;
            }
        } else {
            GosLog.w(LOG_TAG, "onStartCommand. intent is null");
            return 3;
        }
    }
}
