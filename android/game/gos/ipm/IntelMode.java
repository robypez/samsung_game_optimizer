package com.samsung.android.game.gos.ipm;

public enum IntelMode {
    QTABLE_X((String) 0),
    SMOOTH((String) 3),
    LSTM((String) 4),
    DQNN((String) 5),
    QTABLE_ICE((String) 6),
    NECTOR((String) 7),
    FBE((String) 8),
    GEN_QTABLE((String) 9),
    FBE_ICE((String) 11),
    MULTI_ML((String) 10),
    QTABLE2((String) 20),
    QTABLE_ICE_CLUSTER((String) 21);
    
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public static IntelMode swigToEnum(int i) {
        Class<IntelMode> cls = IntelMode.class;
        IntelMode[] intelModeArr = (IntelMode[]) cls.getEnumConstants();
        if (i < intelModeArr.length && i >= 0 && intelModeArr[i].swigValue == i) {
            return intelModeArr[i];
        }
        for (IntelMode intelMode : intelModeArr) {
            if (intelMode.swigValue == i) {
                return intelMode;
            }
        }
        throw new IllegalArgumentException("No enum " + cls + " with value " + i);
    }

    private IntelMode(int i) {
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private IntelMode(IntelMode intelMode) {
        int i = intelMode.swigValue;
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private static class SwigNext {
        /* access modifiers changed from: private */
        public static int next;

        private SwigNext() {
        }

        static /* synthetic */ int access$008() {
            int i = next;
            next = i + 1;
            return i;
        }
    }

    public static IntelMode fromInt(int i) {
        return swigToEnum(i);
    }

    public final int toInt() {
        return swigValue();
    }
}
