package com.samsung.android.game.gos.endpoint;

import android.content.Intent;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import com.samsung.android.game.gos.IGosSystemService;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.TestDataManager;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.dss.TssCore;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.test.gostester.GosTester;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class GosSystemService extends AidlService {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = GosSystemService.class.getSimpleName();
    private static Class<? extends AppCompatActivity> testActivityClass;
    private final IGosSystemService.Stub mBinder = new IGosSystemService.Stub() {
        public String requestOnlyForDumpOrTest(String str, String str2) {
            GosLog.d(GosSystemService.LOG_TAG, "requestOnlyForDumpOrTest(), command: " + str);
            if (!((Boolean) GosSystemService.this.getPermissionInfo().first).booleanValue()) {
                return null;
            }
            GlobalCommand globalCommand = new GlobalCommand();
            DbCommand dbCommand = new DbCommand();
            char c = 65535;
            switch (str.hashCode()) {
                case -1534405302:
                    if (str.equals("test_features")) {
                        c = 3;
                        break;
                    }
                    break;
                case -901770662:
                    if (str.equals("show_test_activity")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1831075:
                    if (str.equals(GosInterface.Command.GET_GLOBAL_DATA)) {
                        c = 0;
                        break;
                    }
                    break;
                case 170563267:
                    if (str.equals("spa_commands")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1379940181:
                    if (str.equals("get_encoded_database")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                return GosSystemService.this.addAdditionalInfoForGmsDump(globalCommand.getGlobalDataWithJson());
            }
            if (c == 1) {
                return dbCommand.getEncodedDatabase(str2);
            }
            if (c == 2) {
                return GosSystemService.showTestActivity();
            }
            if (c == 3) {
                return GosSystemService.getTestApiResponse(str2);
            }
            if (c != 4) {
                return null;
            }
            return IpmCore.getInstance(AppContext.get()).processCommands(str2);
        }

        public void requestWithJsonNoReturn(String str, String str2) {
            String access$000 = GosSystemService.LOG_TAG;
            GosLog.d(access$000, "requestWithJsonNoReturn(), command: " + str);
            if ("spa_commands".equals(str)) {
                IpmCore.getInstance(AppContext.get()).processCommands(str2);
            }
        }
    };

    public static class Command {
        static final String GET_ENCODED_DATABASE = "get_encoded_database";
        static final String SHOW_TEST_ACTIVITY = "show_test_activity";
        static final String SPA_COMMANDS = "spa_commands";
        static final String TEST_FEATURES = "test_features";
    }

    public /* bridge */ /* synthetic */ void onCreate() {
        super.onCreate();
    }

    public static void setTestActivityClass(Class<? extends AppCompatActivity> cls) {
        testActivityClass = cls;
    }

    public static class KeyName {
        static final String GMS_LOG = "gms_log";

        private KeyName() {
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

    public boolean onUnbind(Intent intent) {
        GosLog.i(LOG_TAG, "onUnbind.");
        return true;
    }

    /* access modifiers changed from: private */
    public String addAdditionalInfoForGmsDump(String str) {
        if (str == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            StringBuilder sb = new StringBuilder();
            if (DbHelper.getInstance().getGlobalFeatureFlagDao().isAvailable(Constants.V4FeatureFlag.RESOLUTION)) {
                sb.append("  TSS Available: ");
                sb.append(TssCore.isAvailable());
                sb.append("\n");
            }
            StringBuilder sb2 = new StringBuilder();
            for (GlobalFeatureFlag next : GlobalDbHelper.getInstance().getFeatureFlagMap().values()) {
                if (next.usingUserValue || next.usingPkgValue) {
                    if (sb2.length() > 0) {
                        sb2.append("\n");
                    }
                    sb2.append("  - ");
                    sb2.append(next.name.toUpperCase(Locale.US));
                    sb2.append("(uuv:");
                    sb2.append(next.usingUserValue);
                    sb2.append(", upv:");
                    sb2.append(next.usingPkgValue);
                    sb2.append(")");
                }
            }
            if (sb2.length() > 0) {
                sb2.insert(0, "  Feature state:\n");
                sb.append(sb2.toString());
                sb.append("\n");
            }
            Global global = DbHelper.getInstance().getGlobalDao().get();
            ArrayList arrayList = new ArrayList();
            if (global != null && global.registeredDevice) {
                for (Package next2 : DbHelper.getInstance().getPackageDao().getPackageListNotSyncedWithServer()) {
                    if (!Constants.CategoryCode.SEC_GAME_FAMILY.equals(next2.getCategoryCode())) {
                        if (!Constants.CategoryCode.VR_GAME.equals(next2.getCategoryCode())) {
                            arrayList.add(next2.getPkgName());
                        }
                    }
                }
            }
            sb.append("  Not-synced packages: ");
            sb.append(arrayList.toString());
            sb.append("\n");
            PreferenceHelper preferenceHelper = new PreferenceHelper();
            sb.append("  Selective Target Policy: ");
            sb.append(preferenceHelper.getValue(PreferenceHelper.PREF_SELECTIVE_TARGET_POLICY, false));
            sb.append("\n");
            jSONObject.put("gms_log", sb.toString());
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
                GosLog.d(str, "command: " + stringExtra + ", jsonParam: " + stringExtra2);
                String processCommands = IpmCore.getInstance(AppContext.get()).processCommands(stringExtra2);
                StringBuilder sb = new StringBuilder();
                sb.append("retStr: ");
                sb.append(processCommands);
                GosLog.d(str, sb.toString());
                return 3;
            }
        } else {
            GosLog.w(LOG_TAG, "onStartCommand. intent is null");
            return 3;
        }
    }

    static String getTestApiResponse(String str) {
        try {
            return GosTester.getTestApiResponse(AppContext.get(), str);
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "getTestApiResponse: ", e);
            return "{successful:false}";
        }
    }

    static String showTestActivity() {
        GosLog.d(LOG_TAG, "showTestActivity()");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        if (testActivityClass != null) {
            TestDataManager.setTestMode(true);
            Intent intent = new Intent(AppContext.get(), testActivityClass);
            intent.addFlags(268435456);
            intent.addFlags(131072);
            AppContext.get().startActivity(intent);
            try {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, true);
            } catch (JSONException e2) {
                GosLog.w(LOG_TAG, (Throwable) e2);
            }
        }
        return jSONObject.toString();
    }
}
