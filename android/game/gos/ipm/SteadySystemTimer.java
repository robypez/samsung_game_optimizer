package com.samsung.android.game.gos.ipm;

import java.util.concurrent.TimeUnit;

public class SteadySystemTimer {
    public long now(TimeUnit timeUnit) {
        return timeUnit.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
    }
}
