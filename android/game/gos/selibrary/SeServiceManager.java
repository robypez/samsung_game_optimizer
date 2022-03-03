package com.samsung.android.game.gos.selibrary;

import android.os.IBinder;
import com.samsung.android.game.gos.util.GosLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SeServiceManager {
    private static final String LOG_TAG = "SeServiceManager";
    private Method getServiceMethod;
    private Class serviceManagerClass;

    private SeServiceManager() {
        this.getServiceMethod = null;
        this.serviceManagerClass = null;
        try {
            Class<?> cls = Class.forName("android.os.ServiceManager");
            this.serviceManagerClass = cls;
            this.getServiceMethod = cls.getMethod("getService", new Class[]{String.class});
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            GosLog.w(LOG_TAG, "SeServiceManager(): ", e);
        }
    }

    public static SeServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeServiceManager INSTANCE = new SeServiceManager();

        private SingletonHolder() {
        }
    }

    public IBinder getService(String str) {
        try {
            return (IBinder) this.getServiceMethod.invoke(this.serviceManagerClass, new Object[]{str});
        } catch (IllegalAccessException | InvocationTargetException e) {
            GosLog.w(LOG_TAG, "getService(): ", e);
            return null;
        }
    }
}
