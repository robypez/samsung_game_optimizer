package com.samsung.android.game.gos.ipm;

public enum LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    CRITICAL;
    
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public static LogLevel swigToEnum(int i) {
        Class<LogLevel> cls = LogLevel.class;
        LogLevel[] logLevelArr = (LogLevel[]) cls.getEnumConstants();
        if (i < logLevelArr.length && i >= 0 && logLevelArr[i].swigValue == i) {
            return logLevelArr[i];
        }
        for (LogLevel logLevel : logLevelArr) {
            if (logLevel.swigValue == i) {
                return logLevel;
            }
        }
        throw new IllegalArgumentException("No enum " + cls + " with value " + i);
    }

    private LogLevel(int i) {
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private LogLevel(LogLevel logLevel) {
        int i = logLevel.swigValue;
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

    public static LogLevel fromInt(int i) {
        return swigToEnum(i);
    }

    public final int toInt() {
        return swigValue();
    }
}
