package com.samsung.android.game.gos.feature.dfs;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.ipm.IpmFeature;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FpsController {
    private static final String LOG_TAG = "FpsController";
    private static final int MSG_REQUEST_AS_FIXED = 2;
    private static final int MSG_REQUEST_AS_SCALE = 1;
    private static final int MSG_RESET = 0;
    public static final String TYPE_FIXED = "fixed";
    public static final String TYPE_SCALE = "scale";
    private Map<String, Integer> fpsMap;
    private Handler mHandler;
    private int maxFps;

    public static FpsController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final FpsController INSTANCE = new FpsController();

        private SingletonHolder() {
        }
    }

    private FpsController() {
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    FpsController.this._resetFps();
                } else if (i == 1) {
                    FpsController.this._requestFpsScaleValue(message.arg1, (String) message.obj);
                } else if (i == 2) {
                    FpsController.this._requestFpsFixedValue(message.arg1, (String) message.obj);
                }
            }
        };
        this.maxFps = 120;
        this.fpsMap = new HashMap();
        updateMaxFps();
    }

    public void resetFps() {
        this.mHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: private */
    public void _resetFps() {
        this.fpsMap.clear();
        SeGameManager.getInstance().setTargetFrameRate(this.maxFps);
        IpmFeature.getInstance(AppContext.get()).setDynamicRefreshRate(this.maxFps);
    }

    public void requestFpsScaleValue(int i, String str) {
        GosLog.i(LOG_TAG, String.format(Locale.US, "requestFpsScaleValue(%d, %s)", new Object[]{Integer.valueOf(i), str}));
        Message obtainMessage = this.mHandler.obtainMessage(1);
        obtainMessage.obj = str;
        obtainMessage.arg1 = i;
        this.mHandler.sendMessage(obtainMessage);
    }

    /* access modifiers changed from: private */
    public void _requestFpsScaleValue(int i, String str) {
        if (i < 0) {
            i = 0;
        } else if (i > 100) {
            i = 100;
        }
        this.fpsMap.put(str, Integer.valueOf((int) ((((((float) this.maxFps) - 1.0f) * ((float) i)) / 100.0f) + 1.0f)));
        setMinMapFps();
    }

    public void requestFpsFixedValue(int i, String str) {
        GosLog.i(LOG_TAG, String.format(Locale.US, "requestFpsFixedValue(%d, %s)", new Object[]{Integer.valueOf(i), str}));
        Message obtainMessage = this.mHandler.obtainMessage(2);
        obtainMessage.obj = str;
        obtainMessage.arg1 = i;
        this.mHandler.sendMessage(obtainMessage);
    }

    /* access modifiers changed from: private */
    public void _requestFpsFixedValue(int i, String str) {
        if (((float) i) < 1.0f) {
            i = 1;
        } else {
            int i2 = this.maxFps;
            if (i > i2) {
                i = i2;
            }
        }
        this.fpsMap.put(str, Integer.valueOf(i));
        setMinMapFps();
    }

    private int setMinMapFps() {
        float f = (float) this.maxFps;
        for (Integer intValue : this.fpsMap.values()) {
            float intValue2 = (float) intValue.intValue();
            if (intValue2 < f) {
                f = intValue2;
            }
        }
        int i = (int) f;
        GosLog.i(LOG_TAG, String.format(Locale.US, "setMinMapFps(): fps: %d", new Object[]{Integer.valueOf(i)}));
        boolean targetFrameRate = SeGameManager.getInstance().setTargetFrameRate(i);
        IpmFeature.getInstance(AppContext.get()).setDynamicRefreshRate(targetFrameRate ? i : this.maxFps);
        if (targetFrameRate) {
            return i;
        }
        return 0;
    }

    public void updateMaxFps() {
        WindowManager windowManager = (WindowManager) AppContext.get().getSystemService("window");
        if (windowManager == null) {
            GosLog.e(LOG_TAG, "updateMaxFps failed, windowManager is null");
            return;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        if (defaultDisplay != null) {
            this.maxFps = Math.round(((Float) Arrays.stream(defaultDisplay.getSupportedModes()).max(Comparator.comparingDouble($$Lambda$FpsController$PBiIaPJoQz6nN5KQ8sr6cvc93iQ.INSTANCE)).map($$Lambda$FpsController$N7Z3sSdxICRZWzYsdEvtWuPbQo.INSTANCE).orElse(Float.valueOf(120.0f))).floatValue());
        }
        GosLog.i(LOG_TAG, "updateMaxFps maxFps=" + this.maxFps);
    }

    public int getMaxFps() {
        return this.maxFps;
    }
}
