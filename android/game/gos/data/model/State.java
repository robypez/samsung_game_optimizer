package com.samsung.android.game.gos.data.model;

public final class State {
    public static final String DEFAULT_V4_STATE = "inherited";
    public static final String DISABLED = "disabled";
    public static final String ENABLED = "enabled";
    public static final String FORCIBLY_DISABLED = "forcibly_disabled";
    public static final String FORCIBLY_ENABLED = "forcibly_enabled";
    public static final String INHERITED = "inherited";

    public static boolean isValidV4State(String str) {
        return FORCIBLY_ENABLED.equals(str) || FORCIBLY_DISABLED.equals(str) || "enabled".equals(str) || DISABLED.equals(str) || "inherited".equals(str);
    }
}
