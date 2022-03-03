package com.samsung.android.game.gos.feature.gfi.value;

public class GfiPolicyException extends Exception {
    public GfiPolicyException() {
    }

    public GfiPolicyException(String str) {
        super(str);
    }

    public GfiPolicyException(String str, Throwable th) {
        super(str, th);
    }
}
