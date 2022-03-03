package com.samsung.android.game.gos.feature.ipm;

import com.samsung.android.game.gos.ipm.AndroidSystem;
import com.samsung.android.game.gos.util.PlatformUtil;

public class GosAndroidSystem implements AndroidSystem {
    public boolean isDebugBinary() {
        return PlatformUtil.isDebugBinary();
    }
}
