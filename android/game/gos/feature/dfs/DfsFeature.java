package com.samsung.android.game.gos.feature.dfs;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeServiceManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.Constants;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Locale;

public class DfsFeature implements RuntimeInterface {
    private static final int DFS_START_DELAY = 500;
    private static final String LOG_TAG = "DfsFeature";
    private static final int MSG_DFS_END = 20001;
    private static final int MSG_DFS_START = 20000;
    private static final int TRANSACT_CODE_SET_DFS_API_25 = 1122;
    private Handler mHandler;

    public String getName() {
        return Constants.V4FeatureFlag.DFS;
    }

    private DfsFeature() {
        this.mHandler = new InnerClassHandler(this);
    }

    public static DfsFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        boolean isUsingUserValue = FeatureHelper.isUsingUserValue(Constants.V4FeatureFlag.DFS);
        boolean isUsingPkgValue = FeatureHelper.isUsingPkgValue(Constants.V4FeatureFlag.DFS);
        int round = Math.round(getDfsValueByMode(getActualDfsMode(pkgData, isUsingUserValue, isUsingPkgValue), pkgData, isUsingUserValue, isUsingPkgValue));
        GosLog.i(LOG_TAG, "onFocusIn(), newFps: " + round);
        if (((float) round) != 120.0f) {
            setFramePerSecond(round);
        }
    }

    public void onFocusOut(PkgData pkgData) {
        this.mHandler.removeMessages(MSG_DFS_START);
    }

    public void restoreDefault(PkgData pkgData) {
        GosLog.d(LOG_TAG, "restoreDefault()");
        FpsController.getInstance().resetFps();
    }

    public boolean isAvailableForSystemHelper() {
        return _isAvailable(SeServiceManager.getInstance().getService("SurfaceFlinger"));
    }

    /* access modifiers changed from: package-private */
    public boolean _isAvailable(IBinder iBinder) {
        Parcel obtain;
        boolean z = false;
        if (!(iBinder == null || (obtain = Parcel.obtain()) == null)) {
            obtain.writeInterfaceToken(GfiSurfaceFlingerHelper.SURFACEFLINGER_INTERFACE_TOKEN);
            obtain.writeInt(-1);
            try {
                z = iBinder.transact(TRANSACT_CODE_SET_DFS_API_25, obtain, (Parcel) null, 0);
            } catch (RemoteException | SecurityException unused) {
                GosLog.e(LOG_TAG, "dfs failed");
            }
            obtain.recycle();
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public int getCurrentDfsValue() {
        return SeGameManager.getInstance().getTargetFrameRate();
    }

    static void setFramePerSecond(int i) {
        Handler handler = getInstance().mHandler;
        Message obtainMessage = handler.obtainMessage(MSG_DFS_START);
        obtainMessage.arg1 = i;
        handler.sendMessageDelayed(obtainMessage, 500);
    }

    public static void setDefaultFps() {
        getInstance().mHandler.sendEmptyMessage(MSG_DFS_END);
    }

    /* access modifiers changed from: private */
    public void handleMessage(Message message) {
        GosLog.v(LOG_TAG, "handleMessage");
        int i = message.what;
        if (i == MSG_DFS_START) {
            GosLog.d(LOG_TAG, "MSG_DFS_START");
            FpsController.getInstance().requestFpsFixedValue(message.arg1, "GOS");
        } else if (i == MSG_DFS_END) {
            GosLog.d(LOG_TAG, "MSG_DFS_END");
            getInstance().restoreDefault((PkgData) null);
        }
    }

    private static class InnerClassHandler extends Handler {
        private final WeakReference<DfsFeature> mDfsFeature;

        InnerClassHandler(DfsFeature dfsFeature) {
            super(Looper.getMainLooper());
            this.mDfsFeature = new WeakReference<>(dfsFeature);
        }

        public void handleMessage(Message message) {
            DfsFeature dfsFeature = (DfsFeature) this.mDfsFeature.get();
            if (dfsFeature != null) {
                dfsFeature.handleMessage(message);
            }
        }
    }

    public static float[] getMergedEachModeDfs(Package packageR) {
        float[] eachModeDfs = Global.DefaultGlobalData.getEachModeDfs();
        float[] copyOf = Arrays.copyOf(eachModeDfs, eachModeDfs.length);
        mergeDfsList(copyOf, GlobalDbHelper.getInstance().getEachModeDfs());
        mergeDfsList(copyOf, TypeConverter.csvToFloats(packageR.getEachModeDfs()));
        GosLog.d(LOG_TAG, "getMergedEachModeDfs()-merged: " + Arrays.toString(copyOf) + ", " + packageR.pkgName);
        return copyOf;
    }

    static void mergeDfsList(float[] fArr, float[] fArr2) {
        if (fArr != null && fArr2 != null) {
            int i = 0;
            while (i < fArr2.length && i < fArr.length) {
                if (fArr2[i] >= 1.0f && fArr2[i] <= 120.0f) {
                    fArr[i] = fArr2[i];
                }
                i++;
            }
        }
    }

    public static float getDefaultDfs(Package packageR) {
        float[] mergedEachModeDfs = getMergedEachModeDfs(packageR);
        if (mergedEachModeDfs.length > 1) {
            return mergedEachModeDfs[1];
        }
        return 120.0f;
    }

    public static int getActualDfsMode(PkgData pkgData, boolean z, boolean z2) {
        if (!z) {
            return 1;
        }
        if (!z2) {
            return DbHelper.getInstance().getGlobalDao().getDfsMode();
        }
        return pkgData.getPkg().getCustomDfsMode();
    }

    public static float getDfsValueByMode(int i, PkgData pkgData, boolean z, boolean z2) {
        int validMode = ValidationUtil.getValidMode(i);
        float defaultDfs = getDefaultDfs(pkgData.getPkg());
        if (validMode != 4) {
            float[] mergedEachModeDfs = getMergedEachModeDfs(pkgData.getPkg());
            if (mergedEachModeDfs.length > validMode) {
                return mergedEachModeDfs[validMode];
            }
            GosLog.e(LOG_TAG, String.format(Locale.US, "getDfsValueByMode(): there is no value for mode %d", new Object[]{Integer.valueOf(validMode)}));
            return defaultDfs;
        } else if (!z) {
            return defaultDfs;
        } else {
            if (!z2) {
                return DbHelper.getInstance().getGlobalDao().getCustomDfs();
            }
            return pkgData.getPkg().getCustomDfs();
        }
    }

    private static class SingletonHolder {
        public static DfsFeature INSTANCE = new DfsFeature();

        private SingletonHolder() {
        }
    }
}
