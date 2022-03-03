package com.samsung.android.game.gos.feature.vrr;

public class VrrConstant {
    static final int[] CANDIDATE_VRR_LIST = {120, 96, 90, 60, 48};
    static final int[] CANDIDATE_VRR_LIST_SWITCHABLE_HIGH = {120, 96, 90, 60};
    static final int[] CANDIDATE_VRR_LIST_SWITCHABLE_NORMAL = {60, 48};
    static final int HFR_MODE_SEAMLESS = 2;
    static final int HFR_MODE_SEAMLESS_PLUS = 3;
    static final int HFR_MODE_SWITCHABLE = 1;
    static final int HFR_MODE_UNSUPPORTED = 0;
    static final String PREF_AVAILABLE_REFRESH_RATE = "PREF_AVAILABLE_REFRESH_RATE";
    static final int VRR_SEAMLESS_HIGH = 1;
    static final int VRR_SEAMLESS_NORMAL = 0;
    static final int VRR_SWITCHABLE_ADAPTIVE = 1;
    public static final int VRR_SWITCHABLE_ALWAYS = 2;
    public static final int VRR_SWITCHABLE_NORMAL = 0;
}
