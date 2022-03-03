package com.samsung.android.game.gos.ipm;

import java.io.InputStream;

public interface Ssrm {

    public interface Listener {
        void onActivateChanged(boolean z);

        void onPauseActionsChanged(boolean z);
    }

    void activate();

    void deactivate();

    void deregister(Listener listener);

    InputStream getConfigFile();

    int getPst();

    String getVersion();

    boolean isInitialized();

    void register(Listener listener);
}
