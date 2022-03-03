package com.samsung.android.game.gos.selibrary;

import android.os.PowerManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.value.Constants;

public class SePowerManager {
    public static final int ERR_FAIL = -1;
    private static final String LOG_TAG = SePowerManager.class.getSimpleName();
    public static final int MAX_BRIGHTNESS_LEVEL = 255;
    public static final int MIN_BRIGHTNESS_LEVEL = 0;
    public static final int RESET_BRIGHTNESS_LEVEL = -1;
    private PowerManager mPowerManager;

    private SePowerManager() {
        this.mPowerManager = (PowerManager) AppContext.get().getSystemService(Constants.RingLog.Parameter.POWER);
    }

    public static SePowerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0063 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setMaxBrightnessLimit(int r11) {
        /*
            r10 = this;
            java.lang.String r0 = "setMasterBrightnessLimit"
            java.lang.String r1 = "setMaxBrightnessLimit "
            r2 = -1
            r3 = 2
            r4 = 0
            r5 = 1
            android.os.PowerManager r6 = r10.mPowerManager     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.Class r6 = r6.getClass()     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.Class[] r7 = new java.lang.Class[r3]     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r7[r4] = r8     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r7[r5] = r8     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.reflect.Method r6 = r6.getMethod(r0, r7)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r6.setAccessible(r5)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            android.os.PowerManager r7 = r10.mPowerManager     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r2)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r8[r4] = r9     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r11)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r8[r5] = r9     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r6.invoke(r7, r8)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.String r6 = LOG_TAG     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r7.<init>()     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.String r8 = "setMaxBrightnessLimit by deprecated method: "
            r7.append(r8)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            r7.append(r11)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            java.lang.String r7 = r7.toString()     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
            com.samsung.android.game.gos.util.GosLog.i(r6, r7)     // Catch:{ NoSuchMethodException -> 0x0063, IllegalAccessException -> 0x004d, InvocationTargetException -> 0x004b }
        L_0x0048:
            r4 = r5
            goto L_0x00c8
        L_0x004b:
            r11 = move-exception
            goto L_0x004e
        L_0x004d:
            r11 = move-exception
        L_0x004e:
            java.lang.String r0 = LOG_TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            r2.append(r11)
            java.lang.String r11 = r2.toString()
            com.samsung.android.game.gos.util.GosLog.e(r0, r11)
            goto L_0x00c8
        L_0x0063:
            android.os.PowerManager r6 = r10.mPowerManager     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Class r6 = r6.getClass()     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r7 = 3
            java.lang.Class[] r8 = new java.lang.Class[r7]     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r8[r4] = r9     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r8[r5] = r9     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r8[r3] = r9     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.reflect.Method r0 = r6.getMethod(r0, r8)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r0.setAccessible(r5)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            android.os.PowerManager r6 = r10.mPowerManager     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r7[r4] = r2     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r11)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r7[r5] = r2     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r7[r3] = r2     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r0.invoke(r6, r7)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.String r0 = LOG_TAG     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r2.<init>()     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.String r3 = "setMaxBrightnessLimit by new method: "
            r2.append(r3)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            r2.append(r11)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            java.lang.String r11 = r2.toString()     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            com.samsung.android.game.gos.util.GosLog.i(r0, r11)     // Catch:{ NoSuchMethodException -> 0x00b3, IllegalAccessException -> 0x00b1, InvocationTargetException -> 0x00af }
            goto L_0x0048
        L_0x00af:
            r11 = move-exception
            goto L_0x00b4
        L_0x00b1:
            r11 = move-exception
            goto L_0x00b4
        L_0x00b3:
            r11 = move-exception
        L_0x00b4:
            java.lang.String r0 = LOG_TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            r2.append(r11)
            java.lang.String r11 = r2.toString()
            com.samsung.android.game.gos.util.GosLog.e(r0, r11)
        L_0x00c8:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.selibrary.SePowerManager.setMaxBrightnessLimit(int):boolean");
    }

    public boolean resetMaxBrightnessLimit() {
        return setMaxBrightnessLimit(-1);
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SePowerManager INSTANCE = new SePowerManager();

        private SingletonHolder() {
        }
    }
}
