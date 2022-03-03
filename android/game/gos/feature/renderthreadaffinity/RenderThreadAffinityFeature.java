package com.samsung.android.game.gos.feature.renderthreadaffinity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.RinglogConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class RenderThreadAffinityFeature implements RuntimeInterface {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = RenderThreadAffinityFeature.class.getSimpleName();
    private static final int MSG_SET_AFFINITY = 10;
    private static final int MSG_UNSET_AFFINITY = 11;
    private static final int SET_AFFINITY_DELAY = 10000;
    private Handler mHandler;

    public String getName() {
        return Constants.V4FeatureFlag.RENDER_THREAD_AFFINITY;
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    public static RenderThreadAffinityFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static RenderThreadAffinityFeature INSTANCE = new RenderThreadAffinityFeature();

        private SingletonHolder() {
        }
    }

    private RenderThreadAffinityFeature() {
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                int pid = SeActivityManager.getInstance().getPid(AppContext.get(), (String) message.obj);
                if (pid == -1) {
                    GosLog.e(RenderThreadAffinityFeature.LOG_TAG, "failed to find pid");
                    return;
                }
                int i = message.what;
                if (i == 10) {
                    RenderThreadAffinityFeature.this.applyRTA(pid, true);
                } else if (i == 11) {
                    RenderThreadAffinityFeature.this.applyRTA(pid, false);
                }
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void applyRTA(int i, boolean z) {
        String str = z ? ManagerInterface.Command.SET_RENDER_THREAD_AFFINITY : ManagerInterface.Command.UNSET_RENDER_THREAD_AFFINITY;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ManagerInterface.KeyName.PACKAGE_PID, i);
            SeGameManager.getInstance().requestWithJson(str, jSONObject.toString());
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        String str = LOG_TAG;
        GosLog.v(str, "onResume(). " + pkgData.getPackageName());
        this.mHandler.removeMessages(10);
        Message obtainMessage = this.mHandler.obtainMessage(10);
        obtainMessage.obj = pkgData.getPackageName();
        this.mHandler.sendMessageDelayed(obtainMessage, RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS);
    }

    private void sendUnsetMsg(String str) {
        String str2 = LOG_TAG;
        GosLog.v(str2, "sendUnsetMsg(). " + str);
        this.mHandler.removeMessages(10);
        Message obtainMessage = this.mHandler.obtainMessage(11);
        obtainMessage.obj = str;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void onFocusOut(PkgData pkgData) {
        sendUnsetMsg(pkgData.getPackageName());
    }

    public void restoreDefault(PkgData pkgData) {
        if (pkgData == null) {
            GosLog.e(LOG_TAG, "pkgData is null.");
        } else {
            sendUnsetMsg(pkgData.getPackageName());
        }
    }
}
