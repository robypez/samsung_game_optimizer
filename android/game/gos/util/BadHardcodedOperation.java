package com.samsung.android.game.gos.util;

import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.value.Constants;

public class BadHardcodedOperation {
    public static boolean needsToBlockGfiSettingByGameBooster(String str) {
        return Constants.PACKAGE_NAME_GAME_TOOLS.equals(str) && PackageUtil.isPackageEnabled(AppContext.get(), Constants.PACKAGE_NAME_GAME_PLUGINS) && PackageUtil.isPackageEnabled(AppContext.get(), "com.samsung.android.game.gameboosterplus") && PackageUtil.getPackageVersionCode(AppContext.get(), Constants.PACKAGE_NAME_GAME_PLUGINS) >= 310000000 && PackageUtil.getPackageVersionCode(AppContext.get(), "com.samsung.android.game.gameboosterplus") >= 220000000;
    }
}
