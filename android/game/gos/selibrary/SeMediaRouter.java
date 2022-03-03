package com.samsung.android.game.gos.selibrary;

import android.media.MediaRouter;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.util.GosLog;

public class SeMediaRouter {
    private static final String LOG_TAG = SeMediaRouter.class.getSimpleName();
    private static final int ROUTE_TYPE_REMOTE_DISPLAY = 4;
    private MediaRouter mMediaRouter;

    private SeMediaRouter() {
        this.mMediaRouter = (MediaRouter) AppContext.get().getSystemService("media_router");
    }

    public static SeMediaRouter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean hasRemotePresentationDisplay() {
        MediaRouter.RouteInfo selectedRoute = this.mMediaRouter.getSelectedRoute(4);
        if (selectedRoute == null) {
            GosLog.d(LOG_TAG, "routeinfo is null");
            return false;
        } else if (selectedRoute.getPresentationDisplay() != null) {
            return true;
        } else {
            return false;
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeMediaRouter INSTANCE = new SeMediaRouter();

        private SingletonHolder() {
        }
    }
}
