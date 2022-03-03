package com.samsung.android.game.gos.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
    private static final String LOG_TAG = "ReflectUtils";

    public static Class classForName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException | LinkageError e) {
            GosLog.e(LOG_TAG, "Cannot load class: " + str + " : " + e.getMessage());
            return null;
        }
    }

    public static Constructor getConstructor(Class cls, Class... clsArr) {
        if (cls == null) {
            return null;
        }
        try {
            return cls.getConstructor(clsArr);
        } catch (NoSuchMethodException e) {
            GosLog.e(LOG_TAG, "Cannot load constructor : " + e.getMessage());
            return null;
        }
    }

    public static Method getMethod(Class cls, String str, Class... clsArr) {
        if (cls == null) {
            return null;
        }
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException | NullPointerException e) {
            GosLog.e(LOG_TAG, "Cannot load method: " + e.getMessage());
            return null;
        }
    }

    public static Field getField(Class cls, String str) {
        if (cls == null) {
            return null;
        }
        try {
            return cls.getField(str);
        } catch (NoSuchFieldException | NullPointerException | SecurityException e) {
            GosLog.e(LOG_TAG, "Cannot load field: " + e.getMessage());
            return null;
        }
    }
}
