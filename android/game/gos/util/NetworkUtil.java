package com.samsung.android.game.gos.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.samsung.android.game.gos.context.AppContext;

public class NetworkUtil {
    private static final String LOG_TAG = "NetworkUtil";

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        r2 = r2.getConnectionInfo();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isWifiConnected(android.content.Context r2) {
        /*
            java.lang.String r0 = "wifi"
            java.lang.Object r2 = r2.getSystemService(r0)
            android.net.wifi.WifiManager r2 = (android.net.wifi.WifiManager) r2
            if (r2 == 0) goto L_0x001f
            boolean r0 = r2.isWifiEnabled()
            if (r0 == 0) goto L_0x001f
            android.net.wifi.WifiInfo r2 = r2.getConnectionInfo()
            if (r2 == 0) goto L_0x001f
            int r2 = r2.getNetworkId()
            r0 = -1
            if (r2 == r0) goto L_0x001f
            r2 = 1
            goto L_0x0020
        L_0x001f:
            r2 = 0
        L_0x0020:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "isWifiConnected(), Wi-Fi is "
            r0.append(r1)
            if (r2 == 0) goto L_0x002f
            java.lang.String r1 = "connected"
            goto L_0x0031
        L_0x002f:
            java.lang.String r1 = "NOT connected"
        L_0x0031:
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "NetworkUtil"
            com.samsung.android.game.gos.util.GosLog.d(r1, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.util.NetworkUtil.isWifiConnected(android.content.Context):boolean");
    }

    public static boolean isNetworkConnected() {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.get().getSystemService("connectivity");
        boolean z = false;
        if (!(connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnected())) {
            z = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("isNetworkConnected(), Network is ");
        sb.append(z ? "connected" : "NOT connected");
        GosLog.d(LOG_TAG, sb.toString());
        return z;
    }
}
