package com.samsung.android.game.gos.ipm;

public enum ThreadMlMode {
    DISABLED((String) 0),
    ENABLED((String) 1),
    ENABLED_WITH_LOGGING((String) 2);
    
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public static ThreadMlMode swigToEnum(int i) {
        Class<ThreadMlMode> cls = ThreadMlMode.class;
        ThreadMlMode[] threadMlModeArr = (ThreadMlMode[]) cls.getEnumConstants();
        if (i < threadMlModeArr.length && i >= 0 && threadMlModeArr[i].swigValue == i) {
            return threadMlModeArr[i];
        }
        for (ThreadMlMode threadMlMode : threadMlModeArr) {
            if (threadMlMode.swigValue == i) {
                return threadMlMode;
            }
        }
        throw new IllegalArgumentException("No enum " + cls + " with value " + i);
    }

    private ThreadMlMode(int i) {
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private ThreadMlMode(ThreadMlMode threadMlMode) {
        int i = threadMlMode.swigValue;
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

    public static ThreadMlMode fromInt(int i) {
        return swigToEnum(i);
    }

    public final int toInt() {
        return swigValue();
    }
}
