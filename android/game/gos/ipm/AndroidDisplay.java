package com.samsung.android.game.gos.ipm;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.Iterator;

public class AndroidDisplay {
    private static final float DFS_MAX_VALUE = 60.0f;
    private static final String LOG_TAG = AndroidDisplay.class.getSimpleName();
    /* access modifiers changed from: private */
    public final Display mDisplay;
    private float mDisplayDynamicRefreshRate = Float.MAX_VALUE;
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public void onDisplayChanged(int i) {
            if (i == AndroidDisplay.this.mDisplay.getDisplayId()) {
                AndroidDisplay androidDisplay = AndroidDisplay.this;
                float unused = androidDisplay.mDisplayRefreshRate = androidDisplay.mDisplay.getRefreshRate();
                AndroidDisplay.this.updateRefreshRate();
            }
        }
    };
    private final DisplayManager mDisplayManager;
    /* access modifiers changed from: private */
    public float mDisplayRefreshRate;
    private float mDynamicRefreshRate = Float.MAX_VALUE;
    private final ArrayList<Listener> mListeners = new ArrayList<>();
    private final Looper mLooper;
    private float mRefreshRate;

    public interface Listener {
        void onRefreshRateChanged(float f, float f2);
    }

    public AndroidDisplay(Context context, Looper looper) {
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.mDisplay = defaultDisplay;
        this.mDisplayRefreshRate = defaultDisplay.getRefreshRate();
        this.mLooper = looper;
    }

    public void activate() {
        this.mDisplayManager.registerDisplayListener(this.mDisplayListener, new Handler(this.mLooper));
    }

    public void deactivate() {
        try {
            this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (Exception unused) {
        }
    }

    public void register(Listener listener) {
        this.mListeners.add(listener);
    }

    public void deregister(Listener listener) {
        this.mListeners.remove(listener);
    }

    public float getRefreshRate() {
        return this.mDisplay.getRefreshRate();
    }

    public void setDynamicRefreshRate(float f) {
        if (f >= DFS_MAX_VALUE) {
            f = Float.MAX_VALUE;
        }
        this.mDisplayDynamicRefreshRate = f;
        updateRefreshRate();
    }

    /* access modifiers changed from: private */
    public void updateRefreshRate() {
        if (this.mRefreshRate != this.mDisplayRefreshRate || this.mDynamicRefreshRate != this.mDisplayDynamicRefreshRate) {
            float f = this.mDisplayRefreshRate;
            this.mRefreshRate = f;
            float f2 = this.mDisplayDynamicRefreshRate;
            this.mDynamicRefreshRate = f2;
            float min = Math.min(f, f2);
            Iterator<Listener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                it.next().onRefreshRateChanged(this.mRefreshRate, min);
            }
        }
    }
}
