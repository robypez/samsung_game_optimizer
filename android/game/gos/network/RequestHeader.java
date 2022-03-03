package com.samsung.android.game.gos.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.LocalCache;
import com.samsung.android.game.gos.data.SystemDataHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class RequestHeader {
    static final String HEADER_KEY_CALL_TRIGGER = "ct";
    static final String HEADER_KEY_ELAPSED_TIME = "et";
    static final String HEADER_KEY_USER_AGENT = "User-Agent";
    static final String HEADER_KEY_X_TRACE_ID = "X-Samsung-Trace-Id";
    private static final String LOG_TAG = RequestHeader.class.getSimpleName();
    private static final String USER_AGENT_KEY_API_LEVEL = "al";
    private static final String USER_AGENT_KEY_DEVICE_NAME = "device_name";
    private static final String USER_AGENT_KEY_DMA_ID = "dma_id";
    private static final String USER_AGENT_KEY_GMS_VERSION = "gms_version";
    private static final String USER_AGENT_KEY_GOS_VERSION = "gos_version";
    private static final String USER_AGENT_KEY_INSTALLED_SEC_GAME_FAMILY = "installed_sec_game_family";
    static final String USER_AGENT_KEY_MCC = "mcc";
    static final String USER_AGENT_KEY_MNC = "mnc";
    private static final String USER_AGENT_KEY_MODEL_NAME = "model_name";
    private static final String USER_AGENT_KEY_SAMSUNG_ERRORLOG_AGREE = "samsung_errorlog_agree";
    private static final String USER_AGENT_KEY_UUID = "uuid";
    private static final String USER_AGENT_KEY_VERSION_INCREMENTAL = "version_i";
    private static final String USER_AGENT_KEY_VERSION_RELEASE = "version_r";
    private final String userAgent;

    RequestHeader(Context context) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            float gmsVersion = DbHelper.getInstance().getGlobalDao().getGmsVersion();
            linkedHashMap.put(USER_AGENT_KEY_GMS_VERSION, String.valueOf(gmsVersion));
            String str = LOG_TAG;
            GosLog.v(str, "gms_version : " + gmsVersion);
            long packageVersionCode = PackageUtil.getPackageVersionCode(AppContext.get(), "com.samsung.android.game.gos");
            linkedHashMap.put(USER_AGENT_KEY_GOS_VERSION, String.valueOf(packageVersionCode));
            String str2 = LOG_TAG;
            GosLog.v(str2, "gos_version : " + packageVersionCode);
            String deviceName = DbHelper.getInstance().getGlobalDao().getDeviceName();
            linkedHashMap.put("device_name", deviceName);
            String str3 = LOG_TAG;
            GosLog.v(str3, "device_name : " + deviceName);
            String originalModelName = AppVariable.getOriginalModelName();
            linkedHashMap.put(USER_AGENT_KEY_MODEL_NAME, originalModelName);
            String str4 = LOG_TAG;
            GosLog.v(str4, "model_name : " + originalModelName);
            if (Build.VERSION.SDK_INT >= 4) {
                int i = Build.VERSION.SDK_INT;
                linkedHashMap.put(USER_AGENT_KEY_API_LEVEL, String.valueOf(i));
                String str5 = LOG_TAG;
                GosLog.v(str5, "al : " + i);
            }
            String str6 = Build.VERSION.RELEASE;
            linkedHashMap.put(USER_AGENT_KEY_VERSION_RELEASE, str6);
            String str7 = LOG_TAG;
            GosLog.v(str7, "version_r : " + str6);
            String str8 = Build.VERSION.INCREMENTAL;
            linkedHashMap.put(USER_AGENT_KEY_VERSION_INCREMENTAL, str8);
            String str9 = LOG_TAG;
            GosLog.v(str9, "version_i : " + str8);
            String uuid = GlobalDbHelper.getInstance().getUUID();
            linkedHashMap.put("uuid", uuid);
            String str10 = LOG_TAG;
            GosLog.v(str10, "uuid : " + uuid);
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                String networkOperator = telephonyManager.getNetworkOperator();
                tryToAddMcc(linkedHashMap, networkOperator);
                tryToAddMnc(linkedHashMap, networkOperator);
            }
            String stringsToCsv = TypeConverter.stringsToCsv((Iterable<String>) getInstalledSecGameFamily(context, new LocalCache().getSecGameFamilyPackageNames(), context.getPackageManager().getInstalledPackages(0)));
            String str11 = LOG_TAG;
            GosLog.d(str11, "installed_sec_game_family : " + stringsToCsv);
            linkedHashMap.put(USER_AGENT_KEY_INSTALLED_SEC_GAME_FAMILY, stringsToCsv);
            linkedHashMap.put(USER_AGENT_KEY_SAMSUNG_ERRORLOG_AGREE, String.valueOf(SystemDataHelper.getSamsungErrorlogAgree(context)));
            String dmaId = DbHelper.getInstance().getGlobalDao().getDmaId();
            if (dmaId != null) {
                linkedHashMap.put(USER_AGENT_KEY_DMA_ID, dmaId);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            if (entry.getKey() != null && ((String) entry.getKey()).length() > 0 && entry.getValue() != null && ((String) entry.getValue()).length() > 0) {
                sb.append((String) entry.getKey());
                sb.append(':');
                sb.append((String) entry.getValue());
                sb.append(';');
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(";"));
        sb.append(')');
        this.userAgent = sb.toString();
    }

    /* access modifiers changed from: package-private */
    public void tryToAddMcc(Map<String, String> map, String str) {
        if (str != null) {
            try {
                String substring = str.substring(0, 3);
                String str2 = LOG_TAG;
                GosLog.d(str2, "mcc : " + substring);
                map.put(USER_AGENT_KEY_MCC, substring);
            } catch (StringIndexOutOfBoundsException unused) {
                GosLog.w(LOG_TAG, "tryToAddMcc(), can't find mcc");
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void tryToAddMnc(Map<String, String> map, String str) {
        if (str != null) {
            try {
                String substring = str.substring(3);
                String str2 = LOG_TAG;
                GosLog.d(str2, "mnc : " + substring);
                map.put(USER_AGENT_KEY_MNC, substring);
            } catch (StringIndexOutOfBoundsException unused) {
                GosLog.w(LOG_TAG, "tryToAddMnc(), can't find mnc");
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public List<String> getInstalledSecGameFamily(Context context, List<String> list, List<PackageInfo> list2) {
        ArrayList arrayList = new ArrayList();
        if (!(list == null || list2 == null)) {
            ArrayList arrayList2 = new ArrayList();
            PackageManager packageManager = context.getPackageManager();
            for (PackageInfo next : list2) {
                if (!(next == null || next.packageName == null || !list.contains(next.packageName))) {
                    boolean z = false;
                    try {
                        z = packageManager.getApplicationInfo(next.packageName, 128).enabled;
                    } catch (PackageManager.NameNotFoundException unused) {
                        arrayList2.add(next.packageName);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(next.packageName);
                    sb.append("=");
                    sb.append(z ? "enabled" : State.DISABLED);
                    arrayList.add(sb.toString());
                }
            }
            if (!arrayList2.isEmpty()) {
                String str = LOG_TAG;
                GosLog.w(str, "getInstalledSecGameFamily(), nameNotFoundPackages=" + TypeConverter.stringsToCsv((Iterable<String>) arrayList2));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public String getUserAgent() {
        return this.userAgent;
    }
}
