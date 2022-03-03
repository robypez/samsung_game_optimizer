package com.samsung.android.game.gos.ipm;

import java.util.concurrent.TimeUnit;

public interface ResumeBooster {
    long getBigTurboDuration(TimeUnit timeUnit);

    long getMinLockBoosterDuration(TimeUnit timeUnit);

    boolean isBigTurboWorking();

    boolean isMinLockBoosterWorking();
}
