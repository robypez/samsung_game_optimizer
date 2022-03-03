package com.samsung.android.game.gos.selibrary;

import android.media.AudioManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.value.AppVariable;

public class SeAudioManager {
    private AudioManager am;

    private SeAudioManager() {
        this.am = (AudioManager) AppContext.get().getSystemService(AudioManager.class);
    }

    public static SeAudioManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setVolumeLimitEnabled(int i, boolean z) throws RuntimeException {
        if (this.am != null && !AppVariable.isUnitTest()) {
            this.am.semSetVolumeLimitEnabled(i, z);
        }
    }

    public int getVolume() {
        AudioManager audioManager = this.am;
        if (audioManager == null) {
            return -1;
        }
        return Math.round((((float) this.am.getStreamVolume(3)) * 100.0f) / ((float) audioManager.getStreamMaxVolume(3)));
    }

    public int getCurrentDeviceType() {
        if (this.am == null || AppVariable.isUnitTest()) {
            return 2;
        }
        return this.am.semGetCurrentDeviceType();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeAudioManager INSTANCE = new SeAudioManager();

        private SingletonHolder() {
        }
    }
}
