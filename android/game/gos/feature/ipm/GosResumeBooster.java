package com.samsung.android.game.gos.feature.ipm;

import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostFeature;
import com.samsung.android.game.gos.ipm.ResumeBooster;
import java.util.concurrent.TimeUnit;

public class GosResumeBooster implements ResumeBooster {
    private final ResumeBoostFeature mResumeBoostFeature;

    public GosResumeBooster(ResumeBoostFeature resumeBoostFeature) {
        this.mResumeBoostFeature = resumeBoostFeature;
    }

    public boolean isMinLockBoosterWorking() {
        return this.mResumeBoostFeature.isMinLockBoosterWorking();
    }

    public boolean isBigTurboWorking() {
        return this.mResumeBoostFeature.isBigTurboWorking();
    }

    public long getMinLockBoosterDuration(TimeUnit timeUnit) {
        return timeUnit.convert((long) this.mResumeBoostFeature.getCurrentDurationSec(), TimeUnit.SECONDS);
    }

    public long getBigTurboDuration(TimeUnit timeUnit) {
        return timeUnit.convert((long) this.mResumeBoostFeature.getCurrentBigTurboDurationSec(), TimeUnit.SECONDS);
    }
}
