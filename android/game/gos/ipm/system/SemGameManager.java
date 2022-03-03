package com.samsung.android.game.gos.ipm.system;

import com.samsung.android.game.gos.ipm.common.Reflect;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SemGameManager {
    private static final Constructor<?> sConstructor;
    private static final Method sRequestWithJson;
    private final Object mSemGameManager = Reflect.newInstance(sConstructor, new Object[0]);

    static {
        Class<?> loadClass = Reflect.loadClass("com.samsung.android.game.SemGameManager");
        sConstructor = Reflect.getConstructor(loadClass, new Class[0]);
        sRequestWithJson = Reflect.getMethod(loadClass, "requestWithJson", String.class, String.class);
    }

    public String requestWithJson(String str, String str2) {
        return (String) Reflect.invoke(sRequestWithJson, this.mSemGameManager, str, str2);
    }
}
