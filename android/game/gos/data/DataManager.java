package com.samsung.android.game.gos.data;

import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
    private static final String LOG_TAG = "DataManager";
    private Map<String, IResolutionModeChangedListener> mModeChangedListenerMap;
    private int mResolutionMode;

    public static DataManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataManager INSTANCE = new DataManager();

        private SingletonHolder() {
        }
    }

    private DataManager() {
        this.mResolutionMode = 1;
        this.mModeChangedListenerMap = new ConcurrentHashMap();
        if (DbHelper.getInstance().getGlobalDao().isInitialized()) {
            this.mResolutionMode = DbHelper.getInstance().getGlobalDao().getResolutionMode();
        }
        GosLog.d(LOG_TAG, "Create a DataManager");
    }

    public int getResolutionMode() {
        return this.mResolutionMode;
    }

    public void registerModeChangedListener(String str, IResolutionModeChangedListener iResolutionModeChangedListener) {
        this.mModeChangedListenerMap.put(str, iResolutionModeChangedListener);
    }

    public void notifyModeChanged(int i, boolean z) {
        for (IResolutionModeChangedListener onResolutionModeChanged : this.mModeChangedListenerMap.values()) {
            onResolutionModeChanged.onResolutionModeChanged(i, z);
        }
    }

    public boolean setModes(int i, int i2, boolean z, boolean z2, boolean z3) {
        float f = (float) i;
        boolean z4 = false;
        if (f >= 0.0f && f <= 3.0f) {
            float f2 = (float) i2;
            if (f2 >= 0.0f && f2 <= 3.0f) {
                GosLog.d(LOG_TAG, "setModes(), isDfsCustomMode: " + z2 + ", isResCustomMode: " + z);
                DbHelper.getInstance().getGlobalDao().setDfsMode(new Global.IdAndDfsMode(i2));
                GlobalDbHelper.getInstance().setUsingCustomValue(Constants.V4FeatureFlag.DFS, z2);
                boolean z5 = i == this.mResolutionMode;
                if (GlobalDbHelper.getInstance().isUsingCustomValue(Constants.V4FeatureFlag.RESOLUTION) == z) {
                    z4 = true;
                }
                if (z5 || z4 || z3) {
                    DbHelper.getInstance().getGlobalDao().setResolutionMode(new Global.IdAndResolutionMode(i));
                    GlobalDbHelper.getInstance().setUsingCustomValue(Constants.V4FeatureFlag.RESOLUTION, z);
                    this.mResolutionMode = i;
                    notifyModeChanged(i, z);
                    return true;
                }
                GosLog.w(LOG_TAG, "setModes(), (!resolutionModeChanged && !customModeChanged && !isForced). do noting");
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public Map<String, IResolutionModeChangedListener> getModeChangedListenerMap() {
        return this.mModeChangedListenerMap;
    }
}
