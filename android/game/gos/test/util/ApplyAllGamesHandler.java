package com.samsung.android.game.gos.test.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.samsung.android.game.gos.data.DataManager;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;

class ApplyAllGamesHandler extends HandlerThread {
    private static final int APPLY_ALL_GAMES_HANDLER_ID = 1000;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = ApplyAllGamesHandler.class.getSimpleName();
    private Handler mHandler;

    public ApplyAllGamesHandler() {
        super(ApplyAllGamesHandler.class.getSimpleName());
    }

    public static ApplyAllGamesHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final ApplyAllGamesHandler INSTANCE = new ApplyAllGamesHandler();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public void onLooperPrepared() {
        super.onLooperPrepared();
        this.mHandler = new Handler(getLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                String access$100 = ApplyAllGamesHandler.LOG_TAG;
                GosTestLog.d(access$100, "ApplyAllGamesHandler - handleMessage: " + message);
                GmsGlobalPackageDataSetter.getInstance().applyAllGames(false);
                DataManager.getInstance().notifyModeChanged(DbHelper.getInstance().getGlobalDao().getResolutionMode(), GlobalDbHelper.getInstance().isUsingCustomValue(Constants.V4FeatureFlag.RESOLUTION));
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
                arrayList.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.MANAGED_APP));
                SeActivityManager.getInstance().forceStopPackages(arrayList);
            }
        };
    }

    public void sendApplyGlobalData() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(1000);
            this.mHandler.sendEmptyMessage(1000);
        }
    }
}
