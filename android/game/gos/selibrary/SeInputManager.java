package com.samsung.android.game.gos.selibrary;

import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SeInputManager {
    private static final String LOG_TAG = SeInputManager.class.getSimpleName();
    private Method checkInputFeatureMethod;
    private Method getInstanceMethod;
    private Class inputManagerClass;

    public static SeInputManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Integer checkInputFeature() {
        Method method;
        Object invoke;
        Class cls = this.inputManagerClass;
        Integer num = null;
        if (cls == null || (method = this.getInstanceMethod) == null || this.checkInputFeatureMethod == null) {
            GosLog.w(LOG_TAG, "checkInputFeature(). null check fail");
            return null;
        }
        try {
            Object invoke2 = method.invoke(cls, new Object[0]);
            if (!(invoke2 == null || (invoke = this.checkInputFeatureMethod.invoke(invoke2, new Object[0])) == null)) {
                num = (Integer) invoke;
            }
            String str = LOG_TAG;
            GosLog.i(str, "checkInputFeature(), ret: " + num);
        } catch (IllegalAccessException unused) {
            GosLog.e(LOG_TAG, "failed to invoke checkInputFeatureMethod. IllegalAccessException");
        } catch (ClassCastException | InvocationTargetException unused2) {
            GosLog.e(LOG_TAG, "failed to invoke checkInputFeatureMethod. InvocationTargetException or ClassCastException");
        }
        return num;
    }

    private SeInputManager() {
        try {
            Class<?> cls = Class.forName("android.hardware.input.InputManager");
            this.inputManagerClass = cls;
            this.getInstanceMethod = cls.getMethod("getInstance", new Class[0]);
            if (!AppVariable.isUnitTest()) {
                this.checkInputFeatureMethod = this.inputManagerClass.getMethod("semCheckInputFeature", new Class[0]);
            }
            GosLog.d(LOG_TAG, "succeeded to get checkInputFeature");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            GosLog.w(LOG_TAG, "failed to get checkInputFeature", e);
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeInputManager INSTANCE = new SeInputManager();

        private SingletonHolder() {
        }
    }
}
