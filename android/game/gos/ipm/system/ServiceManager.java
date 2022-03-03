package com.samsung.android.game.gos.ipm.system;

import android.os.IBinder;
import com.samsung.android.game.gos.ipm.common.Reflect;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ServiceManager {
    private static final Method sGetService = Reflect.getMethod(Reflect.loadClass("android.os.ServiceManager"), "getService", String.class);

    private ServiceManager() {
    }

    public static IBinder getService(String str) {
        try {
            return (IBinder) sGetService.invoke((Object) null, new Object[]{str});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
