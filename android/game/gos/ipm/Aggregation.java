package com.samsung.android.game.gos.ipm;

public enum Aggregation {
    MEAN,
    MEDIAN,
    MODE,
    MIN,
    MAX,
    SUM;
    
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public static Aggregation swigToEnum(int i) {
        Class<Aggregation> cls = Aggregation.class;
        Aggregation[] aggregationArr = (Aggregation[]) cls.getEnumConstants();
        if (i < aggregationArr.length && i >= 0 && aggregationArr[i].swigValue == i) {
            return aggregationArr[i];
        }
        for (Aggregation aggregation : aggregationArr) {
            if (aggregation.swigValue == i) {
                return aggregation;
            }
        }
        throw new IllegalArgumentException("No enum " + cls + " with value " + i);
    }

    private Aggregation(int i) {
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private Aggregation(Aggregation aggregation) {
        int i = aggregation.swigValue;
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

    public static Aggregation fromInt(int i) {
        return swigToEnum(i);
    }

    public final int toInt() {
        return swigValue();
    }
}
