package com.samsung.android.game.gos.endpoint;

import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUpdater;
import com.samsung.android.game.gos.data.dao.SettingsAccessiblePackageDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.SettingsAccessiblePackage;
import com.samsung.android.game.gos.feature.dfs.FpsController;
import com.samsung.android.game.gos.util.BadHardcodedOperation;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

class GosCommand {
    private static final String LOG_TAG = GosCommand.class.getSimpleName();

    GosCommand() {
    }

    /* access modifiers changed from: package-private */
    public String setFeatureAccessibility(String str, String str2) {
        String str3 = LOG_TAG;
        GosLog.i(str3, "setFeatureAccessibility(), jsonParam: " + str);
        JSONObject jSONObject = new JSONObject();
        ResponseBuilder responseBuilder = new ResponseBuilder();
        ResponseBuilder responseBuilder2 = new ResponseBuilder();
        try {
            JSONObject jSONObject2 = new JSONObject(str);
            if (str2 == null) {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false).put(GosInterface.KeyName.COMMENT, "callerPkgName is null.");
                return jSONObject.toString();
            }
            if (jSONObject2.has(GosInterface.KeyName.SETTABLE_FEATURES)) {
                setSettableFeatures(str2, jSONObject2.getString(GosInterface.KeyName.SETTABLE_FEATURES));
                responseBuilder.addItem(GosInterface.KeyName.SETTABLE_FEATURES);
                responseBuilder2.addItem("settable_features:" + GlobalDbHelper.getInstance().getSettableFeatures(str2));
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL_ITEMS, responseBuilder).put(GosInterface.KeyName.COMMENT, responseBuilder2);
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    private void setSettableFeatures(String str, String str2) {
        SettingsAccessiblePackageDao settingsAccessiblePackageDao = DbHelper.getInstance().getSettingsAccessiblePackageDao();
        List<String> features = settingsAccessiblePackageDao.getFeatures(str);
        List<String> csvToStringList = str2 != null ? TypeConverter.csvToStringList(str2) : null;
        ArrayList arrayList = new ArrayList();
        if (BadHardcodedOperation.needsToBlockGfiSettingByGameBooster(str)) {
            csvToStringList.remove(Constants.V4FeatureFlag.GFI);
        }
        settingsAccessiblePackageDao.delete(str);
        if (csvToStringList != null) {
            for (String trim : csvToStringList) {
                String trim2 = trim.trim();
                if (Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES.contains(trim2)) {
                    arrayList.add(trim2);
                    settingsAccessiblePackageDao.insertOrUpdate(new SettingsAccessiblePackage(str, trim2));
                }
            }
        }
        ArrayList arrayList2 = new ArrayList();
        if (features != null) {
            ArrayList arrayList3 = new ArrayList();
            for (String next : features) {
                boolean z = false;
                if (arrayList.contains(next)) {
                    z = true;
                }
                if (!z) {
                    long featureSettersCount = GlobalDbHelper.getInstance().getFeatureSettersCount(next);
                    arrayList3.add(next + "(" + featureSettersCount + ")");
                    if (featureSettersCount == 0) {
                        arrayList2.add(next);
                    }
                }
            }
            if (!arrayList3.isEmpty()) {
                String str3 = LOG_TAG;
                GosLog.d(str3, "OFFed features: " + arrayList3.toString());
            }
            if (arrayList2.size() > 0) {
                String str4 = LOG_TAG;
                GosLog.d(str4, "setSettableFeatures() restoreList: " + arrayList2);
                new Thread(new Runnable(arrayList2) {
                    public final /* synthetic */ ArrayList f$0;

                    {
                        this.f$0 = r1;
                    }

                    public final void run() {
                        DataUpdater.restoreFeatureSettingsToDefault(AppContext.get(), (String[]) this.f$0.toArray(new String[this.f$0.size()]));
                    }
                }).start();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String setFpsValue(String str) {
        String str2;
        String str3 = LOG_TAG;
        GosLog.i(str3, "setFpsValue(). jsonParam: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            int i = jSONObject.getInt(GosInterface.KeyName.VAL);
            String string = jSONObject.getString(GosInterface.KeyName.TAG);
            String optString = jSONObject.optString("type", FpsController.TYPE_FIXED);
            GosLog.d(LOG_TAG, String.format(Locale.US, "setFpsValue(). VAL: %d, TAG: %s, TYPE: %s", new Object[]{Integer.valueOf(i), string, optString}));
            if (optString.equals(FpsController.TYPE_SCALE)) {
                FpsController.getInstance().requestFpsScaleValue(i, string);
            } else {
                FpsController.getInstance().requestFpsFixedValue(i, string);
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(GosInterface.KeyName.SUCCESSFUL, true);
            str2 = jSONObject2.toString();
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            str2 = "{\"successful\":false}";
        }
        String str4 = LOG_TAG;
        GosLog.i(str4, "setFpsValue(). response: " + str2);
        return str2;
    }

    static class ResponseBuilder {
        private StringBuilder mStringBuilder = new StringBuilder();

        ResponseBuilder() {
        }

        /* access modifiers changed from: package-private */
        public void addItem(String str) {
            if (this.mStringBuilder.length() > 0) {
                this.mStringBuilder.append(",");
            }
            this.mStringBuilder.append(str);
        }

        public String toString() {
            return this.mStringBuilder.toString();
        }
    }
}
