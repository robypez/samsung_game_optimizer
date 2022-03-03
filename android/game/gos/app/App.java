package com.samsung.android.game.gos.app;

import android.app.Application;
import android.os.Build;
import android.os.Process;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.SecureSettingHelper;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.util.GosLog;

public class App extends Application {
    private static final String LOG_TAG = "App";

    public void onCreate() {
        super.onCreate();
        if (!Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            GosLog.e(LOG_TAG, "The manufacturer is not matched.");
            Process.killProcess(Process.myPid());
        }
        AppContext.initialize(this);
        int myUserId = SeUserHandleManager.getInstance().getMyUserId();
        if (myUserId != 0) {
            GosLog.i(LOG_TAG, "onCreate(), kill process. userId=" + myUserId);
            Process.killProcess(Process.myPid());
        }
        initDeviceInfo();
        SecureSettingHelper.getInstance();
        try {
            Initializer.initGosAsync(this);
        } catch (Throwable th) {
            GosLog.e(LOG_TAG, "onCreate(), kill process. exception", th);
            Process.killProcess(Process.myPid());
        }
    }

    public void onTrimMemory(int i) {
        GosLog.i(LOG_TAG, "onTrimMemory " + i);
        super.onTrimMemory(i);
    }

    public void onLowMemory() {
        GosLog.i(LOG_TAG, "onLowMemory");
        super.onLowMemory();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006d, code lost:
        if (r5 != null) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007c, code lost:
        if (r5 != null) goto L_0x0085;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initDeviceInfo() {
        /*
            r14 = this;
            android.content.Context r0 = r14.createDeviceProtectedStorageContext()
            com.samsung.android.game.gos.data.PreferenceHelper r1 = new com.samsung.android.game.gos.data.PreferenceHelper
            r1.<init>(r0)
            java.lang.String r0 = "DEVICE_NAME"
            r2 = 0
            java.lang.String r3 = r1.getValue((java.lang.String) r0, (java.lang.String) r2)
            java.lang.String r4 = "MODEL_NAME"
            java.lang.String r5 = r1.getValue((java.lang.String) r4, (java.lang.String) r2)
            if (r3 == 0) goto L_0x001a
            if (r5 != 0) goto L_0x00bd
        L_0x001a:
            r6 = 4
            java.lang.String r7 = "ro.product.vendor"
            java.lang.String r8 = "ro.product.system"
            java.lang.String r9 = "ro.product.product"
            java.lang.String r10 = "ro.product.odm"
            java.lang.String[] r7 = new java.lang.String[]{r7, r8, r9, r10}
            r8 = 0
        L_0x0028:
            java.lang.String r9 = "ro.product (Build class)"
            java.lang.String r10 = "App"
            if (r8 >= r6) goto L_0x007a
            r11 = r7[r8]     // Catch:{ Exception -> 0x0063 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0063 }
            r12.<init>()     // Catch:{ Exception -> 0x0063 }
            r12.append(r11)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r13 = ".device"
            r12.append(r13)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x0063 }
            java.lang.String r3 = com.samsung.android.game.gos.selibrary.SeSysProp.getProp(r12)     // Catch:{ Exception -> 0x0063 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0063 }
            r12.<init>()     // Catch:{ Exception -> 0x0063 }
            r12.append(r11)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r13 = ".model"
            r12.append(r13)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x0063 }
            java.lang.String r5 = com.samsung.android.game.gos.selibrary.SeSysProp.getProp(r12)     // Catch:{ Exception -> 0x0063 }
            if (r3 == 0) goto L_0x0060
            if (r5 == 0) goto L_0x0060
            r2 = r11
            goto L_0x007a
        L_0x0060:
            int r8 = r8 + 1
            goto L_0x0028
        L_0x0063:
            r6 = move-exception
            java.lang.String r6 = r6.getMessage()     // Catch:{ all -> 0x0070 }
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r10, (java.lang.String) r6)     // Catch:{ all -> 0x0070 }
            if (r3 == 0) goto L_0x007e
            if (r5 != 0) goto L_0x0085
            goto L_0x007e
        L_0x0070:
            r0 = move-exception
            if (r3 == 0) goto L_0x0075
            if (r5 != 0) goto L_0x0079
        L_0x0075:
            java.lang.String r1 = android.os.Build.DEVICE
            java.lang.String r1 = android.os.Build.MODEL
        L_0x0079:
            throw r0
        L_0x007a:
            if (r3 == 0) goto L_0x007e
            if (r5 != 0) goto L_0x0085
        L_0x007e:
            java.lang.String r2 = android.os.Build.DEVICE
            java.lang.String r3 = android.os.Build.MODEL
            r5 = r3
            r3 = r2
            r2 = r9
        L_0x0085:
            r1.put((java.lang.String) r0, (java.lang.String) r3)     // Catch:{ IllegalStateException -> 0x0091 }
            r1.put((java.lang.String) r4, (java.lang.String) r5)     // Catch:{ IllegalStateException -> 0x0091 }
            java.lang.String r0 = "DEVICE_INFO_SRC_PROP"
            r1.put((java.lang.String) r0, (java.lang.String) r2)     // Catch:{ IllegalStateException -> 0x0091 }
            goto L_0x0099
        L_0x0091:
            r0 = move-exception
            java.lang.String r0 = r0.getMessage()
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r10, (java.lang.String) r0)
        L_0x0099:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "initDeviceInfo(), deviceName: "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r1 = ", modelName: "
            r0.append(r1)
            r0.append(r5)
            java.lang.String r1 = ", srcProp: "
            r0.append(r1)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r10, r0)
        L_0x00bd:
            com.samsung.android.game.gos.value.AppVariable.initDeviceInfo(r3, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.app.App.initDeviceInfo():void");
    }
}
