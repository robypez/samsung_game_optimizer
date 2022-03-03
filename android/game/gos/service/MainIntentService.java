package com.samsung.android.game.gos.service;

import android.app.IntentService;
import android.content.Intent;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUpdater;
import com.samsung.android.game.gos.controller.FeatureSetManager;
import com.samsung.android.game.gos.controller.SystemEventReactor;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import java.util.List;
import java.util.Map;

public class MainIntentService extends IntentService {
    private static final String LOG_TAG = "MainIntentService";
    private NetworkConnector mNc;

    public MainIntentService() {
        super(LOG_TAG);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        if (intent == null) {
            GosLog.w(LOG_TAG, "onHandleIntent. intent is null");
            return;
        }
        int intExtra = intent.getIntExtra("type", Constants.IntentType.UNKNOWN.val());
        Constants.IntentType valueOf = Constants.IntentType.valueOf(intExtra);
        GosLog.i(LOG_TAG, "onHandleIntent(). type(" + valueOf + ") from " + intExtra);
        if (valueOf == Constants.IntentType.BOOT_COMPLETED) {
            if (this.mNc == null) {
                this.mNc = new NetworkConnector(AppContext.get());
            }
            SystemEventReactor.onBootCompleted(AppContext.get(), this.mNc);
            AlarmController.setUpdateAlarm(AppContext.get(), Constants.AlarmIntentType.UPDATE_CHECK);
        } else if (valueOf == Constants.IntentType.MY_PACKAGE_REPLACED) {
            SystemEventReactor.onMyPackageReplaced();
            AlarmController.setUpdateAlarm(AppContext.get(), Constants.AlarmIntentType.UPDATE_CHECK);
        } else if (valueOf == Constants.IntentType.ON_ALARM || valueOf == Constants.IntentType.ON_SERVER_CONNECTION_FAIL_ALARM) {
            GosLog.i(LOG_TAG, "received UPDATE_ALARM type alarm: " + valueOf);
            if (this.mNc == null) {
                this.mNc = new NetworkConnector(AppContext.get());
            }
            AlarmController.onUpdateAlarm(AppContext.get(), this.mNc, valueOf);
        } else if (valueOf == Constants.IntentType.DESKTOP_MODE_CHANGED) {
            SystemEventReactor.onDesktopModeChanged();
        } else if (valueOf == Constants.IntentType.MAKE_DATA_READY) {
            onMakeDataReady();
        } else if (valueOf == Constants.IntentType.INIT_GOS) {
            onInitGos();
        } else if (valueOf == Constants.IntentType.TEST_GPP_TEMPERATURE_REACTOR) {
            onTestGppAutoControl(intent);
        } else {
            GosLog.e(LOG_TAG, "unexpected intent type(" + valueOf + ")");
        }
    }

    private void onMakeDataReady() {
        if (!DbHelper.getInstance().getGlobalDao().isDataReady()) {
            if (this.mNc == null) {
                this.mNc = new NetworkConnector(AppContext.get());
            }
            DataUpdater.updateGlobalAndPkgData(AppContext.get(), DataUpdater.PkgUpdateType.UNIDENTIFIED_AND_UNKNOWN, false, this.mNc, Constants.CallTrigger.MAKE_DATA_READY);
            AlarmController.setUpdateAlarm(AppContext.get(), Constants.AlarmIntentType.UPDATE_CHECK);
            DbHelper.getInstance().getGlobalDao().setDataReady(new Global.IdAndDataReady(true));
            return;
        }
        GosLog.i(LOG_TAG, "already data ready.");
    }

    private void onInitGos() {
        if (!SeGameManager.getInstance().isForegroundGame()) {
            PreferenceHelper preferenceHelper = new PreferenceHelper();
            String value = preferenceHelper.getValue(PreferenceHelper.PREF_FOCUSED_IN_FEATURE_NAMES, (String) null);
            preferenceHelper.remove(PreferenceHelper.PREF_FOCUSED_IN_FEATURE_NAMES);
            List<String> csvToStringList = TypeConverter.csvToStringList(value);
            GosLog.d(LOG_TAG, "onInitGos(), focusedInFeatureNameList=" + csvToStringList);
            Map<String, RuntimeInterface> runtimeFeatureMap = FeatureSetManager.getRuntimeFeatureMap(AppContext.get());
            if (csvToStringList != null) {
                for (String next : csvToStringList) {
                    RuntimeInterface runtimeInterface = runtimeFeatureMap.get(next);
                    if (runtimeInterface != null && FeatureHelper.isAvailable(next)) {
                        runtimeInterface.restoreDefault((PkgData) null);
                    }
                }
            }
        }
        String prop = SeSysProp.getProp("ro.board.platform");
        String prop2 = SeSysProp.getProp("ro.product.board");
        GosLog.i(LOG_TAG, " ChipSet : " + prop + " Variant : " + prop2);
    }

    private void onTestGppAutoControl(Intent intent) {
        if (intent.hasExtra("reset")) {
            AutoControlFeature.resetTestModes(true);
        } else if (intent.hasExtra("level")) {
            AutoControlFeature.applyTestLevel(intent.getIntExtra("level", AutoControlFeature.Level.L0.ordinal()));
        } else if (intent.hasExtra("levelTemperatures")) {
            AutoControlFeature.applyTestTemperatures(intent.getStringExtra("levelTemperatures"));
        }
    }

    public void onDestroy() {
        GosLog.v(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    /* access modifiers changed from: package-private */
    public void setNetworkConnector(NetworkConnector networkConnector) {
        this.mNc = networkConnector;
    }
}
