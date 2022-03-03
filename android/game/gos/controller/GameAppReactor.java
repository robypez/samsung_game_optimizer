package com.samsung.android.game.gos.controller;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.dfs.DfsFeature;
import com.samsung.android.game.gos.feature.dfs.FpsController;
import com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper;
import com.samsung.android.game.gos.selibrary.SeServiceManager;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.BadHardcodedConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameAppReactor {
    private static final String LOG_TAG = "GameAppReactor";
    private static final int SURFACE_GAME_FOCUS_IN = 1;
    private static final int SURFACE_GAME_FOCUS_OUT = 0;
    private static final int TRANSACT_CODE_NOTIFY_GAME_FOCUS_STATE = 1030;
    private static final int TRANSACT_CODE_SET_GAME_FOCUS_IN_OUT = 1035;
    private Map<String, RuntimeInterface> mRuntimeFeatures;

    private GameAppReactor(Context context) {
        this.mRuntimeFeatures = FeatureSetManager.getRuntimeFeatureMap(context);
    }

    public static GameAppReactor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    static class SingletonHolder {
        static GameAppReactor INSTANCE = new GameAppReactor(AppContext.get());

        private SingletonHolder() {
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z, String str, int i) {
        if (pkgData == null) {
            GosLog.e(LOG_TAG, "gameData is null. ");
            return;
        }
        GosLog.i(LOG_TAG, "onFocusIn. [" + pkgData.getPackageName() + "], isCreate: " + z + ", userId: " + i + ", myUserId: " + SeUserHandleManager.getInstance().getMyUserId());
        if (AppVariable.isUnitTest() || DbHelper.getInstance().getGlobalDao().isRegisteredDevice()) {
            GosLog.v(LOG_TAG, "onFocusIn-systemInfo: " + str);
            StatusCollector instance = StatusCollector.getInstance();
            if (instance != null) {
                instance.startCollecting(pkgData, str);
            }
            Map<String, Boolean> actualFeatureFlagMap = pkgData.getActualFeatureFlagMap();
            if (z && PlatformUtil.isDebugBinary()) {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (Map.Entry next : actualFeatureFlagMap.entrySet()) {
                    Boolean bool = (Boolean) next.getValue();
                    if (bool == null || !bool.booleanValue()) {
                        arrayList2.add(next.getKey());
                    } else {
                        arrayList.add(next.getKey());
                    }
                }
                GosLog.i(LOG_TAG, "onFocusIn(): \nactual-ON: " + TypeConverter.stringsToCsv((Iterable<String>) arrayList).replace(",", ", ") + "\nactual-OFF: " + TypeConverter.stringsToCsv((Iterable<String>) arrayList2).replace(",", ", "));
            }
            if (this.mRuntimeFeatures != null) {
                ArrayList arrayList3 = new ArrayList();
                for (Map.Entry next2 : this.mRuntimeFeatures.entrySet()) {
                    Boolean bool2 = actualFeatureFlagMap.get(next2.getKey());
                    if (bool2 != null && bool2.booleanValue()) {
                        ((RuntimeInterface) next2.getValue()).onFocusIn(pkgData, z);
                        arrayList3.add(next2.getKey());
                    }
                }
                new PreferenceHelper().put(PreferenceHelper.PREF_FOCUSED_IN_FEATURE_NAMES, TypeConverter.stringsToCsv((Iterable<String>) arrayList3));
            }
            if (!AppVariable.isUnitTest() && ((long) Build.VERSION.SEM_PLATFORM_INT) >= BadHardcodedConstant.SEP_VERSION_13_1) {
                notifyGameFocusToSurface(SeServiceManager.getInstance().getService("SurfaceFlinger"), 1);
                return;
            }
            return;
        }
        GosLog.w(LOG_TAG, "onFocusIn. Canceled because this is not registered device.");
    }

    /* access modifiers changed from: package-private */
    public void notifyGameFocusToSurface(IBinder iBinder, int i) {
        Parcel obtain;
        if (iBinder != null && (obtain = Parcel.obtain()) != null) {
            obtain.writeInterfaceToken(GfiSurfaceFlingerHelper.SURFACEFLINGER_INTERFACE_TOKEN);
            obtain.writeInt(TRANSACT_CODE_NOTIFY_GAME_FOCUS_STATE);
            obtain.writeInt(i);
            try {
                boolean transact = iBinder.transact(TRANSACT_CODE_SET_GAME_FOCUS_IN_OUT, obtain, (Parcel) null, 0);
                GosLog.i(LOG_TAG, "notifyGameFocusToSurface focus : " + i + " return : " + transact);
            } catch (Exception e) {
                GosLog.w(LOG_TAG, "notifyGameFocusToSurface:" + e);
            }
            obtain.recycle();
        }
    }

    public void onFocusOut(PkgData pkgData, String str, int i) {
        if (pkgData == null) {
            GosLog.e(LOG_TAG, "gameData is null. ");
            return;
        }
        GosLog.i(LOG_TAG, "onFocusOut. : " + pkgData.getPackageName() + ", userId: " + i + ", myUserId: " + SeUserHandleManager.getInstance().getMyUserId());
        if (AppVariable.isUnitTest() || DbHelper.getInstance().getGlobalDao().isRegisteredDevice()) {
            PreferenceHelper preferenceHelper = new PreferenceHelper();
            preferenceHelper.put(PreferenceHelper.PREF_LAST_PAUSED_GAME_PKG_NAME, pkgData.getPackageName());
            preferenceHelper.put(PreferenceHelper.PREF_LAST_PAUSED_GAME_TIME_STAMP, System.currentTimeMillis());
            Map<String, Boolean> actualFeatureFlagMap = pkgData.getActualFeatureFlagMap();
            DfsFeature.setDefaultFps();
            List<String> csvToStringList = TypeConverter.csvToStringList(new PreferenceHelper().getValue(PreferenceHelper.PREF_FOCUSED_IN_FEATURE_NAMES, (String) null));
            if (!(this.mRuntimeFeatures == null || csvToStringList == null)) {
                for (String next : csvToStringList) {
                    Boolean bool = actualFeatureFlagMap.get(next);
                    RuntimeInterface runtimeInterface = this.mRuntimeFeatures.get(next);
                    if (!(bool == null || runtimeInterface == null)) {
                        if (bool.booleanValue()) {
                            runtimeInterface.onFocusOut(pkgData);
                        } else if (FeatureHelper.isAvailable(next)) {
                            runtimeInterface.restoreDefault(pkgData);
                        }
                    }
                }
            }
            new PreferenceHelper().remove(PreferenceHelper.PREF_FOCUSED_IN_FEATURE_NAMES);
            GosLog.v(LOG_TAG, "onFocusOut-systemInfo:" + str);
            StatusCollector instance = StatusCollector.getInstance();
            if (instance != null) {
                instance.stopCollecting(str);
            }
            FpsController.getInstance().resetFps();
            if (!AppVariable.isUnitTest() && ((long) Build.VERSION.SEM_PLATFORM_INT) >= BadHardcodedConstant.SEP_VERSION_13_1) {
                notifyGameFocusToSurface(SeServiceManager.getInstance().getService("SurfaceFlinger"), 0);
                return;
            }
            return;
        }
        GosLog.w(LOG_TAG, "onFocusOut. Canceled because this is not registered device.");
    }
}
