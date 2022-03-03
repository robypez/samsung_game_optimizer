package com.samsung.android.game.gos.service;

import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUpdater;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.controller.FeatureSetManager;
import com.samsung.android.game.gos.controller.GameAppReactor;
import com.samsung.android.game.gos.controller.SystemEventReactor;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.EventSubscriptionDbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.feature.NetworkEventInterface;
import com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature;
import com.samsung.android.game.gos.feature.dfs.FpsController;
import com.samsung.android.game.gos.feature.volumecontrol.VolumeControlFeature;
import com.samsung.android.game.gos.feature.vrr.VrrFeature;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.util.CharacterUtil;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.value.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameIntentService extends IntentService {
    private static final String LOG_TAG = "GameIntentService";
    private NetworkConnector mNc;
    private String preIntentPkgName = null;
    private int preIntentType = -1;

    public GameIntentService() {
        super(LOG_TAG);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        GosLog.v(LOG_TAG, "onHandleIntent. begin");
        if (intent == null) {
            GosLog.w(LOG_TAG, "onHandleIntent. intent is null");
            return;
        }
        Application application = AppContext.get();
        int intExtra = intent.getIntExtra("type", -1);
        String stringExtra = intent.getStringExtra(EventPublisher.EXTRA_KEY_PKG_NAME);
        GosLog.i(LOG_TAG, "onHandleIntent. GameIntentService. type: " + intExtra + ", pkgName: " + stringExtra);
        if (intExtra == 0) {
            if (this.preIntentType == 0) {
                GosLog.i(LOG_TAG, "onHandleIntent. not send pause intent from GMS.");
                onGameFocusOut(application, intent, this.preIntentPkgName);
            }
            onGameFocusIn(application, intent, stringExtra);
        } else if (intExtra == 1) {
            onGameFocusOut(application, intent, stringExtra);
        } else if (intExtra == 5) {
            SystemEventReactor.onResolutionChanged();
        } else if (intExtra == 6) {
            int intExtra2 = intent.getIntExtra("batteryLevel", -1);
            GosLog.i(LOG_TAG, "batteryLevel: " + intExtra2);
        } else if (intExtra != 9) {
            switch (intExtra) {
                case 14:
                    onVrrSettingChanged();
                    break;
                case 15:
                    if (this.mNc == null) {
                        this.mNc = new NetworkConnector(AppContext.get());
                    }
                    onPackageChanged(intent);
                    break;
                case 16:
                    onWifiConnected();
                    break;
                case 17:
                    SystemEventReactor.onUserRemoved(intent);
                    break;
                case 18:
                    onUserSwitched();
                    break;
                case 19:
                    onGosEnabled();
                    break;
                default:
                    GosLog.e(LOG_TAG, "unexpected intent type " + intExtra);
                    break;
            }
        } else {
            onMediaServerRebooted(SeGameManager.getInstance());
        }
        if (intExtra == 0 || intExtra == 1) {
            this.preIntentType = intExtra;
            this.preIntentPkgName = stringExtra;
        }
    }

    private void onGameFocusIn(Context context, Intent intent, String str) {
        PkgData pkgData;
        boolean booleanExtra = intent.getBooleanExtra("create", false);
        if (str != null && (pkgData = PackageDbHelper.getInstance().getPkgData(str)) != null && Constants.CategoryCode.GAME.equalsIgnoreCase(pkgData.getCategoryCode())) {
            String stringExtra = intent.getStringExtra("systemInfo");
            int intExtra = intent.getIntExtra(EventPublisher.EXTRA_KEY_USER_ID, SeUserHandleManager.getInstance().getMyUserId());
            GameAppReactor.getInstance().onFocusIn(pkgData, booleanExtra, stringExtra, intExtra);
            Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.GAME_RESUMED.toString());
            HashMap hashMap = new HashMap();
            hashMap.put(EventPublisher.EXTRA_KEY_IS_CREATE, BuildConfig.VERSION_NAME + booleanExtra);
            hashMap.put(EventPublisher.EXTRA_KEY_USER_ID, String.valueOf(intExtra));
            EventPublisher.publishEvent(context, subscriberListOfEvent, EventSubscription.EVENTS.GAME_RESUMED.toString(), str, (Map<String, String>) hashMap);
        }
    }

    private void onGameFocusOut(Context context, Intent intent, String str) {
        PkgData pkgData;
        if (str != null && (pkgData = PackageDbHelper.getInstance().getPkgData(str)) != null && Constants.CategoryCode.GAME.equalsIgnoreCase(pkgData.getCategoryCode())) {
            String stringExtra = intent.getStringExtra("systemInfo");
            int intExtra = intent.getIntExtra(EventPublisher.EXTRA_KEY_USER_ID, SeUserHandleManager.getInstance().getMyUserId());
            GameAppReactor.getInstance().onFocusOut(pkgData, stringExtra, intExtra);
            Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.GAME_PAUSED.toString());
            HashMap hashMap = new HashMap();
            hashMap.put(EventPublisher.EXTRA_KEY_USER_ID, String.valueOf(intExtra));
            EventPublisher.publishEvent(context, subscriberListOfEvent, EventSubscription.EVENTS.GAME_PAUSED.toString(), str, (Map<String, String>) hashMap);
        }
    }

    public static void onMediaServerRebooted(SeGameManager seGameManager) {
        if (FeatureHelper.isEnabledFlagByUser("volume_control")) {
            String foregroundApp = seGameManager.getForegroundApp();
            if (seGameManager.isForegroundGame() && foregroundApp != null) {
                GosLog.i(LOG_TAG, "MEDIA_SERVER_REBOOTED. fg game is " + foregroundApp);
                PkgData pkgData = PackageDbHelper.getInstance().getPkgData(foregroundApp);
                if (pkgData != null) {
                    VolumeControlFeature.getInstance().onFocusIn(pkgData, false);
                }
            }
        }
    }

    private void onVrrSettingChanged() {
        if (DbHelper.getInstance().getGlobalDao().getGmsVersion() < 100.006f) {
            new Thread(new Runnable() {
                public final void run() {
                    GameIntentService.this.lambda$onVrrSettingChanged$0$GameIntentService();
                }
            }).start();
        } else {
            doJobsForVrrSettingChanged();
        }
    }

    public /* synthetic */ void lambda$onVrrSettingChanged$0$GameIntentService() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doJobsForVrrSettingChanged();
    }

    private void doJobsForVrrSettingChanged() {
        GosLog.i(LOG_TAG, "VRR_SETTING_CHANGED is started.");
        VrrFeature.getInstance().sendVrrSettingChangedMessage();
        FpsController.getInstance().updateMaxFps();
        AutoControlFeature.getInstance().onSettingChangeExternal();
    }

    /* access modifiers changed from: package-private */
    public void onPkgInstalled(String str, int i) {
        PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str);
        if (pkgData == null) {
            SystemEventReactor.onPackageInstallStarted(AppContext.get(), this.mNc, str, i);
            pkgData = PackageDbHelper.getInstance().getPkgData(str);
            if (pkgData == null) {
                GosLog.w(LOG_TAG, "onHandleIntent(), pkgData == null. do nothing. packageName(" + str + ")");
                return;
            }
        }
        SystemEventReactor.onPackageInstalled(AppContext.get(), str, i, this.mNc);
        publishPkgInstalledEvent(pkgData.getCategoryCode(), str, i);
    }

    /* access modifiers changed from: package-private */
    public void publishPkgInstalledEvent(String str, String str2, int i) {
        if (str.equalsIgnoreCase(Constants.CategoryCode.GAME)) {
            Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.GAME_INSTALLED.toString());
            removeSubscriberByUserId(subscriberListOfEvent, i);
            HashMap hashMap = new HashMap();
            hashMap.put(EventPublisher.EXTRA_KEY_USER_ID, String.valueOf(i));
            EventPublisher.publishEvent((Context) AppContext.get(), subscriberListOfEvent, EventSubscription.EVENTS.GAME_INSTALLED.toString(), str2, (Map<String, String>) hashMap);
        } else if (isRootUserId(i)) {
            List<String> subscriberApps = EventSubscriptionDbHelper.getInstance().getSubscriberApps(str2);
            if (subscriberApps.size() > 0) {
                EventPublisher.publishEvent((Context) AppContext.get(), (Map<String, EventSubscription>) CharacterUtil.filterOut(EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.MONITORED_APP_INSTALLED.toString()), subscriberApps), EventSubscription.EVENTS.MONITORED_APP_INSTALLED.toString(), str2, (Map<String, String>) null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onPkgReplaced(String str) {
        SystemEventReactor.onPackageUpdated(str);
        PkgData pkgData = PackageDbHelper.getInstance().getPkgData(str);
        if (pkgData == null) {
            GosLog.w(LOG_TAG, "onHandleIntent(), pkgData == null. do nothing. packageName(" + str + ")");
            return;
        }
        publishPkgReplacedEvent(pkgData.getCategoryCode(), str);
    }

    /* access modifiers changed from: package-private */
    public void publishPkgReplacedEvent(String str, String str2) {
        if (str.equalsIgnoreCase(Constants.CategoryCode.GAME)) {
            EventPublisher.publishEvent((Context) AppContext.get(), EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.GAME_REPLACED.toString()), EventSubscription.EVENTS.GAME_REPLACED.toString(), str2, (Map<String, String>) null);
            return;
        }
        List<String> subscriberApps = EventSubscriptionDbHelper.getInstance().getSubscriberApps(str2);
        if (subscriberApps.size() > 0) {
            EventPublisher.publishEvent((Context) AppContext.get(), (Map<String, EventSubscription>) CharacterUtil.filterOut(EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.MONITORED_APP_REPLACED.toString()), subscriberApps), EventSubscription.EVENTS.MONITORED_APP_REPLACED.toString(), str2, (Map<String, String>) null);
        }
    }

    /* access modifiers changed from: package-private */
    public void publishPkgUninstalledEvent(String str, String str2, int i) {
        if (str.equalsIgnoreCase(Constants.CategoryCode.GAME)) {
            Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.GAME_UNINSTALLED.toString());
            removeSubscriberByUserId(subscriberListOfEvent, i);
            HashMap hashMap = new HashMap();
            hashMap.put(EventPublisher.EXTRA_KEY_USER_ID, String.valueOf(i));
            EventPublisher.publishEvent((Context) AppContext.get(), subscriberListOfEvent, EventSubscription.EVENTS.GAME_UNINSTALLED.toString(), str2, (Map<String, String>) hashMap);
        } else if (isRootUserId(i)) {
            List<String> subscriberApps = EventSubscriptionDbHelper.getInstance().getSubscriberApps(str2);
            if (subscriberApps.size() > 0) {
                EventPublisher.publishEvent((Context) AppContext.get(), (Map<String, EventSubscription>) CharacterUtil.filterOut(EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.MONITORED_APP_UNINSTALLED.toString()), subscriberApps), EventSubscription.EVENTS.MONITORED_APP_UNINSTALLED.toString(), str2, (Map<String, String>) null);
            }
        }
    }

    private void onPackageChanged(Intent intent) {
        Constants.PackageIntentType valueOf = Constants.PackageIntentType.valueOf(intent.getIntExtra("changeType", Constants.PackageIntentType.UNKNOWN.val()));
        String stringExtra = intent.getStringExtra("packageName");
        int intExtra = intent.getIntExtra(EventPublisher.EXTRA_KEY_USER_ID, SeUserHandleManager.getInstance().getMyUserId());
        boolean isPackageInstalledAsUser = PackageUtil.isPackageInstalledAsUser(AppContext.get(), stringExtra, intExtra);
        GosLog.i(LOG_TAG, "onHandleIntent(). extras: " + intent.getExtras().toString());
        GosLog.i(LOG_TAG, String.format(Locale.US, "onHandleIntent(). changeType(%s), packageName(%s), UserHandle(%s), userId(%d), Installed(%s)", new Object[]{valueOf, stringExtra, Integer.valueOf(SeUserHandleManager.getInstance().getCallingUserId()), Integer.valueOf(intExtra), Boolean.valueOf(isPackageInstalledAsUser)}));
        switch (AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType[valueOf.ordinal()]) {
            case 1:
                if (isPackageInstalledAsUser && stringExtra != null) {
                    onPkgInstalled(stringExtra, intExtra);
                    return;
                }
                return;
            case 2:
                if (isPackageInstalledAsUser && stringExtra != null) {
                    onPkgReplaced(stringExtra);
                    return;
                }
                return;
            case 3:
                SystemEventReactor.onPackageInstallStarted(AppContext.get(), this.mNc, stringExtra, intExtra);
                return;
            case 4:
                if (stringExtra != null) {
                    if (!isRootUserId(intExtra) || !isPackageInstalledAsUser) {
                        PkgData pkgData = PackageDbHelper.getInstance().getPkgData(stringExtra);
                        if (pkgData == null) {
                            publishPkgUninstalledEvent(Constants.CategoryCode.NON_GAME, stringExtra, intExtra);
                            GosLog.w(LOG_TAG, "onHandleIntent(), pkgData == null. do nothing. packageName(" + stringExtra + ")");
                            return;
                        }
                        publishPkgUninstalledEvent(pkgData.getCategoryCode(), stringExtra, intExtra);
                        SystemEventReactor.onPackageRemoved(AppContext.get(), stringExtra, intExtra, this.mNc);
                        return;
                    }
                    return;
                }
                return;
            case 5:
                if (isPackageInstalledAsUser) {
                    SystemEventReactor.onPackageEnabled(AppContext.get(), stringExtra, intExtra, this.mNc);
                    return;
                }
                return;
            case 6:
                if (isPackageInstalledAsUser) {
                    SystemEventReactor.onPackageDisabled(stringExtra);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* renamed from: com.samsung.android.game.gos.service.GameIntentService$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.samsung.android.game.gos.value.Constants$PackageIntentType[] r0 = com.samsung.android.game.gos.value.Constants.PackageIntentType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType = r0
                com.samsung.android.game.gos.value.Constants$PackageIntentType r1 = com.samsung.android.game.gos.value.Constants.PackageIntentType.INSTALLED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.value.Constants$PackageIntentType r1 = com.samsung.android.game.gos.value.Constants.PackageIntentType.REPLACED     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.value.Constants$PackageIntentType r1 = com.samsung.android.game.gos.value.Constants.PackageIntentType.INSTALL_STARTED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.samsung.android.game.gos.value.Constants$PackageIntentType r1 = com.samsung.android.game.gos.value.Constants.PackageIntentType.REMOVED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType     // Catch:{ NoSuchFieldError -> 0x003e }
                com.samsung.android.game.gos.value.Constants$PackageIntentType r1 = com.samsung.android.game.gos.value.Constants.PackageIntentType.ENABLED     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$Constants$PackageIntentType     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.samsung.android.game.gos.value.Constants$PackageIntentType r1 = com.samsung.android.game.gos.value.Constants.PackageIntentType.DISABLED     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.service.GameIntentService.AnonymousClass1.<clinit>():void");
        }
    }

    private void onWifiConnected() {
        if (this.mNc == null) {
            this.mNc = new NetworkConnector(AppContext.get());
        }
        AlarmController.onUpdateAlarm(AppContext.get(), this.mNc, Constants.IntentType.WIFI_CONNECTED);
        for (NetworkEventInterface next : FeatureSetManager.getNetworkEventFeatureMap(AppContext.get()).values()) {
            if (FeatureHelper.isEnabledFlagByUser(next.getName())) {
                next.onWifiConnected();
            }
        }
    }

    private void onUserSwitched() {
        if (this.mNc == null) {
            this.mNc = new NetworkConnector(AppContext.get());
        }
        DataUpdater.updateGlobalAndPkgData(AppContext.get(), DataUpdater.PkgUpdateType.ALL, false, this.mNc, Constants.CallTrigger.USER_SWITCHED);
        AlarmController.setUpdateAlarm(AppContext.get(), Constants.AlarmIntentType.UPDATE_CHECK);
    }

    private void onGosEnabled() {
        if (this.mNc == null) {
            this.mNc = new NetworkConnector(AppContext.get());
        }
        DataUpdater.updateGlobalAndPkgData(AppContext.get(), DataUpdater.PkgUpdateType.ALL, false, this.mNc, Constants.CallTrigger.GOS_ENABLED);
        AlarmController.setUpdateAlarm(AppContext.get(), Constants.AlarmIntentType.UPDATE_CHECK);
    }

    /* access modifiers changed from: package-private */
    public void setNetworkConnector(NetworkConnector networkConnector) {
        this.mNc = networkConnector;
    }

    /* access modifiers changed from: package-private */
    public void removeSubscriberByUserId(Map<String, EventSubscription> map, int i) {
        if (!isRootUserId(i)) {
            map.remove(Constants.PACKAGE_NAME_GAME_HOME);
            map.remove(Constants.PACKAGE_NAME_SAMSUNG_APPS);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isRootUserId(int i) {
        return i == SeUserHandleManager.getInstance().getMyUserId();
    }
}
