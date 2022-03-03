package com.samsung.android.game.gos.context;

import android.app.Application;

public class AppContext {
    private static Application sAppContext;

    public static Application get() {
        return sAppContext;
    }

    public static void initialize(Application application) {
        if (sAppContext == null) {
            sAppContext = application;
        }
    }
}
