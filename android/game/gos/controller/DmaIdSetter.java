package com.samsung.android.game.gos.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.util.GosLog;
import com.sec.android.diagmonagent.sa.IDMAInterface;

public class DmaIdSetter {
    private static final String DMA_CLASS_NAME = "com.sec.android.diagmonagent.sa.receiver.SALogReceiverService";
    static final String DMA_PKG_NAME = "com.sec.android.diagmonagent";
    static final int DMA_SUPPORT_VERSION = 540000000;
    private static final String LOG_TAG = "DmaIdSetter";
    private DmaIdServiceConnection serviceConnection;

    static class DmaIdServiceConnection implements ServiceConnection {
        private Context context;
        IDMAInterface mService;

        DmaIdServiceConnection() {
        }

        public void setContext(Context context2) {
            this.context = context2;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.mService = IDMAInterface.Stub.asInterface(iBinder);
            GosLog.d(DmaIdSetter.LOG_TAG, "DMA service connected");
            IDMAInterface iDMAInterface = this.mService;
            if (iDMAInterface != null) {
                try {
                    new Thread(new Runnable(iDMAInterface.checkToken()) {
                        public final /* synthetic */ String f$0;

                        {
                            this.f$0 = r1;
                        }

                        public final void run() {
                            DbHelper.getInstance().getGlobalDao().setDmaId(new Global.IdAndDmaId(this.f$0));
                        }
                    }).start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Context context2 = this.context;
            if (context2 != null) {
                context2.unbindService(this);
                this.context = null;
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            this.mService = null;
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DmaIdSetter INSTANCE = new DmaIdSetter();

        private SingletonHolder() {
        }
    }

    private DmaIdSetter() {
        this.serviceConnection = new DmaIdServiceConnection();
    }

    public static DmaIdSetter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean bindToDmaService(PackageManager packageManager) {
        try {
            if (packageManager.getPackageInfo(DMA_PKG_NAME, 0).getLongVersionCode() >= 540000000) {
                Intent intent = new Intent();
                intent.setClassName(DMA_PKG_NAME, DMA_CLASS_NAME);
                this.serviceConnection.setContext(AppContext.get());
                AppContext.get().bindService(intent, this.serviceConnection, 1);
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
