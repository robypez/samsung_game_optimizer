package com.samsung.android.game.gos.feature.gfi.value;

import android.os.Parcel;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.util.GosLog;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class GfiWatchdogWarningHandler {
    private static final String LOG_TAG = "GfiWatchdogWarningHandler";
    private static final int WARN_FPS_TOO_HIGH = 4;
    private static final int WARN_FPS_TOO_LOW = 0;
    private static final int WARN_INVALID = -1;
    private static final int WARN_STABILITY_TOO_LOW = 1;
    private static final int WARN_TOO_MANY_CLIENT_COMPOSITIONS_CONSECUTIVELY = 2;
    private static final int WARN_TOO_MANY_CLIENT_COMPOSITIONS_ON_AVERAGE = 3;
    Map<Integer, WatchdogWarningQueue> mWarnings = new HashMap();

    /* access modifiers changed from: private */
    public static String codeToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "WARN_INVALID" : "WARN_FPS_TOO_HIGH" : "WARN_TOO_MANY_CLIENT_COMPOSITIONS_ON_AVERAGE" : "WARN_TOO_MANY_CLIENT_COMPOSITIONS_CONSECUTIVELY" : "WARN_STABILITY_TOO_LOW" : "WARN_FPS_TOO_LOW";
    }

    /* access modifiers changed from: private */
    public static int getExpireDuration() {
        return GfiSettings.getInstance(AppContext.get()).getWatchdogExpireDuration() * 60 * 1000;
    }

    private static abstract class WatchdogWarning {
        private int mCode;
        private long mTimestamp;

        /* access modifiers changed from: protected */
        public abstract boolean initSpecial(Parcel parcel);

        WatchdogWarning(int i, long j, Parcel parcel) {
            this.mCode = i;
            this.mTimestamp = j;
            if (!initSpecial(parcel)) {
                this.mCode = -1;
            }
        }

        WatchdogWarning() {
            this.mCode = -1;
            this.mTimestamp = 0;
        }

        public String toString() {
            return "[" + GfiWatchdogWarningHandler.codeToString(this.mCode) + " at time " + this.mTimestamp + "ms]";
        }

        public boolean isValid() {
            return this.mCode != -1;
        }

        public boolean hasExpired() {
            return System.currentTimeMillis() > this.mTimestamp + ((long) GfiWatchdogWarningHandler.getExpireDuration());
        }
    }

    private static class InvalidWarning extends WatchdogWarning {
        /* access modifiers changed from: protected */
        public boolean initSpecial(Parcel parcel) {
            return false;
        }

        private InvalidWarning() {
        }
    }

    private static class SingleIntWarning extends WatchdogWarning {
        int mExtraData;

        SingleIntWarning(int i, long j, Parcel parcel) {
            super(i, j, parcel);
        }

        /* access modifiers changed from: protected */
        public boolean initSpecial(Parcel parcel) {
            if (parcel.dataAvail() < 4) {
                return false;
            }
            this.mExtraData = parcel.readInt();
            return true;
        }
    }

    private static class SingleFloatWarning extends WatchdogWarning {
        float mExtraData;

        SingleFloatWarning(int i, long j, Parcel parcel) {
            super(i, j, parcel);
        }

        /* access modifiers changed from: protected */
        public boolean initSpecial(Parcel parcel) {
            if (parcel.dataAvail() < 4) {
                return false;
            }
            this.mExtraData = parcel.readFloat();
            return true;
        }
    }

    private static class WatchdogWarningQueue {
        private static final int MAX_QUEUE_SIZE = 10;
        private ArrayDeque<WatchdogWarning> mWarnings = new ArrayDeque<>();

        public boolean isEmpty() {
            return this.mWarnings.isEmpty();
        }

        private void removeExpired() {
            while (!this.mWarnings.isEmpty()) {
                if (this.mWarnings.getFirst().hasExpired() || this.mWarnings.size() > 10) {
                    this.mWarnings.removeFirst();
                } else {
                    return;
                }
            }
        }

        public int numWarnings() {
            removeExpired();
            return this.mWarnings.size();
        }

        public void readWarningsFromParcel(Parcel parcel) {
            removeExpired();
            while (parcel.dataAvail() > 0) {
                int dataPosition = parcel.dataPosition();
                int readInt = parcel.readInt();
                WatchdogWarning makeWarning = makeWarning(parcel);
                if (makeWarning.isValid()) {
                    GosLog.d(GfiWatchdogWarningHandler.LOG_TAG, "Detected warning: " + makeWarning);
                    this.mWarnings.addLast(makeWarning);
                }
                parcel.setDataPosition(dataPosition + readInt);
            }
            removeExpired();
        }

        private WatchdogWarning makeWarning(Parcel parcel) {
            int readInt = parcel.readInt();
            long readLong = parcel.readLong();
            if (readInt != 0) {
                if (readInt != 1) {
                    if (readInt != 2) {
                        if (readInt != 3) {
                            if (readInt != 4) {
                                return new InvalidWarning();
                            }
                        }
                    }
                }
                return new SingleFloatWarning(readInt, readLong, parcel);
            }
            return new SingleIntWarning(readInt, readLong, parcel);
        }
    }

    public void readWarningsFromParcel(int i, Parcel parcel) {
        if (parcel != null && parcel.dataAvail() > 0) {
            WatchdogWarningQueue watchdogWarningQueue = this.mWarnings.get(Integer.valueOf(i));
            if (watchdogWarningQueue == null) {
                watchdogWarningQueue = new WatchdogWarningQueue();
                this.mWarnings.put(Integer.valueOf(i), watchdogWarningQueue);
            }
            watchdogWarningQueue.readWarningsFromParcel(parcel);
            if (watchdogWarningQueue.isEmpty()) {
                this.mWarnings.remove(Integer.valueOf(i));
            }
        }
    }

    public boolean shouldEnableFB(int i) {
        int numWarnings;
        WatchdogWarningQueue watchdogWarningQueue = this.mWarnings.get(Integer.valueOf(i));
        if (watchdogWarningQueue == null || (numWarnings = watchdogWarningQueue.numWarnings()) == 0) {
            return true;
        }
        GosLog.d(LOG_TAG, "FB should not be enabled for UID " + i + ": still " + numWarnings + " watchdog warnings in effect");
        return false;
    }
}
