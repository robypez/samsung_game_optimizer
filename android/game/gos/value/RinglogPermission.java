package com.samsung.android.game.gos.value;

public class RinglogPermission {

    public enum PERM_POLICY {
        SIGNATURE,
        SERVER,
        LOCAL_LIST
    }

    public enum PERM_TYPES {
        ALLOW_ALL,
        ALLOW_SOME,
        DENY_SOME,
        DENY_ALL
    }
}
